import java.io.*;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class News {
	String agency, dataType, edition, response, endPoint;
	String BBCEndPoint="http://feeds.bbci.co.uk/news/rss.xml?edition=us";
	String ReutersEndPoint = "http://feeds.reuters.com/reuters/topNews";
	int reelLimit = 10;
	String[][] reel = new String[reelLimit][3];
	
	public News (String a) throws IOException {
		agency = a;
		
		switch(agency) {
			case "BBC": endPoint=BBCEndPoint;
			break;
			case "Reuters": endPoint=ReutersEndPoint;
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
	        if (nList.getLength()<reelLimit) {
	        	reelLimit=nList.getLength();
	        }
	        	;
	        for (int i = 0; i<reelLimit; i++) { //Top 10 News Stories
				Node nNode = nList.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					
					reel[i][0] = eElement.getElementsByTagName("title").item(0).getTextContent();
					reel[i][1] = eElement.getElementsByTagName("description").item(0).getTextContent();
					if (agency.equals("Reuters")) {
						reel[i][1] = reel[i][1].substring(0,reel[i][1].indexOf("<div class=\"feedflare\">"));
					}
					reel[i][2] = eElement.getElementsByTagName("link").item(0).getTextContent();

				}
			}


 
        } catch (SAXException | ParserConfigurationException | IOException e1) {
            e1.printStackTrace();
        }
	}
	
	public void printReel() {
		for (int i =0;i<reelLimit;i++) {
			System.out.println("title: "+reel[i][0]);
			System.out.println("description: "+reel[i][1]);
			System.out.println("link: "+reel[i][2]);
			System.out.println();
			
		}
	}
	
	public void printReelHTML() {
		for (int i =0;i<reelLimit;i++) {
			
			try {
				System.out.println("  <div class=\"w3-card-4 w3-margin w3-white\">");
				System.out.println("  "+agency+" News"+"("+(i+1)+")");
				System.out.println("    <div class=\"w3-container\">");
				System.out.println(
						"      <h5><b><a href=\""
						+reel[i][2]+"\" target=\"_blank\">"
						+reel[i][0] + "</a></b></h5>");

				System.out.println("      <p>" + reel[i][1] + "</p>");
				System.out.println("    </div>");
				System.out.println("  </div>");
			} finally {
				
			}
			

		}

        System.out.println("<hr>");
	}
}
