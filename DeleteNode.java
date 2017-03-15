/**
 * This class extends the thread in the animation class.
 * This thread is started from the Tree class which then runs the 
 * run method in this class.
 * The run method then deletes a node from the current tree by looking at the 
 * root of the tree first and then working its way down to find the node which the
 * user wishes to delete.
 * After a node is deleted, the tree then checks if it needs balancing.
 * 
 * @author Abin Daniel
 * @title MSc Honors Project
 */
public class DeleteNode extends Animation {

	/**Instance Variables**/
	private Tree tree;
	private Node node;
	private String value;

	
	/**Constructor**/
	public DeleteNode(Tree tree, String value)
	{
		//calls the constructor, methods and properties of its parent class
		super(tree.getMainFrame());
		this.tree = tree;
		this.value = value;
		
		//create a new 'Search' node
		this.node = new Node(value,tree);
		tree.setNode(this.node);
		this.node.bgColor(node.getFindColor());
	}

	/**
	 * This method is called from the tree class and checks each node in the tree
	 * and deletes it when it finds that node.
	 */
	public void run()
	{
		//if the tree is empty then make the 'Search' node go down and end the searching process.
		if(this.tree.getRoot() == null)
		{
			this.node.advanceToAboveTheRoot();
			tree.getNote().setNote("The tree is empty.");
			waitOnPause();
			this.node.goDown();
			this.node.bgColor(node.getDeleteColor());
			tree.getNote().setNote("Not found!");
			return;
		}
		else
		{
			//otherwise go to the top of the root node
			Node n = this.tree.getRoot();
			this.node.advanceToNode(n);	//sets the 'Search' node above the current node 'n'
			tree.getNote().setNote("Finding node ["+this.value+"]...");
			waitOnPause();

			Node parent = tree.getRoot();	//set the parent as the root of the tree
			Node delNode = null;			//set the delete node to null
			Node child = tree.getRoot();	//set the child as the root of the tree
			
			boolean exit = false;

			//if the child is not empty then enter this loop
			while(child != null)
			{
				parent = n;	//set the root as the parent
				n = child;	//set the Node 'n' as the child
				
				boolean lessThan = false;
				boolean moreThan = false;
				boolean equals = false;

				// check if the new number is already in the tree
				if(tree.isNumeric(this.value) && tree.isNumeric(n.getData()))
				{
					int thisValue = Integer.parseInt(this.value);
					int nValue = Integer.parseInt(n.getData());

					if(thisValue >= nValue)
						moreThan = true;
					else if(thisValue <= nValue)
						lessThan = true;

					if(thisValue == nValue)
						equals = true;
				}
				else
				{
					//compare as strings
					if(this.value.compareTo(n.getData()) >= 0)
						moreThan = true;
					else if(this.value.compareTo(n.getData()) <= 0)
						lessThan = true;

					if(this.value.compareTo(n.getData()) == 0)
						equals = true;
				}
				
				if(lessThan)
				{
					child = n.getLeft();	//set the 'child' node to the left child of node 'n'

					if(!exit)
					{
						tree.getNote().setNote("Searching left since node ["+this.value+
								"] is less than node ["+n.getData()+"].");
						this.node.advanceToNode(n);
					}
				}
				
				if(moreThan)
				{
					child = n.getRight();	//set the 'child' node to the right child of node 'n'
					
					//if the 'child' node is null then set the child to the left node of node 'n'
					if(child == null)
						child = n.getLeft();	
					
					if(!exit)
					{
						tree.getNote().setNote("Searching right since node ["+this.value+
								"] is greater than node ["+n.getData()+"].");
						this.node.advanceToNode(n);
					}
				}
				
				//enter this condition if the node to be delete has been found
				if(equals)
				{
					delNode = n;	//set node 'n' as the delete node
					tree.getNote().setNote("Found node.");
					waitOnPause();
					
					this.node.advanceTo(n); //make the 'Search' node go to this node to be deleted
					tree.getNote().setNote("Deleting node.");
					this.node.bgColor(node.getDeleteColor());	//change the color of the 'Search' node to red
					exit = true;
					waitOnPause();
				}

				if(!exit)
					waitOnPause();
			}

			//if the delete node is empty then exit the while loop and remove the 'Search' node
			if(delNode == null)
			{
				this.node.goDown();
				tree.getNote().setNote("Node not found.");
				return;
			}

			//otherwise enter this condition
			if(delNode != null)
			{
				delNode.setData(n.getData());	//sets delete node to node 'n'

				//if Node 'n's left child is not empty then set Node 'child' to 
				//..node 'n's left child, vice versa
				if(n.getLeft() != null)
					child = n.getLeft();
				else
					child = n.getRight();

				//if the root node is equal to the delete node value then set the root as the child node
				if(tree.getRoot().getData().compareTo(this.value) == 0)
					this.tree.setRoot(child);
				else
				{
					if(parent.getLeft() == n)
						parent.setLeft(child);
					else
						parent.setRight(child);
				}
			}

			this.node.goDown();
			
			//reposition the tree if the tree is not empty.
			if(this.tree.getRoot() != null)
				this.tree.getRoot().repositionTree();
			
			tree.getNote().setNote("Node deleted.");
			waitOnPause();

			//re-balance the tree
			this.tree.reBalanceNode(parent);
		}
		tree.getNote().setNote("Deletion complete.");
		tree.getMainFrame().getStack().push("d "+this.value);	//add this operation onto the stack
	}
}
