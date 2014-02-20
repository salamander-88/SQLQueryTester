package SQLQT_UI.Logic.Listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;

import SQLQT_Utilities.QueryFileFilter;
/**
 * 
 * @author Anastasiya Goncharova
 *
 */
public class LoadQueryButtonActionListener implements ActionListener
{
	private JFrame frame;
	private JEditorPane sqlSyntaxPane;
	private JTextPane textPane_Response;
	private JTabbedPane tabbedPane;
	
	public LoadQueryButtonActionListener(JFrame frame, JEditorPane sqlSyntaxPane, JTextPane textPane_Response, JTabbedPane tabbedPane)
	{
		this.frame = frame;
		this.sqlSyntaxPane = sqlSyntaxPane;
		this.textPane_Response = textPane_Response;
		this.tabbedPane = tabbedPane;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		JFileChooser loadFile = new JFileChooser();
		loadFile.setFileFilter(new QueryFileFilter());
		int rVal = loadFile.showOpenDialog(frame);
		if(rVal == JFileChooser.APPROVE_OPTION) 
		{
			File file = loadFile.getSelectedFile();
			BufferedReader br = null;
			 
			try 
			{
				String sqlQuery = "";
				String currentLine;
				br = new BufferedReader(new FileReader(file.getAbsolutePath()));
	 
				while ((currentLine = br.readLine()) != null) sqlQuery = sqlQuery + currentLine;
				
				sqlSyntaxPane.setText(sqlQuery);		 
			} 
			catch (IOException e) 
			{
				textPane_Response.setText(e.getMessage());
				tabbedPane.setSelectedComponent(tabbedPane.getComponent(1));
			} 
			finally 
			{
				try 
				{
					if (br != null)br.close();
				} 
				catch (IOException ex) 
				{
					ex.printStackTrace();
				}
			}
		}
	}
}
