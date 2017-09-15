package de.uni_leipzig.er_bloom_eval.er;

import org.junit.Assert;
import org.junit.Test;

import de.uni_leipzig.er_bloom_eval.er.PredefinedERStrategy;

public class EntityResolutionStrategyTest
{
	@Test
	public void testAllPredefinedErStrategiesCouldBeInstantiated()
	{
		for(PredefinedERStrategy predefinedErStrategy : PredefinedERStrategy.values())
		{
			Assert.assertNotNull(predefinedErStrategy.erStrategy());
		}
	}
}