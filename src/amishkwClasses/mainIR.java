package amishkwClasses;

import java.io.PrintWriter;

import org.apache.lucene.benchmark.quality.QualityQuery;
import org.apache.lucene.benchmark.quality.QualityQueryParser;
import org.apache.lucene.search.IndexSearcher;

public class mainIR {

	public static void main(String[] args) throws Throwable {
		// TODO Auto-generated method stub
		// cd /Users/samuelmarticotte/Desktop/4_Education/0_PhD/0_DIC/0_Cours_actuels/Hiver_2018/Psycholinguistique_et_TAL/3_TP1/0_Collection_de_documents/0_documents_a_indexer; 
		//find  -type f ! -name 'AP*' -delete	
		//!!!!CAREFULLL IT DELETES ALL LOCAL DIR FILES!!!
		
		//WELCOME
		System.out.println("Welcome to the Amishkw IR System"); 
		//PrintWriter writer = new PrintWriter("/Users/samuelmarticotte/Desktop/4_Education/0_PhD/0_DIC/0_Cours_actuels/Hiver_2018/Psycholinguistique_et_TAL/3_TP1/0_Collection_de_documents/0_documents_a_indexer/Resultats.txt", "UTF-8");
		//writer.println("Welcome to the Amishkw IR System");
		//writer.close(); //close results file 
		linePrint();
		
		//CONSTANTS
		String documentsDirBase = "/Users/samuelmarticotte/Desktop/4_Education/0_PhD/0_DIC/0_Cours_actuels/Hiver_2018/Psycholinguistique_et_TAL/3_TP1/0_Collection_de_documents/0_documents_a_indexer_baseline";
		String documentsDirSoph = "/Users/samuelmarticotte/Desktop/4_Education/0_PhD/0_DIC/0_Cours_actuels/Hiver_2018/Psycholinguistique_et_TAL/3_TP1/0_Collection_de_documents/0_documents_a_indexer_sophisticated";
		String shortQueriesFile = "/Users/samuelmarticotte/Desktop/4_Education/0_PhD/0_DIC/0_Cours_actuels/Hiver_2018/Psycholinguistique_et_TAL/3_TP1/1_Requetes/2_requetesCourtes.txt";
		String longQueriesFile = "/Users/samuelmarticotte/Desktop/4_Education/0_PhD/0_DIC/0_Cours_actuels/Hiver_2018/Psycholinguistique_et_TAL/3_TP1/1_Requetes/1_requetesLongues.txt";
		String jugementPert= "/Users/samuelmarticotte/Desktop/4_Education/0_PhD/0_DIC/0_Cours_actuels/Hiver_2018/Psycholinguistique_et_TAL/3_TP1/0_Collection_de_documents/2_jugement_pertinence_de_requetes_1-50.txt";
		String topicsFile = "/Users/samuelmarticotte/Desktop/4_Education/0_PhD/0_DIC/0_Cours_actuels/Hiver_2018/Psycholinguistique_et_TAL/3_TP1/0_Collection_de_documents/3_requetes_1-50.txt";
		String outputFile = "/Users/samuelmarticotte/Desktop/4_Education/0_PhD/0_DIC/0_Cours_actuels/Hiver_2018/Psycholinguistique_et_TAL/3_TP1/0_Collection_de_documents/output.txt";
		String docNameField = "docno";
		
		//PART ONE: Indexing Baseline and short queries
		//System.out.println("PART ONE : Indexing Baseline System with short queries and tf-idf");
		
		//1.0 Index documents with a baseline method
		
		//new Indexing().IndexBaseline(documentsDir);
		
		//1.1 Build short query and search
		//System.out.println("PART ONE : Build short query with tf-idf and search ");
		//new Search().shortQuery(documentsDirBase, shortQueriesFile);
		

		//1.2 Evaluate the results with TrecEval
		//System.out.println("PART ONE : Evaluate with TrecEval (not implemented)");

		//linePrint();
		//System.out.println("PART ONE SUCCESSFULLY COMPLETED");
		//linePrint();
		//pressAnyKeyToContinue(); //Press key to continue to PART TWO
		//--------------------------------------------------------------------------------------------- (END PART ONE)
		
		//PART TWO: Indexing with stop filter and porter algorithm
		//search similarity with Okapi BM25 and long queries
		
		//2.0 Index documents with 
		//new Indexing().IndexSophisticated(documentsDirSoph);
	
		//new Search().longQuery(documentsDirSoph, longQueriesFile);
		//2.1 Build long query and search
		
		//2.3 Evaluate the results with TrecEval
		//new Evaluation().TrecEval(jugementPert, topicsFile, documentsDirSoph, "docno");
		new QueryDriver().QueryEval(topicsFile, jugementPert, outputFile, documentsDirSoph, docNameField); 
		
	}//end main IR
	
	public static void linePrint(){
		System.out.println("================================");
	}//end method linePrint
	
	private static void pressAnyKeyToContinue()
	 { 
	        System.out.println("Press Enter key to continue...");
	        try
	        {
	            System.in.read();
	        }  
	        catch(Exception e)
	        {}  
	 }
	
	

}
