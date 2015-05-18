package r2d2;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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

public class AgentEquipement extends jade.core.Agent{
	private static File saveFile = new File("saver2d2.txt");
	private static File equips = new File("equipement.txt");
	
	protected void setup(){
		
		ParallelBehaviour equiparallele = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
		
		equiparallele.addSubBehaviour(new OneShotBehaviour(this) 
			{


				@Override
				public void action() {
					
					System.out.println(getLocalName()+" lancé");
					
					
					if (!AgentEquipement.saveFile.exists()) // si le fichier n'existe pas, le créer
					
					{
						try {
							AgentEquipement.saveFile.createNewFile();
						} catch (IOException e) {
							// TODO Auto-generated catch block
						}
					}
					
			         try {
			        	 FileWriter fw = new FileWriter (AgentEquipement.saveFile);
			        	 FileReader fr = new FileReader (AgentEquipement.saveFile);
					} catch (IOException e) {
						// TODO Auto-generated catch block
					}
				}
			});
		
		equiparallele.addSubBehaviour(new CyclicBehaviour(this) {
			public void action()
			{
				ACLMessage msg = receive();
				if(msg!= null)  {
					String expe=msg.getSender().getLocalName();
					switch (expe) {
					
			        case "glados": // envoyer les prefs utilisateurs à glados
			        	
			        		if (msg.getContent()=="planning"){
			        			
			        			envoimessage("glados","confirmation planning");
			        		}
			        		
			                 break;
			                 
			        	
			        case "ams":
			        	
			        	String contenu = msg.getContent();
			        	String[] part1 = contenu.split(":");
			        	String separation1 = part1[6];
			        	String[] part2 = separation1.split(" ");
			        	String agentareboot = part2[1]; 
			        	defibrillateur(agentareboot);
			        	break;
			        
			        case "dummy":
			        	String htxt = msg.getContent();
			        	String[] h = htxt.split("h");
			        	int heure =  Integer.parseInt(h[0]);
			        	int min = Integer.parseInt(h[1]);
			        	//regarde l'heure
						//regarde le planning
						//annonce l'état de chaque équipement.
			        	
			        	
			        default: System.out.println("r2d2: reçu " + msg.getContent()); ;
			                 break;}
					

				}
				else{
					block();
							};
				}
			});
		
		addBehaviour(equiparallele);
		// ajout du comportement décrit au dessus.
	}

	public void envoimessage(String destinataire,String contenu){
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setContent(contenu);
		message.addReceiver(new AID(destinataire, AID.ISLOCALNAME));
		send(message);
	}
	
	public void defibrillateur(String agentmort){
		if (agentmort.contains("@"))
		{
			String[] part2 = agentmort.split("@");
        	agentmort = part2[0]; 
		}
		ContainerController cc = getContainerController();
		
		switch (agentmort) {
		
        case "senor_meteo":
        	
        	try {
			AgentController ac = cc.createNewAgent("senor_meteo","senor_meteo.AgentMeteo", null);
			ac.start();} 
        	catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        	};
                 break;
        case "c3po":  
        	
        	try {
			AgentController ac = cc.createNewAgent("c3po","c3po.AgentOccupant", null);
			ac.start();} 
        	catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        	};
                 break;
                 
        case "glados": 
        	try {
			AgentController ac = cc.createNewAgent("senor_meteo","senor_meteo.AgentMeteo", null);
			ac.start();} 
        	catch (StaleProxyException e) {
			// TODO Auto-generated catch block
        		e.printStackTrace();
        	};
        		break; 
        		
        default: System.out.println("Erreur dans le reboot d'un agent par defibrillateur.") ;
                 break;}
		
	}
	
}
