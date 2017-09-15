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

import de.uni_leipzig.er_bloom_eval.util.io.PersonTransformer;

@RunWith(Parameterized.class)
public class DateGrammTest
{
	@Parameters
	public static Collection<Object[]> parameters()
	{
		return Arrays.asList
		(
			new Object[]{"19550128", 0, new HashSet<>(Arrays.asList(1955, 0, 28))},
			new Object[]{"19250830", 0, new HashSet<>(Arrays.asList(1925, 7, 30))},
			new Object[]{"19811201", 0, new HashSet<>(Arrays.asList(1981, 11, 1))},
			new Object[]{"19550128", 1, new HashSet<>(Arrays.asList(1954, 1955, 1956, -1, 0, 1, 27, 28, 29))},
			new Object[]{"19250830", 1, new HashSet<>(Arrays.asList(1924, 1925, 1926, 6, 7, 8, 29, 30, 31))},
			new Object[]{"19811201", 1, new HashSet<>(Arrays.asList(1980, 1981, 1982, 10, 11, 12, 0, 1, 2))},
			new Object[]{"19550128", 2, new HashSet<>(Arrays.asList(1953, 1954, 1955, 1956, 1957, -2, -1, 0, 1, 2, 26, 27, 28, 29, 30))},
			new Object[]{"19250830", 2, new HashSet<>(Arrays.asList(1923, 1924, 1925, 1926, 1927, 5, 6, 7, 8, 9, 28,  29, 30, 31, 32))},
			new Object[]{"19811201", 2, new HashSet<>(Arrays.asList(1979, 1980, 1981, 1982, 1983, 9, 10, 11, 12, 13, -1, 0, 1, 2, 3))}
		);
	}
	
	private final String _input;
	
	private final int _radius;
	
	private final Set<Integer> _expected;
	
	public DateGrammTest(String input, int radius, Set<Integer> expected)
	{
		this._input = input;
		this._radius = radius;
		this._expected = expected;
	}
	
	@Test
	public void test()
	{
		Assert.assertEquals(_expected, new DateGramm(_radius).apply(PersonTransformer.parse(_input)));
	}
}