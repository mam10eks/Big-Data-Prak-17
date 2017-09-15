package de.uni_leipzig.er_bloom_eval.batch.run;

import java.util.List;

/**
 * Config for a batch run
 */
public class RunConfig {
	private double[] thresholds;
	private int[] bloomfilterSizes;
	private int[] nGramSizes;
	private int[] dateNGramSizes;
	private List<Integer> inputSizes;
	private String inputFolder;
	private String outPutFolder;
	private String[] methods;
	private String[] bloomFilters;

	/**
	 * Empty constructor
	 */
	public RunConfig() {
	}

	/* Getter + Setter */

	public double[] getThresholds() {
		return thresholds;
	}

	public void setThresholds(double[] thresholds) {
		this.thresholds = thresholds;
	}

	public int[] getBloomfilterSizes() {
		return bloomfilterSizes;
	}

	public void setBloomfilterSizes(int[] bloomfilterSizes) {
		this.bloomfilterSizes = bloomfilterSizes;
	}

	public List<Integer> getInputSizes() {
		return inputSizes;
	}


	public void setInputSizes(List<Integer> inputSizes) {
		this.inputSizes = inputSizes;
	}

	public int[] getnGramSizes() {
		return nGramSizes;
	}

	public void setnGramSizes(int[] nGramSizes) {
		this.nGramSizes = nGramSizes;
	}

	public int[] getDateNGramSizes() {
		return dateNGramSizes;
	}

	public void setDateNGramSizes(int[] dateNGramSizes) {
		this.dateNGramSizes = dateNGramSizes;
	}

	public String getInputFolder() {
		return inputFolder;
	}

	public void setInputFolder(String inputFolder) {
		this.inputFolder = inputFolder;
	}
	
	public void setBloomFilters(String[] filters) {
		this.bloomFilters = filters;
	}
	
	public String[] getBloomFilters() {
		return bloomFilters;
	}

	public String getOutPutFolder() {
		return outPutFolder;
	}

	public void setOutPutFolder(String outPutFolder) {
		this.outPutFolder = outPutFolder;
	}

	public String[] getMethods() {
		return methods;
	}

	public void setMethods(String[] methods) {
		this.methods = methods;
	}
}
