package amishkwClasses;

//Swing
import javax.swing.JLabel;
import javax.swing.JTextArea;

//io
import java.io.File;

//Exceptions
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;


import org.apache.lucene.analysis.Analyzer;
//filters 
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
//Query
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.queryparser.classic.*;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.similarities.BM25Similarity;

//IndexReader
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;

public class Search {

public static void main(String[] args) throws Exception {
}

public void oneQuery(String indexDir, String querySent, PrintWriter outputWriter, Debug debug, String similarity, Analyzer analyzer, JLabel outputJLabel, JTextArea resultsArea) throws IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException {		
	
	//Welcome to shortQueries
	System.out.println("Set requests to ONE QUERY");
	
	//Create IndexReader and Searcher
	IndexReader reader = DirectoryReader.open(FSDirectory.open( new File(indexDir).toPath()));		//Creating an index reader
	IndexSearcher searcher = new IndexSearcher(reader);

	//Set similarity measure
	searcher = new SetSimilarity().SetSimilaritySearcher(searcher, similarity);

		try {			
		//Parse Query		
		QueryParser parser = new QueryParser("contents", analyzer);		
		
		String newQuery = querySent;
		Query query = parser.parse(newQuery);	
		System.out.println("Query = " + query );
		debug.append("Query = " + query );
		
		//Search
		TopScoreDocCollector collector = TopScoreDocCollector.create(1000);
	
		//Create a TopScore Collector
		searcher.search(query, collector);	
	
		//Output to GUI
		
		//Display results 
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		System.out.println("Found " + hits.length + " hits.");
		
		if (hits.length == 0) //No documents were found
		{
			resultsArea.append("Found " + hits.length + " hits.");
			outputJLabel.setText("No documents found");
		}
		System.out.println("--------------------------------");
		
		for (int i=0; i < hits.length; i++){
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			System.out.println((i+1) + ". " + d.get("docno") + " score=" + hits[i].score);
			resultsArea.append((i+1) + ". " + d.get("docno") + " score=" + hits[i].score + "\n");
			
			
		}//end for
		System.out.println("--------------------------------");
		
		outputJLabel.setText("  Top " + hits.length + " documents ranked.");
		
		} catch (Exception e) {
			System.out.println("Parse exception in query");
			e.printStackTrace();
		}//end catch
		
	reader.close();	
}//end oneQueryfunction

//This function creates an IndexSearcher
public void shortQuery(String indexDir, String queriesFile, PrintWriter outputWriter, Debug debug, String similarity, Analyzer analyzer) throws IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException {		
	
	//Welcome to shortQueries
	System.out.println("Set requests to SHORT QUERIES");
	
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
		
		String newQuery = queries[i].substring(3, queries[i].length());	//Strip numbers from title
		Query query = parser.parse(newQuery);	
		System.out.println("Query = " + query );
		debug.append("Query = " + query );
		
		//Search
		TopScoreDocCollector collector = TopScoreDocCollector.create(1000);
	
		//Create a TopScore Collector
		searcher.search(query, collector);	
		
		//Print results
		displayResults(searcher, collector);
		
		String num = String.format("%d", i + 1); //Query number for short queries: to pass to function outputResults
		//Output results to file
		outputResults(searcher, collector, outputWriter, num);
		
		} catch (Exception e) {
			System.out.println("Parse exception in query #" + i);
			e.printStackTrace();
		}//end catch
		
	}//end for
	
	reader.close();																						//close index reader
	
} //end search method

//This function creates an IndexSearcher
public IndexSearcher longQuery(String indexDir, String queriesFile, PrintWriter outputWriter, Debug debug, String similarity, Analyzer analyzer) throws IOException, ParseException {		
	
	//Welcome to shortQueries
	System.out.println("Set requests to LONG QUERIES");
	
	//Create IndexReader and Searcher
	IndexReader reader = DirectoryReader.open(FSDirectory.open( new File(indexDir).toPath()));		//Creating an index reader
	IndexSearcher searcher = new IndexSearcher(reader);												//Create an index searcher
	
	//Set similarity measure
	searcher = new SetSimilarity().SetSimilaritySearcher(searcher, similarity);
	
	//Read queries from file
		String[] queries = readQueriesFile(queriesFile);
		
		for (int i=0; i < queries.length; i++ ){ //for all queries
			
			try {
				//Parse Multiple fields Query														
			
				String str = queries[i];				//take all queries
				String[] parts = str.split("#", 3);		//split them in three
				//String num = parts[0];					//part 1 is number
				String title = parts[1];				//part 2 is title
				String desc = parts[2];					//part 3 is description
				String num = Integer.toString(i + 1);	//(part 1 is number ) other option for queyrid
				System.out.println("Query number: " + num);
				System.out.println("Title of query: " + title); 
				System.out.println("Description of query: " + desc);
				
				
				//Title query
				QueryParser titleQP = new QueryParser("contents", analyzer);				//Built QueryParser
				
				String newTitle = title.substring(3, title.length());	//Strip numbers from title
				Query titleQuery = titleQP.parse(newTitle); 									//Parse titles
				//Desc query
				QueryParser descQP = new QueryParser ("contents", analyzer);				//Build QueryParser
				Query descQuery = descQP.parse(desc);										//Parse descriptions	
											
				
				//Final Query
				Builder booleanQuery = new BooleanQuery.Builder();
				booleanQuery.add(titleQuery, BooleanClause.Occur.SHOULD);
				booleanQuery.add(descQuery, BooleanClause.Occur.SHOULD);
				Query q = booleanQuery.build();
				displayQuery(q);
				
				//Search
				TopScoreDocCollector collector = TopScoreDocCollector.create(1000);								//Create a TopScore Collector
				searcher.search(q, collector);	
				
				//Print results
				//displayResults(searcher, collector);
				
				//Output results to file
				outputResults(searcher, collector, outputWriter, num);
				
			}//end try
			catch (Exception e) {
				System.out.println("Parse exception in query #" + i);
				e.printStackTrace();
			}//end catch
		
	}//end for
		//Close index reader
		outputWriter.close();
		reader.close();
		return searcher;
		
} //end search method

public static void displayQuery(Query q) {
	System.out.println("Query: " + q.toString());
}

//This function reads queries from file one by one and calls shortQuery to search documents
public static String[] readQueriesFile(String queriesFile) throws IOException {
	
	List<String> list = Files.readAllLines(Paths.get(queriesFile), StandardCharsets.UTF_8);
	String[] a = list.toArray(new String[list.size()]); 
	return a;
	
}//end readQueriesfile

//This function display the results of the search showing top documents
//Args: IndexSearcher, TopScoreDocCollector
public static void displayResults(IndexSearcher search, TopScoreDocCollector collect) throws IOException {
	//Display results 
	ScoreDoc[] hits = collect.topDocs().scoreDocs;
	System.out.println("Found " + hits.length + " hits.");
	System.out.println("--------------------------------");
	
	for (int i=0; i < hits.length; i++){
		int docId = hits[i].doc;
		Document d = search.doc(docId);
		System.out.println((i+1) + ". " + d.get("docno") + " score=" + hits[i].score);
	}//end for
	System.out.println("--------------------------------");
}//end display results

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

	System.out.println("--------------------------------");
}//end display results

}//end Search class
