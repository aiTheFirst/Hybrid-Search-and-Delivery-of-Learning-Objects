package Crawler.idWebCrawler.model;
//package model;

/**
 * Class to represent a search engine and all information associated with parsing specific information from search results
 * @author Mike Wojcenovich
 *
 */
public class SearchEngine {

	/**
	 * Name of the site to use for storing results to a file
	 */
	private String siteName;
	
	/**
	 * Loads Documents containing the structure of search result pages
	 */
	private PageLoader loader;
	
	/**
	 * Gets the ids from search result pages
	 */
	public IdGenerator idGenerator;
	
	/**
	 * Writes the ids to a file
	 */
	private IdListFileWriter fileWriter;
	
	/**
	 * Constructor
	 * @param siteName Name of the site to use for storing ids to a file
	 * @param searchActionUrl The URL of the action page of the search form including domain
	 * @param resultsPerPage The number of results per search page
	 * @param urlGenerator The URL generator for search result pages
	 * @param resultPageLocation The tag that will contain the number of results for each search
	 * @param resultPageRegEx The regular expressions to cut unnecessary info from the number of results
	 * @param queryIdLocation The tag that will contain the query id in attribute value
	 * @param queryIdRegEx The regular expressions to cut unnecessary info from the query id
	 * @param xmlIdLocation The tag that will contain an xml id in attribute value
	 * @param xmlIdRegEx The regular expressions to cut unnecessary info from the xml id
	 * @param inputSet The set of inputs and values to this search engine
	 */
	public SearchEngine(String siteName, String searchActionUrl, int resultsPerPage, ResultPageUrlGenerator urlGenerator,
						HtmlTag resultPageLocation, String[] resultPageRegEx,
						HtmlTag queryIdLocation, String[] queryIdRegEx,
						HtmlTag xmlIdLocation, String[] xmlIdRegEx, InputFieldSet inputSet) {
		this.siteName = siteName;
		this.loader = new PageLoader(searchActionUrl, urlGenerator);
		
		this.idGenerator = new IdGenerator(xmlIdLocation, xmlIdRegEx, inputSet, loader,
										resultPageLocation, resultPageRegEx, queryIdLocation, queryIdRegEx, resultsPerPage);
		this.fileWriter = new IdListFileWriter();
	}
	
	/**
	 * Gets the ids for the site and saves them to a file
	 * @return Array of the XML ids
	 */
	public String[] getIds() {
		String[] id = this.idGenerator.generateIds();
		fileWriter.writeXmlIdsToFile(id, siteName /* + ".txt"*/);

                


		return id;
	}
}
