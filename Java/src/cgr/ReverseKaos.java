package cgr;


import constraint.Constraints;
import constraint.mCGR;
import org.apache.commons.math3.fraction.BigFraction;

import java.io.*;
import java.math.BigInteger;

/**
 * ConstrainedKaos 05.11.19
 * Calculating Sequences from last CGR coordinates to DNA string
 *
 * @author Hannah Franziska Löchel
 */
public class ReverseKaos extends CGR {


    /**
     * Method to save codewords
     *
     * @param constraints Constraints object to be saved
     * @param path        Output path (as string)
     */
    public static void saveAsDNA(Constraints constraints, String path) {
        System.out.println("Starting to translate to DNA.");
        mCGR matrix = constraints.getMatrix();
        int len = matrix.getLen();
        int count = 0;
        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter(path));

            for (int key : matrix.getRows().keySet()) {

                BigFraction y = new BigFraction(new BigInteger(String.valueOf(len + 1 - key * 2)), (new BigInteger(String.valueOf(len))));

                for (int col : constraints.getMatrix().getRows().get(key)) {
                    BigFraction x = new BigFraction(new BigInteger(String.valueOf(len + 1 - col * 2)), (new BigInteger(String.valueOf(len))));
                    writer.write(">" + count + "\n");
                    writer.write(reverseKaos(x, y, constraints.getWordlength()) + "\n");
                    count++;
                }


            }

            writer.flush();
            writer.close();
            System.out.println("The translation is done, constrained DNA is saved in " + path);
        } catch (IOException e) {
            System.out.println("Can not find output path, your data wont be stored. ");
        }

    }

    /**
     * @param x      last x-coordinate of a CGR
     * @param y      last y-coordinate of a CGR
     * @param length sequence length
     */
    private static String reverseKaos(BigFraction x, BigFraction y, int length) {
        String sequence;
        BigFraction zx;
        BigFraction zy;
        BigFraction one = new BigFraction(1);
        BigFraction minusOne = new BigFraction(-1);
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {


            if (!isNegative(x) && !isNegative(y)) {
                stringBuilder.append("T");
                zx = one;
                zy = one;
            } else if (!isNegative(x) && isNegative(y)) {
                stringBuilder.append("C");
                zx = one;
                zy = minusOne;
            } else if (isNegative(x) && !isNegative(y)) {
                stringBuilder.append("A");
                zx = minusOne;
                zy = one;
            } else {
                stringBuilder.append("G");
                zx = minusOne;
                zy = minusOne;
            }

            x = x.multiply(new BigFraction(2)).subtract(zx);
            y = y.multiply(new BigFraction(2)).subtract(zy);

        }
        sequence = stringBuilder.toString();
        return sequence;
    }


}
