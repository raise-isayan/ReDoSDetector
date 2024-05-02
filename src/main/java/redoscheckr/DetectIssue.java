package redoscheckr;

import codes.quine.labs.recheck.diagnostics.Diagnostics;
import extension.helpers.StringUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import scala.collection.Iterator;

/**
 *
 * @author isayan
 */
public class DetectIssue {

    private DetectIssue() {
    }

    private ReDoSOption.FieldType[] fieldType;
    private ReDoSOption.StatusType status;
    private String hostspot;
    private final Map<ReDoSOption.FieldType, Object> map = new HashMap<>();

    static DetectIssue parseDiagnostics(Diagnostics diagnostics) {
        DetectIssue result = new DetectIssue();
        result.status = ReDoSOption.StatusType.valueOf(diagnostics.productPrefix().toUpperCase());
        List<String> nameList = new ArrayList<>();
        for (Iterator<String> ite = diagnostics.productElementNames(); ite.hasNext();) {
            nameList.add(ite.next());
        }
        result.fieldType = new ReDoSOption.FieldType[nameList.size()];
        for (int i = 0; i < nameList.size(); i++) {
            result.fieldType[i] = ReDoSOption.FieldType.parseEnum(nameList.get(i));
        }
        for (int i = 0; i < result.fieldType.length; i++) {
            System.out.println(result.fieldType[i] + ":" + diagnostics.productElement(i).getClass().getName());
            result.map.put(result.fieldType[i], diagnostics.productElement(i));
        }
        return result;
    }

    public ReDoSOption.StatusType getStatus() {
        return this.status;
    }

    public String getSource() {
        var source = map.get(ReDoSOption.FieldType.SOURCE);
        return StringUtil.toString(source);
    }

    public String getFlags() {
        var flags = map.get(ReDoSOption.FieldType.FLAGS);
        return StringUtil.toString(flags);
    }

    public ReDoSOption.CheckerType getChecker() {
        var checker = map.get(ReDoSOption.FieldType.CHECKER);
        return ReDoSOption.CheckerType.parseEnum(StringUtil.toString(checker));
    }

    public Optional<String> getAttack() {
        var attack = map.get(ReDoSOption.FieldType.ATTACK);
        if (attack != null) {
            return Optional.ofNullable(StringUtil.toString(attack));
        } else {
            return Optional.empty();
        }
    }

    public Optional<ReDoSOption.ComplexityType> getComplexity() {
        var complex = map.get(ReDoSOption.FieldType.COMPLEXITY);
        if (complex != null) {
            return Optional.of(ReDoSOption.ComplexityType.parseEnum(StringUtil.toString(complex)));
        } else {
            return Optional.empty();
        }
    }

    public Optional<HotSpot> getHotspot() {
        var hotspot = map.get(ReDoSOption.FieldType.HOTSPOT);
        if (hotspot != null) {
            if (hotspot instanceof codes.quine.labs.recheck.diagnostics.Hotspot hs) {
                return Optional.of(HotSpot.valueOf(hs));
            }
        }
        return Optional.empty();
    }

    public Optional<String> getLogger() {
        var error = map.get(ReDoSOption.FieldType.ERROR);
        if (error != null) {
            return Optional.of(StringUtil.toString(error));
        } else {
            return Optional.empty();
        }
    }

    public Optional<String> getError() {
        var error = map.get(ReDoSOption.FieldType.ERROR);
        if (error != null) {
            return Optional.of(StringUtil.toString(error));
        } else {
            return Optional.empty();
        }
    }

}
