package senor_meteo;


import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import outilsmeteo.*;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;


public class AgentMeteo extends Agent{

	Tabmeteo[] tab= new Tabmeteo[7];
	
	protected void setup(){
		
		ParallelBehaviour meteoparallele = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
		
		
		
		meteoparallele.addSubBehaviour(new OneShotBehaviour(this){

			
			public void action(){
				System.out.println(this.getClass().toString()+" lancé");
				try {
				
					JSONObject json = outilsmeteo.JsonReader.meteoFromUrl();// importation de la météo depuis la fonction du package
					JSONArray listejour = json.getJSONArray("list"); // on prends la liste des prévisions journalière
					long valdat =0; 								// initialisation des variable et du tableau ou on les rentre
					String meteo;
					int temperature;
					
									// entrée dans le tableau
					for (int i = 0; i < tab.length; i++) {
						JSONObject temp1 = listejour.getJSONObject(i);
						temperature = temp1.getJSONObject("temp").getInt("day");
						valdat=temp1.getLong("dt");
						meteo = temp1.getJSONArray("weather").getJSONObject(0).getString("description");

						tab[i] = new Tabmeteo();			// entrée des données dans le tableau
						tab[i].setDate(valdat);
						tab[i].setTemperature(temperature);
						tab[i].setMeteo(meteo);
					//	System.out.println(tab[i].toString());
						}
					  
//					  valtest=10;
				} catch (IOException | JSONException e) {
					// TODO Auto-generated catch block
					System.out.println("erreur: Senor meteo n'a pas reçu de donnée du web.");
					e.printStackTrace();
				}
		  }
			
	  });
			
		meteoparallele.addSubBehaviour(new TickerBehaviour(this,43200000){// 43200000 = 12 heures en milisecondes | à modifier selon le rafraichissement voulu
				protected void onTick() {
					try {
						
						JSONObject json = outilsmeteo.JsonReader.meteoFromUrl();// importation de la météo depuis la fonction du package
						JSONArray listejour = json.getJSONArray("list"); // on prends la liste des prévisions journalière
						long valdat =0; 								// initialisation des variable et du tableau ou on les rentre
						String meteo;
						int temperature;
						
										// entrée dans le tableau
						for (int i = 0; i < tab.length; i++) {
							JSONObject temp1 = listejour.getJSONObject(i);
							temperature = temp1.getJSONObject("temp").getInt("day");
							valdat=temp1.getLong("dt");
							meteo = temp1.getJSONArray("weather").getJSONObject(0).getString("description");

							tab[i] = new Tabmeteo();			// entrée des données dans le tableau
							tab[i].setDate(valdat);
							tab[i].setTemperature(temperature);
							tab[i].setMeteo(meteo);
						//	System.out.println(tab[i].toString());
							}
						  
//						  valtest=10;
					} catch (IOException | JSONException e) {
						// TODO Auto-generated catch block
						System.out.println("erreur: Senor meteo n'a pas reçu de donnée du web.");
						e.printStackTrace();
					}
				}
			});
			
			meteoparallele.addSubBehaviour(new CyclicBehaviour(this) {
				public void action()
				{
				ACLMessage msg = receive();
				ACLMessage rep = new ACLMessage(ACLMessage.INFORM);
				if(msg!= null)  {
					AID A = msg.getSender();
					rep.addReceiver(A);
					String reponse ="";
					
					for (int i = 0; i < tab.length; i++) {
						reponse = reponse+tab[i].toString()+";";

						}
					rep.setContent(reponse);
					send(rep); 
					}
				
				else{
					block();
					
							};
				//TODO  gérer la reception de message et l'envoi de message pour donner les prévision météo 
				}
			});
			
			addBehaviour(meteoparallele);
			// ajout du comportement décrit au dessus.
	}
}
