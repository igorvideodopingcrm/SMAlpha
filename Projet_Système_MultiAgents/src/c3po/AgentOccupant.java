package c3po;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import outilsmeteo.Tabmeteo;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;


public class AgentOccupant  extends Agent{
	
	Tabmeteo[] tab= new Tabmeteo[7];//TODO enlever ce tableau test une fois les tests terminé
	protected void setup(){

			addBehaviour(new GUIBehaviour());
			// ajout du comportement décrit au dessous.
			
			
			ParallelBehaviour occuparallele = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
			
			
			
			occuparallele.addSubBehaviour(new OneShotBehaviour(this){
				
				public void action(){
					System.out.println(this.getClass().toString()+" lancé");
					
					ACLMessage message = new ACLMessage(ACLMessage.INFORM);
					message.addReceiver(new AID("senor_meteo", AID.ISLOCALNAME));
					message.setContent("météo!");
					send(message);
			  }
				
		  });
			
			occuparallele.addSubBehaviour(new TickerBehaviour(this,20000){// 43200000 = 12 heures en milisecondes | à modifier selon le rafraichissement voulu
				protected void onTick() {
					
					ACLMessage message = new ACLMessage(ACLMessage.INFORM);
					message.addReceiver(new AID("senor_meteo", AID.ISLOCALNAME));
					message.setContent("météo!");
					send(message);
				
				}
			});
			
			occuparallele.addSubBehaviour(new CyclicBehaviour(this) { // behaviour de réception de message
				
				public void action()
				{
				ACLMessage msg = receive();
				ACLMessage rep = new ACLMessage(ACLMessage.INFORM);
				if(msg!= null)  {
					String contenu=msg.getContent();
					System.out.println("je suis C3PO et j'ai reçu " + contenu);
					}
				else{
					block();
					};
				}
				});
			addBehaviour(occuparallele);
	}
	
	/**
	 * 	Inner class GUIBehaviour
	 */
	private class GUIBehaviour extends Behaviour 
	{


		public void action() {
			// TODO Contenu du Behaviour de La GUI, en charge de son comportement
			
		}


		public boolean done() {
			// TODO Methode à appeler à la fin de la GUI, mettre TRUE pour fermer le Behaviour, False Pour qu'il se rouvre de manière cyclique
			// CF voir les Behaviour One shot et les behaviours cycliques
			return false;
		}
		
	}
}
