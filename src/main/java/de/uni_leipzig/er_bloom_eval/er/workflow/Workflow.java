package de.uni_leipzig.er_bloom_eval.er.workflow;

import de.uni_leipzig.er_bloom_eval.er.EntityResolutionStrategy;
import de.uni_leipzig.er_bloom_eval.er.PredefinedERStrategy;
import de.uni_leipzig.er_bloom_eval.er.n_gramm.DateGramm;
import de.uni_leipzig.er_bloom_eval.er.n_gramm.NGrammTransformer;
import de.uni_leipzig.er_bloom_eval.er.n_gramm.StrictNGrammTransformer;
import de.uni_leipzig.er_bloom_eval.er.n_gramm.StringPreprocessing;
import de.uni_leipzig.er_bloom_eval.util.ArgumentsUtil;
import de.uni_leipzig.er_bloom_eval.util.io.HasAssociatedPerson;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * Describes the steps to perform the entity resolution with an given configuration.
 *
 * @author m.froebe
 */
public abstract class Workflow
{
	private final Namespace parsedArgs;

	/**
	 *
	 * @param parsedArgs
	 * 		The configuration for the execution of the workflow.
	 */
	public Workflow(Namespace parsedArgs)
	{
		this.parsedArgs = parsedArgs;
	}

	/**
	 * Executes the entity resolution workflow.
	 */
	public abstract <V extends HasAssociatedPerson> void executeWorkflow();

	protected int getDateGrammRadius()
	{
		return this.parsedArgs.getInt(ArgumentsUtil.PARAM_DATE_GRAMM_RADIUS);
	}

	protected DateGramm getDategrammTransformer()
	{
		return new DateGramm(getDateGrammRadius());
	}

	protected <V extends HasAssociatedPerson> EntityResolutionStrategy<V> getEntityResolutionStrategy()
	{
		final EntityResolutionStrategy<V> ret = PredefinedERStrategy.valueOf(this.parsedArgs.getString(ArgumentsUtil.PARAM_ER_STRATEGY)).erStrategy();
		ret.doPreparation(this.parsedArgs);

		return ret;
	}

	protected String getFirstInputFile()
	{
		return (String) this.parsedArgs.getList(ArgumentsUtil.PARAM_INPUT).get(0);
	}

	protected int getNGrammLength()
	{
		return this.parsedArgs.getInt(ArgumentsUtil.PARAM_N_GRAMM_LENGTH);
	}

	protected NGrammTransformer getNGrammTransformer()
	{
		return new StrictNGrammTransformer(getNGrammLength(), getStringPreprocessing());
	}

	protected String getOutputFile()
	{
		return this.parsedArgs.getString(ArgumentsUtil.PARAM_OUTPUT);
	}

	protected String getSecondInputFile()
	{
		return (String) this.parsedArgs.getList(ArgumentsUtil.PARAM_INPUT).get(1);
	}

	protected StringPreprocessing getStringPreprocessing()
	{
		return StringPreprocessing.valueOf(this.parsedArgs.getString(ArgumentsUtil.PARAM_STRING_PREPROCESSING));
	}

	protected double getThreshold()
	{
		return this.parsedArgs.getDouble(ArgumentsUtil.PARAM_SIMILARITY_THRESHOLD);
	}
}