package Comparator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Element;



public class RuleBase {
	public class Rule
	{
		public String userProfileParam;
		public String userProfileParamType;
		java.util.List<String> lomParams = new ArrayList<String>();
		java.util.List<String> lomParamType = new ArrayList<String>();
		String ruleScript;
		
		public String toString()
		{
			return "userProfileParam\n"+ruleScript;
		}
	}
	public List<Rule> rules = new ArrayList<Rule>();
	
	public void parseFromFile(String fileName)
	{
		DocumentBuilderFactory docBuilderfact = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = docBuilderfact.newDocumentBuilder();
			Document xmlrules = docBuilder.parse(new FileInputStream(fileName));
			
			NodeList ruleNodes = xmlrules.getElementsByTagName("rule");
			for (int i = 0 ; i < ruleNodes.getLength() ; ++i)
			{
				Rule rule = new Rule();
				NodeList childNodes = ruleNodes.item(i).getChildNodes();
				for ( int j = 0 ; j < childNodes.getLength() ; ++j )
				{
					if ( childNodes.item(j) instanceof org.w3c.dom.Element )
					{
						Element el = (Element)childNodes.item(j);
						if ( el.getNodeName().equals("param"))
						{
							if ( el.getAttribute("source").equals("lom") )
							{
								rule.lomParams.add(el.getAttribute("name"));
								rule.lomParamType.add(el.getAttribute("type"));
							}
							else if ( el.getAttribute("source").equals("userprofile") )
							{
								rule.userProfileParam = el.getAttribute("name");
								rule.userProfileParamType = el.getAttribute("type");
							}
						}
						else if ( el.getNodeName().equals("rulescript"))
						{
							rule.ruleScript = el.getTextContent();
						}
					}
				}
				rules.add(rule);
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
