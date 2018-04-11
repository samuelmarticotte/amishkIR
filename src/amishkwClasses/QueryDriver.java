package amishkwClasses;


/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.benchmark.quality.Judge;
import org.apache.lucene.benchmark.quality.QualityBenchmark;
import org.apache.lucene.benchmark.quality.QualityQuery;
import org.apache.lucene.benchmark.quality.QualityQueryParser;
import org.apache.lucene.benchmark.quality.QualityStats;
import org.apache.lucene.benchmark.quality.trec.Trec1MQReader;
import org.apache.lucene.benchmark.quality.trec.TrecJudge;
import org.apache.lucene.benchmark.quality.trec.TrecTopicsReader;
import org.apache.lucene.benchmark.quality.utils.SimpleQQParser;
import org.apache.lucene.benchmark.quality.utils.SubmissionReport;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;

/**
 * Command-line tool for doing a TREC evaluation run.
 **/
public class QueryDriver {
	
  public static void main(String[] args) throws Exception {
	
		//CONSTANTS
		String documentsDirSoph = "/Users/samuelmarticotte/Desktop/4_Education/0_PhD/0_DIC/0_Cours_actuels/Hiver_2018/Psycholinguistique_et_TAL/3_TP1/0_Collection_de_documents/0_documents_a_indexer_sophisticated";
		String jugementPert= "/Users/samuelmarticotte/Desktop/4_Education/0_PhD/0_DIC/0_Cours_actuels/Hiver_2018/Psycholinguistique_et_TAL/3_TP1/0_Collection_de_documents/2_jugement_pertinence_de_requetes_1-50.txt";
		String topicsFile = "/Users/samuelmarticotte/Desktop/4_Education/0_PhD/0_DIC/0_Cours_actuels/Hiver_2018/Psycholinguistique_et_TAL/3_TP1/0_Collection_de_documents/3_requetes_1-50.txt";
		String outputFile = "/Users/samuelmarticotte/Desktop/4_Education/0_PhD/0_DIC/0_Cours_actuels/Hiver_2018/Psycholinguistique_et_TAL/3_TP1/0_Collection_de_documents/output.txt";
		String docNameField = "docno";
		new QueryDriver().QueryEval(topicsFile, jugementPert, outputFile, documentsDirSoph, docNameField); 
  }
	
  public void QueryEval(String topicsFile2, String jugementPert, String outputFile, String documentsDirSoph, String docNameField ) throws Exception {

	  /**
	   * #1 Read TREC topics as QualityQuery[]
	   * 
	   * #2 Create Judge from TREC Qrel file
	   * 
	   * #3 Verify query and Judge match
	   * 
	   * #4 Create parser to translate queries into Lucene queries
	   * 
	   * #5 Run benchmark
	   * 
	   * #6 Print precision and recall measures
	   */
	  
	//CONSTANTS
    File topicsFile = new File(topicsFile2);
    File qrelsFile = new File(jugementPert);
    SubmissionReport submitLog = new SubmissionReport(new PrintWriter(outputFile), "lucene");
    FSDirectory dir = FSDirectory.open(new File(documentsDirSoph).toPath());
    IndexReader reader = DirectoryReader.open(dir);		//Creating an index reader
    IndexSearcher searcher = new IndexSearcher(reader);
    String fieldSpec = "docname"; // default to Title-only if not specified.

    int maxResults = 1000;

    PrintWriter logger = new PrintWriter(System.out, true);

    // use trec utilities to read trec topics into quality queries
    TrecTopicsReader qReader = new TrecTopicsReader();
    QualityQuery qqs[] = qReader.readQueries(new BufferedReader(new FileReader(topicsFile)));
  
    //Show all queries (5)
    for ( int i = 0; i < 49; i++) {
    	System.out.println(qqs[i].getQueryID() +  " " + qqs[i].getValue("title") );
    }//end for
    
    System.out.println("Reading queries from topics file: check");
    pressAnyKeyToContinue();
    
    // prepare judge, with trec utilities that read from a QRels file
    Judge judge = new TrecJudge(new BufferedReader(new FileReader(qrelsFile)));
    // validate topics & judgments match each other
    judge.validateData(qqs, logger);
    System.out.println("Validate topics againts judgments: error here!!!!!");
    pressAnyKeyToContinue();
    

    Set<String> fieldSet = new HashSet<String>();
    if (fieldSpec.indexOf('T') >= 0) fieldSet.add("title" + "desc");
    
    // set the parsing of quality queries into Lucene queries.
    QualityQueryParser qqParser = new SimpleQQParser(fieldSet.toArray(new String[0]), "desc");
    
 
    // run the benchmark : QualityQuery[], QualityQueryParser, Index Searcher, docNameField
    QualityBenchmark qrun = new QualityBenchmark(qqs, qqParser, searcher, docNameField);
    qrun.setMaxResults(maxResults);
    System.out.println("Max eval: check");
    pressAnyKeyToContinue();
    
    QualityStats stats[] = qrun.execute(judge, submitLog, logger);
    System.out.println("Evaluation stats: check");
    pressAnyKeyToContinue();
    
    // print an avarage sum of the results
    QualityStats avg = QualityStats.average(stats);
    System.out.println("Average stats: check");
    pressAnyKeyToContinue();
    avg.log("SUMMARY", 2, logger, "  ");

    
    
    //searcher.close();
    logger.close();
    reader.close();
    dir.close();

  }//end QueryEval
  
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
  
}//end class