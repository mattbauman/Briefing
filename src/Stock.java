
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Stock{
    public String ticker;

    //Resonse StringBuilder
    public StringBuilder responseHistorical;

    //Historical
    private String EXCHANGE ="", MARKET_OPEN_MINUTE="", MARKET_CLOSE_MINUTE="", INTERVAL="", TIMEZONE_OFFSET="";
    private boolean processHeader=true;
    private Integer timezoneInteger=0;
    private String timezoneString, unixTime;
    String[][] csv2DArray = new String[30][9];
    String historicalStockEndPoint;
    
    Date historicalDateTime; 
    SimpleDateFormat historicalDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

	String currentStockEndPointDomain = "https://finance.google.com/finance?q=";
	String historicalStockEndPointDomain = "https://finance.google.com/finance/getprices?q=";

	//Current Stock values
	String currentStockEndPoint, dataType, responseCurrent, name, symbol, c, l, cp, ccol, op, hi, lo, vo, avvo, hi52, lo52, direction;

    
    public Stock(String t){
        ticker=t;
    }

	public void getCurrent () throws IOException {
		currentStockEndPoint = currentStockEndPointDomain+ticker+"&output=json";
		System.out.println(currentStockEndPoint);
		//System.out.println(endPoint);
		HTTP Stock = new HTTP(currentStockEndPoint,"GET");
		responseCurrent=Stock.getResponse();
		responseCurrent=responseCurrent.substring(3); //removing leading "// "
		responseCurrent=responseCurrent.substring(1,responseCurrent.length()-1); //remove first [ and last ]
		
        JsonElement jelement = new JsonParser().parse(responseCurrent);
        JsonObject  jobject = jelement.getAsJsonObject();
        
        symbol = jobject.get("symbol").getAsString();
        name = jobject.get("name").getAsString();
        l = jobject.get("l").getAsString(); //last trade
        c = jobject.get("c").getAsString(); //change
        cp = jobject.get("cp").getAsString(); //change percent


	}
	

    public void getHistorical() throws IOException{
    	historicalStockEndPoint=historicalStockEndPointDomain+ticker+"&output=csv";
    	System.out.println(historicalStockEndPoint);
        URL url = new URL(historicalStockEndPoint);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            StringBuilder content = new StringBuilder();
            String inputLine;
            int i=0; //loop index for building csv2DArray from each csvLine
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine).append("\n");
                    //Assign header variables
                if (processHeader){
                    if (EXCHANGE.equals("") && inputLine.substring(0,8).equals("EXCHANGE")){
                        EXCHANGE=inputLine.substring(11,inputLine.length());
                    } else if (MARKET_OPEN_MINUTE.equals("") && inputLine.substring(0,18).equals("MARKET_OPEN_MINUTE")){
                        MARKET_OPEN_MINUTE=inputLine.substring(19,inputLine.length());
                    } else if (MARKET_CLOSE_MINUTE.equals("") && inputLine.substring(0,19).equals("MARKET_CLOSE_MINUTE")){
                        MARKET_CLOSE_MINUTE=inputLine.substring(20,inputLine.length());
                    } else if (INTERVAL.equals("") && inputLine.substring(0,8).equals("INTERVAL")){
                        INTERVAL=inputLine.substring(9,inputLine.length());
                    } else if (inputLine.equals("DATA=")){ //Skip
                    } else if (TIMEZONE_OFFSET.equals("") && inputLine.substring(0,15).equals("TIMEZONE_OFFSET")){
                        TIMEZONE_OFFSET=inputLine.substring(16,inputLine.length());
                        processHeader=false; //Done with header
                    }
                } else { //CSV Line
                    String[] csvLine = inputLine.split(",");
                    //check for unix time record (should be first)
                    if (inputLine.substring(0,1).equals("a")){
                        unixTime=csvLine[0].substring(1,csvLine[0].length());                            
                        csvLine[0]="0";
                    }
                    
                    System.arraycopy(csvLine, 0, csv2DArray[i], 0, 5); //add CSVLine to the csv2DArray (fixed 30x5)
                    
                    //Create timezone string ex: GMT-5
                    if (timezoneInteger==0){
                        timezoneInteger = (Integer.parseInt(TIMEZONE_OFFSET)/60);
                        timezoneString = "GMT"+(timezoneInteger.toString());                            
                        historicalDateTimeFormat.setTimeZone(TimeZone.getTimeZone(timezoneString));
                    }

                    //Determine date //convert seconds to milliseconds
                    Integer unixTimeInteger= Integer.parseInt(unixTime);
                    Integer dayInterval = Integer.parseInt(csv2DArray[i][0]);
                    dayInterval = dayInterval*86400;
                    unixTimeInteger = unixTimeInteger+dayInterval;
                    historicalDateTime = new Date(unixTimeInteger*1000L);                        
                    csv2DArray[i][5] = historicalDateTimeFormat.format(historicalDateTime);
//                        System.out.println(historicalDateTimeFormat.format(historicalDateTime));
                    
                    //Assign year/month/day to array record
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(historicalDateTime);
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    csv2DArray[i][6] = Integer.toString(year);
                    csv2DArray[i][7] = Integer.toString(month);
                    csv2DArray[i][8] = Integer.toString(day);
                    
                    //loop index for csvLine
                    i++; 
                }
            }
            responseHistorical=content;
        in.close();
        }

    }
    

    
	public void writeCurrentStockPHP(BufferedWriter a) throws IOException {
		BufferedWriter writer = a;
		boolean nullResponseCurrent;
		
		
		try {
			nullResponseCurrent = responseCurrent.isEmpty();
		} catch (NullPointerException e) {
			nullResponseCurrent=true;
		}
		
		if (nullResponseCurrent) {
			symbol=ticker;
			if(symbol.equals("VFFVX")) {
				name="Vanguard Target Retirement 2055 Fund Investor Shares";
			} else {
				name=ticker;
			}

			int lastTradeIndex=0;
			for(int i = 0; i<csv2DArray.length; i++) {
				if(csv2DArray[i][0] != null) {
					lastTradeIndex=i;
				}
			}
			l=csv2DArray[lastTradeIndex][1];
			String ll=csv2DArray[lastTradeIndex-1][1];
			
			BigDecimal lBigDecimal = new BigDecimal(l);
			BigDecimal llBigDecimal = new BigDecimal(ll);
			BigDecimal cBigDecimal = lBigDecimal.subtract(llBigDecimal);
			cBigDecimal = cBigDecimal.setScale(4);
			BigDecimal cpBigDecimal = cBigDecimal.divide(lBigDecimal,4);
			cpBigDecimal = cpBigDecimal.multiply(new BigDecimal(100));
			cpBigDecimal = cpBigDecimal .setScale(2);
			cBigDecimal = cBigDecimal .setScale(2);
			
			c=cBigDecimal.toString();
			cp=cpBigDecimal.toString();
			
		}
		
		String fontColor="green";
        if (c.substring(0,1).equals("-")) {
        	fontColor = "red";
        }
        
        //if no addition sign on c (change) - add one
        if (!c.substring(0,1).equals("-")&&!c.substring(0,1).equals("+")) {
        	c="+"+c;
        } else {
        	
        	
        }
        
        if (ticker.substring(0,1).equals(".")) {
        	ticker=ticker.substring(1);
        }
		

		
		
		writer.write(
			"  <div class=\"w3-card-4 w3-margin w3-white\"  id=\"stock\">\r\n" + 
			"  Stock\r\n" + 
			"    <div class=\"w3-container w3-white\">\r\n" + 
			"      <h5><b><a href=\"http://www.google.com/finance?q="+symbol+"\" target=\"_blank\">"+name+" ("+ticker+")</a></b></h5>\r\n" + 
			"      <p>$"+l+" <font color=\""+fontColor+"\">"+c+" ("+cp+"%)</font></p>\r\n" + 
			"    <div id=\""+ticker+"_chart_div\"></div>\r\n" + 
			"    </div>\r\n" + 
			"  </div>");
	}
	


    
    public void writeHistoricalStockJSON(BufferedWriter a) throws IOException {
        BufferedWriter writer = a;
        writer.write("{\n" +
	        "  \"cols\": [\n" +
	        "        {\"id\":\"\",\"label\":\"Date\",\"pattern\":\"\",\"type\":\"date\"},\n" +
	        "        {\"id\":\"\",\"label\":\""+ticker+"\",\"pattern\":\"\",\"type\":\"number\"}\n" +
	        "      ],\n" +
	        "  \"rows\": [\n");


        for(int i=0; i<csv2DArray.length; i++){
            if (csv2DArray[i][0] != null && csv2DArray[i][0].length() > 0){
            	writer.write("        {\"c\":[{\"v\":\"Date("+csv2DArray[i][6]+","+csv2DArray[i][7]+","+csv2DArray[i][8]+")\",\"f\":null},{\"v\":"+csv2DArray[i][1]+",\"f\":null}]},\n");                    
            }
        }

        writer.write("      ]\n" +
        "}");


    

    }

}