import java.awt.Color;

/**
 * The following class is used to range search the tree. The user first
 * provides the ranges to be searched within the main window panel. These
 * two values are then used to compare against the current tree and highlights
 * the nodes in green if that node lies within the current range.
 * 
 * @author Abin Daniel
 * @title MSc Honors Project
 *
 */
public class RangeSearch extends Animation {

	private Tree tree;
	private String s1, s2;
	private Node node;
	
	/**Constructor**/
	public RangeSearch(Tree tree, String s1, String s2) 
	{
		super(tree.getMainFrame());
		this.tree = tree;
		this.s1 = s1;
		this.s2 = s2;
		this.tree.emptyTempArray();
		
		this.node = new Node("",tree);
		tree.setNode(this.node);
		this.node.bgColor(node.getFindColor());
	}
	
	/**
	 * This run method is called when the thread is started from the tree class.
	 * It will then play the animation by going through the current tree and highlighting
	 * the nodes in the range.
	 */
	public void run()
	{
		rangeSearch(tree.getRoot(),s1,s2);
		this.node.setColor(node.getInvisibleColor(),node.getInvisibleColor());
		this.node.goDown();
		
		if(tree.tempListSize() == 0)
		{
			tree.getNote().setNote("Range searching complete.\n                           "+
					"There are no nodes which lie within the range");
		}
		else
			tree.getNote().setNote("Range searching complete.\n                           "+
				"The nodes in range are nodes "+printRangeSearch()+".\n");
	}
	
	/**
	 * This method is used to range search the current tree by first checking its root
	 * and then checking its left and right child recursively.
	 * @param r Node
	 * @param k1 String
	 * @param k2 String
	 */
	private void rangeSearch(Node r, String k1, String k2)
	{	
	   int rInt = 0, k1Int = 0, k2Int = 0;
	   
	   //if the current node is empty then exit
	   if ( r == null )
	      return;
	   
	   //set the finding nodes color to orange
	   this.node.bgColor(node.getFindColor());	
	   this.node.setData(r.getData());
	   this.node.advanceTo(r);
	   tree.getNote().setNote("Checking if node ["+r.getData()+
			   "] lies between ["+k1+"] and ["+k2+"]...");
	   waitOnPause();
	   
	   //if the searched node is not within the range then
	   //..change its color to gray.
	   r.bgColor(Color.GRAY);
		   
	 
	   /**CHECKING IF THE NODE LIES WITHIN THE RANGE**/
	   /**
	    * The following if statement checks if the current node is a number
	    * so it can be compared as an number otherwise it will be compared as 
	    * strings.
	    */
	   if(tree.isNumeric(r.getData()) && tree.isNumeric(k1) && tree.isNumeric(k2))
	   {
		   rInt = Integer.parseInt(r.getData());
		   k1Int = Integer.parseInt(k1);
		   k2Int = Integer.parseInt(k2);
		   
		   if(k1Int <= rInt && k2Int >= rInt)
			   checkNode(this.node, r, this.tree);
	   }
	   else
	   {
		   if(tree.isNumeric(r.getData()) && tree.isNumeric(k1))
		   {
			   rInt = Integer.parseInt(r.getData());
			   k1Int = Integer.parseInt(k1);
			   
			   if(k1Int <= rInt && k2.compareTo(r.getData()) >= 0)
				   checkNode(this.node, r, this.tree);
		   }
			else if(tree.isNumeric(r.getData()) && tree.isNumeric(k2))
			{
				rInt = Integer.parseInt(r.getData());
				k2Int = Integer.parseInt(k2);
				
				if(k1.compareTo(r.getData()) <= 0 && k2Int >= rInt)
					checkNode(this.node, r, this.tree);
			}
			else
			{
				if (k1.compareTo(r.getData()) <= 0 && k2.compareTo(r.getData()) >= 0)
					checkNode(this.node, r, this.tree);
			}
	   }
	   
	   
	   //Check if both the current nodes value and the first input string can
	   //..be converted to numbers otherwise compare them as strings.
	   if(tree.isNumeric(r.getData()) && tree.isNumeric(k1))
	   {
		   rInt = Integer.parseInt(r.getData());
		   k1Int = Integer.parseInt(k1);

		   if(k1Int < rInt)
			   rangeSearch(r.getLeft(), k1, k2);
	   }
	   else
	   {
		   if (k1.compareTo(r.getData()) < 0)
			   rangeSearch(r.getLeft(), k1, k2);
	   }
	   
	   //Check if both the current nodes value and the second input string can
	   //..be converted to numbers otherwise compare them as strings.
		if(tree.isNumeric(r.getData()) && tree.isNumeric(k2))
		{
			rInt = Integer.parseInt(r.getData());
			k2Int = Integer.parseInt(k2);
			
			if(k2Int > rInt)
				rangeSearch(r.getRight(), k1, k2);
		}
		else
		{
			if (k2.compareTo(r.getData()) > 0)
				rangeSearch(r.getRight(), k1, k2);
		}
	}
	
	/**
	 * The following method highlights the input node to green color and adds 
	 * it to the temporary array.
	 * @param n Node
	 * @param r Node
	 * @param tree Tree
	 */
	public void checkNode(Node n, Node r, Tree tree)
	{
  		 n.setData(r.getData());	//sets the finding nodes value to the current node
		 n.bgColor(node.getFoundColor());
	     r.bgColor(node.getFoundColor());
	     tree.getNote().setNote("Node ["+r.getData()+"] lies within the range.");
	     tree.getTemp().add(r.getData());
	     waitOnPause();
	}
	
	/**
	 * This method prints all the nodes which lie within the range and returns
	 * it as a string.
	 * @return text String
	 */
	public String printRangeSearch()
	{
		String text = "";
		for(int i=0; i<tree.tempListSize(); i++)
			text += "["+tree.getTemp().get(i)+"] ";
		
		return text;
	}
	
	/**Accessor Methods**/
	public String getRS_String1() { return s1; }
	public String getRS_String2() { return s2; }

}
