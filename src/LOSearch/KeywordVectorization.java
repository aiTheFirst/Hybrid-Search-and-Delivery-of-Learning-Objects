package LOSearch;

import java.io.FileWriter;

import javax.swing.text.Utilities;

import edu.udo.cs.wvtool.config.WVTConfiguration;
import edu.udo.cs.wvtool.config.WVTConfigurationFact;
import edu.udo.cs.wvtool.external.LovinsStemmer;
import edu.udo.cs.wvtool.generic.output.WordVectorWriter;
import edu.udo.cs.wvtool.generic.stemmer.PorterStemmerWrapper;
import edu.udo.cs.wvtool.generic.stemmer.WVTStemmer;
import edu.udo.cs.wvtool.generic.stemmer.WordNetHypernymStemmer;
import edu.udo.cs.wvtool.generic.stemmer.WordNetSynonymStemmer;
import edu.udo.cs.wvtool.generic.vectorcreation.TFIDF;
import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.main.WVTFileInputList;
import edu.udo.cs.wvtool.main.WVTWordVector;
import edu.udo.cs.wvtool.main.WVTool;
import edu.udo.cs.wvtool.wordlist.WVTWordList;


public class KeywordVectorization {

     public static volatile boolean stopThread = false;

	public static double[] start(String[] docs, String query) throws Exception
	{

        // Initialize the WVTool
        WVTool wvt = new WVTool(false);
//        WVTool wvt = new MyWVTool(false);

        // Initialize the configuration
        WVTConfiguration config = new WVTConfiguration();


//        WVTStemmer stemmer = new SynsetStemmer();
        WVTStemmer stemmer = new PorterStemmerWrapper();

        config.setConfigurationRule(WVTConfiguration.STEP_STEMMER, new WVTConfigurationFact(stemmer));

        // Initialize the input list with two classes
        WVTFileInputList list = new WVTFileInputList(docs.length);

        // Add entries
        int i = 0;
        list.addEntry(new WVTDocumentInfo("query.txt", "txt", "", "english", i++));
        for ( String doc : docs )
            list.addEntry(new WVTDocumentInfo(doc, "txt", "", "english", i++));

        // Generate the word list

        WVTWordList wordList = wvt.createWordList(list, config);

        /* threading control
         * Jarrett G. Steele
         */
        if(stopThread) return null;



        // Prune the word list

//        wordList.pruneByFrequency(2, 5);

        // Alternativ I: read an already created word list from a file
        // WVTWordList wordList2 =
        // new WVTWordList(new FileReader("/home/wurst/tmp/wordlisttest.txt"));

        // Alternative II: Use predifined dimensions
        // List dimensions = new Vector();
        // dimensions.add("atheist");
        // dimensions.add("christian");
        // wordList =
        // wvt.createWordList(list, config, dimensions, false);

        // Store the word list in a file
//        wordList.
        wordList.store(new FileWriter("wordlist_keyword.txt"));

        // Create the word vectors

        // Set up an output filter (write sparse vectors to a file)
        WordVectorMemoryWriter wvw = new WordVectorMemoryWriter();

        config.setConfigurationRule(WVTConfiguration.STEP_OUTPUT, new WVTConfigurationFact(wvw));

        config.setConfigurationRule(WVTConfiguration.STEP_VECTOR_CREATION, new WVTConfigurationFact(new TFIDF()));

        // Create the vectors
        wvt.createVectors(list, config, wordList);

        // Alternatively: create word list and vectors together
        // wvt.createVectors(list, config);

        // Close the output file
        // Just for demonstration: Create a vector from a String
        WVTWordVector q = wvt.createVector(query, wordList);
        
        double[] relevancies = new double[docs.length];
        for ( int j = 0 ; j < docs.length ; ++j ){
        	relevancies[j] = Untilities.dotproduct(wvw.getList().get(0), wvw.getList().get(j+1));
                /* threading control
                 * Jarrett G. Steele
                 */
                if(stopThread) return null;

        }
		System.out.println("query");
		for ( double d : q.getValues() ){
			System.out.print(d+" ");
                        /* threading control
                         * Jarrett G. Steele
                         */
                        if(stopThread) return null;

                }
		System.out.println();
       
        return relevancies;
	}
	
	public static void main(String args[]) 
	{
		System.setProperty("wordnet.database.dir", "./data/dict");
		try {
//			start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
