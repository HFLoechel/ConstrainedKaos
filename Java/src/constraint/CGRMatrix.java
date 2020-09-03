package constraint;

import cgr.Kaos;
import org.apache.commons.math3.fraction.BigFraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * rKaos  19.06.20
 * Class to construct and resize CGR-matrix
 *
 * @author Hannah Franziska Löchel
 */

public class CGRMatrix {
    /**
     * CGR in HashMap structure
     */
    private HashMap<Integer, HashSet<Integer>> rows;
    /**
     * length of CGR
     */
    private int len;

    CGRMatrix() {
        this.rows = new HashMap<>();
        this.len = 0;
    }

    CGRMatrix(HashSet<String> constrains, int zoom) {
        this.len = (int) Math.pow(2, zoom);

        this.rows = new HashMap<>();

        for (String constrain : constrains) {
            Kaos kaos = new Kaos(constrain);
            int col = toMatrix(kaos.getxLast(), this.len);
            int row = toMatrix(kaos.getyLast(), this.len);
            if (this.rows.containsKey(row)) {
                HashSet<Integer> cols = this.rows.get(row);
                cols.add(col);
                this.rows.put(row, cols);
            } else {
                HashSet<Integer> cols = new HashSet<>();
                cols.add(col);
                this.rows.put(row, cols);
            }
        }

    }

    /**
     * Constructor for CGRMatrix with one constraint
     *
     * @param constrain as String
     */
    CGRMatrix(String constrain) {
        Kaos kaos = new Kaos(constrain);
        this.len = (int) Math.pow(2, constrain.length());
        int col = toMatrix(kaos.getxLast(), this.len);
        int row = toMatrix(kaos.getyLast(), this.len);
        rows = new HashMap<>();
        HashSet<Integer> cols = new HashSet<>();
        cols.add(col);
        rows.put(row, cols);
    }

    /**
     * Function to convert BigInteger fraction CGR to matrix coordinates
     *
     * @param last BigInteger Fraction
     * @param len  CGR Matrix length
     * @return matrix coordinate
     */
    private int toMatrix(BigFraction last, int len) {
        return (int) Math.ceil((last.doubleValue() + 1) * (len >> 1));
    }

    /**
     * Constructor for CGRMatrix of constrained homopolymers
     *
     * @param hp length of constrained homopolymers
     */
    CGRMatrix(int hp) {
        char[] con = new char[hp];
        Arrays.fill(con, 'a');
        String constrain = new String(con);
        Kaos kaos = new Kaos(constrain);
        this.len = (int) Math.pow(2, hp);
        int col = toMatrix(kaos.getxLast(), this.len);
        int row = toMatrix(kaos.getyLast(), this.len);
        int end = this.len;
        rows = new HashMap<>();
        HashSet<Integer> cols = new HashSet<>();
        cols.add(row);
        cols.add(col);
        rows.put(col, cols);
        rows.put(end, cols);
    }

    /**
     * Constructor for CGRMatrix of GC content, allowed sequences are stored, a fully allowed row is marked with -1
     *
     * @param gc GCContend object
     */
    CGRMatrix(GCContent gc) {
        HashSet<Integer> init = new HashSet<>();
        rows = new HashMap<>();
        init.add(-1);
        for (int element : gc.getGc()) {
            rows.put(element, init);
        }
        this.len = gc.getLen();
    }


    /**
     * Constructor for CGRMatrix
     *
     * @param matrix as HashMap of CGR with codewords or constraints
     * @param length size of CGR
     */
    CGRMatrix(HashMap<Integer, HashSet<Integer>> matrix, int length) {
        this.rows = matrix;
        this.len = length;
    }

    CGRMatrix doubleSize() {
        HashMap<Integer, HashSet<Integer>> d = new HashMap<>();


        for (int row : this.rows.keySet()) {
            int newRow = row * 2;
            int newRowNext = row * 2 - 1;
            HashSet<Integer> newColOne;
            newColOne = new HashSet<>();
            HashSet<Integer> newColTwo;
            newColTwo = new HashSet<>();
            HashSet<Integer> cols = this.rows.get(row);

            for (int col : cols) {
                newColOne.add(col * 2);
                newColOne.add(col * 2 - 1);
                newColTwo.add(col * 2);
                newColTwo.add(col * 2 - 1);
            }
            d.put(newRow, newColOne);
            d.put(newRowNext, newColTwo);
        }
        return new CGRMatrix(d, this.len * 2);
    }

    /**
     * Method to tile over the next iteration of CGR
     *
     * @param cgrMatrix CGRMatrix for tiling
     * @return changed CGRMatrix
     */
    CGRMatrix tiling(CGRMatrix cgrMatrix) {

        for (int row : cgrMatrix.rows.keySet()) {
            int newRow = row + cgrMatrix.len;
            HashSet<Integer> newColOne;

            if (rows.containsKey(row)) {
                newColOne = this.rows.get(row);
            } else {
                newColOne = new HashSet<>();
                this.rows.put(row, newColOne);
            }

            HashSet<Integer> newColTwo;

            if (this.rows.containsKey(newRow)) {
                newColTwo = this.rows.get(newRow);
            } else {
                newColTwo = new HashSet<>();
                this.rows.put(newRow, newColTwo);
            }

            HashSet<Integer> cols = cgrMatrix.rows.get(row);

            for (int col : cols) {
                newColOne.add(col);
                newColOne.add(col + cgrMatrix.len);
                newColTwo.add(col);
                newColTwo.add(col + cgrMatrix.len);
            }
        }


        return this;
    }


    /**
     * Returns he CGR Matrix as HashMap, where the keys are the rows (y-values) and the values are the columns stored in a HashSet (y-Values)
     *
     * @return CGR-Matrix as HashMap/sparsematrix
     */
    public HashMap<Integer, HashSet<Integer>> getRows() {
        return this.rows;
    }

    public CGRMatrix add(CGRMatrix CGRMatrix) {
        for (int row : CGRMatrix.rows.keySet()) {
            if (this.rows.containsKey(row)) {
                HashSet<Integer> col = this.rows.get(row);
                col.addAll(CGRMatrix.rows.get(row));
            } else {
                this.rows.put(row, CGRMatrix.rows.get(row));
            }
        }

        return this;
    }

    /**
     * Filters the allowed sequences of a CGRMatrix containing the forbidden sequences with a given gcContend
     *
     * @param gcContent to filter with
     * @return filterd CGR sparsematrix with codewords
     */
    CGRMatrix filter(GCContent gcContent) {
        HashMap<Integer, HashSet<Integer>> rows2 = new HashMap<>();

        for (int row : gcContent.getGc()) {
            HashSet<Integer> init = new HashSet<>();

            for (int i = 0; i < this.len; i++) {
                init.add(i + 1);
            }

            if (this.rows.containsKey(row)) {
                init.removeAll(this.rows.get(row));
            }

            rows2.put(row, init);
        }
        this.rows = rows2;
        return this;
    }

    /**
     * @return length of CGR sparsematrix
     */
    public int getLen() {
        return this.len;
    }

    void setLen(int len) {
        this.len = (int) Math.pow(2, len);
    }
}
