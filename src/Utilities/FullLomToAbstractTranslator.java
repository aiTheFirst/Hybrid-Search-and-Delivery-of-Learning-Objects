/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Utilities;

import java.io.*;

import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Jarrett G. Steele
 * EE4613 - August 2009
 *
 * At this point in the invoker project, an issue exists with the format of xml lom files
 * The crawler outputs "full xml lom" - the xml files are complete with all tags and attributes obtained from needs.org
 * The LOSearch program was written to use plain text files containing only the abstract portion of the full xml lom.
 * The Personalization program was written to use "simplified xml lom" - xml files having fewer tags and attributes when compared to the full xml lom; conversion from fullt to simplified was done manually.
 *
 * To solve the disconnect between xml lom format among these processes, I've contiuned the hackery with a translator that will convert full xml lom into both plain text abstracts and simplified xml lom. thus satisfing each program.
 *
 * I expect to find the abstract in xml tree, under "general"->"description"->"string".
 *
 */
public class FullLomToAbstractTranslator extends DefaultHandler{


    public static String repoDir = null;

    public static boolean stopThread = false;

    public static boolean foundDesc = false;
    public static boolean inGeneral = false;

    public static String[] filenames = null;
    public static String currentFileName = null;
    public static File dir = null;
    
    public static String outputDir = null;
    public static FileReader r = null;

    public static StringBuffer sb = null; // parser returns chunks when data large, need to build it up.

    public static void closeFileReader(){
        try{
        if( r != null){
            r.close();
        }
        }
        catch(Exception e){
            //oops.
            return;
        }
    }

    /*
     * arg[0] = repo directory of full lom xml
     * arg[1] = output directory within repo directory for abstracts for these full lom
     */
    public static void main (String args[])
	throws Exception
    {
	XMLReader xr = XMLReaderFactory.createXMLReader();
	FullLomToAbstractTranslator handler = new FullLomToAbstractTranslator();
	xr.setContentHandler(handler);
	xr.setErrorHandler(handler);

        //open the directory
        dir = new File(args[0]);

        outputDir = args[1];
        /* create output dir */
        File outDir = new File(dir.toString() + "\\" + outputDir);
        outDir.mkdir();

        
        
        





        //get list of files.
        filenames = dir.list();

        //for each full xml file in this repo directory, do translation into plain abstract text.
        for(int i = 0; i < filenames.length; i++){
            currentFileName = filenames[i];

            //only parse .xml files.

            if( filenames[i].endsWith(".xml") ){

                System.out.println("attempting to parse file: " + args[0] + "\\" + filenames[i]);

                if( stopThread ) return;

                r = new FileReader(args[0] + "\\" + filenames[i]);
                xr.parse(new InputSource(r));
                r.close();
            }
        }
    }


    public FullLomToAbstractTranslator()
    {
        super();

    }




    ////////////////////////////////////////////////////////////////////
    // Event handlers.
    ////////////////////////////////////////////////////////////////////


    public void startDocument ()
    {
	//System.out.println("Start document");

    }


    public void endDocument ()
    {
	//System.out.println("End document");
    }


    public void startElement (String uri, String name,
			      String qName, Attributes atts)
    {

        sb = new StringBuffer(); //reset buffer for new element
        /*
	if ("".equals (uri))
	    System.out.println("Start element: " + qName);
	else
	    System.out.println("Start element: {" + uri + "}" + name);
        */
        /* find the tag titled general */
        if(name.toLowerCase().equals("general")){
            inGeneral = true;
        }
        if(inGeneral && name.toLowerCase().equals("description")){
            foundDesc = true;
        }

    }


    public void endElement (String uri, String name, String qName)
    {
        /*
	if ("".equals (uri))
	    System.out.println("End element: " + qName);
	else
	    System.out.println("End element:   {" + uri + "}" + name);
        */


        if(inGeneral && foundDesc){
            //String allchar = new String(ch);
            //String characters = allchar.substring(start, start + length);
            //System.out.println("EQUIV?:\t" + characters );

            //got the string containing the abstract.
            //write abstact under same name, but .txt
            String newFileName = currentFileName.substring(0, (currentFileName.length()-3) ).concat("txt");

            try{
                FileWriter fw = new FileWriter(dir.toString() + "\\" + outputDir + "\\" + newFileName);
                fw.write(sb.toString());
                fw.close();
            }
            catch(Exception e){
                //can't write file, give up.
                return;
            }

            foundDesc = false;//reset
        }

        if(name.toLowerCase().equals("general")){
            inGeneral = false;
        }
    }


    public void characters (char ch[], int start, int length)
    {
	//System.out.print("Characters:    \"");
        /*
	for (int i = start; i < start + length; i++) {
	    switch (ch[i]) {
	    case '\\':
		System.out.print("\\\\");
		break;
	    case '"':
		System.out.print("\\\"");
		break;
	    case '\n':
		System.out.print("\\n");
		break;
	    case '\r':
		System.out.print("\\r");
		break;
	    case '\t':
		System.out.print("\\t");
		break;
	    default:
		System.out.print(ch[i]);
		break;
	    }
	}
	System.out.print("\"\n");
         */
        String allchar = new String(ch);
        String characters = allchar.substring(start, start + length);
        sb.append(characters); // add to buffer

        

    }


}