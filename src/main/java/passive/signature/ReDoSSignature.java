package passive.signature;

import extension.burp.Severity;
import extension.burp.scanner.SignatureItem;
import extension.burp.scanner.SignatureScanBase;

/**
 *
 * @author isayan
 */
public class ReDoSSignature extends SignatureItem {

    public ReDoSSignature(SignatureScanBase<? extends ReDoSIssueItem> item, Severity serverity) {
        super(item, serverity);
    }

    public ReDoSSignature() {
        this(new ReDoSScan(), Severity.MEDIUM);
    }

}
