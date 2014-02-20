package SQLQT_UI.Logic.Listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
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
public class SaveQueryButtonActionListener implements ActionListener
{
	private JFrame frame;
	private JEditorPane sqlSyntaxPane;
	private JTextPane textPane_Response;
	private JTabbedPane tabbedPane;
	
	public SaveQueryButtonActionListener(JFrame frame, JEditorPane sqlSyntaxPane, JTextPane textPane_Response, JTabbedPane tabbedPane)
	{
		this.frame = frame;
		this.sqlSyntaxPane = sqlSyntaxPane;
		this.textPane_Response = textPane_Response;
		this.tabbedPane = tabbedPane;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		String sqlString = sqlSyntaxPane.getText();
		if(sqlString.contains("Enter query here...")) sqlString = "";
		JFileChooser saveFile = new JFileChooser();
		saveFile.setFileFilter(new QueryFileFilter());
		int rVal = saveFile.showSaveDialog(frame);
		if(rVal == JFileChooser.APPROVE_OPTION) 
		{
			File file = saveFile.getSelectedFile();
			if(!file.getName().toString().endsWith(".sql")) 
			{
				String name = file.getAbsolutePath().concat(".sql");
				file = new File(name);
			}
			FileOutputStream fos = null;
			try 
			{
				fos = new FileOutputStream(file);
				fos.write(sqlString.getBytes());
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
					fos.close();
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
}
