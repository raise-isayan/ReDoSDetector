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
public class RegExTest {

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
    public void testParseJS() {
        System.out.println("testParseJS");
        String test[] = {
            "isInValidEmail(){\n"
            + "  const reg = new RegExp(\"^[A-Za-z0-9]{1}[A-Za-z0-9_.-]*@{1}[A-Za-z0-9_.-]{1,}\\.[A-Za-z0-9]{1,}$\");\n"
            + "  return !reg.test(this.email);"
            + "}",
            "isInValidEmail(){\n"
            + "  const reg = new RegExp(\"^[a-z0-9]{1}[a-z0-9_.-]*@{1}[a-z0-9_.-]{1,}\\.[a-z0-9]{1,}$\", \"i\");\n"
            + "  return !reg.test(this.email);"
            + "}",
            "isInValidEmail(){\n"
            + "  const reg = new RegExp(/^[a-z0-9]{1}[a-z0-9_.-]*@{1}[a-z0-9_.-]{1,}\\.[a-z0-9]{1,}$/i);\n"
            + "  return !reg.test(this.email);"
            + "}",
            "isInValidEmail(){\n"
            + "  const reg = new RegExp(/^[A-Za-z0-9]{1}[A-Za-z0-9_.-]*@{1}[A-Za-z0-9_.-]{1,}\\.[A-Za-z0-9]{1,}$/);\n"
            + "  return !reg.test(this.email);"
            + "}",
            "isInValidEmail(){\n"
            + "  const reg = new RegExp(/^[a-z0-9]{1}[a-z0-9_.-]*@{1}[a-z0-9_.-]{1,}\\.[a-z0-9]{1,}$/i);\n"
            + "  return !reg.test(this.email);",
            "isInValidEmail(){\n"
            + "  return !/^[A-Za-z0-9]{1}[A-Za-z0-9_.-]*@{1}[A-Za-z0-9_.-]{1,}\\.[A-Za-z0-9]{1,}$/.test(this.email);"
            + "}",
            "isInValidEmail(){\n"
            + "  return !/^[a-z0-9]{1}[a-z0-9_.-]*@{1}[a-z0-9_.-]{1,}\\.[a-z0-9]{1,}$/i.test(this.email);"
            + "}",};

        for (int i = 0; i < test.length; i++) {
            for (int j = 0; j < ReDoSScan.JS_PATTERNS.length; j++) {
                Matcher m = ReDoSScan.JS_PATTERNS[j].matcher(test[i]);
                System.out.println("test[" + i + "]:pattern[" + j + "]:" + m.pattern().pattern());;
                while (m.find()) {
                    System.out.println("\tcapture:" + m.group(1));
                    System.out.println("\tflags:" + m.group(2));
                    System.out.println("\tcapture.start:" + m.start(1));
                    System.out.println("\tcapture.end:" + m.end(1));
                }
            }
        }

    }

    @Test
    public void testParseSS() {
        System.out.println("testParseSS");
        String test[] = {
            // https://github.com/elastic/kibana/blob/68d323b4891e7d6147af70b48df3904d098d4a81/x-pack/plugins/cloud_defend/public/common/utils.ts#L193
            "\"zzz\" values must match the pattern: /^(aa|aa)*$/",};
        for (int i = 0; i < test.length; i++) {
            for (int j = 0; j < ReDoSScan.VALIDATION_PATTERNS.length; j++) {
                Matcher m = ReDoSScan.VALIDATION_PATTERNS[j].matcher(test[i]);
                System.out.println("test[" + i + "]:pattern[" + j + "]:" + m.pattern().pattern());;
                while (m.find()) {
                    System.out.println("\tcapture:" + m.group(1));
                    System.out.println("\tflags:" + m.group(2));
                    System.out.println("\tcapture.start:" + m.start(1));
                    System.out.println("\tcapture.end:" + m.end(1));
                }
            }
        }
    }

}
