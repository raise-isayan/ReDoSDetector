package redoscheckr;

import com.google.gson.annotations.Expose;
import java.io.Writer;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import scala.Option;
import scala.concurrent.duration.Duration;

/**
 * https://makenowjust-labs.github.io/recheck/docs/usage/parameters/
 *
 * @author isayan
 *
 */
public class ReDoSOption {

    // https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide/Regular_expressions#advanced_searching_with_flags
    // d	Generate indices for substring matches.	hasIndices
    // g	Global search.	global
    // i	Case-insensitive search.	ignoreCase
    // m	Allows ^ and $ to match next to newline characters.	multiline
    // s	Allows . to match newline characters.	dotAll
    // u	"Unicode"; treat a pattern as a sequence of Unicode code points.	unicode
    // v	An upgrade to the u mode with more Unicode features.	unicodeSets
    // y	Perform a "sticky" search that matches starting at the current position in the target string.	sticky
    public enum RegexFlag {
        HAS_INDICES('d'),
        GLOBAL('g'),
        IGNORE_CASE('i'),
        MULTILINE('m'),
        DOTALL('s'),
        UNICODE('u'),
        UNICODE_SETS('v'),
        STICKY('y');

        private final char flagChar;

        RegexFlag(char flagChar) {
            this.flagChar = flagChar;
        }

        public char getFlagChar() {
            return this.flagChar;
        }

        public static String toFlags(EnumSet<RegexFlag> flags) {
            StringBuilder buff = new StringBuilder();
            flags.forEach(flag -> buff.append(flag.getFlagChar()));
            return buff.toString();
        }
    }

    public enum AccelerationModeType {
        AUTO(codes.quine.labs.recheck.common.AccelerationMode.Auto$.MODULE$),
        ON(codes.quine.labs.recheck.common.AccelerationMode.On$.MODULE$),
        OFF(codes.quine.labs.recheck.common.AccelerationMode.Off$.MODULE$);

        private final codes.quine.labs.recheck.common.AccelerationMode scalar;

        AccelerationModeType(codes.quine.labs.recheck.common.AccelerationMode scalar) {
            this.scalar = scalar;
        }

        public codes.quine.labs.recheck.common.AccelerationMode toValue() {
            return this.scalar;
        }

        public boolean equals(AccelerationModeType accelerationModeType) {
            return this.scalar.equals(accelerationModeType.scalar);
        }

    };

    public enum CheckerType {
        AUTO(codes.quine.labs.recheck.common.Checker.Auto$.MODULE$),
        FUZZ(codes.quine.labs.recheck.common.Checker.Fuzz$.MODULE$),
        AUTOMATON(codes.quine.labs.recheck.common.Checker.Automaton$.MODULE$),
        NONE(null);

        private final codes.quine.labs.recheck.common.Checker scalar;

        CheckerType(codes.quine.labs.recheck.common.Checker scalar) {
            this.scalar = scalar;
        }

        public codes.quine.labs.recheck.common.Checker toValue() {
            return this.scalar;
        }

        public boolean equals(CheckerType checkerType) {
            return this.scalar.equals(checkerType.scalar);
        }

        public static CheckerType parseEnum(String name) {
            String value = name.toUpperCase();
            return CheckerType.valueOf(value);
        }

    };

    public enum LoggerType {
        ON,
        OFF;
    }

    public enum SeederType {
        STATIC(codes.quine.labs.recheck.common.Seeder.Static$.MODULE$),
        DYNAMIC(codes.quine.labs.recheck.common.Seeder.Dynamic$.MODULE$);

        private final codes.quine.labs.recheck.common.Seeder scalar;

        SeederType(codes.quine.labs.recheck.common.Seeder scalar) {
            this.scalar = scalar;
        }

        public codes.quine.labs.recheck.common.Seeder toValue() {
            return this.scalar;
        }

        public boolean equals(SeederType seederType) {
            return this.scalar.equals(seederType.scalar);
        }

    }

    public enum FieldType {
        SOURCE,
        FLAGS,
        COMPLEXITY,
        ATTACK,
        HOTSPOT,
        CHECKER,
        ERROR;

        public static FieldType parseEnum(String s) {
            String value = s.toUpperCase();
            return Enum.valueOf(FieldType.class, value);
        }

    };

    public enum StatusType {
        SAFE,
        VULNERABLE,
        UNKNOWN;
    };

    public enum ComplexityType {
        SAFE(codes.quine.labs.recheck.diagnostics.AttackComplexity.Safe$.MODULE$.apply(false)),
        CONSTANT(codes.quine.labs.recheck.diagnostics.AttackComplexity.Constant$.MODULE$),
        LINEAR(codes.quine.labs.recheck.diagnostics.AttackComplexity.Linear$.MODULE$),
        POLYNOMIAL(codes.quine.labs.recheck.diagnostics.AttackComplexity.Polynomial$.MODULE$.apply(0, false)),
        EXPONENTIAL(codes.quine.labs.recheck.diagnostics.AttackComplexity.Exponential$.MODULE$.apply(false));

        private final static Pattern PATTERN = Pattern.compile("(" + Arrays.asList(ReDoSOption.ComplexityType.values()).stream().map(m -> m.name()).collect(Collectors.joining("|")) + ")");

        private final codes.quine.labs.recheck.diagnostics.AttackComplexity scalar;

        ComplexityType(codes.quine.labs.recheck.diagnostics.AttackComplexity scalar) {
            this.scalar = scalar;
        }

        public static ComplexityType parseEnum(String name) {
            String value = name.toUpperCase();
            Matcher m = PATTERN.matcher(value);
            if (m.find()) {
                return Enum.valueOf(ComplexityType.class, m.group(1));
            }
            throw new IllegalArgumentException(
                "No enum constant " + CheckerType.class.getCanonicalName() + "." + name);
        }

        public boolean equals(ComplexityType complexityType) {
            return this.scalar.equals(complexityType.scalar);
        }

    };

    @Expose
    private AccelerationModeType accelerationMode = AccelerationModeType.AUTO;
    @Expose
    private int attackLimit = 1500000000;
    @Expose
    private int attackTimeout = 1000; // ms
    @Expose
    private CheckerType checker = CheckerType.AUTO;
    @Expose
    private int crossoverSize = 25;
    @Expose
    private double heatRatio = 0.001;
    @Expose
    private int incubationLimit = 2500;
    @Expose
    private int incubationTimeout = 250; // ms
    @Expose
    private LoggerType logger = LoggerType.OFF; // None
    @Expose
    private int maxAttackStringSize = 300000;
    @Expose
    private int maxDegree = 4;
    @Expose
    private int maxGeneStringSize = 2400;
    @Expose
    private int maxGenerationSize = 100;
    @Expose
    private int maxInitialGenerationSize = 500;
    @Expose
    private int maxIteration = 10;
    @Expose
    private int maxNFASize = 35000;
    @Expose
    private int maxPatternSize = 1500;
    @Expose
    private int maxRecallStringSize = 300000;
    @Expose
    private int maxRepeatCount = 30;
    @Expose
    private int maxSimpleRepeatCount = 30;
    @Expose
    private int mutationSize = 50;
    @Expose
    private long randomSeed = 0;
    @Expose
    private int recallLimit = 1500000000;
    @Expose
    private int recallTimeout = -1000; // ms
    @Expose
    private SeederType seeder = SeederType.STATIC;
    @Expose
    private int seedingLimit = 1000;
    @Expose
    private int seedingTimeout = 100; // ms
    @Expose
    private int timeout = 10000; // ms

    /**
     * @return the accelerationMode
     */
    public AccelerationModeType getAccelerationMode() {
        return this.accelerationMode;
    }

    /**
     * @param accelerationMode the accelerationMode to set
     */
    public void setAccelerationMode(AccelerationModeType accelerationMode) {
        this.accelerationMode = accelerationMode;
    }

    /**
     * @return the attackLimit
     */
    public int getAttackLimit() {
        return this.attackLimit;
    }

    /**
     * @param attackLimit the attackLimit to set
     */
    public void setAttackLimit(int attackLimit) {
        this.attackLimit = attackLimit;
    }

    /**
     * @return the attackTimeout
     */
    public int getAttackTimeout() {
        return this.attackTimeout;
    }

    /**
     * @param attackTimeout the attackTimeout to set
     */
    public void setAttackTimeout(int attackTimeout) {
        this.attackTimeout = attackTimeout;
    }

    public scala.concurrent.duration.Duration toAttackTimeout() {
        return Duration.apply(this.attackTimeout, TimeUnit.MILLISECONDS);
    }

    /**
     * @return the checker
     */
    public CheckerType getChecker() {
        return this.checker;
    }

    /**
     * @param checker the checker to set
     */
    public void setChecker(CheckerType checker) {
        this.checker = checker;
    }

    /**
     * @return the crossoverSize
     */
    public int getCrossoverSize() {
        return this.crossoverSize;
    }

    /**
     * @param crossoverSize the crossoverSize to set
     */
    public void setCrossoverSize(int crossoverSize) {
        this.crossoverSize = crossoverSize;
    }

    /**
     * @return the heatRatio
     */
    public double getHeatRatio() {
        return this.heatRatio;
    }

    /**
     * @param heatRatio the heatRatio to set
     */
    public void setHeatRatio(double heatRatio) {
        this.heatRatio = heatRatio;
    }

    /**
     * @return the incubationLimit
     */
    public int getIncubationLimit() {
        return this.incubationLimit;
    }

    /**
     * @param incubationLimit the incubationLimit to set
     */
    public void setIncubationLimit(int incubationLimit) {
        this.incubationLimit = incubationLimit;
    }

    /**
     * @return the incubationTimeout
     */
    public int getIncubationTimeout() {
        return this.incubationTimeout;
    }

    /**
     * @param incubationTimeout the incubationTimeout to set
     */
    public void setIncubationTimeout(int incubationTimeout) {
        this.incubationTimeout = incubationTimeout;
    }

    public scala.concurrent.duration.Duration toIncubationTimeout() {
        return Duration.apply(this.incubationTimeout, TimeUnit.MILLISECONDS);
    }

    /**
     * @return the logger
     */
    public LoggerType getLogger() {
        return this.logger;
    }

    /**
     * @param logger the logger to set
     */
    public void setLogger(LoggerType logger) {
        this.logger = logger;
    }

    /**
     * @return the maxAttackStringSize
     */
    public int getMaxAttackStringSize() {
        return this.maxAttackStringSize;
    }

    /**
     * @param maxAttackStringSize the maxAttackStringSize to set
     */
    public void setMaxAttackStringSize(int maxAttackStringSize) {
        this.maxAttackStringSize = maxAttackStringSize;
    }

    /**
     * @return the maxDegree
     */
    public int getMaxDegree() {
        return this.maxDegree;
    }

    /**
     * @param maxDegree the maxDegree to set
     */
    public void setMaxDegree(int maxDegree) {
        this.maxDegree = maxDegree;
    }

    /**
     * @return the maxGeneStringSize
     */
    public int getMaxGeneStringSize() {
        return this.maxGeneStringSize;
    }

    /**
     * @param maxGeneStringSize the maxGeneStringSize to set
     */
    public void setMaxGeneStringSize(int maxGeneStringSize) {
        this.maxGeneStringSize = maxGeneStringSize;
    }

    /**
     * @return the maxGenerationSize
     */
    public int getMaxGenerationSize() {
        return this.maxGenerationSize;
    }

    /**
     * @param maxGenerationSize the maxGenerationSize to set
     */
    public void setMaxGenerationSize(int maxGenerationSize) {
        this.maxGenerationSize = maxGenerationSize;
    }

    /**
     * @return the maxInitialGenerationSize
     */
    public int getMaxInitialGenerationSize() {
        return this.maxInitialGenerationSize;
    }

    /**
     * @param maxInitialGenerationSize the maxInitialGenerationSize to set
     */
    public void setMaxInitialGenerationSize(int maxInitialGenerationSize) {
        this.maxInitialGenerationSize = maxInitialGenerationSize;
    }

    /**
     * @return the maxIteration
     */
    public int getMaxIteration() {
        return this.maxIteration;
    }

    /**
     * @param maxIteration the maxIteration to set
     */
    public void setMaxIteration(int maxIteration) {
        this.maxIteration = maxIteration;
    }

    /**
     * @return the maxNFASize
     */
    public int getMaxNFASize() {
        return this.maxNFASize;
    }

    /**
     * @param maxNFASize the maxNFASize to set
     */
    public void setMaxNFASize(int maxNFASize) {
        this.maxNFASize = maxNFASize;
    }

    /**
     * @return the maxPatternSize
     */
    public int getMaxPatternSize() {
        return this.maxPatternSize;
    }

    /**
     * @param maxPatternSize the maxPatternSize to set
     */
    public void setMaxPatternSize(int maxPatternSize) {
        this.maxPatternSize = maxPatternSize;
    }

    /**
     * @return the maxRecallStringSize
     */
    public int getMaxRecallStringSize() {
        return this.maxRecallStringSize;
    }

    /**
     * @param maxRecallStringSize the maxRecallStringSize to set
     */
    public void setMaxRecallStringSize(int maxRecallStringSize) {
        this.maxRecallStringSize = maxRecallStringSize;
    }

    /**
     * @return the maxRepeatCount
     */
    public int getMaxRepeatCount() {
        return this.maxRepeatCount;
    }

    /**
     * @param maxRepeatCount the maxRepeatCount to set
     */
    public void setMaxRepeatCount(int maxRepeatCount) {
        this.maxRepeatCount = maxRepeatCount;
    }

    /**
     * @return the maxSimpleRepeatCount
     */
    public int getMaxSimpleRepeatCount() {
        return this.maxSimpleRepeatCount;
    }

    /**
     * @param maxSimpleRepeatCount the maxSimpleRepeatCount to set
     */
    public void setMaxSimpleRepeatCount(int maxSimpleRepeatCount) {
        this.maxSimpleRepeatCount = maxSimpleRepeatCount;
    }

    /**
     * @return the mutationSize
     */
    public int getMutationSize() {
        return this.mutationSize;
    }

    /**
     * @param mutationSize the mutationSize to set
     */
    public void setMutationSize(int mutationSize) {
        this.mutationSize = mutationSize;
    }

    /**
     * @return the randomSeed
     */
    public long getRandomSeed() {
        return this.randomSeed;
    }

    /**
     * @param randomSeed the randomSeed to set
     */
    public void setRandomSeed(long randomSeed) {
        this.randomSeed = randomSeed;
    }

    /**
     * @return the recallLimit
     */
    public int getRecallLimit() {
        return this.recallLimit;
    }

    /**
     * @param recallLimit the recallLimit to set
     */
    public void setRecallLimit(int recallLimit) {
        this.recallLimit = recallLimit;
    }

    /**
     * @return the recallTimeout
     */
    public int getRecallTimeout() {
        return this.recallTimeout;
    }

    /**
     * @param recallTimeout the recallTimeout to set
     */
    public void setRecallTimeout(int recallTimeout) {
        this.recallTimeout = recallTimeout;
    }

    public scala.concurrent.duration.Duration toRecallTimeout() {
        return Duration.apply(this.recallTimeout, TimeUnit.MILLISECONDS);
    }

    /**
     * @return the seeder
     */
    public SeederType getSeeder() {
        return this.seeder;
    }

    /**
     * @param seeder the seeder to set
     */
    public void setSeeder(SeederType seeder) {
        this.seeder = seeder;
    }

    /**
     * @return the seedingLimit
     */
    public int getSeedingLimit() {
        return this.seedingLimit;
    }

    /**
     * @param seedingLimit the seedingLimit to set
     */
    public void setSeedingLimit(int seedingLimit) {
        this.seedingLimit = seedingLimit;
    }

    /**
     * @return the seedingTimeout
     */
    public int getSeedingTimeout() {
        return this.seedingTimeout;
    }

    /**
     * @param seedingTimeout the seedingTimeout to set
     */
    public void setSeedingTimeout(int seedingTimeout) {
        this.seedingTimeout = seedingTimeout;
    }

    public scala.concurrent.duration.Duration toSeedingTimeout() {
        return Duration.apply(this.seedingTimeout, TimeUnit.MILLISECONDS);
    }

    /**
     * @return the timeout
     */
    public int getTimeout() {
        return this.timeout;
    }

    /**
     * @param timeout the timeout to set
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public scala.concurrent.duration.Duration toTimeout() {
        return Duration.apply(this.timeout, TimeUnit.MILLISECONDS);
    }

    public void setProperty(ReDoSOption redosProperty) {
        this.accelerationMode = redosProperty.accelerationMode;
        this.attackLimit = redosProperty.attackLimit;
        this.attackTimeout = redosProperty.attackTimeout; // ms
        this.checker = redosProperty.checker;
        this.crossoverSize = redosProperty.crossoverSize;
        this.heatRatio = redosProperty.heatRatio;
        this.incubationLimit = redosProperty.incubationLimit;
        this.incubationTimeout = redosProperty.incubationTimeout; // ms
        this.logger = redosProperty.logger; // None
        this.maxAttackStringSize = redosProperty.maxAttackStringSize;
        this.maxDegree = redosProperty.maxDegree;
        this.maxGeneStringSize = redosProperty.maxGeneStringSize;
        this.maxGenerationSize = redosProperty.maxGenerationSize;
        this.maxInitialGenerationSize = redosProperty.maxInitialGenerationSize;
        this.maxIteration = redosProperty.maxIteration;
        this.maxNFASize = redosProperty.maxNFASize;
        this.maxPatternSize = redosProperty.maxPatternSize;
        this.maxRecallStringSize = redosProperty.maxRecallStringSize;
        this.maxRepeatCount = redosProperty.maxSimpleRepeatCount;
        this.maxSimpleRepeatCount = redosProperty.maxSimpleRepeatCount;
        this.mutationSize = redosProperty.mutationSize;
        this.randomSeed = redosProperty.randomSeed;
        this.recallLimit = redosProperty.recallLimit;
        this.recallTimeout = redosProperty.recallTimeout; // ms
        this.seeder = redosProperty.seeder;
        this.seedingLimit = redosProperty.seedingLimit;
        this.seedingTimeout = redosProperty.seedingTimeout; // ms
        this.timeout = redosProperty.timeout; // ms
    }

    public Option<codes.quine.labs.recheck.common.Context.Logger> getLogger(Writer writer) {
        return Option.empty();
    }

}
