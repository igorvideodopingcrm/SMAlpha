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
	JSONObject json = null;
	
	protected void setup(){
		ParallelBehaviour meteoparallele = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
		
		
		meteoparallele.addSubBehaviour(new OneShotBehaviour(this){
			public void action(){
				System.out.println("one shot behaviour");
				try {
					json = outilsmeteo.JsonReader.meteoFromUrl();// importation de la m�t�o depuis la fonction du package
					JSONArray jarray = json.getJSONArray("list");
					JSONObject temp = jarray.getJSONObject(1);  //obtient le num�ro 1 de l'array de la m�t�o
				//	temp = temp.getJSONObject("dt");
					System.out.print(temp.getInt("dt")); // marque la date du premier �l�ment
					
					 StringWriter out = new StringWriter();
					  temp.write(out);
					  String jsonText = out.toString();
					  System.out.print(jsonText);
					  Tabmeteo tab[]= new Tabmeteo[7];
					  
				} catch (IOException | JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  }
	  });
			
		meteoparallele.addSubBehaviour(new TickerBehaviour(this,43200000){// 43200000 = 12 heures en milisecondes | � modifier selon le rafraichissement voulu
				protected void onTick() {
					
					
					try {
						json = outilsmeteo.JsonReader.meteoFromUrl(); // importation de la m�t�o depuis la fonction du package
					} catch (IOException | JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					json.toString();
					System.out.println(json.toString());
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
