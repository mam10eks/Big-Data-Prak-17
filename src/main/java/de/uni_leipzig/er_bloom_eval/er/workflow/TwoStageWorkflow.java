package de.uni_leipzig.er_bloom_eval.er.workflow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_leipzig.er_bloom_eval.er.EntityResolution;
import de.uni_leipzig.er_bloom_eval.er.EntityResolutionStrategy;
import de.uni_leipzig.er_bloom_eval.er.PredefinedERStrategy;
import de.uni_leipzig.er_bloom_eval.er.SimilarityFunction;
import de.uni_leipzig.er_bloom_eval.er.jaccard.sorted_set.SortedSetPerson;
import de.uni_leipzig.er_bloom_eval.er.jaccard.sorted_set.SortedSetPersonSimilarityFunction;
import de.uni_leipzig.er_bloom_eval.util.io.CsvReader;
import de.uni_leipzig.er_bloom_eval.util.io.CsvWriter;
import de.uni_leipzig.er_bloom_eval.util.io.HasAssociatedPerson;
import de.uni_leipzig.er_bloom_eval.util.io.PersonTransformer;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * Represents a {@link Workflow entity resolution worklow} with two entity resolution runs.
 * Thereby, the first run is executed in order to filter the pairs for which the exact similarity is to be measured in the second run.
 * <p>
 * A good match for the er strategy in the first stage is {@link PredefinedERStrategy#BIT_ARRAY_TWO_STAGE}
 * </p>
 *
 * <p>
 * I.e. this workflow works like:
 * <ol>
 * 		<li>Stage 1:</li>
 * 		<ol>
 * 			<li>Import and transform two files with persons for which the entity resolution should be performed from two csv files</li>
 * 			<li>Execute the entity resolution</li>
 * 			<li>Remember all pairs with a measured similarity above the threshold</li>
 * 		</ol>
 * 		<li>Stage 2:</li>
 * 		<ol>
 * 			<li>Import and transform the tow files with pearsons again (this time in a way that one could measure the exact similarity)</li>
 * 			<li>Calculate the exact similarity for all pairs</li>
 * 			<li>Write the result (id pairs with similarities) in csf format to the result file</li>
 * 		</ol>
 * <ol>
 * </p>
 *
 * @author m.froebe
 *
 */
public class TwoStageWorkflow extends Workflow
{
	private static final Logger LOGGER = LoggerFactory.getLogger(TwoStageWorkflow.class);

	static private <V extends HasAssociatedPerson> List<V> filterList(Set<Long> idsToHold, List<V> list)
	{
		final List<V> ret = new ArrayList<>();

		for(final V a : list)
		{
			if(idsToHold.contains(a.getId()))
			{
				ret.add(a);
			}
		}

		return ret;
	}

	public TwoStageWorkflow(Namespace parsedArgs)
	{
		super(parsedArgs);
	}

	@Override
	public <V extends HasAssociatedPerson> void executeWorkflow()
	{
		final double threshold = getThreshold();
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

		final EntityResolution<V> entityResolution = new EntityResolution<>(entityResolutionStrategy, threshold);
		final List<Triple<Long, Long, Double>> results = entityResolution.calculateSimilarPairs(firstInput, secondInput);

		LOGGER.info("\tDone: found "+ results.size() +" matches between the imported datasets.\n");

		final Set<Long> idsToHoldFromFirstInput = new HashSet<>();
		final Set<Long> idsToHoldFromSecondInput = new HashSet<>();
		final Map<Long, Set<Long>> pairsToCompare = new LinkedHashMap<>();
		final Map<Long, SortedSetPerson> idToPerson = new LinkedHashMap<>();

		for(final Triple<Long, Long, Double> toHold : results)
		{
			idsToHoldFromFirstInput.add(toHold.getLeft());
			idsToHoldFromSecondInput.add(toHold.getMiddle());

			if(!pairsToCompare.containsKey(toHold.getLeft()))
			{
				pairsToCompare.put(toHold.getLeft(), new HashSet<Long>());
			}

			pairsToCompare.get(toHold.getLeft()).add(toHold.getMiddle());
		}

		results.clear();

		final PersonTransformer<SortedSetPerson> secondStageRecordTransformer = new PersonTransformer<>(getNGrammTransformer(), getDategrammTransformer(), SortedSetPerson.FACTORY);

		final CsvReader<SortedSetPerson> secondCsvReader = new CsvReader<>(secondStageRecordTransformer);

		for(final SortedSetPerson person : filterList(idsToHoldFromFirstInput, secondCsvReader.readCsvFromFile(getFirstInputFile())))
		{
			idToPerson.put(Long.valueOf(person.getId()), person);
		}

		LOGGER.info("Import data from csv-file '"+ getSecondInputFile() +"' ...");
		for(final SortedSetPerson person : filterList(idsToHoldFromSecondInput, secondCsvReader.readCsvFromFile(getSecondInputFile())))
		{
			idToPerson.put(Long.valueOf(person.getId()), person);
		}

		final SimilarityFunction<SortedSetPerson> sim = new SortedSetPersonSimilarityFunction();

		for(final Entry<Long, Set<Long>> entry : pairsToCompare.entrySet())
		{
			final SortedSetPerson a = idToPerson.get(entry.getKey());

			for(final Long vvvv : entry.getValue())
			{
				final SortedSetPerson b = idToPerson.get(vvvv);
				final double s = sim.similarity(a, b);

				if(s >= threshold)
				{
					results.add(new ImmutableTriple<Long, Long, Double>(a.getId(), b.getId(), s));
				}
			}
		}

		LOGGER.info("Writing results to file '"+ getOutputFile() +"' ...");
		CsvWriter.writeToFile(getOutputFile(), results);
		LOGGER.info("\tDone: written "+ results.size() +" records to '"+ getOutputFile() +"'.");
	}
}
