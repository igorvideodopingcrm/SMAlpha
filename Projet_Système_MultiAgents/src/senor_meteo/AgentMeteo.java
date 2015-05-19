package senor_meteo;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import outils.Outils;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;


public class AgentMeteo extends jade.core.Agent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Meteo[] tabMeteo= new Meteo[7];
	// String nomprecedent = "";
	private static File save = new File("savesenor_meteo.txt");
	
	protected void setup(){
		
		ParallelBehaviour meteoparallele = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
		
		
		
		meteoparallele.addSubBehaviour(new OneShotBehaviour(this){

			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void action(){
				System.out.println(getLocalName()+" lancé");
				// File save = new File("savesenor_meteo.txt");
				if (! AgentMeteo.save.exists()) // si le fichier n'existe pas, le créer
				
				{
					try {
						AgentMeteo.save.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
					}
				}
				
		         
				try {
				
					JSONObject json = senor_meteo.JsonReader.meteoFromUrl();// importation de la météo depuis la fonction du package
					JSONArray listejour = json.getJSONArray("list"); // on prends la liste des prévisions journalière
					String meteo;
					int temperature;
					
									// entrée dans le tableau
					JSONObject temp1;
					for (int i = 0; i < tabMeteo.length; i++) {
						temp1 = listejour.getJSONObject(i);
						temperature = temp1.getJSONObject("temp").getInt("day");
						long date = temp1.getLong("dt");
						meteo = temp1.getJSONArray("weather").getJSONObject(0).getString("description");

						tabMeteo[i] = new Meteo(date,temperature,meteo);			// entrée des données dans le tableau
					}
					  
//					  valtest=10;
				} catch (IOException | JSONException e) {
					// TODO Auto-generated catch block
					System.out.println("erreur: Senor meteo n'a pas reçu de donnée du web.");
					e.printStackTrace();
					
					String format = "MM/dd/yyyy";

					java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat( format );
					java.util.Date date = new java.util.Date();

					
					
					
					if (tabMeteo[0].getDate().split(" ")[0].equals(formater.format( date ))){
						for(int i=1;i < tabMeteo.length; i++){
							tabMeteo[i-1]=tabMeteo[i];
						}
					}
				}
				try {
		        	 FileWriter fw = new FileWriter (save);
		        	 String save ="";
						for (int i = 0; i < tabMeteo.length; i++) {
							save = save+tabMeteo[i].toString()+";";
							}
					fw.write (save);
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
				
		  }
			
	  });
			
		meteoparallele.addSubBehaviour(new TickerBehaviour(this,43200000){// 43200000 = 12 heures en milisecondes | à modifier selon le rafraichissement voulu
				/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

				protected void onTick() {
					try {
						
						JSONObject json = senor_meteo.JsonReader.meteoFromUrl();// importation de la météo depuis la fonction du package
						JSONArray listejour = json.getJSONArray("list"); // on prends la liste des prévisions journalière
						String meteo;
						int temperature;
						
										// entrée dans le tableau
						for (int i = 0; i < tabMeteo.length; i++) {
							JSONObject temp1 = listejour.getJSONObject(i);
							temperature = temp1.getJSONObject("temp").getInt("day");
							long date = temp1.getLong("dt");
							meteo = temp1.getJSONArray("weather").getJSONObject(0).getString("description");

							tabMeteo[i] = new Meteo(date,temperature,meteo);			// entrée des données dans le tableau		
						}
						  

					} catch (IOException | JSONException e) {
						// TODO Auto-generated catch block
						System.out.println("erreur: Senor meteo n'a pas reçu de donnée du web.");
						e.printStackTrace();
					}
					try {
			        	 FileWriter fw = new FileWriter(save);
			        	 String save ="";
							for (int i = 0; i < tabMeteo.length; i++) {
								save = save+tabMeteo[i].toString()+";";
								}
						fw.write (save);
						fw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
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
				if(msg!= null)  {	// lorsqu'un message est traité avec du contenu
					Outils.envoimessage(msg.getSender().getLocalName(),tabMeteo,"meteo",this.myAgent);
					}
				else{
					block();
							};
				}
			});
			
			addBehaviour(meteoparallele);
			// ajout du comportement décrit au dessus.
	}
	
}


