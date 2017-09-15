package de.uni_leipzig.er_bloom_eval.er.jaccard.set;

import java.util.HashSet;
import java.util.Set;

import de.uni_leipzig.er_bloom_eval.er.SimilarityFunction;

/**
 * Calculates the similarity between two {@link SetPerson persons in the associated format} by
 * calculating the arithmetic mean of the jaccard index of the components of a person.
 * <p>
 * In our setting this represents the exact solution of our problem, but in a slow fashion.
 * </p>
 *
 * @author m.froebe
 */
@SuppressWarnings("serial")
public class SetPersonSimilarityFunction implements SimilarityFunction<SetPerson>
{
	public static <V> double jaccard(Set<V> a, Set<V> b)
	{
		final HashSet<V> union =  new HashSet<>(a);
		union.addAll(b);
		final double unionSize = union.size();

		return unionSize == 0 ? 0 : ((((double) a.size()) +((double) b.size())) - unionSize)/(unionSize);
	}

	@Override
	public double similarity(SetPerson a, SetPerson b)
	{
		return (jaccard(a.getAddressNGramms(), b.getAddressNGramms()) +
				jaccard(a.getBirthdayNgramms(), b.getBirthdayNgramms()) +
				jaccard(a.getNameNGramms(), b.getNameNGramms()))/3d;
	}
}