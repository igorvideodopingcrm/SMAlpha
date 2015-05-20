package senor_meteo;

import java.io.Serializable;

public class Meteo implements  Serializable{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String date="";
private int temperature=42;
private String meteo="";

public Meteo(){}

public Meteo(long date, int temperature, String meteo){
	this.setDatefromlong(date);
	this.setTemperature(temperature);
	this.setMeteo(meteo);
}

public Meteo(String date, int temperature, String meteo){
	this.setDate(date);
	this.setTemperature(temperature);
	this.setMeteo(meteo);
}


private void setDate(String date) {
	this.date=date;
}

public String getDate() {
	return this.date;
}
public void setDatefromlong(long i) {
	String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date(i*1000)); // i ou i*1000 ?
	this.date = date;
}
public int getTemperature() {
	return this.temperature;
}
public void setTemperature(int temperature) {
	this.temperature = temperature;
}
public String getMeteo() {
	return this.meteo;
}
public void setMeteo(String meteo) {
	this.meteo = meteo;
}

@Override
public String toString() {
	return this.date + "," + this.temperature + "," + this.meteo;
}

}


