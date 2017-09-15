package de.uni_leipzig.er_bloom_eval;

import de.uni_leipzig.er_bloom_eval.er.EntityResolution;
import de.uni_leipzig.er_bloom_eval.er.EntityResolutionStrategy;
import de.uni_leipzig.er_bloom_eval.er.PredefinedERStrategy;
import de.uni_leipzig.er_bloom_eval.er.workflow.Workflow;
import de.uni_leipzig.er_bloom_eval.util.ArgumentsUtil;
import de.uni_leipzig.er_bloom_eval.util.io.HasAssociatedPerson;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * The {@link App#main(String[]) Entry-Point} to this program.
 * Is used to perform the entity-resolution in a defined way for a given input.
 * 
 * <p>
 * Parameters that are passed to the main method are parsed by the {@link ArgumentsUtil}.
 * </p>
 * 
 * @author Maik Fröbe
 */
public class App
{	
	private App()
	{
		// hide entry point class constructor
	}
	
	/**
	 * Executes the entity resolution.
	 * 
	 * <p>
	 * The behaviour is defined by the passed arguments.
	 * </p>
	 * 
	 * @param args
	 * 		Will be {@link ArgumentsUtil parsed} in order to execute the correct variant of the {@link EntityResolution entity resolution}.
	 */
	public static <V extends HasAssociatedPerson> void main(String[] args)
	{
		Namespace parsedArgs = ArgumentsUtil.parseArguments(args);

		EntityResolutionStrategy<V> entityResolutionStrategy = PredefinedERStrategy.valueOf(parsedArgs.getString(ArgumentsUtil.PARAM_ER_STRATEGY)).erStrategy();
		Workflow workflow = entityResolutionStrategy.getWorkflow(parsedArgs);
		
		workflow.executeWorkflow();
	}
}