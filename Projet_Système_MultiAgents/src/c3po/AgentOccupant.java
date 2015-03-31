package c3po;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;


public class AgentOccupant  extends Agent{
	protected void setup(){

			addBehaviour(new GUIBehaviour());
			// ajout du comportement d�crit au dessus.
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
			// TODO Methode � appeler � la fin de la GUI, mettre TRUE pour fermer le Behaviour, False Pour qu'il se rouvre de mani�re cyclique
			// CF voir les Behaviour One shot et les behaviours cycliques
			return false;
		}
		
	}
}
