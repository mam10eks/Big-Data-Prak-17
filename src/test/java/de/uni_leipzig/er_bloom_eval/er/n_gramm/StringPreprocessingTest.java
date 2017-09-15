package de.uni_leipzig.er_bloom_eval.er.n_gramm;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class StringPreprocessingTest
{
	@Parameters
	public static Collection<Object[]> params()
	{
		return Arrays.asList
		(
			new Object[]{StrictNGrammTransformerTest.EXAMPLE_LONG_STRING, StringPreprocessing.LOWER_CASE_MERGE_TRIM_EMPTY_SPACES,"this is an example"},
			new Object[]{StrictNGrammTransformerTest.EXAMPLE_MIDDLE_STRING, StringPreprocessing.LOWER_CASE_MERGE_TRIM_EMPTY_SPACES, StrictNGrammTransformerTest.EXAMPLE_MIDDLE_STRING},
			new Object[]{StrictNGrammTransformerTest.EXAMPLE_LONG_STRING, StringPreprocessing.MERGE_TRIM_EMPTY_SPACES,"This is an example"},
			new Object[]{StrictNGrammTransformerTest.EXAMPLE_MIDDLE_STRING, StringPreprocessing.MERGE_TRIM_EMPTY_SPACES, StrictNGrammTransformerTest.EXAMPLE_MIDDLE_STRING}
		);
	}
	
	private final String _input;
	
	private final StringPreprocessing _transformer;
	
	private final String _expected;
	
	public StringPreprocessingTest(String input, StringPreprocessing transformer, String expected)
	{
		this._input = input;
		this._transformer = transformer;
		this._expected = expected;
	}
	
	@Test
	public void test()
	{
		Assert.assertEquals(_expected, _transformer.apply(_input));
	}
}