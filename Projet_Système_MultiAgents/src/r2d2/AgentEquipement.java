package r2d2;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class AgentEquipement extends Agent{
	
	protected void setup(){
		
		ParallelBehaviour equiparallele = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
		
		equiparallele.addSubBehaviour(new OneShotBehaviour(this) 
			{


				@Override
				public void action() {
					
					System.out.println(getLocalName()+" lanc�");
					
					ContainerController cc = getContainerController();
					try {
						AgentController ac = cc.createNewAgent("senor_meteo","senor_meteo.AgentMeteo", null);
						ac.start();
					} catch (StaleProxyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					ACLMessage message = new ACLMessage(ACLMessage.INFORM);
					message.setContent("je tente de contacter c3po");
					AID a = new AID();
					a.setName("c3po");
					message.addReceiver(a);;
					send(message);
					System.out.println("message envoy� ");
					
					
			//		ACLMessage mes = new ACLMessage(ACLMessage.INFORM);
			//		mes.setContent("je tente de contacter senor_meteo");
			//		mes.addReceiver(new AID("senor_meteo", AID.ISLOCALNAME));;
			//		send(mes);
			//		System.out.println("message envoy� ");
				}
			});
		
		
		equiparallele.addSubBehaviour(new CyclicBehaviour(this) {
			public void action()
			{
				//TODO g�rer la reception de message du planning energie
			}
		});
		
		
		
		equiparallele.addSubBehaviour(new CyclicBehaviour(this) {
			public void action()
			{
			ACLMessage msg = blockingReceive();
			//System.out.println("je suis r2d2 et j'ai re�u un message de " + msg.getContent());
			System.out.println("je suis r2d2 et j'ai re�u un message de " );
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

	public void envoimessage(String destinataire,String contenu){
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setContent(contenu);
		message.addReceiver(new AID(destinataire, AID.ISLOCALNAME));
		send(message);
	}
	
}
