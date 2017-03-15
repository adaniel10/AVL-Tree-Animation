/**
 * This class is used to check if the user has selected the pause button or not.
 * If the pause button has been enabled then the thread will go to sleep
 * temporarily until the play button or the next button is pressed.
 * 
 * @author Abin Daniel
 * @title MSc Honors Project
 */
public class Animation extends Thread {

	public MainFrame mf;
	boolean hold = false;
	  
	  /**
	   * Constructor for the Animation class
	   * @param mf MainPanel		
	   */
	  public Animation(MainFrame mf) {
	    this.mf = mf;
	  }
	  
	  /**
	   * This method is used to suspend the thread
	   */
	  public void waitOnPause()
	  {
	    if (this.mf.isPause())
	    {
	      this.hold = true;
	      synchronized (this)
	      {
	        try
	        {
	          while (this.hold) {
	            wait();
	          }
	        }
	        catch (InterruptedException localInterruptedException) {}
	      }
	    }
	  }

	  /**
	   * This method is used to resume the thread
	   */
	  public void resumeThread()
	  {
	    synchronized (this)
	    {
	      this.hold = false;
	      notifyAll();
	    }
	  }
}
