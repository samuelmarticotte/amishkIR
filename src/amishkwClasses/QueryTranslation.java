package amishkwClasses;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;

//GOOGLE TRANSLATION
//Imports the Google Cloud client library
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

public class QueryTranslation {
	
	
	public void shortQueryTranslation(String indexDir, String queriesFile, PrintWriter outputWriter, Debug debug, String similarity, Analyzer analyzer) throws IOException {
		
		//Welcome to shortQueriesTranslation
		System.out.println("Set requests to FRENCH SHORT QUERIES");
		
		//Create IndexReader and Searcher
		IndexReader reader = DirectoryReader.open(FSDirectory.open( new File(indexDir).toPath()));		//Creating an index reader
		IndexSearcher searcher = new IndexSearcher(reader);

		//Set similarity measure
		searcher = new SetSimilarity().SetSimilaritySearcher(searcher, similarity);
		
		
		//Read queries from file
		String[] queries = readQueriesFile(queriesFile);
		
		for (int i=0; i < queries.length; i++ ){ //for all queries
			
			try {			
			//Parse Query		
			QueryParser parser = new QueryParser("contents", analyzer);		
			
			String newQuery = queries[i].substring(4, queries[i].length());	//Strip numbers from title
			
			//TRANSLATE QUERY INTO FRENCH USING GOOGLE API
			
			System.out.println("Original query = " + newQuery);
			String frenchQuery = translateQueryToFrench(newQuery);
			
			
			Query query = parser.parse(frenchQuery);	
			System.out.println("Query " + queries[i].substring(0, 4) + " = " + query );
			debug.append("Query " + queries[i].substring(0, 4) + " = " + query );
			
			//Search
			TopScoreDocCollector collector = TopScoreDocCollector.create(1000);
		
			//Create a TopScore Collector
			searcher.search(query, collector);	
			
			//Print results
			//displayResults(searcher, collector);
			
			String num = String.format("%d", i + 1); //Query number for short queries: to pass to function outputResults
			//Output results to file
			outputResults(searcher, collector, outputWriter, num);
			
			} catch (Exception e) {
				System.out.println("Parse exception in query #" + i);
				e.printStackTrace();
			}//end catch
			
		}//end for
		
		reader.close();	
		
	}//end method shortQueryTranslation
	
//This functions calls google API taking in French query and returns a Query in English. 
public static String translateQueryToFrench(String frenchQuery){

	String englishQuery;
	
	//Set API key 
	//Translate translate = TranslateOptions.newBuilder().setApiKey("cc8b4383b39ce6cadc82158c81226098e74727cf").build().getService();
	
	//Instantiate a client
	Translate translate = TranslateOptions.getDefaultInstance().newBuilder().setApiKey("AIzaSyCn_iwH9HQdfWxphFVg4nQE325evHhBLnE").build().getService();
	
	//Translate Text
	Translation  translation = translate.translate(frenchQuery,  TranslateOption.sourceLanguage("fr"), TranslateOption.targetLanguage("en"));
	englishQuery = translation.getTranslatedText();
	System.out.println("Translated query = " + englishQuery);
	return englishQuery; //return query in French
	
}//end 



//This function reads queries from file one by one and calls shortQuery to search documents
public static String[] readQueriesFile(String queriesFile) throws IOException {
	
	List<String> list = Files.readAllLines(Paths.get(queriesFile), StandardCharsets.UTF_8);
	String[] a = list.toArray(new String[list.size()]); 
	return a;
	
}//end readQueriesfile

//This function display the results of the search showing top documents
//Args: IndexSearcher, TopScoreDocCollector
public static void outputResults(IndexSearcher search, TopScoreDocCollector collect, PrintWriter outputFile, String queryId) throws IOException {
	//Display results 
	ScoreDoc[] hits = collect.topDocs().scoreDocs; //001 	Q0	AP890605-0050	1	STANDARD
	
	for (int i=0; i < hits.length; i++){
		int docId = hits[i].doc;
		Document d = search.doc(docId);
		String qId = queryId;
		String iter = "Q0";
		String docno = d.get("docno");
		String rank = Integer.toString(i+1);
		String sim = Float.toString((hits[i].score));
		//String similarity = Float.toString(hits[i].score); 
		//String sim = similarity.substring(0, Math.min(similarity.length(), 9));
		
		String results = String.format("%s\t%s\t%s\t%s\t%s\tSTANDARD" + System.lineSeparator(), qId, iter, docno, rank, sim);
		outputFile.append(results);
	}//end for
}

}//end class QueryTranslation

