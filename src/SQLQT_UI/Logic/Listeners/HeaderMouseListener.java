package SQLQT_UI.Logic.Listeners;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
/**
 * 
 * @author Anastasiya Goncharova
 *
 */
public class HeaderMouseListener implements MouseListener
{
	private JTable table;
	private JDialog popupWindow;
	
	public HeaderMouseListener(JTable table, JDialog popupWindow)
	{
		this.table = table;
		this.popupWindow = popupWindow;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Closes image popup window if the mouse entered the table's header
	 */
	@Override
	public void mouseEntered(MouseEvent e) 
	{
		//convert mouse coordinates in the table's header coordinate system
		Point inTableHeaderCoordinates = SwingUtilities.convertPoint((Component) e.getSource(), e.getPoint(), table.getTableHeader());
		
		if(table.getTableHeader().contains(inTableHeaderCoordinates))
		{
			if(popupWindow.isVisible())popupWindow.setVisible(false);
			popupWindow.getContentPane().removeAll();
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
