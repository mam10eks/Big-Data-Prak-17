package de.uni_leipzig.er_bloom_eval.er.jaccard.set;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.uni_leipzig.er_bloom_eval.er.jaccard.set.SetPerson;
import de.uni_leipzig.er_bloom_eval.er.jaccard.set.SetPersonSimilarityFunction;

@RunWith(Parameterized.class)
public class PartitionedPersonNGrammSimilarityFunctionTest
{
	@SuppressWarnings("unchecked")
	@Parameters
	public static Collection<Object[]> parameters()
	{
		return Arrays.asList
		(
			new Object[]
			{
				new SetPerson(Long.valueOf(1),new HashSet<>(Arrays.asList("a", "b", "c")), Collections.EMPTY_SET, Collections.EMPTY_SET),
				new SetPerson(Long.valueOf(1),new HashSet<>(Arrays.asList("a", "c")), Collections.EMPTY_SET, Collections.EMPTY_SET),
				2d/(3d*3d)
			},
			new Object[]
			{
				new SetPerson(Long.valueOf(1),Collections.EMPTY_SET, new HashSet<>(Arrays.asList(1, 2, 3)), Collections.EMPTY_SET),
				new SetPerson(Long.valueOf(1),Collections.EMPTY_SET, new HashSet<>(Arrays.asList(2, 4)), Collections.EMPTY_SET),
				1d/(4d*3d)
			},
			new Object[]
			{
				new SetPerson(Long.valueOf(1),Collections.EMPTY_SET, Collections.EMPTY_SET, Collections.EMPTY_SET),
				new SetPerson(Long.valueOf(1),Collections.EMPTY_SET, Collections.EMPTY_SET, Collections.EMPTY_SET),
				0d
			},
			new Object[]
			{
				new SetPerson(Long.valueOf(1),Collections.EMPTY_SET, Collections.EMPTY_SET, new HashSet<>(Arrays.asList("a", "b", "c"))),
				new SetPerson(Long.valueOf(1),Collections.EMPTY_SET, Collections.EMPTY_SET, new HashSet<>(Arrays.asList("c", "d", "e"))),
				1d/(5d*3d)
			},
			new Object[]
			{
				new SetPerson(Long.valueOf(1),new HashSet<>(Arrays.asList("a", "b", "c")), new HashSet<>(Arrays.asList(1, 2, 3)), new HashSet<>(Arrays.asList("a", "b", "c"))),
				new SetPerson(Long.valueOf(1),new HashSet<>(Arrays.asList("a", "c")), new HashSet<>(Arrays.asList(2, 4)), new HashSet<>(Arrays.asList("c", "d", "e"))),
				((2d/3d) + (1d/4d) + (1d/5d))/3d
			},
			new Object[]
			{
				new SetPerson(Long.valueOf(1),new HashSet<>(Arrays.asList("a")), new HashSet<>(Arrays.asList(1)), new HashSet<>(Arrays.asList("b"))),
				new SetPerson(Long.valueOf(1),new HashSet<>(Arrays.asList("a")), new HashSet<>(Arrays.asList(1)), new HashSet<>(Arrays.asList("b"))),
				1d
			}
		);
	}
	
	private final SetPerson _firstInput;
	
	private final SetPerson _secondInput;
	
	private final double _expected;
	
	public PartitionedPersonNGrammSimilarityFunctionTest(SetPerson firstInput, SetPerson secondInput, double expected)
	{
		_firstInput = firstInput;
		_secondInput = secondInput;
		_expected = expected;
	}
	
	@Test
	public void test()
	{
		Assert.assertEquals(_expected, new SetPersonSimilarityFunction().similarity(_firstInput, _secondInput), 1e-10d);
	}
}