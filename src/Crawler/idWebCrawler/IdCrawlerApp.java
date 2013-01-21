package Crawler.idWebCrawler;

import Crawler.idWebCrawler.model.*;
//import model.*;

/**
 * Runs the program to create files of xml ids
 * @author Mike Wojcenovich
 */
public class IdCrawlerApp extends Thread {

	/**
	 *  July 2009 - Class now executes as thread, req'd for merge into invoker project.
	 *  Jarrett G. Steele
	 */
        public static SearchEngine search = null;
        public static String[] keyword = null;
        public static String outputFilename = null;

	public void run(){
                
                IdCrawlerApp.main(null); // execute as intended by Mike.
	}
	
	public static void main(String[] args) {
		
		String actionUrl = "http://www.needs.org/needs/public/search/search_results/index.jhtml?_DARGS=/needs/include/header.jhtml.1";
		int resultsPerPage = 10;
		ResultPageUrlGenerator urlGenerator = new ResultPageUrlGenerator("http://www.needs.org:80/needs/public/search/search_results/index.jhtml", "page", "queryId");
		
		HtmlTag resultNumberLocation = new HtmlTag("h3", "class", "line");
		String[] resultNumberRegEx = {"Search Results:[\\D]+[\\d]+ to[\\D]+[\\d]+[\\D]+", " total results[\\d\\s\\w]+"};
		HtmlTag queryIdLocation = new HtmlTag("a", "href", "queryId=");
		String[] queryIdRegEx = {"/needs/public/search/search_results/index.jhtml[?]queryId=", "&page=\\d+"};
		
		HtmlTag xmlIdLocation = new HtmlTag("a", "href", "/needs/public/search/search_results/learning_resource/summary/?id=");
		String[] xmlIdRegEx = {"/needs/public/search/search_results/learning_resource/summary/[?]id=", ""};
		
		//modified for threading
		//String[] keyword = {"science", "programming", "history"};
		InputField[] input = {
			new InputField("/needs/forms/SimpleSearchForm.keyword", keyword, ""),
			new InputField("_D:/needs/forms/SimpleSearchForm.keyword", new String[0], " ")
		};
		InputFieldSet inputSet = new InputFieldSet(input);

                //modified for threading
		/*SearchEngine*/ search = new SearchEngine(outputFilename, actionUrl, resultsPerPage, urlGenerator,
								resultNumberLocation, resultNumberRegEx, queryIdLocation, queryIdRegEx,
								xmlIdLocation, xmlIdRegEx, inputSet);
		search.getIds();
	}
}
