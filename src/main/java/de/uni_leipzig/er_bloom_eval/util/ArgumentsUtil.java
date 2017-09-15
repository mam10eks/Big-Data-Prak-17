package de.uni_leipzig.er_bloom_eval.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_leipzig.er_bloom_eval.App;
import de.uni_leipzig.er_bloom_eval.er.PredefinedERStrategy;
import de.uni_leipzig.er_bloom_eval.er.n_gramm.StringPreprocessing;
import de.uni_leipzig.er_bloom_eval.util.bloom.PredefinedBloomFilter;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * Helper to {@link App parse the arguments} that are passed to the program.
 *
 * @author Maik Fröbe
 */
public final class ArgumentsUtil
{
	public static final String PARAM_INPUT = "input";

	public static final String PARAM_OUTPUT = "output";

	public static final String PARAM_N_GRAMM_LENGTH = "nGrammLength";

	public static final String PARAM_DATE_GRAMM_RADIUS = "dateGrammRadius";

	public static final String PARAM_STRING_PREPROCESSING = "stringPreprocessing";

	public static final String PARAM_ER_STRATEGY = "entityResolutionStrategy";

	public static final String PARAM_SIMILARITY_THRESHOLD = "similarityThreshold";

	public static final String PARAM_BITS_IN_BLOOM_FILTER = "bitsInBloomFilter";

	public static final String PARAM_INTERNAL_BLOOM_FILTER = "internalBloomFilter";

	private static final Logger LOGGER = LoggerFactory.getLogger(ArgumentsUtil.class);

	private static void checkThatAssociationsAreCorrect(Namespace namespace, ArgumentParser argumentParser) throws ArgumentParserException
	{
		if(namespace.getString(PARAM_ER_STRATEGY).toLowerCase().contains("bloom") && !(namespace.get(PARAM_BITS_IN_BLOOM_FILTER) instanceof Integer))
		{
			throw new ArgumentParserException("IF a bloom filter is used you need to specify the amount of bits for the bloom-filter.", argumentParser);
		}
	}

	/**
	 * Defines and returns the {@link ArgumentParser argument parser} that is responsible to guide the user
	 * through the usage of this program and parse the user-input.
	 *
	 * @return
	 * 		The {@link ArgumentParser} that forms the interface between the user and this program.
	 */
	private static ArgumentParser createArguments()
	{
		final ArgumentParser ret = ArgumentParsers.newArgumentParser("EntityResolution")
				.defaultHelp(Boolean.TRUE)
				.description("An evaluation of entity resolution using the jaccard index leveraging set comparison by bloom filters.");

		ret.addArgument("--"+ PARAM_INPUT, "-i")
		.nargs(2)
		.required(Boolean.TRUE)
		.type(String.class)
		.help("Gives two csv-seperated files with person records. "
				+ "The Goal is to find similar recors between both files. "
				+ "Both files need to be in a predefined format.");

		ret.addArgument("--"+ PARAM_OUTPUT, "-o")
		.required(Boolean.TRUE)
		.help("Defines the output file where all similar pairs of records are written to.");

		ret.addArgument("--"+ PARAM_N_GRAMM_LENGTH, "-n")
		.required(Boolean.FALSE)
		.setDefault(3)
		.type(Integer.class)
		.help("Defines the length of the n-gramms that are used to execute the fuzzy search between two objects."
				+ "I.e. for strict n-gramms with n=3,  a String like \"An Example\" would yield to the following n-gramms: "
				+ "[\"An \", \"n E\", \"Exa\", \"xam\", \"amp\", \"mpl\", \"ple\"]. "
				+ "Please note that Strings are preprocessed by --"+PARAM_STRING_PREPROCESSING
				+", and that different n-gramm-strategies could be applied." );

		ret.addArgument("--"+ PARAM_DATE_GRAMM_RADIUS, "-r")
		.required(Boolean.FALSE)
		.setDefault(1)
		.type(Integer.class)
		.help("Defines the fuzziness that is used while transforming a date into a set of integers. "
				+ "For each component <code>c</code> of a date the resulting integer-set will be enriched by all elements in the interval [c-radius, c+radius].");

		ret.addArgument("--"+ PARAM_STRING_PREPROCESSING, "-s")
		.required(Boolean.FALSE)
		.choices(enumValues(StringPreprocessing.class))
		.type(String.class)
		.setDefault(StringPreprocessing.LOWER_CASE_MERGE_TRIM_EMPTY_SPACES.name())
		.help("Describes the way in which every string is normalized. Possible strategies are:\n "
				+ "--"+ StringPreprocessing.LOWER_CASE_MERGE_TRIM_EMPTY_SPACES +": transform every string to lower case and remove accumulations of neighboring whitespaces. "
				+ "I.e. \" A b   x   s \" will be normalized to \"a b x s\".\n"
				+ "--"+ StringPreprocessing.MERGE_TRIM_EMPTY_SPACES +": beahaves like --"+ StringPreprocessing.LOWER_CASE_MERGE_TRIM_EMPTY_SPACES +", with the difference, that large and lower case is not changed. "
				+ "I.e. \" A b   x   s \" will be normalized to \"A b x s\"");

		ret.addArgument("--"+ PARAM_SIMILARITY_THRESHOLD)
		.required(Boolean.FALSE)
		.setDefault(0.9d)
		.type(Double.class)
		.help("Specifies the minimum threshold that must be met in order that a pair is considered as similar.");

		ret.addArgument("--"+ PARAM_ER_STRATEGY, "-er")
		.required(Boolean.TRUE)
		.choices(enumValues(PredefinedERStrategy.class))
		.type(String.class)
		.help("Specifies the entity resolution strategy that is used to solve the problem. They are specified in the following way:"+ prettyErStrategyDescriptions());

		ret.addArgument("--"+ PARAM_BITS_IN_BLOOM_FILTER)
		.required(Boolean.FALSE)
		.type(Integer.class)
		.setDefault(320)
		.help("Specifies the number of bits in the bloom filter that is used in connection with a bloom entity resolution strategy.");

		ret.addArgument("--"+ PARAM_INTERNAL_BLOOM_FILTER)
		.required(Boolean.FALSE)
		.type(String.class)
		.choices(enumValues(PredefinedBloomFilter.class))
		.setDefault(PredefinedBloomFilter.BITSET_BLOOM_FILTER.name())
		.help("Specifies the internal bloom filter that is used in connection with a bloom entity resolution strategy.");

		return ret;
	}

	public static String[] enumValues(Class<? extends Enum<?>> e)
	{
		final List<String> ret = new ArrayList<>();

		for(final Enum<?> val :e.getEnumConstants())
		{
			ret.add(val.name());
		}

		return ret.toArray(new String[ret.size()]);
	}

	/**
	 * Parse the passed arguments.
	 *
	 * <p>
	 * An illegal input will cause the program to exit and writes usage informations to stdout.
	 * </p>
	 *
	 * @param args
	 * 		All arguments that are passed to this program.
	 * 		I.e. the parameters of the {@link App#main(String[]) entry-point main-method}.
	 * @return
	 * 		The parsed parameters.
	 */
	public static Namespace parseArguments(String[] args)
	{
		final ArgumentParser argumentParser = createArguments();

		try
		{
			final Namespace ret = argumentParser.parseArgs(args);

			LOGGER.info("########################################## STARTING WITH SETTINGS: #########################################################" +
					"\n#" +
					"\n# "+ PARAM_INPUT +":\t\t\t[ '"+ ret.getList(PARAM_INPUT).get(0) +"', '"+ ret.getList(PARAM_INPUT).get(1) +"' ]" +
					"\n# "+ PARAM_OUTPUT+":\t\t\t'"+ ret.get(PARAM_OUTPUT) +"'" +
					"\n# "+ PARAM_ER_STRATEGY +":\t"+ ret.get(PARAM_ER_STRATEGY) +
					"\n# "+ PARAM_N_GRAMM_LENGTH +":\t\t\t"+ ret.get(PARAM_N_GRAMM_LENGTH) +
					"\n# "+ PARAM_DATE_GRAMM_RADIUS +":\t\t"+ ret.get(PARAM_DATE_GRAMM_RADIUS) +
					"\n# "+ PARAM_STRING_PREPROCESSING +":\t\t"+ ret.get(PARAM_STRING_PREPROCESSING) +
					"\n# "+ PARAM_SIMILARITY_THRESHOLD +":\t\t"+ ret.get(PARAM_SIMILARITY_THRESHOLD) +
					"\n# "+ PARAM_BITS_IN_BLOOM_FILTER +":\t\t"+ ret.get(PARAM_BITS_IN_BLOOM_FILTER) +
					"\n#" +
					"\n############################################################################################################################\n");

			checkThatAssociationsAreCorrect(ret, argumentParser);

			return ret;
		}
		catch(final ArgumentParserException exception)
		{
			argumentParser.handleError(exception);

			System.exit(1);
			throw new RuntimeException();
		}
	}

	private static String prettyErStrategyDescriptions()
	{
		String ret = "";

		for(final PredefinedERStrategy erStrategy : PredefinedERStrategy.values())
		{
			ret += "\n\t"+ erStrategy.name() +": "+ erStrategy.getDescription();
		}

		return ret;
	}

	private ArgumentsUtil()
	{
		// hide utility class constructor
	}
}