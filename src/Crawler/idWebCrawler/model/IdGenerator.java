package Crawler.idWebCrawler.model;
//package model;

import java.util.Vector;

import org.w3c.dom.Document;

/**
 * Class for loading pages and generating the xml ids from them
 * @author Mike Wojcenovich
 */
public class IdGenerator {
	
	/**
	 * Parser for getting query info from Document instance. i.e. queryId and number of results
	 */
	private QueryParser queryParser;
	
	/**
	 * Parser for getting ids from a Document instance.
	 */
	private PageIdParser idParser;
	
	/**
	 * Loads instances of Document that represent pages from a query
	 */
	private PageLoader loader;
	
	/**
	 * Input information for a form
	 */
	private InputFieldSet inputSet;
	
	/**
	 * Number of results that appear per page
	 */
	int resultsPerPage;

        public volatile boolean stopThread = false;
	
	/**
	 * Constructor
	 * @param xmlIdLocation The tag where the xmlIds will appear in
	 * @param xmlIdRegEx The regular expressions to cut unnecessary info from the result
	 * @param inputSet The set of input information for a form
	 * @param loader Loads Document instances that represent the search results
	 * @param resultNumberLocation The tag where the number of results appear in
	 * @param resultNumberRegEx The regular expressions for cutting unnecessary info from the result
	 * @param queryIdLocation The tag that queryId will appear in
	 * @param queryIdRegEx The regular expressions that will cut unnecessary info from the result
	 * @param resultsPerPage The number of results per page of search results
	 */
	public IdGenerator(HtmlTag xmlIdLocation, String[] xmlIdRegEx, InputFieldSet inputSet, PageLoader loader,
			HtmlTag resultNumberLocation, String[] resultNumberRegEx,
			HtmlTag queryIdLocation, String[] queryIdRegEx, int resultsPerPage) {
		this.idParser = new PageIdParser(xmlIdLocation, xmlIdRegEx);
		this.inputSet = inputSet;
		this.loader = loader;
		
		this.queryParser = new QueryParser(resultNumberLocation, resultNumberRegEx, queryIdLocation, queryIdRegEx);
		this.resultsPerPage = resultsPerPage;
	}
	
	/**
	 * Generates the xml ids for all input field sets
	 * @return An array of xml ids
	 */
	public String[] generateIds() {
		
		Vector<String> idVector = new Vector<String>();
		
		for(int i = 0; i < inputSet.getSetSize(); i++) {
			Document firstPage = loader.loadFirstPage(inputSet.createQuerySet(i));
			String queryId = queryParser.getQueryId(firstPage);
			int results = queryParser.getPageResults(firstPage);
			
			if(results > 0 && queryId != null) {
				int pages = (int)Math.ceil((double)results/resultsPerPage);
				
				for(int j = 1; j <= pages; j++) {
					Vector<String> tempIdVector = this.generateIds(queryId, j);
					idVector.addAll(tempIdVector);

                                        // slow down process so that needs.org doesn't forbid futher requests. Jarrett G. Steele.
                                        try{
                                            Thread.sleep(500);
                                        }
                                        catch(Exception e){
                                        }

                                        // thread stop routine
                                        if( stopThread ) return null;
				}
			}
		}
		
		String[] id = new String[idVector.size()];
		for(int i = 0; i < id.length; i++) {
			id[i] = idVector.elementAt(i);
		}
		return id;
	}
	
	/**
	 * Generates the xml id for a specific search result page
	 * @param queryId The query id for this search
	 * @param pageNum The page number
	 * @return A Vector of XML ids
	 */
	private Vector<String> generateIds(String queryId, int pageNum) {
		Vector<String> idVector = new Vector<String>();
		Document page = loader.loadPage(queryId, pageNum);
		
		String[] id = idParser.getIds(page);
		for(int i = 0; i < id.length; i++) {
			idVector.add(id[i]);
		}
		
		return idVector;
	}
}
