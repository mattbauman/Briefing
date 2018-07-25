import java.io.BufferedWriter;
import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Weather {
	String response, city, stateAbbr, dataType, feature, request ;
	String key = "f58a4e9128db7a73";
	String endPoint = "http://api.wunderground.com/api/";
	String[][] hourlyWeather = new String[36][5];
	
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
            	
            	hourlyWeather[i][0] =  //Date
            				FCTTIME.get("weekday_name").getAsString()+", "+
            				FCTTIME.get("month_name").getAsString()+" "+
            				FCTTIME.get("mday_padded").getAsString()+" "+
            				FCTTIME.get("year").getAsString()
            				;
            	hourlyWeather[i][1]=FCTTIME.get("civil").getAsString(); //Hour
            	JsonObject temp = hourly_forecast.get("temp").getAsJsonObject();
            	hourlyWeather[i][2] = temp.get("english").getAsString(); //Temperature
            	hourlyWeather[i][3] = //Date YYYY-MM-DD
            			FCTTIME.get("year").getAsString()+"-"+
            			FCTTIME.get("mon_padded").getAsString()+"-"+
            			FCTTIME.get("mday_padded").getAsString()
            			;
            	hourlyWeather[i][4] =  hourly_forecast.get("wx").getAsString(); //Condition
            }        
        	
        }

	
	public void writeHourlyWeatherPHP(BufferedWriter a) throws IOException {
		BufferedWriter writer = a;
		for (int i =0;i<hourlyWeather.length;i++) {
			//Write div open and table start
			if (i==0||!hourlyWeather[i][3].equals(hourlyWeather[i-1][3])) {
				writer.write(
					"<div class=\"w3-card-4 w3-margin w3-white\">"+"\r\n"+
					"  Weather"+" ("+city+", "+stateAbbr+")\r\n"+
					"  <div class=\"w3-container w3-white\">"+"\r\n"+
					"    <h5><b><a href=\"https://www.wunderground.com/hourly/us/"+
					stateAbbr+"/"+city+"/date/"+hourlyWeather[i][3]+"\" target=\"_blank\">"+hourlyWeather[i][0]+"</a></b></h5>"+"\r\n");
				writer.write(
					"      <table class=\"w3-table w3-bordered\">\r\n" + 
					"        <thead>\r\n" + 
					"          <tr class=\"w3-theme\">\r\n" + 
					"            <th>Hour</th>\r\n" + 
					"            <th>Condition</th>\r\n" + 
					"            <th>Temperature</th>\r\n" + 
					"          </tr>\r\n" + 
					"        </thead>\r\n" +
					"        <tbody>\r\n");
			}
			//Write body
			writer.write(
				"          <tr>\r\n" + 
				"            <td>"+hourlyWeather[i][1]+"</td>\r\n" + 
				"            <td>"+hourlyWeather[i][4]+"</td>\r\n" + 
				"            <td>"+hourlyWeather[i][2]+"\u00b0 F</td>\r\n" + 
				"          </tr>\r\n");

			//Write table end and div end
			if (i==35||!hourlyWeather[i][3].equals(hourlyWeather[i+1][3])) {
				writer.write(
					"        </tbody>\r\n" +
					"      </table>\r\n" +
			        "  </div>\r\n" +
	        		"</div>\r\n");
			}

		}
	}
	


}
