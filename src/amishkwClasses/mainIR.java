package amishkwClasses;

import java.io.File;
import java.io.PrintWriter;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.benchmark.quality.QualityQuery;
import org.apache.lucene.benchmark.quality.QualityQueryParser;
import org.apache.lucene.search.IndexSearcher;


public class mainIR {

	@SuppressWarnings("restriction")
	public static void main(String[] args) throws Throwable {

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
		
		//WELCOME
		System.out.println("Welcome to the Amishkw IR System"); 		
		linePrint();
		
		//CREATE DEBUG FILE
		Debug debug = new Debug(debugFile); // this is a debug file 
		System.out.println("Created debug file : " + debugFile.toString());

		
		//CREATE OUTPUT FILE
		File file = new File(outputFile);
		PrintWriter results = new PrintWriter(file);
		
	//PART ONE:
		
		//INDEX BASELINE TFIDF
		
				//EXPERIMENT 1: 
				//new Indexing(debug).IndexBaseline(documentsDir, debug, "TFIDF", new StandardAnalyzer());
				//new Search().shortQuery(documentsDir, shortQueriesFile, results, debug, "TFIDF");
		
				//EXPERIMENT 2
				//new Indexing(debug).IndexBaseline(documentsDir, debug, "TFIDF", new StandardAnalyzer());
				//new Search().longQuery(documentsDir, longQueriesFile, results, debug, "TFIDF");
		
		//INDEX BASELINE BM25
	
				//EXPERIMENT 3
				//new Indexing(debug).IndexBaseline(documentsDir, debug, "BM25");
				//new Search().shortQuery(documentsDir, shortQueriesFile, results, debug, "BM25");
		
				//EXPERIMENT 4
				//new Indexing(debug).IndexBaseline(documentsDir, debug, "BM25");
				//new Search().longQuery(documentsDir, longQueriesFile, results, debug, "BM25");
		
		//SOPHISTICATED + TFIDF
		
				//EXPERIMENT 5
				//new Indexing(debug).IndexSophisticated(documentsDir, debug, "TFIDF", new AnalyzerSophisticated());
				//new Search().shortQuery(documentsDir, shortQueriesFile, results, debug, "TFIDF", new AnalyzerSophisticated());
		
				//EXPERIMENT 6
				//new Indexing(debug).IndexSophisticated(documentsDir, debug, "TFIDF", new AnalyzerSophisticated());
				//new Search().longQuery(documentsDir, longQueriesFile, results, debug, "TFIDF", new AnalyzerSophisticated());
		
		//SOPHISTICATED + BM25
		
				//EXPERIMENT 7
				//new Search().shortQuery(documentsDir, shortQueriesFile, results, debug, "BM25", new AnalyzerSophisticated());
				//new Indexing(debug).IndexSophisticated(documentsDir, debug, "BM25", new AnalyzerSophisticated());
		
				//EXPERIMENT 8
				//new Search().longQuery(documentsDir, longQueriesFile, results, debug, "BM25", new AnalyzerSophisticated());
				//new Indexing(debug).IndexSophisticated(documentsDir, debug, "BM25", new AnalyzerSophisticated());
	//PART TWO
			
		//QUERY EXPANSION
		//new QueryExpansion().longQueryExpansion(documentsDir, longQueriesFile, results, debug, "BM25", new EnglishAnalyzer());
		
		//QUERY TRANSLATION	
		
				//EXPERIMENT 9 
				//new Indexing(debug).IndexBaseline(documentsDir, debug, "TFIDF", new StandardAnalyzer());
				//new QueryTranslation().shortQueryTranslation(documentsDir, frenchQueriesFile, results, debug, "TFIDF", new StandardAnalyzer());
				
				//EXPERIMENT 10
				//new Indexing(debug).IndexSophisticated(documentsDir, debug, "TFIDF", new AnalyzerSophisticated());
				//new QueryTranslation().shortQueryTranslation(documentsDir, frenchQueriesFile, results, debug, "TFIDF", new AnalyzerSophisticated());
				
				//EXPERIMENT 11
				//new Indexing(debug).IndexBaseline(documentsDir, debug, "BM25", new StandardAnalyzer());
				//new QueryTranslation().shortQueryTranslation(documentsDir, frenchQueriesFile, results, debug, "BM25", new StandardAnalyzer());
				
				//EXPERIMENT12
				new Indexing(debug).IndexSophisticated(documentsDir, debug, "BM25", new AnalyzerSophisticated());
				//new QueryTranslation().shortQueryTranslation(documentsDir, frenchQueriesFile, results, debug, "BM25", new AnalyzerSophisticated());
				
		//Close debug
		debug.closeDebugFile();
		System.out.print("Closed debug file");
		
	}//end main IR
	
	public static void linePrint(){
		System.out.println("================================");
	}//end method linePrint
}
