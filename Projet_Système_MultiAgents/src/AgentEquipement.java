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
				// g�rer la reception de message de Energie pour les actions
				// et le checking des pr�f�rences utilisateurs pour ensuite faire
				// ou ne pas faire l'action
			}
		});
		equiparallele.addSubBehaviour(new TickerBehaviour(this,3000){
				protected void onTick() {
					//Espace ou l'agent �quipement cherche dans son planning ce qu'il doit faire
					// pour la p�riode � venir et cherche � faire ce qu'il peut pour la p�riode � venir.
				}
			});
		addBehaviour(equiparallele);
		// ajout du comportement d�crit au dessus.
	}

}
