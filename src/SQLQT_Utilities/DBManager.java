package SQLQT_Utilities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author Anastasiya Goncharova
 *
 */
public final class DBManager
{
	public static final String tableId = DBManager.generateTableID(); //unique table identifier for each application instance to avoid table access concurrency
	
	/**
	 * Generates a unique table identifier based on the current time in milliseconds
	 * @return
	 */
	private static String generateTableID()
	{
		String id = "";
		id = String.valueOf(System.currentTimeMillis());
		return id;
	}
	
	/**
	 * Creates the table patients in database and fills it in with the sample data
	 */
	public static void initializeDatabase()
	{
		DBManager.recreateTable();
		DBManager.fillInTableWithSampleData();
	}
	
	/**
	 * Drops existing table and creates new one.
	 */
	protected static void recreateTable()
	{
		Statement st = null;
		try 
		{
			st = ConnectionToBD.con.createStatement();
			st.executeUpdate("drop table if exists patients" + tableId);
			st.executeUpdate("create table patients" + tableId + " (id integer primary key, firstname string, lastname string, appointment char(10), image blob)");
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
	    {
	      try
	      {
	    	  if(st != null) st.close();
	      }
	      catch(SQLException e)
	      {
	        // connection close failed.
	        System.err.println(e);
	      }
	    }
	}
	
	/**
	 * Resets the table by filling it in with the sample rows.
	 * @return either number of rows that were affected in successful case or error message otherwise
	 */
	public static Object resetTable()
	{
		Object result = null;
		result = DBManager.removeAllRowsFromDB();
		if(!result.getClass().getName().toString().contains("String"))
			result = DBManager.fillInTableWithSampleData();
		return result;
	}
	
	/**
	 *  Fills in the created table with the sample rows.
	 * @return either number of rows that were affected in successful case or error message otherwise
	 */
	protected static Object fillInTableWithSampleData()
	{
		Object result = null;
		String imgPath = "";
		String[][] names = {{"Erick","Schmidt"},{"Robert","Richardson"},{"Adam","Cast"},{"Alex","Fishman"},{"Mike","Adams"},
							{"Ivan","Aivazovsky"},{"John","Richman"},{"Allan","Smith"},{"Jorge","Axe"},{"Democritus",""},
							{"John","Adams"},{"Marry","Allis"},{"Sarra","Royal"},{"Maria","Razumovska"},{"Linda","Smith"},
							{"Scrooge","McDuck"},{"Anna","Morozova"},{"Marina","Ivanova"},{"Natalia","Kuznezova"},{"Maria","Zubareva"},
							{"Leonardo","da Vinci"},{"Mona","Lisa"},{"Nina","Kraft"},{"Antonio","Barrelli"},{"Kim","Cho"}};
		byte[] img = null;
		PreparedStatement pst = null;
		try
		{
			ConnectionToBD.con.setAutoCommit(false);
			String insertQuery = "insert into patients" + tableId + " values(?,?,?,?,?);";
			pst = ConnectionToBD.con.prepareStatement(insertQuery);
			int rowsCount = 0;
			for(int i=1; i<=25; i++)
			{
				imgPath = "/images/man" + i + ".png";
				Object res = FileManager.getByteArrayFromFile(imgPath);
				if(res != null && !res.getClass().getName().toString().contains("String")) 
				{
					//img = (byte[]) FileManager.getByteArrayFromFile(imgPath);
					img = (byte[]) res;
					
				}
				else img = "null".getBytes();
				
				pst.setInt(1, i);
				pst.setString(2, names[i-1][0]);
				pst.setString(3, names[i-1][1]);
				pst.setString(4, "2013-12-" + (i));
				pst.setBytes(5, img);
				pst.addBatch();
				rowsCount ++;
			}
			pst.executeBatch();
			ConnectionToBD.con.setAutoCommit(true);
			result = rowsCount;
		}
		catch(SQLException e)
		{
			result = e.getMessage();
	    }
	    finally
	    {
	      try
	      {
	    	  if(pst != null) pst.close();
	      }
	      catch(SQLException e)
	      {
	        System.err.println(e);
	      }
	    }
		return result;
	}
	
	protected static Object removeAllRowsFromDB()
	{
		Object result = null;
		Statement st = null;
		try 
		{
			st = ConnectionToBD.con.createStatement();
			result = st.executeUpdate("delete from patients" + tableId);
		} 
		catch (SQLException e) 
		{
			result = e.getMessage();
		}
		finally
	    {
	      try
	      {
	    	  if(st != null) st.close();
	      }
	      catch(SQLException e)
	      {
	        System.err.println(e);
	      }
	    }
		return result;
	}
	
	public static ResultSet getAllRowsFromDatabase()
	{
		ResultSet result = null;
		String query = "select id, firstname, lastname, appointment, image from patients" + tableId;
		Statement st;
		try 
		{
			st = ConnectionToBD.con.createStatement();
			result = st.executeQuery(query);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static Object updateQuery(String query)
	{
		try {
			Object result;
			int count = 0;
			if(query.contains("insert")) 
			{
				result = DBManager.runInsertQuery(query);
				if(result.getClass().getName().toString().contains("Integer")) count = (int)result; //if no error
				else if(result.getClass().getName().toString().contains("String")) return (String)result; //if error occurred
			}
			else 
			{
				Statement st = ConnectionToBD.con.createStatement();
				count = st.executeUpdate(query);
				st.close();
			}
			return count;
		} catch (SQLException e) {
			return e.getMessage();
		} catch (NullPointerException e) {
			String str = e.toString();
			return str;
		}
	}

	public static Object selectQuery(String query)
	{
		ResultSet rs = null;
		try {
			Statement st = ConnectionToBD.con.createStatement();
			rs = st.executeQuery(query);
		} catch (SQLException e) {
			return e.getMessage();
		} catch (NullPointerException e) {
			return e.toString();
		}
		return rs;
	}
	
	protected static Object runInsertQuery(String query) throws SQLException
	{
		Object result = null;
		Object obj = null;
		int rowsCount = 0;
		
		boolean errorFlag = false;
		String imagePath = QueryParser.getImagePathFromQuery(query);
		//if the image path is in insert query
		if(!imagePath.equals("null") && !imagePath.equals(""))
		{
			//load image from file specified
			obj = FileManager.getByteArrayFromFile(imagePath);
			if(obj.getClass().getName().toString().contains("String")) 
			{
				errorFlag = true;
				result = obj;
			}
			else
			{
				byte[] img = (byte[]) obj;
				String insert = query.replace("'" + imagePath + "'", "?");
				PreparedStatement pst = ConnectionToBD.con.prepareStatement(insert);
				pst.setBytes(1, img);
				pst.executeUpdate();
				pst.close();
			}

			if(!errorFlag)
			{
				rowsCount ++;
				result = rowsCount;
			}
		}
		else
		{
			if(imagePath.equals("")) query = query.replace("''", "null");
			Statement st = ConnectionToBD.con.createStatement();
			result = st.executeUpdate(query);
			st.close();
		}
		return result;
	}
}
