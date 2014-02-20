package SQLQT_Utilities;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
/**
 * 
 * @author Anastasiya Goncharova
 *
 */
public final class FileManager 
{
	public static Object getByteArrayFromFile(String filePath)
	{
		byte[] resultArray = null;
		InputStream is = null;
		try
		{
			is = FileManager.class.getClass().getResourceAsStream(filePath);
			resultArray = IOUtils.toByteArray(is);
        }
		catch(Exception e)
		{
			e.printStackTrace();
			return e.getMessage();
        } 
		finally 
		{
            try 
            {
                is.close();
            } 
            catch (Exception e) { }
        }
		return resultArray;
	}
}
