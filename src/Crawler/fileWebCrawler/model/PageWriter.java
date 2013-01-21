package Crawler.fileWebCrawler.model;
//package model;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Class for writing contents to a file
 * @author Mike Wojcenovich
 */
public class PageWriter {

	public PageWriter() {}
	
	/**
	 * Writes an XML file
	 * @param filename The file where the XML should be saved
	 * @param content The content to write to the file
	 */
	public void writeXmlFile(String filename, String content) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
			out.write(content);
			out.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
