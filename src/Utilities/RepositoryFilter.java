/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package fullLomTranslator;
package Utilities;

import java.io.*;
import java.util.*;

import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;


/**
 *
 * @author Jarrett G. Steele
 *
 * This class will take any repository directory, and create another repository where all lom's in the repo
 * have "title" "description" "interactivity_type" "interactivity_level" "difficulty" and "intendedenduserrole" attributes populated.
 *
 */
public class RepositoryFilter{

    
    public static String repoDir = null;
    
    public static String[] filenames = null;
    public static String currentFileName = null;

    public static File dir = null;
    public static FileReader r = null;
    public static FileWriter fw = null;
   
    	
    public static Map<String,String> parseFromFile(String fileName)
    {
        Map<String,String> attributes = new HashMap<String, String>();
        
		DocumentBuilderFactory docBuilderfact = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = docBuilderfact.newDocumentBuilder();
			Document xmlrules = docBuilder.parse(new FileInputStream(fileName));
			
			NodeList attNodes = xmlrules.getElementsByTagName("Attribute");
			for (int i = 0 ; i < attNodes.getLength() ; ++i)
			{
				NodeList childNodes = attNodes.item(i).getChildNodes();
				String name = null,value = null;
				for ( int j = 0 ; j < childNodes.getLength() ; ++j )
				{
					if ( childNodes.item(j) instanceof org.w3c.dom.Element )
					{
						Element el = (Element)childNodes.item(j);
						if ( el.getNodeName().equals("Name"))
							name = el.getTextContent();
						else if ( el.getNodeName().equals("Value"))
							value = el.getTextContent();
					}
				}
                                /* only add attributes if not null and not empty - JGS*/
				if ( name != null && value != null && !name.isEmpty() && !value.isEmpty() )
					attributes.put(name, value);
			}
                        
			
		}
                catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

                return attributes;
    }

    public static void copy(File src, File dst) throws IOException {

        dst.createNewFile();

        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    
    /*
     * args[0] = repo directory (ie programming_repo) of full xml loms
     * 
     * args[1] = output directory name within repo dir for filtered simplified loms and abstracts.
     *
     */
    public static void main (String args[])
	throws Exception
    {
	//open the directory
        dir = new File(args[0] + "\\" + "simplified");

        File outDir = new File(args[0] + "\\" + args[1]);
        outDir.mkdir();

        File abOutDir = new File(outDir.toString()+"\\"+"abstracts");
        abOutDir.mkdir();

        File simpOutDir = new File(outDir.toString()+"\\"+"simplified");
        simpOutDir.mkdir();


        Map<String,String> attributes = null;

        //get list of files.
        filenames = dir.list();

        boolean qualifies;

        //for each simp xml file in this repo directory, attempt to find all required attributes (mentioned in class desc above).
        for(int i = 0; i < filenames.length; i++){
            currentFileName = filenames[i];
            qualifies = false; // assume false

            //only parse .xml files
            if( filenames[i].endsWith(".xml") ){

                System.out.println("Repository Filter: attempting to parse file: " + dir.toString() + "\\" + filenames[i]);

                attributes = parseFromFile(dir.toString() + "\\" + currentFileName);

                /* test hashmap for proper attributes */
                if( attributes.containsKey("title") && attributes.containsKey("description") && attributes.containsKey("interactivity_level")
                        && attributes.containsKey("interactivity_type") && attributes.containsKey("intended_end_user_role") && attributes.containsKey("difficulty")
                        && attributes.containsKey("content_language"))
                {
                    qualifies = true;
                }
                
                if(qualifies){
                    copy(new File(dir.toString()+"\\"+currentFileName),new File(simpOutDir.toString()+"\\"+currentFileName));


                    //assuming that abstracts are present
                    String abstractName = currentFileName.substring(0,currentFileName.length()-9).concat(".txt");
                    copy(new File(args[0]+"\\"+"abstracts"+"\\"+abstractName),new File(abOutDir.toString()+"\\"+abstractName));

                }
                
            }
        }
    }


    public RepositoryFilter()
    {
        super();

    }




    

}