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
    private Optional<String> log = Optional.empty();
    private final Map<ReDoSOption.FieldType, Object> map = new HashMap<>();

    static DetectIssue parseDiagnostics(Diagnostics diagnostics, Optional<String> log) {
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
            result.map.put(result.fieldType[i], diagnostics.productElement(i));
        }
        result.log = log;
        return result;
    }


    static DetectIssue parseDiagnostics(Diagnostics diagnostics) {
        return parseDiagnostics(diagnostics, Optional.empty());
    }

    public ReDoSOption.StatusType getStatus() {
        return this.status;
    }

    public String getSource() {
        var source = this.map.get(ReDoSOption.FieldType.SOURCE);
        return StringUtil.toString(source);
    }

    public String getFlags() {
        var flags = this.map.get(ReDoSOption.FieldType.FLAGS);
        return StringUtil.toString(flags);
    }

    public ReDoSOption.CheckerType getChecker() {
        var checker = this.map.get(ReDoSOption.FieldType.CHECKER);
        return ReDoSOption.CheckerType.parseEnum(StringUtil.toString(checker));
    }

    public Optional<AttackString> getAttack() {
        var attack = this.map.get(ReDoSOption.FieldType.ATTACK);
        if (attack instanceof codes.quine.labs.recheck.diagnostics.AttackPattern attackPattern) {
            AttackString attackString = AttackString.valueOf(attackPattern);
            return Optional.ofNullable(attackString);
        } else {
            return Optional.empty();
        }
    }

    public Optional<ReDoSOption.ComplexityType> getComplexity() {
        var complex = this.map.get(ReDoSOption.FieldType.COMPLEXITY);
        if (complex != null) {
            return Optional.of(ReDoSOption.ComplexityType.parseEnum(StringUtil.toString(complex)));
        } else {
            return Optional.empty();
        }
    }

    public Optional<HotSpot> getHotspot() {
        var hotspot = this.map.get(ReDoSOption.FieldType.HOTSPOT);
        if (hotspot != null) {
            if (hotspot instanceof codes.quine.labs.recheck.diagnostics.Hotspot hs) {
                return Optional.of(HotSpot.valueOf(hs));
            }
        }
        return Optional.empty();
    }

    public Optional<String> getLog() {
        return log;
    }

    public Optional<String> getError() {
        var error = this.map.get(ReDoSOption.FieldType.ERROR);
        if (error != null) {
            return Optional.of(StringUtil.toString(error));
        } else {
            return Optional.empty();
        }
    }

    public String debugStringClassName() {
        StringBuilder buff = new StringBuilder();
        for (int i = 0; i < this.fieldType.length; i++) {
            buff.append(this.fieldType[i]).append(":").append(this.map.get(fieldType[i]).getClass().getName());
            buff.append("\r\n");
        }
        return buff.toString();
    }

}
