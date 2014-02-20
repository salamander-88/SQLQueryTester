package SQLQT_UI.Logic.Listeners;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
/**
 * 
 * @author Anastasiya Goncharova
 *
 */
public class PopupWindowMouseListener extends MouseInputAdapter
{
	private JDialog popupWindow;
	private JTable table;
	private int currentRow;
	private int currentColumn;
	
	public PopupWindowMouseListener(JDialog popupWindow, JTable table, int currentRow, int currentColumn)
	{
		this.popupWindow = popupWindow;
		this.table = table;
		this.currentRow = currentRow;
		this.currentColumn = currentColumn;
	}
	
	public void mouseEntered(MouseEvent e) 
	{
		//convert mouse position to the table coordinate system
		Point p = SwingUtilities.convertPoint((Component) e.getSource(), e.getPoint(), table);
		
		//if mouse was moved out of table borders, close image popup window
		if(!table.contains(p))
		{
			if(popupWindow.isVisible()) popupWindow.setVisible(false);
			popupWindow.getContentPane().removeAll();
		}
		else
		{
			//take current table's cell under the mouse cursor
			int row = table.rowAtPoint(p);
			int column = table.columnAtPoint(p);
			
			//if mouse was moved to another cell, close corresponding image popup window
			if(row != currentRow || column != currentColumn)
			{
				if(popupWindow.isVisible()) popupWindow.setVisible(false);
				popupWindow.getContentPane().removeAll();
			}
		}
	}

	public void mouseExited(MouseEvent e)
	{
		//convert mouse position to the table coordinate system
		Point p = SwingUtilities.convertPoint((Component) e.getSource(), e.getPoint(), table);
		
		//if mouse was moved out of table borders, close image popup window
		if(!table.contains(p))
		{
			if(popupWindow.isVisible()) popupWindow.setVisible(false);
			popupWindow.getContentPane().removeAll();
		}
	}

	public void mouseMoved(MouseEvent e) 
	{
		//convert mouse position to the table coordinate system
		Point p = SwingUtilities.convertPoint((Component) e.getSource(), e.getPoint(), table);
		
		//if mouse was moved out of table borders, close image popup window
		if(!table.contains(p))
		{
			if(popupWindow.isVisible()) popupWindow.setVisible(false);
			popupWindow.getContentPane().removeAll();
		}
		else
		{
			//take current table's cell under the mouse cursor
			int row = table.rowAtPoint(p);
			int column = table.columnAtPoint(p);
			
			//if mouse was moved to another cell, close corresponding image popup window
			if(row != currentRow || column != currentColumn)
			{
				if(popupWindow.isVisible()) popupWindow.setVisible(false);
				popupWindow.getContentPane().removeAll();
			}
		}
	}

	public void setCurrentCell(int row, int column)
	{
		currentRow = row;
		currentColumn = column;
	}
}
