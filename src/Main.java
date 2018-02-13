import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
//		Stock SP500 = new Stock(".INX","json");
//		System.out.println(SP500.endPoint);
//		System.out.println(SP500.response);
//		
//		SP500.parseJSON();
//		SP500.printStuff();
//		
//		
//		News BBC = new News("BBC");
//		BBC.parseXML();
//		BBC.printReelHTML();
//		
//		News Reuters = new News("Reuters");
//		Reuters.parseXML();
//		Reuters.printReelHTML();
//		

		
		Weather minneapolis = new Weather("hourly","Minneapolis","MN","json");
		minneapolis.parseJSON();
		minneapolis.printHourlyWeatherHTML();
		

	}

}
