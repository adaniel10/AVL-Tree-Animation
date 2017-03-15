/**
 * This class runs the main thread whenever an operation is executed such as insertion,
 * deletion and range search.
 * 
 * @author Abin Daniel
 * @title MSc Honors Project
 */
public class MainThread extends Thread 
{
	private String data, end;
	private int id;
	private MainFrame mf;

	/**
	 * Constructor
	 * @param mf MainFrame
	 * @param id int
	 * @param data String
	 * @param end String
	 */
	public MainThread(MainFrame mf,int id, String data, String end) 
	{
		this.data = data;
		this.end = end;
		this.mf = mf;
		this.id = id;
	}

	/**
	 * This run method is called whenever an operation is performed. 
	 */
	public void run() {
		mf.performOperation(id,data,end);
	}
	
	/**
	 * Accessor Methods
	 */
	public String getNums() { return data; }
	public int getID() { return id; }
}
	


