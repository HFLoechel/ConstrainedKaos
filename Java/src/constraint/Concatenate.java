package constraint;


import java.util.HashMap;
import java.util.HashSet;


/**
 * ConstrainedKaos  10.12.20
 * Class for concatenation scheme
 *
 * @author Hannah Franziska Löchel
 */
public class Concatenate {
    private HashMap<String, HashSet<String>> concatenate;
    private HashMap<String, mCGR> prependingCGR;
    private HashMap<String, mCGR> appendingCGR;


    /**
     * @param length of code words
     * @param input  Input, containing constrained motifs
     */
    public Concatenate(int length, HashMap<Integer, HashSet<String>> input) {
        // key: prepending: value: appending
        this.concatenate = new HashMap<>();
        this.prependingCGR = new HashMap<>();
        this.appendingCGR = new HashMap<>();


        for (Integer i : input.keySet()) {
            for (String s : input.get(i)) {


                for (int j = 1; j < s.length(); j++) {
                    String prepending = s.substring(0, j);
                    String appending = s.substring(j);
                    //dismiss subsequences combinations longer then codeword length
                    if (prepending.length() <= length && appending.length() <= length) {
                        if (this.concatenate.containsKey(prepending)) {
                            HashSet<String> l = this.concatenate.get(prepending);
                            l.add(appending);
                        } else {
                            HashSet<String> l = new HashSet<>();
                            l.add(appending);
                            this.concatenate.put(prepending, l);
                        }
                    } //else {
                       // System.out.println(prepending + " " + appending);
                   // }
                }
            }


        }

        for (String pre : concatenate.keySet()) {

            mCGR preC = new mCGR(pre);

            //spot endings
            for (int i = pre.length(); i < length; i++) {
                preC = preC.doubleSize();
            }


            //Start of motif, end of codeword
            prependingCGR.put(pre, preC);
            for (String ap : concatenate.get(pre)
            ) {
                if (!appendingCGR.containsKey(ap)) {
                    mCGR empty = new mCGR(ap);
                    mCGR apC = new mCGR();
                    apC.setLen(length);
                    apC.spotStart(empty);


                    //end of motif, start of next codeword
                    appendingCGR.put(ap, apC);
                }
            }
        }


    }

    /**
     * returns concatenation scheme for motifs
     * @return HashMap with motifs key: prefix (ending of codeword), value: postfix (start of concatenated codeword)
     */
    public HashMap<String, HashSet<String>> getScheme() {
        return this.concatenate;
    }

    /**
     * returns mCGRs for postfix of motif
     * @return HashMap with mCGRs key: postfix (ending of motif), value: mCGR (position of codewords starting with postfix)
     */
    public HashMap<String, mCGR> getAppending() {
        return this.appendingCGR;
    }
    /**
     * returns mCGRs for prefix of motif
     * @return HashMap with mCGRs key: prefix (start of motif), value: mCGR (position of codewords ending with prefix)
     */
    public HashMap<String, mCGR> getPrepending() {
        return this.prependingCGR;
    }


}
