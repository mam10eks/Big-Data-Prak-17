package de.uni_leipzig.er_bloom_eval.er;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.uni_leipzig.er_bloom_eval.er.EntityResolution;
import de.uni_leipzig.er_bloom_eval.er.EntityResolutionStrategy;
import de.uni_leipzig.er_bloom_eval.er.SimilarityFunction;
import de.uni_leipzig.er_bloom_eval.util.io.HasAssociatedPerson;
import de.uni_leipzig.er_bloom_eval.util.io.InternalDataStructureFactory;

@RunWith(Parameterized.class)
public class EntityResolutionTest
{
	@Parameters
	public static Collection<Object[]> parameters()
	{
		return Arrays.asList(
				new Object[]{listWithElements(3), listWithElements(2)},
				new Object[]{listWithElements(2), listWithElements(3)},
				new Object[]{listWithElements(10), listWithElements(100)},
				new Object[]{listWithElements(100), listWithElements(10)},
				new Object[]{listWithElements(100), listWithElements(100)}
			);
	}

	@SuppressWarnings("serial")
	private static class IntegerWrapper implements HasAssociatedPerson
	{
		private final Integer _i;
		
		public IntegerWrapper(int i)
		{
			this._i = i;
		}

		@Override
		public long getId()
		{
			return _i;
		}
	}
	
	private static List<IntegerWrapper> listWithElements(int numberElements)
	{
		List<IntegerWrapper> ret = new ArrayList<>();
		
		for(int i=0; i< numberElements; i++)
		{
			ret.add(new IntegerWrapper(i));
		}
		
		return ret;
	}
	
	private final List<IntegerWrapper> _a;
	
	private final List<IntegerWrapper> _b;
	
	public EntityResolutionTest(List<IntegerWrapper> a, List<IntegerWrapper> b)
	{
		this._a = a;
		this._b = b;
	}
	
	@Test
	public void testAllSimilaritiesAreCalculated()
	{
		final List<Pair<Long, Long>> pairsWithCheckedSimilarity = new ArrayList<>();
		
		@SuppressWarnings("serial")
		List<Triple<Long, Long, Double>> similarPairs = new EntityResolution<IntegerWrapper>(createTestEntityResolutionStrategy(new SimilarityFunction<EntityResolutionTest.IntegerWrapper>()
		{

			@Override
			public double similarity(IntegerWrapper o1, IntegerWrapper o2)
			{
				pairsWithCheckedSimilarity.add(new ImmutablePair<Long, Long>(o1._i.longValue(), o2._i.longValue()));
				return 1;
			}
		}), 1d).calculateSimilarPairs(_a, _b);
			
		pairsWithCheckedSimilarity.equals(crossProduct(_a, _b));
		
		Assert.assertEquals(crossProduct(_a, _b), pairsWithCheckedSimilarity);
		Assert.assertEquals(crossProduct(_a, _b), projectPersonPairs(similarPairs));
		
		for(Triple<Long, Long, Double> match : similarPairs)
		{
			Assert.assertEquals(1.0d, match.getRight().doubleValue(), 1e-10);
		}
	}
	
	private static List<Pair<Long, Long>> projectPersonPairs(List<Triple<Long, Long, Double>> a)
	{
		List<Pair<Long, Long>> ret = new ArrayList<>();
		
		for(Triple<Long, Long, Double> elem : a)
		{
			ret.add(new ImmutablePair<>(elem.getLeft(), elem.getMiddle()));
		}
		
		return ret;
	}
	
	@SuppressWarnings("serial")
	private static EntityResolutionStrategy<IntegerWrapper> createTestEntityResolutionStrategy(SimilarityFunction<IntegerWrapper> similarityFunction)
	{
		return new EntityResolutionStrategy<IntegerWrapper>(similarityFunction, new InternalDataStructureFactory<IntegerWrapper>()
		{
			@Override
			public IntegerWrapper apply(long id, Set<String> addressGramm, Set<Integer> birtdayGramm, Set<String> nameGramm)
			{
				return null;
			}
		});
	}
	
	private static List<Pair<Long,Long>> crossProduct(List<IntegerWrapper> a, List<IntegerWrapper> b)
	{
		List<Pair<Long,Long>> ret = new ArrayList<>();
		
		for(IntegerWrapper aw : a)
		{
			for(IntegerWrapper bw : b)
			{
				ret.add(new ImmutablePair<Long, Long>(aw._i.longValue(), bw._i.longValue()));
			}
		}
		
		return ret;
	}
}