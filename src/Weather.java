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
        System.out.println("JsonObject?: "+jelement.isJsonObject());
        JsonObject  jobject = jelement.getAsJsonObject();

        
        System.out.println("hourly_forecast is Array?: "+jobject.get("hourly_forecast").isJsonArray());
        JsonArray hourly_forecasts = jobject.get("hourly_forecast").getAsJsonArray();
        for (int i=0; i<hourly_forecasts.size(); i++) {
//        	System.out.println(hourly_forecast.get(i));
//        	System.out.println(hourly_forecast.get(i).isJsonObject());
        	JsonObject hourly_forecast = hourly_forecasts.get(i).getAsJsonObject();
        	
        	JsonObject FCTTIME = hourly_forecast.get("FCTTIME").getAsJsonObject();
        	String pretty = FCTTIME.get("pretty").getAsString();
        	System.out.println(pretty);
        	
        	JsonObject temp = hourly_forecast.get("temp").getAsJsonObject();
        	String english = temp.get("english").getAsString();
        	System.out.println(english);
        	
        }
        //JsonElement FCTTIME = ((JsonObject)hourly_forecast.get(0)).get("FCTTIME");
        //System.out.println(FCTTIME.getAsString());
        
        //JsonObject FCTTIME = hourly_forecast.contains("FCTTIME");
 
	}
	
}
