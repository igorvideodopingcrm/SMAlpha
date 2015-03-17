import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;


public class AgentMeteo  extends Agent{
	protected void setup(){
		ParallelBehaviour meteoparallele = new ParallelBehaviour(ParallelBehaviour.WHEN_ANY);
			meteoparallele.addSubBehaviour(new TickerBehaviour(this,3000){
				protected void onTick() {
					//Espace ou Senor Meteo va chercher la météo sur le net pour l'afficher
					//Ou la distribuer à qui de droit. Le timer ci dessus, 3000, est en millième de seconde (actuellement 3 s)
					//A modifier selon la fréquence de rafraichissement voulue.
				}
			});
			meteoparallele.addSubBehaviour(new CyclicBehaviour(this) {
				public void action()
				{
					// gérer la reception de message
					// et l'envoi de message pour donner les prévision météo
				}
			});
	}
}
