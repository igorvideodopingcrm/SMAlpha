package senor_meteo;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
				System.out.println(getLocalName()+" lanc�");
				// File save = new File("savesenor_meteo.txt");
				if (! AgentMeteo.save.exists()) // si le fichier n'existe pas, le cr�er
				{
					try {
						AgentMeteo.save.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
					}
				}
				else 
				{
    				FileReader fr;
					try { // tente de charger la m�t�o du fichier texte
						fr = new FileReader(save);
						BufferedReader br = new BufferedReader(fr);
	    				String linesave = br.readLine();
	    			    String[] tabsemaine = linesave.split(";");
	    			    for (int m = 0; m < tabsemaine.length; m++) {	    			    
	    			    	String[] tabjour = tabsemaine[m].split(",");
	    			    	tabMeteo[m] = new Meteo(tabjour[0],Integer.parseInt(tabjour[1]),tabjour[2]);			    	
	    			    }
	    			    br.close();
	    			    fr.close();
					} catch (IOException e) {
						
					}
    				
				}
				
		         
				try {
				
					JSONObject json = senor_meteo.JsonReader.meteoFromUrl();// importation de la m�t�o depuis la fonction du package
					JSONArray listejour = json.getJSONArray("list"); // on prends la liste des pr�visions journali�re
					String meteo;
					int temperature;
					
									// entr�e dans le tableau
					JSONObject temp1;
					for (int i = 0; i < tabMeteo.length; i++) {
						temp1 = listejour.getJSONObject(i);
						temperature = temp1.getJSONObject("temp").getInt("day");
						long date = temp1.getLong("dt");
						meteo = temp1.getJSONArray("weather").getJSONObject(0).getString("description");

						tabMeteo[i] = new Meteo(date,temperature,meteo);			// entr�e des donn�es dans le tableau
					}
					  
//					  valtest=10;
				} catch (IOException | JSONException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					
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
			
		meteoparallele.addSubBehaviour(new TickerBehaviour(this,43200000){// 43200000 = 12 heures en milisecondes | � modifier selon le rafraichissement voulu
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
						// TODO Auto-generated catch block
						System.out.println("erreur: Senor meteo n'a pas re�u de donn�e du web.");
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
				if(msg!= null)  {	// lorsqu'un message est trait� avec du contenu
					if (msg.getSender().getLocalName().equals("ams")){	// ams envoi un message si l'agent qu'on a cherch� � contact� n'est pas actif
						String agentreboot = Outils.defibrillateur( msg.getContent(),this.myAgent);
					}
					else{
					Outils.envoimessage(msg.getSender().getLocalName(),tabMeteo,"meteo",this.myAgent);
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


