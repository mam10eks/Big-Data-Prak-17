package de.uni_leipzig.er_bloom_eval.batch.run;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.codehaus.jackson.map.ObjectMapper;

import de.uni_leipzig.er_bloom_eval.er.PredefinedERStrategy;
import de.uni_leipzig.er_bloom_eval.er.evaluation.Compare;
import de.uni_leipzig.er_bloom_eval.er.evaluation.CompareObject;
import de.uni_leipzig.er_bloom_eval.util.ArgumentsUtil;

/**
 * Run different configurations in one try
 */
public class Run {
	private static final double DEFAULT_THRESHOLD = 0.7;
	private static final int DEFAULT_BLOOMFILTER_SIZE = 128;
	private static final int DEFAULT_N_GRAM_SIZE = 3;
	private static final int DEFAULT_DATE_RADIUS = 1;
	private final RunConfig runConfig;

	public Run(RunConfig runConfig) {
		this.runConfig = runConfig;
	}

	/**
	 * Run program with config
	 * @param config config
	 * @return console output
	 * @throws Exception error
	 */
	private String executeEntityResolution(Map<String, String> config) throws Exception {
		String paramString = "";

		for(final Entry<String, String> entry : config.entrySet())
		{
			paramString += " "+ entry.getKey() +" "+ entry.getValue();
		}

		final String exec = "java -jar target/er-bloom-eval-0.0.1-SNAPSHOT-jar-with-dependencies.jar "+ paramString;

		System.out.println("\n" + exec + "\n");
		final Process process = Runtime.getRuntime().exec(exec);

		// Get console output
		final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		final StringBuilder builder = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
			builder.append(System.getProperty("line.separator"));
		}

		if (process.waitFor() != 0) {
			throw new RuntimeException("Illegal responseCode");
		}

		return builder.toString();
	}

	/**
	 * Get the evaluation of an entity resolution
	 * @param master result data from "master"
	 * @param toCompare result data from file to compare to master
	 * @return enriched result data
	 * @throws IOException error
	 */
	private ResultData getEvaluation(ResultData master, ResultData toCompare) throws IOException {
		final String masterFilePath = master.getConfiguration().get("--" + ArgumentsUtil.PARAM_OUTPUT);
		final String toCompareFilePath = toCompare.getConfiguration().get("--" + ArgumentsUtil.PARAM_OUTPUT);
		final Compare compare = new Compare();
		final CompareObject compareObject = compare.compareFiles(masterFilePath, toCompareFilePath);
		toCompare.setNumberOfFalsePositive(compareObject.getFalsePositives().size());
		toCompare.setNumberOfFalseNegative(compareObject.getFalseNegatives().size());

		return toCompare;
	}

	/**
	 * Get length of file
	 * @param file path to file
	 * @return length
	 */
	private int getFileLineCount(String file) {
		try {
			return new String(Files.readAllBytes(Paths.get(file))).split(Pattern.quote("\n")).length;
		}
		catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Starts the batch run
	 * @throws Exception error
	 */
	public void run() throws Exception {
		final List<ResultData> resultDataList = new ArrayList<>();
		final File inputFolder = new File(this.runConfig.getInputFolder());
		double[] thresholds = {DEFAULT_THRESHOLD};
		int[] bloomFilterSizes = {DEFAULT_BLOOMFILTER_SIZE};
		int[] nGramSizes = {DEFAULT_N_GRAM_SIZE};
		int[] dateRadii = {DEFAULT_DATE_RADIUS};

		if ((this.runConfig.getThresholds() != null) && (this.runConfig.getThresholds().length > 0)) {
			thresholds = this.runConfig.getThresholds();
		}

		if ((this.runConfig.getBloomfilterSizes() != null) && (this.runConfig.getBloomfilterSizes().length > 0)) {
			bloomFilterSizes = this.runConfig.getBloomfilterSizes();
		}

		if ((this.runConfig.getnGramSizes() != null) && (this.runConfig.getnGramSizes().length > 0)) {
			nGramSizes = this.runConfig.getnGramSizes();
		}

		if ((this.runConfig.getDateNGramSizes() != null) && (this.runConfig.getDateNGramSizes().length > 0)) {
			dateRadii = this.runConfig.getDateNGramSizes();
		}

		if (inputFolder.isDirectory()) {
			final File[] subFolders = inputFolder.listFiles();

			for (final File subFolder : subFolders) {
				if (subFolder.isDirectory()) {
					for (final double threshold : thresholds) {
						for (final int nGramSize : nGramSizes) {
							for (final int dateRadius : dateRadii) {

								final Map<String, String> config = new LinkedHashMap<>();
								config.put("--" + ArgumentsUtil.PARAM_SIMILARITY_THRESHOLD, String.valueOf(threshold));
								config.put("--" + ArgumentsUtil.PARAM_N_GRAMM_LENGTH, String.valueOf(nGramSize));
								config.put("--" + ArgumentsUtil.PARAM_DATE_GRAMM_RADIUS, String.valueOf(dateRadius));

								final ResultData master = startEntityResolution(config, subFolder, PredefinedERStrategy.SET.name());

								if(master == null) {
									continue;
								}

								resultDataList.add(master);
								ResultData resultData = startEntityResolution(config, subFolder, PredefinedERStrategy.SORTED_SET.name());
								resultDataList.add(resultData);

								for(final String bloomFilter : this.runConfig.getBloomFilters()) {
									for (final int bloomFilterSize : bloomFilterSizes) {
										config.put("--" + ArgumentsUtil.PARAM_BITS_IN_BLOOM_FILTER, String.valueOf(bloomFilterSize));
										config.put("--" + ArgumentsUtil.PARAM_INTERNAL_BLOOM_FILTER, bloomFilter);

										for(final String bitArrayApproach : new String[]{PredefinedERStrategy.BIT_ARRAY.name(), PredefinedERStrategy.BIT_ARRAY_LOWER_BOUND.name(), PredefinedERStrategy.BIT_ARRAY_AND_SORTED_SET.name(), PredefinedERStrategy.BIT_ARRAY_TWO_STAGE.name()})
										{
											resultData = startEntityResolution(config, subFolder, bitArrayApproach);
											resultData = getEvaluation(master, resultData);
											resultDataList.add(resultData);
										}
									}
								}
							}
						}
					}
				}
			}
		}

		writeOutputJSON(resultDataList);
	}

	/**
	 * Run entity resolution with a given config
	 * @param config config
	 * @param folder folder which contains the two data sets as csv files
	 * @param method entity resolution method
	 * @return result
	 * @throws Exception error
	 */
	private ResultData startEntityResolution(Map<String, String> config, File folder, String method) throws Exception {
		final File[] inputFiles = folder.listFiles();

		File firstFile = null;
		File secondFile = null;

		for (final File inputFile : inputFiles) {
			if (inputFile.isFile() && FilenameUtils.getBaseName(inputFile.getName()).startsWith("org")) {
				firstFile = inputFile;
			} else if (inputFile.isFile() && FilenameUtils.getBaseName(inputFile.getName()).startsWith("dup")) {
				secondFile = inputFile;
			}
		}

		if ((firstFile != null) || (secondFile != null)) {
			final int datasetSize = getFileLineCount(firstFile.getAbsolutePath()) + getFileLineCount(secondFile.getAbsolutePath());

			if (Integer.parseInt(folder.getName()) != datasetSize) {
				throw new RuntimeException();
			}

			if(!this.runConfig.getInputSizes().contains(datasetSize)) {
				System.out.println("Dont execute dataset of size "+ datasetSize);
				return null;
			}

			String bloomFilterSize = "";

			if (config.get("--" + ArgumentsUtil.PARAM_BITS_IN_BLOOM_FILTER) != null) {
				bloomFilterSize = config.get("--" + ArgumentsUtil.PARAM_BITS_IN_BLOOM_FILTER);
			}

			final String outputPath = Paths.get(this.runConfig.getOutPutFolder(), String.valueOf(datasetSize)).toString();
			final String outputFile = Paths.get(outputPath,
					"OUT_" +
							method +
							"_" + config.get("--" + ArgumentsUtil.PARAM_SIMILARITY_THRESHOLD) +
							"_" + config.get("--" + ArgumentsUtil.PARAM_N_GRAMM_LENGTH) +
							"_" + config.get("--" + ArgumentsUtil.PARAM_DATE_GRAMM_RADIUS) +
							"_" + bloomFilterSize +
					".csv").toString();
			new File(outputPath).mkdirs();

			config = new LinkedHashMap<>(config);
			config.put("--" + ArgumentsUtil.PARAM_INPUT, firstFile + " " + secondFile);
			config.put("--" + ArgumentsUtil.PARAM_OUTPUT, outputFile);
			config.put("--" + ArgumentsUtil.PARAM_ER_STRATEGY, method);

			final long start = System.currentTimeMillis();
			final String consoleOutput = executeEntityResolution(config);
			final ResultData resultData = new ResultData();
			resultData.setOutput(consoleOutput);
			resultData.setConfiguration(config);
			resultData.setRunTime(System.currentTimeMillis() - start);
			resultData.setDataSize(datasetSize);

			return resultData;
		}

		return null;
	}

	/**
	 * Write results to json file
	 * @param resultDataList list of result data objects
	 * @throws IOException error
	 */
	private void writeOutputJSON(List<ResultData> resultDataList) throws IOException {
		final String outputFile = Paths.get(this.runConfig.getOutPutFolder(), "result.json").toString();
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(outputFile), resultDataList);
	}
}