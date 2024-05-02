package redoscheckr;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import redoscheckr.ReDoSOption.AccelerationModeType;
import redoscheckr.ReDoSOption.SeederType;

/**
 *
 * @author isayan
 */
public class ReDoSOptionITest {

    public ReDoSOptionITest() {
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
    public void testComplexityType() {
        System.out.println("testComplexityType");
        String s = Arrays.asList(ReDoSOption.ComplexityType.values()).stream().map(m -> m.name()).collect(Collectors.joining("|"));
        System.out.println(s);
    }

    @Test
    public void testFlags() {
        System.out.println("getFlags");
        EnumSet<ReDoSOption.RegexFlag> flags = EnumSet.allOf(ReDoSOption.RegexFlag.class);
        String value = ReDoSOption.RegexFlag.toFlags(flags);
        assertEquals(value, "dgimsuvy");
    }

    @Test
    public void testGetOption() {
        System.out.println("getOpton");
        ReDoSOption option = new ReDoSOption();
        assertEquals(option.getAccelerationMode(), AccelerationModeType.AUTO); // accelerationMode
        assertEquals(option.getAttackLimit(), 1500000000); // attackLimit
        assertEquals(option.getAttackTimeout(), 1000); // attackTimeout
        assertEquals(option.getChecker(), ReDoSOption.CheckerType.AUTO); // checker
        assertEquals(option.getCrossoverSize(), 25); // crossoverSize
        assertEquals(option.getHeatRatio(), 0.001); // heatRatio
        assertEquals(option.getIncubationLimit(), 2500); // incubationLimit
        assertEquals(option.getIncubationTimeout(), 250); // incubationTimeout
        assertEquals(option.getLogger(), ReDoSOption.LoggerType.OFF); // logger
        assertEquals(option.getMaxAttackStringSize(), 300000); // maxAttackStringSize
        assertEquals(option.getMaxDegree(), 4); // maxDegree
        assertEquals(option.getMaxGeneStringSize(), 2400); // maxGeneStringSize
        assertEquals(option.getMaxGenerationSize(), 100); // maxGenerationSize
        assertEquals(option.getMaxInitialGenerationSize(), 500); // maxInitialGenerationSize
        assertEquals(option.getMaxIteration(), 10); // maxIteration
        assertEquals(option.getMaxNFASize(), 35000); // maxNFASize
        assertEquals(option.getMaxPatternSize(), 1500); // maxPatternSize
        assertEquals(option.getMaxRecallStringSize(), 300000); // maxRecallStringSize
        assertEquals(option.getMaxRepeatCount(), 30); // maxRepeatCount
        assertEquals(option.getMaxSimpleRepeatCount(), 30); // maxSimpleRepeatCount
        assertEquals(option.getMutationSize(), 50); // mutationSize
        assertEquals(option.getRandomSeed(), 0); // randomSeed
        assertEquals(option.getRecallLimit(), 1500000000); // recallLimit
        assertEquals(option.getRecallTimeout(), -1000); // recallTimeout
        assertEquals(option.getSeeder(), SeederType.STATIC); // seeder
        assertEquals(option.getSeedingLimit(), 1000); // seedingLimit
        assertEquals(option.getSeedingTimeout(), 100); // seedingTimeout
        assertEquals(option.getTimeout(), 10000); // timeout
    }

}
