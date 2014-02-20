package SQLQT_Utilities;
/**
 * 
 * @author Anastasiya Goncharova
 *
 */
public final class QueryParser
{
	public static String getImagePathFromQuery(String query)
	{
		String path = "";
		int idIndex = -1;
		String[] insertValues = null;
		
		String values = query.substring(query.lastIndexOf("(")+1, query.lastIndexOf(")"));
		insertValues = values.split(",");
		int length = insertValues.length;
		for(int i=0; i<length; i++) 
		{
			insertValues[i] = insertValues[i].trim();
			if(insertValues[i].startsWith("'"))
				insertValues[i] = insertValues[i].substring(1, insertValues[i].length()-1);
		}
		
		if(query.contains(") values") || query.contains(") VALUES") || query.contains(")values") || query.contains(")VALUES"))
		{
			String columns = query.substring(query.indexOf("(")+1, query.indexOf(")"));
			String[] columnNames = columns.split(",");
			for(int i=0; i<columnNames.length; i++)
				if(columnNames[i].contains("image"))
				{
					idIndex = i;
					break;
				}
			
			if(idIndex > -1) path = insertValues[idIndex];
		}
		else path = insertValues[insertValues.length-1];
		
		return path;
	}
}
