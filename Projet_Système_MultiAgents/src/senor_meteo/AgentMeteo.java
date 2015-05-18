package senor_meteo;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;


public class AgentMeteo extends jade.core.Agent{

	Meteo[] tab= new Meteo[7];
	String nomprecedent = "";
	File fichier = new File("sauvsenor_meteo.txt");
	
	protected void setup(){
		
		ParallelBehaviour meteoparallele = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
		
		
		
		meteoparallele.addSubBehaviour(new OneShotBehaviour(this){

			
			public void action(){
				System.out.println(getLocalName()+" lancé");
				File fichier = new File("sauvsenor_meteo.txt");
				if (! fichier.exists()) // si le fichier n'existe pas, le créer
				
				{
					try {
						fichier.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
					}
				}
				
		         
				try {
				
					JSONObject json = senor_meteo.JsonReader.meteoFromUrl();// importation de la météo depuis la fonction du package
					JSONArray listejour = json.getJSONArray("list"); // on prends la liste des prévisions journalière
					long valdat =0; 								// initialisation des variable et du tableau ou on les rentre
					String meteo;
					int temperature;
					
									// entrée dans le tableau
					JSONObject temp1;
					for (int i = 0; i < tab.length; i++) {
						temp1 = listejour.getJSONObject(i);
						temperature = temp1.getJSONObject("temp").getInt("day");
						valdat=temp1.getLong("dt");
						meteo = temp1.getJSONArray("weather").getJSONObject(0).getString("description");

						tab[i] = new Meteo();			// entrée des données dans le tableau
						tab[i].setDate(valdat);
						tab[i].setTemperature(temperature);
						tab[i].setMeteo(meteo);
						 
						}
					  
//					  valtest=10;
				} catch (IOException | JSONException e) {
					// TODO Auto-generated catch block
					System.out.println("erreur: Senor meteo n'a pas reçu de donnée du web.");
					e.printStackTrace();
				}
				try {
		        	 FileWriter fw = new FileWriter (fichier);
		        	 String save ="";
						for (int i = 0; i < tab.length; i++) {
							save = save+tab[i].toString()+";";
							}
					fw.write (save);
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
				
		  }
			
	  });
			
		meteoparallele.addSubBehaviour(new TickerBehaviour(this,43200000){// 43200000 = 12 heures en milisecondes | à modifier selon le rafraichissement voulu
				protected void onTick() {
					try {
						
						JSONObject json = senor_meteo.JsonReader.meteoFromUrl();// importation de la météo depuis la fonction du package
						JSONArray listejour = json.getJSONArray("list"); // on prends la liste des prévisions journalière
						long valdat =0; 								// initialisation des variable et du tableau ou on les rentre
						String meteo;
						int temperature;
						
										// entrée dans le tableau
						for (int i = 0; i < tab.length; i++) {
							JSONObject temp1 = listejour.getJSONObject(i);
							temperature = temp1.getJSONObject("temp").getInt("day");
							valdat=temp1.getLong("dt");
							meteo = temp1.getJSONArray("weather").getJSONObject(0).getString("description");

							tab[i] = new Meteo();			// entrée des données dans le tableau
							tab[i].setDate(valdat);
							tab[i].setTemperature(temperature);
							tab[i].setMeteo(meteo);
		
							}
						  

					} catch (IOException | JSONException e) {
						// TODO Auto-generated catch block
						System.out.println("erreur: Senor meteo n'a pas reçu de donnée du web.");
						e.printStackTrace();
					}
					try {
			        	 FileWriter fw = new FileWriter(fichier);
			        	 String save ="";
							for (int i = 0; i < tab.length; i++) {
								save = save+tab[i].toString()+";";
								}
						fw.write (save);
						fw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
					}
				}
			});
			
			meteoparallele.addSubBehaviour(new CyclicBehaviour(this) {
				public void action()
				{
				ACLMessage msg = receive();
				if(msg!= null)  {														// lorsqu'un message est traité avec du contenu
					envoiobjet(msg.getSender().getLocalName(),tab);
					}
				else{
					block();
							};
				}
			});
			
			addBehaviour(meteoparallele);
			// ajout du comportement décrit au dessus.
	}
	
	
	
	public void envoimessage (String destinataire,String contenu){
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setContent(contenu);
		message.addReceiver(new AID(destinataire, AID.ISLOCALNAME));
		send(message);
	}
	
	public void envoiobjet (String destinataire,java.io.Serializable contenu){
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		try {
			message.setContentObject(contenu);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		message.addReceiver(new AID(destinataire, AID.ISLOCALNAME));
		send(message);
	}
	
	
	public void defibrillateur(String agentmort){
		if (agentmort.contains("@"))
		{
			String[] part2 = agentmort.split("@");
        	agentmort = part2[0]; 
		}
		ContainerController cc = getContainerController();
		
		switch (agentmort) {
		
        case "r2d2":
        	
        	try {
			AgentController ac = cc.createNewAgent("r2d2","r2d2.AgentEquipement", null);
			ac.start();}
        	catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        	};
                 break;
                 
        case "c3po":  
        	
        	try {
			AgentController ac = cc.createNewAgent("c3po","c3po.AgentOccupant", null);
			ac.start();} 
        	catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        	};
                 break;
                 
        case "glados": 
        	try {
			AgentController ac = cc.createNewAgent("senor_meteo","senor_meteo.AgentMeteo", null);
			ac.start();} 
        	catch (StaleProxyException e) {
			// TODO Auto-generated catch block
        		e.printStackTrace();
        	};
        		break; 
        		
        default: System.out.println("Erreur dans le reboot d'un agent par defibrillateur.") ;
                 break;}
		
	}
	
}


