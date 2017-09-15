package de.uni_leipzig.er_bloom_eval.er.n_gramm;

import de.uni_leipzig.er_bloom_eval.er.EntityResolution;
import de.uni_leipzig.er_bloom_eval.util.io.CsvReader;
import de.uni_leipzig.er_bloom_eval.util.io.PersonTransformer;

/**
 * Defines different strategies that could be used to normalize {@link String strings}
 * during the preprocessing of the {@link EntityResolution entity resolution}.
 * 
 * <p>
 * The idea is to remove and/or change parts out of a String that doesn't contribute to its content,
 * and hence improve the content-based similarity.
 * In our setting this could be the {@link StringPreprocessing#MERGE_TRIM_EMPTY_SPACES merging of neighbouring spaces}
 * and/or {@link StringPreprocessing#LOWER_CASE_MERGE_TRIM_EMPTY_SPACES forgetting the case}. 
 * </p>
 * 
 * <p>
 * This preprocessing is applied during the phase of the {@link CsvReader#readCsvFromString parsing-process of the input}.
 * </p>
 * 
 * @author Maik Fröbe
 * @see CsvReader
 * @see PersonTransformer
 */
public enum StringPreprocessing
{
	/**
	 * Transforms every string by removeing accumulations of neighboring white spaces.
	 * <p>
	 * I.e. <code>" A&nbsp;b&nbsp;&nbsp;&nbsp;x&nbsp;&nbsp;&nbsp;s "</code> will be normalized to <code>"A&nbsp;b&nbsp;x&nbsp;s"</code>.
	 * </p>
	 */
	MERGE_TRIM_EMPTY_SPACES(new StringPreprocessingFunction()
		{
			@Override
			public String apply(String s)
			{
				return s.trim().replaceAll("\\s+", " ");
			}
		}),
	
	/**
	 * Transforms every string to lower case and remove accumulations of neighboring white spaces.
	 * <p>
	 * I.e. <code>"&nbsp;A&nbsp;b&nbsp;&nbsp;&nbsp;x&nbsp;&nbsp;s&nbsp;"</code> will be normalized to <code>"a&nbsp;b&nbsp;x&nbsp;s"</code>.
	 * </p>
	 */
	LOWER_CASE_MERGE_TRIM_EMPTY_SPACES(new StringPreprocessingFunction()
		{
			@Override
			public String apply(String s)
			{
				return s.trim().replaceAll("\\s+", " ").toLowerCase();
			}
		});
	
	private static interface StringPreprocessingFunction
	{
		public String apply(String s);
	}
	
	private final StringPreprocessingFunction _preprocessing;
	
	/**
	 * @param preprocessing
	 * 		The strategy that should be applied to strings when {@link StringPreprocessing#apply(String)} is called.
	 */
	private StringPreprocessing(StringPreprocessingFunction preprocessing)
	{
		_preprocessing = preprocessing;
	}
	
	/**
	 * Normalize the passed String by leveraging the associated {@link StringPreprocessing strategy}.
	 * 
	 * @param s
	 * 		The string that should be normalized.
	 * @return
	 * 		A normalized copy of <code>s</code>.
	 */
	public String apply(String s)
	{
		return _preprocessing.apply(s);
	}
}