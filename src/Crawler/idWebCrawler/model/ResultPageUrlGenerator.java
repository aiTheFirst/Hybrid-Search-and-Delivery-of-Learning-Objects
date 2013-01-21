package Crawler.idWebCrawler.model;
//package model;

/**
 * Class to generate search result page links
 * @author Mike Wojcenovich
 *
 */
public class ResultPageUrlGenerator {
	
	/**
	 * The main URL before GET parameters and excluding the ending '?'
	 */
	private String url;
	
	/**
	 * The GET param name for page number
	 */
	private String pageNumParamName;
	
	/**
	 * The GET param name for query id
	 */
	private String queryIdParamname;
	
	/**
	 * Constructor
	 * @param url The main URL before GET parameters and excluding the ending '?'
	 * @param pageNumParamName The GET param name for page number
	 * @param queryIdParamName The GET param name for query id
	 */
	public ResultPageUrlGenerator(String url, String pageNumParamName, String queryIdParamName) {
		this.url = url;
		this.pageNumParamName = pageNumParamName;
		this.queryIdParamname = queryIdParamName;
	}
	
	/**
	 * @param pageNum The number of the page to generate
	 * @param queryId The query id for this search
	 * @return A link for a search result page
	 */
	public String constructLink(int pageNum, String queryId) {
		String resultLink = this.url + "?" + this.queryIdParamname + "=" + queryId +
							"&" + this.pageNumParamName + "=" + pageNum;
		return resultLink;
	}
}