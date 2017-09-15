package de.uni_leipzig.er_bloom_eval.er.evaluation;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * Stores the results of a compare
 */
public class CompareObject {
	private String fileName;
	private List<Pair<Long, Long>> falsePositives;
	private List<Pair<Long, Long>> falseNegatives;

	/**
	 * Empty constructor
	 */
	public CompareObject() {
	}

	/**
	 * Constructor
	 * @param fileName name of the file which is compared to the master
	 * @param falsePositives List<Pair<Long, Long>> of false positives
	 * @param falseNegatives List<Pair<Long, Long>> false negatives
	 */
	public CompareObject(String fileName, List<Pair<Long, Long>> falsePositives, List<Pair<Long, Long>> falseNegatives) {
		this.fileName = fileName;
		this.falsePositives = falsePositives;
		this.falseNegatives = falseNegatives;
	}

	/* Getter + Setter */

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<Pair<Long, Long>> getFalsePositives() {
		return falsePositives;
	}

	public void setFalsePositives(List<Pair<Long, Long>> falsePositives) {
		this.falsePositives = falsePositives;
	}

	public List<Pair<Long, Long>> getFalseNegatives() {
		return falseNegatives;
	}

	public void setFalseNegatives(List<Pair<Long, Long>> falseNegatives) {
		this.falseNegatives = falseNegatives;
	}
}
