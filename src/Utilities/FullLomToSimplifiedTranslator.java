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

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

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
 * Consider doing this translation, from one form of xml to another, with XSLT; it would be better to use a standard method with a ruleset.
 *
 */
public class FullLomToSimplifiedTranslator extends DefaultHandler{


    public static String repoDir = null;

    public static boolean stopThread = false;

    public static String[] filenames = null;
    public static String currentFileName = null;
    public static File dir = null;

    public static FileReader r = null;
    public static FileWriter fw = null;

    /* these fields will be populated by the parser, then they can be written to the simplified xml file */
    public static String title = null;
    public static String description = null;
    public static String content_language = null;
    public static String format = null;
    public static String requirement = "DEFAULT";                // not in full XML.
    public static String other_platform_requirement = null;
    public static String duration = "DEFAULT";                   // not in full XML.
    public static String interactivity_type = null;
    public static String intended_user_language = "DEFAULT";     // not in full XML.
    public static String interactivity_level = null;
    public static String context = "DEFAULT";                    // not in full XML.
    public static String typical_age_range_min = "DEFAULT";      // not in full XML.
    public static String typical_age_range_max = "DEFAULT";      // not in full XML.
    public static String difficulty = null;
    public static String typical_learning_time = "DEFAULT";      // not in full XML.
    public static String taxon_path_concept_and_discipline = "DEFAULT";  // not in full XML.
    public static String taxon_path_skilllevel_prerequisite = "DEFAULT"; // not in full XML.
    public static String taxon_path_idea_discipline = "DEFAULT";         // not in full XML.
    public static String intended_end_user_role = null;

    /* control variables for parsing */
    public static boolean inGeneral = false;
    public static boolean inEducational = false;
    public static boolean inTechnical = false;
    public static boolean foundTitle = false;
    public static boolean foundDescription = false;
    public static boolean foundContentLanguage = false;
    public static boolean foundFormat = false;
    public static boolean foundOtherPlatformRequirement = false;
    public static boolean foundInteractivityType = false;
    public static boolean foundInteractivityLevel = false;
    public static boolean foundDifficulty = false;
    public static boolean foundIntendedEndUserRole = false;

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
     * args[0] = repo directory of full lom xmls
     * args[1] = output directory within repo dir for simplified lom xml
     */
    public static void main (String args[])
	throws Exception
    {
	XMLReader xr = XMLReaderFactory.createXMLReader();
	FullLomToSimplifiedTranslator handler = new FullLomToSimplifiedTranslator();
	xr.setContentHandler(handler);
	xr.setErrorHandler(handler);

        //open the directory
        dir = new File(args[0]);

        File outDir = new File(dir.toString() + "\\" + args[1]);
        outDir.mkdir();


        //get list of files.
        filenames = dir.list();

        //for each full xml file in this repo directory, do translation into simplified xml lom.
        for(int i = 0; i < filenames.length; i++){
            currentFileName = filenames[i];

            //only parse .xml files, don't parse the simplified ones either.
            if( filenames[i].endsWith(".xml") && !filenames[i].endsWith("_SIMP.xml") ){

                if( stopThread ){
                    System.out.println("thread termination requested.");
                    return;
                }
                
                System.out.println("Simplified Translator: attempting to parse file: " + args[0] + "\\" + filenames[i]);

                //open new filename to write simplified xml.
                //write abstact under same name, but suffix is _SIMP.xml
                String newFileName = currentFileName.substring(0, (currentFileName.length()-4) ).concat("_SIMP.xml");
                fw = new FileWriter(dir.toString() + "\\" + args[1] + "\\" + newFileName);

                r = new FileReader(args[0] + "\\" + filenames[i]);
                xr.parse(new InputSource(r));

                // at this point the parser has populated the required fields to create a simplified XML file.
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.newDocument();

                // add root
                Element rootElement = document.createElement("Attributes");
                document.appendChild(rootElement);

                Element a = null;
                Element b = null;
                Element c = null;

                a = document.createElement("Attribute");
                b = document.createElement("Name");
                b.appendChild(document.createTextNode("title"));
                c = document.createElement("Value");
                c.appendChild(document.createTextNode(title));
                a.appendChild(b);
                a.appendChild(c);
                rootElement.appendChild(a); // add title to dom.

                a = document.createElement("Attribute");
                b = document.createElement("Name");
                b.appendChild(document.createTextNode("description"));
                c = document.createElement("Value");
                c.appendChild(document.createTextNode(description));
                a.appendChild(b);
                a.appendChild(c);
                rootElement.appendChild(a); // add description to dom.

                a = document.createElement("Attribute");
                b = document.createElement("Name");
                b.appendChild(document.createTextNode("content_language"));
                c = document.createElement("Value");
                c.appendChild(document.createTextNode(content_language));
                a.appendChild(b);
                a.appendChild(c);
                rootElement.appendChild(a); // add content_language to dom.

                a = document.createElement("Attribute");
                b = document.createElement("Name");
                b.appendChild(document.createTextNode("format"));
                c = document.createElement("Value");
                c.appendChild(document.createTextNode(format));
                a.appendChild(b);
                a.appendChild(c);
                rootElement.appendChild(a); // add format to dom.

                a = document.createElement("Attribute");
                b = document.createElement("Name");
                b.appendChild(document.createTextNode("requirement"));
                c = document.createElement("Value");
                c.appendChild(document.createTextNode(requirement));
                a.appendChild(b);
                a.appendChild(c);
                rootElement.appendChild(a); // add requirement to dom.

                a = document.createElement("Attribute");
                b = document.createElement("Name");
                b.appendChild(document.createTextNode("other_platform_requirement"));
                c = document.createElement("Value");
                c.appendChild(document.createTextNode(other_platform_requirement));
                a.appendChild(b);
                a.appendChild(c);
                rootElement.appendChild(a); // add other_platform_requirement to dom.

                a = document.createElement("Attribute");
                b = document.createElement("Name");
                b.appendChild(document.createTextNode("duration"));
                c = document.createElement("Value");
                c.appendChild(document.createTextNode(duration));
                a.appendChild(b);
                a.appendChild(c);
                rootElement.appendChild(a); // add duration to dom.

                a = document.createElement("Attribute");
                b = document.createElement("Name");
                b.appendChild(document.createTextNode("interactivity_type"));
                c = document.createElement("Value");
                c.appendChild(document.createTextNode(interactivity_type));
                a.appendChild(b);
                a.appendChild(c);
                rootElement.appendChild(a); // add interactivity_type to dom.

                a = document.createElement("Attribute");
                b = document.createElement("Name");
                b.appendChild(document.createTextNode("intended_user_language"));
                c = document.createElement("Value");
                c.appendChild(document.createTextNode(intended_user_language));
                a.appendChild(b);
                a.appendChild(c);
                rootElement.appendChild(a); // add intended_user_language to dom.

                a = document.createElement("Attribute");
                b = document.createElement("Name");
                b.appendChild(document.createTextNode("interactivity_level"));
                c = document.createElement("Value");
                c.appendChild(document.createTextNode(interactivity_level));
                a.appendChild(b);
                a.appendChild(c);
                rootElement.appendChild(a); // add interactivity_level to dom.

                a = document.createElement("Attribute");
                b = document.createElement("Name");
                b.appendChild(document.createTextNode("context"));
                c = document.createElement("Value");
                c.appendChild(document.createTextNode(context));
                a.appendChild(b);
                a.appendChild(c);
                rootElement.appendChild(a); // add context to dom.

                a = document.createElement("Attribute");
                b = document.createElement("Name");
                b.appendChild(document.createTextNode("typical_age_range_min"));
                c = document.createElement("Value");
                c.appendChild(document.createTextNode(typical_age_range_min));
                a.appendChild(b);
                a.appendChild(c);
                rootElement.appendChild(a); // add typical_age_range_min to dom.

                a = document.createElement("Attribute");
                b = document.createElement("Name");
                b.appendChild(document.createTextNode("typical_age_range_max"));
                c = document.createElement("Value");
                c.appendChild(document.createTextNode(typical_age_range_max));
                a.appendChild(b);
                a.appendChild(c);
                rootElement.appendChild(a); // add typical_age_range_max to dom.

                a = document.createElement("Attribute");
                b = document.createElement("Name");
                b.appendChild(document.createTextNode("difficulty"));
                c = document.createElement("Value");
                c.appendChild(document.createTextNode(difficulty));
                a.appendChild(b);
                a.appendChild(c);
                rootElement.appendChild(a); // add difficulty to dom.

                a = document.createElement("Attribute");
                b = document.createElement("Name");
                b.appendChild(document.createTextNode("typical_learning_time"));
                c = document.createElement("Value");
                c.appendChild(document.createTextNode(typical_learning_time));
                a.appendChild(b);
                a.appendChild(c);
                rootElement.appendChild(a); // add typical_learning_time to dom.

                a = document.createElement("Attribute");
                b = document.createElement("Name");
                b.appendChild(document.createTextNode("taxon_path_concept_and_discipline"));
                c = document.createElement("Value");
                c.appendChild(document.createTextNode(taxon_path_concept_and_discipline));
                a.appendChild(b);
                a.appendChild(c);
                rootElement.appendChild(a); // add taxon_path_concept_and_discipline to dom.

                a = document.createElement("Attribute");
                b = document.createElement("Name");
                b.appendChild(document.createTextNode("taxon_path_skilllevel_prerequisite"));
                c = document.createElement("Value");
                c.appendChild(document.createTextNode(taxon_path_skilllevel_prerequisite));
                a.appendChild(b);
                a.appendChild(c);
                rootElement.appendChild(a); // add taxon_path_skilllevel_prerequisite to dom.

                a = document.createElement("Attribute");
                b = document.createElement("Name");
                b.appendChild(document.createTextNode("taxon_path_idea_discipline"));
                c = document.createElement("Value");
                c.appendChild(document.createTextNode(taxon_path_idea_discipline));
                a.appendChild(b);
                a.appendChild(c);
                rootElement.appendChild(a); // add taxon_path_idea_discipline to dom.

                a = document.createElement("Attribute");
                b = document.createElement("Name");
                b.appendChild(document.createTextNode("intended_end_user_role"));
                c = document.createElement("Value");
                c.appendChild(document.createTextNode(intended_end_user_role));
                a.appendChild(b);
                a.appendChild(c);
                rootElement.appendChild(a); // add intended_end_user_role to dom.

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(document);
                StreamResult result =  new StreamResult(fw);
                transformer.transform(source, result);

                r.close();
                fw.close();
            }
        }
    }


    public FullLomToSimplifiedTranslator()
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
        /*
	if ("".equals (uri))
	    System.out.println("Start element: " + qName);
	else
	    System.out.println("Start element: {" + uri + "}" + name);
        */
        
        sb = new StringBuffer(); //reset buffer for new element

        /* discover current node in xml */
        if(name.toLowerCase().equals("general")){
            inGeneral = true;
        }
        if(name.toLowerCase().equals("educational")){
            inEducational = true;
        }
        if(name.toLowerCase().equals("technical")){
            inTechnical = true;
        }
        if(inGeneral && name.toLowerCase().equals("title")){
            foundTitle = true;
        }
        if(inGeneral && name.toLowerCase().equals("description")){
            foundDescription = true;
        }
        if(inEducational && name.toLowerCase().equals("language")){
            foundContentLanguage = true;
        }
        if(inTechnical && name.toLowerCase().equals("format")){
            foundFormat = true;
        }
        if(inTechnical && name.toLowerCase().equals("otherplatformrequirements")){
            foundOtherPlatformRequirement = true;
        }
        if(inEducational && name.toLowerCase().equals("interactivitytype")){
            foundInteractivityType = true;
        }
        if(inEducational && name.toLowerCase().equals("interactivitylevel")){
            foundInteractivityLevel = true;
        }
        if(inEducational && name.toLowerCase().equals("difficulty")){
            foundDifficulty = true;
        }
        if(inEducational && name.toLowerCase().equals("intendedenduserrole")){
            foundIntendedEndUserRole = true;
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
        
        /* discover current node in xml */
        if(name.toLowerCase().equals("general")){
            inGeneral = false;
        }
        if(name.toLowerCase().equals("educational")){
            inEducational = false;
        }
        if(name.toLowerCase().equals("technical")){
            inTechnical = false;
        }

        /* what are my characters here? */
        //System.out.println("LEAVING ELEMENT... SB:\t\n" + sb + "\n");

        if(foundTitle){
            title = sb.toString();
            foundTitle = false; //reset
        }
        if(foundDescription){
            description = sb.toString();
            foundDescription = false; //reset
        }
        if(foundContentLanguage){
            content_language = sb.toString();
            foundContentLanguage = false; //reset
        }
        if(foundFormat){
            format = sb.toString();
            foundFormat = false; //reset
        }
        if(foundOtherPlatformRequirement){
            other_platform_requirement = sb.toString();
            foundOtherPlatformRequirement = false; //reset
        }
        if(foundInteractivityType){
            interactivity_type = sb.toString();
            foundInteractivityType = false; //reset
        }
        if(foundInteractivityLevel){
            interactivity_level = sb.toString();
            foundInteractivityLevel = false; //reset
        }
        if(foundDifficulty){
            difficulty = sb.toString();
            foundDifficulty = false; //reset
        }
        if(foundIntendedEndUserRole){
            intended_end_user_role = sb.toString();
            foundIntendedEndUserRole = false; //reset
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

        sb.append(characters);

        

    }


}