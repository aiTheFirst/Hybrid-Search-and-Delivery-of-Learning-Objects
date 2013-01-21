package Crawler.idWebCrawler.model;
//package model;

import org.apache.commons.httpclient.NameValuePair;

/**
 * Class to represent an input field for a search form
 * @author Mike Wojcenovich
 */
public class InputField {

	/**
	 * Name of the search field. The name attribute value of an input tag.
	 */
	private String name;
	
	/**
	 * An array values to execute in this field
	 */
	private String[] value;
	
	/**
	 * The default value to use for this field
	 */
	private String defaultVal;
	
	/**
	 * Constructor
	 * @param name The name attribute of the input tag for this field
	 * @param value An array of values to execute in this field
	 * @param defaultVal The default value to use in this field
	 */
	public InputField(String name, String[] value, String defaultVal) {
		this.name = name;
		this.value = value;
		this.defaultVal = defaultVal;
	}
	
	/**
	 * Generates a NameValuePair instance
	 * @param index The index in the array of values to use, if instance is greater than array length then default value is used
	 * @return A NameValuePair instance
	 */
	public NameValuePair generateNameValuePair(int index) {
		if(index >= value.length) {
			return new NameValuePair(name, defaultVal);
		}
		
		return new NameValuePair(name, value[index]);
	}
	
	/**
	 * @return The length of the value array
	 */
	public int getValueSize() {
		return value.length;
	}
}
