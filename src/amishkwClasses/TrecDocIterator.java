package amishkwClasses;

//https://github.com/isoboroff/trec-demo/blob/master/src/IndexTREC.java

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

public class TrecDocIterator implements Iterator<Document> {

	protected BufferedReader rdr;
	protected boolean at_eof = false;
	
	//Iterates through files, adds fields
	
	public TrecDocIterator(File file) throws FileNotFoundException {
		rdr = new BufferedReader(new FileReader(file));
	}


	@Override
	public Document next() {
		Document doc = new Document();
		StringBuffer sb = new StringBuffer();
		try {
			String line;
			Pattern docno_tag = Pattern.compile("<DOCNO>\\s*(\\S+)\\s*<");
			boolean in_doc = false;
			while (true) {
				line = rdr.readLine();
				if (line == null) {
					at_eof = true;
					break;
				}
				if (!in_doc) {
					if (line.startsWith("<DOC>"))
						in_doc = true;
					else
						continue;
				}
				if (line.startsWith("</DOC>")) {
					in_doc = false;
					sb.append(line);
					break;
				}

				Matcher m = docno_tag.matcher(line);
				if (m.find()) {
					String docno = m.group(1);
					doc.add(new StringField("docno", docno, Field.Store.YES));
				}

				sb.append(line);
			}
			if (sb.length() > 0)
			{
					String sbstring = sb.toString().replace("<DOC>", "");
					sbstring = sbstring.replace("</DOC>", "");
					sbstring = sbstring.replace("</HEAD>", "");
					sbstring = sbstring.replace("<HEAD>", "");
					sbstring = sbstring.replace("</BYLINE>", "");
					sbstring = sbstring.replace("<BYLINE>", " ");
					sbstring = sbstring.replace("</DATELINE>", "");
					sbstring = sbstring.replace("<DATELINE>", " ");
					sbstring = sbstring.replace("</TEXT>", "");
					sbstring = sbstring.replace("<TEXT>", " ");			
					doc.add(new TextField("contents", sb.toString(), Field.Store.YES));
			}
		} catch (IOException e) {
			doc = null;
		}
		return doc;
	}

	@Override
	public void remove() {
		// Do nothing, but don't complain
	}
	
	@Override
	public boolean hasNext() {
		return !at_eof;
	}

}