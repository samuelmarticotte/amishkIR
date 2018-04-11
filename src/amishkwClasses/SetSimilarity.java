package amishkwClasses;

import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;

public class SetSimilarity {

	public static void main(String[] args) {
		// TODO Auto-generated method stub		
	}
		//Set similarity for index writer
		public IndexWriterConfig SetSimilarityIndex( IndexWriterConfig cfg, String similarity )
		{
			int switche = 0;
			if (similarity == "BM25"){ switche = 0; }
			else
				switche = 1;
			
			switch (switche) {
			
			case 0: 
				//Set similarity measure
				cfg.setSimilarity(new BM25Similarity());
				BM25Similarity custom = new BM25Similarity(); //Default values are k1 =  1.2b = 0.75
				cfg.setSimilarity(custom);
				System.out.println("Set similarity to BM25");
				break;
			case 1:
				//Set similarity measure
				cfg.setSimilarity(new ClassicSimilarity());
				ClassicSimilarity custom2 = new ClassicSimilarity(); //Default values are k1 =  1.2b = 0.75
				cfg.setSimilarity(custom2);
				System.out.println("Set similarity to TF-IDF");
				break;
			default: 
				System.out.println("Similarity not found : error");
				System.exit(0); //exit
			}
			return cfg;
		}//end constructor set similarity for index writer
		
		
		//Set similarity for indexSearcher
		public IndexSearcher SetSimilaritySearcher (IndexSearcher searcher, String similarity )
		{
			int switche = 0;
			if (similarity == "BM25"){ switche = 0; }
			else
				switche = 1;
			
			switch (switche) {
			case 0:
				//Set similarity measure
				searcher.setSimilarity(new BM25Similarity());
				BM25Similarity custom = new BM25Similarity(); //Default values are k1 =  1.2b = 0.75
				searcher.setSimilarity(custom);
				System.out.println("Set similarity to BM25");
				break;
				
			case 1: 
				//Set similarity measure
				searcher.setSimilarity(new ClassicSimilarity());
				ClassicSimilarity custom2 = new ClassicSimilarity(); //Default values are k1 =  1.2b = 0.75
				searcher.setSimilarity(custom2);
				System.out.println("Set similarity to TF-IDF");
				break;
			default: //if does not find similarity
				System.out.println("Similarity not found : error");
				System.exit(0); //exit
			}
			return searcher;
		}//end constructor set similarity for index searcher
}
