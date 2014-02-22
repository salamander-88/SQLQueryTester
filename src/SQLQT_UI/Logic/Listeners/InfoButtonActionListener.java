package SQLQT_UI.Logic.Listeners;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.plaf.ColorUIResource;

import SQLQT_Utilities.FileManager;
/**
 * 
 * @author Anastasiya Goncharova
 *
 */
public class InfoButtonActionListener implements ActionListener
{
	private JFrame mainWindow;
	private JFrame referenceWindow;
	
	public InfoButtonActionListener(JFrame mainWindow, JTabbedPane tabbedPane, JTextPane textPane_Response)
	{
		this.mainWindow = mainWindow;
		
		//create reference window
		referenceWindow = new JFrame("Table description");
		referenceWindow.setBounds(this.mainWindow.getX()+50, this.mainWindow.getY()+50, 740, 370);
		referenceWindow.setMinimumSize(new Dimension(400, 200));
		
		//create reference window's content pane
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getViewport().setBackground(new ColorUIResource(251, 249, 236));
		contentPane.setBackground(new ColorUIResource(251, 249, 236));

		JTextPane reference = new JTextPane();
		reference.setContentType("text/html");
		reference.setEditable(false);
		reference.setBackground(null);
		reference.setBorder(null);

		String referenceText = 	"";

		BufferedReader br = null;
		InputStream is = null;
		try
		{
			String currentLine;
			
			is = FileManager.class.getClass().getResourceAsStream("/txt/reference.txt");
			br = new BufferedReader(new InputStreamReader(is));

			while ((currentLine = br.readLine()) != null) referenceText = referenceText + currentLine;
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
		reference.setText(referenceText);		
		reference.setCaretPosition(0);
		scrollPane.getViewport().add(reference);
		contentPane.add(Box.createRigidArea(new Dimension(5,0)));
		contentPane.add(scrollPane);
		
		referenceWindow.setContentPane(contentPane);
		referenceWindow.setVisible(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		//avoid opening of multiple reference windows
		if(!referenceWindow.isVisible()) referenceWindow.setVisible(true);
	}
}
