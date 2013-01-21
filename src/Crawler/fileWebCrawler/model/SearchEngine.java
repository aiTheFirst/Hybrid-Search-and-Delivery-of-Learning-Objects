package Crawler.fileWebCrawler.model;
//package model;

// Jarrett G. Steele - need io for creating directory.
import java.io.*;

/**
 * Class for handling getting and saving all XML files from a site
 * @author mike
 *
 */
public class SearchEngine {
	
	/**
	 * The name of the site to use in the xml file name
	 */
	private String siteName;
	
	/**
	 * The file that contains the xml ids
	 */
	private String idListFilename;
	
	/**
	 * For loading XML files from the site
	 */
	private PageLoader loader;
	
	/**
	 * For writing XML files
	 */
	private PageWriter writer;

        //added for threading
        // Jarrett G. Steele
        public volatile boolean stopThread = false;

        //added directory for organizing repositories
        public String directory = null;



	/**
	 * Constructor
	 * @param siteName The name of the site to use in the XML filenames
	 * @param idListFilename The file containing ids for this site
	 * @param xmlUrl The url to the xml files excluding the id
         *
         * Jarrett G. Steele - added parameter to constructor to take directory name to store repository
	 */
	public SearchEngine(String siteName, String idListFilename, String xmlUrl, String repoDirectory ) {
		this.siteName = siteName;
		this.idListFilename = idListFilename;
		
		this.loader = new PageLoader(xmlUrl);
		this.writer = new PageWriter();

                //Jarrett G. Steele
                this.directory = repoDirectory;
	}
	
	/**
	 * Save XML files from this site to the computer
	 */
	public void createXmlFiles() {

                /* Jarrett G. Steele
                 * added code to create directory for organizing multiple repositories
                 */
                boolean success = (new File(directory)).mkdir();
                if(! success ){
                    /* oh no can't create directory.., what to do? */
                    return;
                }

		IdListFileReader reader = new IdListFileReader();
		String[] id = reader.readXmlIdsFromFile(idListFilename);
		if(id == null) {
			System.out.println("Invalid filename or filename contained no xml ids for the file " + idListFilename);
			return;
		}
		
		for(int i = 0; i < id.length; i++) {
                        // Jarrett G. Steele - added some debug output..
                        System.out.println("writing xml lom for id: " + id[i]);


                        // slow down process so that needs.org doesn't forbid futher requests. Jarrett G. Steele.
                        try{
                            Thread.sleep(2000);
                        }
                        catch(Exception e){
                            
                        }

			String page = loader.getPage(id[i]);
			if(id[i] != null) {
				writer.writeXmlFile(directory + "\\" + siteName + "-" + id[i] + ".xml", page);
			}

                        // added for threading
                        if( stopThread ) return;
		}
	}
}
