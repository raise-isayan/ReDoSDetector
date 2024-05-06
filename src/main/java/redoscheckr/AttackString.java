package redoscheckr;

/**
 *
 * @author isayan
 */
public class AttackString {

    private AttackString() {
    }

    private codes.quine.labs.recheck.diagnostics.AttackPattern scala;

    public static AttackString valueOf(codes.quine.labs.recheck.diagnostics.AttackPattern scala) {
        final AttackString value = new AttackString();
        value.scala = scala;
        return value;
    }

    /**
     * @return the fixedSize
     */
    public int getFixedSize() {
        return scala.fixedSize();
    }

    /**
     * @return the repeatSize
     */
    public int getRepeatSize() {
        return scala.repeatSize();
    }

    /**
     * @return the n
     */
    public int getRepeatCount() {
        return scala.n();
    }

    /**
     * @return the asUString
     */
    public String getAsUString() {
        return scala.asUString();
    }

    /**
     * @return the getSuffix
     */
    public String getSuffix() {
        return scala.suffix();
    }

    @Override
    public String toString() {
        return scala.toString();
    }
}
