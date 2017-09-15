package de.uni_leipzig.er_bloom_eval.batch.run;

import java.util.Arrays;

import de.uni_leipzig.er_bloom_eval.util.ArgumentsUtil;
import de.uni_leipzig.er_bloom_eval.util.bloom.PredefinedBloomFilter;

/**
 * Main class for batch run
 */
public class RunMain {
    /**
     * Main method
     * @param args arguments
     * @throws Exception error
     */
    public static void main(String[] args) throws Exception {

        RunConfig config = new RunConfig();
        config.setInputFolder("dummy_input_data");
        config.setOutPutFolder("output");
        config.setThresholds(new double[]{0.7 /*,0.5, 0.9*/});
        config.setInputSizes(Arrays.asList(10000, 100000, 25000));
        config.setBloomFilters(ArgumentsUtil.enumValues(PredefinedBloomFilter.class));
        config.setnGramSizes(new int[]{3 /*, 2, 4*/});
        config.setDateNGramSizes(new int[]{1 /*, 0, 2*/});
        config.setBloomfilterSizes(new int[]{128 /*, 320, 640, 960, 1280*/});

        Run run = new Run(config);
        run.run();
    }
}
