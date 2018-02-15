
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class StockHistorical {
    private final String ticker;

    //Resonse StringBuilder
    public StringBuilder responseHistorical;

    //Historical
    private String EXCHANGE ="", MARKET_OPEN_MINUTE="", MARKET_CLOSE_MINUTE="", INTERVAL="", TIMEZONE_OFFSET="";
    private boolean processHeader=true;
    private Integer timezoneInteger=0;
    private String timezoneString, unixTime;
    String[][] csv2DArray = new String[30][9];
    String endPoint;
    
    Date historicalDateTime; 
    SimpleDateFormat historicalDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

    
    public StockHistorical(String t){
        ticker=t;
    }


    public void getHistorical() throws IOException{
    	endPoint="https://finance.google.com/finance/getprices?q="+ticker+"&output=csv";
    	System.out.println(endPoint);

        URL url = new URL(endPoint);
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

    
    public void printHistoricalPrice(){
        for(int i=0; i<csv2DArray.length; i++){
            if (csv2DArray[i][0] != null && csv2DArray[i][0].length() > 0){
                System.out.println(csv2DArray[i][5]+"\t"+"$"+csv2DArray[i][1]);
            }
        }
    }
    
    public void createHistoricalStockJSON() {
        //Get the file reference
        Path path = Paths.get("/home/matt/stack/apache2/htdocs/mattbauman.com/briefing/stock/"+ticker+"_HistoricalStock.json");
        String os = System.getProperty("os.name");
        if (os.equals("Windows 10")) {
    		path = Paths.get("C:/Users/matt/Desktop/"+ticker+"_HistoricalStock.json");			
		}


        //Use try-with-resource to get auto-closeable writer instance
        try (BufferedWriter writer = Files.newBufferedWriter(path))
        {
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
        } catch (IOException ex) {
            
        }

    

    }

}