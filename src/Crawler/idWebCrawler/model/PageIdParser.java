package Crawler.idWebCrawler.model;
//package model;

import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class for obtaining XML ids from search result pages.
 * @author Mike Wojcenovich
 */
public class PageIdParser {

	/**
	 * Tag that contains the XML id in its attribute value
	 */
	private HtmlTag xmlIdLocation;
	
	/**
	 * Regular expression to cut unnecessary info from the beginning of the result info
	 */
	private String preXmlIdRegEx;
	
	/**
	 * Regular expression to cut unnecessary info from the end of the result info
	 */
	private String postXmlIdRegEx;
	
	/**
	 * Constructor
	 * @param xmlIdLocation Tag that contains the XML id in its attribute value
	 * @param xmlIdRegEx Regular expressions to cut unnecessary info from result
	 */
	public PageIdParser(HtmlTag xmlIdLocation, String[] xmlIdRegEx) {
		this.xmlIdLocation = xmlIdLocation;
		this.preXmlIdRegEx = xmlIdRegEx[0];
		this.postXmlIdRegEx = xmlIdRegEx[1];
	}
	
	/**
	 * Gets the XML ids from a search result page
	 * @param page The Document containing the structure of a search result page
	 * @return An array of XML ids
	 */
	public String[] getIds(Document page) {
		
		Vector<String> idList = new Vector<String>();
		
		NodeList list = page.getElementsByTagName(xmlIdLocation.getName());
		
		for(int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if(node.getAttributes().getNamedItem(xmlIdLocation.getAttribute()).getNodeValue().contains(xmlIdLocation.getAttrValue())){
				
				String xmlId = node.getAttributes().getNamedItem(xmlIdLocation.getAttribute()).getNodeValue();
				
				xmlId = xmlId.replaceFirst(this.preXmlIdRegEx, "");
				xmlId = xmlId.replaceFirst(this.postXmlIdRegEx, "");
				
				idList.add(xmlId);
			}
		}
		
		String[] xmlId = new String[idList.size()];
		for(int i = 0; i < idList.size(); i++) {
			xmlId[i] = idList.elementAt(i);
		}
		
		return xmlId;
	}
}
