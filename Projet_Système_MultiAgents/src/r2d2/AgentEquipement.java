package r2d2;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import outils.Outils;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class AgentEquipement extends jade.core.Agent{
	File fichier = new File("sauvr2d2.txt");
	File equips = new File("equipement.txt");
	
	protected void setup(){
		
		ParallelBehaviour equiparallele = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
		
		equiparallele.addSubBehaviour(new OneShotBehaviour(this) 
			{


				@Override
				public void action() {
					
					System.out.println(getLocalName()+" lancé");
					
					
					if (! fichier.exists()) // si le fichier n'existe pas, le créer
					
					{
						try {
							fichier.createNewFile();
						} catch (IOException e) {
							// TODO Auto-generated catch block
						}
					}
					
			         try {
			        	 FileWriter fw = new FileWriter (fichier);
			        	 FileReader fr = new FileReader (fichier);
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
			        	
						if (msg.getLanguage().equals("planning")){
							Outils.envoimessage("glados","confirmation","confplanning",this.myAgent);
						}
			                 break;
			                 
			        	
			        case "ams":
			        	
			        	String contenu = msg.getContent();
			        	String[] separmessage = contenu.split(":");
			        	String contientnom = separmessage[6];
			        	String[] nomagent = contientnom.split(" ");
			        	String agentareboot = nomagent[1]; 
			        	Outils.defibrillateur(agentareboot,this.myAgent);
			        	break;
			        
			        case "dummy":
			        	String heuretext = msg.getContent();
			        	String[] h = heuretext.split("h");
			        	int heure =  Integer.parseInt(h[0]);
			        	int min = Integer.parseInt(h[1]);
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


	
}
