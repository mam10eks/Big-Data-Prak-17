package de.uni_leipzig.er_bloom_eval.er.jaccard.bit_array.plain;

import java.util.BitSet;
import java.util.Collection;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_leipzig.er_bloom_eval.er.jaccard.sorted_set.SortedSetPerson;
import de.uni_leipzig.er_bloom_eval.er.n_gramm.DateGramm;
import de.uni_leipzig.er_bloom_eval.er.n_gramm.NGrammTransformer;
import de.uni_leipzig.er_bloom_eval.util.ArgumentsUtil;
import de.uni_leipzig.er_bloom_eval.util.bloom.BloomFilter;
import de.uni_leipzig.er_bloom_eval.util.bloom.PredefinedBloomFilter;
import de.uni_leipzig.er_bloom_eval.util.io.HasAssociatedPerson;
import de.uni_leipzig.er_bloom_eval.util.io.InternalDataStructureFactory;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * Represents a {@link HasAssociatedPerson person} by representing all components of the person
 * (i.e. {@link NGrammTransformer ngramms} of the address and name and {@link DateGramm date-gramms} of the birthday)
 * as bit arrays (which are internally represented by {@link BitSet}).
 * <p>
 * By leveraging this representation, the associated {@link PlainBitArrayPersonSimilarityFunction similarity function of the plain bit arrays}
 * yields to an approximate (but really fast) solution representing the wanted arithmetic mean of the jaccard index of the components.
 * </p>
 * <p>
 * This solution is much faster then the approach leveraging {@link SortedSetPerson sorted sets}, but <b>delivers in general not the exact result</b>.
 * I.e. we could have false positives and false negatives.
 * </p>
 *
 * @author m.froebe
 */
@SuppressWarnings("serial")
public class PlainBitArrayPerson implements HasAssociatedPerson
{
	public static final InternalDataStructureFactory<PlainBitArrayPerson> FACTORY = new InternalDataStructureFactory<PlainBitArrayPerson>()
	{
		@Override
		public PlainBitArrayPerson apply(long id, Set<String> addressGramm, Set<Integer> birtdayGramm, Set<String> nameGramm)
		{
			return new PlainBitArrayPerson(id, toBloomFilter(addressGramm),
					toBloomFilter(birtdayGramm), toBloomFilter(nameGramm));
		}
	};

	private static Integer _bitsInBloomFilter = null;

	private static PredefinedBloomFilter _filterFactory = null;

	private static final Logger LOGGER = LoggerFactory.getLogger(PlainBitArrayPerson.class);

	public static synchronized void setFactory(Namespace args)
	{
		if(_bitsInBloomFilter == null)
		{
			_bitsInBloomFilter = args.getInt(ArgumentsUtil.PARAM_BITS_IN_BLOOM_FILTER);
			_filterFactory = PredefinedBloomFilter.valueOf(args.getString(ArgumentsUtil.PARAM_INTERNAL_BLOOM_FILTER));
		}
		else
		{
			LOGGER.warn("The factory is already defined. This operation is a no-op now.");
		}
	}

	public static <V> BitSet toBloomFilter(Collection<V> input)
	{
		final BloomFilter<V> ret = _filterFactory.createNewBloomFilter(_bitsInBloomFilter);

		for(final V element : input)
		{
			ret.add(element);
		}

		return ret.getBitSet();
	}

	private final long _id;

	private final BitSet _addressBloom;

	private final BitSet _birthdayBloom;

	private final BitSet _nameBloom;

	public PlainBitArrayPerson(long id, BitSet addressBloom, BitSet birthdayBloom, BitSet nameBloom)
	{
		this._id = id;
		this._addressBloom = addressBloom;
		this._birthdayBloom = birthdayBloom;
		this._nameBloom = nameBloom;
	}

	public BitSet getAddressBloom()
	{
		return this._addressBloom;
	}

	public BitSet getBirthdayBloom()
	{
		return this._birthdayBloom;
	}

	@Override
	public long getId()
	{
		return this._id;
	}

	public BitSet getNameBloom()
	{
		return this._nameBloom;
	}
}