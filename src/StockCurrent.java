import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class StockCurrent {
	String endPointDomain = "https://finance.google.com/finance?q=";
	
	//Request values
	String ticker, endPoint, dataType;

	//Response values
	String response, name, symbol, c, l, cp, ccol, op, hi, lo, vo, avvo, hi52, lo52, direction;

	
	
	public StockCurrent (String a, String b) throws IOException {
		ticker = a;
		dataType = "&output="+b;
		endPoint = endPointDomain+ticker+dataType;
		//System.out.println(endPoint);
		HTTP Stock = new HTTP(endPoint,"GET");
		response=Stock.getResponse();
		response=response.substring(3); //removing leading "// "
		response=response.substring(1,response.length()-1); //remove first [ and last ]
	}
	
	public void parseJSON() {

        JsonElement jelement = new JsonParser().parse(response);
        JsonObject  jobject = jelement.getAsJsonObject();
        
        symbol = jobject.get("symbol").getAsString();
        name = jobject.get("name").getAsString();
        l = jobject.get("l").getAsString(); //last trade
        c = jobject.get("c").getAsString(); //change
        cp = jobject.get("cp").getAsString(); //change percent

        if (c.substring(0,1).equals("-")) {
        	direction="down";
        } else {
        	direction="up";
        }

	}
	
	
	public void printStuff() {
		System.out.println(name+" ("+symbol+")");
		System.out.println("$"+l+" "+c+" "+cp+"%");
		System.out.println(direction);
		System.out.println();


	}
	
}
