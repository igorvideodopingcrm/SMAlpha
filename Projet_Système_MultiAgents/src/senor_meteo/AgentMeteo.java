package senor_meteo;
import java.io.IOException;

import org.json.JSONException;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import outilsmeteo.OwmClient;
import outilsmeteo.WeatherData;
import outilsmeteo.WeatherData.Clouds;
import outilsmeteo.WeatherData.WeatherCondition;
import outilsmeteo.WeatherStatusResponse;


public class AgentMeteo  extends Agent{
	protected void setup(){
		ParallelBehaviour meteoparallele = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
			
		meteoparallele.addSubBehaviour(new TickerBehaviour(this,3000){
				protected void onTick() {
					
					
					OwmClient owm = new OwmClient ();
					WeatherStatusResponse currentWeather = null;
					try {
						currentWeather = owm.currentWeatherAtCity("Lapua","FI");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (currentWeather.hasWeatherStatus ()) {
					    WeatherData weather = currentWeather.getWeatherStatus().get (0);
				        float weatherCondition = weather.getTemp()-273;
				        String description = String.valueOf(weatherCondition);
					    System.out.println ("la temp�rature � saint pierre est de "+description);
					}
					
					
					//TODO Espace ou Senor Meteo va chercher la m�t�o sur le net pour les agent en ayant besoin
					//Ou la distribuer � qui de droit. Le timer ci dessus, 3000, est en milli�me de seconde (actuellement 3 s)
					//A modifier selon la fr�quence de rafraichissement voulue.
				}
			});
		
			meteoparallele.addSubBehaviour(new CyclicBehaviour(this) {
				public void action()
				{
				ACLMessage msg = receive();
				ACLMessage rep = null;
				if(msg!= null)  {
					AID A = msg.getSender();
					rep.addReceiver(A);
					rep.setContent("contenu � mettre dans le message");
					// TODO fonction qui met dans le message la m�t�o obtenue et envoie du message
					}
				
				else{
					block();
					
							};
				//TODO  g�rer la reception de message et l'envoi de message pour donner les pr�vision m�t�o 
				}
			});
			
			addBehaviour(meteoparallele);
			// ajout du comportement d�crit au dessus.
	}
}
