package SQLQT_Utilities;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;

import SQLQT_UI.Graphics.StatusBar;
/**
 * 
 * @author Anastasiya Goncharova
 *
 */
public final class TableManager 
{
	/**
	 * Constructs the next row of the table that is displayed on the main frame
	 * @param rs - database table row
	 * @param rsmd - database table meta data
	 * @return row of the table that is displayed on the main frame
	 * @throws SQLException
	 */
	public static Vector<Object> getNextRow(ResultSet rs, ResultSetMetaData rsmd) throws SQLException
	{
		Vector<Object> currentRow = new Vector<Object>();
	
		for(int i=1; i<=rsmd.getColumnCount(); i++)
		{
			switch(rsmd.getColumnType(i)) 
			{
				case Types.VARCHAR:
					currentRow.addElement(rs.getString(i));
					break;
				case Types.INTEGER:
					currentRow.addElement(rs.getInt(i));
					break;
				case Types.BLOB:
					ImageIcon img = new ImageIcon(rs.getBytes(i));
					ImageIcon compressedImage = ImageProcessing.scaleImage(img, 20, 20);
					currentRow.addElement(compressedImage);
					currentRow.addElement(img);
					break;
				default: 
					currentRow.addElement(null);
			}
		}

		return currentRow;
	}
	
	protected static int getImageColumnIndex(JTable table)
	{
		int index = 0;
		for(int i=0; i<table.getColumnCount(); i++)
		{
			if(table.getColumnModel().getColumn(i).getHeaderValue().toString().equals("image"))
			{
				index = i;
				break;
			}
			else index = -1;
		}
		return index;
	}
	
	/**
	 * Fills in the table that is displayed on the main frame with the data from database table res
	 * @param res - table from the database
	 * @param table - table that should be filled in with the data
	 */
	public static void constructTable(ResultSet res, JTable table)
	{
		ResultSetMetaData rsMetaData;
		try 
		{
			boolean imageExist = false;
			rsMetaData = res.getMetaData();
			Vector<Object> columnNames = new Vector<Object>();
			Vector<Object> rows = new Vector<Object>();

			for(int i=1; i<=rsMetaData.getColumnCount(); i++) 
			{
				columnNames.addElement(rsMetaData.getColumnName(i));
				if(rsMetaData.getColumnName(i).equals("image"))
				{
					columnNames.addElement("original_image");
					imageExist = true;
				}
			}
			while(res.next()) 
				rows.addElement(TableManager.getNextRow(res, rsMetaData));

			table.setModel(new DefaultTableModel(rows, columnNames));
			int imgIndex = TableManager.getImageColumnIndex(table);
			if(imgIndex != -1)
				table.getColumnModel().getColumn(imgIndex).setCellRenderer(table.getDefaultRenderer(ImageIcon.class));
			if(imageExist) 
				table.getColumnModel().removeColumn(table.getColumn("original_image"));
			table.setRowHeight(20);
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads the data from the database and constructs the table, which is displayed on the main frame, with the loaded data
	 * @param table - table that is filled in with the data from the database
	 * @throws SQLException
	 */
	public static void displayRowsFromDB(JTable table)
	{
		ResultSet res = DBManager.getAllRowsFromDatabase();
		TableManager.constructTable(res, table);
	}

	/**
	 * Constructs the table that is displayed on the textPane_Response and fills it in with results of query
	 * @param query - select query
	 * @param textPane_Response - pane that displays select results
	 * @param statusBar
	 * @return constructed table with the time of query processing if no error occurred, error message otherwise
	 * @throws SQLException
	 */
	public static Object getTableWithResult(String query, JTextPane textPane_Response, StatusBar statusBar) throws SQLException
	{
		JTable table = new JTable();
		
		Object result = null;
		ResultSet res;
		String msg;
				
		long start = System.currentTimeMillis();
		Object answer = DBManager.selectQuery(query);
		long finish = System.currentTimeMillis();
		long time = finish-start;
		if(answer.getClass().getName().toString().contains("RS")) //if no error occurred
		{
			res = (ResultSet) answer;
			TableManager.constructTable(res, table);
			Pair<JTable,Long> pair = new Pair<JTable,Long>(table,time);
			result = pair;
		}
		else if(answer.getClass().getName().toString().contains("String")) 
		{
			msg = (String) answer;
			textPane_Response.setText(msg);
			statusBar.updateStatus("Error: " + msg, -1);
		}
		return result;
	}
}
