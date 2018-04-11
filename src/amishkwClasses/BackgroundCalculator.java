
/*
package amishkwClasses;

import javax.swing.JFrame;
import javax.swing.SwingWorker;
import javax.swing.JLabel;
import java.util.concurrent.ExecutionException;


public class BackgroundCalculator extends SwingWorker< String, Object > {

    private final JLabel resultJLabel; //label to display the result
    
    //constructor
    public BackgroundCalculator( int number, JLabel label )
    {
        
    }//end constructor BackgroundCalculator
    
    //long-running code to be run in a worker thread
    public String doInBackground()
    {
    	
        
    }//end method doInBackground
    
    //code to run on the event dispatch thread when doInBackground returns
    protected void done()
    {
        try
        {
            //get result of doInBackground and display it
            resultJLabel.setText( get () );
            
        }//end try
        catch( InterruptedException exception )
        {
            resultJLabel.setText( "Interrupted while waiting for results. " );
        }//end catch1
        catch( ExecutionException ex )
        {
            resultJLabel.setText( "Error encountered while parsing Queries " );
        }//end catch2
    }//end method done
    

}// end class BackgroundCalculator
*/
