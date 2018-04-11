package amishkwClasses;

//io
import java.io.File;

//Exceptions
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
	// TODO Auto-generated method stub
	
	String documentsDirBase = "/Users/samuelmarticotte/Desktop/4_Education/0_PhD/0_DIC/0_Cours_actuels/Hiver_2018/Psycholinguistique_et_TAL/3_TP1/0_Collection_de_documents/0_documents_a_indexer_baseline";
	String documentsDirSoph = "/Users/samuelmarticotte/Desktop/4_Education/0_PhD/0_DIC/0_Cours_actuels/Hiver_2018/Psycholinguistique_et_TAL/3_TP1/0_Collection_de_documents/0_documents_a_indexer_sophisticated";
	String shortQueriesFile = "/Users/samuelmarticotte/Desktop/4_Education/0_PhD/0_DIC/0_Cours_actuels/Hiver_2018/Psycholinguistique_et_TAL/3_TP1/1_Requetes/2_requetesCourtes.txt";
	String longQueriesFile = "/Users/samuelmarticotte/Desktop/4_Education/0_PhD/0_DIC/0_Cours_actuels/Hiver_2018/Psycholinguistique_et_TAL/3_TP1/1_Requetes/1_requetesLongues.txt";
	//new Indexing().IndexBaseline(directory);
	//new Indexing().IndexSophisticated(directory); //Index all documents from the folder
	
	//Query
	//System.out.println("===Short queries===");
	//new Search().shortQuery(documentsDirBase, shortQueriesFile);			//Search query in indexed documents
	//System.out.println("===Long queries===");
	//new Search().longQuery(documentsDirSoph, longQueriesFile);
}


//This function creates an IndexSearcher
public void shortQuery(String indexDir, String queriesFile) throws IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException {		
	
	//Create IndexReader and Searcher
	IndexReader reader = DirectoryReader.open(FSDirectory.open( new File(indexDir).toPath()));		//Creating an index reader
	IndexSearcher searcher = new IndexSearcher(reader);												//Create an index searcher
	
	//Read queries from file
	String[] queries = readQueriesFile(queriesFile);
	
	for (int i=0; i < queries.length; i++ ){ //for all queries
		
		try {
		//Parse Query
		//QueryParser parser = new QueryParser("title", new StandardAnalyzer());							//Standard Analyzer
		QueryParser parser = new QueryParser("contents", new StandardAnalyzer());							//Sophisticated Analyzer																
		Query query = parser.parse(queries[i]);	
		System.out.println("Query = " + query );
		
		//Search
		TopScoreDocCollector collector = TopScoreDocCollector.create(10);								//Create a TopScore Collector
		searcher.search(query, collector);	
		
		//Print results
		displayResults(searcher, collector);	
		
		} catch (Exception e) {
			System.out.println("Parse exception in query #" + i);
			e.printStackTrace();
		}//end catch
		
	}//end for
	
	reader.close();																						//close index reader
	
} //end search method

//This function creates an IndexSearcher
public IndexSearcher longQuery(String indexDir, String queriesFile) throws IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException {		
	
	//Create IndexReader and Searcher
	IndexReader reader = DirectoryReader.open(FSDirectory.open( new File(indexDir).toPath()));		//Creating an index reader
	IndexSearcher searcher = new IndexSearcher(reader);												//Create an index searcher
	
	//Set similarity measure
	searcher.setSimilarity(new BM25Similarity());
	BM25Similarity custom = new BM25Similarity(); //Default values are k1 =  1.2b = 0.75
	searcher.setSimilarity(custom);
	
	//Parse query with standard analyzer
	Analyzer analyzer = new AnalyzerSophisticated();

	//Read queries from file
		String[] queries = readQueriesFile(queriesFile);
		
		for (int i=0; i < queries.length; i++ ){ //for all queries
			
			try {
				//Parse Multiple fields Query														
			
				String str = queries[i];				//take all queries
				String[] parts = str.split("#", 3);		//split them in three
				String num = parts[0];					//part 1 is number
				String title = parts[1];				//part 2 is title
				String desc = parts[2];					//part 3 is description
				System.out.println("Query number: " + num);
				System.out.println("Title of query: " + title); 
				System.out.println("Description of query: " + desc);
				
				
				//Title query
				QueryParser titleQP = new QueryParser("contents", analyzer);				//Built QueryParser
				Query titleQuery = titleQP.parse(title); 									//Parse titles
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
				TopScoreDocCollector collector = TopScoreDocCollector.create(1);								//Create a TopScore Collector
				searcher.search(q, collector);	
				
				//Print results
				displayResults(searcher, collector);	
				
			}//end try
			catch (Exception e) {
				System.out.println("Parse exception in query #" + i);
				e.printStackTrace();
			}//end catch
			
	}//end for
	
	//Close index reader
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

}//end Search class
