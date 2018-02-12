import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Weather {
	String response;
	String city;
	String stateAbbr;
	String dataType;
	String feature;
	String request;
	String key = "f58a4e9128db7a73";
	String endPoint = "http://api.wunderground.com/api/";
	String termsofService;
	
	public Weather(String a, String b, String c, String d) throws IOException {
		feature = a;
		city = b;
		stateAbbr = c;
		dataType = d;
		request = endPoint+key+"/"+feature+"/q/"+stateAbbr+"/"+city+"."+dataType;
		HTTP hourlyWeather = new HTTP(request,"GET");
		response=hourlyWeather.getResponse();
	}
	
	public void parseJSON() {

        JsonElement jelement = new JsonParser().parse(response);
        JsonObject  jobject = jelement.getAsJsonObject();
        JsonArray hourly_forecasts = jobject.get("hourly_forecast").getAsJsonArray();
        for (int i=0; i<hourly_forecasts.size(); i++) {
        	JsonObject hourly_forecast = hourly_forecasts.get(i).getAsJsonObject();
        	
        	JsonObject FCTTIME = hourly_forecast.get("FCTTIME").getAsJsonObject();
        	if (i==0||FCTTIME.get("civil").getAsString().equals("12:00 AM")) {
        		//weekday_name, month_name mday_padded year
        		System.out.println(
        				FCTTIME.get("weekday_name").getAsString()+", "+
        				FCTTIME.get("month_name").getAsString()+" "+
        				FCTTIME.get("mday_padded").getAsString()+" "+
        				FCTTIME.get("year").getAsString()
        				);
        	}
        	System.out.print("<li>");
        	System.out.print(FCTTIME.get("civil").getAsString()+": ");
        	
        	JsonObject temp = hourly_forecast.get("temp").getAsJsonObject();
        	String english = temp.get("english").getAsString();
        	System.out.println(english + "\u00b0"+"F"+"</li>");
        	
        }
        //JsonElement FCTTIME = ((JsonObject)hourly_forecast.get(0)).get("FCTTIME");
        //System.out.println(FCTTIME.getAsString());
        
        //JsonObject FCTTIME = hourly_forecast.contains("FCTTIME");
 
	}
	
}
