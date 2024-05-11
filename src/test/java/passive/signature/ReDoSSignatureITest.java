package passive.signature;

import extension.burp.IPropertyConfig;
import extension.helpers.SmartCodec;
import java.util.regex.Matcher;
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
public class ReDoSSignatureITest {

    public ReDoSSignatureITest() {
    }

    private ReDoSSignature signature = new ReDoSSignature();
    private ReDoSScan scan = new ReDoSScan();

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
    public void testReDoSSignature() {
        System.out.println("testReDoSSignature");
        IPropertyConfig cfg = signature.getSignatureConfig();
        System.out.println(cfg.loadSetting());
    }

    @Test
    public void testReDoSScan() {
        System.out.println("testReDoSScan");
        System.out.println(scan.loadSetting());
    }

    @Test
    public void testReDoSJS() {
        System.out.println("testReDoSJS");
        for (int i = 0; i < ReDoSScan.MATCH.length; i++) {
            Matcher m = ReDoSScan.MATCH[i].getMatcher("var re = new RegExp(\"([a-z]*)\");");
            if (m.find()) {
                assertEquals("([a-z]*)", m.group(1));
                break;
            }
        }
    }

    @Test
    public void testReDoSValidation() {
        System.out.println("testReDoSValidation");
        for (int i = 0; i < ReDoSScan.MATCH.length; i++) {
            Matcher m = ReDoSScan.MATCH[i].getMatcher(" match the pattern: (a|a)*$ -");
            if (m.find()) {
                assertEquals("(a|a)*$", m.group(1));
                break;
            }
        }
        for (int i = 0; i < ReDoSScan.MATCH.length; i++) {
            Matcher m = ReDoSScan.MATCH[i].getMatcher(" match the pattern: '(a|a)*$' -");
            if (m.find()) {
                assertEquals("(a|a)*$", m.group(1));
                break;
            }
        }
        for (int i = 0; i < ReDoSScan.MATCH.length; i++) {
            Matcher m = ReDoSScan.MATCH[i].getMatcher(" match the pattern: \"(a|a)*$\" -");
            if (m.find()) {
                assertEquals("(a|a)*$", m.group(1));
                break;
            }
        }
        for (int i = 0; i < ReDoSScan.MATCH.length; i++) {
            Matcher m = ReDoSScan.MATCH[i].getMatcher(" match the pattern: \"(a|a)*$\" -");
            if (m.find()) {
                assertEquals("(a|a)*$", m.group(1));
                break;
            }
        }
        for (int i = 0; i < ReDoSScan.MATCH.length; i++) {
            Matcher m = ReDoSScan.MATCH[i].getMatcher(" match the pattern: &#39;(a|a)*$&#39; -");
            if (m.find()) {
                String regex = SmartCodec.toHtmlDecode(m.group(1), SmartCodec.ENCODE_PATTERN_ALPHANUM);
                assertEquals("(a|a)*$", regex);
                break;
            }
        }
        for (int i = 0; i < ReDoSScan.MATCH.length; i++) {
            Matcher m = ReDoSScan.MATCH[i].getMatcher(" match the pattern: &quot;(a|a)*$&quot; -");
            boolean find = m.find();
            if (find) {
                System.out.println("regex:" + ReDoSScan.MATCH[i]);
                String regex = SmartCodec.toHtmlDecode(m.group(1), SmartCodec.ENCODE_PATTERN_ALPHANUM);
                assertEquals("(a|a)*$", regex);
                break;
            }
        }
    }

}
