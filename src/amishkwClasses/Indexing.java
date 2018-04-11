package amishkwClasses;

//Indexing
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

//Documents
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.Analyzer;
 
//filters
import org.apache.lucene.analysis.standard.StandardAnalyzer;

//io
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

public class Indexing {
	
	private IndexWriter indexWriter;
	Debug debug;

	//------------------------------------------------------------------------------------------------(Main Indexer Method)
	
	public static void main(String dir) throws Exception {
	}
	
	Indexing(Debug dg){
		this.debug = dg;
	}
	
	//-----------------------------------------------------------------------------------------------(Baseline Indexing)
	
	//This method indexes documents with baseline 
	//Settings = tf-idf, standard analyzer, stop list and stemming
	//Arg 1: directory of documents
	public void IndexBaseline(String dir, Debug debug, String similarity, Analyzer analyzer) throws Exception {
		
		//Calculat time
		DecimalFormat df = new DecimalFormat(".##");
		long start = System.nanoTime(); //calculate current time
		
		//Create Index Directory
		Directory indexDir = FSDirectory.open(new File(dir).toPath()); 	//opens the directory to files
		IndexWriterConfig cfg = new IndexWriterConfig(analyzer);		//Creates index indexWriter using the standard analyser
	
		//Set similarity
		cfg = new SetSimilarity().SetSimilarityIndex(cfg, similarity);
		
		cfg.setOpenMode(OpenMode.CREATE);								//Creation mode
		indexWriter = new IndexWriter(indexDir, cfg);					//Create an IndexindexWriter
		
		//Index all files
		FileFilter filter = null; 									
		indexDir(dir, filter);	  										
		
		//calculate elapsed time
		long elapsedTime = System.nanoTime() - start; 
		double seconds = (double)elapsedTime / 1000000000.0;
		System.out.println("Calculation time : " + df.format(seconds) + "seconds");
		System.out.println("Analyzer was baseline");	//Using Stop filter and Porter stemming
		close(); //close IndexindexWriter
		
	}//end IndexBaseline method
	
	//------------------------------------------------------------------------------------------------(Sophisticated Indexing)
	
	//This method indexes documents with Okapi-BM25
	//Settings : 
	//Arg1 : directory of documents
	public void IndexSophisticated(String dir, Debug debug, String similarity, Analyzer analyzer) throws Exception {
		
		//Calculate time
		DecimalFormat df = new DecimalFormat(".##");
		long start = System.nanoTime(); //calculate current time
		
		//Open directory for indexing
		Directory indexDir = FSDirectory.open(new File(dir).toPath()); 	//opens the directory to files
		System.out.println("Index files in directory:\n" + dir.toString());
		IndexWriterConfig cfg = new IndexWriterConfig(analyzer);		//Creates index indexWriter using the standard analyser
		
		//Setting similarity
		cfg = new SetSimilarity().SetSimilarityIndex(cfg, similarity);
		//cfg = new SetSimilarity().SetSimilarityIndex(cfg, similarity);
		
		cfg.setRAMBufferSizeMB(256.0);									//Increase RAM buffer size
		cfg.setOpenMode(OpenMode.CREATE);								//Creation mode
		indexWriter = new IndexWriter(indexDir, cfg);

		
		FileFilter filter = null; 										//no file filter for now : all files indexed
		indexDir(dir, filter);	  										//Index all files
		
		
		long elapsedTime = System.nanoTime() - start; //calculate elapsed time
		double seconds = (double)elapsedTime / 1000000000.0;
		System.out.println("Calculation time : " + df.format(seconds) + "seconds");			//How much time for computation
		System.out.println("Analyzer was sophisticated : Stop filter & Porter stemming");	//Using Stop filter and Porter stemming
		close(); //close IndexindexWriter

	}//end IndexSophisticated method

	
	//------------------------------------------------------------------------------------------------(Index Directory)
	
	public int indexDir(String dataDir, FileFilter filter) throws Exception {
		
		File[] files = new File(dataDir).listFiles();
		for(File f: files) {
			if (/* add other conditions to filter files */(filter == null||filter.accept(f))){
				indexFile(f);		
			}//end if
			
		}//end for 
		
		System.out.println("Indexed a total of : " + indexWriter.numDocs() + " documents");
		return indexWriter.numDocs();
		
	}//end index directory method
	
	//------------------------------------------------------------------------------------------------(Index File)
	
	//This method indexes a file
	//Arg1 : file as entry
	private void indexFile(File f) throws Exception {
		
		//Document doc = getDocument(f); 							//grab document using  local method
		//Document doc = new JTidyHTMLHandler().getDocument(f);						//Grab document using html crawler
		//indexWriter.addDocument(doc);
		TrecDocIterator docs = new TrecDocIterator(f);
		Document doc;
	
		//Grabs next document 
		while (docs.hasNext()) {
			doc = docs.next();
			if (doc != null && doc.getField("contents") != null) 								//until no content grab documents
				
				indexWriter.addDocument(doc);
			System.out.println("Added: " + doc.get("docno")); //Prints added document
			//this.debug.append("Added: " + doc.get("docno")); 
			//System.out.println(doc);
		}//end while
		
	}//end indexFile method
	//------------------------------------------------------------------------------------------------(Close indexWriter)
	
	
	
	
	public void close() throws IOException {
		indexWriter.close();
	}//close indexWriter
	
}


