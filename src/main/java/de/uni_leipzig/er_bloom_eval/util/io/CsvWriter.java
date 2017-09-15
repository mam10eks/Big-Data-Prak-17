package de.uni_leipzig.er_bloom_eval.util.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Triple;

public final class CsvWriter
{	
	private CsvWriter()
	{
		// hide utility class constructor
	}
	
	public static void writeToFile(String file, List<Triple<Long, Long, Double>> content)
	{
		try
		{
			Files.write(Paths.get(file), toCsv(content), StandardCharsets.UTF_8);
		}
		catch (IOException exception)
		{
			throw new RuntimeException("Couldnt write the content to the file '"+ file +"'.", exception);
		}
	}
	
	private static Iterable<String> toCsv(List<Triple<Long, Long, Double>> content)
	{
		List<String> ret = new ArrayList<>();
		
		for(Triple<Long, Long, Double> element : content)
		{
			ret.add(element.getLeft() +","+ element.getMiddle() +","+ element.getRight());
		}
		
		return ret;
	}
}