package SQLQT_Utilities;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JDialog;
/**
 * 
 * @author Anastasiya Goncharova
 *
 */
public class GeneralUtilities 
{
	/**
	 * Returns the screen bounds.
	 * @return bounds of the screen
	 */
	public static Rectangle getVirtualBounds() 
	{
        Rectangle bounds = new Rectangle(0, 0, 0, 0);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice lstGDs[] = ge.getScreenDevices();
        for (GraphicsDevice gd : lstGDs) 
        {
            bounds.add(gd.getDefaultConfiguration().getBounds());
        }
        return bounds;
    }
	
	/**
	 * Checks if component is inside the screen bounds
	 * @param component - JDialog component which position is checked
	 * @return true if component inside the screen bounds, false otherwise
	 */
	public static boolean isInsideScreen(JDialog component) 
	{
		boolean inside;
        Rectangle virtualBounds = getVirtualBounds();
        inside = virtualBounds.contains(component.getBounds());
		return inside;
	}
	
	/**
	 * Poses component inside the screen bounds
	 * @param component - JDialog component which is posed
	 */
	public static void poseInsideScreen(JDialog component)
	{
		Rectangle componentBounds = component.getBounds();
		Rectangle screenBounds = getVirtualBounds();
		Point newLocation;
		int newLocationX;
		int newLocationY;

		if(!isInsideScreen(component))
		{
			if(componentBounds.getX() < 0) 
				newLocationX = 0;
			else if(componentBounds.getX() + componentBounds.getWidth() > screenBounds.getWidth()) 
				newLocationX = screenBounds.width - componentBounds.width;
			else 
				newLocationX = componentBounds.x;
			
			if(componentBounds.getY() < 0)
				newLocationY = 0;
			else if(componentBounds.getY() + componentBounds.getHeight() > screenBounds.getHeight())
				newLocationY = screenBounds.height - componentBounds.height;
			else
				newLocationY = componentBounds.y;

			newLocation = new Point(newLocationX, newLocationY);
			component.setLocation(newLocation);
		}
	}
}
