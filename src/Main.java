import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class Main {

	public static void main(String[] args) throws IOException {
		
		String os = System.getProperty("os.name");
		String masterPathString = "/opt/bitnami/apache2/htdocs/mattbauman.com/briefing/";
		if (os.equals("Windows 10")) {
			masterPathString = "C:/Users/matt/Desktop/mattbauman.com/briefing/";		
		}
		System.out.println(LocalDateTime.now().toString().substring(0, 19));
		Path runDateTimePath = 	Paths.get(masterPathString+"runDateTime.php");
        try (BufferedWriter runDateTimeWriter = Files.newBufferedWriter(runDateTimePath)){
        	runDateTimeWriter.write(LocalDateTime.now().toString().substring(0, 19));
    		System.out.println(runDateTimePath);
        }
		Path newsPath = Paths.get(masterPathString+"news_reels.php");			
		Path hourlyWeatherPath = Paths.get(masterPathString+"weather.php");
		
		
		//NEWS
		News BBC = new News("BBC");
		BBC.parseXML();		
		News Reuters = new News("Reuters");
		Reuters.parseXML();
		News NPR = new News("NPR");
		NPR.parseXML();
		News PBS = new News("PBS");
		PBS.parseXML();
		News ST = new News("Star Tribune");
		ST.parseXML();
        try (BufferedWriter newsWriter = Files.newBufferedWriter(newsPath)){
    		for (int i=0;i<20&&i<BBC.reel.length&&i<Reuters.reel.length&&i<NPR.reel.length&&i<PBS.reel.length&&i<ST.reel.length;i++) {
    			if (i<Reuters.reel.length) {
    				Reuters.writeNewsReelPHP(i,newsWriter);
    			}
    			if (i<BBC.reel.length) {
    				BBC.writeNewsReelPHP(i,newsWriter);
    			}
    			if (i<NPR.reel.length) {
    				NPR.writeNewsReelPHP(i,newsWriter);
    			}
    			if (i<PBS.reel.length) {
    				PBS.writeNewsReelPHP(i,newsWriter);
    			}
    			if (i<ST.reel.length) {
    				ST.writeNewsReelPHP(i,newsWriter);
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

//		Google Finance API deprecated 2018-08-01        
//        //Stock S&P 500
//        Stock SP500 = new Stock(".INX");
////        SP500.getCurrent();
//        SP500.getHistorical();
////		
//        Path currentStockPathSP500 = Paths.get(masterPathString+"stock/"+SP500.ticker+".php");
//		try (BufferedWriter currentStockWriter = Files.newBufferedWriter(currentStockPathSP500)){
//			SP500.writeCurrentStockPHP(currentStockWriter);		
//			System.out.println(currentStockPathSP500);
//		}
////
//		Path historicalStockPathSP500 = Paths.get(masterPathString+"stock/"+SP500.ticker+"_HistoricalStock.json");
//		try (BufferedWriter historicalStockWriter = Files.newBufferedWriter(historicalStockPathSP500)){
//			SP500.writeHistoricalStockJSON(historicalStockWriter);
//			System.out.println(historicalStockPathSP500);
//		}
//		
//		//Stock Vanguard 2055 VFFVX
//        Stock VFFVX = new Stock("VFFVX");
////        VFFVX.getCurrent();
//        VFFVX.getHistorical();
//		
//        Path currentStockPathVFFVX = Paths.get(masterPathString+"stock/"+VFFVX.ticker+".php");
//		try (BufferedWriter currentStockWriter = Files.newBufferedWriter(currentStockPathVFFVX)){
//			VFFVX.writeCurrentStockPHP(currentStockWriter);		
//			System.out.println(currentStockPathVFFVX);
//		}
//
//		Path historicalStockPathVFFVX = Paths.get(masterPathString+"stock/"+VFFVX.ticker+"_HistoricalStock.json");
//		try (BufferedWriter historicalStockWriter = Files.newBufferedWriter(historicalStockPathVFFVX)){
//			VFFVX.writeHistoricalStockJSON(historicalStockWriter);
//			System.out.println(historicalStockPathVFFVX);
//		}
		System.out.println(LocalDateTime.now().toString().substring(0, 19));
		
	}
}
