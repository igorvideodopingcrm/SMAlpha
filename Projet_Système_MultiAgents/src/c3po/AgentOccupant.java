package c3po;
import glados.Equipement; 		// importation des packages du systeme multi agent
import senor_meteo.JsonReader;	
import senor_meteo.Meteo;
import outils.Smalpha;

import java.io.FileNotFoundException; // libraire java
import java.io.FileReader;
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


public class AgentOccupant extends Smalpha{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static File serveur=new File("serveur.txt"); //adresse du serveur dans le fichier serveur.txt pour l'envoi des données|interface utilisateur
	private static File log = new File("log.txt");			//Fichier de log d'erreur 
	
	protected void setup(){	

			ParallelBehaviour occuparallele = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);//comportement composé des sous-comportement de l'agentOccupant
			occuparallele.addSubBehaviour(new TickerBehaviour(this,30000){ // comportement de demande cyclique de la météo
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				
				protected void onTick(){
					
					((Smalpha) this.myAgent).envoimessage("senor_meteo","demandemeteo","demandemeteo");
				}
			});
			
			occuparallele.addSubBehaviour(new CyclicBehaviour(this) { // comportement "boite de reception"
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				
				
				public void action(){
				ACLMessage msg = receive();								//on cherche à obtenir un message de la boite de reception
				if(msg!= null)  {										//si on n'a pas de message: msg == null;
					if (msg.getSender().getLocalName().equals("ams")){			// ams envoi un message si l'agent qu'on a cherché à contacté n'est pas actif|est mort
						String agentreboot = ((Smalpha) this.myAgent).defibrillateur( msg.getContent());// on réanime l'agent qui est mort|absent
						if (agentreboot.contains("senor_meteo")){		//selon l'agent reboot, on lui renvoie le message
							((Smalpha) this.myAgent).envoimessage("senor_meteo","demandemeteo","demandemeteo");
						}
					}
					else{
						switch (msg.getLanguage()){	//on a reçu un message, on en cherche le sujet
						
						/*case "prefs":  // cas d'envoi des préférence utilisateur à faire gérer par l'agentEnergie ( actuellement géré coté serveur web)			
								System.out.println("prefs envoyées");
								this.envoimessage("glados","prefs","prefs",this.myAgent);
								break;*/ 
						
							case "planning": // le planning vient d'être reçu de l'agentEnergie
								((Smalpha) this.myAgent).envoimessage("glados","confirmation planning","planning");
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
									((Smalpha) this.myAgent).envoimessage("glados",listequip,"equipements");
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
	        		
							case "meteo":  // envoyer la météo sur l'application
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
								printlog.print(this.myAgent.getLocalName() +" message non traité:\n "+msg.getLanguage());
								printlog.close();
							} catch (FileNotFoundException e) {								
								e.printStackTrace();
							}
								break;
						}
					}	
				}
				else{
					block();	// si on n'a pas reçu de message, on bloque le comportement jusqu'à en recevoir un.
				}
				}
			});
			
			if (! AgentOccupant.log.exists()) // si le fichier n'existe pas, le creer
			{
				try {
					AgentOccupant.log.createNewFile();
				} catch (IOException e) {
				}
			}
			
			System.out.println(this.getLocalName()+" lancé");  // ensemble de tache au lancement
			// envoi de message à senor meteo pour obtenir la météo et la traiter avant toute chose.
			this.envoimessage("senor_meteo","meteo demande","meteo demande");
			Meteo[]tabMeteo= new Meteo[7];
			tabMeteo=(Meteo[]) this.receptionobjet("senor_meteo","meteo","meteo","meteo");
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
			addBehaviour(occuparallele); // on ajoute le comportement qu'on a décrit au dessus.
	}
	
	
	public static JSONArray postserver(String title,String message,Agent a) throws IOException {	//fonction d'envoi de donnée au serveur web. retourne un JSONArray des données|de confirmation
		String tempText ="";
		try {
			FileReader fr = new FileReader(AgentOccupant.serveur);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
		    tempText = "http://"+line+"/c3po.php?"+title+"="+message;
		    br.close();
		    fr.close();
		}
		catch (IOException e){
			try {
	    		PrintStream printlog = new PrintStream(log);
				printlog.print(a.getLocalName());
				e.printStackTrace(printlog);
				printlog.close();
			}
			catch (FileNotFoundException e1) {
			}
		}	
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
