import java.io.*;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class News {
	String agency;
	String BBCEndPoint="http://feeds.bbci.co.uk/news/rss.xml?edition=us";
	String ReutersEndPoint = "http://feeds.reuters.com/reuters/topNews";
	String dataType;
	String edition;
	String response;
	
	
	public News (String a) throws IOException {
		agency = a;
		String endPoint = null;
		if (agency.equals("BBC")){
			endPoint=BBCEndPoint;
		}
		else if (agency.equals("Reuters")) {
			endPoint=ReutersEndPoint;
		}

		HTTP News = new HTTP(endPoint,"GET");
		response=News.getResponse();
	}	
	
	public void parseXML() { 
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        
        try {
			dBuilder = dbFactory.newDocumentBuilder();
	        Document doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(response.getBytes("utf-8"))));
	        doc.getDocumentElement().normalize();
	        NodeList nList = doc.getElementsByTagName("item");
	        

	        for (int temp = 0; temp < 5; temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					System.out.println("  <div class=\"w3-card-4 w3-margin w3-white\">");
					System.out.println("  "+agency+" News");
					System.out.println("    <div class=\"w3-container\">");
					System.out.println(
							"      <h5><b><a href=\""
							+eElement.getElementsByTagName("link").item(0).getTextContent()+"\" target=\"_blank\">"
							+eElement.getElementsByTagName("title").item(0).getTextContent() + "</a></b></h5>");
					String description = eElement.getElementsByTagName("description").item(0).getTextContent();
					if (agency.equals("Reuters")) {
						description = description.substring(0,description.indexOf("<div class=\"feedflare\">"));
					}
					System.out.println("      <p>" + description + "</p>");
					System.out.println("    </div>");
					System.out.println("  </div>");
				}
			}
	        System.out.println("<hr>");

 
        } catch (SAXException | ParserConfigurationException | IOException e1) {
            e1.printStackTrace();
        }
	}
}
