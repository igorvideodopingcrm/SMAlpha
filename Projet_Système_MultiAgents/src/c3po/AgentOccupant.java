package c3po;
import java.io.IOException;

import org.apache.http.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import senor_meteo.Tabmeteo;

import java.util.ArrayList;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;


//import src.BasicNameValuePair;
//import src.ClientProtocolException;
//import src.DefaultHttpClient;
//import src.EditText;
//import src.HttpClient;
//import src.HttpPost;
//import src.HttpResponse;
//import src.NameValuePair;
import org.apache.*;

import java.lang.Exception;
import java.lang.String;
import java.io.File;
import java.io.UnsupportedEncodingException;





//import src.UrlEncodedFormEntity;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;


public class AgentOccupant  extends jade.core.Agent{
	
	String server="127.0.0.0";
	
	File fichier = new File("sauvc3po.txt");
	Tabmeteo[] tab= new Tabmeteo[7];//TODO enlever ce tableau test une fois les tests terminé
	protected void setup(){

			addBehaviour(new GUIBehaviour());
			// ajout du comportement décrit au dessous.
			
			
			ParallelBehaviour occuparallele = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
			
			
			
			occuparallele.addSubBehaviour(new OneShotBehaviour(this){
				
				public void action(){
					System.out.println(getLocalName()+" lancé");
					
					
					if (! fichier.exists()) // si le fichier n'existe pas, le créer
					{
						try {
							fichier.createNewFile();
						} catch (IOException e) {
							// TODO Auto-generated catch block
						}
					}
					
					ACLMessage message = new ACLMessage(ACLMessage.INFORM);
					message.addReceiver(new AID("senor_meteo", AID.ISLOCALNAME));
					message.setContent("météo!");
					send(message);
			  }
				
		  });
			
			occuparallele.addSubBehaviour(new TickerBehaviour(this,20000){// 43200000 = 12 heures en milisecondes | à modifier selon le rafraichissement voulu
				protected void onTick() {									// timer d'obtention de la météo par l'agentoccupant auprès de l'agent météo
					
					ACLMessage message = new ACLMessage(ACLMessage.INFORM);
					message.addReceiver(new AID("senor_meteo", AID.ISLOCALNAME));
					message.setContent("météo!");
					send(message);
				
				}
			});
			
			occuparallele.addSubBehaviour(new CyclicBehaviour(this) { // behaviour de réception de message
				
				public void action()
				{
				ACLMessage msg = receive();
				if(msg!= null)  {
					String expe=msg.getSender().getLocalName();
					switch (expe) {
					
			        case "glados": // envoyer les prefs utilisateurs à glados
			        	
			        		
			        		if (msg.getContent().toString().equals("prefs")){
			        			
			        			// envoi des préférences utilisateurs
			        			envoimessage("glados","prefs utilisateur");
			        		}
			        		else
			        		{
			        			
			        			envoimessage("glados","confirmation planning");
			        		}
			                 break;
			                 
			        case "senor_meteo":  // envoyer la météo sur l'application
			        			postserver("meteo",msg.getContent().toString());
						
			        		break; 
			        	
			        case "ams":
			        	
			        	String contenu = msg.getContent();
			        	String[] part1 = contenu.split(":");
			        	String separation1 = part1[6];
			        	String[] part2 = separation1.split(" ");
			        	String agentareboot = part2[1]; 
			        	defibrillateur(agentareboot);
			        	
			        	break;
			        	
			        default: System.out.println("je suis C3PO et j'ai reçu " + msg.getContent()); ;
			                 break;}
					

				}
				}
				});
			addBehaviour(occuparallele);
	}
	
	/**
	 * 	Inner class GUIBehaviour
	 */
	private class GUIBehaviour extends Behaviour 
	{


		public void action() {
			// TODO Contenu du Behaviour de La GUI, en charge de son comportement
			
		}


		public boolean done() {
			// TODO Methode à appeler à la fin de la GUI, mettre TRUE pour fermer le Behaviour, False Pour qu'il se rouvre de manière cyclique
			// CF voir les Behaviour One shot et les behaviours cycliques
			return false;
		}
		
	}
	
	public void envoimessage(String destinataire,String contenu){
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setContent(contenu);
		message.addReceiver(new AID(destinataire, AID.ISLOCALNAME));
		send(message);
	}
	
	public static void postserver(String title,String message) throws IOException {
			
			String tempText = "http://"+server+"?"+title+"="+message;
			
		    InputStream is = new URL(tempText).openStream();
		    try {
		      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		    } finally {
		      is.close();
		    }
	  	}
	
	
	public void defibrillateur(String agentmort){
		if (agentmort.contains("@"))
		{
			String[] part2 = agentmort.split("@");
        	agentmort = part2[0]; 
		}
		ContainerController cc = getContainerController();
		
		switch (agentmort) {
		
        case "senor_meteo":
        	
        	try {
			AgentController ac = cc.createNewAgent("senor_meteo","senor_meteo.AgentMeteo", null);
			ac.start();} 
        	catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        	};
                 break;
                 
        case "r2d2":
        	
        	try {
			AgentController ac = cc.createNewAgent("r2d2","r2d2.AgentEquipement", null);
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
