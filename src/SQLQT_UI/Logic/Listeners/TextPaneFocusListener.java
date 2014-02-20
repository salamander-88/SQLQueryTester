package SQLQT_UI.Logic.Listeners;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JEditorPane;
/**
 * 
 * @author Anastasiya Goncharova
 *
 */
public class TextPaneFocusListener implements FocusListener{

	private JEditorPane editorPane;
	
	public TextPaneFocusListener(JEditorPane editorPane)
	{
		this.editorPane = editorPane;
	}
	
	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub
		if(this.editorPane.getText().equals("")) this.editorPane.setText("Enter query here...");
	}

}
