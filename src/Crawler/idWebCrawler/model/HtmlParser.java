package Crawler.idWebCrawler.model;
//package model;

import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.parser.DocumentBuilderImpl;
import org.lobobrowser.html.parser.InputSourceImpl;
import org.lobobrowser.html.test.SimpleUserAgentContext;
import org.w3c.dom.Document;

import java.io.InputStream;

/**
 * Class for parsing a input stream from a http method to
 * a Document object
 * @author Mike Wojcenovich
 */
public class HtmlParser {
	
	/**
	 * Used to parse the input stream.
	 */
	private DocumentBuilderImpl dbi;
	
	/**
	 * Constructor
	 */
	public HtmlParser() {
		UserAgentContext context = new SimpleUserAgentContext();
		dbi = new DocumentBuilderImpl(context);
	}
	
	/**
	 * Parses a stream to a Document object
	 * @param stream The stream of the page
	 * @param uri The URI for the method
	 * @param charSet The character set for the method request
	 * @return The document for the page obtained from a HTTP method
	 */
	public Document parse(InputStream stream, String uri, String charSet) {
		try {
			return dbi.parse(new InputSourceImpl(stream, uri, charSet));
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
