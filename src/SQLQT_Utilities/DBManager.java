package SQLQT_Utilities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import SQLQT_UI.Graphics.StatusBar;
/**
 * 
 * @author Anastasiya Goncharova
 *
 */
public final class DBManager
{
	/**
	 * Creates the table patients in database and fills it in with the sample data
	 */
	public static void initializeDatabase()
	{
		String imgPath = "";
		String[][] names = {{"Erick","Schmidt"},{"Robert","Richardson"},{"Adam","Cast"},{"Alex","Fishman"},{"Mike","Adams"},
							{"Ivan","Aivazovsky"},{"John","Richman"},{"Allan","Smith"},{"Jorge","Axe"},{"Democritus",""},
							{"John","Adams"},{"Marry","Allis"},{"Sarra","Royal"},{"Maria","Razumovska"},{"Linda","Smith"},
							{"Scrooge","McDuck"},{"Anna","Morozova"},{"Marina","Ivanova"},{"Natalia","Kuznezova"},{"Maria","Zubareva"},
							{"Leonardo","da Vinci"},{"Mona","Lisa"},{"Nina","Kraft"},{"Antonio","Barrelli"},{"Kim","Cho"}};
		byte[] img = null;
		
		Statement st = null;
		PreparedStatement pst = null;
		try
		{
			st = ConnectionToBD.con.createStatement();
			
			st.executeUpdate("drop table if exists patients");
			st.executeUpdate("create table patients (id integer primary key, firstname string, lastname string, appointment char(10), image blob)");
			
			String insertQuery = "insert into patients values(?,?,?,?,?);";
			pst = ConnectionToBD.con.prepareStatement(insertQuery);
			for(int i=1; i<=25; i++)
			{
				imgPath = "/images/man" + i + ".png";
				Object res = FileManager.getByteArrayFromFile(imgPath);
				if(res != null && !res.getClass().getName().toString().contains("String")) 
				{
					//img = (byte[]) FileManager.getByteArrayFromFile(imgPath);
					img = (byte[]) res;
					pst.setInt(1, i);
					pst.setString(2, names[i-1][0]);
					pst.setString(3, names[i-1][1]);
					pst.setString(4, "2013-12-" + (i));
					pst.setBytes(5, img);
					pst.executeUpdate();
				}
				else
				{
					if(res == null)
					System.out.println("null");
				}
			}
		}
		catch(SQLException e)
		{
			System.err.println(e.getMessage());
	    }
	    finally
	    {
	      try
	      {
	    	  if(st != null) st.close();
	    	  if(pst != null) pst.close();
	      }
	      catch(SQLException e)
	      {
	        // connection close failed.
	        System.err.println(e);
	      }
	    }
	}

	public static ResultSet getAllRowsFromDatabase() throws SQLException
	{
		String query = "select id, firstname, lastname, appointment, image from patients";
		Statement st = ConnectionToBD.con.createStatement();
		ResultSet result = st.executeQuery(query);
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
