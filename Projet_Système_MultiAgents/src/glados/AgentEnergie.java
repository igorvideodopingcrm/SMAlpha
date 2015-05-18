package glados;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import senor_meteo.Meteo;
import outils.Outils;


public class AgentEnergie extends jade.core.Agent{
	File fichier = new File("sauvglados.txt");
	Meteo[] tab= new Meteo[7];
	
	protected void setup(){
		
		ParallelBehaviour energieparallele = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
		
		energieparallele.addSubBehaviour(new OneShotBehaviour(this){


			public void action(){
				System.out.println(getLocalName()+" lanc�");
				
				if (! fichier.exists()) // si le fichier n'existe pas, le cr�er
				
				{
					try {
						fichier.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
					}
				}
				
		         
				faireplanning(this.myAgent);
		  }
			
	  });
		energieparallele.addSubBehaviour(new TickerBehaviour(this,86400000){ // une fois par jour
			protected void onTick() {

				faireplanning(this.myAgent);
			}
			});
		
		addBehaviour(energieparallele);
		// ajout du comportement d�crit au dessus.
	}
	
	
	public void faireplanning(Agent a){
		//recup �quipement -> ajout dans init
		
		ArrayList <Equipement> init = new ArrayList <Equipement>();
		ArrayList <Equipement> planning = new ArrayList <Equipement>();
				
		Outils.envoimessage("senor_meteo","meteo demande",a);
		tab =(Meteo[])Outils.receptionobjet("senor_meteo","meteo demande",a);
				for (int i = 0; i < tab.length; i++) {
					System.out.println(tab[i].toString());
				}
		
		Outils.envoimessage("c3po","prefs",a);
		String prefs = Outils.receptionmessage("c3po","prefs",a);
		System.out.println(prefs);
		
	//	recup conso max // r�cup�re la consommation max que peut s'autoriser glados � l'instant T
		int consoT[]= new int[24]; 
		Equipement eCourrant;
	//	while(!init.isEmpty()) {
	//			d�terminer le plus compliqu� 
	//			+grosse conso
	//			++dif entre dur�e et taille plage faible.
	//	planning.add(eCourrant);
	//	init.remove(eCourrant);
	//	placerEquipement(eCourrant,consoT);}
	//	}
		try {
			FileWriter fw = new FileWriter("sauvglados.txt");
			FileReader fr = new FileReader("sauvglados.txt");
			BufferedReader br = new BufferedReader (fr);
			fw.write("planning sauv glados");
			fw.close();
			String line = br.readLine();
			 
		        while (line != null)
		        {
		            System.out.println (line);
		            line = br.readLine();
		        }
		 
		    br.close();
		    fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Outils.envoimessage("c3po",planning,a);
		String S = Outils.receptionmessage("c3po",planning,a);
		Outils.envoimessage("r2d2",planning, a);
		String J = Outils.receptionmessage("r2d2",planning,a);
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
	