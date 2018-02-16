import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

	public static void main(String[] args) throws IOException {
		String os = System.getProperty("os.name");
		String masterPathString = "/opt/bitnami/apache2/htdocs/mattbauman.com/briefing/";
		if (os.equals("Windows 10")) {
			masterPathString = "C:/Users/matt/Desktop/mattbauman.com/briefing/";		
		}
		Path newsPath = Paths.get(masterPathString+"news_reels.php");			
		Path hourlyWeatherPath = Paths.get(masterPathString+"weather.php");
		
		
		//NEWS
		News BBC = new News("BBC");
		int newsStoryCount = 5;
		BBC.parseXML(newsStoryCount);		
		News Reuters = new News("Reuters");
		Reuters.parseXML(newsStoryCount);
        try (BufferedWriter newsWriter = Files.newBufferedWriter(newsPath)){
    		for (int i=0;i<newsStoryCount;i++) {
    			if (i<BBC.reel.length) {
    				BBC.writeNewsReelPHP(i,newsWriter);
    			}
    			if (i<Reuters.reel.length) {
    				Reuters.writeNewsReelPHP(i,newsWriter);
    			}
    		}
    		System.out.println(newsPath);
        }

        
		//WEATHER
		Weather minneapolis = new Weather("hourly","Minneapolis","MN","json");
		minneapolis.parseJSON();

        try (BufferedWriter hourlyWeatherWriter = Files.newBufferedWriter(hourlyWeatherPath)){
        	minneapolis.writeHourlyWeatherPHP(hourlyWeatherWriter);  
        	System.out.println(hourlyWeatherPath);
        }

        
        //Stock S&P 500
        Stock SP500 = new Stock(".INX");
        SP500.getCurrent();
        SP500.getHistorical();
		
        Path currentStockPathSP500 = Paths.get(masterPathString+"stock/"+SP500.ticker.substring(1)+".php");
		try (BufferedWriter currentStockWriter = Files.newBufferedWriter(currentStockPathSP500)){
			SP500.writeCurrentStockPHP(currentStockWriter);		
			System.out.println(currentStockPathSP500);
		}

		Path historicalStockPathSP500 = Paths.get(masterPathString+"stock/"+SP500.ticker+"_HistoricalStock.json");
		try (BufferedWriter historicalStockWriter = Files.newBufferedWriter(historicalStockPathSP500)){
			SP500.writeHistoricalStockJSON(historicalStockWriter);
			System.out.println(historicalStockPathSP500);
		}
		
		//Stock Vanguard 2055 VFFVX
        Stock VFFVX = new Stock("VFFVX");
//        VFFVX.getCurrent();
        VFFVX.getHistorical();
		
        Path currentStockPathVFFVX = Paths.get(masterPathString+"stock/"+VFFVX.ticker+".php");
		try (BufferedWriter currentStockWriter = Files.newBufferedWriter(currentStockPathVFFVX)){
			VFFVX.writeCurrentStockPHP(currentStockWriter);		
			System.out.println(currentStockPathVFFVX);
		}

		Path historicalStockPathVFFVX = Paths.get(masterPathString+"stock/"+VFFVX.ticker+"_HistoricalStock.json");
		try (BufferedWriter historicalStockWriter = Files.newBufferedWriter(historicalStockPathVFFVX)){
			VFFVX.writeHistoricalStockJSON(historicalStockWriter);
			System.out.println(historicalStockPathVFFVX);
		}
		
	}
}
