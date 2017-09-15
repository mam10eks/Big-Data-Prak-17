//package de.uni_leipzig.er_bloom_eval.batch.eval;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//import org.codehaus.jackson.map.ObjectMapper;
//
//import de.uni_leipzig.er_bloom_eval.batch.run.ResultData;
//import de.uni_leipzig.er_bloom_eval.er.PredefinedERStrategy;
//import de.uni_leipzig.er_bloom_eval.util.ArgumentsUtil;
//import de.uni_leipzig.er_bloom_eval.util.bloom.PredefinedBloomFilter;
//import joinery.DataFrame;
//import joinery.DataFrame.PlotType;
//
//public class Evaluation
//{
//	public static void main(String[] args) throws Exception
//	{
//        final ObjectMapper objectMapper = new ObjectMapper();
//        List<ResultData> blaaa = objectMapper.reader()
//        	.readValue(objectMapper.getJsonFactory().createJsonParser(new File("TEST_DATA_29_07_2017/result.json")), objectMapper.getTypeFactory().constructCollectionType(List.class, ResultData.class));
//
//        List<RuntimeEvaluationObject> evaluatedStuff = new ArrayList<>();
//        
//        for(ResultData rd : blaaa)
//        {
//        	if(!rd.getConfiguration().get("--"+ ArgumentsUtil.PARAM_SIMILARITY_THRESHOLD).equals("0.7"))
//        	{
//        		continue;
//        	}
//        	
//        	if(addToExisting(evaluatedStuff, rd))
//        	{
//        		continue;
//        	}
//        	
//        	RuntimeEvaluationObject newObject = new RuntimeEvaluationObject();
//        	newObject.add(rd);
//        	evaluatedStuff.add(newObject);
//        }
//        
//        Thread[] threads = new Thread[]{
//        	new EvaluationThread(new RuntimeEvaluation(), evaluatedStuff)
////        	new EvaluationThread(new FalsePositiveEvaluation(), evaluatedStuff)
//        	
//        	};
//        
//        for(Thread t : threads)
//        {
//        	t.start();
//        }
//        
//        for(Thread t : threads)
//        {
//        	t.join();
//        }
//	}
//	
//	private static class FalsePositiveEvaluation implements EvaluationConsumer
//	{
//		@Override
//		public void evaluate(List<RuntimeEvaluationObject> evaluatedStuff)
//		{
//			evaluatedStuff = evaluatedStuff.stream()
//					.filter(e ->
//						(PredefinedERStrategy.JACCARD_PARTITIONED_BLOOM_FILTER.equals(e.erStrategy) &&
//						PredefinedBloomFilter.BITSET_BLOOM_FILTER.equals(e.bloomFilter) && (e.bitsInBloomFilter == 128|| e.bitsInBloomFilter == 640 || e.bitsInBloomFilter == 1280)))
//					.collect(Collectors.toList());
//			
//			List<String> columns = new ArrayList<>();
//			
//			for(String name : names(evaluatedStuff))
//			{
//				columns.add("FP_"+ name);
//				columns.add("FN_"+ name);
//			}
//
//			DataFrame<Number> frame = new DataFrame<>(columns);
//			
//			for(Integer size : sizes(evaluatedStuff))
//			{
//				List<Integer> values = new ArrayList<>();
//				
//				for(RuntimeEvaluationObject bla : evaluatedStuff)
//				{
//					ResultData rd = determineResultData(size, bla);
//					
//					values.add(rd.getNumberOfFalsePositive());
//					values.add(rd.getNumberOfFalseNegative());
//				}
//				
//				
//				frame.append(size, values);
//			}
//			
//			frame.show();
////			frame.plot(PlotType.BAR);
//		}
//	}
//	
//	private static interface EvaluationConsumer
//	{
//		public void evaluate(List<RuntimeEvaluationObject> evaluatedStuff);
//	}
//	
//	private static class EvaluationThread extends Thread
//	{
//		public EvaluationThread(final EvaluationConsumer consumer, final List<RuntimeEvaluationObject> evaluatedStuff)
//		{
//			super(new Runnable()
//			{
//				@Override
//				public void run()
//				{
//					consumer.evaluate(evaluatedStuff);
//				}
//			});
//		}
//	}
//	
//	private static class RuntimeEvaluation implements EvaluationConsumer
//	{	
//		@Override
//		public void evaluate(List<RuntimeEvaluationObject> evaluatedStuff)
//		{
//			evaluatedStuff = evaluatedStuff.stream()
//				.filter(e -> 
//					/*Arrays.asList(PredefinedERStrategy.JACCARD_PARTITIONED_SORTED_N_GRAMM_SET).contains(e.erStrategy) ||*/
//					
//					((PredefinedERStrategy.JACCARD_PARTITIONED_BLOOM_FILTER.equals(e.erStrategy) || PredefinedERStrategy.BLOOM_SORTED_SET.equals(e.erStrategy)
//						|| PredefinedERStrategy.BETTER_BLOOM_FILTER_N_GRAMM_SET.equals(e.erStrategy) || "BETTER_BLOOM_FILTER_N_GRAMM_SET_2_PHASE".equals(e.erStrategy.name()))
//					&& e.bitsInBloomFilter == 128 && 
//					PredefinedBloomFilter.BITSET_BLOOM_FILTER.equals(e.bloomFilter)))
//				.collect(Collectors.toList());
//			
//			DataFrame<Number> frame = new DataFrame<>(names(evaluatedStuff));
//			
//			for(Integer size : sizes(evaluatedStuff))
//			{
//				List<Double> runtimes = new ArrayList<>();
//				
//				for(RuntimeEvaluationObject bla : evaluatedStuff)
//				{
//					runtimes.add(((double)determineResultData(size, bla).getRunTime())/60000d);
//				}
//				
//				frame.append(size, runtimes);
//			}
//			System.out.println("afasfafa ");
//			frame.plot(PlotType.LINE_AND_POINTS);
//		}	
//	}
//	
//	public static ResultData determineResultData(int size, RuntimeEvaluationObject reo)
//	{
//		for(ResultData rd : reo._resultData)
//		{
//			if(rd.getDataSize() == size)
//			{
//				return rd;
//			}
//		}
//		
//		return null;
//	}
//	
//	public static List<String> names(List<RuntimeEvaluationObject> evaluatedStuff)
//	{
//		List<String> ret = new ArrayList<>();
//		
//		for(RuntimeEvaluationObject bla : evaluatedStuff)
//		{
//			ret.add(prettyString(bla));
//		}
//		
//		return ret;
//	}
//	
//	public static List<Integer> sizes(List<RuntimeEvaluationObject> evaluatedStuff)
//	{
//		Set<Integer> tmp = new HashSet<>();
//		
//		for(RuntimeEvaluationObject bla : evaluatedStuff)
//		{
//			for(ResultData rd : bla._resultData)
//			{
//				tmp.add(rd.getDataSize());
//			}
//		}
//		
//		List<Integer> ret = new ArrayList<>(tmp);
//		Collections.sort(ret);
//		
//		return ret;
//	}
//	
//	private static String prettyString(RuntimeEvaluationObject a)
//	{
//		if(a.erStrategy.name().equalsIgnoreCase(PredefinedERStrategy.JACCARD_PARTITIONED_BLOOM_FILTER.name())  && 
//			PredefinedBloomFilter.BITSET_BLOOM_FILTER.equals(a.bloomFilter))
//		{
//			return a.bitsInBloomFilter +"-BitArray";
//		
//		}
//		else if(a.erStrategy.name().equalsIgnoreCase(PredefinedERStrategy.BLOOM_SORTED_SET.name())  && 
//				PredefinedBloomFilter.BITSET_BLOOM_FILTER.equals(a.bloomFilter))
//		{
//				return a.bitsInBloomFilter +"-BitArrayAsFilter";
//			
//		}
//		else if(a.erStrategy.name().equalsIgnoreCase(PredefinedERStrategy.BETTER_BLOOM_FILTER_N_GRAMM_SET.name())  && 
//				PredefinedBloomFilter.BITSET_BLOOM_FILTER.equals(a.bloomFilter))
//		{
//				return a.bitsInBloomFilter +"-BitArrayFilterOnly";
//			
//		}
//		else if(a.erStrategy.name().equalsIgnoreCase("BETTER_BLOOM_FILTER_N_GRAMM_SET_2_PHASE")  && 
//				PredefinedBloomFilter.BITSET_BLOOM_FILTER.equals(a.bloomFilter))
//		{
//				return a.bitsInBloomFilter +"-BitArray-2-Phasen";
//		}
//		else if(a.erStrategy.name().equalsIgnoreCase(PredefinedERStrategy.JACCARD_PARTITIONED_N_GRAMM_SET.name()))
//		{
//			return "Trivialer Ansatz";
//		}
//		else if(a.erStrategy.name().equalsIgnoreCase(PredefinedERStrategy.JACCARD_PARTITIONED_SORTED_N_GRAMM_SET.name()))
//		{
//				return "Sortier-Ansatz";	
//		}
//		
//		throw new RuntimeException("Dont know what to do: "+ a.erStrategy.name()+ " "+ a.bitsInBloomFilter +" "+ a.bloomFilter);
//	}
//	
//	private static boolean addToExisting(List<RuntimeEvaluationObject> existing, ResultData e)
//	{
//    	for(RuntimeEvaluationObject possibleMatch : existing)
//    	{
//    		if(possibleMatch.add(e))
//    		{
//    			return Boolean.TRUE;
//    		}
//    	}
//    	
//    	return Boolean.FALSE;
//	}
//}