package passive.signature;

import extension.burp.IPropertyConfig;
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
}
