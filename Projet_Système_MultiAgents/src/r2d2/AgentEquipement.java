package r2d2;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class AgentEquipement extends jade.core.Agent{
	File fichier = new File("sauvr2d2.txt");
	File equips = new File("equipement.txt");
	protected void setup(){
		
		ParallelBehaviour equiparallele = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
		
		equiparallele.addSubBehaviour(new OneShotBehaviour(this) 
			{


				@Override
				public void action() {
					
					System.out.println(getLocalName()+" lanc�");
					
					
					if (! fichier.exists()) // si le fichier n'existe pas, le cr�er
					
					{
						try {
							fichier.createNewFile();
						} catch (IOException e) {
							// TODO Auto-generated catch block
						}
					}
					
			         try {
			        	 FileWriter fw = new FileWriter (fichier);
			        	 FileReader fr = new FileReader (fichier);
					} catch (IOException e) {
						// TODO Auto-generated catch block
					}
				}
			});
		
		equiparallele.addSubBehaviour(new CyclicBehaviour(this) {
			public void action()
			{
				ACLMessage msg = receive();
				if(msg!= null)  {
					String expe=msg.getSender().getLocalName();
					switch (expe) {
					
			        case "glados": // envoyer les prefs utilisateurs � glados
			        	
			        		if (msg.getContent()=="planning"){
			        			
			        			envoimessage("glados","confirmation planning");
			        		}
			        		
			                 break;
			                 
			        case "senor_meteo":  // envoyer la m�t�o sur l'application
			        			
						//		HttpClient httpclient = new DefaultHttpClient();
						//        HttpPost httppost = new HttpPost((String) params[0]);//rajouter de quoi joindre le serveur
						        
						//        ArrayList<NameValuePair> queryParams = new ArrayList<NameValuePair>();
						//        queryParams.add(new BasicNameValuePair("contenu", contenu));
						        
						        // create and launch the POST request
					//	        try {
					//				httppost.setEntity(new UrlEncodedFormEntity(queryParams));
					//		        HttpResponse httpResponse;
					//				httpResponse = httpclient.execute(httppost);
					//				response = EntityUtils.toString(httpResponse.getEntity());
									/*JSONObject jsonResponse = new JSONObject(jsonString);
									response = jsonResponse.get("message").toString();*/
					//			} catch (UnsupportedEncodingException e) {
									//Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
					//			}  catch (ClientProtocolException e) {
									//Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
					//			} catch (IOException e) {
									//Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
					//			}  catch (ParseException e) {
									
									//Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
								 //catch (JSONException e) {
									//Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
								//}
								
						//	else{
					//			block();
						//		};
						//		}
			        		break; 
			        	
			        case "ams":
			        	
			        	String contenu = msg.getContent();
			        	String[] part1 = contenu.split(":");
			        	String separation1 = part1[6];
			        	String[] part2 = separation1.split(" ");
			        	String agentareboot = part2[1]; 
			        	defibrillateur(agentareboot);
			        	
			        	break;
			        	
			        default: System.out.println("je suis C3PO et j'ai re�u " + msg.getContent()); ;
			                 break;}
					

				}
				else{
					block();
							};
				}
			});
		
		equiparallele.addSubBehaviour(new CyclicBehaviour(this) {
			public void action() {
				//regarde l'heure
				//regarde le planning
				//annonce l'�tat de chaque �quipement.
					
			}
		});
		addBehaviour(equiparallele);
		// ajout du comportement d�crit au dessus.
	}

	public void envoimessage(String destinataire,String contenu){
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setContent(contenu);
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
		
        case "senor_meteo":
        	
        	try {
			AgentController ac = cc.createNewAgent("senor_meteo","senor_meteo.AgentMeteo", null);
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
