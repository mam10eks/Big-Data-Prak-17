package de.uni_leipzig.er_bloom_eval.er.n_gramm;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * 
 * @author Maik Fröbe
 */
@RunWith(Parameterized.class)
public class SpecialBoundaryNGrammTransformerTest
{
	@Parameters
	public static Collection<Object[]> parameters()
	{
		return Arrays.asList
		(
			new Object[]{StrictNGrammTransformerTest.EXAMPLE_SHORT_STRING, 1, new HashSet<>(Arrays.asList("a", "b", "c"))},
			new Object[]{StrictNGrammTransformerTest.EXAMPLE_MIDDLE_STRING, 1, new HashSet<>(Arrays.asList("a", "b", "c", "d", "e"))},
			new Object[]{StrictNGrammTransformerTest.EXAMPLE_SHORT_STRING, 2, new HashSet<>(Arrays.asList(" a", "ab", "bc", "c " ))},
			new Object[]{StrictNGrammTransformerTest.EXAMPLE_MIDDLE_STRING, 2, new HashSet<>(Arrays.asList(" a", "ab", "bc", "cd", "de", "e "))},
			new Object[]{StrictNGrammTransformerTest.EXAMPLE_SHORT_STRING, 3, new HashSet<>(Arrays.asList("  a", " ab", "abc", "bc ", "c  " ))},
			new Object[]{StrictNGrammTransformerTest.EXAMPLE_MIDDLE_STRING, 3, new HashSet<>(Arrays.asList("  a", " ab", "abc", "bcd", "cde", "de ", "e  "))},
			new Object[]{StrictNGrammTransformerTest.EXAMPLE_LONG_STRING, 3, new HashSet<>(Arrays.asList("  t", " th", "thi", "his", "is ", "s i", " is", "is ", "s a", " an", "an ", "n e", " ex", "exa", "xam", "amp", "mpl", "ple", "le ", "e  "))},
			new Object[]{StrictNGrammTransformerTest.EXAMPLE_SHORT_STRING, 4, new HashSet<>(Arrays.asList("   a", "  ab", " abc", "abc ", "bc  ", "c   "))},
			new Object[]{StrictNGrammTransformerTest.EXAMPLE_MIDDLE_STRING, 4, new HashSet<>(Arrays.asList("   a", "  ab", " abc", "abcd", "bcde", "cde ", "de  ", "e   "))},
			new Object[]{StrictNGrammTransformerTest.EXAMPLE_LONG_STRING, 4, new HashSet<>(Arrays.asList("   t","  th", " thi", "this", "his ", "is i", "s is", " is ", "is a", "s an", " an ", "an e", "n ex", " exa", "exam", "xamp", "ampl", "mple", "ple ", "le  ", "e   "))},
			new Object[]{StrictNGrammTransformerTest.EXAMPLE_MIDDLE_STRING, 5, new HashSet<>(Arrays.asList("    a", "   ab", "  abc", " abcd", "abcde", "bcde ", "cde  ", "de   ", "e    "))}
		);
	}
	
	private final String _input;
	
	private final int _n;
	
	private final Set<String> _expected;
	
	public SpecialBoundaryNGrammTransformerTest(String input, int n, Set<String> expected)
	{
		this._input = input;
		this._n = n;
		this._expected = expected;
	}
	
	@Test
	public void test()
	{
		Assert.assertEquals(_expected, new SpecialBoundaryNGrammTransformer(_n, StringPreprocessing.LOWER_CASE_MERGE_TRIM_EMPTY_SPACES).apply(_input));
	}
}