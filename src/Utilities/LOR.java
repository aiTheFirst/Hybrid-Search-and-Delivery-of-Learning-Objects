package Utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class LOR {
	
	public List<LO> los = new ArrayList<LO>();
	
	public void parseFromFile(String fileName)
	{
		DocumentBuilderFactory docBuilderfact = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = docBuilderfact.newDocumentBuilder();
			Document xmlrules = docBuilder.parse(new FileInputStream(fileName));
			
			NodeList ruleNodes = xmlrules.getElementsByTagName("LO");
			for (int i = 0 ; i < ruleNodes.getLength() ; ++i)
			{
				LO lo = new LO();
				NodeList childNodes = ruleNodes.item(i).getChildNodes();
				for ( int j = 0 ; j < childNodes.getLength() ; ++j )
				{
					if ( childNodes.item(j) instanceof org.w3c.dom.Element )
					{
						Element el = (Element)childNodes.item(j);
						if ( el.getNodeName().equals("Name"))
							lo.LOName = el.getTextContent(); 							
						else if ( el.getNodeName().equals("URL"))
							lo.LOURL = el.getTextContent(); 							
						else if ( el.getNodeName().equals("LOMURL"))
							lo.LOMURL = el.getTextContent(); 							
					}
				}
				los.add(lo);
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
	
	public void getAllLoms()
	{
		for ( LO lo : los)
		{
			lo.lom = new LOM();
//			System.out.println(lo.LOMURL);
			lo.lom.parseFromFile(lo.LOMURL);
		}
	}
}
