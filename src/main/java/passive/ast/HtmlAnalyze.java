package passive.ast;

import extension.view.base.CaptureItem;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author isayan
 */
public class HtmlAnalyze {

    private final static Pattern SCRIPT_TAG = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    private final String input;

    public HtmlAnalyze(String input) {
        this.input = input;
    }

    public boolean analyze() {
        Matcher m = SCRIPT_TAG.matcher(input);
        while (m.find()) {
            CaptureItem item = new CaptureItem();
            item.setCaptureValue(m.group(1));
            item.setStart(m.start(1));
            item.setEnd(m.end(1));
            this.captureList.add(item);
        }
        return !this.captureList.isEmpty();
    }

    private final List<CaptureItem> captureList = new ArrayList<>();

    public List<CaptureItem> getCaputreList() {
        return this.captureList;
    }

}
