package glados;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import senor_meteo.Tabmeteo;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;


public class AgentEnergie extends Agent{
	
	protected void setup(){
		
		ParallelBehaviour energieparallele = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
		
		energieparallele.addSubBehaviour(new OneShotBehaviour(this){


			public void action(){
				System.out.println(getLocalName()+" lancé");
				// TODO faire planning en tant que fonction pour la mettre ici
				faireplanning();
		  }
			
	  });
		energieparallele.addSubBehaviour(new TickerBehaviour(this,86400000){ // une fois par jour
			protected void onTick() {

				faireplanning();
			}
			});
		
		addBehaviour(energieparallele);
		// ajout du comportement décrit au dessus.
	}

	public void faireplanning(){	
		//recup équipement -> ajout dans init
		
		ArrayList <Equipement> init = new ArrayList <Equipement>();
		ArrayList <Equipement> planning = new ArrayList <Equipement>();
		
		System.out.println("envoi des messages");
		
		envoimessage("senor_meteo","meteo demande");
		receptionmessage("senor_meteo","meteo demande");
		envoimessage("c3po","prefs demandes");
		receptionmessage("c3po","prefs demande");
	//	recup conso max // récupère la consommation max que peut s'autoriser glados à l'instant T
		int consoT[]= new int[24]; 
		Equipement eCourrant;
	//	while(!init.isEmpty()) {
	//			déterminer le plus compliqué 
	//			+grosse conso
	//			++dif entre durée et taille plage faible.
	//	planning.add(eCourrant);
	//	init.remove(eCourrant);
	//	placerEquipement(eCourrant,consoT);}
	//	}
		envoimessage("c3po","planning à écrire ici");
		envoimessage("r2d2","planning à écrire ici");
		receptionmessage("r2d2","planning");
		
	}

	
	public static void placerEquipement(Equipement e, int [] consoT){	
				int min[]= new int[2]; // 0=p 1=i | créa tableau 2 dimension min
				min[0]= Integer.MAX_VALUE;// infini
				for (int i =e.getDébutmin(); i< e.getFinmax()-e.getDurée() ; i++ ){ 		
					if (penalite(e,i,consoT) < min[0]) {			// p = fonction pénalité
					min[0] = penalite(e,i,consoT);
					min[1]=i;
					}
				}
				e.setIndice( min[1]);
				//placer dans le planning // planning [24] ou on add les conso de chaque h
				for (int i=e.getIndice(); i<e.getIndice()+e.getDurée(); i++) {
					consoT[i]+= e.getConso();
					}
				}
	
	public void envoimessage(String destinataire,String contenu){
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setContent(contenu);
		message.addReceiver(new AID(destinataire, AID.ISLOCALNAME));
		send(message);
	}
	
	public void receptionmessage(String agentcontacte, String messageoriginal){
		System.out.println("en attente de receptions");
		ACLMessage msg = blockingReceive();
		System.out.println("reçu message de"+ msg.getSender().getLocalName());
		if (msg.getSender().getLocalName()!=agentcontacte)
		{
			defibrillateur(agentcontacte);
			envoimessage(agentcontacte,messageoriginal);
		}
	}
	
	public void defibrillateur(String agentmort){
		System.out.println("defib en cours");
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
        		
        default: System.out.println("Erreur dans le reboot d'un agent par defibrillateur.") ;
                 break;}
		
	}
	
	public static int penalite (Equipement e,int h, int[] consoT){
		int pTot=0;
		for(int i=h; i<h + e.getDurée();i++){
	//		if(alpha >0)
	//			pTot+= consoT[i] + e.conso;
	//		if(beta1 >0)
	//			ptot += P1
	//		if(beta2 >0)
	//			ptot += P1
	//		if(beta3>0)
	//			ptot += P1
		}
		return pTot;	
		
	}
	}
	



