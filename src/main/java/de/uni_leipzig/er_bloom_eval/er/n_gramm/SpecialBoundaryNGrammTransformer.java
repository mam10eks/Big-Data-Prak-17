package de.uni_leipzig.er_bloom_eval.er.n_gramm;

import java.util.HashSet;
import java.util.Set;

/**
 * {@link SpecialBoundaryNGrammTransformer#apply(String) Transforms} Strings into n-gramm's.
 * 
 * <p>
 * This transformation extracts real n-gramm's including special start and end n-gramm's,
 * that are contained in the string after
 * {@link StrictNGrammTransformer#_normalizer applying the defined preprocessing}.
 * <br>
 * E.g. for an input
 * <br><code>
 * "An example"
 * </code><br>
 * with n=3, a possible result would look like:
 * <br><code>
 * ["  A", " An", "An ", "n e", " ex", "exa", "xam", "amp", "mpl", "ple", "le ", "e  "]
 * </code>.
 * </p>
 * 
 * @author Maik Fröbe
 * @see StringPreprocessing
 * @see StrictNGrammTransformer
 */
@SuppressWarnings("serial")
public class SpecialBoundaryNGrammTransformer implements NGrammTransformer
{
	private final int _length;
	
	private final StringPreprocessing _normalizer;
	
	private final String _boundary;
	
	/**
	 * @param length
	 * 		The length of the n-gramm's that {@link SpecialBoundaryNGrammTransformer#apply(String) will be received}.
	 * @param normalizer
	 * 		The normalization that will be applied to inputs before they will be
	 * 		{@link SpecialBoundaryNGrammTransformer#apply(String) transformed to n-gramm's.}
	 */
	public SpecialBoundaryNGrammTransformer(int length, StringPreprocessing normalizer)
	{
		this._length = length;
		this._normalizer = normalizer;
		this._boundary = determineBoundary(_length);
	}

	/**
	 * {@link StrictNGrammTransformer#_normalizer Normalizes} the passed <code>input</code> and extracts all n-gramm's.
	 * 
	 * @param input
	 * 		The string for which n-gramm's should be extracted.
	 * @return
	 * 		The n-gramm's in the passed <code>input</code>.
	 */
	public Set<String> apply(String input)
	{
		final String normalizedInput = _boundary + _normalizer.apply(input) + _boundary;
		Set<String> ret = new HashSet<>();
		
		for(int i=0; i< normalizedInput.length() -_length+1; i++)
		{
			ret.add(normalizedInput.substring(i, i+_length));
		}
		
		return ret;
	}
	
	private static String determineBoundary(int length)
	{
		String ret = "";
		
		for(int i=0; i< length-1; i++)
		{
			ret += " ";
		}
		
		return ret;
	}
}