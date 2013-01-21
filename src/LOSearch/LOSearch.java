package LOSearch;
import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import java.io.*;
import java.util.*;


public class LOSearch extends Thread {

        /**
	 *  July 2009 - Class now executes as thread, req'd for merge into invoker project.
	 *  Jarrett G. Steele
	 */
        public void run() {
                LOSearch.main(null); // execute as intended by Hamid.
	}

        //added for threading
        public static volatile boolean stopThread = false;

        public static String query = null;
        public static String abstractDir = null;

        // store for sorted results
        public static String[] conceptResults = null;
        public static String[] keywordResults = null;

        //control for auto detect results available
        public static boolean lOSearchComplete = false;

	public static void main(String[] args) {

            try {
		System.setProperty("wordnet.database.dir", "./data/dict");
		System.setProperty("wvtool.wnconfig", "./file_properties.xml");
		
		/* Jarrett G. Steele
                 *
                 * String[] docs changed to be abstracts of some repository.
                 *
                 * need directory of repository
                 */

                // open abstract directory and create String[] docs with content
                File abstracts = new File(abstractDir);

                String[] abstractNames = abstracts.list();

                String[] docs = new String[abstractNames.length];

                for(int i = 0; i < abstractNames.length; i++ ){
                    docs[i] = abstractDir + "\\" + abstractNames[i];
                }



		/* ORIGINAL.
                  String[] docs = { "./data/dataset/001.txt" ,
				"./data/dataset/002.txt" ,
				"./data/dataset/003.txt" ,
				"./data/dataset/004.txt" ,
				"./data/dataset/005.txt" ,
				"./data/dataset/006.txt" ,
				"./data/dataset/007.txt" ,
				"./data/dataset/008.txt" ,
				"./data/dataset/009.txt" ,
				"./data/dataset/010.txt" ,
				"./data/dataset/011.txt" ,
				"./data/dataset/012.txt" ,
				"./data/dataset/013.txt" ,
				"./data/dataset/014.txt" ,
				"./data/dataset/015.txt" ,
				"./data/dataset/016.txt" ,
				"./data/dataset/017.txt" ,
				"./data/dataset/018.txt" ,
				"./data/dataset/019.txt" ,
				"./data/dataset/020.txt" ,
				"./data/dataset/021.txt" ,
				"./data/dataset/022.txt" ,
				"./data/dataset/023.txt" ,
				"./data/dataset/024.txt" ,
				"./data/dataset/025.txt" ,
				"./data/dataset/026.txt" ,
				"./data/dataset/027.txt" ,
				"./data/dataset/028.txt" ,
				"./data/dataset/029.txt" ,
				"./data/dataset/030.txt" ,
				"./data/dataset/031.txt" ,
				"./data/dataset/032.txt" ,
				"./data/dataset/033.txt" ,
				"./data/dataset/034.txt" ,
				"./data/dataset/035.txt" ,
				"./data/dataset/036.txt" ,
				"./data/dataset/037.txt" ,
				"./data/dataset/038.txt" ,
				"./data/dataset/039.txt" ,
				"./data/dataset/040.txt" ,
				"./data/dataset/041.txt" ,
				"./data/dataset/042.txt" ,
				"./data/dataset/043.txt" ,
				"./data/dataset/044.txt" ,
				"./data/dataset/045.txt" ,
				"./data/dataset/046.txt" ,
				"./data/dataset/047.txt" ,
				"./data/dataset/048.txt" ,
				"./data/dataset/049.txt" ,
				"./data/dataset/050.txt" ,
				"./data/dataset/051.txt" ,
				"./data/dataset/052.txt" ,
				"./data/dataset/053.txt" ,
				"./data/dataset/054.txt" ,
				"./data/dataset/055.txt" ,
				"./data/dataset/056.txt" ,
				"./data/dataset/057.txt" ,
				"./data/dataset/058.txt" ,
				"./data/dataset/059.txt" ,
				"./data/dataset/060.txt" ,
				"./data/dataset/061.txt" ,
				"./data/dataset/062.txt" ,
				"./data/dataset/063.txt" ,
				"./data/dataset/064.txt" ,
				"./data/dataset/065.txt" ,
				"./data/dataset/066.txt" ,
				"./data/dataset/067.txt" ,
				"./data/dataset/068.txt" ,
				"./data/dataset/069.txt" ,
				"./data/dataset/070.txt" 		
		};
                 */

                //this query found to be not used; instead read from file named "query.txt" in both Concept & Keyword Vecorization classes.
                // Jarrett G. Steele.
		String query = "course";

                //Added for thread control
                ConceptVectorization.stopThread = LOSearch.stopThread;
                KeywordVectorization.stopThread = LOSearch.stopThread;

		double[] r_keyword = ConceptVectorization.start(docs, query);
                //Added for thread control
                ConceptVectorization.stopThread = LOSearch.stopThread;
                KeywordVectorization.stopThread = LOSearch.stopThread;

		double[] r_concept = KeywordVectorization.start(docs, query);
		
		double[] r1 = new double[r_keyword.length];
		double[] r2 = new double[r_keyword.length];
		double[] r3 = new double[r_keyword.length];
		double[] r4 = new double[r_keyword.length];
		double[] r5 = new double[r_keyword.length];
		double[] r6 = new double[r_keyword.length];
		double[] r7 = new double[r_keyword.length];
		double[] r8 = new double[r_keyword.length];
		double[] r9 = new double[r_keyword.length];

                // r1 of interest for keyword, r6 of interest for keyword+concept, JGS
		for ( int i = 0 ; i < r_keyword.length ; ++i )
		{
			r1[i] = r_keyword[i];
			//r2[i] = r_keyword[i] * 0.125 + r_concept[i] * 0.875;
			//r3[i] = r_keyword[i] * 0.25 + r_concept[i] * 0.75;
			//r4[i] = r_keyword[i] * 0.375 + r_concept[i] * 0.625;
			//r5[i] = r_keyword[i] * 0.5 + r_concept[i] * 0.5;
			r6[i] = r_keyword[i] * 0.625 + r_concept[i] * 0.375;			
			//r7[i] = r_keyword[i] * 0.75 + r_concept[i] * 0.25;
			//r8[i] = r_keyword[i] * 0.875 + r_concept[i] * 0.125;
			//r9[i] = r_concept[i];

                        if(stopThread) return;
		}

                /* REmove unneeded output. Jarrett G. Steele */
                /*
		for (double d : r1)
		   System.out.print(d+" , ");
		
		System.out.println();
		for (double d : r2)
			   System.out.print(d+" , ");
		System.out.println();
                 */

                /* only r6 is of interest */

                /*
		System.out.println("\n--------r1:");
		*/
                keywordResults = printbyorder(docs, r1);
                
                /*
		System.out.println("\n--------r2:");
		printbyorder(docs, r2);
		System.out.println("\n--------r3:");
		printbyorder(docs, r3);
		System.out.println("\n--------r4:");
		printbyorder(docs, r4);
		System.out.println("\n--------r5:");
		printbyorder(docs, r5);
                */

		//System.out.println("\n--------r6:");
		conceptResults = printbyorder(docs, r6);
		
                lOSearchComplete = true;



                /*
                System.out.println("\n--------r7:");
		printbyorder(docs, r7);
		System.out.println("\n--------r8:");
		printbyorder(docs, r8);
		System.out.println("\n--------r9:");
		printbyorder(docs, r9);
                */

               
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }

            
            
            
	}
        /* JGS - modified to return String[] results, instead of void.
         */
	public static String[] printbyorder(String[] docs,double[] r)
	{
                //results = new String[r.length];
                String[] ret = new String[r.length];

		for (int j = 0 ; j < r.length ; ++j )
		{
			double max = -1.0;
			int maxIndex = -1;
			for ( int i = 0 ; i < r.length ; ++i )
				if ( r[i] > max )
				{
					max = r[i];
					maxIndex = i;
				}

                        java.text.DecimalFormat format = new java.text.DecimalFormat("0.00000");
                        String s = format.format(r[maxIndex]);
                        //output relevance + tab + document name
                        ret[j] = s + "  " + docs[maxIndex];


                        
                        
			System.out.println(docs[maxIndex]);
			r[maxIndex] = -1.0;

                       

                        //added for threading control. JGS
                        if(stopThread) return null;
		}
                return ret;
	}

        /* Modified to return results to GUI - Jarrett G. Steele 
        public static void getOrderedResults(String[] docs,double[] r)
	{
                results = new String[r.length];

		for (int j = 0 ; j < r.length ; ++j )
		{
			double max = -1.0;
			int maxIndex = -1;
			for ( int i = 0 ; i < r.length ; ++i )
				if ( r[i] > max )
				{
					max = r[i];
					maxIndex = i;
				}

			System.out.println(docs[maxIndex]);
                        results[j] = docs[maxIndex];
			r[maxIndex] = -1.0;

                        //added for threading control.
                        if(stopThread) return;
		}

                
	}*/

}
