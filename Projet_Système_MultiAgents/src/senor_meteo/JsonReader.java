package senor_meteo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonReader {

  public static String readAll(Reader rd) throws IOException { // fonction pour passer les streams en String
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }

  public static JSONObject meteoFromUrl() throws IOException, JSONException { // fonction permettant d'obtenir la vidéo de l'api openweathermap. la longitude et latitude sont celle de chambéry
		
	  	String lat="45.57"; 
		String longit="5.93";
		String tempText ="http://api.openweathermap.org/data/2.5/forecast/daily?lat="+lat+ "&lon="+longit+"&cnt=7&mode=json&units=metric";  // Renvoie les prévision selon les coordonnées au dessus
		
	    InputStream is = new URL(tempText).openStream();
	    try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      String jsonText = readAll(rd);
	      JSONObject json = new JSONObject(jsonText);
	      return json;
	    } finally {
	      is.close();
	    }
  	}
  
}