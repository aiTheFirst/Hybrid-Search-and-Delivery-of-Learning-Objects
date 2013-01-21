package Crawler.idWebCrawler.model;
//package model;

import org.apache.commons.httpclient.NameValuePair;

/**
 * A class to represent all of the fields of a search form
 * @author Mike Wojcenovich
 */
public class InputFieldSet {

	/**
	 * An array of InputField instances
	 */
	private InputField[] input;
	
	/**
	 * The number of value sets based on the largest value array length in the given InputField instances
	 */
	private int setSize;
	
	/**
	 * Constructor
	 * @param input The array of InputField instances
	 */
	public InputFieldSet(InputField[] input) {
		this.input = input;
		this.setSize = this.constructSetSize();
	}
	
	/**
	 * Gets the largest value array length by checking the array length of every InputField in the array.
	 * @return The largest value array length.
	 */
	private int constructSetSize() {
		int max = input[0].getValueSize();
		for(int i = 1; i < input.length; i++) {
			if(input[i].getValueSize() > max)
				max = input[i].getValueSize();
		}
		return max;
	}
	
	/**
	 * @return The number of value sets
	 */
	public int getSetSize() {
		return setSize;
	}
	
	/**
	 * @param index The index of value array to use in all InputField instances
	 * @return An array of NameValuePair instances
	 */
	public NameValuePair[] createQuerySet(int index) {
		if(index >= setSize) return null;
		
		NameValuePair[] nameValue = new NameValuePair[input.length];
		for(int i = 0; i < input.length; i++) {
			nameValue[i] = input[i].generateNameValuePair(index);
		}
		
		return nameValue;
	}
}
