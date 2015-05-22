package senor_meteo;

import outils.Smalpha;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

import org.json.JSONArray;		//import de org.json
import org.json.JSONException;
import org.json.JSONObject;

import jade.core.behaviours.CyclicBehaviour;	//import de jade
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;


public class AgentMeteo extends Smalpha{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Meteo[] tabMeteo= new Meteo[7];
	private static File save = new File("savesenor_meteo.txt");
	private static File log = new File("log.txt");			//Fichier de log d'erreur
	protected void setup(){
		
		ParallelBehaviour meteoparallele = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
		
		meteoparallele.addSubBehaviour(new OneShotBehaviour(this){	//comportement d'initalisation au lancement.

			/**
			 * 
			 */

			public void action(){
				System.out.println(getLocalName()+" lanc�"); 
				
				if (! AgentMeteo.log.exists()) 			// si le fichier n'existe pas, le creer
				{
					try {
						AgentMeteo.log.createNewFile();
					} catch (IOException e) {
					}
				}
				if (! AgentMeteo.save.exists()) 		// si le fichier n'existe pas, le cr�er
				{
					try {
						AgentMeteo.save.createNewFile();
					} catch (IOException e) {
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
				try {
				
					JSONObject json = senor_meteo.JsonReader.meteoFromUrl();	// importation de la m�t�o depuis la fonction du package
					JSONArray listejour = json.getJSONArray("list"); 			// on prends la liste des pr�visions journali�re
					String meteo;
					int temperature;
									
					JSONObject temp1;			
					for (int i = 0; i < tabMeteo.length; i++) {
						temp1 = listejour.getJSONObject(i);
						temperature = temp1.getJSONObject("temp").getInt("day");
						long date = temp1.getLong("dt");
						meteo = temp1.getJSONArray("weather").getJSONObject(0).getString("description");
						tabMeteo[i] = new Meteo(date,temperature,meteo);			// entr�e des donn�es dans le tableau
					}
				}
				catch (IOException | JSONException e){
					try {
						PrintStream printlog = new PrintStream(log);
						printlog.print(this.myAgent.getLocalName());
						e.printStackTrace(printlog);
						printlog.close();
					}
					catch (FileNotFoundException e1) {
					}
					String format = "MM/dd/yyyy";	//chargement de la m�t�o du bloc note
					java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat( format );
					java.util.Date date = new java.util.Date();					
					if (!tabMeteo[0].getDate().split(" ")[0].equals(formater.format( date ))){
						for(int i=1;i < tabMeteo.length; i++){
							tabMeteo[i-1]=tabMeteo[i];
						}
					}
				}
				try{
		        	 FileWriter fw = new FileWriter (save);	// tente de charger la m�t�o du fichier texte par rapport au jour actuel
		        	 String save ="";
						for (int i = 0; i < tabMeteo.length; i++) {
							save = save+tabMeteo[i].toString()+";";
							}
					fw.write (save);
					fw.close();
				}
				catch (IOException e){
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
			
		meteoparallele.addSubBehaviour(new TickerBehaviour(this,30000){// behaviour charg� de rafraichier la m�t�o. dur�e en miliseconde � changer selon la dur�e ce que l'on souhaite.
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

				protected void onTick() {
					try {
						
						JSONObject json = senor_meteo.JsonReader.meteoFromUrl();// importation de la m�t�o depuis la fonction du package
						JSONArray listejour = json.getJSONArray("list"); // on prends la liste des pr�visions journali�re
						String meteo;
						int temperature;
						
										// entr�e dans le tableau
						for (int i = 0; i < tabMeteo.length; i++) {
							JSONObject temp1 = listejour.getJSONObject(i);
							temperature = temp1.getJSONObject("temp").getInt("day");
							long date = temp1.getLong("dt");
							meteo = temp1.getJSONArray("weather").getJSONObject(0).getString("description");

							tabMeteo[i] = new Meteo(date,temperature,meteo);			// entr�e des donn�es dans le tableau		
						}
						  

					} catch (IOException | JSONException e) {
						try {
							PrintStream printlog = new PrintStream(log);
							printlog.print(this.myAgent.getLocalName());
							e.printStackTrace(printlog);
							printlog.close();
						}
						catch (FileNotFoundException e1) {
						}
						
						e.printStackTrace();
						String format = "MM/dd/yyyy";

						java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat( format );
						java.util.Date date = new java.util.Date();

						
						
						
						if (!tabMeteo[0].getDate().split(" ")[0].equals(formater.format( date ))){
							for(int i=1;i < tabMeteo.length; i++){
								tabMeteo[i-1]=tabMeteo[i];
							}
						}
					}
					try {
			        	 FileWriter fw = new FileWriter(save);	// tente de charger la m�t�o du fichier texte par rapport au jour actuel
			        	 String save ="";
							for (int i = 0; i < tabMeteo.length; i++) {
								save = save+tabMeteo[i].toString()+";";
								}
						fw.write (save);
						fw.close();
					} catch (IOException e) {
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
			
			meteoparallele.addSubBehaviour(new CyclicBehaviour(this) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public void action()
				{
					ACLMessage msg = receive();
					if(msg!= null)  {										// lorsqu'un message est trait� avec du contenu
						if (msg.getSender().getLocalName().equals("ams")){	// ams envoi un message si l'agent qu'on a cherch� � contact� n'est pas actif
							((Smalpha) this.myAgent).defibrillateur( msg.getContent());
						}
						else{  // l'agent est actif et demande la m�t�o
						((Smalpha) this.myAgent).envoimessage(msg.getSender().getLocalName(),tabMeteo,"meteo");
						}
					}
					else{
					block();
					};
				}
			});
			
			addBehaviour(meteoparallele);
			// ajout du comportement d�crit au dessus.
	}
	
}


