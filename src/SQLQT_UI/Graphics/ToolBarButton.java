package SQLQT_UI.Graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Icon;
import javax.swing.JButton;

class ToolBarButton extends JButton 
{
	private static final long serialVersionUID = 1L;
	
	Shape shape;
	
	public ToolBarButton(Icon ic) 
	{
		super(ic);
		Dimension size = new Dimension(ic.getIconWidth(), ic.getIconHeight());
		size.width = size.height = Math.max(size.width, size.height);
		setPreferredSize(size);
		setContentAreaFilled(false);
	}

	protected void paintComponent(Graphics g) 
	{
	     if (getModel().isArmed()) g.setColor(Color.lightGray);
	     else 
	     {
	          //g.setColor(getBackground());
	    	 g.setColor(new Color(226,226,226));
	     }
	     g.fillRoundRect(0, 0, getWidth()-3, getHeight()-1, 6, 6);
	     super.paintComponent(g);
	}
	protected void paintBorder(Graphics g) 
	{
	     g.setColor(Color.lightGray);
	     g.drawRoundRect(0, 0, getWidth()-3, getHeight()-1, 6, 6);
	}
	
	
	public boolean contains(int x, int y) 
	{
	     if (shape == null || !shape.getBounds().equals(getBounds())) 
	    	 shape = new RoundRectangle2D.Float(0, 0, getWidth()-3, getHeight()-1, 6, 6);
	     return shape.contains(x, y);
	}
}
