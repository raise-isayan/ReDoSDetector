package passive.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
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

    private final CharStream input;

    public JavaScriptAnalyze(String scriptBody) {
        this.input = CharStreams.fromString(scriptBody);
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

    public boolean analyze() {
        JavaScriptLexer lexer = new JavaScriptLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaScriptParser parser = new JavaScriptParser(tokens);

        tokens.fill();
        for (Token token : tokens.getTokens()) {
            if (token.getType() == JavaScriptLexer.RegularExpressionLiteral) {
                String regexLiteral = token.getText();

                // 値を抽出する正規表現: /pattern/flags
                Pattern pattern = Pattern.compile("^(/.*?/)([a-z]*)$");
                Matcher matcher = pattern.matcher(regexLiteral);

                if (matcher.matches()) {
                    String patternValue = stripSlash(matcher.group(1));
                    String flags = matcher.group(2);

                    RegExPattermItem item = new RegExPattermItem();
                    item.setCaptureValue(regexLiteral);
                    item.setRegExPattern(patternValue);
                    item.setRegExFlag(flags);
                    item.setStart(token.getStartIndex());
                    item.setEnd(token.getStopIndex());
                    this.regList.add(item);
                }
            }
        }

        ParseTree tree = parser.program();  // JS プログラム全体を解析
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
                        regList.add(item);
                    }
                }
            }
        }, tree);
        return true;
    }

    private final List<RegExPattermItem> regList = new ArrayList<>();

    public List<RegExPattermItem> getRegExpList() {
        return this.regList;
    }

}
