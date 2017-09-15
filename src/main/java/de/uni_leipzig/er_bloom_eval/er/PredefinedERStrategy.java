package de.uni_leipzig.er_bloom_eval.er;

import de.uni_leipzig.er_bloom_eval.er.jaccard.bit_array.plain.PlainBitArrayPerson;
import de.uni_leipzig.er_bloom_eval.er.jaccard.bit_array.plain.PlainBitArrayPersonSimilarityFunction;
import de.uni_leipzig.er_bloom_eval.er.jaccard.bit_array.upper_bound.BitArrayUpperBoundPerson;
import de.uni_leipzig.er_bloom_eval.er.jaccard.bit_array.upper_bound.BitArrayUpperBoundPersonSimilarityFunction;
import de.uni_leipzig.er_bloom_eval.er.jaccard.bit_array.upper_bound_and_sorted_set.UpperBoundAndSortedSetPerson;
import de.uni_leipzig.er_bloom_eval.er.jaccard.bit_array.upper_bound_and_sorted_set.UpperBoundAndSortedSetPersonSimilarityFunction;
import de.uni_leipzig.er_bloom_eval.er.jaccard.set.SetPerson;
import de.uni_leipzig.er_bloom_eval.er.jaccard.set.SetPersonSimilarityFunction;
import de.uni_leipzig.er_bloom_eval.er.jaccard.sorted_set.SortedSetPerson;
import de.uni_leipzig.er_bloom_eval.er.jaccard.sorted_set.SortedSetPersonSimilarityFunction;
import de.uni_leipzig.er_bloom_eval.er.workflow.TwoStageWorkflow;
import de.uni_leipzig.er_bloom_eval.er.workflow.Workflow;
import de.uni_leipzig.er_bloom_eval.util.ArgumentsUtil;
import de.uni_leipzig.er_bloom_eval.util.io.HasAssociatedPerson;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * A collection of {@link EntityResolutionStrategy entity resolution strategies} that
 * {@link PredefinedERStrategy#erStrategy() could be instantiated}.
 * <p>
 * This functionality is used to retrieve a entity resolution strategy for a {@link ArgumentsUtil user input}.
 * </p>
 *
 * @author Maik Fröbe
 */
public enum PredefinedERStrategy
{
	SET(SetErStrategy.class, "The exact -but slow- solution of our entity resolution problem with the jaccard index."),
	SORTED_SET(SortedSetErStrategy.class,"A faster -but exact- solution (then 'SET') of the entity resolution leveraging the idea of the sort-merge-join"),
	BIT_ARRAY(BitArrayErStrategy.class, "A really fast but -not exact- solution of the entity resolution calculating the similarity as and/or of the associated bit arrays."),
	BIT_ARRAY_AND_SORTED_SET(BitArrayAndSortedSetErStrategy.class, "Calculates the exact solution of the er problem in a really fast way. Therefore the upper bound of the "
			+ "exact result is calculated using bit arrays, and if that etimation is above the threshold the correct solution is calculated using the '"+ SORTED_SET +"' approach."),
	BIT_ARRAY_TWO_STAGE(BitArrayAndSortedSetTwoStageErStrategy.class, "Calculates the exact solution of the er problem in a really fast way. Therefore, it executes the entity resolution two times. "
			+ "In the first run, for all possible pairs the upper bound of the exact solution is calculated using bit arrays. "
			+ "Then a completely new entity resolution is started, where only for tuples which have an upper bound similarity above the threshold the similarity is caclulated using the '"+ SORTED_SET +"' approach."),
	BIT_ARRAY_LOWER_BOUND(BitArrayUpperBoundErStrategy.class, "A really fast (fastest) but -not exact- solution of the entity resolution calculating an upper bound of the exact similarity using bit arrays."
			+ "Should be seen as intermediate step for comming to the solutions '"+ BIT_ARRAY_AND_SORTED_SET +"' and '"+ BIT_ARRAY_TWO_STAGE +"'.");

	private static class BitArrayAndSortedSetErStrategy extends EntityResolutionStrategy<UpperBoundAndSortedSetPerson>
	{
		public BitArrayAndSortedSetErStrategy()
		{
			super(new UpperBoundAndSortedSetPersonSimilarityFunction(), UpperBoundAndSortedSetPerson.FACTORY);
		}

		@Override
		public void doPreparation(Namespace args)
		{
			UpperBoundAndSortedSetPersonSimilarityFunction.setSimilarityThreshold(args.getDouble(ArgumentsUtil.PARAM_SIMILARITY_THRESHOLD));
			PlainBitArrayPerson.setFactory(args);
			super.doPreparation(args);
		}
	}

	private static class BitArrayAndSortedSetTwoStageErStrategy extends BitArrayUpperBoundErStrategy
	{
		@SuppressWarnings("unused")
		public BitArrayAndSortedSetTwoStageErStrategy()
		{
			super();
		}

		@Override
		public Workflow getWorkflow(Namespace parsedArgs)
		{
			return new TwoStageWorkflow(parsedArgs);
		}
	}

	private static class BitArrayErStrategy extends EntityResolutionStrategy<PlainBitArrayPerson>
	{
		public BitArrayErStrategy()
		{
			super(new PlainBitArrayPersonSimilarityFunction(), PlainBitArrayPerson.FACTORY);
		}

		@Override
		public void doPreparation(Namespace args)
		{
			PlainBitArrayPerson.setFactory(args);
			super.doPreparation(args);
		}
	}

	private static class BitArrayUpperBoundErStrategy extends EntityResolutionStrategy<BitArrayUpperBoundPerson>
	{
		public BitArrayUpperBoundErStrategy()
		{
			super(new BitArrayUpperBoundPersonSimilarityFunction(), BitArrayUpperBoundPerson.FACTORY);
		}

		@Override
		public void doPreparation(Namespace args)
		{
			PlainBitArrayPerson.setFactory(args);
			super.doPreparation(args);
		}
	}

	private static class SetErStrategy extends EntityResolutionStrategy<SetPerson>
	{
		public SetErStrategy()
		{
			super(new SetPersonSimilarityFunction(), SetPerson.FACTORY);
		}
	}

	private static class SortedSetErStrategy extends EntityResolutionStrategy<SortedSetPerson>
	{
		public SortedSetErStrategy()
		{
			super(new SortedSetPersonSimilarityFunction(), SortedSetPerson.FACTORY);
		}
	}

	private final String _clazzName;

	private final String _description;

	/**
	 * @param clazz
	 * 		The class that will be instantiated from this enum.
	 * 		<p>
	 * 		Please be sure that this class has an public constructor without any arguments.
	 * 		</p>
	 * @param description
	 * 		The description given to the user.
	 */
	private <V extends HasAssociatedPerson> PredefinedERStrategy(Class<? extends EntityResolutionStrategy<V>> clazz, String description)
	{
		this._clazzName = clazz.getName();
		this._description = description;
	}

	@SuppressWarnings("unchecked")
	public <V extends HasAssociatedPerson> EntityResolutionStrategy<V> erStrategy()
	{
		try
		{
			return (EntityResolutionStrategy<V>) Class.forName(this._clazzName).newInstance();
		}
		catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
		{
			throw new RuntimeException("Couldnt resolve the Entity-Resolution-Strategy for the clazz: ''. Please be sure that the class has a public default constructor.");
		}
	}

	/**
	 * Retrieves a string that describes (documents) the approach for the user.
	 *
	 * @return
	 * 		The description for the user.
	 */
	public String getDescription()
	{
		return this._description;
	}
}