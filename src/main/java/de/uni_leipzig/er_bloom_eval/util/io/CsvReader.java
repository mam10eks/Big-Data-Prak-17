package de.uni_leipzig.er_bloom_eval.util.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Parses and transforms a CSV as input into an {@link List list} of records of type <code>V</code>.
 * 
 * @author Maik Fröbe
 * @param <V>
 * 		The type in which a record in the CSV-File should be transformed.
 */
public class CsvReader<V extends HasAssociatedPerson>
{
	private final PersonTransformer<V> _personTransformer;
	
	/**
	 * @param transformer
	 * 		Is applied to each {@link CSVRecord} that is contained in a processed input in order to receive objects of type <code>V</code>.
	 */
	public CsvReader(PersonTransformer<V> transformer)
	{
		this._personTransformer = transformer;
	}
	
	/**
	 * Parses the passed content.
	 * 
	 * @param content
	 * 		A String in the {@link CsvFields supported CSV-Format}.
	 * @return
	 * 		A transformation of each {@link CSVRecord} by leveraging the {@link CsvReader#_transformer defined transformation rule}.
	 */
	public List<V> readCsvFromString(String content)
	{
		try
		{
			List<V> ret = new ArrayList<>();
			
			for(CSVRecord record : CSVParser.parse(content, CSVFormat.DEFAULT).getRecords())
			{
				ret.add(_personTransformer.apply(record));
			}
			
			return ret;
		}
		catch(IOException ioException)
		{
			throw new RuntimeException(ioException);
		}
	}
	
	/**
	 * Parses the content of the file.
	 * 
	 * @param file
	 * 		Points to a file with a content in the {@link CsvFields supported CSV-Format}.
	 * @return
	 * 		A transformation of each {@link CSVRecord} by leveraging the {@link CsvReader#_transformer defined transformation rule}.
	 */
	public List<V> readCsvFromFile(String file)
	{
		try
		{
			return readCsvFromString(new String(Files.readAllBytes(Paths.get(file))));
		}
		catch(IOException ioException)
		{
			throw new RuntimeException(ioException);
		}
	}
}