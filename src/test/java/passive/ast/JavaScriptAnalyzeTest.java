package passive.ast;

import java.io.InputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import extension.helpers.FileUtil;
import extension.helpers.StringUtil;
import extension.view.base.CaptureItem;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import java.util.logging.Level;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import passive.ast.JavaScriptAnalyze.AnalyzeOption;
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
    public void testRegex() {
        System.out.println("testRegex");
        try {
            long start = System.currentTimeMillis();
            InputStream regexStream = JavaScriptAnalyzeTest.class.getResourceAsStream("/resources/regexliteral.js");
            String input = StringUtil.getStringRaw(FileUtil.readAllBytes(regexStream));
            JavaScriptAnalyze jsAnalyze = new JavaScriptAnalyze(EnumSet.allOf(AnalyzeOption.class));
            jsAnalyze.analyze(input);
            System.out.println("RegExp");
            List<RegExPattermItem> regList = jsAnalyze.getRegExpList();
            for (RegExPattermItem item : regList) {
                System.out.println("capture:" + item.getCaptureValue());
                System.out.println("pattern:" + item.getRegExPattern());
                System.out.println("flag:" + item.getRegExFlag());
            }
            System.out.println("Comment");
            List<CaptureItem> commentList = jsAnalyze.getCommentList();
            for (CaptureItem item : commentList) {
                System.out.println("capture:" + item.getCaptureValue());
            }
            long end = System.currentTimeMillis();
            System.out.println("time:" + (end - start));
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
            JavaScriptAnalyze jsAnalyze = new JavaScriptAnalyze(EnumSet.allOf(AnalyzeOption.class));
            for (CaptureItem captureItem : htmlList) {
                jsAnalyze.analyze(captureItem.getCaptureValue());
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
