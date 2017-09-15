package de.uni_leipzig.er_bloom_eval.er.bloom_filter;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.uni_leipzig.er_bloom_eval.er.jaccard.bit_array.plain.PlainBitArrayPerson;
import de.uni_leipzig.er_bloom_eval.er.jaccard.bit_array.plain.PlainBitArrayPersonSimilarityFunction;
import de.uni_leipzig.er_bloom_eval.util.ArgumentsUtil;
import de.uni_leipzig.er_bloom_eval.util.bloom.PredefinedBloomFilter;
import net.sourceforge.argparse4j.inf.Namespace;

@RunWith(Parameterized.class)
public class PartitionedPersonBloomFilterSimilarityFunctionTest<V>
{
	@Parameters
	public static Collection<Object[]> parameters()
	{
		Map<String, Object> args = new HashMap<>();
		args.put(ArgumentsUtil.PARAM_BITS_IN_BLOOM_FILTER, 640);
		args.put(ArgumentsUtil.PARAM_INTERNAL_BLOOM_FILTER, PredefinedBloomFilter.BITSET_BLOOM_FILTER.name());
		PlainBitArrayPerson.setFactory(new Namespace(args));
		
		return Arrays.asList
		(
			new Object[]{bf("a", "b", "c"), bf("a", "b", "c"), 1d},
			new Object[]{bf(1, 2, 3), bf(1, 2, 3), 1d},
			new Object[]{bf("a", "b", "c"), bf("d", "e", "f"), 0d},
			new Object[]{bf("a", "b", "c"), bf("a", "e", "f"), 1d/5d},
			new Object[]{bf("a", "b", "c"), bf("a", "c", "f"), 1d/2d},
			new Object[]{bf("a", "b", "c"), bf("a", "b"), 2d/3d},
			new Object[]{bf(1, 2, 3), bf(4, 5, 6), 0d},
			new Object[]{bf(1, 2, 3), bf(1, 4, 5), 1d/5d},
			new Object[]{bf(1, 2, 3), bf(1, 3, 4), 1d/2d},
			new Object[]{bf(1, 2, 3), bf(1, 3), 2d/3d }
		);
	}
	
	@SafeVarargs
	private static <V> BitSet bf(V...val)
	{
		return PlainBitArrayPerson.toBloomFilter(Arrays.asList(val));
	}
	
	private final BitSet _firstInput;
	
	private final BitSet _secondInput;
	
	private final double _expected;
	
	public PartitionedPersonBloomFilterSimilarityFunctionTest(BitSet a, BitSet b, double expected)
	{
		this._firstInput = a;
		this._secondInput = b;
		this._expected = expected;
	}
	
	@Test
	public void test()
	{
		Assert.assertEquals(_expected, PlainBitArrayPersonSimilarityFunction.jaccard(_firstInput, _secondInput), 1e-1);
	}
}