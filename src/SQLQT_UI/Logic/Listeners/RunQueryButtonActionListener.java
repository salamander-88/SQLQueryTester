package SQLQT_UI.Logic.Listeners;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;

import SQLQT_UI.Graphics.StatusBar;
import SQLQT_Utilities.ConnectionToBD;
import SQLQT_Utilities.DBManager;
import SQLQT_Utilities.Pair;
import SQLQT_Utilities.TableManager;
/**
 * 
 * @author Anastasiya Goncharova
 *
 */
public class RunQueryButtonActionListener implements ActionListener{

	private JEditorPane sqlSyntaxPane;
	//private String query;
	private JTextPane textPane_Response;
	private JScrollPane scrollPane_selectResult;
	private JScrollPane scrollPane_textResponse;
	private JTabbedPane tabbedPane;
	private StatusBar statusBar;
	private JTable table;
	private JFrame frame;

	public RunQueryButtonActionListener(JFrame frame, JTable table, JEditorPane sqlSyntaxPane, JTextPane textPane_Response, JScrollPane scrollPane_selectResult, JScrollPane scrollPane_textResponse, JTabbedPane tabbedPane, StatusBar statusBar)
	{
		this.frame = frame;
		this.table = table;
		this.sqlSyntaxPane = sqlSyntaxPane;
		this.textPane_Response = textPane_Response;
		this.scrollPane_selectResult = scrollPane_selectResult;
		this.scrollPane_textResponse = scrollPane_textResponse;
		this.tabbedPane = tabbedPane;
		this.statusBar = statusBar;
	}

	public void actionPerformed(ActionEvent arg0) 
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				String query = sqlSyntaxPane.getText();
				if(!query.equals("Enter query here...") && !query.equals(""))
				{
					String[] subQueries = query.split(";"); //array of sub-queries if multiple queries were typed 

					boolean error = false; //indicates if some error occurs during transaction processing
					try 
					{
						//open transaction
						ConnectionToBD.con.setAutoCommit(false);
						for(int i=0; i<subQueries.length; i++)
						{
							if(!subQueries[i].replace("\n", "").equals("")) //exclude empty queries
							{
								//clean result pane
								if(scrollPane_selectResult.getViewport().getComponentCount() > 0) 
								{
									scrollPane_selectResult.getViewport().remove(0);
									scrollPane_selectResult.getViewport().revalidate();
									scrollPane_selectResult.getViewport().repaint();
								}
								//clean response pane
								if(!textPane_Response.getText().equals("")) textPane_Response.setText("");

								if(!error)
								{
									if(subQueries[i].contains("select")) //process select query
									{
										statusBar.updateStatus("Running", -1);
										Object res = TableManager.getTableWithResult(subQueries[i], textPane_Response, statusBar);
										if(res != null) //if no error
										{
											Font font = new Font("Courier New", Font.PLAIN, 14);
											Font tableHeaderFont = new Font("Courier New", Font.BOLD, 14);
											Pair<JTable,Long> answer = (Pair<JTable,Long>) res;
											JTable table = answer.getL();
											table.getTableHeader().setFont(tableHeaderFont);
											table.setFont(font);
											TableMouseListener mouseListener = new TableMouseListener(table, frame);
											table.addMouseListener(mouseListener);
											table.addMouseMotionListener(mouseListener);
											long processingTime = answer.getR();
											//display select results
											scrollPane_selectResult.setViewportView(table);
											tabbedPane.setSelectedComponent(scrollPane_selectResult);
											textPane_Response.setText("Select was done succesfully.\n" + table.getRowCount() + " rows returned");
											statusBar.updateStatus("Ready", processingTime);
										}
										else 
										{
											//display error
											tabbedPane.setSelectedComponent(scrollPane_textResponse);
											error = true;
										}
									}
									else //process other queries
									{
										tabbedPane.setSelectedComponent(scrollPane_textResponse);
										statusBar.updateStatus("Running", -1);
										long start = System.currentTimeMillis();
										Object result = DBManager.updateQuery(subQueries[i]);
										long finish = System.currentTimeMillis();
										long time = finish-start;
										if(result.getClass().getName().toString().contains("Integer")) //if no error
										{
											if((Integer)result == 1) textPane_Response.setText("Request was done succesfully.\n1 row was affected");
											else textPane_Response.setText("Request was done succesfully.\n" + (Integer)result + " rows were affected");
											statusBar.updateStatus("Ready", time);
										}
										else if(result.getClass().getName().contains("String")) 
										{
											//display error
											String str = (String)result;
											textPane_Response.setText(str);
											statusBar.updateStatus("Error: " + str, -1);
											error = true;
										}
									}
								}
								else break;
							}
						}
						//if error occur during transaction execution, roll back changes
						if(error) ConnectionToBD.con.rollback();
						else ConnectionToBD.con.commit();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		//display sample table with possible changes
		TableManager.displayRowsFromDB(table);
	}
}
