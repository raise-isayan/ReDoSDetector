package redoscheckr;

/**
 *
 * @author isayan
 */
public class Pumps {

    private final String prefix;

    private final String pump;

    private final int bias;

    public Pumps(String prefix, String repeat, int bias) {
        this.prefix = prefix;
        this.pump = repeat;
        this.bias = bias;
    }

    /**
     * @return the prefix
     */
    public String getPrefix() {
        return this.prefix;
    }

    /**
     * @return the pump
     */
    public String getPump() {
        return this.pump;
    }

    /**
     * @return the bias
     */
    public int getBias() {
        return this.bias;
    }
}
