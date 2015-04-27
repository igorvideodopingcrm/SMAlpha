package outilsmeteo;

public class Tabmeteo {

String date;
int temperature;
String meteo;


public String getDate() {
	return date;
}
public void setDate(String date) {
	this.date = date;
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
	return "Tabmeteo [date=" + date + ", temperature=" + temperature
			+ ", meteo=" + meteo + "]";
}




}


