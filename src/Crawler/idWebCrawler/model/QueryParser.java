package Crawler.idWebCrawler.model;
//package model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Parser for getting search result information
 * @author Mike Wojcenovich
 */
public class QueryParser {

	/**
	 * The tag that contains the number of results for the search
	 */
	private HtmlTag resultNumberLocation;
	
	/**
	 * Regular expression to cut unnecessary info from the beginning of the result number
	 */
	private String preResultNumberRegEx;
	
	/**
	 * Regular expression to cut unnecessary info from the end of the result number
	 */
	private String postResultNumberRegEx;
	
	/**
	 * The tag that contains the query id in its attribute value
	 */
	private HtmlTag queryIdLocation;
	
	/**
	 * Regular expression to cut unnecessary info from the beginning of the query id
	 */
	private String preQueryIdRegEx;
	
	/**
	 * Regular expression to cut unnecessary info from the end of the query id
	 */
	private String postQueryIdRegEx;
	
	/**
	 * Constructor
	 * @param resultNumberLocation The tag that contains the number of results for the search
	 * @param resultNumberRegEx Regular expressions to cut unnecessary info from the result number
	 * @param queryIdLocation The tag that contains the query id for the search
	 * @param queryIdRegEx Regular expressions to cut unnecessary info from the query id
	 */
	public QueryParser(HtmlTag resultNumberLocation, String[] resultNumberRegEx,
					   HtmlTag queryIdLocation, String[] queryIdRegEx) {
		this.resultNumberLocation = resultNumberLocation;
		this.preResultNumberRegEx = resultNumberRegEx[0];
		this.postResultNumberRegEx = resultNumberRegEx[1];
		this.queryIdLocation = queryIdLocation;
		this.preQueryIdRegEx = queryIdRegEx[0];
		this.postQueryIdRegEx = queryIdRegEx[1];
	}
	
	/**
	 * @param page The first page of a search query
	 * @return The number of results for this search query
	 */
	public int getPageResults(Document page) {
		
		NodeList list = page.getElementsByTagName(resultNumberLocation.getName());
		for(int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if(node.getAttributes().getNamedItem(resultNumberLocation.getAttribute()).getNodeValue().equals(resultNumberLocation.getAttrValue())){
				
				Element resultElement = (Element)node;
				NodeList resultChildNodes = resultElement.getChildNodes();
				
				String resultString	= (resultChildNodes.item(0)).getNodeValue();
				
				resultString = resultString.replaceFirst(this.preResultNumberRegEx, "");
				resultString = resultString.replaceFirst(this.postResultNumberRegEx, "");
				
				int results = Integer.parseInt(resultString);
				
				return results;
			}
		}
		
		return 0;
	}
	
	/**
	 * @param page The first page of a search query
	 * @return The query id for this search query
	 */
	public String getQueryId(Document page) {
		
		NodeList list = page.getElementsByTagName(queryIdLocation.getName());
		for(int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if(node.getAttributes().getNamedItem(queryIdLocation.getAttribute()).getNodeValue().contains(queryIdLocation.getAttrValue())){
				
				String queryId = node.getAttributes().getNamedItem(queryIdLocation.getAttribute()).getNodeValue();
				
				queryId = queryId.replaceFirst(this.preQueryIdRegEx, "");
				queryId = queryId.replaceFirst(this.postQueryIdRegEx, "");
				
				return queryId;
			}
		}
		
		return null;
	}
	
	/* Unused method that might be implemented later if there is time
	private String findTag(NodeList currentTagList, HtmlTag[] tag, int tagIndex) {
		for(int i = 0; i < currentTagList.getLength(); i++) {
			
			Node tagNode = currentTagList.item(i);
			if(tagNode.getAttributes().getNamedItem(tag[tagIndex].getAttribute()) != null) {
				if(tagNode.getAttributes().getNamedItem(tag[tagIndex].getAttribute()).equals(tag[tagIndex].getAttrValue())) {
					if(tagIndex == (tag.length - 1)) {
						return tagNode.getNodeValue();
					}
					else{
						Element tagElement = (Element)tagNode;
						NodeList newTagList = tagElement.getElementsByTagName(tag[tagIndex+1].getName());
						String result = findTag(newTagList, tag, tagIndex+1);
						if(result != null) {
							return result;
						}
					}
				}
			}	
		}
		
		return null;
	}
	*/
}