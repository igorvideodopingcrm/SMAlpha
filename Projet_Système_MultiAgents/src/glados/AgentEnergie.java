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
				System.out.println(getLocalName()+" lance");
				
				if (! fichier.exists()) // si le fichier n'existe pas, le creer
				
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
		// ajout du comportement decrit au dessus.
	}
	
	
	public void faireplanning(Agent a){
		//recup equipement -> ajout dans init
		
		ArrayList <Equipement> init = new ArrayList <Equipement>();
		ArrayList <Equipement> planning = new ArrayList <Equipement>();
				
		envoimessage("senor_meteo","meteo demande");
		String meteo = receptionmessage("senor_meteo","meteo demande");
		System.out.println(meteo);
		envoimessage("c3po","prefs");
		envoimessage("c3po","equipements");
		
		String prefs = receptionmessage("c3po","prefs");
 // CORRIGER ICI !!!!
		Outils.envoimessage("c3po","","demandeprefs",a);
		
		System.out.println("attentes prefs");
		Serializable prefs = Outils.receptionobjet("c3po","prefsutilisateur","demandeprefs","prefs",a);
		JSONArray equipements = receptionmessage("c3po","equipements");
		System.out.println(prefs);
		
		Outils.envoimessage("senor_meteo","","demandemeteo",a);
		tab =(Meteo[])Outils.receptionobjet("senor_meteo","meteo demande","demandemeteo","meteo",a);
		
		for (int i = 0; i < tab.length; i++) {
			System.out.println(tab[i].toString());
		}

		
	//	recup conso max // récupère la consommation max que peut s'autoriser glados à l'instant T
		int consoT[]= new int[24]; 
		Equipement eCourant;
		while(!init.isEmpty()) {
				eCourant=pop(init); 
				planning.add(eCourrant);
				placerEquipement(eCourrant,consoT);}
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
		Outils.envoimessage("c3po",planning,"planning",a);
		String S = (String) Outils.receptionobjet("c3po",planning,"planning","confplanning",a);
		Outils.envoimessage("r2d2",planning,"planning",a);
		String J = (String) Outils.receptionobjet("r2d2",planning,"planning","confplanning", a);
	}

	
	public static void placerEquipement(Equipement e, int [] consoT){	
				int min[]= new int[2]; // 0=p 1=i | crea tableau 2 dimension min
				min[0]= Integer.MAX_VALUE;// infini
				for (int i =e.getDebutmin(); i< e.getFinmax()-e.getDuree() ; i++ ){ 		
					if (penalite(e,i,consoT) < min[0]) {			// p = fonction penalite
					min[0] = penalite(e,i,consoT);
					min[1]=i;
					}
				}
				e.setIndice( min[1]);
				//placer dans le planning // planning [24] ou on add les conso de chaque h
				for (int i=e.getIndice(); i<e.getIndice()+e.getDuree(); i++) {
					consoT[i]+= e.getConso();
					}
				}
	
	public static Equipement pop(ArrayList liste){
		Equipement e=liste.get(0);
		liste.remove(0);
		return e;
	}
	
	public static int penalite (Equipement e,int h, int[] consoT){
		int pTot=0;
		int[] tempT=Array.copyOf(consoT,consoT.length);
		for(int i=h; i<h + e.getDuree();i++){
			tempT[i]+=e.getConso();
			if(AgentEnergie.alpha >0)
				pTot+= AgentEnergie.alpha*(tempT[i]);
		}
		if(AgentEnergie.beta1 >0)
			pTot += AgentEnergie.beta1*p1(e,tempT);
		if(AgentEnergie.beta2 >0)
			pTot += AgentEnergie.beta2*p2(e,tempT);
		if(AgentEnergie.beta3>0)
			pTot += AgentEnergie.beta3*p3(e,tempT);
		
		return pTot;	
		
	}
	public static int p1(Equipement e,int[] tempT){
		int somme=0;
		for (int i=0;i<24-1;i++){
			somme+=Math.abs(tempT[i+1]-tempT[i]);
		}
		return somme;
	}
	public static int p2(Equipement e,int[] tempT){
		int somme=0;
		for (int i=0;i<24-1;i++){
			somme+=(tempT[i+1]-tempT[i])^2;
		}
		return somme;
	}
	public static int p3(Equipement e,int[] tempT){
		int somme=0;
		for (int i=0;i<24-1;i++){
			if(tempT[i+1]>tempT[i]) somme+=(tempT[i+1]-tempT[i])^2;
		}
		return somme;
	}
	
}
	



