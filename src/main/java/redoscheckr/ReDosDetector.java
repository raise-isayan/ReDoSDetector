package redoscheckr;

import codes.quine.labs.recheck.ReDoS;
import codes.quine.labs.recheck.common.CancellationToken;
import codes.quine.labs.recheck.common.Context.Logger;
import codes.quine.labs.recheck.common.Parameters;
import codes.quine.labs.recheck.diagnostics.Diagnostics;
import com.google.gson.JsonObject;
import extension.helpers.json.JsonUtil;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.util.Arrays;
import java.util.Optional;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import passive.signature.ReDoSDetectorTab;
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

    private final static java.util.ResourceBundle BUNDLE = java.util.ResourceBundle.getBundle("burp/resources/release");

    public static void main(String[] args) {
        try {
            String regex_value = null;
            String flag_value = null;
            for (int i = 0; i < args.length; i += 2) {
                String[] param = Arrays.copyOfRange(args, i, args.length);
                if (param.length > 1) {
                    if ("-regex".equals(param[0])) {
                        regex_value = param[1];
                    }
                    if ("-flag".equals(param[0])) {
                        flag_value = param[1];
                    }
                } else if (param.length > 0) {
                    if ("-v".equals(param[0])) {
                        System.out.print("version: " + getVersion());
                        System.exit(0);
                    }
                    if ("-h".equals(param[0])) {
                        usage();
                        System.exit(0);
                    }
                    if ("-gui".equals(param[0])) {
                        EventQueue.invokeLater(MainPanel::createAndShowGui);
                    }
                } else {
                    throw new IllegalArgumentException("argment err:" + String.join(" ", param));
                }
            }

            // 必須チェック
            if (regex_value == null) {
                System.out.println("-regex argument err ");
                System.out.println("");
                usage();
                return;
            }
            if (flag_value == null) {
                flag_value = "";
            }

            ReDosDetector detect = new ReDosDetector();
            DetectIssue issue = detect.scan(regex_value, flag_value);
            JsonObject json = toJson(issue);
            System.out.println(JsonUtil.prettyJson(json, false));
        } catch (Exception ex) {
            String errmsg = String.format("%s: %s", ex.getClass().getName(), ex.getMessage());
            System.out.println(errmsg);
            usage();
        }
    }

    public static JsonObject toJson(DetectIssue issue) {
        JsonObject root = new JsonObject();
        root.addProperty("Source", issue.getSource());
        root.addProperty("Status", issue.getStatus().name());
        root.addProperty("Checker", issue.getChecker().name());
        if (issue.getComplexity().isPresent()) {
            root.addProperty("Complexity", issue.getComplexity().get().name().toLowerCase());
        }
        if (issue.getAttack().isPresent()) {
            AttackString attackString = issue.getAttack().get();
            root.addProperty("Attack String", attackString.toString());
        }
        if (issue.getHotspot().isPresent()) {
            String source = issue.getSource();
            root.addProperty("Hotspot", source);
        }
        if (issue.getError().isPresent()) {
            root.addProperty("Error", issue.getError().get());
        }
        return root;
    }

    private static void usage() {
        final String projname = BUNDLE.getString("projname");
        System.out.println("Usage:");
        System.out.println(String.format("\tjava -jar %s.jar -regex <pattern> -flag <flag> ", projname));
        System.out.println("Opton:");
        System.out.println("\t-gui GUI Interface");
        System.out.println("\t-v version");
        System.out.println("\t-h help");
    }

    public static String getProjectName() {
        return BUNDLE.getString("projname");
    }

    public static String getVersion() {
        return BUNDLE.getString("version");
    }

    private final static class MainPanel extends JPanel {

        private MainPanel() {
            super(new BorderLayout());
            ReDoSDetectorTab redosTab = new ReDoSDetectorTab();
            ReDoSOption option = new ReDoSOption();
            redosTab.setOption(option);
            this.add(redosTab, BorderLayout.CENTER);
            setPreferredSize(new Dimension(800, 800));
        }

        private static void createAndShowGui() {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                JFrame frame = new JFrame(getProjectName());
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.getContentPane().add(new MainPanel());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } catch (UnsupportedLookAndFeelException ignored) {
                Toolkit.getDefaultToolkit().beep();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
//                logger.log(Level.SEVERE, ex.getMessage(), ex);
                return;
            }
        }
    }

}
