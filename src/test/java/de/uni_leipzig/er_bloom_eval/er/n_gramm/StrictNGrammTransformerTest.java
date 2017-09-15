package de.uni_leipzig.er_bloom_eval.er.n_gramm;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class StrictNGrammTransformerTest
{
	static final String EXAMPLE_LONG_STRING = "  This \t\t  is\n an\n\r\n example\n\r \n\n";
	
	static final String EXAMPLE_SHORT_STRING = "abc";
	
	static final String EXAMPLE_MIDDLE_STRING = "abcde";
	
	@Parameters
	public static Collection<Object[]> parameters()
	{
		return Arrays.asList
		(
			new Object[]{EXAMPLE_SHORT_STRING, 1, new HashSet<>(Arrays.asList("a", "b", "c"))},
			new Object[]{EXAMPLE_MIDDLE_STRING, 1, new HashSet<>(Arrays.asList("a", "b", "c", "d", "e"))},
			new Object[]{EXAMPLE_SHORT_STRING, 2, new HashSet<>(Arrays.asList("ab", "bc" ))},
			new Object[]{EXAMPLE_MIDDLE_STRING, 2, new HashSet<>(Arrays.asList("ab", "bc", "cd", "de"))},
			new Object[]{EXAMPLE_SHORT_STRING, 3, new HashSet<>(Arrays.asList("abc" ))},
			new Object[]{EXAMPLE_MIDDLE_STRING, 3, new HashSet<>(Arrays.asList("abc", "bcd", "cde"))},
			new Object[]{EXAMPLE_LONG_STRING, 3, new HashSet<>(Arrays.asList("thi", "his", "is ", "s i", " is", "is ", "s a", " an", "an ", "n e", " ex", "exa", "xam", "amp", "mpl", "ple"))},
			new Object[]{EXAMPLE_SHORT_STRING, 4, new HashSet<>(Collections.emptySet())},
			new Object[]{EXAMPLE_MIDDLE_STRING, 4, new HashSet<>(Arrays.asList("abcd", "bcde"))},
			new Object[]{EXAMPLE_LONG_STRING, 4, new HashSet<>(Arrays.asList("this", "his ", "is i", "s is", " is ", "is a", "s an", " an ", "an e", "n ex", " exa", "exam", "xamp", "ampl", "mple"))},
			new Object[]{EXAMPLE_SHORT_STRING, 5, new HashSet<>(Collections.emptySet())},
			new Object[]{EXAMPLE_MIDDLE_STRING, 5, new HashSet<>(Arrays.asList("abcde"))},
			new Object[]{EXAMPLE_SHORT_STRING, 6, new HashSet<>(Collections.emptySet())},
			new Object[]{EXAMPLE_MIDDLE_STRING, 6, new HashSet<>(Collections.emptySet())}
		);
	}
	
	private final String _input;
	
	private final int _n;
	
	private final Set<String> _expected;
	
	public StrictNGrammTransformerTest(String input, int n, Set<String> expected)
	{
		this._input = input;
		this._n = n;
		this._expected = expected;
	}
	
	@Test
	public void test()
	{
		Assert.assertEquals(_expected, new StrictNGrammTransformer(_n, StringPreprocessing.LOWER_CASE_MERGE_TRIM_EMPTY_SPACES).apply(_input));
	}
}