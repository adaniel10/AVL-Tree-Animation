import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This is the main class which is used to start off and 
 * run the GUI program.
 * 
 * @author Abin Daniel
 * @title MSc Honors Project
 *
 */
public class RunMe 
{
	public static void main(String[] args) {
		new RunMe();
	}
	
	/**
	 * Constructor -
	 * The following method creates a start up welcome frame before the user
	 * proceeds to the main panel.
	 */
	public RunMe() {
		
		JFrame startFrame = new JFrame("Welcome");
		JButton launch = new JButton("Launch");
		launch.setPreferredSize(new Dimension(10, 35));
		JPanel welcomePanel = new JPanel(new GridLayout(5,1));	//2 rows
		
		//adding a label and a button to the welcome panel
		welcomePanel.add(new JLabel("       Welcome to AVL Trees Learning Tool!"));
		welcomePanel.add(new JLabel("        Click 'Launch' to start the program."));
		welcomePanel.add(launch);
		welcomePanel.add(Box.createVerticalStrut(5));
		welcomePanel.add(new JLabel("                        Abin Daniel"));
		
		//setting the size and location of the startup panel
		startFrame.setLayout(new FlowLayout());
		startFrame.add(welcomePanel);	//adding the welcome panel onto the frame
		startFrame.setSize(450, 195);
		startFrame.setLocation(400, 300);
		startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startFrame.setVisible(true);
		
		// add the action listener to the launch button
		launch.addActionListener(new ActionListener() 
		{
			// here is the event handler
			public void actionPerformed(ActionEvent e) {
				startFrame.setVisible(false);	//hides the start up window
				MainFrame cg = new MainFrame();	//launches the main window
				cg.setVisible(true);			//displays the main window
			}
		});
	}
}