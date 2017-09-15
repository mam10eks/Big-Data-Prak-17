package de.uni_leipzig.er_bloom_eval.er.n_gramm;

import java.util.HashSet;
import java.util.Set;

/**
 * {@link StrictNGrammTransformer#apply(String) Transforms} Strings into n-gramm's.
 * 
 * <p>
 * This transformation extracts only real n-gramm's, that are contained in the string after
 * {@link StrictNGrammTransformer#_normalizer applying the defined preprocessing}.
 * <br>
 * E.g. for an input
 * <br><code>
 * "An example"
 * </code><br>
 * with n=3, a possible result would look like:
 * <br><code>
 * ["An ", "n e", " ex", "exa", "xam", "amp", "mpl", "ple"]
 * </code>.
 * </p>
 * 
 * @author Maik Fröbe
 * @see StringPreprocessing
 * @see SpecialBoundaryNGrammTransformer
 */
@SuppressWarnings("serial")
public class StrictNGrammTransformer implements NGrammTransformer
{
	private final int _length;
	
	private final StringPreprocessing _normalizer;
	
	/**
	 * @param length
	 * 		The length of the n-gramm's that {@link StrictNGrammTransformer#apply(String) will be received}.
	 * @param normalizer
	 * 		The normalization that will be applied to inputs before they will be
	 * 		{@link StrictNGrammTransformer#apply(String) transformed to n-gramm's.}
	 */
	public StrictNGrammTransformer(int length, StringPreprocessing normalizer)
	{
		this._length = length;
		this._normalizer = normalizer;
	}

	/**
	 * {@link StrictNGrammTransformer#_normalizer Normalizes} the passed <code>input</code> and extracts all n-gramm's.
	 * 
	 * @param input
	 * 		The string for which n-gramm's should be extracted.
	 * @return
	 * 		The n-gramm's in the passed <code>input</code>.
	 */
	@Override
	public Set<String> apply(String input)
	{
		final String normalizedInput = _normalizer.apply(input);
		Set<String> ret = new HashSet<>();
		
		for(int i=0; i< normalizedInput.length() -_length+1; i++)
		{
			ret.add(normalizedInput.substring(i, i+_length));
		}
		
		return ret;
	}
}