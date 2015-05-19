package c3po;
import glados.Equipement;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import senor_meteo.JsonReader;
import senor_meteo.Meteo;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import outils.Outils;

import java.lang.String;
import java.io.File;


import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;


public class AgentOccupant extends jade.core.Agent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static String server="192.168.1.42";
	
	private static File save = new File("savec3po.txt");
	private Meteo[]tabMeteo= new Meteo[7];
    
	protected void setup(){


			ParallelBehaviour occuparallele = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
			
			occuparallele.addSubBehaviour(new OneShotBehaviour(this){
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public void action(){
					System.out.println(getLocalName()+" lancé");
					
					
					if (! AgentOccupant.save.exists()) // si le fichier n'existe pas, le créer
					{
						try {
							AgentOccupant.save.createNewFile();
						} catch (IOException e) {
							// TODO Auto-generated catch block
						}
					}
					
				//	Outils.envoimessage("senor_meteo","demandemeteo","demandemeteo", this.myAgent);
				//	Outils.receptionobjet("senor_meteo", "meteo","demandemeteo","meteo",this.myAgent);
			  }
				
		  });
			
			occuparallele.addSubBehaviour(new TickerBehaviour(this,5000){// 43200000 = 12 heures en milisecondes | à modifier selon le rafraichissement voulu
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				protected void onTick() {									// timer d'obtention de la météo par l'agentoccupant auprès de l'agent météo
					
					ACLMessage message = new ACLMessage(ACLMessage.INFORM);
					message.addReceiver(new AID("senor_meteo", AID.ISLOCALNAME));
					message.setContent("météo!");
					send(message);
				
				}
			});
			
			occuparallele.addSubBehaviour(new CyclicBehaviour(this) { // behaviour de réception de message
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public void action()
				{
				ACLMessage msg = receive();
				if(msg!= null)  {
					
				if (msg.getSender().getLocalName().equals("ams")){

		        	String contenu = msg.getContent();
		        	String[] separmessage = contenu.split(":");
		        	String contientnom = separmessage[6];
		        	String[] nomagent = contientnom.split(" ");
		        	String agentareboot = nomagent[1]; 
		        	Outils.defibrillateur(agentareboot,this.myAgent);
				}else{
					switch (msg.getLanguage())
					{
					case "prefs": // glados demande les prefs
	        			System.out.println("prefs envoyées");
	        			Outils.envoimessage("glados","prefs","prefs",this.myAgent);
	        			
	        			/*try {
							JSONArray jsonEquip =postserver("prefs","");
							
							String listequip = jsonEquip.toString();
							
							Outils.envoimessage("glados",listequip,"prefs",this.myAgent);
						
	        			} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
	        			break;
	        			
	        		case "planning": // le planning vient d'être reçu
	        			System.out.println("envoi planning web");
	        			Outils.envoimessage("glados","confirmation planning","planning",this.myAgent);
	        			ArrayList <Equipement> planning = new ArrayList <Equipement>();
					try {
						planning = (ArrayList<Equipement>) msg.getContentObject();
					} catch (UnreadableException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	        			try {
	        				String envoi1 = "";
	        				FileWriter fw = new FileWriter("savec3po.txt");
	        				FileReader fr = new FileReader("savec3po.txt");
	        				BufferedReader br = new BufferedReader(fr);
	        				for (int j=0;j<planning.size();j++)
	        				{
	        					envoi1 = envoi1+planning.get(j).toString();
	        				fw.write(planning.get(j).toString());
	        				}
	        				fw.close();
	        				String line = br.readLine();
	        				
	        				System.out.println ("je suis c3po et ceci est le planning");
	        			        while (line != null)
	        			        {
	        			            System.out.println (line);
	        			            line = br.readLine();
	        			        }
	        			 
	        			    br.close();
	        			    fr.close();
	        			    postserver("planning",envoi1);
	        			} catch (IOException e) {
	        				// TODO Auto-generated catch block
	        				e.printStackTrace();
	        			}
	        			break;

	        		case "equipements":// glados demande les equipements
	        			
	        			try {
							JSONArray jsonEquip =postserver("equipements","");
							
							String listequip = jsonEquip.toString();
							
							Outils.envoimessage("glados",listequip,"equipements",this.myAgent);
						
	        			} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	        			
	        		break;
	        		
	        		case "meteo":  // envoyer la météo sur l'application
			        	try {
							tabMeteo=(Meteo[])msg.getContentObject();
						} catch (UnreadableException e) {

							System.out.println ("trycatchmeteo");
							e.printStackTrace();
						}
			        	String envoi ="";
						for (int i = 0; i < tabMeteo.length; i++) {
							envoi = envoi+tabMeteo[i].toString()+";";
							}
						try {
							postserver("meteo",envoi);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        		break;
					}
				}
	
						
				}
				else
				{
					block();
				}
					}
				});
			addBehaviour(occuparallele);
	}
	
	
	public static JSONArray postserver(String title,String message) throws IOException {
			String tempText = "http://"+server+"/SMAlpha_html/c3po.php?"+title+"="+message;
			tempText=tempText.replace(" ","_");
		    InputStream is = new URL(tempText).openStream();
		    try {
		      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		      String jsonText = JsonReader.readAll(rd);
		      System.out.println(jsonText);
		      JSONArray json = new JSONArray(jsonText);
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
