package Crawler.fileWebCrawler;

import java.io.BufferedReader;
import java.io.InputStreamReader;

//import model.SearchEngine;
import Crawler.fileWebCrawler.model.*;

public class FileWebCrawlerApp extends Thread {

        /**
	 *  July 2009 - Class now executes as thread, req'd for merge into invoker project.
         *
         * Program also takes a directory string for storing repository, keep multiple repo's organized
         * instead of being foolishly dumped into the project root directory.         *
         *
	 *  Jarrett G. Steele
	 */
        public static SearchEngine search = null;
        public static String filename = "needs.txt";
        public static String directory = null;
        
        public void run(){
                FileWebCrawlerApp.main(null); // execute as intended by Mike.
	}

	public static void main(String[] args) {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.print("Enter the name of the file containing the XML ids for needs.org: ");
                        //modified for threading
			//String filename = in.readLine();
			//in.close();

                        // modified for threading
			/*SearchEngine*/ search = new SearchEngine("needs", filename, "http://www.needs.org:80/smete/public/learning_objects/action/export_lom.jhtml?id=",directory);
			search.createXmlFiles();
			
			System.out.println("XML files where successfully created.");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
