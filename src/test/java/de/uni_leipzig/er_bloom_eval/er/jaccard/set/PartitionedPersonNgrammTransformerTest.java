package de.uni_leipzig.er_bloom_eval.er.jaccard.set;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import de.uni_leipzig.er_bloom_eval.er.EntityResolutionStrategy;
import de.uni_leipzig.er_bloom_eval.er.PredefinedERStrategy;
import de.uni_leipzig.er_bloom_eval.er.jaccard.set.SetPerson;
import de.uni_leipzig.er_bloom_eval.er.n_gramm.DateGramm;
import de.uni_leipzig.er_bloom_eval.er.n_gramm.StrictNGrammTransformer;
import de.uni_leipzig.er_bloom_eval.er.n_gramm.StringPreprocessing;
import de.uni_leipzig.er_bloom_eval.util.io.CsvReader;
import de.uni_leipzig.er_bloom_eval.util.io.PersonTransformer;

public class PartitionedPersonNgrammTransformerTest
{
	private static final String DUMMY_IMPORT = 
			"89550,mia,stephenson,20,verbrugghen street,,brighton,2423,,19550128,42,04 96846341,8427707,6\n" +
			"283290,teileah,rafii,266,truscott street,,port augusta,5291,wa,19250830,29,04 98317026,4233308,9\n" +
			"249540,trey,egan,69,groom street,wondilibi,beaconsfield upper,2607,sa,19090109,34,03 25681215,6276441,4";
	
	private static final List<SetPerson> EXPECTED = Arrays.asList
	(
		new SetPerson(89550l, set("verbrugghen street 20 2423 brighton"), new HashSet<>(Arrays.asList(1955, 0, 28)), set("mia stephenson")),
		new SetPerson(2l, set("truscott street 266 5291 port augusta wa"), new HashSet<>(Arrays.asList(1925, 7, 30)), set("teileah rafii")),
		new SetPerson(249540l, set("groom street 69 2607 beaconsfield upper wondilibi sa"), new HashSet<>(Arrays.asList(1909, 0, 9)), set("trey egan"))
	);
	
	@Test
	public void test()
	{
		StrictNGrammTransformer nGrammTransformer = new StrictNGrammTransformer(1, StringPreprocessing.LOWER_CASE_MERGE_TRIM_EMPTY_SPACES);
		EntityResolutionStrategy<SetPerson> erStrategy = PredefinedERStrategy.SET.erStrategy();
		CsvReader<SetPerson> reader = new CsvReader<>(new PersonTransformer<>(nGrammTransformer, new DateGramm(0), erStrategy.getDataStructureFactory()));
		
		List<SetPerson> actual = reader.readCsvFromString(DUMMY_IMPORT);
		
		Assert.assertEquals(EXPECTED.size(), actual.size());
		
		for(int i=0; i< EXPECTED.size(); i++)
		{
			Assert.assertEquals(EXPECTED.get(i).getAddressNGramms(), actual.get(i).getAddressNGramms());
			Assert.assertEquals(EXPECTED.get(i).getBirthdayNgramms(), actual.get(i).getBirthdayNgramms());
			Assert.assertEquals(EXPECTED.get(i).getNameNGramms(), actual.get(i).getNameNGramms());
		}
	}
	
	private static Set<String> set(String t)
	{
		Set<String> ret = new HashSet<>();
		
		for(char c : t.toCharArray())
		{
			ret.add(""+ c);
		}
		
		return ret;
	}
}