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
import javax.swing.SwingUtilities;
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
	
	protected void setFont(String name, int stile, int size)
	{
		this.getComponent(1).setFont(new Font(name, stile, size));
		this.getComponent(this.getComponentCount()-1).setFont(new Font(name, stile, size));
	}
	
	/**
	 * Sets the application status message slot on the status bar.
	 * @param statusString - the application status message
	 */
	protected void setStatus(String statusString)
	{
		JLabel status = (JLabel) this.getComponent(1);
		status.setText(statusString);
		//status.paintImmediately(status.getBounds());
	}
	
	/**
	 * Sets the query processing time slot on the status bar.
	 * @param processingTime - the time of query processing
	 */
	protected void setTime(long processingTime)
	{
		JLabel time = (JLabel) this.getComponent(this.getComponentCount()-1);
		if(processingTime == -1)
			time.setText("");
		else
			time.setText(String.valueOf(processingTime) + " ms");
	}
	
	/**
	 * Updates application's status bar. The status bar contains a slot for the application status message
	 * and a slot for the query processing time. If processingTime equals -1, it means either there are no queries 
	 * were processed by application or errors occurred. In this case time slot on the status bar is empty.
	 * @param statusString - the application status message
	 * @param processingTime - the time of query processing
	 */
	public void updateStatus(String statusString, long processingTime)
	{
		class UpdateTask implements Runnable {
			StatusBar statusBar;
	        String statusString;
	        long processingTime;
	        
	        UpdateTask(StatusBar statusBar, String statusString, long processingTime)
	        { 
	        	this.statusBar = statusBar;
	        	this.statusString = statusString;
	        	this.processingTime = processingTime;
	        }
	        public void run() {
	        	statusBar.setStatus(statusString);
	        	statusBar.setTime(processingTime);
	        }
	    }
	    //Thread t = new Thread(new UpdateTask(this, statusString, processingTime));
	    //t.start();
	    
		SwingUtilities.invokeLater(new UpdateTask(this, statusString, processingTime));
	}
}
