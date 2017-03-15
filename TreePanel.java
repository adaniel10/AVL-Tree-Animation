import java.awt.*;
import javax.swing.*;

/**
 * This class is used to draw the nodes and their connections onto the 
 * center panel of the frame. It implements runnable and this thread is started
 * after the main frame is initialized.
 * 
 * @author Abin Daniel
 * @title MSc Honors Project
 */
@SuppressWarnings("serial")
public class TreePanel extends JPanel implements Runnable {
	
	/**Instance Variable**/
	private boolean name = false, lines = false;
	private Tree tree = null;
	private MainFrame mf;
	private Thread thread = null;
	private Dimension dimensionSize, currentSize;
	private Image img;
	private Graphics graph;
	
	/**Constructor**/
	public TreePanel() {
		//empty constructor
	}
	
	/**
	 * This method initializes the tree and the mainframe
	 * object.
	 * @param tree Tree
	 * @param mf Mainframe
	 */
	public void setObjects(Tree tree, MainFrame mf) {
		this.tree = tree;
		this.mf = mf;
	}
	
	/**
	 * Sets the depth name and lines by using the values of
	 * the boolean variables.
	 * @param name boolean
	 * @param lines boolean
	 */
	public void init(boolean name, boolean lines)
	{
		this.name = name;
		this.lines = lines;
	}
	
	/**
	 * The method starts running the thread in this class.
	 */
	public void startDrawingThread() 
	{
		if (this.thread == null) 
		{
			this.thread = new Thread(this);
			this.thread.start();
		}
	}
	
	/**
	 * The following method paints the drawing onto the panel by first checking
	 * the size of the screen, then drawing the necessary depth lines and/or names
	 * and finally drawing the nodes.
	 */
	public void paintComponent(Graphics g)
	{	
		currentSize = getSize();
		
		//This condition checks the size of the screen and adjust the drawing of the tree
		//...accordingly.
		/* The following if statement is copied from function check_size() obtained from 
		 * https://github.com/ibrahimshbat/AACAT/blob/master/src/main/java/AVLTreeDataStructure/TreeCanvas.java */
		if ((this.img == null) || (currentSize.width != this.dimensionSize.width)
				|| (currentSize.height != this.dimensionSize.height)) 
		{
			this.img = createImage(currentSize.width, currentSize.height);
			this.graph = this.img.getGraphics();
			this.dimensionSize = currentSize;
			this.tree.readjustTheNodes();
		}
		
		this.graph.setColor(Color.WHITE); //sets the color to white
		this.graph.fillRect(0, 0, getWidth(), getHeight()); //paints the background in white
		
	    if(this.tree != null)
	    {
	    	if(lines)
	    		drawDashedLine(this.graph);
	    	
	    	if(name)
	    		drawDepthString(this.graph);
	    	
	    	this.tree.drawOntoTheDisplayPanel(this.graph);
	    }
	   g.drawImage(this.img, 0, 0, null);
	}
	
	/**
	 * The following method takes a graphics object as a parameter and
	 * draws the lines across the panel.
	 * @param g
	 */
	public void drawDashedLine(Graphics g)
	{
        //creates a copy of the Graphics instance
		Graphics2D g2d = (Graphics2D) img.getGraphics();

        //set the stroke of the copy, not the original 
        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, 
        		BasicStroke.JOIN_BEVEL, 0, new float[]{1,2}, 0);
        g2d.setStroke(dashed);
        
        final int x1 = 0;
        final int x2 = this.getWidth();
    	final int numLines = 10;
    	int y = 100;
        
        //draws the dashed lines
        for(int i=0; i<numLines; i++)
        {
        	g2d.drawLine(x1, y, x2, y); // (x1,y1) (x2,y2)
        	y += 55;
        }
        
        //gets rid of the copy
        g2d.dispose();
	}
	
	/**
	 * This method is used to identify the depth of a line.
	 * @param g
	 */
	public void drawDepthString(Graphics g)
	{
		//creates a copy of the Graphics instance
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        
		final int numLines = 10;
		int y = 100;
    	int x = this.getWidth();
    	g2d.setFont(new Font("Courier New", Font.PLAIN, 11)); 
    	
        for(int i=0; i<numLines; i++)
        {
        	g2d.drawString("depth "+i, x-50, y-5);
        	y += 55;
        }
        
        //gets rid of the copy
        g2d.dispose();
	}
	
	/**
	 * This method runs continuously when started and repaints the tree and 
	 * allows the animation speed to be altered.
	 */
	public void run() 
	{
		for(;;)
		{
			repaint();	//re-draw the tree onto the panel
			
			try 
			{
				int sleepValue = mf.getSpeedSlider().getValue();
				//pause the thread according to the value of the JSlider
				Thread.sleep(sleepValue);
			} 
			catch (InterruptedException e) { break; }
		}
	}
	
	/**Accessor Methods**/
	public Dimension getDimensionSize() { return dimensionSize; }
	public Dimension getCurrentSize() { return currentSize; }
}
