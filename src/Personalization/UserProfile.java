package personalization;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.w3c.dom.Text;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import Controller.*;


public class UserProfile {
	public Map<String,String> attributes = new HashMap<String, String>();
	public Map<String,Double> attributesWeights = new HashMap<String, Double>();
	public Map<String,Double> attributesValues = new HashMap<String, Double>();


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
				String name = null,value = null ;
				double weight = 0.0;
				for ( int j = 0 ; j < childNodes.getLength() ; ++j )
				{
					if ( childNodes.item(j) instanceof org.w3c.dom.Element )
					{
						Element el = (Element)childNodes.item(j);
						if ( el.getNodeName().equals("Name"))
							name = el.getTextContent();
						else if ( el.getNodeName().equals("Value"))
							value = el.getTextContent();
						else if ( el.getNodeName().equals("Weight"))
							weight = Double.parseDouble(el.getTextContent());
					}
				}
				if ( name != null && value != null)
				{
					attributes.put(name, value);
					attributesWeights.put(name, weight);
                                       // attributesValues.put(name, value);
				}
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




	public void print()
	{
          System.out.println("START USERPROFILE PRINT");
		for ( Entry<String, String> ent : attributes.entrySet() )
		{
			System.out.print(ent.getKey());
			System.out.print("Weight:");
			System.out.println(attributesWeights.get(ent.getKey()));
                        System.out.print("Values:");
                        System.out.println(attributes.get(ent.getKey()));
		}
          System.out.println("END USERPROFILE PRINT");
          //loadUserProfile();
         // saveUser();

         String[] lines = (String[])(attributes.values().toArray(new String[0]));
         System.out.println("lines.length = " + lines.length);
         writeAdapted();
        }
        

        public void writeAdapted(){

        //this method saves the adapted user profile
        //overwriting the xml file corresponding to the logged in user

        EE4913App app = new EE4913App();
        String currentuserxml = app.senduserName();

        System.out.println("starting to write adaptation of "+currentuserxml);

    try {
        String filename = currentuserxml;
        System.out.println(filename+"check 2");

        String[][] tarray = new String[18][3];//temp array to load map contents
        int count = 0;
        for( Entry<String,String> entry : attributes.entrySet()){
            tarray[count][0] = entry.getKey();
            tarray[count][1] = attributes.get(entry.getKey());
            tarray[count][2] = Double.toString(attributesWeights.get(entry.getKey()));
            count++;
        }
     
        String[][] array = null;
        
                array = new String [][]{
                    {tarray[0][0],tarray[0][1],tarray[0][2]},
                    {tarray[1][0],tarray[1][1],tarray[1][2]},
                    {tarray[2][0],tarray[2][1],tarray[2][2]},
                    {tarray[3][0],tarray[3][1],tarray[3][2]},
                    {tarray[4][0],tarray[4][1],tarray[4][2]},
                    {tarray[5][0],tarray[5][1],tarray[5][2]},
                    {tarray[6][0],tarray[6][1],tarray[6][2]},
                    {tarray[7][0],tarray[7][1],tarray[7][2]},
                    {tarray[8][0],tarray[8][1],tarray[8][2]},
                    {tarray[9][0],tarray[9][1],tarray[9][2]},
                    {tarray[10][0],tarray[10][1],tarray[10][2]},
                    {tarray[11][0],tarray[11][1],tarray[11][2]},
                    {tarray[12][0],tarray[12][1],tarray[12][2]},
                    {tarray[13][0],tarray[13][1],tarray[13][2]},
                    {tarray[14][0],tarray[14][1],tarray[14][2]},
                    {tarray[15][0],tarray[15][1],tarray[15][2]},
                    {tarray[16][0],tarray[16][1],tarray[16][2]},
                    {tarray[17][0],tarray[17][1],tarray[17][2]}

                };

         
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            //create the root element and add it to the document
            Element root = doc.createElement("Attributes");
            doc.appendChild(root);
            System.out.println("building doc");

            for (int i=1;i<19;i++){
                //create child element, add an attribute, and add to root
                Element child = doc.createElement("Attribute");
                child.setAttribute("id",Integer.toString(i));
                Element child2 = doc.createElement("Name");
                child.appendChild(child2);
                Element child3 = doc.createElement("Value");
                child.appendChild(child3);
                Element child4 = doc.createElement("Weight");
                child.appendChild(child4);
                root.appendChild(child);

                //add a text element to the children
                //ONLY CHANGE THESE
                Text text = doc.createTextNode(array[i-1][0]);
                child2.appendChild(text);
                text = doc.createTextNode(array[i-1][1]);
                child3.appendChild(text);
                text = doc.createTextNode(array[i-1][2]);
                child4.appendChild(text);
            }
                System.out.println("");
                System.out.println(filename+"2nd");
                //
                Source source = new DOMSource(doc);

                //==================================================//
                //--------------------------------------------------//
                //~~MUST SET FULL PATH TO XML FILE TO WORK IN AJAX~~//
                //--------------------------------------------------//
                //==================================================//

                System.out.print(filename);

                // Prepare the output file

                File file = new File(filename);
                Result result = new StreamResult(file);

                // Write the DOM document to the file
                Transformer xformer = TransformerFactory.newInstance().newTransformer();
                xformer.transform(source, result);

                System.out.print("SUCCESS: File Created");



            //
            } catch (Exception e) {
                    System.out.print("ERROR: File not modified");
            }
//
//            catch (TransformerConfigurationException e) {
//                Login.status.setText("ERROR: File not created");
//            }
//
//            catch (TransformerException e) {
//                Login.status.setText("ERROR: File not created");
//            }


//        System.out.print("In the writeXML method yo");
//            try {
//                // Prepare the DOM document for writing
//                Source source = new DOMSource(doc);
//
//                //==================================================//
//                //--------------------------------------------------//
//                //~~MUST SET FULL PATH TO XML FILE TO WORK IN AJAX~~//
//                //--------------------------------------------------//
//                //==================================================//
//                String filename = "src/users/tester.xml";
//
//                // Prepare the output file
//
//                File file = new File(filename);
//                Result result = new StreamResult(file);
//
//                // Write the DOM document to the file
//                Transformer xformer = TransformerFactory.newInstance().newTransformer();
//                xformer.transform(source, result);
//
//                System.out.print("SUCCESS: File Created");
//            }
//
//
//            catch (TransformerConfigurationException e) {
//                Login.status.setText("ERROR: File not created");
//            } catch (TransformerException e) {
//                Login.status.setText("ERROR: File not created");
//            }



        //friday end
        }

        //saturday end

        // This method writes a DOM document to a file
        public void writeXmlFile(Document doc) {

        System.out.print("In the writeXML method yo");
            try {
                // Prepare the DOM document for writing
                Source source = new DOMSource(doc);

                //==================================================//
                //--------------------------------------------------//
                //~~MUST SET FULL PATH TO XML FILE TO WORK IN AJAX~~//
                //--------------------------------------------------//
                //==================================================//
                String filename = "src/users/tester.xml";

                // Prepare the output file

                File file = new File(filename);
                Result result = new StreamResult(file);

                // Write the DOM document to the file
                Transformer xformer = TransformerFactory.newInstance().newTransformer();
                xformer.transform(source, result);

                System.out.print("SUCCESS: File Created");
            }


            catch (TransformerConfigurationException e) {
                Login.status.setText("ERROR: File not created");
            } catch (TransformerException e) {
                Login.status.setText("ERROR: File not created");
            }
            }


        //friday end


        //monday start
  
    @Action
    public void loadUserProfile() {

        String[][] profile_in = new String[18][2];

         String user = "tester";  // Grab user name from text field
        //String user = Login.user_in1.getText();  // Grab user name from text field


        //==================================================//
        //--------------------------------------------------//
        //~~MUST SET FULL PATH TO XML FILE TO WORK IN AJAX~~//
        //--------------------------------------------------//
        //==================================================//
        String fileName = "src/users/"+user+"personalized"+".xml";  // Process into a file name



        DocumentBuilderFactory docBuilderfact = DocumentBuilderFactory.newInstance();
//		try {
//			DocumentBuilder docBuilder = docBuilderfact.newDocumentBuilder();
//			Document xmlrules = docBuilder.parse(new FileInputStream(fileName));
//
//			NodeList attNodes = xmlrules.getElementsByTagName("Attribute");
//			for (int i = 0 ; i < attNodes.getLength() ; ++i)
//			{
//				NodeList childNodes = attNodes.item(i).getChildNodes();
//				for ( int j = 0 ; j < childNodes.getLength() ; ++j )
//				{
//					if ( childNodes.item(j) instanceof org.w3c.dom.Element )
//					{
//						Element el = (Element)childNodes.item(j);
//						if ( el.getNodeName().equals("Value"))
//							profile_in[i][0] = el.getTextContent();
//						else if ( el.getNodeName().equals("Weight")){
//                                                        double weight_in = Double.parseDouble(el.getTextContent());
//                                                        weight_in = weight_in * 100;
//                                                        int weight_int = (int)weight_in;
//							profile_in[i][1] = Integer.toString(weight_int);
//                                                }
//					}
//				}
//
//			}
//
//		} catch (FileNotFoundException e) {
//			Login.status1.setText("ERROR: User not found");
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        boolean verif = false;
//
//        String pass = new String(Login.pass_in1.getPassword());
//        if (pass.equals(profile_in[17][0])){
//            verif = true;
//        }
//        else Login.status1.setText("Error: Wrong password");

////        if (verif){
//            Login.status1.setText("");

            // Load values into gui
//            Login.age_in1.setText(profile_in[0][0]);
//            Login.age_weight1.setValue(Integer.parseInt(profile_in[0][1]));
//
//            Login.language_in1.setText(profile_in[1][0]);
//            Login.language_weight1.setValue(Integer.parseInt(profile_in[1][1]));
//
//            Login.writing_in1.setText(profile_in[2][0]);
//            Login.writing_weight1.setValue(Integer.parseInt(profile_in[2][1]));
//
//            Login.reading_in1.setText(profile_in[3][0]);
//            Login.reading_weight1.setValue(Integer.parseInt(profile_in[3][1]));
//
//            Login.speaking_in1.setText(profile_in[4][0]);
//            Login.speaking_weight1.setValue(Integer.parseInt(profile_in[4][1]));
//
//            Login.listening_in1.setText(profile_in[5][0]);
//            Login.listening_weight1.setValue(Integer.parseInt(profile_in[5][1]));
//
//            Login.prioritygoal_in1.setText(profile_in[6][0]);
//            Login.prioritygoal_weight1.setValue(Integer.parseInt(profile_in[6][1]));
//
//            Login.qualifications_in1.setText(profile_in[7][0]);
//            Login.qualifications_weight1.setValue(Integer.parseInt(profile_in[7][1]));
//
//            Login.goal_in1.setText(profile_in[8][0]);
//            Login.goal_weight1.setValue(Integer.parseInt(profile_in[8][1]));
//
//            Login.activity_in1.setText(profile_in[9][0]);
//            Login.activity_weight1.setValue(Integer.parseInt(profile_in[9][1]));
//
//            Login.status_in1.setText(profile_in[10][0]);
//            Login.status_weight1.setValue(Integer.parseInt(profile_in[10][1]));
//
//            Login.cognitive_in1.setText(profile_in[11][0]);
//            Login.cognitive_weight1.setValue(Integer.parseInt(profile_in[11][1]));
//
//            Login.physical_in1.setText(profile_in[12][0]);
//            Login.physical_weight1.setValue(Integer.parseInt(profile_in[12][1]));
//
//            Login.skill_in1.setText(profile_in[13][0]);
//            Login.skill_weight1.setValue(Integer.parseInt(profile_in[13][1]));
//
//            Login.role_in1.setText(profile_in[14][0]);
//            Login.role_weight1.setValue(Integer.parseInt(profile_in[14][1]));
//
//            Login.keywords_in1.setText(profile_in[15][0]);
//            Login.keywords_weight1.setValue(Integer.parseInt(profile_in[15][1]));
//
//            Login.io_in1.setText(profile_in[16][0]);
//            Login.io_weight1.setValue(Integer.parseInt(profile_in[16][1]));
//
//            Login.passNew_in1.setText(profile_in[17][0]);
//            Login.passVer_in1.setText(profile_in[17][0]);
//        }



    }

    @Action
    public void saveUser() {
        //######################//
        //######################//
       // int error = 0;


        // Reset all labels to black
//        Login.age_label1.setForeground(Color.black);
//        Login.language_label1.setForeground(Color.black);
//        Login.writing_label1.setForeground(Color.black);
//        Login.reading_label1.setForeground(Color.black);
//        Login.speaking_label1.setForeground(Color.black);
//        Login.listening_label1.setForeground(Color.black);
//        Login.prioritygoal_label1.setForeground(Color.black);
//        Login.qualifications_label1.setForeground(Color.black);
//        Login.goal_label1.setForeground(Color.black);
//        Login.activity_label1.setForeground(Color.black);
//        Login.status_label1.setForeground(Color.black);
//        Login.cognitive_label1.setForeground(Color.black);
//        Login.physical_label1.setForeground(Color.black);
//        Login.skill_label1.setForeground(Color.black);
//        Login.role_label1.setForeground(Color.black);
//        Login.keywords_label1.setForeground(Color.black);
//        Login.io_label1.setForeground(Color.black);
//        Login.user_label1.setForeground(Color.black);
//        Login.pass_label1.setForeground(Color.black);
//        Login.passVer_label1.setForeground(Color.black);
//
//
//
//        if (Login.age_in1.getText().equalsIgnoreCase("")){
//            Login.age_label1.setForeground(Color.red);
//            error = 1;
//        }
//
//        if (Login.language_in1.getText().equalsIgnoreCase("")){
//            Login.language_label1.setForeground(Color.red);
//            error = 1;
//        }
//
//        if (Login.writing_in1.getText().equalsIgnoreCase("")){
//            Login.writing_label1.setForeground(Color.red);
//            error = 1;
//        }
//
//        if (Login.reading_in1.getText().equalsIgnoreCase("")){
//            Login.reading_label1.setForeground(Color.red);
//            error = 1;
//        }
//
//        if (Login.speaking_in1.getText().equalsIgnoreCase("")){
//            Login.speaking_label1.setForeground(Color.red);
//            error = 1;
//        }
//
//        if (Login.listening_in1.getText().equalsIgnoreCase("")){
//            Login.listening_label1.setForeground(Color.red);
//            error = 1;
//        }
//
//        if (Login.prioritygoal_in1.getText().equalsIgnoreCase("")){
//            Login.prioritygoal_label1.setForeground(Color.red);
//            error = 1;
//        }
//
//        if (Login.qualifications_in1.getText().equalsIgnoreCase("")){
//            Login.qualifications_label1.setForeground(Color.red);
//            error = 1;
//        }
//
//        if (Login.goal_in1.getText().equalsIgnoreCase("")){
//            Login.goal_label1.setForeground(Color.red);
//            error = 1;
//        }
//
//        if (Login.activity_in1.getText().equalsIgnoreCase("")){
//            Login.activity_label1.setForeground(Color.red);
//            error = 1;
//        }
//
//        if (Login.status_in1.getText().equalsIgnoreCase("")){
//            Login.status_label1.setForeground(Color.red);
//            error = 1;
//        }
//
//        if (Login.cognitive_in1.getText().equalsIgnoreCase("")){
//            Login.cognitive_label1.setForeground(Color.red);
//            error = 1;
//        }
//
//        if (Login.physical_in1.getText().equalsIgnoreCase("")){
//            Login.physical_label1.setForeground(Color.red);
//            error = 1;
//        }
//
//        if (Login.skill_in1.getText().equalsIgnoreCase("")){
//            Login.skill_label1.setForeground(Color.red);
//            error = 1;
//        }
//
//        if (Login.role_in1.getText().equalsIgnoreCase("")){
//            Login.role_label1.setForeground(Color.red);
//            error = 1;
//        }
//
//        if (Login.keywords_in1.getText().equalsIgnoreCase("")){
//            Login.keywords_label1.setForeground(Color.red);
//            error = 1;
//        }
//
//        if (Login.io_in1.getText().equalsIgnoreCase("")){
//            Login.io_label1.setForeground(Color.red);
//            error = 1;
//        }
//
//        if (Login.user_in1.getText().equalsIgnoreCase("")){
//            Login.user_label1.setForeground(Color.red);
//            error = 1;
//        }
//
//        if (Login.passNew_in1.getText().equalsIgnoreCase("")){
//            Login.passNew_label1.setForeground(Color.red);
//            error = 1;
//        }
//
//        if (Login.passVer_in1.getText().equalsIgnoreCase("")){
//            Login.passVer_label1.setForeground(Color.red);
//            error = 1;
//        }
//
//
//        // Verify Passwords Match
//        if (!Login.passNew_in1.getText().equals(Login.passVer_in1.getText())){
//            Login.passNew_label1.setForeground(Color.red);
//            Login.passVer_label1.setForeground(Color.red);
//            error = 2;
//        }
//
//
//        // Set error color
//        Login.status1.setForeground(Color.red);
//
//        // Print error
//        switch (error) {
//            case 0:  break;
//            case 1:  Login.status1.setText("Error: Empty Fields"); break;
//            case 2:  Login.status1.setText("Error: Passwords do not match"); break;
//        }

////		for ( Entry<String, String> ent : attributes.entrySet() )
////		{
////			System.out.print(ent.getKey());
////			System.out.print(":");
////			System.out.println(attributesWeights.get(ent.getKey()));
////		}
        // If no errors, continue and build user profile
     try {

      
        double tmp;

        String[][] values = new String[3][3];
        for ( Entry<String, String> ent : attributes.entrySet() )
	{

//            try {
            // ENDED HERE MONDAY. Write attribute and weight to array
                {
                    values[0][0]= Double.toString(attributesWeights.get(ent.getKey()));
                    System.out.println("Save user"+attributesWeights.get(ent.getKey()));
                };
                            
                
//            }catch (Exception e) {
//            Login.status1.setText("ERROR: Changes not saved");
//            }

        }


                  

            // Build an array of values for attributes, grabbed from create user gui in login
//            String[][] values = new String[][] {
//
//
//
//
////                {"age",Login.age_in1.getText(),Double.toString((tmp=(Login.age_weight.getValue()))/100)},
////                {"language",Login.language_in1.getText(),Double.toString((tmp=(Login.language_weight.getValue()))/100)},
////                {"proficiency_writing",Login.writing_in1.getText(),Double.toString((tmp=(Login.writing_weight.getValue()))/100)},
////                {"proficiency_reading",Login.reading_in1.getText(),Double.toString((tmp=(Login.reading_weight.getValue()))/100)},
////                {"proficiency_speaking",Login.speaking_in1.getText(),Double.toString((tmp=(Login.speaking_weight.getValue()))/100)},
////                {"proficiency_listening",Login.listening_in1.getText(),Double.toString((tmp=(Login.listening_weight.getValue()))/100)},
////                {"priority_of_goal",Login.prioritygoal_in1.getText(),Double.toString((tmp=(Login.prioritygoal_weight.getValue()))/100)},
////                {"qualifications",Login.qualifications_in1.getText(),Double.toString((tmp=(Login.qualifications_weight.getValue()))/100)},
////                {"goal",Login.goal_in1.getText(),Double.toString((tmp=(Login.goal_weight.getValue()))/100)},
////                {"activity_type",Login.activity_in1.getText(),Double.toString((tmp=(Login.activity_weight.getValue()))/100)},
////                {"activity_status",Login.status_in1.getText(),Double.toString((tmp=(Login.status_weight.getValue()))/100)},
////                {"cognitive_preferences",Login.cognitive_in1.getText(),Double.toString((tmp=(Login.cognitive_weight.getValue()))/100)},
////                {"physical_preferences",Login.physical_in1.getText(),Double.toString((tmp=(Login.physical_weight.getValue()))/100)},
////                {"skill_level",Login.skill_in1.getText(),Double.toString((tmp=(Login.skill_weight.getValue()))/100)},
////                {"role",Login.role_in1.getText(),Double.toString((tmp=(Login.role_weight.getValue()))/100)},
////                {"keywords_of_in1terests",Login.keywords_in1.getText(),Double.toString((tmp=(Login.keywords_weight.getValue()))/100)},
////                {"input_output_technology",Login.io_in1.getText(),Double.toString((tmp=(Login.io_weight.getValue()))/100)},
////                {"password",Login.passNew_in1.getText(),"0"}
//            };



            //Build document
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            //create the root element and add it to the document
            Element root = doc.createElement("Attributes");
            doc.appendChild(root);
            System.out.print("building doc");
            for (int i=1;i<19;i++){
                 Element child = doc.createElement("Attribute");
                 child.setAttribute("id",Integer.toString(i));
                 Element child14 = doc.createElement("Weight");
                 child.appendChild(child14);
                Text text = doc.createTextNode(values[i-1][0]);
                child14.appendChild(text);
                 System.out.print("building doc"+text);
            }

//-----------------------------------------------------------------------------------//
//            for (int i=1;i<19;i++){
//                //create child element, add an attribute, and add to root
//                Element child = doc.createElement("Attribute");
//                child.setAttribute("id",Integer.toString(i));
//                Element child2 = doc.createElement("Name");
//                child.appendChild(child2);
//                Element child3 = doc.createElement("Value");
//                child.appendChild(child3);
//                Element child4 = doc.createElement("Weight");
//                child.appendChild(child4);
//                root.appendChild(child);
//
//                //add a text element to the children
//                //ONLY CHANGE THESE
//                Text text = doc.createTextNode(values[i-1][0]);
//                child2.appendChild(text);
//                text = doc.createTextNode(values[i-1][1]);
//                child3.appendChild(text);
//                text = doc.createTextNode(values[i-1][2]);
//                child4.appendChild(text);
//            }


            //Write xml file
           // writeXmlFile(doc);

//--------------------------------------------------------------------------------//




       
        } catch (Exception e) {
            System.out.print("ERROR: File not modified");
        }
    }

    // This method writes a DOM document to a file
public static void writeXmlFile1(Document doc) {
    try {
        // Prepare the DOM document for writing
        Source source = new DOMSource(doc);

        //==================================================//
        //--------------------------------------------------//
        //~~MUST SET FULL PATH TO XML FILE TO WORK IN AJAX~~//
        //--------------------------------------------------//
        //==================================================//
        //String filename = "src/users/"+Login.user_in1.getText()+"personalized"+".xml";

        String filename = "src/users/tester_personalized.xml";
        System.out.print("writing file:"+filename);

        // Prepare the output file
        File file = new File(filename);
        Result result = new StreamResult(file);

        // Write the DOM document to the file
        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.transform(source, result);

        System.out.print("SUCCESS: Changes saved");
    }


    catch (TransformerConfigurationException e) {
        System.out.print("ERROR: Changes not saved");
    } catch (TransformerException e) {
        System.out.print("ERROR: Changes not saved");
    }


    }

        //monday end






}
