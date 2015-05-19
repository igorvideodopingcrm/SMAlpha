package c3po;
import java.io.IOException;

import org.apache.http.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import senor_meteo.JsonReader;
import senor_meteo.Meteo;



import java.util.ArrayList;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

import outils.Outils;

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
import java.io.BufferedInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;


// serialization d'objet.
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;




//import src.UrlEncodedFormEntity;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;


public class AgentOccupant  extends jade.core.Agent{
	
	static String server="192.168.1.42";
	
	File fichier = new File("sauvc3po.txt");
	Meteo[] tab= new Meteo[7];
    
	protected void setup(){


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
					
					Outils.envoimessage("senor_meteo","demandemeteo","demandemeteo", this.myAgent);
					Outils.receptionobjet("senor_meteo", "meteo","demandemeteo","meteo",this.myAgent);
			  }
				
		  });
			
			occuparallele.addSubBehaviour(new TickerBehaviour(this,5000){// 43200000 = 12 heures en milisecondes | à modifier selon le rafraichissement voulu
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
					String expediteur=msg.getSender().getLocalName();
					switch (expediteur) {
					
			        case "glados":
			        	
			        	switch (msg.getLanguage())
			        		{case "demandeprefs": // glados demande les prefs
			        			System.out.println("prefs envoyées");
			        			Outils.envoimessage("glados","prefs","prefs",this.myAgent);
			        			break;
			        			
			        		case "planning": // le planning vient d'être reçu
			        			Outils.envoimessage("glados","confirmation planning","confplanning",this.myAgent);
			        			break;
			        		}
			        		else if (msg.getContent().toString().equals("equipements"))
			        		{
			        			try {
									
									
									JSONObject json = postserver("equipements","");
									JSONArray equipements = json.getJSONArray();//surement erreur
									envoimessage("glados",equipements);//fonction non existante
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
			        			envoimessage("glados","confirmation planning");
			        		}
			        		else
			        		{
			        			
			        			envoimessage("glados","confirmation planning");
			        		}
			                 break;
			                 
			        case "senor_meteo":  // envoyer la météo sur l'application
						
			        	try {
							tab=(Meteo[])msg.getContentObject();
							
						} catch (UnreadableException e) {
							e.printStackTrace();
						}
			        	
			        	String envoi ="";
						for (int i = 0; i < tab.length; i++) {
							envoi = envoi+tab[i].toString()+";";
							}
			       	try {
							postserver("meteo",envoi);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        		break; 
			        	
			        case "ams":
			        	
			        	String contenu = msg.getContent();
			        	String[] separmessage = contenu.split(":");
			        	String contientnom = separmessage[6];
			        	String[] nomagent = contientnom.split(" ");
			        	String agentareboot = nomagent[1]; 
			        	Outils.defibrillateur(agentareboot,this.myAgent);
			        	break;
			        	
			        	
			        default: System.out.println("je suis C3PO et j'ai reçu " + msg.getContent()); ;
			                 break;}	

						}
				else
				{
					block();
				}
					}
				});
			addBehaviour(occuparallele);
	}
	
	

	
	public static JSONObject postserver(String title,String message) throws IOException {
			String tempText = "http://"+server+"/SMAlpha_html/c3po.php?"+title+"="+message;
			tempText=tempText.replace(" ", "_");
		    InputStream is = new URL(tempText).openStream();
		    try {
		      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		      String jsonText = JsonReader.readAll(rd);
		      System.out.println(jsonText);
		      JSONObject json = new JSONObject(jsonText);
		      return json;
		    } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
		      is.close();
		    }
			return null;
	  	}
	
	

}
