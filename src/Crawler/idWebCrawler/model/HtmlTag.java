package Crawler.idWebCrawler.model;
//package model;

/**
 * Class to represent info on an HTML tag that will
 * be used to get specific information from a Document instance.
 * @author Mike Wojcenovich
 */
public class HtmlTag {

	/**
	 * The name of the tag. i.e. link, a, h1
	 */
	private String name;
	
	/**
	 * An attribute name of the tag. i.e. class, id
	 */
	private String attribute;
	
	/**
	 * Corresponding value or part of the consistent value depnding on the use of the instance,
	 * of the set attribute name.
	 */
	private String attrValue;
	
	/**
	 * Constructor
	 * @param name Name of the tag
	 * @param attribute Attribute name of the tag
	 * @param attrValue Corresponding attribute value of the set tag
	 */
	public HtmlTag(String name, String attribute, String attrValue) {
		this.name = name;
		this.attribute = attribute;
		this.attrValue = attrValue;
	}
	
	/**
	 * @return The name of the tag
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return The attribute name of the tag
	 */
	public String getAttribute() {
		return this.attribute;
	}
	
	/**
	 * @return The attribute value of the tag
	 */
	public String getAttrValue() {
		return this.attrValue;
	}
}