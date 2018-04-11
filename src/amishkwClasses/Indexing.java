package amishkwClasses;

//Indexing
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
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

	//------------------------------------------------------------------------------------------------(Main Indexer Method)
	
	public static void main(String dir) throws Exception {
		// TODO Auto-generated method stub
		//new IndexingBaseline().IndexBaseline("/Users/samuelmarticotte/Desktop/4_Education/0_PhD/0_DIC/0_Cours_actuels/Hiver_2018/Psycholinguistique_et_TAL/3_TP1/0_Collection_de_documents/0_documents_a_indexer_baseline");
		//new Indexing().IndexSophisticated("/Users/samuelmarticotte/Desktop/4_Education/0_PhD/0_DIC/0_Cours_actuels/Hiver_2018/Psycholinguistique_et_TAL/3_TP1/0_Collection_de_documents/0_documents_a_indexer_sophisticated");
		//new Indexing().IndexBaseline(dir); //call from main
	}
	
	//-----------------------------------------------------------------------------------------------(Baseline Indexing)
	
	//This method indexes documents with baseline 
	//Settings = tf-idf, standard analyzer, stop list and stemming
	//Arg 1: directory of documents
	public void IndexBaseline(String dir) throws Exception {
		
		//Calculat time
		DecimalFormat df = new DecimalFormat(".##");
		long start = System.nanoTime(); //calculate current time
		
		//Create analyzer
		Directory indexDir = FSDirectory.open(new File(dir).toPath()); 	//opens the directory to files
		Analyzer analyzer = new StandardAnalyzer();						//Creates a standard analyser for tokenization
		IndexWriterConfig cfg = new IndexWriterConfig(analyzer);		//Creates index indexWriter using the standard analyser
		cfg.setOpenMode(OpenMode.CREATE);								//Creation mode
		indexWriter = new IndexWriter(indexDir, cfg);					//Create an IndexindexWriter
		
		FileFilter filter = null; 										//All files will be indexed
		indexDir(dir, filter);	  										//Index all files					
		
		long elapsedTime = System.nanoTime() - start; //calculate elapsed time
		double seconds = (double)elapsedTime / 1000000000.0;
		System.out.println("Calculation time : " + df.format(seconds) + "seconds");
		close(); //close IndexindexWriter
		
	}//end IndexBaseline method
	
	//------------------------------------------------------------------------------------------------(Sophisticated Indexing)
	
	//This method indexes documents with Okapi-BM25
	//Settings : 
	//Arg1 : directory of documents
	public void IndexSophisticated(String dir) throws Exception {
		
		//Calculate time
		DecimalFormat df = new DecimalFormat(".##");
		long start = System.nanoTime(); //calculate current time
		
		//Open directory for indexing
		Directory indexDir = FSDirectory.open(new File(dir).toPath()); 	//opens the directory to files
		System.out.println("Index files in directory:\n" + dir.toString());

		//Analyzer is sophisticated : stop + porter
		Analyzer analyzer = new AnalyzerSophisticated();

		//Write the index
		IndexWriterConfig cfg = new IndexWriterConfig(analyzer);		//Creates index indexWriter using the standard analyser
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
			System.out.println("Added: " + doc.get("docno")); 									//Prints added document						
			//System.out.println(doc);
		}//end while
		
	}//end indexFile method
	//------------------------------------------------------------------------------------------------(Close indexWriter)
	
	public void close() throws IOException {
		indexWriter.close();
	}//close indexWriter
	
}


