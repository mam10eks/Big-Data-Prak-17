package de.uni_leipzig.er_bloom_eval.er;

import java.io.Serializable;

/**
 * A similarity function, which determines the similarity between objects.
 * <br>
 * Each similarity function is a mapping:
 * <p>
 * s: V x V -> [0,1]
 * </p>
 * S satisfies for all i,j of type V:
 * <ul>
 * 		<li>s(i,j) = s(j,i)</li>
 * 		<li>1 = s(i,i) >= s(i,j)</li>
 * </ul>
 * 
 * @author Maik Fröbe
 * @param <V> the type of objects that may be compared by this similarity function.
 * 
 */
public interface SimilarityFunction<V> extends Serializable
{
	/**
	 * Determines the similarity between the passed arguments.
	 * <p>
	 * Similarities should always be between inclusive 0 and inclusive 1.
	 * </p>
	 * 
	 * @param a
	 * 		the first object to be compared.
	 * @param b
	 * 		the second object to be compared.
	 * @return
	 * 		Similarity between <code>a</code> and <code>b</code>.<br>
	 * 		Is always in the interval [0,1], and satisfies for all i,j of type V:
	 * 		<p>
	 *			<ul>
	 *				<li>similarity(i,j) = similarity(j,i)</li>
	 *				<li>1 = similarity(i,i) >= similarity(i,j)</li>
	 *			</ul>
	 * 		</p>
	 */
	public double similarity(V a, V b);
}