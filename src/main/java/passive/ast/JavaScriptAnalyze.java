package passive.ast;

import extension.view.base.CaptureItem;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import passive.ast.js.JavaScriptLexer;
import passive.ast.js.JavaScriptParser;
import passive.ast.js.JavaScriptParserBaseListener;
import passive.signature.RegExPattermItem;

/**
 *
 * @author isayan
 */
public class JavaScriptAnalyze {

    public enum AnalyzeOption {
        JS_COMMENTS, REGEXP
    };

    private final static Pattern COMMWNT = Pattern.compile("(//|/*)");

    public boolean existsComment() {
        Matcher m = COMMWNT.matcher(this.script);
        return m.find();
    }

    // 値を抽出する正規表現: /pattern/flags
    private final static Pattern REGEXP_LITERAL = Pattern.compile("(/.*?/)([a-z]*)");

    public boolean existsRegExpLiteral() {
        Matcher m = REGEXP_LITERAL.matcher(this.script);
        return m.find();
    }

    private EnumSet<AnalyzeOption> option = EnumSet.noneOf(AnalyzeOption.class);

    private final String script;

    public JavaScriptAnalyze(String script, EnumSet<AnalyzeOption> option) {
        this.script = script;
        this.option = option;
    }

    // 引用符で囲まれた文字列を剥がす
    private String stripQuotes(String text) {
        if ((text.startsWith("\"") && text.endsWith("\"")) || (text.startsWith("'") && text.endsWith("'"))) {
            String leteral = text.substring(1, text.length() - 1);
            return leteral.replace("\\\\/", "/").replaceAll("\\\\\\\\", "\\\\");
        }
        return text;
    }

    private String stripSlash(String text) {
        if ((text.startsWith("/") && text.endsWith("/"))) {
            String leteral = text.substring(1, text.length() - 1);
            return leteral.replaceAll("\\\\/", "/").replaceAll("\\\\\\\\", "\\\\");
        }
        return text;
    }

    public EnumSet<AnalyzeOption> getOption() {
        return this.option;
    }

    private final static Pattern EXISTS_REGEXP = Pattern.compile("\\sRegExp\\W");

    public boolean existsRegExp() {
        Matcher m = EXISTS_REGEXP.matcher(this.script);
        return m.find();
    }

    private final JavaScriptLexer lexer = new JavaScriptLexer(CharStreams.fromString(""));
    private final CommonTokenStream tokens = new CommonTokenStream(lexer);
    private final JavaScriptParser parser = new JavaScriptParser(tokens);

    public boolean analyze() {
        // 解析不要時は解析行わない
        boolean isCommentAnalyze = this.option.contains(AnalyzeOption.JS_COMMENTS) && existsComment();
        boolean isRegExpLiteralAnalyze = this.option.contains(AnalyzeOption.REGEXP) && existsRegExpLiteral();
        boolean isRegExpFuncAnalyze = this.option.contains(AnalyzeOption.REGEXP) && existsRegExp();
        if (!(isCommentAnalyze || isRegExpLiteralAnalyze || isRegExpFuncAnalyze)) {
            return false;
        }
        // インスタンスの使いまわし
        this.lexer.setInputStream(CharStreams.fromString(this.script));
        this.tokens.setTokenSource(this.lexer);
        this.parser.getInterpreter().setPredictionMode(PredictionMode.SLL);

        tokens.fill();
        for (Token token : tokens.getTokens()) {
            if (isCommentAnalyze) {
                if (token.getChannel() == Token.HIDDEN_CHANNEL) {
                    String text = token.getText();
                    if (text.startsWith("//") || text.startsWith("/*")) {
                        CaptureItem item = new CaptureItem();
                        item.setCaptureValue(text);
                        item.setStart(token.getStartIndex());
                        item.setEnd(token.getStopIndex());
                        this.commentList.add(item);
                    }
                }
            }
            if (isRegExpLiteralAnalyze) {
                if (token.getType() == JavaScriptLexer.RegularExpressionLiteral) {
                    String regexLiteral = token.getText();

                    Matcher matcher = REGEXP_LITERAL.matcher(regexLiteral);
                    if (matcher.matches()) {
                        String patternValue = stripSlash(matcher.group(1));
                        String flags = matcher.group(2);

                        RegExPattermItem item = new RegExPattermItem();
                        item.setCaptureValue(regexLiteral);
                        item.setRegExPattern(patternValue);
                        item.setRegExFlag(flags);
                        item.setStart(token.getStartIndex());
                        item.setEnd(token.getStopIndex());
                        this.regexList.add(item);
                    }
                }
            }
        }
        if (isRegExpFuncAnalyze) {
            this.parser.setTokenStream(tokens);
            final ParseTree tree = parser.program();  // JS プログラム全体を解析
            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(new JavaScriptParserBaseListener() {

                @Override
                public void enterArgumentsExpression(JavaScriptParser.ArgumentsExpressionContext ctx) {
                    String captureText = ctx.getText();
                    String regexText = ctx.singleExpression().getText();
                    if (regexText.contains("RegExp")) {
                        JavaScriptParser.ArgumentsContext argsCtx = ctx.arguments();
                        List<JavaScriptParser.ArgumentContext> arguments = argsCtx != null ? argsCtx.argument() : List.of();
                        if (arguments.size() >= 1) {
                            String patternValue = stripQuotes(arguments.get(0).getText());
                            String flags = (arguments.size() >= 2) ? stripQuotes(arguments.get(1).getText()) : "";

                            // トークン位置を取得
                            Token token = arguments.get(0).getStart();

                            RegExPattermItem item = new RegExPattermItem();
                            item.setCaptureValue(captureText);
                            item.setRegExPattern(patternValue);
                            item.setRegExFlag(flags);
                            item.setStart(token.getStartIndex());
                            item.setEnd(token.getStopIndex());
                            regexList.add(item);
                        }
                    }
                }
            }, tree);
        }
        return !this.commentList.isEmpty() || !this.regexList.isEmpty();
    }

    private final List<CaptureItem> commentList = new ArrayList<>();

    public List<CaptureItem> getCommentList() {
        return this.commentList;
    }

    private final List<RegExPattermItem> regexList = new ArrayList<>();

    public List<RegExPattermItem> getRegExpList() {
        return this.regexList;
    }

}
