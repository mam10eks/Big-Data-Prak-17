package de.uni_leipzig.er_bloom_eval.er.jaccard.bit_array.upper_bound;

import java.util.BitSet;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import de.uni_leipzig.er_bloom_eval.er.jaccard.bit_array.plain.PlainBitArrayPerson;
import de.uni_leipzig.er_bloom_eval.er.jaccard.set.SetPerson;
import de.uni_leipzig.er_bloom_eval.er.n_gramm.DateGramm;
import de.uni_leipzig.er_bloom_eval.er.n_gramm.NGrammTransformer;
import de.uni_leipzig.er_bloom_eval.util.io.HasAssociatedPerson;
import de.uni_leipzig.er_bloom_eval.util.io.InternalDataStructureFactory;

/**
 * Represents a {@link HasAssociatedPerson person} by representing all components of the person
 * (i.e. {@link NGrammTransformer ngramms} of the address and name and {@link DateGramm date-gramms} of the birthday)
 * as a pair of an bit array and an integer which represents the amount of elements in the originally set.
 * <p>
 * By leveraging this representation, the associated {@link BitArrayUpperBoundPersonSimilarityFunction similarity function}
 * yields to an upper bound of the exact result which would be obtained by {@link SetPerson the approach using the set representation}
 * ,but mutch faster.
 * </p>
 *
 * @author Maik Fröbe
 */
@SuppressWarnings("serial")
public class BitArrayUpperBoundPerson implements HasAssociatedPerson
{
	public static final InternalDataStructureFactory<BitArrayUpperBoundPerson> FACTORY = new InternalDataStructureFactory<BitArrayUpperBoundPerson>()
	{
		@Override
		public BitArrayUpperBoundPerson apply(long id, Set<String> addressGramm, Set<Integer> birtdayGramm, Set<String> nameGramm)
		{
			return new BitArrayUpperBoundPerson(id,
					new ImmutablePair<>(addressGramm.size(), PlainBitArrayPerson.toBloomFilter(addressGramm)),
					new ImmutablePair<>(birtdayGramm.size(), PlainBitArrayPerson.toBloomFilter(birtdayGramm)),
					new ImmutablePair<>(nameGramm.size(), PlainBitArrayPerson.toBloomFilter(nameGramm)));
		}
	};

	private final long _id;

	private final Pair<Integer, BitSet> _address;

	private final Pair<Integer, BitSet> _birthday;

	private final Pair<Integer, BitSet> _name;

	public BitArrayUpperBoundPerson(long id, Pair<Integer, BitSet> address, Pair<Integer, BitSet> birthday, Pair<Integer, BitSet> name)
	{
		this._id = id;
		this._address = address;
		this._birthday = birthday;
		this._name = name;
	}

	public Pair<Integer, BitSet> getAddress()
	{
		return this._address;
	}

	public Pair<Integer, BitSet> getBirthday()
	{
		return this._birthday;
	}

	@Override
	public long getId()
	{
		return this._id;
	}

	public Pair<Integer, BitSet> getName()
	{
		return this._name;
	}
}