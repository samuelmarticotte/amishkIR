package amishkwClasses;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.store.FSDirectory;



public class QueryExpansion {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	//This function creates an IndexSearcher
	public IndexSearcher longQueryExpansion(String indexDir, String queriesFile, PrintWriter outputWriter, Debug debug, String similarity, Analyzer analyzer) throws IOException, ParseException {		
		
		//Welcome to shortQueries
		System.out.println("Set requests to LONG QUERIES EXPANSION");
		
		//Create IndexReader and Searcher
		IndexReader reader = DirectoryReader.open(FSDirectory.open( new File(indexDir).toPath()));		//Creating an index reader
		IndexSearcher searcher = new IndexSearcher(reader);												//Create an index searcher
		
		//Set similarity measure
		searcher = new SetSimilarity().SetSimilaritySearcher(searcher, similarity);
		
		//Read queries from file
			String[] queries = Search.readQueriesFile(queriesFile);
			
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
					Search.displayQuery(q);
					
					//Search
					TopScoreDocCollector collector = TopScoreDocCollector.create(1000);								//Create a TopScore Collector
					searcher.search(q, collector);	
					
					//Print results
					//displayResults(searcher, collector);
					
					//Output results to file
					Search.outputResults(searcher, collector, outputWriter, num);
					
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

}
