package de.uni_leipzig.er_bloom_eval.er.workflow;

import java.util.List;

import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_leipzig.er_bloom_eval.er.EntityResolution;
import de.uni_leipzig.er_bloom_eval.er.EntityResolutionStrategy;
import de.uni_leipzig.er_bloom_eval.util.io.CsvReader;
import de.uni_leipzig.er_bloom_eval.util.io.CsvWriter;
import de.uni_leipzig.er_bloom_eval.util.io.HasAssociatedPerson;
import de.uni_leipzig.er_bloom_eval.util.io.PersonTransformer;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * Represents a {@link Workflow entity resolution worklow} with a single entity resolution run.
 * <p>
 * I.e. this workflow works like:
 * <ol>
 * 		<li>Import and transform two files with persons for which the entity resolution should be performed from two csv files</li>
 * 		<li>Execute the entity resolution</li>
 * 		<li>Write the result (id pairs with similarities) in csf format to the result file</li>
 * <ol>
 * </p>
 *
 * @author m.froebe
 *
 */
public class SingleStageWorkflow extends Workflow
{
	private static final Logger LOGGER = LoggerFactory.getLogger(SingleStageWorkflow.class);

	public SingleStageWorkflow(Namespace parsedArgs)
	{
		super(parsedArgs);
	}

	@Override
	public <V extends HasAssociatedPerson> void executeWorkflow()
	{
		final EntityResolutionStrategy<V> entityResolutionStrategy = getEntityResolutionStrategy();
		final PersonTransformer<V> csvRecordTransformer = new PersonTransformer<>(getNGrammTransformer(), getDategrammTransformer(), entityResolutionStrategy.getDataStructureFactory());

		final CsvReader<V> csvReader = new CsvReader<>(csvRecordTransformer);

		LOGGER.info("Import data from csv-file '"+ getFirstInputFile() +"'  ...");
		final List<V> firstInput = csvReader.readCsvFromFile(getFirstInputFile());
		LOGGER.info("\tDone: imported "+ firstInput.size() +" records from '"+ getFirstInputFile() +"'.\n");

		LOGGER.info("Import data from csv-file '"+ getSecondInputFile() +"' ...");
		final List<V> secondInput = csvReader.readCsvFromFile(getSecondInputFile());
		LOGGER.info("\tDone: imported "+ secondInput.size() +" records from '"+ getSecondInputFile() +"'.\n");

		LOGGER.info("Start Entity-Resolution for imported data ...");

		final EntityResolution<V> entityResolution = new EntityResolution<>(entityResolutionStrategy, getThreshold());
		final List<Triple<Long, Long, Double>> results = entityResolution.calculateSimilarPairs(firstInput, secondInput);

		LOGGER.info("\tDone: found "+ results.size() +" matches between the imported datasets.\n");

		LOGGER.info("Writing results to file '"+ getOutputFile() +"' ...");
		CsvWriter.writeToFile(getOutputFile(), results);
		LOGGER.info("\tDone: written "+ results.size() +" records to '"+ getOutputFile() +"'.");
	}
}
