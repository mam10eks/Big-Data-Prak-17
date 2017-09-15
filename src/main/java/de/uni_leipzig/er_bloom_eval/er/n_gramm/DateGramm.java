package de.uni_leipzig.er_bloom_eval.er.n_gramm;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Transforms a Date into a set of integers.
 * Thereby, the day, month, and week component of a date are returned in that set.
 * 
 * <p>
 * Besides that, the defined {@link DateGramm#_radius radius} can apply a fuzziness where for each component <code>c</code>
 * all elements out of the interval <code>[c - _radius, c + _raduis]</code> are contained in the resulting integer set. 
 * </p>
 * 
 * @author Maik Fröbe
 */
@SuppressWarnings("serial")
public class DateGramm implements Serializable
{
	private final int _radius;
	
	/**
	 * @param radius
	 * 		Defines the fuzziness that is used while transforming a date into a set of integers.
	 * 		For each component <code>c</code> of a date the resulting integer-set will be enriched by
	 * 		all elements in the interval <code>[c-radius, c+radius]</code>.
	 */
	public DateGramm(int radius)
	{
		this._radius = radius;
	}

	/**
	 * Applies {@link DateGramm the strategy} for the defined {@link DateGramm#_radius radius} to the passed parameter.
	 *  
	 * @param date
	 * 		The date that should be processed.
	 * @return
	 * 		The result of the application to the <code>date</code>-parameter.
	 */
	public Set<Integer> apply(Date date)
	{
		if(date == null)
		{
			return Collections.emptySet();
		}
			
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
			
		List<Integer> dateFields = Arrays.asList(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		Set<Integer> ret = new HashSet<>();
		
		for(int i=0; i< _radius+1; i++)
		{
			for(int dateCompoment : dateFields)
			{
				ret.add(dateCompoment +i);
				ret.add(dateCompoment -i);
			}
		}
		
		return ret;
	}
}