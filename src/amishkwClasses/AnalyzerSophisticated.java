package amishkwClasses;

//filters
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

//This class is a sophisticated analyzer to get the best result for indexing and searching

public class AnalyzerSophisticated extends Analyzer{
	
		@Override
		protected TokenStreamComponents createComponents(String fieldName) {
			
			StandardTokenizer source = new StandardTokenizer();							//Creates a standard tokenizer
			source.setMaxTokenLength(255);												//set max token length to 255
			
			TokenStream filter = new StandardFilter(source);							//tokenizes stream
			
			filter = new StopFilter(filter, StopAnalyzer.ENGLISH_STOP_WORDS_SET);		//stop word filter is applied
			filter = new LowerCaseFilter(filter);
			filter = new PorterStemFilter(filter);										//porter stemmer filter is applied
			
			System.out.println("Analyzer includes stop filter & porter stemming");
			return new TokenStreamComponents(source, filter);
		}//end TokenStream

}//end AnalyzerSophisticated

