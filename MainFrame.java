import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;
/**
 * This class is used to create and display the main frame. It creates and initializes
 * all the components needed to be displayed onto this frame.
 * The class also handles all the necessary events from the user and performs its
 * execution as required.
 * 
 * @author Abin Daniel
 * @title MSc Honors Project
 *
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame implements ActionListener
{
	/**Instance Variables**/
	private TreePanel treePanel;
	private Tree tree;
	public JPanel rightPanel, center, bottom;
	private JLabel heightLabel, sizeLabel;
	private JTextField insertField, deleteField, toField, fromField;
	private JTextArea history;
	private JButton insertButton, deleteButton, clearButton, searchButton, 
						resetRangeButton, playButton, pauseButton, nextButton, previous;
	private JMenu menuFile, menuPrint, menuOptions, menuWindow;
	private JMenuItem mItemNew, mItemOpen, mItemExit, mItemInOrder, mItemPreOrder, 
						mItemPostOrder, mItemMin, mItemImage, mItemTextFile, 
						mItemfindMaxButton, mItemfindMinButton, mItemGenRanTree;
	private JCheckBoxMenuItem tgDepthName, tgDepthLines;
	private boolean toggleDepthName = true, toggleDepthLines = true;
	private Notes notes;
	private boolean pause = false;
	private int STEPS = 10;
	private JSlider slider;
	private ArrayList<String> words;
	private Stack<String> stack;
	private boolean openF = false;
	public static final int PREF_W = 1200;
	public static final int PREF_H = 600;
	
	public MainFrame()
	{
		//Creating the frame
		this.setSize(1100,680);	//width,height
		this.setLocation(150,100);	//position of the frame
		this.setTitle("VisuAVL");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(PREF_W, PREF_H));
		
		//Initializing the treePanel, tree and array objects
		treePanel = new TreePanel();
		tree = new Tree(this);
		stack = new Stack<String>();
		
		this.layoutComponents();	//creates and lays out the components onto the frame
		
		slider.setValue(50);		//setting the speed of the animation
		tree.setDS(notes);			//initializing the notes
		treePanel.init(toggleDepthName, toggleDepthLines);
		treePanel.setObjects(tree, this);
		treePanel.startDrawingThread();			//start painting the tree panel from its class		
	}
	
	/**
	 * The following method initializes and adds each component onto the window.
	 */
	public void layoutComponents()
	{	
		createMenu();	//creates the menu bar
		
		//center panel
		center = new JPanel(new BorderLayout());
        center.add(treePanel);
		this.add(center,BorderLayout.CENTER);
		
		createRightPanel(); //creates the right panel
		
		//bottom panel
		bottom = new JPanel(new BorderLayout());
		bottom.setBackground(Color.WHITE);
		notes = new Notes();
		LineBorder line = new LineBorder(Color.blue, 2, true);	//blue line border
		bottom.setBorder(line);
		JScrollPane sp = new JScrollPane(notes);
		bottom.add(sp);
		this.add(bottom, BorderLayout.SOUTH);
		
		
		//Adding listener to each button
		insertButton.addActionListener(this);
		deleteButton.addActionListener(this);
		clearButton.addActionListener(this);
		searchButton.addActionListener(this);
		resetRangeButton.addActionListener(this);
		playButton.addActionListener(this);
		nextButton.addActionListener(this);
		pauseButton.addActionListener(this);
		previous.addActionListener(this);
	}
	
	/**
	 * This method is used to create a simple menu bar for the frame which allows
	 * the user to view/select further options. 
	 */
	public void createMenu()
	{
		//Create the menu bar.
		JMenuBar menuBar = new JMenuBar();
		
		//Building the file menu.
		menuFile = new JMenu("File");
		
		//Creating and adding sub menus to the file menu
		mItemNew = new JMenuItem("New");
		mItemOpen = new JMenuItem("Open");
		JMenu mItemSaveAs = new JMenu("Save As");
		mItemTextFile = new JMenuItem("Text File");
		mItemImage = new JMenuItem("Image");
		mItemExit = new JMenuItem("Exit");
		
		mItemSaveAs.add(mItemTextFile);
		mItemSaveAs.add(mItemImage);
		
		menuFile.add(mItemNew);
		menuFile.add(mItemOpen);
		menuFile.addSeparator();
		menuFile.add(mItemSaveAs);
		menuFile.addSeparator();
		menuFile.add(mItemExit);
	
		//Building the print menu
		menuPrint = new JMenu("Print Traversal");
		
		//Creating and adding sub menus to the print menu
		mItemInOrder = new JMenuItem("In Order");
		mItemPreOrder = new JMenuItem("Pre Order");
		mItemPostOrder = new JMenuItem("Post Order");
		menuPrint.add(mItemInOrder);
		menuPrint.add(mItemPreOrder);
		menuPrint.add(mItemPostOrder);
		
		//Building the options menu
		menuOptions = new JMenu("Options");
		tgDepthName = new JCheckBoxMenuItem("Depth Name",true);
		tgDepthLines = new JCheckBoxMenuItem("Depth Lines",true);
		mItemfindMinButton = new JMenuItem("Get Minimum Node");
		mItemfindMaxButton = new JMenuItem("Get Maximum Node");
		mItemGenRanTree = new JMenuItem("Generate Random Tree");
		
		menuOptions.add(mItemGenRanTree);
		menuOptions.addSeparator();
		menuOptions.add(mItemfindMinButton);
		menuOptions.add(mItemfindMaxButton);
		menuOptions.addSeparator();
		menuOptions.add(tgDepthName);
		menuOptions.add(tgDepthLines);
		
		//Building the windows menu
		menuWindow = new JMenu("Window");
		
		//Creating and adding sub menus to the print menu
		mItemMin = new JMenuItem("Minimise");
		menuWindow.add(mItemMin);
		
		//Adding menus to the menu bar
		menuBar.add(menuFile);
		menuBar.add(menuPrint);
		menuBar.add(menuOptions);
		menuBar.add(menuWindow);
		
		//listening to menu items
		mItemNew.addActionListener(this);
		mItemOpen.addActionListener(this);
		mItemTextFile.addActionListener(this);
		mItemImage.addActionListener(this);
		mItemExit.addActionListener(this);
		mItemGenRanTree.addActionListener(this);
		mItemfindMaxButton.addActionListener(this);
		mItemfindMinButton.addActionListener(this);
		tgDepthName.addActionListener(this);
		tgDepthLines.addActionListener(this);
		mItemMin.addActionListener(this);
		mItemInOrder.addActionListener(this);
		mItemPreOrder.addActionListener(this);
		mItemPostOrder.addActionListener(this);
		
		this.setJMenuBar(menuBar);	//adding menu onto the frame
	}
	
	/**
	 * This method is used to create the right panel which allows the user to interact
	 * with the tree.
	 */
	public void createRightPanel()
	{
		insertButton = new JButton("Insert");
		deleteButton = new JButton("Delete");
		insertField = new JTextField(5);
		deleteField = new JTextField(5);
		heightLabel = new JLabel("Height: -1");
		sizeLabel = new JLabel("Size: 0");
		clearButton = new JButton("Clear Tree");
		previous = new JButton("<<");
		
		//Splitting the right panel into 3 sections
		rightPanel = new JPanel(new GridLayout(3,1));	//3 rows 1 column
		rightPanel.setBackground(Color.lightGray);
		rightPanel.setPreferredSize(new Dimension(310,100));	//size of right panel
		
		//First right panel - Tree Properties panel
		JPanel tree_pan = new JPanel(new GridLayout(4,1));	//4 rows
		JPanel tree_1 = new JPanel();
		JPanel tree_2 = new JPanel();
		JPanel tree_3 = new JPanel();
		JPanel tree_4 = new JPanel();
		
		//adding buttons and fields
		tree_1.add(insertButton);
		tree_1.add(insertField);
		tree_4.add(deleteButton);
		tree_4.add(deleteField);
		
		tree_2.add(heightLabel);
		tree_2.add(Box.createHorizontalStrut(50));
		tree_2.add(sizeLabel);
		tree_3.add(clearButton);
		
		tree_pan.add(tree_1);
		tree_pan.add(tree_4);
		tree_pan.add(tree_2);
		tree_pan.add(tree_3);
		tree_pan.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Tree Properties"));
		rightPanel.add(tree_pan);
		
		//Second right panel
		JPanel pan1 = new JPanel(new GridLayout(2,1));
		
		//Range Search panel
		JPanel range_pan = new JPanel(new GridLayout(2,1));
		JPanel range_1 = new JPanel();
		JPanel range_2 = new JPanel();
		JLabel toLabel = new JLabel("To:");
		JLabel fromLabel = new JLabel("From:");
		toField = new JTextField(5);
		fromField = new JTextField(5);
		searchButton = new JButton("Search");
		resetRangeButton = new JButton("Reset");
		
		range_1.add(fromLabel);
		range_1.add(fromField);
		range_1.add(Box.createHorizontalStrut(50));
		range_1.add(toLabel);
		range_1.add(toField);
		
		range_2.add(searchButton);
		range_2.add(resetRangeButton);
		
		range_pan.add(range_1);
		range_pan.add(range_2);
		range_pan.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Range Search"));
		pan1.add(range_pan);
		
		playButton = new JButton("Play");
		pauseButton = new JButton("Pause");
		playButton.setEnabled(false);
		nextButton = new JButton(">>");
		previous.setSize(1, 1);

		nextButton.setEnabled(false);
		slider = new JSlider(1,250);

		JPanel animPanel = new JPanel(new GridLayout(2,1));
		JPanel animButtons = new JPanel(new GridLayout(1,4));
		
		animButtons.add(playButton);
		animButtons.add(pauseButton);
		animButtons.add(previous);
		animButtons.add(nextButton);
		animPanel.add(animButtons);
		
		JPanel animB2 = new JPanel();
		JLabel speed = new JLabel("Speed:");
		Font f = speed.getFont();
		speed.setFont(f.deriveFont(f.getStyle() ^ Font.ITALIC));
		
		animB2.add(speed);
		animB2.add(new JLabel("hi"));
		animB2.add(slider);
		animB2.add(new JLabel("low"));
		animPanel.add(animB2);
		animPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Animation Options"));
		pan1.add(animPanel);
		
		rightPanel.add(pan1);
		
		//Third right panel
		JPanel pan3 = new JPanel(new BorderLayout());
		history = new JTextArea(8,23);
		JScrollPane scrollPane = new JScrollPane(history);
		history.setEditable(false);
		history.setLineWrap(true);
		history.setWrapStyleWord(true);
		
		
		pan3.add(scrollPane);
		pan3.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Log"));
		
		rightPanel.add(pan3);
		this.add(rightPanel,BorderLayout.EAST);
	}
	
	/**
	 * The following method refreshes the main window by updating the height
	 * and the size of the tree as each node is added/removed. It also sets the
	 * toggle for depth names and lines.
	 */
	public void refresh()
	{
		treePanel.init(toggleDepthName, toggleDepthLines);
		heightLabel.setText("Height: "+tree.maxDepth(tree.getRoot()));
		sizeLabel.setText("Size: "+tree.countNodes());
	}
	
	/**
	 * This method is used to open a file from the menu and load a pre-saved tree onto the 
	 * panel.
	 */
	public void openFile()
	{
		boolean check = true;
		
		if(this.tree.getRoot() != null)
		{
			int reply = JOptionPane.showConfirmDialog(null, "The current tree will be cleared. \nWould you like to proceed?"
					,"Warning Message", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	        if (reply == JOptionPane.YES_OPTION) {
	        	tree.clearTree();
	        	refresh();
	          check = true;
	        }
	        else {
	           check = false;
	        }
		}	
		
		if(check)
		{
			JFileChooser fc = new JFileChooser();
			fc.addChoosableFileFilter(new FileNameExtensionFilter(".txt", "txt"));
			fc.setCurrentDirectory(new File(System.getProperty("user.home")+"/Desktop"));
			int result = fc.showOpenDialog(this);
			
			if (result == JFileChooser.APPROVE_OPTION) 
			{
				File selectedFile = fc.getSelectedFile();
				history.append("~ Selected file: " + selectedFile.getAbsolutePath()+"\n");
				
				String ext = fc.getSelectedFile().getAbsolutePath();
				
				//if the file format is not a text file then throw an error
				if(!ext.contains(".txt"))
				{
					JOptionPane.showMessageDialog(null, "Invalid File Format!!!\nPlease select a .txt file."
							,"Error Message", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(selectedFile.length() == 0)
				{
					JOptionPane.showMessageDialog(null, "This file does not contain any data."
							,"File Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				try 
				{
					FileReader reader = new FileReader(selectedFile);
					Scanner in = new Scanner(reader);
					tree.emptyTempArray();
					
					//if the text file has a next line then enter this loop
					while(in.hasNextLine())
					{
						String line = in.nextLine(); //reads a line
						tree.getTemp().add(line);
					}
					in.close();
					reader.close();
				} 
				catch (FileNotFoundException e) { 
					JOptionPane.showConfirmDialog(null, "File not found!!!"
							,"Error Message", JOptionPane.ERROR_MESSAGE);
				}
				catch(IOException x) { 
					JOptionPane.showConfirmDialog(null, "File not found!!!"
							,"Error Message", JOptionPane.ERROR_MESSAGE);
				}
				
				//Asks the user if they want to load the tree with or without the animation.
				int reply = JOptionPane.showConfirmDialog(null, 
						"Would you like to load the tree \nwith step-by-step animation?"
						,"Information Message", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
		        
				if (reply == JOptionPane.YES_OPTION)
					activatePauseButton();
		        else 
		        	deactivatePauseButton();
				
				//start the main thread
				activateNextButton();
				MainThread localThread = new MainThread(this,4,"","");
				localThread.start();
			}
		}
		
	}
		
	/**
	 * This method is used to export a traversal of a tree to an external text file.
	 * @param operationName String
	 */
	public void writeFile(String operationName, String method)
	{
	    JFileChooser chooser = new JFileChooser();
	    chooser.setCurrentDirectory(new File(System.getProperty("user.home")+"/Desktop"));	//sets the default directory
	    int retrival = chooser.showSaveDialog(null);
	    
	    if (retrival == JFileChooser.APPROVE_OPTION) 
	    {
	        try {
	            FileWriter fw = new FileWriter(chooser.getSelectedFile()+".txt");
	            String fileName = chooser.getSelectedFile().getName()+".txt";
	            String test = ""+chooser.getCurrentDirectory();
	            String outputText = null;
	            
	            if(method.compareTo("traversal") == 0)
	            	outputText = printList();
	            else
	            	outputText = printStack();
	            	
	            fw.write(outputText);	//write to the file
	            fw.close();
	            
	            //if the input string is not empty then print with the string on the history text area
	            if(!operationName.isEmpty())
	            	history.append("~ Exported "+operationName+" tree as "+fileName+" to: "+test+".\n");
	            else
	            	history.append("~ Exported tree as "+fileName+" to: "+test+".\n");
	        } 
	        catch (Exception ex) {
	        	JOptionPane.showConfirmDialog(null, "Error writing file!!!"
						,"Error Message", JOptionPane.ERROR_MESSAGE);
	        }
	    }
	}
		
	/**
	 * This method is used to check the contents of the temporary array in the tree class
	 * and add it to a string.
	 * @return String text
	 */
	private String printList() 
	{
		String text = "";
		for(int i=0; i<tree.tempListSize(); i++)
			text += tree.getTemp().get(i)+" ";
		return text;
	}
	private String printListWithBrackets() 
	{
		String text = "";
		for(int i=0; i<tree.tempListSize(); i++)
			text += "["+tree.getTemp().get(i)+"] ";
		return text;
	}
	
	/**
	 * This method takes the stack and outputs each string followed
	 * by a new line.
	 * @return text String
	 */
	private String printStack() 
	{
		String text = "";
		for(int i=0; i<stack.size(); i++)
			text += stack.get(i)+"\n";
		return text;
	}
	
	
	/**
	 * The following method checks if there is a current tree present and gives the option 
	 * for the user to go back and save the tree or proceed to create a new tree. 
	 * @param msg String
	 * @param hMsg String
	 */
	public void newTree(String msg, String hMsg)
	{
		boolean newTree = true;
		if(this.tree.getRoot() != null)
		{
			int reply = JOptionPane.showConfirmDialog(null, 
					msg, "Warning Message", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	        if (reply == JOptionPane.YES_OPTION) {
	          newTree = true;
	        }
	        else {
	           newTree = false;
	        }
		}
		
		if(newTree)
		{
			history.append("~ "+hMsg+"\n");
			tree.clearTree();
			tree.getNote().setNote("");
			stack.clear();
			refresh();
		}
	}
	
	/**
	 * Clears all the input fields
	 */
	public void clearInputFields()
	{
		insertField.setText("");
		deleteField.setText("");
		fromField.setText("");
		toField.setText("");
	}
	
	/**
	 * This method takes the input string trims it down to 3 characters if it is
	 * longer than 3. The input string is then checked if its empty or if it contains 
	 * a decimal and will throw an error if it does. Otherwise it will return the string 
	 * back.
	 * @param s String
	 * @return s
	 */
	public String trimString(String s)
	{	
		s = s.trim();
		
		if(s.length() > 3)
			s = s.substring(0,3);
		
		if(s.isEmpty())
		{
			JOptionPane.showMessageDialog(null, "You have not entered a valid input(s).", 
					"Error: Empty Input Detected!", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		else if(s.contains("."))
		{
			JOptionPane.showMessageDialog(null, "Please input a non-decimal value.", 
					"Error: Decimal Detected!", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		else
			return s;
	}

	/**
	 * The following method takes an input and checks whether there are more words in the input.
	 * If there are, then a scanner scans each word and trims it if it longer than 3 characters.
	 * This trimmed word is now added to the array list.
	 * @param input String
	 */
	@SuppressWarnings("resource")
	public void multipleInputs(String input)
	{
		Scanner in = new Scanner(input);
		tree.emptyTempArray();
		String[] specialCh = {"!","@","]","#","$","%","^","&","*",",","+","=",")","£"};
		
		while(in.hasNext())
		{
			String word = in.next();
			String trimmed = trimString(word);
			
			if(trimmed == null)
				return;
			
			tree.getTemp().add(trimmed);
			
			//if the trimmed word contains a special character then throw an error.
			for (int i=0; i<specialCh.length; i++) 
			{
				if(trimmed.contains(specialCh[i]))
				{
					JOptionPane.showMessageDialog(null, "You have entered an input(s) using special characters!"
							,"Error Message", JOptionPane.ERROR_MESSAGE);
					tree.emptyTempArray();
					return;
				}
			}
		}
	}
	
	/**
	 * This method displays an error message that the tree is empty and
	 * clears all the input fields.
	 */
	public void emptyTreeError()
	{
		JOptionPane.showMessageDialog(null, "The tree is empty."
				,"Error Message", JOptionPane.ERROR_MESSAGE);
		clearInputFields();
	}
	
	/**
	 * This method is used to listen to events and perform the necessary actions required.
	 * @param e ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object s = e.getSource();
		AbstractButton absButton = (AbstractButton) e.getSource();
		
		//checks whether a button is selected or not
		boolean selected = absButton.getModel().isSelected();		
		
		if(s == insertButton)
		{
			/**
			 * If the insert button is pressed, then the program will send the input
			 * from the user to trimString method for the string to be processed and 
			 * returned back. If the string is valid then the main thread is started and
			 * the input is inserted onto the tree.
			 */
			String text = trimString(insertField.getText());
			
			if(text != null)
			{
				multipleInputs(insertField.getText());
				activateNextButton();			//enables the next button
				MainThread mainThread = new MainThread(this,0,text,"");
				mainThread.start();		//starts the main thread
			}
			clearInputFields();
			insertField.requestFocus();
		}
		else if(s == deleteButton)
		{
			/**
			 * If the delete button is pressed, then the program will send the input
			 * from the user to trimString method for the string to be processed and 
			 * returned back. If the string is valid then the main thread is started and
			 * the program checks whether the node is present in the tree and deletes it
			 * if found.
			 */
			String text = trimString(deleteField.getText());
			
			if(text != null)
			{
				multipleInputs(deleteField.getText());
				activateNextButton();
				MainThread mainThread = new MainThread(this,1,text,"");
				mainThread.start();
			}
			clearInputFields();
			deleteField.requestFocus();
		}
		else if(s == clearButton) {
			newTree("Are you sure you want to clear the current tree?", "Current tree cleared.");
		}
		else if(s == mItemExit) {
			System.exit(0);		//terminate the program
		}
		else if(s == tgDepthName)
		{
			//enables and disables the depth names by setting the variable
			//..toggleDepthName to true or false.
			if(selected)
			{
				history.append("~ Toggle Depth Name - Enabled.\n");
				toggleDepthName = true;
				refresh();
			}
			else
			{
				history.append("~ Toggle Depth Name - Disabled.\n");
				toggleDepthName = false;
				refresh();
			}
		}
		else if(s == tgDepthLines)
		{
			//enables and disables the depth line by setting the variable
			//..toggleDepthLines to true or false.
			if(selected)
			{
				history.append("~ Toggle Depth Lines - Enabled.\n");
				toggleDepthLines = true;
				refresh();
			}
			else
			{
				history.append("~ Toggle Depth Lines - Disabled.\n");
				toggleDepthLines = false;
				refresh();
			}
		}
		else if(s == mItemMin)
		{
			this.setState(Frame.ICONIFIED);
		}
		else if(s == playButton)
		{
			deactivatePauseButton();
			history.append("~ Resuming Animation.\n");
		}
		else if(s == pauseButton)
		{
			activatePauseButton();
			history.append("~ Paused Animation.\n");
		}
		else if(s == nextButton)
		{
			//if the next button is pressed then step forward the animation
			this.tree.advanceAnAnimationStep();
		}
		else if(s == mItemImage)
		{
			if(tree.getRoot() == null)
			{
				emptyTreeError();
				return;
			}
			else
			{
				//takes the tree panel and converts it to an image format
				makePanelImage(treePanel);
			}
		}
		else if(s == searchButton)
		{
			/**
			 * When the user searches for a range, the program will first check whether
			 * both fields are empty or not otherwise, it will check if the two inputs are 
			 * numbers so it can be compared as ints for error checking otherwise it will
			 * compare as strings.
			 * If all the error checks pass, then the main thread starts and range searching
			 * begins.
			 */
			if(tree.getRoot() == null)
			{
				emptyTreeError();
				return;
			}
			
			String from = trimString(fromField.getText());
			String to = trimString(toField.getText());
			
			boolean proceed = false;
			
			if(from != null && to != null)
			{
				if(tree.isNumeric(from) && tree.isNumeric(to))
				{
					int fromInt = Integer.parseInt(from);
					int toInt = Integer.parseInt(to);
					
					if(fromInt > toInt)
					{
						JOptionPane.showMessageDialog(null, 
								"'From' field input cannot be greater than 'To' field input.", 
								"Error: Invalid Input!", JOptionPane.ERROR_MESSAGE);
						clearInputFields();
					}
					else
						proceed = true;
				}
				else if(from.compareTo(to) > 0)
				{
					JOptionPane.showMessageDialog(null, 
							"'From' field input cannot be greater than 'To' field input.", 
							"Error: Invalid Input!", JOptionPane.ERROR_MESSAGE);
				}
				else
					proceed = true;
			}
			
			if(proceed)
			{
				tree.resetAllNodesColor();
				history.append("~ Range searching from "+from+" to "+to+".\n");
				activateNextButton();
				MainThread localThread = new MainThread(this,2,from,to);
				localThread.start();
			}
			
			clearInputFields();
			fromField.requestFocus();
		}
		else if(s == resetRangeButton)
		{
			/**
			 * If the reset button is pressed by the user then all the colors of
			 * nodes in the current tree will be reset back to their original color.
			 */
			tree.resetAllNodesColor();
			notes.clearNotes();
			clearInputFields();
			history.append("~ Resetting the range search.\n");
		}
		else if(s == mItemNew)
		{
			newTree("Any unsaved changes made to the current tree will be lost. \nWould you like to proceed?",
					"Menu item: New selected.");
		}
		else if(s == mItemOpen)
		{
			history.append("~ Menu item: Open selected.\n");
			openFile();
		}
		else if(s == mItemfindMinButton)
		{
			history.append("~ Finding the minimum node. \n");
			
			if(tree.getRoot() == null)
				emptyTreeError();
			else
				JOptionPane.showMessageDialog(null,"The Minimum Node in the current tree is "
					+"Node ["+tree.findMin()+"].");
		}
		else if(s == mItemfindMaxButton)
		{
			history.append("~ Finding the maximum node. \n");
			
			if(tree.getRoot() == null)
				emptyTreeError();
			else
				JOptionPane.showMessageDialog(null,"The Maximum Node in the current tree is "
					+ "Node ["+tree.findMax()+"].");
		}
		else if(s == mItemPostOrder)
		{
			tree.emptyTempArray();	//clear the array
			tree.printPostOrder(tree.getRoot());
			
			//if the tree is empty then throw an error
			if(tree.getRoot() == null)
				emptyTreeError();
			else
			{
				int reply = JOptionPane.showConfirmDialog(null, 
						"Post Order: "+printListWithBrackets()+
						"\nWould you like to save this details to a text file?", 
						"Information Message", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
		       
				if (reply == JOptionPane.YES_OPTION)
		        	writeFile("postorder","traversal");
			}
	        
		}
		else if(s == mItemPreOrder)
		{
			tree.emptyTempArray();
			tree.printPreOrder(tree.getRoot());
			
			//if the tree is empty then throw an error
			if(tree.getRoot() == null)
				emptyTreeError();
			else
			{
				int reply = JOptionPane.showConfirmDialog(null, 
						"Pre Order: "+printListWithBrackets()+
						"\nWould you like to save this details to a text file?", 
						"Information Message", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
		       
				if (reply == JOptionPane.YES_OPTION)
		        	writeFile("preorder","traversal");
			}
		}
		else if(s == mItemInOrder)
		{
			tree.emptyTempArray();
			tree.printInOrder(tree.getRoot());
			
			//if the tree is empty then throw an error
			if(tree.getRoot() == null)
				emptyTreeError();
			else
			{
				int reply = JOptionPane.showConfirmDialog(null, 
						"In Order: "+printListWithBrackets()+
						"\nWould you like to save this details to a text file?", 
						"Information Message", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
		       
				if (reply == JOptionPane.YES_OPTION)
		        	writeFile("inorder","traversal");
			}
		}
		else if(s == mItemTextFile)
		{
			if(tree.getRoot() == null)
			{
				emptyTreeError();
				return;
			}
			else
				writeFile("","stack");
		}
		else if(s == mItemGenRanTree)
		{
			 new RandomTreeGenerator(this);
		}
		else if(s == previous)
		{
			history.append("~ Stepped Backward.\n");
			deactivatePauseButton();
			tree.clearTree();
			tree.getNote().setNote("");
			tree.emptyTempArray();
			refresh();
			setSteps(1);
			
			if (!stack.empty())
				stack.pop();
			
			if(stack.isEmpty())
			{
				tree.clearTree();
				refresh();
			}
			else
			{
				for(int i=0; i<stack.size(); i++)
					tree.getTemp().add(stack.get(i));
				
				stack.clear();
			}
			
			//activate the next button and start the main thread
			activateNextButton();
			MainThread localThread = new MainThread(this,4,"","");
			localThread.start();
		}
	}
	
	/**
	 * The following method generates a random tree where the user can choose
	 * which input methods they wish to have from the Random Tree Generator class.
	 * @param pos boolean
	 * @param neg boolean
	 * @param word boolean
	 * @param size int
	 */
	public void randomGenerator(boolean pos, boolean neg, boolean word, int size)
	{
		tree.emptyTempArray();
		
		if(pos && !neg && !word)
			generateArray(size, 100, 0, true);
		else if(!pos && neg && !word)
			generateArray(size, 100, 99, true);
		else if(!pos && !neg && word)
			wordGenerator(size);
		else if(pos && neg && !word)
			generateArray(size, 100, 50, true);
		else if(pos && !neg && word)
		{
			if(size%2 == 0)
			{
				int size0 = size/2;
				generateArray(size0, 100, 0, true);
				wordGenerator(size0);
			}
			else
			{
				int size1 = size/2+1;
				int size2 = size/2;
				generateArray(size1, 100, 0, true);
				wordGenerator(size2);
			}
		}
		else if(!pos && neg && word)
		{
			if(size%2 == 0)
			{
				int size0 = size/2;
				generateArray(size0, 100, 99, true);
				wordGenerator(size0);
			}
			else
			{
				int size1 = size/2+1;
				int size2 = size/2;
				generateArray(size1, 100, 99, true);
				wordGenerator(size2);
			}
		}
		else if(pos && neg && word)
		{
			if(size%3 == 0)
			{
				int size0 = size/3;
				generateArray(size0, 100, 0, true);
				generateArray(size0, 100, 99, true);
				wordGenerator(size0);
			}
			else
			{
				int size1 = size/2+1;
				int size2 = (size - size1) / 2;
				generateArray(size1, 100, 0, true);
				generateArray(size2, 100, 99, true);
				wordGenerator(size2);
			}
		}

	    activateNextButton();
		MainThread localThread = new MainThread(this,3,"","");
		localThread.start();
	}
	
	/**
	 * The following method initializes an array list and adds each word 
	 * from the text file 'words.txt' onto it.
	 * @param size int
	 */
	private void wordGenerator(int size)
	{
		words = new ArrayList<String>();
		try 
		{
			Scanner in = new Scanner(new FileReader("words.txt"));
			
			while(in.hasNextLine())
				words.add(in.nextLine());
			
			in.close();
			generateArray(size, words.size()-1, 0, false);
				
		} 
		catch (FileNotFoundException e) {
		}
	}
	
	/**
	 * This method generates a random array by using a random object
	 * to select the which index from the 'words' array to get or
	 * chooses a random number and adds it to the 'tree' array.
	 * @param size int
	 * @param brac int
	 * @param sub int
	 * @param isInt boolean
	 */
	private void generateArray(int size, int brac, int sub, boolean isInt)
	{
		Random ran = new Random();

		for (int i = 0; i < size; i++)
		{
			//selects a random index position when selecting words/selects a random 
			//...to be inserted into the tree arrayList.
			int randomInt = ran.nextInt(brac)-sub; 
			int counter = 0;

			if(isInt)
			{
				//if the user selects a number then a random number is inserted into the
				//...tree arrayList. This arrayList is then checked if it contains any duplicates.
				//If it does then the number is removed and 'i' is decremented by 1.
				String numToAdd = ""+randomInt;
				tree.getTemp().add(numToAdd);

				for(int j=0; j<tree.tempListSize(); j++)
				{
					if(numToAdd.compareTo(tree.getTemp().get(j)) == 0)
					{
						counter ++;
						
						if(counter >= 2)
						{
							tree.getTemp().remove(j);
							i--;
						}
					}
				}
			}
			else
			{
				//if the user selects a word then a random number is used as an index position 
				//...to retrieve a word from the word arrayList. The retrieved word is then 
				//...checked against the tree arrayList to see if it contains any duplicates.
				//If it does then the word is removed and 'i' is decremented by 1.
				String wordToAdd = words.get(randomInt);
				tree.getTemp().add(wordToAdd);

				//checking if there are any duplicates in the tree arraylist
				if(tree.tempListSize() > 0)
				{
					for(int j=0; j<tree.tempListSize(); j++)
					{
						if(wordToAdd.compareTo(tree.getTemp().get(j)) == 0)
						{
							counter++;

							if(counter >= 2)
							{
								tree.getTemp().remove(j);
								i--;
							}
						}
					} 
				}
			}
		}
	}
	
	/**
	 * The following method deactivates the next button as well as
	 * enabling and disabling other buttons on the main window.
	 */
	private void deactivateNextButton()
	{
		insertButton.setEnabled(true);
		deleteButton.setEnabled(true);
		clearButton.setEnabled(true);
		searchButton.setEnabled(true);
		resetRangeButton.setEnabled(true);
		nextButton.setEnabled(false);
		previous.setEnabled(true);
	}
	
	/**
	 * The following method activates the next button as well as
	 * enabling and disabling other buttons on the main window.
	 */
	private void activateNextButton()
	{
		insertButton.setEnabled(false);
		deleteButton.setEnabled(false);
		clearButton.setEnabled(false);
		searchButton.setEnabled(false);
		resetRangeButton.setEnabled(false);
		nextButton.setEnabled(true);
		previous.setEnabled(false);
	}
	
	/**
	 * If the pause button is pressed then set the variable 'pause' to
	 * true and set the pause and previous button to false and set 
	 * the play button to true.
	 */
	private void activatePauseButton()
	{
		this.pause = true;
		pauseButton.setEnabled(false);
		playButton.setEnabled(true);
		previous.setEnabled(false);
	}
	
	/**
	 * If the play button is pressed then set the variable 'pause' to
	 * false and disable the play button and enable the pause and
	 * previous button.
	 */
	private void deactivatePauseButton()
	{
		this.pause = false;
		playButton.setEnabled(false);
		pauseButton.setEnabled(true);
		previous.setEnabled(true);
	}
	
	/**
	 * This method is used to perform the necessary operations that the
	 * user chooses from the main window and updates the size and the height
	 * field accordingly.
	 * @param x int
	 * @param s1 String
	 * @param s2 String
	 */
	public void performOperation(int x, String s1, String s2) 
	{
		if(x == 0)
		{
			for(int i=0; i<tree.tempListSize(); i++)
			{
				tree.insert(tree.getTemp().get(i));
				history.append("~ Inserting: "+tree.getTemp().get(i)+"\n");
				refresh();
			}
		}
		else if(x == 1)
		{
			for(int i=0; i<tree.tempListSize(); i++)
			{
				tree.delete(tree.getTemp().get(i));
				history.append("~ Deleting: "+tree.getTemp().get(i)+"\n");
				refresh();
			}
		}
		else if(x == 2)
		{
			tree.rangesearch(s1,s2);
			refresh();
		}
		else if(x == 3)
		{
			for(int i=0; i<tree.tempListSize(); i++)
			{
				tree.insert(tree.getTemp().get(i));
				history.append("~ Inserting: "+tree.getTemp().get(i)+"\n");
				stack.push("i "+tree.getTemp().get(i));
				refresh();
			}
		}
		else if(x == 4)
		{
			if(this.pause == false)
				openF = true;
			
			for(int i=0; i<tree.tempListSize(); i++)
			{
				String line = tree.getTemp().get(i);
				
				String [] words = line.split(" "); //splits the line with the space as a delimiter
				
				if(words[0].compareTo("i") == 0)
					tree.insert(words[1]);
				else if(words[0].compareTo("d") == 0)
					tree.delete(words[1]);
				else
				{
					JOptionPane.showMessageDialog(null, 
							"This File is invalid/corrupted.", 
							"Error Input Format", JOptionPane.ERROR_MESSAGE);
					break;
				}
				
				refresh();
			}
			openF = false;
			setSteps(10);
		}
		deactivateNextButton();
	}
	
	/**
	 * The following method takes a panel as its parameter and converts it to a 
	 * buffered image ready to be exported to an external file.
	 * @param panel Component
	 */
	private void makePanelImage(Component panel)
    {
        Dimension size = panel.getSize();
        BufferedImage image = new BufferedImage(
                    size.width, size.height , BufferedImage.TYPE_INT_ARGB);
        panel.paint(image.createGraphics());
        
	    JFileChooser chooser = new JFileChooser();
	    chooser.setCurrentDirectory(new File(System.getProperty("user.home")+"/Desktop"));	//sets the default directory
	    int retrival = chooser.showSaveDialog(null);
	    
	    if (retrival == JFileChooser.APPROVE_OPTION) 
	    {
	        try 
	        {
	            File f = chooser.getSelectedFile();
	            String test = f.getAbsolutePath();
	            ImageIO.write(image,"png",new File(test));
	            history.append("~ Exported tree as an image to: "+test+".\n");
	        } 
	        catch (Exception ex) {}
	    }
    }

	/**Accessor Methods**/
	public TreePanel getTreePanel() { return treePanel; }
	public JSlider getSpeedSlider() { return slider; }
	public boolean isPause() { return pause; }
	public int getSteps() { return STEPS; }
	public boolean getOpenF() { return openF; }
	
	public void setSteps(int s) { STEPS = s; }
	public Stack<String> getStack() { return stack; }
	public int getStackSize() { return stack.size(); }
}
