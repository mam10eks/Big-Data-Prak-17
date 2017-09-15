package de.uni_leipzig.er_bloom_eval.batch.eval;

import java.util.ArrayList;
import java.util.List;

import de.uni_leipzig.er_bloom_eval.batch.run.ResultData;
import de.uni_leipzig.er_bloom_eval.er.PredefinedERStrategy;
import de.uni_leipzig.er_bloom_eval.util.ArgumentsUtil;
import de.uni_leipzig.er_bloom_eval.util.bloom.PredefinedBloomFilter;
import net.sourceforge.argparse4j.inf.Namespace;

public class RuntimeEvaluationObject
{
	public Integer bitsInBloomFilter = null;
	
	public PredefinedBloomFilter bloomFilter = null;
	
	public PredefinedERStrategy erStrategy = null;
	
	public final List<ResultData> _resultData = new ArrayList<>();
	
	public boolean add(ResultData a)
	{
		final Namespace ns =  ResultData.getParsedConfiguration(a);
		final Integer aBitsInBf = ns.getInt(ArgumentsUtil.PARAM_BITS_IN_BLOOM_FILTER);
		final PredefinedBloomFilter abf = PredefinedBloomFilter.valueOf(ns.getString(ArgumentsUtil.PARAM_INTERNAL_BLOOM_FILTER));
		final PredefinedERStrategy aer = PredefinedERStrategy.valueOf(ns.getString(ArgumentsUtil.PARAM_ER_STRATEGY));
		
		synchronized(_resultData)
		{
			if(_resultData.isEmpty())
			{
				bitsInBloomFilter = aBitsInBf;
				bloomFilter = abf;
				erStrategy = aer;
				
				return _resultData.add(a);
			}
			else if(equal(bitsInBloomFilter, aBitsInBf) && equal(bloomFilter, abf) && equal(erStrategy, aer))
			{
				
				
				return _resultData.add(a);
			}
		}
		
		return Boolean.FALSE;
	}
	
	public boolean equal(Object a, Object b)
	{
		return (a == null && b == null) || (a != null && a.equals(b));
	}
}