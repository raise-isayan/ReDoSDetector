package redoscheckr;

import extension.helpers.StringUtil;
import java.util.ArrayList;
import java.util.List;
import scala.collection.Iterator;
import scala.collection.immutable.Seq;

/**
 *
 * @author isayan
 */
public class HotSpot {

    private HotSpot() {
    }

    public enum Temperature {
        HEAT(codes.quine.labs.recheck.diagnostics.Hotspot.Heat$.MODULE$),
        NORMAL(codes.quine.labs.recheck.diagnostics.Hotspot.Normal$.MODULE$);

        private final codes.quine.labs.recheck.diagnostics.Hotspot.Temperature scalar;

        Temperature(codes.quine.labs.recheck.diagnostics.Hotspot.Temperature scalar) {
            this.scalar = scalar;
        }

        public codes.quine.labs.recheck.diagnostics.Hotspot.Temperature toValue() {
            return this.scalar;
        }

        public static HotSpot.Temperature parseEnum(String s) {
            String value = s.toUpperCase();
            return Enum.valueOf(HotSpot.Temperature.class, value);
        }

    }

    public static class Spot {
        private final int start;
        private final int end;
        private final Temperature temperature;

        public Spot(int start, int end, Temperature temperature) {
            this.start = start;
            this.end = end;
            this.temperature = temperature;
        }

        /**
         * @return the start
         */
        public int getStart() {
            return this.start;
        }

        /**
         * @return the end
         */
        public int getEnd() {
            return this.end;
        }

        /**
         * @return the temperature
         */
        public Temperature getTemperature() {
            return this.temperature;
        }

        public static Spot valueOf(codes.quine.labs.recheck.diagnostics.Hotspot.Spot spot) {
            Spot value = new Spot(spot.start(), spot.end(), Temperature.parseEnum(StringUtil.toString(spot.temperature())));
            return value;
        }

        @Override
        public String toString() {
            StringBuilder buff = new StringBuilder();
            buff.append("start = ").append(this.start);
            buff.append(",");
            buff.append("end = ").append(this.end);
            buff.append(",");
            buff.append("temperature = ").append(this.temperature);
            return buff.toString();
        }

    }

    private final List<Spot> spots = new ArrayList<>();

    public List<Spot> getSpotList() {
        return this.spots;
    }

    public static HotSpot valueOf(codes.quine.labs.recheck.diagnostics.Hotspot hotsopt) {
        final HotSpot value = new HotSpot();
        Seq<codes.quine.labs.recheck.diagnostics.Hotspot.Spot> seq = hotsopt.spots();
        for (Iterator<codes.quine.labs.recheck.diagnostics.Hotspot.Spot> ite = seq.iterator(); ite.hasNext();) {
            codes.quine.labs.recheck.diagnostics.Hotspot.Spot s = ite.next();
            value.spots.add(Spot.valueOf(s));
        }
        return value;
    }

}
