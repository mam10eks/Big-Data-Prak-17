package de.uni_leipzig.er_bloom_eval.er.evaluation;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Get false positives and negatives from to result csv files
 */
public class Compare {
    /**
     * Empty constructor
     */
    public Compare() {
    }

    /**
     * Get all false positive and negative from two different csv files
     *
     * @param masterFilePath    path to the master csv file
     * @param toCompareFilePAth path to the csv file to compare with the master csv file
     * @return CompareObject
     * @throws IOException error
     */
    public CompareObject compareFiles(String masterFilePath, String toCompareFilePAth) throws IOException {
        final File masterFile = new File(masterFilePath);
        final File toCompareFile = new File(toCompareFilePAth);
        final List<Pair<Long, Long>> masterIds = getIds(readCSVFile(masterFile));

        if (!masterFile.isFile() || !toCompareFile.isFile()) {
            throw new IOException("Master file is no file!");
        }

        final String filename = FilenameUtils.getBaseName(toCompareFile.getName());
        final List<Pair<Long, Long>> fileIds = getIds(readCSVFile(toCompareFile));
        final List<Pair<Long, Long>> falsePositives = new ArrayList<>();
        final List<Pair<Long, Long>> falseNegatives = new ArrayList<>();

        for (Pair<Long, Long> fileId : fileIds) {
            if (!masterIds.contains(fileId)) {
                falsePositives.add(fileId);
            }
        }

        for (Pair<Long, Long> masterId : masterIds) {
            if (!fileIds.contains(masterId)) {
                falseNegatives.add(masterId);
            }
        }

        return new CompareObject(filename, falsePositives, falseNegatives);
    }

    /**
     * Get all false positive and negative from two different csv files in multiple folders
     *
     * @param inputDirPath   path where the input files are located
     * @param masterFilePath path to the master file
     * @return {List<CompareObject>}
     * @throws IOException error
     */
    public List<CompareObject> compareFolder(String inputDirPath, String masterFilePath) throws IOException {
        final File inputDirectory = new File(inputDirPath);
        final File masterFile = new File(masterFilePath);

        if (!inputDirectory.isDirectory()) {
            throw new IOException("Input Folder is no folder!");
        }

        if (!masterFile.isFile()) {
            throw new IOException("Master file is no file!");
        }

        final File[] files = inputDirectory.listFiles();
        final List<Pair<Long, Long>> masterIds = getIds(readCSVFile(masterFile));
        final List<CompareObject> compareObjects = new ArrayList<>();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && !file.equals(masterFile)) {
                    final String filename = FilenameUtils.getBaseName(file.getName());
                    final List<Pair<Long, Long>> fileIds = getIds(readCSVFile(file));
                    final List<Pair<Long, Long>> falsePositives = new ArrayList<>();
                    final List<Pair<Long, Long>> falseNegatives = new ArrayList<>();

                    for (Pair<Long, Long> fileId : fileIds) {
                        if (!masterIds.contains(fileId)) {
                            falsePositives.add(fileId);
                        }
                    }

                    for (Pair<Long, Long> masterId : masterIds) {
                        if (!fileIds.contains(masterId)) {
                            falseNegatives.add(masterId);
                        }
                    }

                    final CompareObject compareObject = new CompareObject(filename, falsePositives, falseNegatives);
                    compareObjects.add(compareObject);
                }
            }
        }

        return compareObjects;
    }

    /**
     * Get ids from a csv file and store them as a List of Pair.
     *
     * @param csvData csv data as string
     * @return {List<Pair<Long, Long>>}
     * @throws IOException error
     */
    private List<Pair<Long, Long>> getIds(String csvData) throws IOException {
        final List<CSVRecord> csvRecords = CSVParser.parse(csvData, CSVFormat.DEFAULT).getRecords();
        final List<Pair<Long, Long>> ids = new ArrayList<>();

        for (CSVRecord csvRecord : csvRecords) {
            ids.add(Pair.of(Long.valueOf(csvRecord.get(0)), Long.valueOf(csvRecord.get(1))));
        }
        return ids;
    }

    /**
     * Read a csv csvFile into a string.
     *
     * @param csvFile input csvFile
     * @return output string
     * @throws IOException error
     */
    private String readCSVFile(File csvFile) throws IOException {
        return FileUtils.readFileToString(csvFile, Charset.defaultCharset());
    }

    /**
     * Test main
     *
     * @param args args
     * @throws IOException error
     */
    public static void main(String[] args) throws IOException {
        final String workingDir = Paths.get("/home/me/Dev/Java/Test/data/result data").toString();
        final String fileToCompareTo = Paths.get(workingDir, "JACCARD_PARTITIONED_SORTED_N_GRAMM_SET.csv").toString();
        final Compare compare = new Compare();
        final List<CompareObject> compareObjects = compare.compareFolder(workingDir, fileToCompareTo);

        for (CompareObject compareObject : compareObjects) {
            final List<Pair<Long, Long>> falsePositives = compareObject.getFalsePositives();
            final List<Pair<Long, Long>> falseNegatives = compareObject.getFalseNegatives();

            System.out.println(compareObject.getFileName());
            System.out.print("False positives: " + falsePositives.size());
            System.out.print("\t\t[");

            for (int i = 0; i < falsePositives.size(); i++) {
                System.out.print("(" + falsePositives.get(i).getKey() + ", " + falsePositives.get(i).getValue() + ")");

                if (i < falsePositives.size() - 1) {
                    System.out.print(", ");
                }
            }

            System.out.println("]");
            System.out.print("False negatives: " + falseNegatives.size());
            System.out.print("\t\t[");

            for (int i = 0; i < falseNegatives.size(); i++) {
                System.out.print("(" + falseNegatives.get(i).getKey() + ", " + falseNegatives.get(i).getValue() + ")");

                if (i < falseNegatives.size() - 1) {
                    System.out.print(", ");
                }
            }

            System.out.println("]");
            System.out.println();
        }
    }
}
