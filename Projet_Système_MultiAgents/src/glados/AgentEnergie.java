package glados;
import jade.core.Agent;		//package jade
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.UnreadableException;

import java.io.File;		
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;	//package org.json
import org.json.JSONException;
import org.json.JSONObject;

import outils.Outils;


public class AgentEnergie extends jade.core.Agent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static int alpha=1;								//coefficient de pénalité de
	private static int beta1=1;								//coefficient de pénalité de
	private static int beta2=1;								//coefficient de pénalité de
	private static int beta3=1;								//coefficient de pénalité de
	private static File log = new File("log.txt");			//Fichier de log d'erreur
		
	protected void setup(){
		
		ParallelBehaviour energieparallele = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL); //comportement composé des sous-comportement de l'agentEnergie
		
		energieparallele.addSubBehaviour(new OneShotBehaviour(this){	//sous-comportement au lancement
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void action(){
				System.out.println(getLocalName()+" lance");
				
				if (! AgentEnergie.log.exists()) // si le fichier n'existe pas, le creer
				
				{
					try {
						AgentEnergie.log.createNewFile();
					} catch (IOException e) {
					}
				}
				
		         
				try {
					faireplanning(this.myAgent);
				} catch (UnreadableException e) {
					try {
			    		PrintStream printlog = new PrintStream(log);
						printlog.print(this.myAgent.getLocalName());
						e.printStackTrace(printlog);
						printlog.close();
					}
					catch (FileNotFoundException e1) {
					}
				}
		  }
			
	  });
		energieparallele.addSubBehaviour(new TickerBehaviour(this,30000){ // comportement répété une fois toute les 30 secondes
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			protected void onTick() {

				try {
					faireplanning(this.myAgent);
				} catch (UnreadableException e) {
					try {
			    		PrintStream printlog = new PrintStream(log);
						printlog.print(this.myAgent.getLocalName());
						e.printStackTrace(printlog);
						printlog.close();
					}
					catch (FileNotFoundException e1) {
					}
				}
			}
			});
		
		addBehaviour(energieparallele);
		// ajout du comportement decrit au dessus.
	}
	
	
	public void faireplanning(Agent a) throws UnreadableException{	//fonction de réalisation du planning
		//recup equipement -> ajout dans init
		
		ArrayList <Equipement> init = new ArrayList <Equipement>();
		ArrayList <Equipement> planning = new ArrayList <Equipement>();
				
		
		/*Outils.envoimessage("c3po","prefs","prefs", a);	
		Serializable prefs = Outils.receptionobjet("c3po","prefs","prefs","prefs",a);*/	
		
		Outils.envoimessage("c3po","equipements","equipements", a); //demande des equipement à c3po
		String equipstring =(String)Outils.receptionobjet("c3po","equipements","equipements","equipements", a);
		
		equipstring.replace("]","");
		String[] tabequipJson= equipstring.split("\\{");
		JSONObject jsonequip = null;
		JSONArray Arrayequip = new JSONArray() ;
		for (int h = 1; h < tabequipJson.length; h++) {	
			try {
				jsonequip = new JSONObject("{"+tabequipJson[h]);
				Arrayequip.put(jsonequip);
			}
			catch (JSONException e1){
				try {
		    		PrintStream printlog = new PrintStream(log);
					printlog.print(a.getLocalName());
					e1.printStackTrace(printlog);
					printlog.close();
				}
				catch (FileNotFoundException e2){
				}
			}
			}
	
		if (Arrayequip != null) { 
			  for (int i=0;i<Arrayequip.length();i++)				//ajout des equipements dans le tableau init
			  {
				  try {
					init.add(new Equipement(Arrayequip.getJSONObject(i).getString("nom"), Arrayequip.getJSONObject(i).getInt("duree"), Arrayequip.getJSONObject(i).getInt("conso"), Arrayequip.getJSONObject(i).getInt("debut_min"), Arrayequip.getJSONObject(i).getInt("fin_max")));
				} catch (JSONException e) {
					try {
			    		PrintStream printlog = new PrintStream(log);
						printlog.print(a.getLocalName());
						e.printStackTrace(printlog);
						printlog.close();
					}
					catch (FileNotFoundException e1){
					}
				}
			  }
			}
		Collections.sort(init); // tri des equipement dans init pour les mettres du plus compliqué au moins compliqué à placé.(théorie du seau de pierre)
		int consoT[]= new int[24]; 
		Equipement eCourant;
		while(!init.isEmpty()) {
				eCourant=pop(init); 
				planning.add(eCourant);		//placement des équipement dans le planning
				placerEquipement(eCourant,consoT);
		}
		Outils.envoimessage("c3po",planning,"planning",a);		//envoi du planning à qui de droit
		Outils.receptionobjet("c3po",planning,"planning","planning",a);
		Outils.envoimessage("r2d2",planning,"planning",a);
		Outils.receptionobjet("r2d2",planning,"planning","planning", a);
		}

	
	public static void placerEquipement(Equipement e, int [] consoT){	// placement des equipements dans le planning selon la pénalité
				int min[]= new int[2]; 		// 0= penalite 1=indice | creation d'un tableau à 2 dimension
				min[0]= Integer.MAX_VALUE;	// infini
				min[1]=e.getDebutmin();
				for (int i =e.getDebutmin(); i< e.getFinmax()-e.getDuree() ; i++ ){ 
					if (penalite(e,i,consoT) < min[0]) {
					min[0] = penalite(e,i,consoT);
					min[1]=i;
					}
				}
				e.setIndice( min[1]);
				for (int i=e.getIndice(); i<e.getIndice()+e.getDuree(); i++) {					//placement dans le planning
					consoT[i]+= e.getConso();
				}
				System.out.println(e.toString());
		}
	
	public static Equipement pop(ArrayList<Equipement> liste){
		Equipement e=liste.get(0);
		liste.remove(0);
		return e;
	}
	
	public static int penalite (Equipement e,int h, int[] consoT){	//fonction du calcul de la penalité. renvoi la pénalité sous forme d'int
		int pTot=0;
		int[] tempT= new int[consoT.length];
				System.arraycopy(consoT, 0, tempT, 0, consoT.length);
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
		for (int i=0;i<24-2;i++){
			somme+=Math.abs(tempT[i+1]-tempT[i]);
		}
		return somme;
	}
	public static int p2(Equipement e,int[] tempT){
		int somme=0;
		for (int i=0;i<24-2;i++){
			somme+=(tempT[i+1]-tempT[i])^2;
		}
		return somme;
	}
	public static int p3(Equipement e,int[] tempT){
		int somme=0;
		for (int i=0;i<24-2;i++){
			if(tempT[i+1]>tempT[i]) somme+=(tempT[i+1]-tempT[i])^2;
		}
		return somme;
	}
	
}
	



