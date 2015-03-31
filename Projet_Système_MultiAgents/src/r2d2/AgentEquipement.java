package r2d2;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;

public class AgentEquipement extends Agent{
	
	protected void setup(){
		
		ParallelBehaviour equiparallele = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
		
		equiparallele.addSubBehaviour(new CyclicBehaviour(this) {
			public void action()
			{
				//TODO g�rer la reception de message de Energie pour une action et le checking des pr�f�rences utilisateurs
			}
		});
		equiparallele.addSubBehaviour(new CyclicBehaviour(this) {
			public void action() {
				//TODO utilisation du planning re�u par l'agentEquipement.
					
			}
		});
		addBehaviour(equiparallele);
		// ajout du comportement d�crit au dessus.
	}

}
