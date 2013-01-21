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
 * This program will generate an xml file similar to Hamid's "LOTest.xml" that contains references to all simplified xml loms to be used by personalization program.
 *
 */
public class PersonalizationTestXMLGenerator extends Thread {


    public static String repoDir = null;
    public static String outputName = null;

    public static boolean stopThread = false;

    public static String[] filenames = null;

    public static File dir = null;
    
    public static FileWriter fw = null;
    

    public void run(){
        try{
            PersonalizationTestXMLGenerator.main(new String[] {repoDir,outputName});
        }
        catch(Exception e){
            //oops. give up.
            return;
        }
    }

    /*
     * args[0] = repo directory of simplified lom xmls
     * args[1] = name of output test xml file.
     */
    public static void main (String args[])
	throws Exception
    {
	
        //open the repo directory ex. myrepo/simplified
        dir = new File(args[0]);

        //get list of files.
        filenames = dir.list();

        //open new filename to write test xml file.
        fw = new FileWriter(args[1]);

                
        // at this point the parser has populated the required fields to create a simplified XML file.
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        // add root
        Element rootElement = document.createElement("LOs");
        document.appendChild(rootElement);

        //for each simplified xml file, make an entry in the test xml file.
        for(int i = 0; i < filenames.length; i++){
            Element lo = document.createElement("LO");
            Element name = document.createElement("Name");
            Element url = document.createElement("URL");
            Element lomurl = document.createElement("LOMURL"); //actually a path to filename.

            name.appendChild(document.createTextNode("LO" + (i+1)));
            url.appendChild(document.createTextNode("url" + (i+1)));
            lomurl.appendChild(document.createTextNode(dir.toString() + "\\" + filenames[i]));

            lo.appendChild(name);
            lo.appendChild(url);
            lo.appendChild(lomurl);

            rootElement.appendChild(lo);

            if( stopThread ) return;
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result =  new StreamResult(fw);
        transformer.transform(source, result);


        fw.close();
    }
    
}