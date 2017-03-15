/**
 * This class extends the thread in the animation class.
 * This thread is started from the Tree class which then runs the 
 * run method in this class.
 * The run method inserts a new node into the current tree by checking first if the
 * node already exists in the tree or not and then adds it to its correct location.
 * After the node is inserted, the tree then checks if it needs any balancing.
 * 
 * @author Abin Daniel
 * @title MSc Honors Project
 */
public class InsertNode extends Animation {
	
	/**Instance Variables**/
	private Tree tree;
	private Node node;
	private String value;
	
	/**Constructor**/
	public InsertNode(Tree tree, String value)
	{
		//calls the constructor, methods and properties of its parent class
		super(tree.getMainFrame()); 
		this.mf = tree.getMainFrame();
		this.tree = tree;
		this.value = value;
		
		//creating a new 'Search' node
		this.node = new Node(value, tree); 
		this.node.bgColor(node.getInsertColor());
		tree.setNode(this.node);
	}
	
	/**
	 * This method is used to go through the tree and insert a new node.
	 */
	public void run() 
	{
		Node currentNode = this.tree.getRoot();
		
		//if the tree is empty and set the new node as the root node
		if (this.tree.getRoot() == null) 
		{
			this.tree.setRoot(this.node = new Node(this.node));
			this.node.advanceToTheRoot();
			tree.getNote().setNote("Inserting a new root node [" + node.getData()+"].");
			waitOnPause();
		} 
		else 
		{
			//otherwise go above the node and start to search
			this.node.advanceToAboveTheRoot();
			tree.getNote().setNote("Starting to insert node ["+this.value+"].");
			waitOnPause();
			
			while (true) 
			{
				int result = 0;
				boolean exit = false;
				
				//if the new node and the node which is being search are both numbers then 
				//..convert their values into numbers and compare them.
				if(tree.isNumeric(currentNode.getData()) && tree.isNumeric(this.value))
				{
					int current = Integer.parseInt(currentNode.getData());
					int thisValue = Integer.parseInt(this.value);
					
					if (current == thisValue)
						result = 1;
					
					// if the new node comes before the current node, go left
					if (thisValue < current) 
						result = 2;
					else if (thisValue > current) 
						result = 3;
				}
				else
				{
					//else the node which is being searched is a number so compare
					//..them both as words.
					if (currentNode.getData().compareTo(this.value) == 0) 
						result = 1;
					
					// if the new node comes before the current node, go left
					if (this.value.compareTo(currentNode.getData()) < 0) 
						result = 2;
					else if (this.value.compareTo(currentNode.getData()) > 0)
						result = 3;
				}
				
				switch(result)
				{
					case 1:
						//if the node already exists in the tree then remove the 'Search' node
						tree.getNote().setNote("Node ["+this.value+"] already exists in the tree.");
						if(!mf.getOpenF())
							this.node.bgColor(node.getDeleteColor());
						else
							this.node.setColor(node.getInvisibleColor(), node.getInvisibleColor());
						waitOnPause();
						this.node.goDown();
						tree.getNote().setNote("");
						waitOnPause();
						return;
						
					case 2:
						//if the new node is less than the node which is being searched then go to its left
						//...child. If the left child is empty then set the new node as the left child and
						//...connect them both.
						tree.getNote().setNote("Checking left side since node [" + this.value +
								"] is less than node ["+ currentNode.getData()+"].");
						if (currentNode.getLeft() != null)
						{
							currentNode = currentNode.getLeft();
							break;
						}
						else 
						{
							currentNode.connectNode(this.node = new Node(this.node),"left");
							tree.getNote().setNote("Node ["+this.value+"] inserted since node ["+currentNode.getData()+
									"]'s left child is empty.");
							exit = true;
							break;
						}
						
					case 3:
						//if the new node is greater than the node which is being searched then go to its right
						//...child. If the right child is empty then set the new node as the right child and
						//...connect them both.
						tree.getNote().setNote("Going to right side since node [" + this.value +
								"] is greater than node ["+ currentNode.getData()+"].");
						
						if (currentNode.getRight() != null)
						{
							currentNode = currentNode.getRight();
							break;
						}
						else 
						{
							// create a new node
							this.node = new Node(this.node);
							currentNode.connectNode(this.node,"right");
							tree.getNote().setNote("Node ["+this.value+"] inserted since node ["+currentNode.getData()+
									"]'s right child is empty.");
							exit = true;
							break;
						}
						
					default:
						break;	
				}
				
				if(exit)
					break;
				
				//go to above the next node which is being searched.
				this.node.advanceToNode(currentNode);
				waitOnPause();	
			}
			
			this.node = (this.tree.node = null);
			
			//if the tree is not empty then reposition it.
			if(this.tree.getRoot() != null)
					this.tree.getRoot().repositionTree();
			
			waitOnPause();
			
			//check if the tree is balanced.
			this.tree.reBalanceNode(currentNode);
		}
		tree.getNote().setNote("Insertion Complete.");
		tree.getMainFrame().getStack().push("i "+this.value); //add the operation to the stack
	}
}
