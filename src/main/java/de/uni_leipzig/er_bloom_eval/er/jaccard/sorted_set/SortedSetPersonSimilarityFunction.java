package de.uni_leipzig.er_bloom_eval.er.jaccard.sorted_set;

import de.uni_leipzig.er_bloom_eval.er.SimilarityFunction;

/**
 * Calculates the similarity between two {@link SortedSetPerson persons in the associated format} by
 * calculating the arithmetic mean of the jaccard index of the components of a person.
 * <p>
 * In our setting this represents the exact solution of our problem, but in a slow fashion.
 * </p>
 *
 * @author m.froebe
 */
@SuppressWarnings("serial")
public class SortedSetPersonSimilarityFunction implements SimilarityFunction<SortedSetPerson>
{
	public static <T extends Comparable<T>> double sortedJaccard(T[] a, T[] b)
	{
		int overlap = 0,
				posInA = 0,
				posInB = 0;
		final int aSize = a.length, bSize = b.length;

		while((posInA < aSize) && (posInB < bSize))
		{
			final int aComparedToB = a[posInA].compareTo(b[posInB]);

			if(aComparedToB == 0)
			{
				posInA++;
				posInB++;
				overlap++;
			}
			else if( aComparedToB < 0)
			{
				posInA++;
			}
			else
			{
				posInB++;
			}
		}

		return (overlap)/((((double) aSize) +((double) bSize)) - (overlap));
	}

	@Override
	public double similarity(SortedSetPerson a, SortedSetPerson b)
	{
		return (sortedJaccard(a.getSortedAddressNGramms(), b.getSortedAddressNGramms()) +
				sortedJaccard(a.getSortedBirthdayNgramms(), b.getSortedBirthdayNgramms()) +
				sortedJaccard(a.getSortedNameNGramms(), b.getSortedNameNGramms()))/3d;
	}
}