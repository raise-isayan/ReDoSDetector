package passive.ast;

import extension.burp.TypeParameter;
import extension.helpers.FileUtil;
import extension.helpers.StringUtil;
import extension.view.base.CaptureItem;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author isayan
 */
public class HtmlAnalyzeTest {

    public HtmlAnalyzeTest() {
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

    @Test
    public void testHtmlAnalyze() {
        try {
            System.out.println("testHtmlAnalyze");
            InputStream htmlStream = HtmlAnalyzeTest.class.getResourceAsStream("/resources/script.html");
            String input = StringUtil.getStringCharset(FileUtil.readAllBytes(htmlStream), StandardCharsets.UTF_8);
            HtmlAnalyze htmlAnalyze = new HtmlAnalyze(input);
            htmlAnalyze.analyze();
            System.out.println("TypeParameter:");
            for (TypeParameter item : htmlAnalyze.getParameterInputList()) {
                System.out.println("\tname:" + item.name());
                System.out.println("\ttype:" + item.type());
                System.out.println("\tvalue:" + item.value());
            }
            System.out.println("comments:");
            for (CaptureItem item : htmlAnalyze.getCommentList()) {
                System.out.println("\tcapture:" + item.getCaptureValue());
                System.out.println("\tstart:" + item.start());
                System.out.println("\tend:" + item.end());
                System.out.println("\source:" + input.substring(item.start(), item.end()));
            }
            System.out.println("pattern:");
            for (CaptureItem item : htmlAnalyze.getInputPatternList()) {
                System.out.println("\tcapture:" + item.getCaptureValue());
                System.out.println("\tstart:" + item.start());
                System.out.println("\tend:" + item.end());
                System.out.println("\source:" + input.substring(item.start(), item.end()));
            }
        } catch (IOException ex) {
            fail();
        }

    }

}
