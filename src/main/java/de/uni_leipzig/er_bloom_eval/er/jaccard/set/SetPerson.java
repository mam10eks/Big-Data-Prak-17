package de.uni_leipzig.er_bloom_eval.er.jaccard.set;

import java.util.Collections;
import java.util.Set;

import de.uni_leipzig.er_bloom_eval.er.n_gramm.DateGramm;
import de.uni_leipzig.er_bloom_eval.er.n_gramm.NGrammTransformer;
import de.uni_leipzig.er_bloom_eval.util.io.HasAssociatedPerson;
import de.uni_leipzig.er_bloom_eval.util.io.InternalDataStructureFactory;

/**
 * Represents a {@link HasAssociatedPerson person} by representing all components of the person
 * (i.e. {@link NGrammTransformer ngramms} of the address and name and {@link DateGramm date-gramms} of the birthday)
 * as {@link Set sets}.
 * <p>
 * By leveraging this representation, the associated {@link SetPersonSimilarityFunction similarity function of the sets}
 * yields to an exact (but slow) solution representing the wanted arithmetic mean of the jaccard index of the components.
 * </p>
 *
 * @author m.froebe
 */
@SuppressWarnings("serial")
public class SetPerson implements HasAssociatedPerson
{
	public static final InternalDataStructureFactory<SetPerson> FACTORY = new InternalDataStructureFactory<SetPerson>()
	{
		@Override
		public SetPerson apply(long id, Set<String> addressGramm, Set<Integer> birtdayGramm, Set<String> nameGramm)
		{
			return new SetPerson(id, addressGramm, birtdayGramm, nameGramm);
		}

	};

	private final long _id;

	private final Set<String> _addressNGramms;

	private final Set<Integer> _birthdayNGramms;

	private final Set<String> _nameNGramms;

	public SetPerson(long id, Set<String> addressNGramms, Set<Integer> birthdayNGramms, Set<String> nameNGramms)
	{
		this._id = id;
		this._addressNGramms = Collections.unmodifiableSet(addressNGramms);
		this._birthdayNGramms = Collections.unmodifiableSet(birthdayNGramms);
		this._nameNGramms = Collections.unmodifiableSet(nameNGramms);
	}

	public Set<String> getAddressNGramms()
	{
		return this._addressNGramms;
	}

	public Set<Integer> getBirthdayNgramms()
	{
		return this._birthdayNGramms;
	}

	@Override
	public long getId()
	{
		return this._id;
	}

	public Set<String> getNameNGramms()
	{
		return this._nameNGramms;
	}
}