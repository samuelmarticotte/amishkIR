package amishkwClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Debug {
	
	PrintWriter writer; 
	
		Debug(String filename) throws FileNotFoundException, UnsupportedEncodingException {
			try {
			File file = new File(filename);
			writer = new PrintWriter(file, "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}//end Debug constructor
		
		//Append debug text
		public void append(String txt)
		{	writer.println(txt);
		
		}//end append method
		
		public void closeDebugFile(){
			writer.close();
		}//end closeDebug
	
	

}//end class Debug
