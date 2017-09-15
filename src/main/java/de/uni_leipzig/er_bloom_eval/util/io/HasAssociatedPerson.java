package de.uni_leipzig.er_bloom_eval.util.io;

import java.io.Serializable;

/**
 * Suggests that an Object is associated with a single {@link Person}.
 * 
 * <p>
 * Hence this object has the possibility to access
 * {@link Person#getId() the id of the associated person person} by leveraging
 * {@link HasAssociatedPerson#getId()}.
 * </p>
 * 
 * @author Maik Fröbe
 */
public interface HasAssociatedPerson extends Serializable
{
	/**
	 * @return {@link Person#getId() The id} of the associated person.
	 */
	public long getId();
}