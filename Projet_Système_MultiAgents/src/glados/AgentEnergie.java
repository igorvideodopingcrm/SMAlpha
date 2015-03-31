package glados;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class AgentEnergie extends Agent{
	
	protected void setup(){
		
		ParallelBehaviour energieparallele = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
		
		energieparallele.addSubBehaviour(new TickerBehaviour(this,3000){
			protected void onTick() {
			// TODO Calcul et envoi du planning Equipement
			}
		});
		
		energieparallele.addSubBehaviour(new CyclicBehaviour(this) {
			public void action()
			{
				ACLMessage msg = receive();
				if(msg!= null)  {
					String A = msg.getContent();
					// TODO g�rer la reception de message contenant les questions et l'envoi de r�ponses.
					}
				else
					{
					block();
					// le block ici bloque le behaviour jusqu'� ce qu'il re�oive un message pour �viter l'attente active
					
							};
				
			}
		});
		
		
		energieparallele.addSubBehaviour(new TickerBehaviour(this,3000){
			protected void onTick() {
				// TODO Recuperation de la M�t�o � Senor Meteo
			}
		});
		
		addBehaviour(energieparallele);
		// ajout du comportement d�crit au dessus.
	}

}
