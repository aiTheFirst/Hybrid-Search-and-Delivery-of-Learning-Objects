package Crawler.fileWebCrawler.model;
//package model;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * Class for loading XML pages from a site
 * @author Mike Wojcenovich
 */
public class PageLoader {
	
	/**
	 * The client for loading pages
	 */
	private HttpClient client;
	
	/**
	 * The url to the xml file excluding the id
	 */
	private String xmlUrl;
	
	/**
	 * Constructor
	 * @param xmlUrl The url to the xml file excluding the id
	 */
	public PageLoader(String xmlUrl) {
		this.client = new HttpClient();
		this.xmlUrl = xmlUrl;
	}
	
	/**
	 * Get a page from an xml id
	 * @param xmlId The xml id for the XML page to be retrieved
	 * @return A string containing the contents of the XML file
	 */
	public String getPage(String xmlId) {
		GetMethod method = new GetMethod(xmlUrl + xmlId);
		
		try {
			client.executeMethod(method);
			String page = method.getResponseBodyAsString();
			method.releaseConnection();
			return page;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
		
	}
}
