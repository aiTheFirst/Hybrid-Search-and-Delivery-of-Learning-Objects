package Utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class LOM {
	public Map<String,String> attributes = new HashMap<String, String>();
	
	public void parseFromFile(String fileName)
	{
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
				if ( name != null && value != null)
					attributes.put(name, value);
			}
			
		} catch (FileNotFoundException e) {
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
	}
}
