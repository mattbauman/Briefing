import java.io.*;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class News {
	String agency, dataType, edition, response, endPoint;
	String BBCUSEndPoint="http://feeds.bbci.co.uk/news/rss.xml?edition=us";
	String ReutersTopNewsEndPoint = "http://feeds.reuters.com/reuters/topNews";
	String ReutersUSEndPoint = "http://feeds.reuters.com/Reuters/domesticNews";
	String[][] reel = new String[100][3];
	
	public News (String a) throws IOException {
		agency = a;
		
		switch(agency) {
			case "BBC": endPoint=BBCUSEndPoint;
			break;
			case "Reuters": endPoint=ReutersTopNewsEndPoint;
			break;
			case "ReutersUS": endPoint=ReutersUSEndPoint;
			break;
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
	        for (int i = 0; i<nList.getLength(); i++) {
				Node nNode = nList.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					
					reel[i][0] = eElement.getElementsByTagName("title").item(0).getTextContent();
					reel[i][1] = eElement.getElementsByTagName("description").item(0).getTextContent();
					if (agency.length()>=7&&agency.substring(0,7).equals("Reuters")) {
						reel[i][1] = reel[i][1].substring(0,reel[i][1].indexOf("<div class=\"feedflare\">"));
					}
					reel[i][2] = eElement.getElementsByTagName("link").item(0).getTextContent();
				}
			}
        } catch (SAXException | ParserConfigurationException | IOException e1) {
            e1.printStackTrace();
        }
	}
		
	public void writeNewsReelPHP(int a, BufferedWriter b) throws IOException {
		int i = a;
		BufferedWriter writer = b;
		String agencyEdition = null;
		switch(agency) {
			case "BBC": agencyEdition = "BBC US";
			break;
			case "Reuters": agencyEdition = "Reuters Top";
			break;
			case "ReutersUS": agencyEdition = "Reuters US";
			break;
		}
		if(reel[i][0] != null) {
			writer.write(
					"<div class=\"w3-card-4 w3-margin w3-white\">"+agencyEdition+" News"+"("+(i+1)+")"+"\r\n" + 
					"	<div class=\"w3-container\">\r\n" + 
					"		<h5><b><a href=\""+reel[i][2]+"\" target=\"_blank\">"+reel[i][0]+"</a></b></h5>\r\n" + 
					"		<p>"+reel[i][1]+"</p>\r\n" + 
					"	</div>\r\n" + 
					"</div>\r\n"
					);
		}

		
		
		
	}
}
