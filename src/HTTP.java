import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HTTP {
	public String request;
	public String requestMethod;
	public String APIKey;
	
	
	public HTTP(String a, String b) {
		request=a;
		requestMethod=b;
	}
	
    public String getResponse() throws IOException{        
		URL url = new URL(request);
	    HttpURLConnection con = (HttpURLConnection) url.openConnection();
	    con.setRequestMethod(requestMethod);
	    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	    String inputLine;
	    StringBuilder content = new StringBuilder();
	    while ((inputLine = in.readLine()) != null) {
	    	content.append(inputLine.trim()); 
	    }
	    return content.toString();
    }

}
