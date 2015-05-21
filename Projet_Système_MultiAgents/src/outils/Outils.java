package outils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.concurrent.Semaphore;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;


public class Outils {


	
	static Semaphore boitemess = new Semaphore(1);

	
	public static String defibrillateur(String messageagentmort, Agent a){ // fonction de défibrillateur. cette fonction sert à reboot des Agents qui seraient tombé
		
		String[] separmessage = messageagentmort.split(":");
		String contientnom = separmessage[6];
		String[] nomagent = contientnom.split(" ");
		String agentmort = nomagent[1]; 
		
		if (agentmort.contains("@"))
		{
			String[] part2 = agentmort.split("@");
        	agentmort = part2[0]; 
		}
		ContainerController cc = a.getContainerController();
		
		switch (agentmort) {
		
        case "r2d2":
        	
        	try {
			AgentController ac = cc.createNewAgent("r2d2","r2d2.AgentEquipement", null);
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
			AgentController ac = cc.createNewAgent("glados","glados.AgentEnergie", null);
			ac.start();} 
        	catch (StaleProxyException e) {
			// TODO Auto-generated catch block
        		e.printStackTrace();
        	};
        		break;
        		
        case "senor_meteo":
        	
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
		return agentmort;
	}


		
	public static void envoimessage(String destinataire,java.io.Serializable contenu,String titre, Agent a){ // fonction d'envoi de message ACL contenant un objet 
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		try {
			message.setContentObject(contenu);
			message.setLanguage(titre);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		message.addReceiver(new AID(destinataire, AID.ISLOCALNAME));
		a.send(message);
	}

		
	public static Serializable receptionobjet(String agentcontacte, Serializable messageoriginal,String titreorigin, String titreattendu, Agent receveur){ // fonction de reception d'objet à l'envoi d'une string 
		
		Serializable contenu = null;
		boolean msgrecu =false;
		MessageTemplate IDams =MessageTemplate.MatchSender(new AID("ams", AID.ISLOCALNAME)); 
		
		MessageTemplate titre =MessageTemplate.MatchLanguage(titreattendu);
		MessageTemplate IDcontact =MessageTemplate.MatchSender(new AID(agentcontacte, AID.ISLOCALNAME)); 
		MessageTemplate filtreAttendu = MessageTemplate.and(titre,IDcontact);
		
		MessageTemplate msgAccepte = MessageTemplate.or(IDams,filtreAttendu);
		ACLMessage msg = receveur.blockingReceive(msgAccepte);
		
		while (!msgrecu){
			if (!msg.getSender().getLocalName().contains(agentcontacte))
			{	Outils.defibrillateur(msg.getContent(),receveur);
				envoimessage(agentcontacte,messageoriginal,titreorigin,receveur);
				msg = receveur.blockingReceive(msgAccepte);
			}
			else{
				try {
					contenu=msg.getContentObject();
					msgrecu = true;
				} catch (UnreadableException e) {
					receveur.putBack(msg);
					try {
			    		PrintStream printlog = new PrintStream("log.txt");
						printlog.print(receveur.getLocalName());
						e.printStackTrace(printlog);
						printlog.close();
					}
					catch (FileNotFoundException e1){
					}
					msgrecu = true;
				}
			}
		}
		return contenu;

	} 
	
	
	
	
}
