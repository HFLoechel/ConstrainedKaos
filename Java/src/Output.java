import cgr.ReverseKaos;
import constraint.Concatenate;
import constraint.mCGR;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

/**
 * ConstrainedKaos  10.12.20
 * Class to write output files
 *
 * @author Hannah Franziska Löchel
 */
public class Output {

    /**
     * @param reverseKaos sequences from mCGR
     * @param concatenate concatenation schema
     * @param fileOutput  output file
     */
    public Output(ReverseKaos reverseKaos, Concatenate concatenate, String fileOutput) {

        reverseKaos.saveAsDNA(fileOutput);
        String pathCon = fileOutput + "-concatenate.json";
        String pathMCGR = fileOutput + "-mCGR.json";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(pathCon));
            writer.write("{ \"motif\" :");
            String motifs = buildJson(concatenate.getScheme());
            writer.write(motifs);
            writer.write(", ");
            writer.write(" \"prepending(row:col)\" :");
            writer.write(buildJsonmCGR(concatenate.getPrepending()));
            writer.write(", ");
            writer.write(" \"appending(row:col)\" :");
            writer.write(buildJsonmCGR(concatenate.getAppending()));
            writer.write("}");
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(pathMCGR));
            writer.write("{ \"mCGR(row,col)\" :");
            writer.write(buildJsonSequences(reverseKaos));
            writer.write("}");
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("The concatenations scheme is saved in " + pathCon);
        System.out.println("The mCGR is saved in " + pathMCGR);

    }

    /**
     * @param reverseKaos sequences from mCGR
     * @param fileOutput  output file
     */
    public Output(ReverseKaos reverseKaos, String fileOutput) {
        reverseKaos.saveAsDNA(fileOutput);
    }

    private String buildJsonSequences(ReverseKaos reverseKaos) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{ ");
        int i = 1;
        for (String sequence : reverseKaos.getSequences().keySet()) {
            stringBuilder.append("\"").append(sequence).append("\"").append(" :[");
            int row = reverseKaos.getRow(sequence);

            int col = reverseKaos.getCol(sequence);
            stringBuilder.append(row).append(",").append(col);
            stringBuilder.append("]");
            if (i < reverseKaos.getSequences().size()) {
                stringBuilder.append(", ");
            }
            i++;
        }
        stringBuilder.append("}");
        return (stringBuilder.toString());
    }


    /**
     * @param mCGRs prepending or appending HashMap with corresponding mCGRs
     * @return String in json format
     */

    private String buildJsonmCGR(HashMap<String, mCGR> mCGRs) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{ ");

        int k = 1;
        for (String motif : mCGRs.keySet()) {
            stringBuilder.append("\"").append(motif.toUpperCase()).append("\"").append(" :{ ");

            int i = 1;
            for (int row : mCGRs.get(motif).getRows().keySet()) {
                stringBuilder.append("\"").append(row).append("\"").append(" :[");
                int j = 1;
                for (int col : mCGRs.get(motif).getRows().get(row)) {
                    stringBuilder.append(col);
                    if (j < mCGRs.get(motif).getRows().get(row).size()) {
                        stringBuilder.append(",");
                    }


                    j++;
                }
                stringBuilder.append("]");
                if (i < mCGRs.get(motif).getRows().keySet().size()) {
                    stringBuilder.append(", ");
                }


                i++;
            }
            stringBuilder.append("}");
            if (k < mCGRs.keySet().size()) {
                stringBuilder.append(", ");
            }

            k++;

        }
        stringBuilder.append("}");

        return (stringBuilder.toString());

    }


    /**
     * @param concatenate HashMap with subsequences of motifs
     * @return String in json format
     */
    private String buildJson(HashMap<String, HashSet<String>> concatenate) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{ ");
        int i = 1;
        for (String motifPre : concatenate.keySet()) {
            stringBuilder.append("\"").append(motifPre.toUpperCase()).append("\"").append(" :[");
            int j = 1;
            for (String motifPost : concatenate.get(motifPre)) {
                stringBuilder.append("\"").append(motifPost.toUpperCase()).append("\"");
                if (j < concatenate.get(motifPre).size()) {
                    stringBuilder.append(",");
                }


                j++;
            }
            stringBuilder.append("]");
            if (i < concatenate.size()) {
                stringBuilder.append(", ");
            }
            i++;
        }
        stringBuilder.append("}");
        return (stringBuilder.toString());


    }

}
