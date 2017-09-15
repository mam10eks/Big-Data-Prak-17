package de.uni_leipzig.er_bloom_eval.util.io;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.csv.CSVRecord;

import de.uni_leipzig.er_bloom_eval.er.n_gramm.DateGramm;
import de.uni_leipzig.er_bloom_eval.er.n_gramm.NGrammTransformer;

@SuppressWarnings("serial")
public class PersonTransformer<V extends HasAssociatedPerson> implements Serializable
{
	static final String DATE_FORMAT = "yyyyMMdd";
	
	private final NGrammTransformer _stringTransformer;
	
	private final DateGramm _dateTransformer;
	
	private final InternalDataStructureFactory<V> _internalTransformer;
	
	public PersonTransformer(NGrammTransformer stringTransformer, DateGramm dateTransformer, InternalDataStructureFactory<V> internalTransformer)
	{
		this._stringTransformer = stringTransformer;
		this._dateTransformer = dateTransformer;
		this._internalTransformer = internalTransformer;
	}
	
	public V apply(CSVRecord csvRecord)
	{
		long id = Long.parseLong(csvRecord.get(CsvFields.ID.getColumnNumber()));
		Date birthDay = parse(csvRecord.get(CsvFields.BIRTHDAY.getColumnNumber()));
		String fullName = csvRecord.get(CsvFields.FIRST_NAME.getColumnNumber()) +" "+ csvRecord.get(CsvFields.LAST_NAME.getColumnNumber());
		String fullAddress = csvRecord.get(CsvFields.ADDRESS_STREET.getColumnNumber()) +" "+ csvRecord.get(CsvFields.ADDRESS_NUMBER.getColumnNumber())+
			" "+ csvRecord.get(CsvFields.ADDRESS_ZIP_CODE.getColumnNumber())+" "+ csvRecord.get(CsvFields.ADDRESS_SUBURB.getColumnNumber()) +
			" "+ csvRecord.get(CsvFields.ADDRESS_DONT_KNOW_WHAT.getColumnNumber()) +" "+csvRecord.get(CsvFields.ADDRESS_STATE.getColumnNumber());
		
		return _internalTransformer.apply(id, _stringTransformer.apply(fullAddress), _dateTransformer.apply(birthDay),
				_stringTransformer.apply(fullName));
	}
	
	public static Date parse(String date)
	{
		try
		{
			if(date == null || date.isEmpty())
			{
				return null;
			}
			
			return new SimpleDateFormat(DATE_FORMAT).parse(date);
		}
		catch (ParseException e)
		{
			throw new RuntimeException("Couldnt parse the date '"+ date +"', since it seems to be not in the expected format: '"+ DATE_FORMAT +"'.");
		}
	}
}