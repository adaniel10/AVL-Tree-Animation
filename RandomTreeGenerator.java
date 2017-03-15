import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This class is used to generate a random tree onto the main display panel.
 * The user can choose whether they choose as an input - words / positive numbers
 * or negative numbers onto the tree panel.
 * 
 * @author Abin Daniel
 * @title MSc Honors Project
 *
 */
@SuppressWarnings("serial")
public class RandomTreeGenerator extends JFrame 
{
	/**Instance Variables**/
	private boolean chkPosNum = false;
	private boolean chkNegNum = false;
	private boolean chkWords = false;
	private JTextField genSizeField;
	private JButton generate;
	private MainFrame mf;
    
	/**
	 * Constructor
	 * Creates a frame for displaying the generator.
	 * @param mf MainFrame
	 */
	public RandomTreeGenerator(MainFrame mf)
	{
		setSize(600,150);	//width,height
		setLocation(350,300);
		setTitle("Random Tree Generator");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setPreferredSize(new Dimension(600, 150));
		this.mf = mf;
		
		initialize();	//initializes all the checkboxes and adds to the frame
		
		this.setVisible(true);
	}
	
	/**
	 * The following method initializes all the checkboxes and size input field
	 * for the user to select and generate from.
	 */
	public void initialize()
	{
		JPanel mainPanel = new JPanel(new GridLayout(3,1));	//main panel with 3 rows
		
		//creating checkboxes, textfields and buttons
		final JCheckBox posNum = new JCheckBox("Positive Numbers");
		final JCheckBox negNum = new JCheckBox("Negative Numbers");
	    final JCheckBox words = new JCheckBox("Words");
	    genSizeField = new JTextField(2);
	    generate = new JButton("Generate");
	    
	    
	    //listening to all the checkboxes and setting them to true
	    //...if they are checked otherwise false.
	    posNum.addItemListener(new ItemListener() 
	    {
	    	public void itemStateChanged(ItemEvent e) 
	    	{
	    		if(e.getStateChange() == 1)
	    			chkPosNum = true;
	    		else
	    			chkPosNum = false;
	    	}           
	    });

	    negNum.addItemListener(new ItemListener() 
	    {
	    	public void itemStateChanged(ItemEvent e) 
	    	{             
	    		if(e.getStateChange() == 1)
	    			chkNegNum = true;
	    		else
	    			chkNegNum = false;
	    	}           
	    });

	    words.addItemListener(new ItemListener() 
	    {
	    	public void itemStateChanged(ItemEvent e) 
	    	{             
	    		if(e.getStateChange() == 1)
	    			chkWords = true;
	    		else
	    			chkWords = false;
	    	}           
	    });

	    generate.addActionListener(new ActionListener()
	    {
	    	@Override
	    	public void actionPerformed(ActionEvent e) 
	    	{
	    		if(e.getSource() == generate)
	    		{
	    			try
	    			{
	    				/**
	    				 * if the generate button is pressed then check if the user has selected
	    				 * at least one input otherwise throw an error.
	    				 */
	    				if(chkPosNum == false && chkNegNum == false && chkWords == false)
	    				{
	    					JOptionPane.showMessageDialog(null, "Please select at least one input", 
	    							"Error Message", JOptionPane.ERROR_MESSAGE);
	    				}
	    				else
	    				{
	    					String sizeS = genSizeField.getText();
	    					int sizeI = Integer.parseInt(sizeS);	//convert to int

	    					if(sizeI <= 0)
	    					{
	    						//if the size is less than or equal to 0 then throw an error
	    						JOptionPane.showMessageDialog(null, "Please enter a size valid size.", 
	    								"Error Message", JOptionPane.ERROR_MESSAGE);
	    						genSizeField.requestFocus();
	    					}
	    					else if(sizeI > 25)
	    					{
	    						//if the size is greater than 30, then warn the user that all the nodes may
	    						//...not full display on the tree panel.
	    						int reply = JOptionPane.showConfirmDialog(null, 
	    								"The size you have entered may not display all the nodes in the screen.\n"
	    										+"Please enter a size between 1 - 25 otherwise press No to proceed with your current size.", 
	    										"Warning Message", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

	    						if (reply == JOptionPane.NO_OPTION)
	    						{
	    							mf.randomGenerator(chkPosNum, chkNegNum, chkWords, sizeI);
	    							setVisible(false);
	    						}

	    						if (reply == JOptionPane.YES_OPTION)
	    							genSizeField.requestFocus();
	    					}
	    					else
	    					{
	    						//generates the tree
	    						mf.randomGenerator(chkPosNum, chkNegNum, chkWords, sizeI);
	    						setVisible(false); //hides this current frame
	    					}


	    				}
	    			}
	    			catch(NumberFormatException ie) 
	    			{
	    				//if the user enter a non number in the size field then throw an error
	    				JOptionPane.showMessageDialog(null, "Please enter a valid number for size.", 
	    						"Error Message", JOptionPane.ERROR_MESSAGE);
	    				genSizeField.setText("");
	    				genSizeField.requestFocus();
	    			}

	    		}

	    	}	   
	    });
	       
	    //laying out the checkboxes
	    JPanel firstPanel = new JPanel();
	    firstPanel.add(new JLabel("Choose an input: "));
	    firstPanel.add(posNum);
	    firstPanel.add(negNum);
	    firstPanel.add(words);

	    //laying out the size label and field
	    JPanel secondPanel = new JPanel();
	    secondPanel.add(new JLabel("Size of the tree: "));
	    secondPanel.add(genSizeField);

	    //laying out the generate button
	    JPanel thirdPanel = new JPanel();
	    thirdPanel.add(generate);

	    //adding all the sub panels to the main panel
	    mainPanel.add(firstPanel);
	    mainPanel.add(secondPanel);
	    mainPanel.add(thirdPanel);

	    //adding the main panel to this frame
	    this.add(mainPanel);
	}
	
	/**Accessor methods**/
	public boolean getPosNum() { return chkPosNum; }
	public boolean getNegNum() { return chkNegNum; }
	public boolean getWords() { return chkWords; }
}
