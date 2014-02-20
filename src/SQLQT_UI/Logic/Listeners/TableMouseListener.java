package SQLQT_UI.Logic.Listeners;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import SQLQT_Utilities.GeneralUtilities;
import SQLQT_Utilities.ImageProcessing;
/**
 * 
 * @author Anastasiya Goncharova
 *
 */
public class TableMouseListener extends MouseInputAdapter
{
	private JTable table;
	private JFrame frame;
	private JDialog popupWindow;
	private int previousRow;
	private int previousColumn;
	private PopupWindowMouseListener mouseListener;
	
	public TableMouseListener(JTable table, JFrame frame)
	{
		this.table = table;
		this.frame = frame;
		popupWindow = new JDialog();
		popupWindow.setFocusable(false);
		popupWindow.setFocusableWindowState(false);
		popupWindow.setUndecorated(true);
		popupWindow.setVisible(false);
		mouseListener = new PopupWindowMouseListener(popupWindow, this.table, previousRow, previousColumn);
		popupWindow.addMouseListener(mouseListener);
		popupWindow.addMouseMotionListener(mouseListener);
	}
	
	public void mouseEntered(MouseEvent e) 
	{
		if(frame.isFocused())
		{
			if(!popupWindow.isVisible())
			{
				//initialize previous table cell under the mouse
				previousRow = table.rowAtPoint(e.getPoint());
				previousColumn = table.columnAtPoint(e.getPoint());
				//convert cell coordinates to screen coordinate system for further image popup window positioning
				Point cellCoorinates = (Point)table.getCellRect(previousRow, previousColumn, false).getLocation().clone();
				SwingUtilities.convertPointToScreen(cellCoorinates, table);
				int width = table.getCellRect(previousRow, previousColumn, false).width;
				String columnName = table.getColumnName(previousColumn);

				//check if the mouse position is inside the table and the column under the mouse is image column
				if(previousRow > -1 && previousRow < table.getRowCount() && previousColumn > -1 && previousColumn < table.getColumnCount() && columnName.equals("image"))
				{
					ImageIcon content = (ImageIcon) table.getModel().getValueAt(previousRow, previousColumn+1);
					content = ImageProcessing.scaleImage(content, 250, 350);
					int iconHeight = content.getIconHeight();
					int iconWidth = content.getIconWidth();
					JLabel label = new JLabel(content);
					//put image from current table cell to popup window
					popupWindow.getContentPane().removeAll();
					popupWindow.setSize(iconWidth, iconHeight);
					popupWindow.getContentPane().add(label);
					//set previous cell for listener
					mouseListener.setCurrentCell(previousRow, previousColumn);
					
					popupWindow.setLocation(cellCoorinates.x + width + 1, cellCoorinates.y);
					//pose popup window inside screen borders
					GeneralUtilities.poseInsideScreen(popupWindow);
					popupWindow.setVisible(true);
				}
				else
				{
					if(popupWindow.isVisible()) popupWindow.setVisible(false);
					popupWindow.getContentPane().removeAll();
				}
			}
		}
	}
	
	public void mouseExited(MouseEvent e)
	{
		if(frame.isFocused())
		{
			HeaderMouseListener listener = new HeaderMouseListener(table, popupWindow);
			MouseListener[] formerListeners = table.getTableHeader().getMouseListeners();
			for(int i=0; i<formerListeners.length; i++)
				table.getTableHeader().removeMouseListener(formerListeners[i]);
			table.getTableHeader().addMouseListener(listener);
			
			Point inPopupCoordinates = SwingUtilities.convertPoint((Component) e.getSource(), e.getPoint(), popupWindow);
			Point inTableHeaderCoordinates = SwingUtilities.convertPoint((Component) e.getSource(), e.getPoint(), table.getTableHeader());
			
			//check if the mouse cursor is outside the image popup window or if it is under the table header
			if(popupWindow.isVisible() && !popupWindow.contains(inPopupCoordinates) || table.getTableHeader().contains(inTableHeaderCoordinates))
			{
				popupWindow.setVisible(false);
				popupWindow.getContentPane().removeAll();
			}
		}
	}
	
	public void mouseMoved(MouseEvent e) 
	{
		if(frame.isFocused())
		{
			//get current table cell under the mouse cursor
			int row = table.rowAtPoint(e.getPoint());
			int column = table.columnAtPoint(e.getPoint());
			//convert cell coordinates to screen coordinate system for further image popup window positioning
			Point cellCoorinates = (Point)table.getCellRect(row, column, false).getLocation().clone();
			SwingUtilities.convertPointToScreen(cellCoorinates, table);
			int width = table.getCellRect(row, column, false).width;
			String columnName = table.getColumnName(column);
			
			//check if the mouse cursor was moved to another cell
			if(previousRow != row || previousColumn != column)
			{
				previousRow = row;
				previousColumn = column;
				//check if the mouse position is inside the table and the column under the mouse is image column
				if(row > -1 && row < table.getRowCount() && column > -1 && column < table.getColumnCount() && columnName.equals("image"))
				{
					ImageIcon content = (ImageIcon) table.getModel().getValueAt(row, column+1);
					content = ImageProcessing.scaleImage(content, 250, 350);
					int iconHeight = content.getIconHeight();
					int iconWidth = content.getIconWidth();
					JLabel label = new JLabel(content);
					//put image from current table cell to popup window
					popupWindow.getContentPane().removeAll();
					popupWindow.setSize(iconWidth, iconHeight);
					popupWindow.getContentPane().add(label);
					//set previous cell for listener
					mouseListener.setCurrentCell(previousRow, previousColumn);

					popupWindow.setLocation(cellCoorinates.x + width + 1, cellCoorinates.y);
					//pose popup window inside screen borders
					GeneralUtilities.poseInsideScreen(popupWindow);
					popupWindow.setVisible(true);
				}
				else
				{
					if(popupWindow.isVisible()) popupWindow.setVisible(false);
					popupWindow.getContentPane().removeAll();
				}
			}
		}
	}
}
