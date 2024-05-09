package redoscheckr;

import codes.quine.labs.recheck.ReDoS;
import codes.quine.labs.recheck.common.CancellationToken;
import codes.quine.labs.recheck.common.Context.Logger;
import codes.quine.labs.recheck.common.Parameters;
import codes.quine.labs.recheck.diagnostics.Diagnostics;
import java.util.Optional;
import scala.Option;

/**
 *
 * @author isayan
 */
public class ReDosDetector {

    public DetectIssue scan(String regvalue, String flags) {
        final ReDoSOption option = new ReDoSOption();
        return scan(regvalue, flags, option);
    }

    public DetectIssue scan(String regvalue, String flags, ReDoSOption option) {
        // https://makenowjust-labs.github.io/recheck/docs/usage/parameters/
        Option<Logger> logger = option.getLogger() == ReDoSOption.LoggerType.OFF ? Option.empty() : Option.empty();
        Parameters params = Parameters.apply(
                option.getAccelerationMode().toValue(), // accelerationMode
                option.getAttackLimit(), // attackLimit
                option.toAttackTimeout(), // attackTimeout
                option.getChecker().toValue(), // checker
                option.getCrossoverSize(), // crossoverSize
                option.getHeatRatio(), // heatRatio
                option.getIncubationLimit(), // incubationLimit
                option.toIncubationTimeout(), // incubationTimeout
                logger, // logger
                option.getMaxAttackStringSize(), // maxAttackStringSize
                option.getMaxDegree(), // maxDegree
                option.getMaxGeneStringSize(), // maxGeneStringSize
                option.getMaxGenerationSize(), // maxGenerationSize
                option.getMaxInitialGenerationSize(), // maxInitialGenerationSize
                option.getMaxIteration(), // maxIteration
                option.getMaxNFASize(), // maxNFASize
                option.getMaxPatternSize(), // maxPatternSize
                option.getMaxRecallStringSize(), // maxRecallStringSize
                option.getMaxRepeatCount(), // maxRepeatCount
                option.getMaxSimpleRepeatCount(), // maxSimpleRepeatCount
                option.getMutationSize(), // mutationSize
                option.getRandomSeed(), // randomSeed
                option.getRecallLimit(), // recallLimit
                option.toRecallTimeout(), // recallTimeout
                option.getSeeder().toValue(), // seeder
                option.getSeedingLimit(), // seedingLimit
                option.toSeedingTimeout(), // seedingTimeout
                option.toTimeout() // timeout
        );

        CancellationToken token = new CancellationToken();
        Diagnostics result = ReDoS.check(regvalue, flags, params, Option.apply(token));
        return DetectIssue.parseDiagnostics(result, Optional.empty());
    }

}
