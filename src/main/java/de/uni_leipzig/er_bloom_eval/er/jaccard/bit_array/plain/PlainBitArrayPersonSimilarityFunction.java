package de.uni_leipzig.er_bloom_eval.er.jaccard.bit_array.plain;

import java.util.BitSet;

import de.uni_leipzig.er_bloom_eval.er.SimilarityFunction;

/**
 * Calculates the similarity between two {@link PlainBitArrayPerson persons in the associated format} by
 * calculating the arithmetic mean of the quotient of the intersection and union (i.e. an approximation of the jaccard index) of
 * of the bit arrays of a person.
 * <p>
 * In our setting this represents a really fast solution of our problem, but not correct.
 * </p>
 *
 * @author m.froebe
 */
@SuppressWarnings("serial")
public class PlainBitArrayPersonSimilarityFunction implements SimilarityFunction<PlainBitArrayPerson>
{
	public static <V> double jaccard(BitSet a, BitSet b)
	{
		final BitSet union = (BitSet) a.clone();
		final BitSet intersection = (BitSet) a.clone();

		union.or(b);

		intersection.and(b);

		return ((double)intersection.cardinality())/((double) union.cardinality());
	}

	@Override
	public double similarity(PlainBitArrayPerson a, PlainBitArrayPerson b)
	{
		return (jaccard(a.getAddressBloom(), b.getAddressBloom()) + jaccard(a.getBirthdayBloom(), b.getBirthdayBloom())
		+ jaccard(a.getNameBloom(), b.getNameBloom()) )/3d;
	}
}