package senor_meteo;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;


public class AgentMeteo  extends Agent{
	protected void setup(){
		ParallelBehaviour meteoparallele = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
			
		meteoparallele.addSubBehaviour(new TickerBehaviour(this,3000){
				protected void onTick() {
					//TODO Espace ou Senor Meteo va chercher la m�t�o sur le net pour les agent en ayant besoin
					//Ou la distribuer � qui de droit. Le timer ci dessus, 3000, est en milli�me de seconde (actuellement 3 s)
					//A modifier selon la fr�quence de rafraichissement voulue.
				}
			});
		
			meteoparallele.addSubBehaviour(new CyclicBehaviour(this) {
				public void action()
				{
				ACLMessage msg = receive();
				if(msg!= null)  {
					String A = msg.getContent();
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
