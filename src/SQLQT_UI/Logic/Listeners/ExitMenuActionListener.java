package SQLQT_UI.Logic.Listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
/**
 * 
 * @author Anastasiya Goncharova
 *
 */
public class ExitMenuActionListener implements ActionListener
{
	JFrame frame;
	
	public ExitMenuActionListener(JFrame frame)
	{
		this.frame = frame;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		frame.dispose();
	}
}
