package redoscheckr;

import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redoscheckr.ReDoSOption.CheckerType;

/**
 *
 * @author isayan
 */
public class ReDosDetectorTest {

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
    public void testSafe() {
        System.out.println("testSafe");
        ReDosDetector detect = new ReDosDetector();
        {
            ReDoSOption option = new ReDoSOption();
            String regex = "^foo$";
            String flags = "";
            DetectIssue result = detect.scan(regex, flags, option);
            assertEquals(result.getStatus(), ReDoSOption.StatusType.SAFE);
            assertEquals(result.getSource(), regex);
            assertEquals(result.getFlags(), flags);
            assertEquals(result.getChecker(), ReDoSOption.CheckerType.AUTOMATON);
            assertFalse(result.getAttack().isPresent());
            assertFalse(result.getHotspot().isPresent());
            assertTrue(result.getComplexity().isPresent());
            if (result.getComplexity().isPresent()) {
                assertEquals(result.getComplexity().get(), ReDoSOption.ComplexityType.SAFE);
            }
            assertTrue(result.getLog().isEmpty());
        }
        {
            ReDoSOption option = new ReDoSOption();
            option.setChecker(ReDoSOption.CheckerType.FUZZ);
            String regex = "^foo$";
            String flags = "";
            DetectIssue result = detect.scan(regex, flags, option);
            assertEquals(result.getStatus(), ReDoSOption.StatusType.SAFE);
            assertEquals(result.getSource(), regex);
            assertEquals(result.getFlags(), flags);
            assertEquals(result.getChecker(), ReDoSOption.CheckerType.FUZZ);
            assertFalse(result.getAttack().isPresent());
            assertFalse(result.getHotspot().isPresent());
            assertTrue(result.getComplexity().isPresent());
            if (result.getComplexity().isPresent()) {
                assertEquals(result.getComplexity().get(), ReDoSOption.ComplexityType.SAFE);
            }
            assertTrue(result.getLog().isEmpty());
        }
        {
            ReDoSOption option = new ReDoSOption();
            option.setChecker(ReDoSOption.CheckerType.AUTOMATON);
            String regex = "^.*$";
            String flags = "";
            DetectIssue result = detect.scan(regex, flags, option);
            assertEquals(result.getStatus(), ReDoSOption.StatusType.SAFE);
            assertEquals(result.getSource(), regex);
            assertEquals(result.getFlags(), flags);
            assertEquals(result.getChecker(), ReDoSOption.CheckerType.AUTOMATON);
            assertFalse(result.getAttack().isPresent());
            assertFalse(result.getHotspot().isPresent());
            assertTrue(result.getComplexity().isPresent());
            if (result.getComplexity().isPresent()) {
                assertEquals(result.getComplexity().get(), ReDoSOption.ComplexityType.LINEAR);
            }
            assertTrue(result.getLog().isEmpty());
        }
    }

    @Test
    public void testUnknown() {
        System.out.println("testUnknown");
        ReDosDetector detect = new ReDosDetector();
        {
            ReDoSOption option = new ReDoSOption();
            String regex = "";
            String flags = "x";
            DetectIssue result = detect.scan(regex, flags, option);
            assertEquals(result.getStatus(), ReDoSOption.StatusType.UNKNOWN);
            assertEquals(result.getSource(), regex);
            assertEquals(result.getFlags(), flags);
            assertEquals(result.getChecker(), ReDoSOption.CheckerType.NONE);
            assertFalse(result.getAttack().isPresent());
            assertFalse(result.getHotspot().isPresent());
            assertFalse(result.getComplexity().isPresent());
            assertTrue(result.getLog().isEmpty());
        }
        {
            ReDoSOption option = new ReDoSOption();
            String regex = "(";
            String flags = "";
            DetectIssue result = detect.scan(regex, flags, option);
            assertEquals(result.getStatus(), ReDoSOption.StatusType.UNKNOWN);
            assertEquals(result.getSource(), regex);
            assertEquals(result.getFlags(), flags);
            assertEquals(result.getChecker(), ReDoSOption.CheckerType.NONE);
            assertFalse(result.getAttack().isPresent());
            assertFalse(result.getHotspot().isPresent());
            assertFalse(result.getComplexity().isPresent());
            assertTrue(result.getLog().isEmpty());
        }
    }

    @Test
    public void testVulnerable() {
        System.out.println("testVulnerable");
        ReDosDetector detect = new ReDosDetector();
        {
            System.out.println("----------------------------------------------");
            String regex = "^(?:a|a)*$";
            String flags = "";
            ReDoSOption option = new ReDoSOption();
            DetectIssue result = detect.scan(regex, flags, option);
            assertEquals(result.getStatus(), ReDoSOption.StatusType.VULNERABLE);
            assertEquals(result.getSource(), regex);
            assertEquals(result.getFlags(), flags);
            assertEquals(result.getChecker(), ReDoSOption.CheckerType.AUTOMATON);
            System.out.println("source:" + result.getSource());

            assertTrue(result.getAttack().isPresent());
            if (result.getAttack().isPresent()) {
                AttackString atk = result.getAttack().get();
                System.out.println("attack:" + atk.toString());
                List<Pumps> pumps = atk.getPumpList();
                for (int i = 0; i < pumps.size(); i++) {
                    System.out.println("pumps[" + i + "].prefix:" + pumps.get(i).getPrefix());
                    System.out.println("pumps[" + i + "].pump:" + pumps.get(i).getPump());
                    System.out.println("pumps[" + i + "].bias:" + pumps.get(i).getBias());
                }
            }

            assertTrue(result.getHotspot().isPresent());
            if (result.getHotspot().isPresent()) {
                System.out.println("hotspot:");
                HotSpot hotspot = result.getHotspot().get();
                for (HotSpot.Spot spot : hotspot.getSpotList()) {
                    System.out.println("spot:" + spot.toString());
                }
            }

            assertTrue(result.getComplexity().isPresent());
            if (result.getComplexity().isPresent()) {
//                assertEquals(result.getComplexity().get(), ReDoSOption.ComplexityType.EXPONENTIAL);
                System.out.println(result.getComplexity().get());
            }
            assertTrue(result.getLog().isEmpty());
        }
        {
            System.out.println("----------------------------------------------");
            String regex = "^([a-z]+)*$";
            String flags = "";
            ReDoSOption option = new ReDoSOption();
            DetectIssue result = detect.scan(regex, flags, option);
            assertEquals(result.getStatus(), ReDoSOption.StatusType.VULNERABLE);
            assertEquals(result.getSource(), regex);
            assertEquals(result.getFlags(), flags);
            assertEquals(result.getChecker(), ReDoSOption.CheckerType.AUTOMATON);
            System.out.println("source:" + result.getSource());

            assertTrue(result.getAttack().isPresent());
            if (result.getAttack().isPresent()) {
                AttackString atk = result.getAttack().get();
                System.out.println("attack:" + atk.toString());
                List<Pumps> pumps = atk.getPumpList();
                for (int i = 0; i < pumps.size(); i++) {
                    System.out.println("pumps[" + i + "].prefix:" + pumps.get(i).getPrefix());
                    System.out.println("pumps[" + i + "].pump:" + pumps.get(i).getPump());
                    System.out.println("pumps[" + i + "].bias:" + pumps.get(i).getBias());
                }
            }

            assertTrue(result.getHotspot().isPresent());
            if (result.getHotspot().isPresent()) {
                System.out.println("hotspot:");
                HotSpot hotspot = result.getHotspot().get();
                for (HotSpot.Spot spot : hotspot.getSpotList()) {
                    System.out.println("spot:" + spot.toString());
                }
            }

            assertTrue(result.getComplexity().isPresent());
            if (result.getComplexity().isPresent()) {
                assertEquals(result.getComplexity().get(), ReDoSOption.ComplexityType.EXPONENTIAL);
            }
            assertTrue(result.getLog().isEmpty());
        }
        {
            System.out.println("----------------------------------------------");
            String regex = "^(.*)*$";
            String flags = "";
            ReDoSOption option = new ReDoSOption();
            DetectIssue result = detect.scan(regex, flags, option);
            assertEquals(result.getStatus(), ReDoSOption.StatusType.VULNERABLE);
            assertEquals(result.getSource(), regex);
            assertEquals(result.getFlags(), flags);
            assertEquals(result.getChecker(), ReDoSOption.CheckerType.AUTOMATON);
            System.out.println("source:" + result.getSource());

            assertTrue(result.getAttack().isPresent());
            if (result.getAttack().isPresent()) {
                AttackString atk = result.getAttack().get();
                System.out.println("attack:" + atk.toString());
                List<Pumps> pumps = atk.getPumpList();
                for (int i = 0; i < pumps.size(); i++) {
                    System.out.println("pumps[" + i + "].prefix:" + pumps.get(i).getPrefix());
                    System.out.println("pumps[" + i + "].pump:" + pumps.get(i).getPump());
                    System.out.println("pumps[" + i + "].bias:" + pumps.get(i).getBias());
                }
            }

            assertTrue(result.getHotspot().isPresent());
            if (result.getHotspot().isPresent()) {
                HotSpot hotspot = result.getHotspot().get();
                System.out.println("hotspot:");
                for (HotSpot.Spot spot : hotspot.getSpotList()) {
                    System.out.println("spot:" + spot.toString());
                }
            }

            assertTrue(result.getComplexity().isPresent());
            if (result.getComplexity().isPresent()) {
                assertEquals(result.getComplexity().get(), ReDoSOption.ComplexityType.EXPONENTIAL);
            }
            assertTrue(result.getLog().isEmpty());
        }
        {
            System.out.println("----------------------------------------------");
            String regex = "^(a|a)*$";
            String flags = "";
            ReDoSOption option = new ReDoSOption();
            DetectIssue result = detect.scan(regex, flags, option);
            assertEquals(result.getStatus(), ReDoSOption.StatusType.VULNERABLE);
            assertEquals(result.getSource(), regex);
            assertEquals(result.getFlags(), flags);
            assertEquals(result.getChecker(), ReDoSOption.CheckerType.AUTOMATON);
            System.out.println("source:" + result.getSource());

            assertTrue(result.getAttack().isPresent());
            if (result.getAttack().isPresent()) {
                AttackString atk = result.getAttack().get();
                System.out.println("attack:" + atk.toString());
                List<Pumps> pumps = atk.getPumpList();
                for (int i = 0; i < pumps.size(); i++) {
                    System.out.println("pumps[" + i + "].prefix:" + pumps.get(i).getPrefix());
                    System.out.println("pumps[" + i + "].pump:" + pumps.get(i).getPump());
                    System.out.println("pumps[" + i + "].bias:" + pumps.get(i).getBias());
                }
            }
            assertTrue(result.getComplexity().isPresent());
            if (result.getComplexity().isPresent()) {
                assertEquals(result.getComplexity().get(), ReDoSOption.ComplexityType.EXPONENTIAL);
            }
            assertTrue(result.getHotspot().isPresent());
            if (result.getHotspot().isPresent()) {
                System.out.println("hotspot:");
                HotSpot hotspot = result.getHotspot().get();
                for (HotSpot.Spot spot : hotspot.getSpotList()) {
                    System.out.println("spot:" + spot.toString());
                }
            }
            assertTrue(result.getComplexity().isPresent());
            if (result.getComplexity().isPresent()) {
                assertEquals(result.getComplexity().get(), ReDoSOption.ComplexityType.EXPONENTIAL);
            }
            assertTrue(result.getLog().isEmpty());
        }

        {
            System.out.println("----------------------------------------------");
            String regex = "\\snew\\s*RegExp\\(\\s*(?:\"(.*?)\"\\s*(?:,\\s*\"([dgimsuvy]+)\"\\s*)?)\\)";
            String flags = "";
            ReDoSOption option = new ReDoSOption();
            DetectIssue result = detect.scan(regex, flags, option);
            assertEquals(result.getStatus(), ReDoSOption.StatusType.VULNERABLE);
            assertEquals(result.getSource(), regex);
            assertEquals(result.getFlags(), flags);
            assertEquals(result.getChecker(), ReDoSOption.CheckerType.AUTOMATON);
            System.out.println("source:" + result.getSource());

            assertTrue(result.getAttack().isPresent());
            if (result.getAttack().isPresent()) {
                AttackString atk = result.getAttack().get();
                System.out.println("attack:" + atk.toString());
                List<Pumps> pumps = atk.getPumpList();
                for (int i = 0; i < pumps.size(); i++) {
                    System.out.println("pumps[" + i + "].prefix:" + pumps.get(i).getPrefix());
                    System.out.println("pumps[" + i + "].pump:" + pumps.get(i).getPump());
                    System.out.println("pumps[" + i + "].bias:" + pumps.get(i).getBias());
                }
            }
            assertTrue(result.getComplexity().isPresent());
            if (result.getComplexity().isPresent()) {
                assertEquals(result.getComplexity().get(), ReDoSOption.ComplexityType.POLYNOMIAL);
            }
            assertTrue(result.getHotspot().isPresent());
            if (result.getHotspot().isPresent()) {
                System.out.println("hotspot:");
                HotSpot hotspot = result.getHotspot().get();
                for (HotSpot.Spot spot : hotspot.getSpotList()) {
                    System.out.println("spot:" + spot.toString());
                }
            }
            assertTrue(result.getComplexity().isPresent());
            if (result.getComplexity().isPresent()) {
                assertEquals(result.getComplexity().get(), ReDoSOption.ComplexityType.POLYNOMIAL);
            }
            assertTrue(result.getLog().isEmpty());

        }

    }

    @Test
    public void testAttackString() {
        System.out.println("testAttackString");
        ReDosDetector detect = new ReDosDetector();
        {
            String regex = "(aa|aa)*$";
            String flags = "";
            DetectIssue result = detect.scan(regex, flags);
            System.out.println("debug:" + result.debugStringClassName());
            assertTrue(result.getAttack().isPresent());
            if (result.getAttack().isPresent()) {
                AttackString atk = result.getAttack().get();
                System.out.println("asUString:" + atk.getAsUString());
                System.out.println("getFixedSize:" + atk.getFixedSize());
                System.out.println("getRepeatSize:" + atk.getRepeatSize());
                System.out.println("getRepeatCount:" + atk.getRepeatCount());
                System.out.println("toString:" + atk.toString());
            }
            assertTrue(result.getHotspot().isPresent());
            assertTrue(result.getLog().isEmpty());
        }
    }

    @Test
    public void testTimeout() {
        System.out.println("testTimeout");
        ReDosDetector detect = new ReDosDetector();
        {
            ReDoSOption option = new ReDoSOption();
            option.setTimeout(-1000);
            String regex = "^foo$";
            String flags = "";
            DetectIssue result = detect.scan(regex, flags, option);
            assertEquals(result.getStatus(), ReDoSOption.StatusType.UNKNOWN);
            assertEquals(result.getSource(), regex);
            assertEquals(result.getFlags(), flags);
            assertFalse(result.getAttack().isPresent());
            assertFalse(result.getHotspot().isPresent());
            assertEquals(result.getChecker(), CheckerType.NONE);
            assertFalse(result.getComplexity().isPresent());
            assertTrue(result.getLog().isEmpty());
        }
    }

}
