package Crawler.fileWebCrawler.model;
//package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

/**
 * Class for reading a id list file
 * @author Mike Wojcenovich
 *
 */
public class IdListFileReader {

	public IdListFileReader() {}
	
	/**
	 * Reads xml ids from a file
	 * @param filename File that contains the xml ids
	 * @return The array of ids
	 */
	public String[] readXmlIdsFromFile(String filename) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			
			Vector<String> idVector = new Vector<String>();
			String line;
			
			while((line = in.readLine()) != null) {
				if(!line.equals(""))
					idVector.add(line);
			}
			
			String[] id = new String[idVector.size()];
			for(int i = 0; i < id.length; i++) {
				id[i] = idVector.elementAt(i);
			}
			
                        //added close routine
                        in.close();

			return id;
		}
		catch(Exception e) {
			return null;
		}
	}
}
