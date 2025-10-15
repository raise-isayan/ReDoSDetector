package passive.ast;

import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import extension.helpers.FileUtil;
import extension.helpers.StringUtil;
import extension.view.base.CaptureItem;
import java.util.EnumSet;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import passive.ast.js.JavaScriptLexer;
import passive.ast.js.JavaScriptParser;
import passive.ast.js.JavaScriptParserBaseListener;
import passive.signature.RegExPattermItem;

/**
 *
 * @author isayan
 */
public class JavaScriptAnalyzeTest {

    private final static Logger logger = Logger.getLogger(JavaScriptAnalyzeTest.class.getName());

    public JavaScriptAnalyzeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    // 引用符で囲まれた文字列を剥がす
    private String stripQuotes(String text) {
        if ((text.startsWith("\"") && text.endsWith("\"")) || (text.startsWith("'") && text.endsWith("'"))) {
            String leteral = text.substring(1, text.length() - 1);
            return leteral.replaceAll("\\\\/", "/").replaceAll("\\\\\\\\", "\\\\");
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

    @Test
    public void testAntlr() {
        System.out.println("testAntlr");
        try {
            InputStream regexStream = JavaScriptAnalyzeTest.class.getResourceAsStream("/resources/regexliteral.js");

            CharStream input = CharStreams.fromStream(regexStream);

            JavaScriptLexer lexer = new JavaScriptLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            JavaScriptParser parser = new JavaScriptParser(tokens);

//            tokens.fill();
            for (Token token : tokens.getTokens()) {
                if (token.getType() == JavaScriptLexer.RegularExpressionLiteral) {
                    String regexLiteral = token.getText();
                    System.out.println("Capture:" + regexLiteral);

                    // 値を抽出する正規表現: /pattern/flags
                    Pattern pattern = Pattern.compile("^(/.*?/)([a-z]*)$");
                    Matcher matcher = pattern.matcher(regexLiteral);

                    if (matcher.matches()) {
                        String patternValue = stripSlash(matcher.group(1));
                        String flags = matcher.group(2);

                        System.out.println("  Literalパターン: " + patternValue);
                        System.out.println("  フラグ:   " + flags);

                        int line = token.getLine();
                        int column = token.getCharPositionInLine();
                        int offset = token.getStartIndex();
                        int end = token.getStopIndex();

                        System.out.println("位置: 行=" + line + ", 列=" + column + ", 文字オフセット=" + offset + ",End=" + end);

                    } else {
                        System.out.println("  ⚠ フォーマット不正: " + regexLiteral);
                    }
                }
            }

            ParseTree tree = parser.program();  // JS プログラム全体を解析
            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(new JavaScriptParserBaseListener() {

                @Override
                public void enterArgumentsExpression(JavaScriptParser.ArgumentsExpressionContext ctx) {
                    String ctxText = ctx.getText();
                    String regexText = ctx.singleExpression().getText();
                    System.out.println("Ctx:" + ctxText);
                    System.out.println("Capture:" + regexText);
                    if (regexText.contains("RegExp")) {
                        JavaScriptParser.ArgumentsContext argsCtx = ctx.arguments();
                        List<JavaScriptParser.ArgumentContext> arguments = argsCtx != null
                                ? argsCtx.argument()
                                : List.of();

                        if (arguments.size() >= 1) {
                            String patternArg = stripQuotes(arguments.get(0).getText());
                            String flags = (arguments.size() >= 2) ? stripQuotes(arguments.get(1).getText()) : "";

                            // トークン位置を取得
                            Token patternToken = arguments.get(0).getStart();

                            int line = patternToken.getLine();
                            int column = patternToken.getCharPositionInLine();
                            int offset = patternToken.getStartIndex();
                            int end = patternToken.getStopIndex();

                            System.out.println("RegExp パターン: " + patternArg);
                            System.out.println("フラグ:           " + flags);
                            System.out.println("位置: 行=" + line + ", 列=" + column + ", 文字オフセット=" + offset + ",End=" + end);
                            System.out.println();
                        }
                    }
                }
            }, tree);

        } catch (IOException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }

    }

    @Test
    public void testJavaScriptAnalyze() {
        System.out.println("testJavaScriptAnalyze");
        try {
            InputStream regexStream = JavaScriptAnalyzeTest.class.getResourceAsStream("/resources/regexliteral.js");
            String input = StringUtil.getStringCharset(FileUtil.readAllBytes(regexStream), StandardCharsets.ISO_8859_1);
            JavaScriptAnalyze jsAnalyze = new JavaScriptAnalyze(input, EnumSet.allOf(JavaScriptAnalyze.AnalyzeOption.class));
            jsAnalyze.analyze();
            System.out.println("regexList");
            List<RegExPattermItem> regexpList = jsAnalyze.getRegExpList();
            for (RegExPattermItem item : regexpList) {
                System.out.println("capture:" + item.getCaptureValue());
                System.out.println("regex:" + item.getRegExPattern());
                System.out.println("flag:" + item.getRegExFlag());
                int start = item.start();
                int end = item.end();
                System.out.println("start:" + start + String.format("(%x)", start));
                System.out.println("end:" + end + String.format("(%x)", end));
            }
            System.out.println("commentList");
            List<CaptureItem> commentList = jsAnalyze.getCommentList();
            for (CaptureItem item : commentList) {
                System.out.println("capture:" + item.getCaptureValue());
                System.out.println("capture-UTF-8:" + StringUtil.getStringCharset(StringUtil.getBytesRaw(item.getCaptureValue()), StandardCharsets.UTF_8));
                int start = item.start();
                int end = item.end();
                System.out.println("start:" + start + String.format("(%x)", start));
                System.out.println("end:" + end + String.format("(%x)", end));
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @Test
    public void testHtmlAnalyze() {
        System.out.println("testHtmlAnalyze");
        try {
            InputStream regexStream = JavaScriptAnalyzeTest.class.getResourceAsStream("/resources/script.html");
            String input = StringUtil.getStringCharset(FileUtil.readAllBytes(regexStream), StandardCharsets.ISO_8859_1);
            HtmlAnalyze htmlAnalyze = new HtmlAnalyze(input);
            htmlAnalyze.analyze();
            List<CaptureItem> htmlList = htmlAnalyze.getCaputreList();
            for (CaptureItem captureItem : htmlList) {
                JavaScriptAnalyze jsAnalyze = new JavaScriptAnalyze(captureItem.getCaptureValue(), EnumSet.allOf(JavaScriptAnalyze.AnalyzeOption.class));
                jsAnalyze.analyze();
                List<RegExPattermItem> regexpList = jsAnalyze.getRegExpList();
                System.out.println("regexList");
                for (RegExPattermItem item : regexpList) {
                    System.out.println("capture:" + item.getCaptureValue());
                    System.out.println("regex:" + item.getRegExPattern());
                    System.out.println("flag:" + item.getRegExFlag());
                    int start = captureItem.start() + item.start();
                    int end = captureItem.start() + item.end();
                    System.out.println("start:" + start + String.format("(%x)", start));
                    System.out.println("end:" + end + String.format("(%x)", end));
                }
                System.out.println("commentList");
                List<CaptureItem> commentList = jsAnalyze.getCommentList();
                for (CaptureItem item : commentList) {
                    System.out.println("capture:" + item.getCaptureValue());
                    System.out.println("capture-UTF-8:" + StringUtil.getStringCharset(StringUtil.getBytesRaw(item.getCaptureValue()), StandardCharsets.UTF_8));
                    int start = item.start();
                    int end = item.end();
                    System.out.println("start:" + start + String.format("(%x)", start));
                    System.out.println("end:" + end + String.format("(%x)", end));
                }
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

}
