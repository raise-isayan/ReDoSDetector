
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
    public void testRegexFlag() {
        System.out.println("testRegexFlag");
        {
            String flag = ReDoSOption.RegexFlag.toFlags(EnumSet.allOf(ReDoSOption.RegexFlag.class));
            assertEquals("dgimsuvy", flag);
        }
        {
            String flag = ReDoSOption.RegexFlag.toFlags(EnumSet.noneOf(ReDoSOption.RegexFlag.class));
            assertEquals("", flag);
        }
        {
            String flag = ReDoSOption.RegexFlag.toFlags(EnumSet.of(ReDoSOption.RegexFlag.GLOBAL));
            assertEquals("g", flag);
        }
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

    @Test
    public void testEnum() {
        System.out.println("testEnum");
        {
            AccelerationModeType auto = ReDoSOption.AccelerationModeType.AUTO;
            assertEquals(auto, ReDoSOption.AccelerationModeType.AUTO);
            AccelerationModeType off = ReDoSOption.AccelerationModeType.OFF;
            assertEquals(off, ReDoSOption.AccelerationModeType.OFF);
            AccelerationModeType on = ReDoSOption.AccelerationModeType.ON;
            assertEquals(on, ReDoSOption.AccelerationModeType.ON);
        }

        {
            ReDoSOption.CheckerType auto = ReDoSOption.CheckerType.AUTO;
            assertEquals(auto, ReDoSOption.CheckerType.AUTO);
            ReDoSOption.CheckerType none = ReDoSOption.CheckerType.NONE;
            assertEquals(none, ReDoSOption.CheckerType.NONE);
        }

        {
            ReDoSOption.FieldType attack = ReDoSOption.FieldType.ATTACK;
            assertEquals(attack, ReDoSOption.FieldType.ATTACK);
            ReDoSOption.FieldType source = ReDoSOption.FieldType.SOURCE;
            assertEquals(source, ReDoSOption.FieldType.SOURCE);
        }

        {
            ReDoSOption.ComplexityType constant = ReDoSOption.ComplexityType.CONSTANT;
            assertEquals(constant, ReDoSOption.ComplexityType.CONSTANT);
            ReDoSOption.ComplexityType exponential = ReDoSOption.ComplexityType.EXPONENTIAL;
            assertEquals(exponential, ReDoSOption.ComplexityType.EXPONENTIAL);
            ReDoSOption.ComplexityType linear = ReDoSOption.ComplexityType.LINEAR;
            assertEquals(linear, ReDoSOption.ComplexityType.LINEAR);
            ReDoSOption.ComplexityType polynomial = ReDoSOption.ComplexityType.POLYNOMIAL;
            assertEquals(polynomial, ReDoSOption.ComplexityType.POLYNOMIAL);
            ReDoSOption.ComplexityType safe = ReDoSOption.ComplexityType.SAFE;
            assertEquals(safe, ReDoSOption.ComplexityType.SAFE);
        }

        {
            ReDoSOption.RegexFlag dotall = ReDoSOption.RegexFlag.DOTALL;
            assertEquals(dotall, ReDoSOption.RegexFlag.DOTALL);
            ReDoSOption.RegexFlag unicode_set = ReDoSOption.RegexFlag.UNICODE_SETS;
            assertEquals(unicode_set, ReDoSOption.RegexFlag.UNICODE_SETS);
        }

        {
            ReDoSOption.SeederType dynamic = ReDoSOption.SeederType.DYNAMIC;
            assertEquals(dynamic, ReDoSOption.SeederType.DYNAMIC);
            ReDoSOption.SeederType statics = ReDoSOption.SeederType.STATIC;
            assertEquals(statics, ReDoSOption.SeederType.STATIC);
        }

        {
            ReDoSOption.StatusType vule = ReDoSOption.StatusType.VULNERABLE;
            assertEquals(vule, ReDoSOption.StatusType.VULNERABLE);
            ReDoSOption.StatusType safe = ReDoSOption.StatusType.SAFE;
            assertEquals(safe, ReDoSOption.StatusType.SAFE);
        }

    }

}
