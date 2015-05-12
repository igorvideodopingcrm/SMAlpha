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


public class AgentEnergie extends Agent{
	
	protected void setup(){
		
		ParallelBehaviour energieparallele = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
		
		energieparallele.addSubBehaviour(new OneShotBehaviour(this){


			public void action(){
				System.out.println(getLocalName()+" lanc�");
				// TODO faire planning en tant que fonction pour la mettre ici
				faireplanning();
		  }
			
	  });
		energieparallele.addSubBehaviour(new TickerBehaviour(this,86400000){ // une fois par jour
			protected void onTick() {
				faireplanning();
			}
			});
		
		energieparallele.addSubBehaviour(new CyclicBehaviour(this) {  // boite de reception de message
			public void action()
			{
				ACLMessage msg = receive();
				if(msg!= null)  {
					String A = msg.getContent();
					// TODO g�rer la reception de message contenant les questions et l'envoi de r�ponses.
					}
				else
					{
					block();
					// le block ici bloque le behaviour jusqu'� ce qu'il re�oive un message pour �viter l'attente active
							};
				
			}
		});
		
		addBehaviour(energieparallele);
		// ajout du comportement d�crit au dessus.
	}

	public static void faireplanning(){	
		//recup �quipement -> ajout dans init
		ArrayList <Equipement> init = new ArrayList <Equipement>();
		ArrayList <Equipement> planning = new ArrayList <Equipement>();
		
		ACLMessage demandemeteo = new ACLMessage(ACLMessage.INFORM);
		demandemeteo.setContent("");
		demandemeteo.addReceiver(new AID("senor_meteo", AID.ISLOCALNAME));
	//	send(demandemeteo);
		
	
	//	recup parameter ( pref users) 
		//	-surface
		//	-temperature 
	//	recup conso max // r�cup�re la consommation max que peut s'autoriser glados � l'instant T
		int consoT[]= new int[24]; 
		Equipement eCourrant;
		while(!init.isEmpty()) {
	//		d�terminer le plus compliqu� 
	//			+grosse conso
	//			++dif entre dur�e et taille plage faible.
		//	planning.add(eCourrant);
		//	init.remove(eCourrant);
		//	placerEquipement(eCourrant,consoT);}
	//	}
	//TODO	pr�parer le message ACL pour l'envoi du planning 
		
	}
	}
	
	public static void placerEquipement(Equipement e, int [] consoT){	
				int min[]= new int[2]; // 0=p 1=i | cr�a tableau 2 dimension min
				min[0]= Integer.MAX_VALUE;// infini
				for (int i =e.getD�butmin(); i< e.getFinmax()-e.getDur�e() ; i++ ){ 		
					if (penalite(e,i,consoT) < min[0]) {			// p = fonction p�nalit�
					min[0] = penalite(e,i,consoT);
					min[1]=i;
					}
				}
				e.setIndice( min[1]);
				//placer dans le planning // planning [24] ou on add les conso de chaque h
				for (int i=e.getIndice(); i<e.getIndice()+e.getDur�e(); i++) {
					consoT[i]+= e.getConso();
					}
				}

	public static int penalite (Equipement e,int h, int[] consoT){
		int pTot=0;
		for(int i=h; i<h + e.getDur�e();i++){
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
	



