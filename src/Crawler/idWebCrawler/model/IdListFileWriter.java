package Crawler.idWebCrawler.model;
//package model;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Writes XML ids to a file for later use
 * @author Mike Wojcenovich
 */
public class IdListFileWriter {

	/**
	 * Constructor
	 */
	public IdListFileWriter() { }
	
	/**
	 * Writes XML ids to a file
	 * @param id An array of XML ids
	 * @param filename The name of the file to save the XML ids to
	 */
	public void writeXmlIdsToFile(String[] id, String filename) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
			for(int i = 0; i < id.length; i++) {
				out.write(id[i] + "\n");
			}
			
			out.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
