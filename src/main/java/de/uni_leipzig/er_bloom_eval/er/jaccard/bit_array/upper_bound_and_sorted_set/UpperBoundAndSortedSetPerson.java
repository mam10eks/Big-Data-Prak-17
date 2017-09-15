package de.uni_leipzig.er_bloom_eval.er.jaccard.bit_array.upper_bound_and_sorted_set;

import java.util.BitSet;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import de.uni_leipzig.er_bloom_eval.er.jaccard.bit_array.plain.PlainBitArrayPerson;
import de.uni_leipzig.er_bloom_eval.er.jaccard.bit_array.upper_bound.BitArrayUpperBoundPersonSimilarityFunction;
import de.uni_leipzig.er_bloom_eval.er.jaccard.sorted_set.SortedSetPerson;
import de.uni_leipzig.er_bloom_eval.er.jaccard.sorted_set.SortedSetPersonSimilarityFunction;
import de.uni_leipzig.er_bloom_eval.er.n_gramm.DateGramm;
import de.uni_leipzig.er_bloom_eval.er.n_gramm.NGrammTransformer;
import de.uni_leipzig.er_bloom_eval.util.io.HasAssociatedPerson;
import de.uni_leipzig.er_bloom_eval.util.io.InternalDataStructureFactory;

/**
 * Represents a {@link HasAssociatedPerson person} by representing all components of the person
 * (i.e. {@link NGrammTransformer ngramms} of the address and name and {@link DateGramm date-gramms} of the birthday) in two different ways:
 * <ul>
 * 		<li>An pair consisting of an bit array and an integer representing the original amount of elements in the original set</li>
 * 		<li>A sorted set obtained by the original set</li>
 * </ul>
 *
 * <p>
 * By leveraging this representation, the associated {@link UpperBoundAndSortedSetPersonSimilarityFunction similarity function}
 * is able to obtain the exact correct result (like with the {@link SortedSetPersonSimilarityFunction sorted set approach}) but in a much faster way.
 * This is done by calculating the exact result only if the upper bound estimated by the bit array filter (like in {@link BitArrayUpperBoundPersonSimilarityFunction the bit array upper bound approach})
 * is above the threshold.
 * </p>
 *
 * @author Maik Fröbe
 */
@SuppressWarnings("serial")
public class UpperBoundAndSortedSetPerson implements HasAssociatedPerson
{
	public static final InternalDataStructureFactory<UpperBoundAndSortedSetPerson> FACTORY = new InternalDataStructureFactory<UpperBoundAndSortedSetPerson>()
	{
		@Override
		public UpperBoundAndSortedSetPerson apply(long id, Set<String> addressGramm, Set<Integer> birtdayGramm, Set<String> nameGramm)
		{
			return new UpperBoundAndSortedSetPerson(id,

					//address
					new ImmutablePair<>(addressGramm.size(), PlainBitArrayPerson.toBloomFilter(addressGramm)),
					SortedSetPerson.toStrArray(SortedSetPerson.toSortedList(addressGramm)),

					//birthday
					new ImmutablePair<>(birtdayGramm.size(), PlainBitArrayPerson.toBloomFilter(birtdayGramm)),
					SortedSetPerson.toIntArray(SortedSetPerson.toSortedList(birtdayGramm)),

					//name
					new ImmutablePair<>(nameGramm.size(), PlainBitArrayPerson.toBloomFilter(nameGramm)),
					SortedSetPerson.toStrArray(SortedSetPerson.toSortedList(nameGramm)));
		}
	};

	private final long _id;

	private final Pair<Integer, BitSet> _addressBloom;

	private final Pair<Integer, BitSet> _birthdayBloom;

	private final Pair<Integer, BitSet> _nameBloom;

	private final String[] _addressNGrammsSortedSet;

	private final Integer[] _birthdayNGrammsSortedSet;

	private final String[] _nameNGrammsSortedSet;

	public UpperBoundAndSortedSetPerson(long id, Pair<Integer, BitSet> address, String[] addressNGrammsSortedSet, Pair<Integer, BitSet> birthday, Integer[] birthdayNGrammsSortedSet, Pair<Integer, BitSet> name, String[] nameNGrammsSortedSet)
	{
		this._id = id;
		this._addressBloom = address;
		this._birthdayBloom = birthday;
		this._nameBloom = name;
		this._addressNGrammsSortedSet = addressNGrammsSortedSet;
		this._birthdayNGrammsSortedSet = birthdayNGrammsSortedSet;
		this._nameNGrammsSortedSet = nameNGrammsSortedSet;
	}

	public Pair<Integer, BitSet> getAddressBloom()
	{
		return this._addressBloom;
	}

	public Pair<Integer, BitSet> getBirthdayBloom()
	{
		return this._birthdayBloom;
	}

	@Override
	public long getId()
	{
		return this._id;
	}

	public Pair<Integer, BitSet> getNameBloom()
	{
		return this._nameBloom;
	}

	public String[] getSortedAddressNGrammsSortedSet()
	{
		return this._addressNGrammsSortedSet;
	}

	public Integer[] getSortedBirthdayNgrammsSortedSet()
	{
		return this._birthdayNGrammsSortedSet;
	}

	public String[] getSortedNameNGrammsSortedSet()
	{
		return this._nameNGrammsSortedSet;
	}
}