package de.uni_leipzig.er_bloom_eval.er.jaccard.bit_array.upper_bound;

import java.util.BitSet;

import org.apache.commons.lang3.tuple.Pair;

import de.uni_leipzig.er_bloom_eval.er.SimilarityFunction;

/**
 * Calculates the similarity between two {@link BitArrayUpperBoundPerson persons in the associated format} by
 * calculating the arithmetic mean of an upper bound of the compoment wise similarity.
 * <p>
 * In our setting this represents a really fast solution of our problem to deliver an upper bound of the exact similarity.
 * </p>
 *
 * @author m.froebe
 */
@SuppressWarnings("serial")
public class BitArrayUpperBoundPersonSimilarityFunction implements SimilarityFunction<BitArrayUpperBoundPerson>
{
	public static <V> double jaccard(Pair<Integer, BitSet> a, Pair<Integer, BitSet> b)
	{
		final BitSet union = (BitSet) a.getRight().clone();
		union.or(b.getRight());

		final double unionSize = union.cardinality();

		return ((((double) a.getLeft()) + ((double) b.getLeft())) - unionSize)/ unionSize;
	}

	@Override
	public double similarity(BitArrayUpperBoundPerson a, BitArrayUpperBoundPerson b)
	{
		return (jaccard(a.getAddress(), b.getAddress()) + jaccard(a.getBirthday(), b.getBirthday())
		+ jaccard(a.getName(), b.getName()) )/3d;
	}
}