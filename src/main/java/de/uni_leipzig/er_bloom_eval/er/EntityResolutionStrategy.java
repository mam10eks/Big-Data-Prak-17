package de.uni_leipzig.er_bloom_eval.er;

import org.apache.commons.csv.CSVRecord;

import de.uni_leipzig.er_bloom_eval.er.workflow.SingleStageWorkflow;
import de.uni_leipzig.er_bloom_eval.er.workflow.Workflow;
import de.uni_leipzig.er_bloom_eval.util.io.HasAssociatedPerson;
import de.uni_leipzig.er_bloom_eval.util.io.InternalDataStructureFactory;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * An EntityResolutionStrategy is responsible for two things:
 * <ul>
 * 		<li>{@link EntityResolutionStrategy#createPersonTransformer(Function, Function) Transforming} an input record 
 * 			(a {@link Person}) into a type <code>V</code>, which is used within the strategy.</li>
 * 		<li>{@link EntityResolutionStrategy#getSimilarityFunction() Calculate the similarity} between two 
 * 			transformed objects of type <code>V</code>.</li>
 * </ul>
 * 
 * @author Maik Fröbe
 * @param <V> the type of objects that are internally used in the strategy.
 */
public class EntityResolutionStrategy<V extends HasAssociatedPerson>
{
	private final SimilarityFunction<V> _similarityFunction;
	
	private final InternalDataStructureFactory<V> _dataStructureFactory;
	
	/**
	 * @param similarityFunction
	 * 		A function that is used to calculate the similarity between two objects of type <code>V</code>.
	 * @param dataStructureFactory
	 * 		A function that is used to transform a person into a object of type <code>V</code>.
	 */
	public EntityResolutionStrategy(SimilarityFunction<V> similarityFunction, InternalDataStructureFactory<V> dataStructureFactory)
	{
		this._similarityFunction = similarityFunction;
		this._dataStructureFactory = dataStructureFactory;
	}
	
	/**
	 * Get a transformer that is used to transform an input record (a {@link CSVRecord}) into a type <code>V</code>.
	 * 
	 * @return
	 * 		The factory that is used to map persons into the type <code>V</code>.
	 */
	public InternalDataStructureFactory<V> getDataStructureFactory()
	{
		return _dataStructureFactory;
	}
	
	/**
	 * Is executed in order to prepare internal objects for an entity resolution.
	 * By default, this is an no-op.
	 * You should overwrite this method if a strategy needs such a preparation.
	 * 
	 * @param args
	 */
	public void doPreparation(Namespace args)
	{
		
	}
	
	/**
	 * 
	 * @param parsedArgs
	 * @return
	 */
	public Workflow getWorkflow(Namespace parsedArgs)
	{
		return new SingleStageWorkflow(parsedArgs);
	}

	/**
	 * @return
	 * 		A function that is used to calculate the similarity between two objects of type <code>V</code>.
	 */
	public SimilarityFunction<V> getSimilarityFunction()
	{
		return _similarityFunction;
	}
}