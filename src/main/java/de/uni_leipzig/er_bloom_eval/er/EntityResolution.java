package de.uni_leipzig.er_bloom_eval.er;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import de.uni_leipzig.er_bloom_eval.util.io.HasAssociatedPerson;

/**
 * Uses a appropriate {@link EntityResolutionStrategy entity resolution strategy} to
 * {@link EntityResolution#calculateSimilarPairs(List, List) perform the entity resolution between two data sets}
 * of an associated type <code>V</code>.
 * 
 * @author Maik Fröbe
 * @param <V> the type of objects that may be compared.
 * @see HasAssociatedPerson
 * @see SimilarityFunction
 */
public final class EntityResolution<V extends HasAssociatedPerson>
{
	private final SimilarityFunction<V> _similarityFunction;
	
	private final double _threshold;
	
	/**
	 * @param entityResolutionStrategy
	 * 		The {@link EntityResolutionStrategy#getSimilarityFunction() similarity function of this strategy}
	 * 		will be used to determine the similarity between records.
	 * @param threshold
	 * 		A following {@link EntityResolution#calculateSimilarPairs(List, List) entity resolution}
	 * 		will use this threshold as a lower boundary for similar records.
	 * 		I.e. all pais with a similarity below <code>threshold</code> will be discarded.
	 */
	public EntityResolution(EntityResolutionStrategy<V> entityResolutionStrategy, double threshold)
	{
		this._similarityFunction = entityResolutionStrategy.getSimilarityFunction();
		this._threshold = threshold;
	}
	
	/**
	 * Performs the entity resolution between all pairs of <code>a<code> and <code>b</code>.<br>
	 * All pairs with a {@link EntityResolution#_similarityFunction similarity} below the {@link EntityResolution#_threshold defined threshold} will be discarded.
	 * 
	 * @param a
	 * 		The first data set, for which similar entries in <code>b</code> should be found.
	 * @param b
	 * 		The second data set, for which similar entries in <code>a</code> should be found.
	 * @return
	 * 		The results of the entity resolution as a list of triple's with the structure
	 * 		<p>
	 * 		<code>
	 * 			[&lt;ID_OF_OBJECT_FROM_A&gt;, &lt;ID_OF_OBJECT_FROM_B&gt;, &lt;SIMILARITY_BETWEEN_A_AND_B&gt;]
	 * 		</code>
	 * 		</p>
	 * 		with a similarity above (greater than or equal to) the {@link EntityResolution#_threshold}.
	 * 
	 * @see HasAssociatedPerson
	 * @see SimilarityFunction
	 */
	public List<Triple<Long, Long, Double>> calculateSimilarPairs(List<V> a, List<V> b)
	{
		List<Triple<Long, Long, Double>> ret = new ArrayList<>();
		
		for(V objectFromA : a)
		{
			for(V objectFromB : b)
			{
				Double similarity = _similarityFunction.similarity(objectFromA, objectFromB);
				
				if(similarity >= _threshold)
				{
					ret.add(new ImmutableTriple<>(objectFromA.getId(), objectFromB.getId(), similarity));
				}
			}
		}
		
		return ret;
	}
}