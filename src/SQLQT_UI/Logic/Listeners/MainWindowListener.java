package SQLQT_UI.Logic.Listeners;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import SQLQT_Utilities.ConnectionToBD;
import SQLQT_Utilities.DBManager;
/**
 * 
 * @author Anastasiya Goncharova
 *
 */
public class MainWindowListener implements WindowListener
{
	JFrame frame;
	public MainWindowListener(JFrame frame)
	{
		this.frame = frame;
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) {
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		try {
			Statement st = ConnectionToBD.con.createStatement();
			st.executeUpdate("drop table if exists patients" + DBManager.tableId);
			ConnectionToBD.con.close();
			File file = new File("patients" +  DBManager.tableId + ".db");
			file.delete();
			System.exit(0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		String ObjButtons[] = {"Yes","No"};
		int PromptResult = JOptionPane.showOptionDialog(frame,"Are you sure you want to exit?",null,JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,ObjButtons,ObjButtons[1]);
		if(PromptResult==JOptionPane.YES_OPTION)
		{
			try {
				Statement st = ConnectionToBD.con.createStatement();
				st.executeUpdate("drop table if exists patients" + DBManager.tableId);
				ConnectionToBD.con.close();
				File file = new File("patients" +  DBManager.tableId + ".db");
				file.delete();
				System.exit(0);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		
	}
}
