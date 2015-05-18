package outils;

import java.io.IOException;
import java.io.Serializable;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.MessageTemplate.MatchExpression;
import jade.lang.acl.UnreadableException;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import jdk.nashorn.internal.ir.Block;


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
	
	/*public static Serializable receptionobjet(String agentcontacte, Serializable messageoriginal,String titreorigin, String titreattendu, Agent a){
		Serializable contenu;
		
		int nbmessageattente= a.getCurQueueSize();
		if (nbmessageattente==0){
			a.block();
		}
		
		
		contenu=reception(agentcontacte,messageoriginal,titreorigin,titreattendu,a);
		return contenu;
	}
	
	*/
	
	
	public static Serializable receptionobjet(String agentcontacte, Serializable messageoriginal,String titreorigin, String titreattendu, Agent a){ // fonction de reception d'objet à l'envoi d'une string 
				
		Serializable contenu = null;
		MessageTemplate IDams =MessageTemplate.MatchSender(new AID("ams", AID.ISLOCALNAME)); 
		ACLMessage msgams = a.blockingReceive(IDams,1);
		
		if (msgams==null)
			{
			MessageTemplate titre =MessageTemplate.MatchLanguage(titreattendu);
			MessageTemplate IDcontact =MessageTemplate.MatchSender(new AID(agentcontacte, AID.ISLOCALNAME)); 
			MessageTemplate filtre = MessageTemplate.and(titre,IDcontact);
			ACLMessage msgcontact = a.receive(filtre);
			System.out.println("boucle "+agentcontacte);
			if (msgcontact!=null ){
				/*if ( msgcontact.getSender().getLocalName().equals(agentcontacte)){*/
					try {
						contenu=msgcontact.getContentObject();
					} catch (UnreadableException e) {
						// TODO Auto-generated catch block
						a.putBack(msgcontact);
						e.printStackTrace();
					}

				}
			else
				{
				contenu=receptionobjet(agentcontacte,messageoriginal,titreorigin,titreattendu,a);
				}
			}
		else
		{
			String messageams = msgams.getContent();
        	String[] separmessage = messageams.split(":");
        	String contientnom = separmessage[6];
        	String[] nomagent = contientnom.split(" ");
        	String agentareboot = nomagent[1]; 
        	Outils.defibrillateur(agentareboot,a);
        	envoimessage(agentcontacte,messageoriginal,titreorigin,a);
        	contenu=receptionobjet(agentcontacte,messageoriginal,titreorigin,titreattendu,a);
		}
		return contenu;	
	}
	
}
