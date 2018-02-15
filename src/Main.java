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
		String os = System.getProperty("os.name");
		int newsStoryCount = 5;
		Path newsPath;
		if (os.equals("Windows 10")) {
			newsPath = Paths.get("C:/Users/matt/Desktop/news_reels.php");			
		} else {
	        newsPath = Paths.get("/home/matt/stack/apache2/htdocs/mattbauman.com/briefing/news_reels.php");
		}
        
        try (BufferedWriter newsWriter = Files.newBufferedWriter(newsPath))
        {

    		for (int i =0;i<newsStoryCount;i++) {
    			if (i<BBC.reel.length) {
    				BBC.writeNewsReelPHP(i,newsWriter);
    			}
    			if (i<Reuters.reel.length) {
    				Reuters.writeNewsReelPHP(i,newsWriter);
    			}
    		}
        } catch (IOException ex) {
            
        }
        
		//WEATHER
		Weather minneapolis = new Weather("hourly","Minneapolis","MN","json");
		minneapolis.parseJSON();
		Path hourlyWeatherPath;
		if (os.equals("Windows 10")) {
			hourlyWeatherPath = Paths.get("C:/Users/matt/Desktop/weather.php");			
		} else {
			hourlyWeatherPath = Paths.get("/home/matt/stack/apache2/htdocs/mattbauman.com/briefing/weather.php");
		}

        try (BufferedWriter hourlyWeatherWriter = Files.newBufferedWriter(hourlyWeatherPath)){
    		minneapolis.writeHourlyWeatherPHP(hourlyWeatherWriter);
        } catch (IOException ex) {
            
        }


		StockCurrent SP500 = new StockCurrent(".INX","json");
		System.out.println(SP500.endPoint);
		System.out.println(SP500.response);
		
		SP500.parseJSON();
		SP500.printStuff();
		
		StockHistorical VFFVX = new StockHistorical("VFFVX");
		VFFVX.getHistorical();
		VFFVX.createHistoricalStockJSON();
		System.out.println(VFFVX.responseHistorical);	


	}

}
