package r2d2;
import glados.Equipement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import outils.Outils;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class AgentEquipement extends jade.core.Agent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static File saveFile = new File("saver2d2.txt");
	protected void setup(){
		
		ParallelBehaviour equiparallele = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
		
		equiparallele.addSubBehaviour(new OneShotBehaviour(this) 
			{
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void action() {
					
					System.out.println(getLocalName()+" lancé");
					
					
					if (!AgentEquipement.saveFile.exists()) // si le fichier n'existe pas, le créer
					
					{
						try {
							AgentEquipement.saveFile.createNewFile();
						} catch (IOException e) {
							// TODO Auto-generated catch block
						}
					}
					
			        
				}
			});
		
		equiparallele.addSubBehaviour(new CyclicBehaviour(this) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void action()
			{
				ACLMessage msg = receive();
				if(msg!= null)  {
					String expe=msg.getSender().getLocalName();
					switch (expe) {
					
			        case "glados": 
			        	
						if (msg.getLanguage().equals("planning")){
							ArrayList <Equipement> planning = new ArrayList <Equipement>();
							try {
								planning = (ArrayList<Equipement>) msg.getContentObject();
							} catch (UnreadableException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
		        			try {
		        				FileWriter fw = new FileWriter(saveFile);
		        				FileReader fr = new FileReader(saveFile);
		        				BufferedReader br = new BufferedReader(fr);
		        				for (int j=0;j<planning.size();j++)
		        				{
		        					fw.write(planning.get(j).toString());
		        					fw.write("\n");
		        				}
		        				fw.close();
		        				String line = br.readLine();
		        				
		        				System.out.println ("je suis r2d2 et ceci est le planning");
		        			        while (line != null)
		        			        {
		        			            System.out.println (line);
		        			            line = br.readLine();
		        			        }
		        			 
		        			    br.close();
		        			    fr.close();
		        			 
		        			} catch (IOException e) {
		        				// TODO Auto-generated catch block
		        				e.printStackTrace();
		        			}
							
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
			        
			        case "dummy":
			        	String heuretext = msg.getContent();
			        	String[] h = heuretext.split("h");
			        	int heure =  Integer.parseInt(h[0]);
			        	Equipallume(heure);

			        	break;
			        	
			        default: System.out.println("r2d2: reçu " + msg.getContent()); ;
			            break;
			            }
					

				}
				else{
					block();
							};
				}
			});
		
		addBehaviour(equiparallele);
		// ajout du comportement décrit au dessus.
	}


	public ArrayList <Equipement> Equipallume(int heure) {
		ArrayList <Equipement> equipAllume = new ArrayList <Equipement>();
		ArrayList <Equipement> planning = new ArrayList <Equipement>();
    	
    	try {
			FileReader fr = new FileReader(saveFile);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
		        while (line != null)
		        	
		        {	 System.out.println (line);
		        	planning.add(new Equipement(line));
		            line = br.readLine();
		            
		        }
		 
		    br.close();
		    fr.close();
		 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	for (int j=0;j<planning.size();j++)
		{	
			if(planning.get(j).getIndice()<heure && ((planning.get(j).getIndice()+planning.get(j).getDuree())>heure))
			{
				equipAllume.add(planning.get(j));
			};
		}
    	System.out.println("Equipements allumés: " + equipAllume.size());
    	for (int j=0;j< equipAllume.size();j++)
		{
    		System.out.println(equipAllume.get(j).toString());
		}
    	System.out.println("\n");
		//annonce l'état de chaque équipement.*/
		
		return equipAllume;
	}
}
