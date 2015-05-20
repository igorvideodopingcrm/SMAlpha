package c3po;
import glados.Equipement; 		// importation des packages du systeme multi agent
import senor_meteo.JsonReader;	
import senor_meteo.Meteo;
import outils.Outils;

import java.io.FileNotFoundException; // libraire java
import java.io.IOException;		
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.net.URL;
import java.nio.charset.Charset;
import java.lang.String;

import org.json.JSONArray;		// jar org.json
import org.json.JSONException;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.*;


public class AgentOccupant extends jade.core.Agent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String server="192.168.1.42"; 			//adresse du serveur pour l'envoi des donn�es|interface utilisateur
	private static File log = new File("log.txt");			//Fichier de log d'erreur 
	
	protected void setup(){	

			ParallelBehaviour occuparallele = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);//comportement compos� des sous-comportement de l'agentOccupant
			occuparallele.addSubBehaviour(new OneShotBehaviour(this){								//sous-Comportement au lancement
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public void action(){
					
					System.out.println(this.myAgent.getLocalName()+" lanc�");
			  }	
		  });
			occuparallele.addSubBehaviour(new TickerBehaviour(this,30000){ // comportement de demande cyclique de la m�t�o
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				
				protected void onTick(){
					
					Outils.envoimessage("senor_meteo","demandemeteo","demandemeteo", this.myAgent);
				}
			});
			
			occuparallele.addSubBehaviour(new CyclicBehaviour(this) { // comportement "boite de reception"
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				
				
				public void action(){
				ACLMessage msg = receive();								//on cherche � obtenir un message de la boite de reception
				if(msg!= null)  {										//si on n'a pas de message: msg == null;
					if (msg.getSender().getLocalName().equals("ams")){			// ams envoi un message si l'agent qu'on a cherch� � contact� n'est pas actif|est mort
						String agentreboot = Outils.defibrillateur( msg.getContent(),this.myAgent);// on r�anime l'agent qui est mort|absent
						if (agentreboot.contains("senor_meteo")){		//selon l'agent reboot, on lui renvoie le message
							Outils.envoimessage("senor_meteo","demandemeteo","demandemeteo", this.myAgent);
						}
					}
					else{
						switch (msg.getLanguage()){	//on a re�u un message, on en cherche le sujet
						
						/*case "prefs":  // cas d'envoi des pr�f�rence utilisateur � faire g�rer par l'agentEnergie ( actuellement g�r� cot� serveur web)			
								System.out.println("prefs envoy�es");
								Outils.envoimessage("glados","prefs","prefs",this.myAgent);
								break;*/ 
						
							case "planning": // le planning vient d'�tre re�u de l'agentEnergie
								System.out.println("planning re�u");
								Outils.envoimessage("glados","confirmation planning","planning",this.myAgent);
								ArrayList <Equipement> planning = new ArrayList <Equipement>();
								try {
									planning = (ArrayList<Equipement>) msg.getContentObject();
									String envoi1 = "";
									for (int j=0;j<planning.size();j++){
										envoi1 = envoi1+planning.get(j).toString();
									}
									postserver("planning",envoi1,this.myAgent);
								}
								catch (UnreadableException|IOException e) {
									try {
										PrintStream printlog = new PrintStream(log);
										printlog.print(this.myAgent.getLocalName());
										e.printStackTrace(printlog);
										printlog.close();
									}
									catch (FileNotFoundException e1) {
									}
								}
								break;

							case "equipements":// l'agentEnergie demande les equipements
								
								try {
									JSONArray jsonEquip=postserver("equipements","",this.myAgent);	
									String listequip = jsonEquip.toString();
									Outils.envoimessage("glados",listequip,"equipements",this.myAgent);
								}
								catch (IOException e) {
									try {
										PrintStream printlog = new PrintStream(log);
										printlog.print(this.myAgent.getLocalName());
										e.printStackTrace(printlog);
										printlog.close();
									}
									catch (FileNotFoundException e1) {
									}
								}
								break;
	        		
							case "meteo":  // envoyer la m�t�o sur l'application
								try {
									Meteo[]tabMeteo= new Meteo[7];
									tabMeteo=(Meteo[])msg.getContentObject();
									String envoi ="";
									for (int i = 0; i < tabMeteo.length; i++) {
										envoi = envoi+tabMeteo[i].toString()+";";
									}
									postserver("meteo",envoi,this.myAgent);
								}
								catch (UnreadableException|IOException e) {
									try {
										PrintStream printlog = new PrintStream(log);
										printlog.print(this.myAgent.getLocalName());
										e.printStackTrace(printlog);
										printlog.close();
									}
									catch (FileNotFoundException e1) {
									}
								}
								break;
								
							default:
									
							PrintStream printlog;
							try {
								printlog = new PrintStream(log);
								printlog.print(this.myAgent.getLocalName() +" message non trait�:\n "+msg.getLanguage());
								printlog.close();
							} catch (FileNotFoundException e) {								
								e.printStackTrace();
							}
								break;
						}
					}	
				}
				else{
					block();	// si on n'a pas re�u de message, on bloque le comportement jusqu'� en recevoir un.
				}
				}
			});
			
			// envoi de message � senor meteo pour obtenir la m�t�o et la traiter avant toute chose.
			Outils.envoimessage("senor_meteo","meteo demande","meteo demande", this);
			Meteo[]tabMeteo= new Meteo[7];
			tabMeteo=(Meteo[])Outils.receptionobjet("senor_meteo","meteo","meteo","meteo", this);
			String envoi ="";
			for (int i = 0; i < tabMeteo.length; i++) {
				envoi = envoi+tabMeteo[i].toString()+";";
			}
			try {
				postserver("meteo",envoi,this);
				
			} catch (IOException e) {
				try {
					PrintStream printlog = new PrintStream(log);
					printlog.print(this.getLocalName());
					e.printStackTrace(printlog);
					printlog.close();
				}
				catch (FileNotFoundException e1) {
				}
			}
			addBehaviour(occuparallele); // on ajoute le comportement qu'on a d�crit au dessus.
	}
	
	
	public static JSONArray postserver(String title,String message,Agent a) throws IOException {	//fonction d'envoi de donn�e au serveur web. retourne un JSONArray des donn�es|de confirmation
			String tempText = "http://"+AgentOccupant.server+"/SMAlpha_html/c3po.php?"+title+"="+message;
			tempText=tempText.replace(" ","_");
		    InputStream is = new URL(tempText).openStream();
		    try {
		      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		      String jsonText = JsonReader.readAll(rd);
		      JSONArray json = new JSONArray(jsonText);
		      return json;
		    }
		    catch (JSONException e) {
		    	try {
		    		PrintStream printlog = new PrintStream(log);
					printlog.print(a.getLocalName());
					e.printStackTrace(printlog);
					printlog.close();
				}
				catch (FileNotFoundException e1) {
				}
			}
		    finally{
		      is.close();
		    }
			return null;
	}
	

}
