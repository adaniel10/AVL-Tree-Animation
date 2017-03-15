import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;

/**
 * This class is used to display all the necessary steps when
 * animating the AVL tree using java's text pane.
 * 
 * @author Abin Daniel
 * @title MSc Honors Project
 */
@SuppressWarnings("serial")
public class Notes extends JTextPane {
	
	/**
	 * Constructor for Notes Class
	 */
	public Notes() 
	{
		this.setFont(new Font("Helvetica", Font.PLAIN, 16));
		this.setForeground(Color.RED);	//setting font color to red
		this.setPreferredSize(new Dimension(800,50));
		SimpleAttributeSet attributeSet = new SimpleAttributeSet();
	    StyleConstants.setAlignment(attributeSet, StyleConstants.ALIGN_CENTER);
	    this.setCharacterAttributes(attributeSet, true);
	    this.setEditable(false);
	    clearNotes();
	}
	
	/**
	 * This method is used to set the text on the notes field.
	 */
	public void setNote(String note) {
		this.setText("\n   Comments:    "+note);
	}
	
	/**
	 * This method is used to clear the notes field.
	 */
	public void clearNotes() {
		this.setText("\n   Comments:");
	}

}
