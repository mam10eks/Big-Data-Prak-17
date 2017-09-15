package de.uni_leipzig.er_bloom_eval.er.jaccard.bit_array.upper_bound_and_sorted_set;

import de.uni_leipzig.er_bloom_eval.er.SimilarityFunction;
import de.uni_leipzig.er_bloom_eval.er.jaccard.bit_array.upper_bound.BitArrayUpperBoundPerson;
import de.uni_leipzig.er_bloom_eval.er.jaccard.bit_array.upper_bound.BitArrayUpperBoundPersonSimilarityFunction;
import de.uni_leipzig.er_bloom_eval.er.jaccard.sorted_set.SortedSetPersonSimilarityFunction;

/**
 * Calculates the similarity between two {@link BitArrayUpperBoundPerson persons in the associated format} by
 * calculating the arithmetic mean of an upper bound of the compoment wise similarity.
 * If this upper bound is above the similarity threshold (i.e. the pair could be similar), then the exact similarity is calculated
 * with the {@link SortedSetPersonSimilarityFunction sorted set approach}.
 * <p>
 * In our setting this represents a really fast solution of our problem to deliver the exact similarity of all similar pairs.
 * </p>
 *
 * @author m.froebe
 */
@SuppressWarnings("serial")
public class UpperBoundAndSortedSetPersonSimilarityFunction implements SimilarityFunction<UpperBoundAndSortedSetPerson>
{
	private static double _similarityThreshold;

	public static void setSimilarityThreshold(double similarityThreshold)
	{
		_similarityThreshold = similarityThreshold;
	}

	@Override
	public double similarity(UpperBoundAndSortedSetPerson a, UpperBoundAndSortedSetPerson b)
	{
		final double upperBoundSimilarity = (BitArrayUpperBoundPersonSimilarityFunction.jaccard(a.getAddressBloom(), b.getAddressBloom())
				+ BitArrayUpperBoundPersonSimilarityFunction.jaccard(a.getBirthdayBloom(), b.getBirthdayBloom())
				+ BitArrayUpperBoundPersonSimilarityFunction.jaccard(a.getNameBloom(), b.getNameBloom()) )/3d;

		if(upperBoundSimilarity < _similarityThreshold)
		{
			return upperBoundSimilarity;
		}

		return (SortedSetPersonSimilarityFunction.sortedJaccard(a.getSortedAddressNGrammsSortedSet(), b.getSortedAddressNGrammsSortedSet()) +
				SortedSetPersonSimilarityFunction.sortedJaccard(a.getSortedBirthdayNgrammsSortedSet(), b.getSortedBirthdayNgrammsSortedSet()) +
				SortedSetPersonSimilarityFunction.sortedJaccard(a.getSortedNameNGrammsSortedSet(), b.getSortedNameNGrammsSortedSet()))/3d;
	}
}