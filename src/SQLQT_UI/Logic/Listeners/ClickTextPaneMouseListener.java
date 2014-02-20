package SQLQT_UI.Logic.Listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JEditorPane;
/**
 * 
 * @author Anastasiya Goncharova
 *
 */
public class ClickTextPaneMouseListener implements MouseListener{
	
	private JEditorPane sqlSyntaxPane;
	private boolean clicked;
	
	public ClickTextPaneMouseListener(JEditorPane sqlSyntaxPane)
	{
		this.sqlSyntaxPane = sqlSyntaxPane;
		clicked = false;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(!clicked || sqlSyntaxPane.getText().contains("Enter query here..."))
		{
			sqlSyntaxPane.setText("");
			clicked = true;
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		if(!clicked || sqlSyntaxPane.getText().contains("Enter query here..."))
		{
			sqlSyntaxPane.setText("");
			clicked = true;
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
}
