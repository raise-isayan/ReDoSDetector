package passive.ast;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import extension.helpers.FileUtil;
import extension.helpers.StringUtil;
import extension.view.base.CaptureItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author isayan
 */
public class HtmlAnalyzeTest {

    private final static Logger logger = Logger.getLogger(HtmlAnalyzeTest.class.getName());

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
    public void testHtmlAnalyzeT() {
        System.out.println("testHtmlAnalyzeT");
        try {
            InputStream htmlStream = HtmlAnalyzeTest.class.getResourceAsStream("/resources/script.html");
            String input = StringUtil.getStringCharset(FileUtil.readAllBytes(htmlStream), StandardCharsets.UTF_8);
            HtmlAnalyze analyze = new HtmlAnalyze(input);
            analyze.analyze();
            List<CaptureItem> scriptList = analyze.getCaputreList();
            for (CaptureItem item : scriptList) {
                System.out.println("capture:" + item.getCaptureValue());
                System.out.println("start:" + item.start());
                System.out.println("end:" + item.end());
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

}
