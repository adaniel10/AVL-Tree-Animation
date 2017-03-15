import java.awt.*;

/**
 * This class is used to represent a node in a Tree Class.
 * It calculates the height and the size of the node and sends the balancing
 * difference back to the balancing method in the tree class when required.
 * This class also draws the shape and the data of the node inside its shape.
 * It also sets the color of the node to be displayed to the user.
 * 
 * @author Abin Daniel
 * @title MSc Honors Project
 */
public class Node implements Comparable<Node>
{
	private Node left = null, right = null, parent = null;
	private String data;
	private int sumOfTheHeight = 1, size = 1, height = 1;
	private int numOfSteps, leftSize, rightSize, x_coord, y_coord, advanceToX, advanceToY;
	private Color fgcolor, bgcolor;
	private Tree tree;
	private int [] p = new int[6];
	private Font valueFont = new Font("Courier New", Font.BOLD, 15);
	
	
	/* Constructor */
	public Node(String data, Tree tree, int xPos, int yPos)
	{
		this.tree = tree;
		this.data = data;
		
		//setting the x and y coordinates
		this.advanceToX = xPos;
		this.x_coord = advanceToX;
		this.advanceToY = yPos;
		this.y_coord = advanceToY;
		
		//setting the color of the nodes to aqua color with black border
		setColor(getBlackColor(), getPresetColor());
		
		this.numOfSteps = 0;
	}    
	
	//Constructor - creates a node with an initial position
	public Node(String data, Tree p) {
		this(data,p,p.getScreenWidth()/2,-20);
	}
	
	//Another constructor for this class
	public Node(Node n) {
		this(n.data, n.tree, n.x_coord, n.y_coord);
	}
	
	/**
	 * Mutator methods
	 * This method is used so each time it is called, it can be used to set a variable.
	 */
	public void setLeft(Node l) { this.left = l; }
	public void setRight(Node r) { this.right = r; }
	public void setHeight(int h) { this.height = h; }
	public void setData(String s) { this.data = s; }
	public void setParent(Node p) { this.parent = p; }
	public void fgColor(Color color) { this.fgcolor = color; }
	public void bgColor(Color color) { this.bgcolor = color; }
	public void setArrayP(int value, int index) { p[index] = value; }
	
	/**
	 * The following method moves the 'search' node to above the input node.
	 */
	public void advanceToNode(Node node) 
	{
		advanceTo(node.advanceToX, node.advanceToY - 30 - 15);
	}

	/**
	 * This method makes the first node being entered into a new tree to go to its root position.
	 */
	public void advanceToTheRoot() 
	{
		advanceTo(this.tree.getRootX(), this.tree.getRootY());
	}

	/**
	 * This method makes the new node go above the root node before being entered into the tree.
	 */
	public void advanceToAboveTheRoot() 
	{
		advanceTo(this.tree.getRootX(), this.tree.getRootY() - 30 - this.tree.getYspan());
	}

	/**
	 * This method moves a node to the right side of a node which is being searched.
	 */
	public void advanceToTheRight() 
	{
		advanceTo(this.tree.getScreenWidth() + 15, this.tree.getScreenHeight() + 45);
	}

	/**
	 * This method moves a node to the left side of a node which is being searched.
	 */
	public void advanceToTheLeft() 
	{
		advanceTo(-15, this.tree.getScreenHeight() + 45);
	}

	/**
	 * The following method is used to drop the node below the screen if it is being deleted or 
	 * if the node has not been found.
	 */
	public void goDown() 
	{
		advanceTo(this.advanceToX, this.tree.getScreenHeight() + 45);
	}
	
	/**
	 * Accessor methods
	 */
	public Node getLeft() { return this.left; }
	public Node getRight() { return this.right; }
	public int getHeight() { return this.height; }
	public String getData() { return this.data; }
	public Node getParent() { return this.parent; }
	public int getX() { return x_coord; }
	public int getY() { return y_coord; }
	public boolean isRootNode() { return this.parent == null; }
	public boolean isLeft() { return this.parent.left == this; }
	public int getArrayP(int index) { return p[index]; }
	
	//access all the colors required for the nodes
	public Color getPresetColor() { return new Color(0x40, 0xFF, 0xFF); }
	public Color getInsertColor() { return Color.yellow.brighter(); }
	public Color getDeleteColor() { return Color.red; }
	public Color getFindColor() { return Color.orange; }
	public Color getFoundColor() { return Color.green; }
	public Color getInvisibleColor() { return Color.white; }
	public Color getBlackColor() { return Color.black; }
	
	/**
	 * This method takes a node as a parameter and uses it to compare
	 * the string value with another node.
	 * @param o Node
	 */
	@Override
	public int compareTo(Node o) 
	{
		if(this.getData().equals(o.getData()))
			return 0;
		
		if(this.getData().compareTo(o.getData()) < 0)
			return -1;
		
		if(this.getData().compareTo(o.getData()) > 0)
			return 1;
		
		return 0;
	}
	
	//Sets the color of the node
	public void setColor(Color color1, Color color2) {
		this.fgcolor = color1;
		this.bgcolor = color2;
	}
	
	/**
	 * The following method is used to connect a node to the left side
	 * or the right size of the 'this' node.
	 */
	public void connectNode(Node node, String position)
	{
		if(position.equals("right"))
			this.right = node;
		else
			this.left = node;
		
		//if the node 'n' is not null then set its parent 
		//...to 'this.
		if (node != null) 
			node.parent = this;
	}

	/**
	 * This following method calculates the height and size of the node.
	 */
	public void performCalculations() 
	{
		for(int i=0; i<p.length; i++)
			p[i] = 0;
		
		if (this.right != null) 
		{
			setArrayP(this.right.height,3);
			setArrayP(this.right.sumOfTheHeight,5);
			setArrayP(this.right.size,1);
		}
		
		if (this.left != null) 
		{
			setArrayP(this.left.height,2);
			setArrayP(this.left.sumOfTheHeight,4);
			setArrayP(this.left.size,0);
		}
		
		setCalculations();

	}
	
	/**
	 * This method is used to return the balance difference of the node.
	 * @return result int
	 */
	public int balanceDifference() {
		
		int result, x1, x2;
		
		if(this.left == null)
			x1 = 0;
		else
			x1 = this.left.height;
		
		if(this.right == null)
			x2 = 0;
		else
			x2 = this.right.height;
		
		result = x2 - x1;
	
		return result;
	}
	
	/**
	 * This method is used to draw the tree by drawing the lines that joins nodes
	 * together in the tree. The nodes are then drawn.
	 * @param graph Graphics
	 */
	public void drawTree(Graphics graph) 
	{
		// makes the lines, shape and the value of the node in a smooth format
		((Graphics2D) graph).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		//sets the color of the connection line
		graph.setColor(getBlackColor());
		
		if (this.left != null) 
		{
			//this draw the line connections between 2 nodes
			graph.drawLine(this.x_coord, this.y_coord, this.left.x_coord, this.left.y_coord);
			this.left.drawTree(graph);
		}
		
		if (this.right != null) 
		{
			//this draw the line connections between 2 nodes
			graph.drawLine(this.x_coord, this.y_coord, this.right.x_coord, this.right.y_coord);
			this.right.drawTree(graph);
		}
		
		drawNode(graph);
	}

	/**
	 * This method is used to draw the node as a circle shape and the value of the node
	 * inside the shape.
	 * @param graph Graphics
	 */
	public void drawNode(Graphics graph) 
	{
		//draws the node shape
		graph.setColor(this.bgcolor);
		graph.fillOval(this.x_coord - 15, this.y_coord - 15, 30, 30);
		graph.setColor(this.fgcolor);
		graph.drawOval(this.x_coord - 15, this.y_coord - 15, 30, 30);
		
		// draws the value inside the node
		graph.setFont(valueFont);
		graph.setColor(this.fgcolor);
		FontMetrics fontMetrics = graph.getFontMetrics(valueFont);
		graph.drawString(this.data, this.x_coord - fontMetrics.stringWidth(""+this.data) / 2,
			this.y_coord - fontMetrics.getHeight() / 2 + fontMetrics.getAscent());
	}

	/**
	 * This method is used to move the nodes in the tree a node at a time.
	 * It checks if 'this' node's right / left child  is null or not.
	 * If it is not null then check the next right / left node. 
	 */
	public void moveEachNodeInTheTree() 
	{
		if (this.right != null)
			this.right.moveEachNodeInTheTree();
		
		if (this.left != null)
			this.left.moveEachNodeInTheTree();
		
		shiftNodes();
	}
	
	/**
	 * This method sets the calculations for the height of the node,
	 * its size and the total height.
	 */
	private void setCalculations()
	{
		this.height = (tree.max(getArrayP(2), getArrayP(3))) + 1;
		this.size = 1 + getArrayP(0) + getArrayP(1);
		this.sumOfTheHeight = this.size + getArrayP(4) + getArrayP(5);
		
		balanceDifference();
	}

	/**
	 * This method is used to move the new node from its initial position from the top
	 * of the screen to its new position in the tree.
	 * Code modified from function move() obtained from
	 * https://github.com/ibrahimshbat/AACAT/blob/master/src/main/java/AVLTreeDataStructure/AVLNode.java
	 */
	public void shiftNodes()
	{
		if (this.numOfSteps > 0) 
		{
			this.x_coord = ((this.advanceToX - this.x_coord) / this.numOfSteps ) + this.x_coord;
			this.y_coord = ((this.advanceToY - this.y_coord) / this.numOfSteps) + this.y_coord;
			this.numOfSteps--;
		}
	}

	/**
	 * This method is used to set the nodes left and right size.
	 */
	public void calculateAngleBetweenNodes()
	{
		//if 'this' node's left child is not null then set the size of the
		//..left to the left's leftSize plus the left's rightSize.
		if(this.left != null)
			this.leftSize = this.left.leftSize + this.left.rightSize;
		else
			this.leftSize = this.tree.getXspan() + this.tree.getRadius() + 5;
			
		//if 'this' node's right child is not null then set the size of the
		//..right to the right's rightSize plus the right's leftSize.
		if(this.right != null)
			this.rightSize = this.right.rightSize + this.right.leftSize;
		else
			this.rightSize = this.tree.getRadius() + this.tree.getXspan() + 5;
	}

	/**
	 * This method is used to go through the tree until each nodes is 
	 * positioned.
	 */
	public void repositionTree() 
	{
		if (this.left != null)
			this.left.repositionTree();
		
		if (this.right != null)
			this.right.repositionTree();
		
		calculateAngleBetweenNodes();
		setNodePositon();
	}

	/**
	 * This method is used to set the node to its right position.
	 */
	private void setNodePositon()
	{
		if (this.parent == null)
			advanceToTheRoot();

		if (this.right != null) {
			this.right.advanceTo(this.advanceToX + this.right.leftSize, this.advanceToY + 40);
			this.right.setNodePositon();
		}
		
		if (this.left != null) {
			this.left.advanceTo(this.advanceToX - this.left.rightSize, this.advanceToY + 40);
			this.left.setNodePositon();
		}
	}
	
	/**
	 * The following method moves a node by setting its x and y
	 * coordinates.
	 * @param x int
	 * @param y int
	 */
	public void advanceTo(int x, int y) {
		this.advanceToX = x;
		this.advanceToY = y+15;
		
		//checks which step count the application is at.
		this.numOfSteps = this.tree.getMainFrame().getSteps();
	}
	
	/**
	 * The following method moves from a node's x and y coordinates 
	 * to its set x and y coordinates.
	 * @param node Node
	 */
	public void advanceTo(Node node) 
	{
		advanceTo(node.advanceToX, node.advanceToY-15);
	}
}
