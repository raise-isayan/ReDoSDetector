package passive.signature;

import extension.burp.scanner.IssueItem;
import redoscheckr.DetectIssue;

/**
 *
 * @author isayan
 */
public class ReDoSIssueItem extends IssueItem {

    private String flgags;

    /**
     * @return the flgags
     */
    public String getFlgags() {
        return flgags;
    }

    /**
     * @param flgags the flgags to set
     */
    public void setFlgags(String flgags) {
        this.flgags = flgags;
    }

    private DetectIssue detectIssue;

    /**
     * @return the detectIssue
     */
    public DetectIssue getReDoSIssue() {
        return detectIssue;
    }

    /**
     * @param detectIssue the detectIssue to set
     */
    public void setDetectIssue(DetectIssue detectIssue) {
        this.detectIssue = detectIssue;
    }

}
