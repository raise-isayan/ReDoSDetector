package passive.signature;

import burp.api.montoya.collaborator.Interaction;
import burp.api.montoya.http.HttpService;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.scanner.AuditResult;
import burp.api.montoya.scanner.ScanCheck;
import burp.api.montoya.scanner.audit.issues.AuditIssue;
import burp.api.montoya.scanner.audit.issues.AuditIssueConfidence;
import burp.api.montoya.scanner.audit.issues.AuditIssueDefinition;
import burp.api.montoya.scanner.audit.issues.AuditIssueSeverity;
import extension.burp.Confidence;
import extension.burp.IBurpTab;
import extension.burp.IPropertyConfig;
import extension.burp.Severity;
import extension.burp.scanner.IssueItem;
import extension.burp.scanner.ScannerCheckAdapter;
import extension.burp.scanner.SignatureScanBase;
import extension.helpers.ConvertUtil;
import extension.helpers.HttpMessageWapper;
import extension.helpers.HttpResponseWapper;
import extension.helpers.HttpUtil;
import extension.helpers.SmartCodec;
import extension.helpers.json.JsonUtil;
import java.awt.Component;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import redoscheckr.AttackString;
import redoscheckr.DetectIssue;
import redoscheckr.HotSpot;
import redoscheckr.ReDoSOption;
import redoscheckr.ReDosDetector;

/**
 *
 * @author isayan
 */
public class ReDoSScan extends SignatureScanBase<ReDoSIssueItem> implements IBurpTab, IPropertyConfig {

    private final static Logger logger = Logger.getLogger(ReDoSScan.class.getName());

    public final static String SIGNATURE_PROPERTY = "ReDoSDetectorProperty";

    public ReDoSScan() {
        super("ReDoS Detector");
    }

    private final ReDoSDetectorTab tabReDoSDetectorTab = new ReDoSDetectorTab();

    private final ReDosDetector detect = new ReDosDetector();

    final static Pattern JS_PATTERNS[] = {
        Pattern.compile("\\Wnew\\s{1,6}RegExp\\(\\s{0,6}(?:\"(.*?)\"\\s*(?:,\\s{0,6}\"(d?g?i?m?s?u?v?y?)\"\\s{0,6})?)\\)"),
        Pattern.compile("\\Wnew\\s{1,6}RegExp\\((?:/(.*?)/(d?g?i?m?s?u?v?y?))\\)"),
        Pattern.compile("/(.*)/(d?g?i?m?s?u?v?y?)\\.(?:test|exec)\\(.*?\\)"),};

    final static Pattern VALIDATION_PATTERNS[] = {
        Pattern.compile("\\Wmatch the pattern(?: of)?: [\"\'`/]?(.*?)[\"\'`/]?\\s"),
        Pattern.compile("\\Wregular expression pattern: [\"\'`/]?(.*?)[\"\'`/]?\\s"),
        Pattern.compile("\\Wpattern [\"\'`/]?(.*?)[\"\'`/]?\\s"),};

    // https://www.php.net/regexp.reference.internal-options
    final static Pattern ERROR_PATTERNS[] = {
        Pattern.compile("\\WPattern.compile\\([\"](.*)[\"]\\)\\W"), // Java
        Pattern.compile("\\Wpreg_match\\([\"']/(.*)/(i?m?s?x?U?X?J?)[\"']\\)\\W"), // php
        Pattern.compile("\\Wpreg_replace\\([\"']/(.*)/(i?m?s?x?U?X?J?)[\"']\\)\\W"), // php
        Pattern.compile("\\Wpreg_all\\([\"']/(.*)/(i?m?s?x?U?X?J?)[\"']\\)\\W"), // php
    };

    @Override
    public ScanCheck passiveScanCheck() {
        return new ScannerCheckAdapter() {

            @Override
            public AuditResult passiveAudit(HttpRequestResponse baseRequestResponse) {
                List<AuditIssue> issues = new ArrayList<>();
                HttpResponseWapper wrapResponse = new HttpResponseWapper(baseRequestResponse.response());
                if (wrapResponse.hasHttpResponse() && wrapResponse.getBodyByte().length > 0) {
                    try {
                        if (wrapResponse.isContentMimeType(HttpMessageWapper.ContentMimeType.JAVA_SCRIPT)) {
                            for (int i = 0; i < ReDoSScan.JS_PATTERNS.length; i++) {
                                Matcher m = ReDoSScan.JS_PATTERNS[i].matcher(wrapResponse.getMessageString(wrapResponse.getGuessCharset(StandardCharsets.UTF_8.name())));
                                while (m.find()) {
                                    String regex = ConvertUtil.decodeJsLangQuote(m.group(1), false);
                                    String flags = m.group(2);
                                    DetectIssue result = detect.scan(regex, flags, this.getOption());
                                    if (result.getStatus() == ReDoSOption.StatusType.VULNERABLE) {
                                        List<ReDoSIssueItem> issueList = new ArrayList<>();
                                        ReDoSIssueItem issueItem = new ReDoSIssueItem();
                                        issueItem.setType("Client Side");
                                        issueItem.setMessageIsRequest(false);
                                        issueItem.setServerity(Severity.LOW);
                                        issueItem.setConfidence(Confidence.FIRM);
                                        issueItem.setCaptureValue(m.group(1));
                                        if (m.groupCount() >= 2) {
                                            issueItem.setFlgags(m.group(2));
                                        }
                                        issueItem.setStart(m.start(1));
                                        issueItem.setEnd(m.end(1));
                                        issueItem.setDetectIssue(result);
                                        issueList.add(issueItem);
                                        issues.add(makeScanIssue(baseRequestResponse, issueList));
                                    }
                                }
                            }
                        } else {
                            // validation error
                            for (int i = 0; i < ReDoSScan.VALIDATION_PATTERNS.length; i++) {
                                Matcher m = ReDoSScan.VALIDATION_PATTERNS[i].matcher(wrapResponse.getMessageString(wrapResponse.getGuessCharset(StandardCharsets.UTF_8.name())));
                                while (m.find()) {
                                    String regex = ConvertUtil.decodeJsonLiteral(m.group(1), false);
                                    String flags = "";
                                    DetectIssue result = detect.scan(regex, flags, this.getOption());
                                    if (result.getStatus() == ReDoSOption.StatusType.VULNERABLE) {
                                        List<ReDoSIssueItem> issueList = new ArrayList<>();
                                        ReDoSIssueItem issueItem = new ReDoSIssueItem();
                                        issueItem.setType("Server Side");
                                        issueItem.setMessageIsRequest(false);
                                        issueItem.setServerity(Severity.MEDIUM);
                                        issueItem.setConfidence(Confidence.FIRM);
                                        issueItem.setCaptureValue(m.group(1));
                                        if (m.groupCount() >= 2) {
                                            issueItem.setFlgags(m.group(2));
                                        }
                                        issueItem.setStart(m.start(1));
                                        issueItem.setEnd(m.end(1));
                                        issueItem.setDetectIssue(result);
                                        issueList.add(issueItem);
                                        issues.add(makeScanIssue(baseRequestResponse, issueList));
                                    }
                                }
                            }
                            // error code
                            for (int i = 0; i < ReDoSScan.ERROR_PATTERNS.length; i++) {
                                Matcher m = ReDoSScan.ERROR_PATTERNS[i].matcher(wrapResponse.getMessageString(wrapResponse.getGuessCharset(StandardCharsets.UTF_8.name())));
                                while (m.find()) {
                                    String regex = SmartCodec.toHtmlDecode(m.group(1), SmartCodec.ENCODE_PATTERN_ALPHANUM);
                                    String flags = m.group(2);
                                    DetectIssue result = detect.scan(regex, flags, this.getOption());
                                    if (result.getStatus() == ReDoSOption.StatusType.VULNERABLE) {
                                        List<ReDoSIssueItem> issueList = new ArrayList<>();
                                        ReDoSIssueItem issueItem = new ReDoSIssueItem();
                                        issueItem.setType("Server Side");
                                        issueItem.setMessageIsRequest(false);
                                        issueItem.setServerity(Severity.MEDIUM);
                                        issueItem.setConfidence(Confidence.FIRM);
                                        issueItem.setCaptureValue(m.group(1));
                                        if (m.groupCount() >= 2) {
                                            issueItem.setFlgags(m.group(2));
                                        }
                                        issueItem.setStart(m.start(1));
                                        issueItem.setEnd(m.end(1));
                                        issueItem.setDetectIssue(result);
                                        issueList.add(issueItem);
                                        issues.add(makeScanIssue(baseRequestResponse, issueList));
                                    }
                                }
                            }
                        }
                    } catch (UnsupportedEncodingException ex) {
                        logger.log(Level.SEVERE, ex.getMessage(), ex);
                    }
                }
                return AuditResult.auditResult(issues);
            }

            private ReDoSOption getOption() {
                return property;
            }
        };

    }

    public AuditIssue makeScanIssue(HttpRequestResponse messageInfo, List<ReDoSIssueItem> issueItems) {

        return new AuditIssue() {

            public IssueItem getItem() {
                if (issueItems.isEmpty()) {
                    return null;
                } else {
                    return issueItems.get(0);
                }
            }

            final String ISSUE_BACKGROUND = "\r\n"
                    + "<h4>ReDoS Detector:</h4>"
                    + "<ul>"
                    + "The Regular expression Denial of Service (ReDoS) is a Denial of Service attack"
                    + "</ul>"
                    + "<h4>Reference:</h4>"
                    + "<ul>"
                    + "  <li><a href=\"https://owasp.org/www-community/attacks/Regular_expression_Denial_of_Service_-_ReDoS\">https://owasp.org/www-community/attacks/Regular_expression_Denial_of_Service_-_ReDoS</a></li>"
                    + "  <li><a href=\"https://makenowjust-labs.github.io/recheck/\">https://makenowjust-labs.github.io/recheck/</a></li>"
                    + "</ul>";

            @Override
            public String name() {
                String issueName = getIssueName();
                if (!"".equals(getItem().getType())) {
                    issueName += " " + "(" + getItem().getType() + ")";
                }
                return issueName;
            }

            @Override
            public String detail() {
                StringBuilder detail = new StringBuilder();
                detail.append("<h4>ReDoS:</h4>");
                for (ReDoSIssueItem markItem : issueItems) {
                    DetectIssue detectIssue = markItem.getReDoSIssue();
                    detail.append(toDetectIssueLabel(detectIssue, false));
                }
                return detail.toString();
            }

            @Override
            public String remediation() {
                return null;
            }

            @Override
            public HttpService httpService() {
                return messageInfo.request().httpService();
            }

            @Override
            public String baseUrl() {
                return messageInfo.request().url();
            }

            @Override
            public AuditIssueSeverity severity() {
                IssueItem item = getItem();
                return item.getServerity().toAuditIssueSeverity();
            }

            @Override
            public AuditIssueConfidence confidence() {
                IssueItem item = getItem();
                return item.getConfidence().toAuditIssueConfidence();
            }

            @Override
            public List<HttpRequestResponse> requestResponses() {
                return Arrays.asList(messageInfo);
            }

            @Override
            public AuditIssueDefinition definition() {
                return AuditIssueDefinition.auditIssueDefinition(name(), ISSUE_BACKGROUND, remediation(), severity());
            }

            @Override
            public List<Interaction> collaboratorInteractions() {
                return new ArrayList<>();
            }
        };
    }

    public static String toStatusLabel(ReDoSOption.StatusType status) {
        String result = status.name().toLowerCase();
        switch (status) {
            case VULNERABLE:
                result = "<strong color='red'>" + HttpUtil.toHtmlEncode(status.name().toLowerCase()) + "</strong>";
                break;
        }
        return result;
    }

    public static String toDetectIssueLabel(DetectIssue issue, boolean fullTag) {
        StringBuilder detail = new StringBuilder();
        if (fullTag) {
            detail.append("<html>");
        }
        detail.append("<p><strong>Source:</strong></p>");
        detail.append("<ul>");
        detail.append(HttpUtil.toHtmlEncode(issue.getSource()));
        detail.append("</ul>");
        detail.append("<p><strong>Status:</strong></p>");
        detail.append("<ul>");
        detail.append(toStatusLabel(issue.getStatus()));
        detail.append("</ul>");
        detail.append("<p><strong>Checker:</strong></p>");
        detail.append("<ul>");
        detail.append(HttpUtil.toHtmlEncode(issue.getChecker().name().toLowerCase()));
        detail.append("</ul>");
        if (issue.getComplexity().isPresent()) {
            detail.append("<p><strong>Complexity:</strong></p>");
            detail.append("<ul>");
            detail.append(HttpUtil.toHtmlEncode(issue.getComplexity().get().name().toLowerCase()));
            detail.append("</ul>");
        }
        if (issue.getAttack().isPresent()) {
            AttackString attackString = issue.getAttack().get();
            detail.append("<p><strong>Attack String:</strong></p>");
            detail.append("<ul>");
            detail.append("<p><code>");
            detail.append(HttpUtil.toHtmlEncode(attackString.toString()));
            detail.append("</code></p>");
            detail.append("<p><code>");
            detail.append(HttpUtil.toHtmlEncode(attackString.getAsUString()));
            detail.append("</code></p>");
            detail.append("</ul>");
        }
        if (issue.getHotspot().isPresent()) {
            String source = issue.getSource();
            HotSpot hotsopt = issue.getHotspot().get();
            detail.append("<p><strong>Hotspot:</strong></p>");
            detail.append("<ul>");
            detail.append("<p><code>");
            List<HotSpot.Spot> sps = hotsopt.getSpotList();
            for (int i = 0; i < sps.size(); i++) {
                if (i == 0) {
                    detail.append(HttpUtil.toHtmlEncode(source.substring(0, sps.get(i).getStart())));
                }
                detail.append("<strong color='red'>");
                detail.append(HttpUtil.toHtmlEncode(source.substring(sps.get(i).getStart(), sps.get(i).getEnd())));
                detail.append("</strong>");

                if (i + 1 < sps.size()) {
                    detail.append(source.substring(sps.get(i).getEnd(), sps.get(i + 1).getStart()));
                }
                if (i + 1 == sps.size()) {
                    detail.append(HttpUtil.toHtmlEncode(source.substring(sps.get(i).getEnd())));
                }
            }
            detail.append("</code></p>");
            detail.append("</ul>");
        }
        if (issue.getLogger().isPresent()) {
            detail.append("<p><strong>Logger:</strong></p>");
            detail.append("<ul>");
            detail.append("<p><code>");
            detail.append(HttpUtil.toHtmlEncode(issue.getLogger().get()));
            detail.append("</code></p>");
            detail.append("</ul>");
        }
        if (issue.getError().isPresent()) {
            detail.append("<p><strong>Error:</strong></p>");
            detail.append("<ul>");
            detail.append("<p>");
            detail.append(HttpUtil.toHtmlEncode(issue.getError().get()));
            detail.append("</p>");
            detail.append("</ul>");
        }
        if (fullTag) {
            detail.append("</html>");
        }
        return detail.toString();
    }

    private final ReDoSOption property = new ReDoSOption();

    @Override
    public String getTabCaption() {
        return "ReDoSDetector";
    }

    @Override
    public Component getUiComponent() {
        return this.tabReDoSDetectorTab;
    }

    @Override
    public String getSettingName() {
        return SIGNATURE_PROPERTY;
    }

    @Override
    public void saveSetting(String value) {
        ReDoSOption redosProperty = JsonUtil.jsonFromString(value, ReDoSOption.class, true);
        this.property.setProperty(redosProperty);
        this.tabReDoSDetectorTab.setOption(this.property);
    }

    @Override
    public String loadSetting() {
        ReDoSOption redosProperty = this.tabReDoSDetectorTab.getOption();
        this.property.setProperty(redosProperty);
        return JsonUtil.jsonToString(this.property, true);
    }

    @Override
    public String defaultSetting() {
        ReDoSOption redosProperty = new ReDoSOption();
        return JsonUtil.jsonToString(redosProperty, true);
    }

}
