package senor_meteo;


import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;





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
					json = outilsmeteo.JsonReader.meteoFromUrl(); // importation de la météo depuis la fonction du package
					System.out.println(json.get("list").toString());
					
				} catch (IOException | JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  }
	  });
			
		meteoparallele.addSubBehaviour(new TickerBehaviour(this,43200000){// 43200000 = 12 heures en milisecondes | à modifier selon le rafraichissement voulu
				protected void onTick() {
					
					
					try {
						json = outilsmeteo.JsonReader.meteoFromUrl(); // importation de la météo depuis la fonction du package
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
					rep.setContent("contenu à mettre dans le message");
					// TODO fonction qui met dans le message la météo obtenue et envoie du message
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
