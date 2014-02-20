package SQLQT_UI.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
/**
 * 
 * @author Anastasiya Goncharova
 *
 */
public class StatusBar extends JPanel
{
	private static final long serialVersionUID = 1L;

	public StatusBar(int frameWidth)
	{
		super();
		this.setMinimumSize(new Dimension(100, 20));
		this.setPreferredSize(new Dimension(frameWidth, 20));
		this.setMaximumSize(new Dimension(Short.MAX_VALUE, 20));
		this.setBorder(new BevelBorder(BevelBorder.LOWERED));
		this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		
		JLabel status = new JLabel("Ready");
		JLabel time = new JLabel("");
		status.setPreferredSize(new Dimension(frameWidth-80, 20));
		status.setMaximumSize(new Dimension(Short.MAX_VALUE, 20));
		time.setPreferredSize(new Dimension(70, 20));
		time.setMaximumSize(new Dimension(70, 20));
		
		JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
		sep.setMaximumSize(new Dimension(2, 20));
		sep.setForeground(Color.lightGray);
		
		this.add(Box.createRigidArea(new Dimension(5, 0)));
		this.add(status);
		this.add(sep);
		this.add(Box.createRigidArea(new Dimension(5, 0)));
		this.add(time);
		
		this.setFont("Arial", Font.PLAIN, 11);
	}
	
	public void setFont(String name, int stile, int size)
	{
		this.getComponent(1).setFont(new Font(name, stile, size));
		this.getComponent(this.getComponentCount()-1).setFont(new Font(name, stile, size));
	}
	
	public void setStatus(String statusString)
	{
		JLabel status = (JLabel) this.getComponent(1);
		status.setText(statusString);
	}
	
	public void setTime(long processingTime)
	{
		JLabel time = (JLabel) this.getComponent(this.getComponentCount()-1);
		time.setText(String.valueOf(processingTime) + " ms");
	}
}
