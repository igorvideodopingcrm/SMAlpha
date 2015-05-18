package outils;

import java.io.IOException;
import java.io.Serializable;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;


public class Outils {

	public static void defibrillateur(String agentmort, Agent a){ // fonction de défibrillateur. cette fonction sert à reboot des Agents qui seraient tombé
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
			AgentController ac = cc.createNewAgent("senor_meteo","senor_meteo.AgentMeteo", null);
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
		
	}

	public static void envoimessage(String destinataire,String contenu, Agent a){ // fonction d'envoi de message ACL contenant un String  
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setContent(contenu);
		message.addReceiver(new AID(destinataire, AID.ISLOCALNAME));
		a.send(message);
	}

	public static void envoimessage (String destinataire,java.io.Serializable contenu, Agent a){ // fonction d'envoi de message ACL contenant un objet 
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		try {
			message.setContentObject(contenu);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		message.addReceiver(new AID(destinataire, AID.ISLOCALNAME));
		a.send(message);
	}
	
	
	public static Serializable receptionobjet(String agentcontacte, String messageoriginal, Agent a){ // fonction de reception d'objet à l'envoi d'une string 
		ACLMessage msg = a.blockingReceive();
		Serializable contenu;
		if (msg.getSender().getLocalName().contains("ams"))
		{
			defibrillateur(agentcontacte,a);
			envoimessage(agentcontacte,messageoriginal,a);
			contenu=receptionobjet(agentcontacte,messageoriginal,a);
		}
		else
		{
			try {
				contenu=msg.getContentObject();
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				System.out.println("Erreur dans la lecture d'un message d'objet");
				return null;
			}
		}
		return contenu;
	}
	
	
	public static String receptionmessage(String agentcontacte, String messageoriginal, Agent a){ // fonction de reception de string à l'envoi d'une string
		ACLMessage msg = a.blockingReceive();
		String contenu ;
		if (msg.getSender().getLocalName().contains("ams"))
		{
			defibrillateur(agentcontacte, a);
			 envoimessage(agentcontacte,messageoriginal,a);
			contenu =receptionmessage(agentcontacte,messageoriginal,a);
		}
		else{
			contenu = msg.getContent();
		}
		return contenu;
	}
	
	
	public static String receptionmessage(String agentcontacte, java.io.Serializable messageoriginal, Agent a){ // fonction de reception de string à l'envoi d'un objet
		ACLMessage msg = a.blockingReceive();
		String contenu ;
		if (msg.getSender().getLocalName().contains("ams"))
		{
			defibrillateur(agentcontacte, a);
			 envoimessage(agentcontacte,messageoriginal,a);
			contenu =receptionmessage(agentcontacte,messageoriginal,a);
		}
		else{
			contenu = msg.getContent();
		}
		return contenu;
	}

}
