import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;

public class AgentEquipement extends Agent{
	
	protected void setup(){
		
		ParallelBehaviour equiparallele = new ParallelBehaviour(ParallelBehaviour.WHEN_ANY);
		
		equiparallele.addSubBehaviour(new CyclicBehaviour(this) {
			public void action()
			{
				// gérer la reception de message de Energie pour les actions
				// et le checking des préférences utilisateurs pour ensuite faire
				// ou ne pas faire l'action
			}
		});
		equiparallele.addSubBehaviour(new TickerBehaviour(this,3000){
				protected void onTick() {
					//Espace ou l'agent équipement cherche dans son planning ce qu'il doit faire
					// pour la période à venir et cherche à faire ce qu'il peut pour la période à venir.
				}
			});
		addBehaviour(equiparallele);
		// ajout du comportement décrit au dessus.
	}

}
