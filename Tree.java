import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * This class represents a tree class which is made up
 * of a variety of nodes. It handles all the events from the user
 * input and sends it appropriate methods as required.
 * 
 * @author Abin Daniel
 * @title MSc Honors Project
 *
 */
public class Tree {
	
	/**Instance variables**/
	private Node root;
	public Node node;
	private ArrayList<String> temp;
	private MainFrame mf;
	private int radius, xCoordinateSpanLenth, yCoordinateSpanLength, xRootCoordinate, 
					yRootCoordinate, heightOfScreen, widthOfScreen;
	private Animation anim;
	private Notes note;
	
	//Constructor
	public Tree(MainFrame mf) 
	{
		this.root = null;
		this.node = null;
		this.mf = mf;
		this.note = new Notes();
		temp = new ArrayList<String>();
	}
    
	/**
	 * This method is used the check whether the input string
	 * is a number or not.
	 * @param input String
	 * @return boolean
	 */
	public boolean isNumeric(String input) {
		  try 
		  {
		    Integer.parseInt(input);
		    return true;
		  }
		  catch (NumberFormatException e) {
		    return false;
		  }
	}
	
	/**
	 * This method is used to create and insert a new node into a tree
	 * and start the animation thread.
	 * @param data String
	 */
	public void insert(String data)
	{
		InsertNode insNode = new InsertNode(this, data);
		startAnimationThread(insNode);
	}
	
	/**
	 * This method is used to delete a node from the tree and start the animation
	 * thread.
	 * @param data String
	 */
	public void delete(String data)
	{
		DeleteNode delNode = new DeleteNode(this,data);
		startAnimationThread(delNode);
	}
	
	/**
	 * This method is used to range search the tree using two input values and then
	 * starts the animation thread.
	 * @param s1 String
	 * @param s2 String
	 */
	public void rangesearch(String s1, String s2)
	{
		RangeSearch rs = new RangeSearch(this, s1, s2);
		startAnimationThread(rs);
	}
    
	/**
	 * The method is used to rotate the node in a tree. If the input node has a left node then
	 * a right rotation will be performed, vice versa.
	 * @param node Node
	 */
	public void rotateNode(Node node) 
	{
		boolean test = false;
		
		if(node.isLeft())
			test = true;
		else
			test = false;
		
		Node currentNode = node.getParent();
		
		//if the current node is the root node then set the input 'node' as the root
		//...node and set its parent to null
		if (currentNode.isRootNode()) 
		{
			this.root = node;
			node.setParent(null);
		} 
		//else if the current node has a left node then link this
		//...node with the input 'node' otherwise link it with its right.
		else if (currentNode.isLeft()) 
			currentNode.getParent().connectNode(node,"left");
		else
			currentNode.getParent().connectNode(node,"right");
		
		//Left rotation
		if(!test)
		{
			//if the input 'node's left child is empty then set the current node's right
			//...child to null.
			if (node.getLeft() == null)
				currentNode.setRight(null);
			else 
				currentNode.connectNode(node.getLeft(),"right");
			
			//connect the input 'node' with the current node.
			node.connectNode(currentNode,"left");
		}
		
		//Right rotation
		if(test)
		{
			if (node.getRight() == null) 
				currentNode.setLeft(null);
			else 
				currentNode.connectNode(node.getRight(),"left");
			
			node.connectNode(currentNode,"right");
		}
		
		//If there are nodes present in the current tree then position
		//...all the nodes by giving them their x and y coordinates.
		if (this.root != null)
			this.root.repositionTree();
		
		if (node.getLeft() != null) 
			node.getLeft().performCalculations();
		
		if (node.getRight() != null) 
			node.getRight().performCalculations();
		
		node.performCalculations();
	}
	
	/**
	 * This method advances an animation step by temporarily resuming the animation thread.
	 */
	public void advanceAnAnimationStep() { this.anim.resumeThread(); }

	/**
	 * This method is used to start the thread in the
	 * Animation class.
	 * @param anim AnimationThread
	 */
	private void startAnimationThread(Animation anim) 
	{
		this.anim = anim;
		this.anim.start();
		try 
		{
			this.anim.join();
		} 
		catch (InterruptedException ie) {}
	}

	/**
	 * This method makes the tree empty.
	 */
	public void clearTree() 
	{
		getNote().setNote("");
		this.root = null;
		this.node = null;
	}

	/**
	 * The method is used to draw the tree on the TreeDisplay panel.
	 * @param graph Graphics
	 */
	public void drawOntoTheDisplayPanel(Graphics graph) 
	{	
		if (this.root != null) 
		{
			this.root.moveEachNodeInTheTree();
			this.root.drawTree(graph);
		}
		if (this.node != null) 
		{
			this.node.shiftNodes();
			this.node.drawNode(graph);
		}
	}

	/**
	 * This method calculates the width and the height of the tree display
	 * panel and sets the x and y distance between the nodes when being displayed.
	 */
	public void readjustTheNodes() 
	{
		int ten = 10;
		this.radius = ten+5;
		this.xCoordinateSpanLenth = ten;
		this.yCoordinateSpanLength = ten;
		
		this.widthOfScreen = this.mf.getTreePanel().getDimensionSize().width; 	// the screen width
		this.heightOfScreen = this.mf.getTreePanel().getDimensionSize().height; 	// the screen height
		
		//this ensures the root node is displayed in the middle of the tree display panel
		this.xRootCoordinate = (this.widthOfScreen / 2); 			//x coordinate
		this.yRootCoordinate = (5 * this.radius + this.yCoordinateSpanLength); 	//y coordinate
		
		repositionTheTree();	
	}

	/**Accessor Methods**/
	public Node getRoot() { return root; }
	public Node getNode() { return node; } 
	public int getRootX() { return xRootCoordinate; }
	public int getRootY() { return yRootCoordinate; }
	public int getRadius() { return radius; }
	public int getXspan() { return xCoordinateSpanLenth; }
	public int getYspan() { return yCoordinateSpanLength; }
	public int getScreenHeight() { return heightOfScreen; }
	public int getScreenWidth() { return widthOfScreen; }
	public Notes getNote() { return this.note; }
	public MainFrame getMainFrame() { return this.mf; }
	
	/**Mutator methods**/
	public void setDS(Notes note) { this.note = note; }
	public void setNode(Node n) { this.node = n; }
	public void setRoot(Node r) { this.root = r; }
	
	/**
	 * This method is used to reset all the nodes back to its default color.
	 */
	public void resetAllNodesColor() { resetAllNodesColor(root); }
	private void resetAllNodesColor(Node n)
	{
		if(n == null)
			return;
		else
		{
			n.bgColor(root.getPresetColor());
			resetAllNodesColor(n.getLeft());
			resetAllNodesColor(n.getRight());
		}
	}
	
	public int tempListSize() { return temp.size(); } //returns the size of the range search array
	public ArrayList<String> getTemp() { return temp; }	//returns the range search list
	public void emptyTempArray() { temp.clear(); }	//clears the Temp array
	
	/**
	 * Counts the number of nodes present in the tree
	 * @param n Node
	 * @return sum int
	 */
	private int countNodes(Node n) 
	{ 
		if(n == null){
			return 0;
		}
		else
		{
			int sum = 1;
			sum += countNodes(n.getLeft());
			sum += countNodes(n.getRight());
			return sum;
		}
	}
	public int countNodes() { return countNodes(root); }
	
	/**
	 * The following method calculates which input is greater and returns it.
	 * @param left int
	 * @param right int
	 * @return int
	 */
	public int max (int left, int right) 
	{
		if(left > right)
			return left;
		else
			return right;
	}
	
	/**
	 * This method is used to find the node with the maximum depth
	 * by using a recursive method.
	 * @param n Node
	 * @return int
	 */
	public int maxDepth(Node n) 
	{ 
		if(n == null)
			return -1;
		else
		{
			int lHeight = maxDepth(n.getLeft());
			int rHeight = maxDepth(n.getRight());
			
			return max(lHeight,rHeight)+1;
		}
	}

	/**
	 * The following method finds the maximum node present in the current tree.
	 * @param n Node
	 * @return
	 */
	private String findMax(Node n)
	{
		if(n == null)
			return "The current tree is empty!";
		else
		{	
			String sum = "";
			if(n.getRight() != null)
			{
				sum += findMax(n.getRight());
				return sum;
			}
			else
				return n.getData();
		}
	}
	public String findMax() { return findMax(root); }

	/**
	 * The following method finds the minimum node present in the current tree.
	 * @param n Node
	 * @return
	 */
	private String findMin(Node n)
	{
		if(n == null)
			return "The current tree is empty!";
		else
		{	
			String sum = "";
			if(n.getLeft() != null)
			{
				sum += findMin(n.getLeft());
				return sum;
			}
			else
				return n.getData();
		}
	}
	public String findMin() { return findMin(root); }
	
	/**
	 * This method is used to nodes in the current tree in post order.
	 * @param n Node
	 */
	public void printPostOrder(Node n)
	{
	  if (n == null) 
		  return;
	  
	  // first recur on both subtrees 
	  printPostOrder(n.getLeft()); 
	  printPostOrder(n.getRight());
	  
	  // then deal with the node 
	  temp.add(n.getData()); 		
	}
	
	/**
	 * This method is used to print all the nodes in the tree in pre order.
	 * @param n
	 */
	public void printPreOrder(Node n)
	{
	  if (n == null) 
		  return;
	  
	  //first add the current node 
	  temp.add(n.getData());
	  
	  //then recur on both subtrees 
	  printPreOrder(n.getLeft()); 
	  printPreOrder(n.getRight());
	}
	
	/**
	 * This method is used to print the current tree in In Order.
	 * @param n Node
	 */
	public void printInOrder(Node n)
	{
	  if (n == null) 
		  return;
	  
	  //first recur on left subtree
	  printInOrder(n.getLeft()); 
	  
	  //then add the current node 
	  temp.add(n.getData());
	  
	  //then recur on the right subtree
	  printInOrder(n.getRight());		
	}
	
	/**
	 * The following method checks the balance difference of each node and performs
	 * single or double rotations if necessary.
	 * @param currentNode Node
	 * Code modified from function run obtained from
	 * https://github.com/ibrahimshbat/AACAT/blob/master/src/main/java/AVLTreeDataStructure/AVLInsert.java
	 */
	public void reBalanceNode(Node currentNode)
	{
		while (currentNode != null) 
		{
			currentNode.bgColor(Color.PINK); 	// marking a nodes color with pink to calculate its details
			currentNode.performCalculations();	//Perform calculations on this node
			getNote().setNote("Checking balance at node ["+currentNode.getData()+"]...");
			anim.waitOnPause();
			
			//if the balance difference of this node is -2 then a right or left-right rotation needs to be performed
			if (currentNode.balanceDifference() == -2) 
			{
				getNote().setNote("Balancing required at node ["+currentNode.getData()+
						"] since it has a balance difference of 2.");
				anim.waitOnPause();
				
				//if the current nodes balance difference is 1 then perform a right rotation
				if (currentNode.getLeft().balanceDifference() != 1) 
				{
					getNote().setNote("Performing right rotation at node ["+currentNode.getLeft().getData()+
							"] by making node ["+currentNode.getLeft().getParent().getData()+"] its right subtree.");
					//setting this node back to its normal color since it will not require an update
					currentNode.bgColor(root.getPresetColor());
					currentNode = currentNode.getLeft();
					//setting this nodes color to pink since it will require an update
					currentNode.bgColor(Color.PINK);
					rotateNode(currentNode);	//rotate this node
				} 
				else //otherwise perform a left-right rotation
				{
					currentNode.bgColor(root.getPresetColor());
					currentNode = currentNode.getLeft().getRight();
					currentNode.bgColor(Color.PINK);
					getNote().setNote("Performing a left-right rotation at node ["+currentNode.getData()+"].");
					anim.waitOnPause();
					
					getNote().setNote("Making node ["+currentNode.getParent().getData()+
							"] the left subtree of node ["+currentNode.getData()+"].");
					rotateNode(currentNode);	//rotate this node
					anim.waitOnPause();
					
					getNote().setNote("Performing right rotation at node ["+currentNode.getData()+
							"] by making node ["+currentNode.getParent().getData()+"] its right subtree.");
					rotateNode(currentNode);	//rotate this node
				}
			
				anim.waitOnPause();
			} 
			//if the balance difference of this node is 2 then a left or right-left rotation needs to be performed
			else if (currentNode.balanceDifference() == 2) 
			{
				getNote().setNote("Balancing required at node ["+currentNode.getData()+
						"] since it has a balance difference of 2.");
				anim.waitOnPause();
				
				//if the current nodes balance difference is 1 then perform a left rotation
				if (currentNode.getRight().balanceDifference() != -1) 
				{
					getNote().setNote("Performing left rotation at node ["+currentNode.getRight().getData()+
							"] by making node ["+currentNode.getRight().getParent().getData()+"] its left subtree.");
					currentNode.bgColor(root.getPresetColor());
					currentNode = currentNode.getRight();
					currentNode.bgColor(Color.PINK);
					rotateNode(currentNode);	//rotate this node
				} 
				else //otherwise perform a right-left rotation
				{
					currentNode.bgColor(root.getPresetColor());
					currentNode = currentNode.getRight().getLeft();
					currentNode.bgColor(Color.PINK);
					getNote().setNote("Performing a right-left rotation at node ["+currentNode.getData()+"].");
					anim.waitOnPause();
					
					getNote().setNote("Making node ["+currentNode.getParent().getData()+
							"] the right subtree of node ["+currentNode.getData()+"].");
					rotateNode(currentNode);	//rotate this node
					anim.waitOnPause();
					
					getNote().setNote("Performing left rotation at node ["+currentNode.getData()+
							"] by making node ["+currentNode.getParent().getData()+"] its left subtree.");
					rotateNode(currentNode);	//rotate this node
				}
				anim.waitOnPause();
			}
			
			//set the nodes color to its normal aqua color
			currentNode.bgColor(new Color(0x40, 0xFF, 0xFF));
			
			//set the current node's parent as the current node
			currentNode = currentNode.getParent();	
		}
	}
	
	/**
	 * This method is used to reposition the tree if it is not empty.
	 */
	private void repositionTheTree()
	{
		//if the root is not null then re-balance the tree.
		if (this.root != null)
			this.root.repositionTree();
	}
}
