import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

	public static void main(String[] args) throws IOException {

		//NEWS
		News BBC = new News("BBC");
		BBC.parseXML();		
		News Reuters = new News("Reuters");
		Reuters.parseXML();
		int maxReelLimit=Reuters.reelLimit;
		if (BBC.reelLimit>maxReelLimit) {
			maxReelLimit=BBC.reelLimit;
		}
        //Path newsPath = Paths.get("C:/Users/matt/Desktop/news_reels.php");
        Path newsPath = Paths.get("/home/matt/stack/apache2/htdocs/mattbauman.com/briefing/news_reels.php");
        try (BufferedWriter newsWriter = Files.newBufferedWriter(newsPath))
        {
    		newsWriter.write("<div class=\"w3-col l8 s12\">");
    		for (int i =0;i<maxReelLimit;i++) {
    			if (i<BBC.reelLimit) {
    				BBC.writeNewsReelPHP(i,newsWriter);
    			}
    			if (i<Reuters.reelLimit) {
    				Reuters.writeNewsReelPHP(i,newsWriter);
    			}
    		}
    		newsWriter.write("</div>");
        } catch (IOException ex) {
            
        }
        
		//WEATHER
		Weather minneapolis = new Weather("hourly","Minneapolis","MN","json");
		minneapolis.parseJSON();
//		Path hourlyWeatherPath = Paths.get("C:/Users/matt/Desktop/weather.php");
		Path hourlyWeatherPath = Paths.get("/home/matt/stack/apache2/htdocs/mattbauman.com/briefing/weather.php");
        try (BufferedWriter hourlyWeatherWriter = Files.newBufferedWriter(hourlyWeatherPath)){
        	hourlyWeatherWriter.write("<div class=\"w3-col l4\">");
    		minneapolis.writeHourlyWeatherPHP(hourlyWeatherWriter);
    		hourlyWeatherWriter.write("</div>");
        } catch (IOException ex) {
            
        }

//		Stock SP500 = new Stock(".INX","json");
//		System.out.println(SP500.endPoint);
//		System.out.println(SP500.response);
//		
//		SP500.parseJSON();
//		SP500.printStuff();
//		
//

	}

}
