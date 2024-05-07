package passive.signature;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author isayan
 */
public class ReDoSTest {

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
    public void testReDoSVuln1() {
        System.out.println("testReDoS");
        Pattern p = Pattern.compile("/([a-zA-Z0-9]+)*$/");
        Matcher m = p.matcher("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@");
        boolean result = m.find();
        System.out.println("math:" + result);
    }

}
