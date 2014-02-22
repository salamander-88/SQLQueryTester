package SQLQT_UI.Logic.Listeners;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.sql.SQLException;
import java.sql.Statement;

import SQLQT_Utilities.ConnectionToBD;
/**
 * 
 * @author Anastasiya Goncharova
 *
 */
public class MainWindowListener implements WindowListener
{

	@Override
	public void windowActivated(WindowEvent arg0) {
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		try {
			Statement st = ConnectionToBD.con.createStatement();
			st.executeUpdate("drop table if exists patients");
			ConnectionToBD.con.close();
			File file = new File("patients.db");
			file.delete();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		try {
			Statement st = ConnectionToBD.con.createStatement();
			st.executeUpdate("drop table if exists patients");
			ConnectionToBD.con.close();
			File file = new File("patients.db");
			file.delete();
		} catch (SQLException e) {
			e.printStackTrace();
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
