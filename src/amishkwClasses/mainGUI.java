package amishkwClasses;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class mainGUI extends JFrame

{
	//CONSTANTS
	String documentsDir = "/Users/samuelmarticotte/Desktop/4_Education/0_PhD/0_DIC/0_Cours_actuels/Hiver_2018/Psycholinguistique_et_TAL/3_TP1/0_Collection_de_documents/0_documents_a_indexer_sophisticated";
	String shortQueriesFile = "/Users/samuelmarticotte/Desktop/4_Education/0_PhD/0_DIC/0_Cours_actuels/Hiver_2018/Psycholinguistique_et_TAL/3_TP1/1_Requetes/2_requetesCourtes.txt";
	String longQueriesFile = "/Users/samuelmarticotte/Desktop/4_Education/0_PhD/0_DIC/0_Cours_actuels/Hiver_2018/Psycholinguistique_et_TAL/3_TP1/1_Requetes/1_requetesLongues.txt";
	String frenchQueriesFile = "/Users/samuelmarticotte/Desktop/4_Education/0_PhD/0_DIC/0_Cours_actuels/Hiver_2018/Psycholinguistique_et_TAL/3_TP1/1_Requetes/4_requete_fr.1-50_optional_translingue.txt";
	String jugementPert= "/Users/samuelmarticotte/Desktop/4_Education/0_PhD/0_DIC/0_Cours_actuels/Hiver_2018/Psycholinguistique_et_TAL/3_TP1/0_Collection_de_documents/2_qrels";
	String topicsFile = "/Users/samuelmarticotte/Desktop/4_Education/0_PhD/0_DIC/0_Cours_actuels/Hiver_2018/Psycholinguistique_et_TAL/3_TP1/0_Collection_de_documents/topics.1-50.top";
	String outputFile = "/Users/samuelmarticotte/Desktop/4_Education/0_PhD/0_DIC/0_Cours_actuels/Hiver_2018/Psycholinguistique_et_TAL/3_TP1/0_Collection_de_documents/3_output.top";
	String debugFile= "/Users/samuelmarticotte/Desktop/4_Education/0_PhD/0_DIC/0_Cours_actuels/Hiver_2018/Psycholinguistique_et_TAL/3_TP1/0_Collection_de_documents/3_debug.txt";
	String docNameField = "docno";
	
    //Graphical components declaration
	
	//PANEL
    private final JPanel workerJPanel = new JPanel( new GridLayout( 2, 2, 6, 6 ) );
    
    //QUERY FIELD
    private final JTextField queryTextField = new JTextField();
    
    //QUERY BUTTON
    private final JButton queryJButton = new JButton( "Query");
    
    //OUTPUT HINT
    private final JLabel outputJLabel = new JLabel();
    
    //THREAD EVENT
    private final JPanel eventThreadJPanel = new JPanel( new GridLayout( 2, 2, 5, 5 ) );
   
    //OUTPUT RESULTS
    //private final JTextArea resultsArea = new JTextArea(5, 20);
    private final JTextArea resultsArea = new JTextArea(50, 25);
    
    //AREA SCROLLPANE
    //JScrollPane areaScrollPane = new JScrollPane(resultsArea);
    
    //JPANEL for results
    JPanel middlePanel = new JPanel();
    
    //JFRAME for results
    JFrame frame = new JFrame();    

    //constructor
    public mainGUI()
    {
    	//SET TITLE
        super( "Amishk IR" );
        
        //SET GENERAL LAYOUT
        setLayout( new GridLayout( 2, 1, 40, 40 ) );
        
        //ADD GUI COMPONENTS TO THREAD
        workerJPanel.setBorder( new TitledBorder( new LineBorder( Color.YELLOW ), "Welcome to Amishkw Information Research" ) );
        workerJPanel.add( new JLabel( "Enter your search terms:" ) );
        workerJPanel.add( queryTextField );
        workerJPanel.setSize(600,400);
        queryTextField.setText("Antitrust Cases Pending");
        
        //RESULTS PANEL
        resultsArea.setEditable(false);
        JScrollPane scroll = new JScrollPane( resultsArea);
        middlePanel.setBorder(new TitledBorder ( new EtchedBorder(), "Search Results"));
        middlePanel.add(scroll);
        
        frame.add( middlePanel);
        frame.pack();
        //frame.setLocationRelativeTo(workerJPanel);
        frame.setVisible(true);
        
        
        //QUERY BUTTON EVENTS
        queryJButton.addActionListener(
            new ActionListener()
        {
            public void actionPerformed( ActionEvent event )
                {
                    String query;
                    try
                    {
                    	resultsArea.setText("");
                    	
                		//CREATE DEBUG FILE
                		Debug debug = new Debug(debugFile); // this is a debug file 
                		System.out.println("\nCreated debug file : " + debugFile.toString());
                		
                		//CREATE OUTPUT FILE
                		File file = new File(outputFile);
                		PrintWriter results = new PrintWriter(file);
                		
                		query = queryTextField.getText();
                		
                        //indicate that the calculation has begun
                        outputJLabel.setText( "Parsing queries..." );
                        
                        
                        
                        //SEARCH ONE QUERY
                        //new Indexing(debug).IndexSophisticated(documentsDir, debug, "BM25", new AnalyzerSophisticated());
                        new Search().oneQuery(documentsDir, query, results, debug, "BM25", new AnalyzerSophisticated(), outputJLabel, resultsArea);
                        
                		debug.closeDebugFile();
                		results.close();
                		
                		//CLOSE DEBUG
                		System.out.print("Closed debug file");
                    
                    }//end try
                    catch( NumberFormatException ex )
                    {
                        //display an error message if the user did not enter an integer
                        outputJLabel.setText( "Invalid search terms. Please retry with valid terms" );
                        return;
                    }//end catch
                    catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
                    }
                    //create a task to perform calculation in background
                    //BackgroundCalculator task = new BackgroundCalculator( n, outputJLabel);
                    //task.execute(); //execute task
 catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (org.apache.lucene.queryparser.classic.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                
                }//end action performed
            
            }//end Action Listener
        );//end addActionListener
    
        workerJPanel.add( queryJButton );
        workerJPanel.add( outputJLabel );
       
        add( workerJPanel );
        add( eventThreadJPanel );
        setSize( 500, 100 );
        setVisible( true );
    }//end constructor FibonacciNumbers

	public static void main ( String args[] ) throws FileNotFoundException, UnsupportedEncodingException
	{
		//WELCOME
		System.out.println("Welcome to the Amishkw IR System"); 		
		linePrint();
        
        FibonacciNumber application = new FibonacciNumber();
        application.setDefaultCloseOperation( EXIT_ON_CLOSE );
        
        System.out.printf("\n**************GOOD BYE**************\n");
	}//end main
	
	public static void linePrint(){
		System.out.println("================================");
	}//end method linePrint

}// end class FibonacciNumber
