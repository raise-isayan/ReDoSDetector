package redoscheckr;

import codes.quine.labs.recheck.unicode.UString;
import java.util.ArrayList;
import java.util.List;
import scala.collection.Iterator;
import scala.collection.immutable.Seq;

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

    public List<Pumps> getPumpList() {
        List<Pumps> pumps = new ArrayList<>();
        Seq<scala.Tuple3<codes.quine.labs.recheck.unicode.UString, codes.quine.labs.recheck.unicode.UString, Object>> seq = this.scala.pumps();
        for (Iterator<scala.Tuple3<codes.quine.labs.recheck.unicode.UString, codes.quine.labs.recheck.unicode.UString, Object>> ite = seq.iterator(); ite.hasNext();) {
            scala.Tuple3<UString, UString, Object> tuple = ite.next();
            pumps.add(new Pumps(tuple._1().asString(), tuple._2().asString(), (int)tuple._3()));
        }
        return pumps;
    }

    /**
     * @return the fixedSize
     */
    public int getFixedSize() {
        return this.scala.fixedSize();
    }

    /**
     * @return the repeatSize
     */
    public int getRepeatSize() {
        return this.scala.repeatSize();
    }

    /**
     * @return the n
     */
    public int getRepeatCount() {
        return this.scala.n();
    }

    /**
     * @return the asUString
     */
    public String getAsUString() {
        return this.scala.asUString();
    }

    /**
     * @return the getSuffix
     */
    public String getSuffix() {
        return this.scala.suffix();
    }

    @Override
    public String toString() {
        return this.scala.toString();
    }
}
