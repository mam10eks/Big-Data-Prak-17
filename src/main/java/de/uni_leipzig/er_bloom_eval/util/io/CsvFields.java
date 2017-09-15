package de.uni_leipzig.er_bloom_eval.util.io;

/**
 * Describes where to find relevant informations in the supported CSV-file.
 * 
 * @author Maik Fröbe
 */
public enum CsvFields
{
	ID(0),
	FIRST_NAME(1),
	LAST_NAME(2),
	ADDRESS_NUMBER(3),
	ADDRESS_STREET(4),
	ADDRESS_DONT_KNOW_WHAT(5),
	ADDRESS_SUBURB(6),
	ADDRESS_ZIP_CODE(7),
	ADDRESS_STATE(8),
	BIRTHDAY(9);
	
	private final int _columnNumber;
	
	/**
	 * @param columnNumber
	 * 		The column number of the field in the supported CSV-file.
	 */
	private CsvFields(int columnNumber)
	{
		_columnNumber = columnNumber;
	}
	
	/**
	 * @return
	 *		The column number of the field in the supported CSV-file.
	 */
	public int getColumnNumber()
	{
		return _columnNumber;
	}
}