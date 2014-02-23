package SQLQT_UI.Logic.Listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
		String ObjButtons[] = {"Yes","No"};
		int PromptResult = JOptionPane.showOptionDialog(frame,"Are you sure you want to exit?",null,JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,ObjButtons,ObjButtons[1]);
		if(PromptResult==JOptionPane.YES_OPTION)
			frame.dispose();
	}
}
