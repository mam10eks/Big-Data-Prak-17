package de.uni_leipzig.er_bloom_eval.er.jaccard.sorted_set;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import de.uni_leipzig.er_bloom_eval.er.jaccard.bit_array.plain.PlainBitArrayPerson;
import de.uni_leipzig.er_bloom_eval.er.jaccard.set.SetPerson;
import de.uni_leipzig.er_bloom_eval.er.n_gramm.DateGramm;
import de.uni_leipzig.er_bloom_eval.er.n_gramm.NGrammTransformer;
import de.uni_leipzig.er_bloom_eval.util.io.HasAssociatedPerson;
import de.uni_leipzig.er_bloom_eval.util.io.InternalDataStructureFactory;

/**
 * Represents a {@link HasAssociatedPerson person} by representing all components of the person
 * (i.e. {@link NGrammTransformer ngramms} of the address and name and {@link DateGramm date-gramms} of the birthday)
 * as sorted sets (which are internally represented by arrays).
 * <p>
 * By leveraging this representation, the associated {@link SortedSetPersonSimilarityFunction similarity function of the sorted sets}
 * yields to an exact (but slow) solution representing the wanted arithmetic mean of the jaccard index of the components.
 * </p>
 * <p>
 * This solution is much faster then the approach leveraging {@link SetPerson plain sets}, but delivers the same result.
 * The Bit-Array approaches (e.g. {@link PlainBitArrayPerson}) are much faster.
 * </p>
 *
 * @author m.froebe
 */
@SuppressWarnings("serial")
public class SortedSetPerson implements HasAssociatedPerson
{
	public static final InternalDataStructureFactory<SortedSetPerson> FACTORY = new InternalDataStructureFactory<SortedSetPerson>()
	{
		@Override
		public SortedSetPerson apply(long id, Set<String> addressGramm, Set<Integer> birtdayGramm, Set<String> nameGramm)
		{
			return new SortedSetPerson(id, addressGramm, birtdayGramm, nameGramm);
		}
	};

	public static Integer[] toIntArray(List<Integer> a)
	{
		return a.toArray(new Integer[a.size()]);
	}

	public static <T extends Comparable<T>> List<T> toSortedList(Set<T> set)
	{
		final ArrayList<T> ret = new ArrayList<T>(set);
		Collections.sort(ret);

		return ret;
	}

	public static String[] toStrArray(List<String> a)
	{
		return a.toArray(new String[a.size()]);
	}

	private final long _id;

	private final String[] _addressNGramms;

	private final Integer[] _birthdayNGramms;

	private final String[] _nameNGramms;

	public SortedSetPerson(long id, Set<String> addressNGramms, Set<Integer> birthdayNGramms, Set<String> nameNGramms)
	{
		this._id = id;
		this._addressNGramms = toStrArray(toSortedList(addressNGramms));
		this._birthdayNGramms = toIntArray(toSortedList(birthdayNGramms));
		this._nameNGramms = toStrArray(toSortedList(nameNGramms));
	}

	@Override
	public long getId()
	{
		return this._id;
	}

	public String[] getSortedAddressNGramms()
	{
		return this._addressNGramms;
	}

	public Integer[] getSortedBirthdayNgramms()
	{
		return this._birthdayNGramms;
	}

	public String[] getSortedNameNGramms()
	{
		return this._nameNGramms;
	}
}