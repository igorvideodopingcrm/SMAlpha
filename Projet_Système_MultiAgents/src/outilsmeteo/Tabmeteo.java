package outilsmeteo;

public class Tabmeteo {

String date="";
int temperature=42;
String meteo="";


public String getDate() {
	return date;
}
public void setDate(long i) {
	String dat = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date(i*1000)); // i ou i*1000 ?
	this.date = dat;
}
public int getTemperature() {
	return temperature;
}
public void setTemperature(int temperature) {
	this.temperature = temperature;
}
public String getMeteo() {
	return meteo;
}
public void setMeteo(String meteo) {
	this.meteo = meteo;
}

@Override
public String toString() {
	return  date + "," + temperature + "," + meteo;
}




}


