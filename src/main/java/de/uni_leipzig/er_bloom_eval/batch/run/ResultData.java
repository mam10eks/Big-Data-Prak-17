package de.uni_leipzig.er_bloom_eval.batch.run;

import net.sourceforge.argparse4j.inf.Namespace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import de.uni_leipzig.er_bloom_eval.util.ArgumentsUtil;

/**
 * Holds result data information
 */
public class ResultData {
    private Map<String, String> configuration;
    private int numberOfFalsePositive;
    private int numberOfFalseNegative;
    private String output;
    private int similarPairs;
    private int dataSize;
    private long runTime;

    public static Namespace getParsedConfiguration(ResultData resultData) {
        List<String> args = new ArrayList<>();

        for (Map.Entry<String, String> e : resultData.configuration.entrySet()) {
            args.add(e.getKey());

            if (e.getValue().contains(" ")) {
                args.addAll(Arrays.asList(e.getValue().split(Pattern.quote(" "))));
            } else {
                args.add(e.getValue());
            }
        }

        return ArgumentsUtil.parseArguments(args.toArray(new String[args.size()]));
    }


    /**
     * empty constructor
     */
    public ResultData() {
    }

    /* Getter + Setter */

    public Map<String, String> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Map<String, String> configuration) {
        this.configuration = configuration;
    }

    public int getSimilarPairs() {
        return similarPairs;
    }

    public void setSimilarPairs(int similarPairs) {
        this.similarPairs = similarPairs;
    }

    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    public long getRunTime() {
        return runTime;
    }

    public void setRunTime(long runTime) {
        this.runTime = runTime;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public int getNumberOfFalsePositive() {
        return numberOfFalsePositive;
    }

    public void setNumberOfFalsePositive(int numberOfFalsePositive) {
        this.numberOfFalsePositive = numberOfFalsePositive;
    }

    public int getNumberOfFalseNegative() {
        return numberOfFalseNegative;
    }

    public void setNumberOfFalseNegative(int numberOfFalseNegative) {
        this.numberOfFalseNegative = numberOfFalseNegative;
    }
}
