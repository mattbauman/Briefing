import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Stock {
	String endPointDomain = "https://finance.google.com/finance?q=";
	
	//Request values
	String ticker, endPoint, dataType;

	//Response values
	String response, symbol, c, l, cp, ccol, op, hi, lo, vo, avvo, hi52, lo52;

	
	
	public Stock (String a, String b) throws IOException {
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
        cp = jobject.get("cp").getAsString(); //change percent
        c = jobject.get("c").getAsString(); //change
        l = jobject.get("l").getAsString(); //last trade
        lo52 = jobject.get("lo52").getAsString(); //52 week low
        hi52 = jobject.get("hi52").getAsString(); //52 week high  
	}
	
	
	public void printStuff() {
		System.out.println(symbol);
		System.out.println("$"+l);
		System.out.println(cp+"%");
		System.out.println(c);

		System.out.println("$"+lo52);
		System.out.println("$"+hi52);

	}
	
}
