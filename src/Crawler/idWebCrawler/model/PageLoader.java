package Crawler.idWebCrawler.model;
//package model;

import org.w3c.dom.Document;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * Class for loading pages for search
 * @author Mike Wojcenovich
 */
public class PageLoader {
	
	/**
	 * Parser to convert an input stream to a Document instance
	 */
	private HtmlParser htmlParser;
	
	/**
	 * The Http Client to generate page requests
	 */
	private HttpClient  client;
	
	/**
	 * The URL of the action page for this search
	 */
	private String searchActionUrl;
	
	/**
	 * The generator for result page urls
	 */
	private ResultPageUrlGenerator urlGenerator;
	
	public PageLoader(String searchActionUrl, ResultPageUrlGenerator urlGenerator) {
		this.htmlParser = new HtmlParser();
		this.client = new HttpClient();
		
		this.searchActionUrl = searchActionUrl;
		this.urlGenerator = urlGenerator;
	}
	
	/**
	 * Loads the first page of a search
	 * @param input The name and values for the fields
	 * @return The Document instance of the first result page
	 */
	public Document loadFirstPage(NameValuePair[] input) {
		PostMethod method = new PostMethod(searchActionUrl);
		method.setRequestBody(input);
		
		try{
			client.executeMethod(method);
			
			Document firstPage = htmlParser.parse(method.getResponseBodyAsStream(), method.getURI().toString(),
													method.getRequestCharSet());
			
			method.releaseConnection();
			return firstPage;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Loads a search result page
	 * @param queryId The query id for this search
	 * @param pageNum The number of the search result page to generate
	 * @return The Document instance corresponding to this search result page
	 */
	public Document loadPage(String queryId, int pageNum) {
		GetMethod method = new GetMethod(urlGenerator.constructLink(pageNum, queryId));
		
		try {
			client.executeMethod(method);
			
			Document page = htmlParser.parse(method.getResponseBodyAsStream(), method.getURI().toString(),
												method.getRequestCharSet());
			
			method.releaseConnection();
			return page;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
