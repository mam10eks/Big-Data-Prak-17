package de.uni_leipzig.er_bloom_eval.er.jaccard.set;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class JaccardIndexTest<V>
{	
	@Parameters
	public static Collection<Object[]> params()
	{
		return Arrays.asList
			(
				new Object[]{new HashSet<>(Arrays.asList()), new HashSet<>(Arrays.asList()), 0d},
				new Object[]{new HashSet<>(Arrays.asList(1)), new HashSet<>(Arrays.asList(2)), 0d},
				new Object[]{new HashSet<>(Arrays.asList(1)), new HashSet<>(Arrays.asList()), 0d},
					
				new Object[]{new HashSet<>(Arrays.asList(1,2,3)), new HashSet<>(Arrays.asList(1,2,3)), 1d},
				new Object[]{new HashSet<>(Arrays.asList(1,2,3)), new HashSet<>(Arrays.asList(1,2,4)), (2d/4d)},
				new Object[]{new HashSet<>(Arrays.asList(1,2,3)), new HashSet<>(Arrays.asList(1,4, 5)), (1d/5d)},
				new Object[]{new HashSet<>(Arrays.asList(1,2,3)), new HashSet<>(Arrays.asList(1)), (1d/3d)}
			);
	}
	
	private final Set<V> _firstInput;
	
	private final Set<V> _secondInput;
	
	private final double _expected;
	
	public JaccardIndexTest(Set<V> firstInput, Set<V> secondInput, double expected)
	{
		this._firstInput = firstInput;
		this._secondInput = secondInput;
		this._expected = expected;
	}
	
	@Test
	public void test()
	{
		Assert.assertEquals(_expected, SetPersonSimilarityFunction.jaccard(_firstInput, _secondInput), 1e-10);
	}
}