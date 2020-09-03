package cgr;

import org.apache.commons.math3.fraction.BigFraction;

/**
 * Interface for CGR and reverse CGR
 * rKaos  20.11.19
 * By Hannah Franziska Löchel
 */
public abstract class CGR {
    BigFraction[] xCoord;
    BigFraction[] yCoord;


    /**
     * @return x-coordinates
     */
    public BigFraction[] getXCoord() {
        return this.xCoord;
    }


    /**
     * @return y-coordinates
     */
    public BigFraction[] getYCoord() {
        return this.yCoord;
    }

    /**
     * reuturns -, if input is negative
     *
     * @param bigFraction input bigFraction
     * @return true for negative, false for positive
     */
    static boolean isNegative(BigFraction bigFraction) {
        char c = bigFraction.toString().toCharArray()[0];
        return c == '-';
    }

}
