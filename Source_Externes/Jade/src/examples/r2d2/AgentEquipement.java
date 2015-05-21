package r2d2;

import glados.Equipement;	//import des packages du système multi agent
import outils.Outils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;	//import de jade
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
	private static File log = new File("log.txt");			//Fichier de log d'erreur
	
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
					
					if (! AgentEquipement.log.exists()) // si le fichier n'existe pas, le creer
					{
						try {
							AgentEquipement.log.createNewFile();
						} catch (IOException e) {
						}
					}
					
					if (!AgentEquipement.saveFile.exists()) // si le fichier n'existe pas, le créer
					
					{
						try {
							AgentEquipement.saveFile.createNewFile();
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
					
			        
				}
			});
		
			equiparallele.addSubBehaviour(new CyclicBehaviour(this) {	//behaviour "boite de reception"
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
						case "glados": 	        	// glados vient de nous envoyer le planning.
								ArrayList <Equipement> planning = new ArrayList <Equipement>();
								try {		
									planning = (ArrayList<Equipement>) msg.getContentObject();
								} catch (UnreadableException e) {
									try {
							    		PrintStream printlog = new PrintStream(log);
										printlog.print(this.myAgent.getLocalName());
										e.printStackTrace(printlog);
										printlog.close();
									}
									catch (FileNotFoundException e1) {
									}
								}
		        			try {
		        				FileWriter fw = new FileWriter(saveFile);	//sauvegarde du planning dans le document de l'agentEquipement
		        				for (int j=0;j<planning.size();j++)
		        				{
		        					fw.write(planning.get(j).toString());
		        					fw.write("\n");
		        				}
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
		        			Outils.envoimessage("glados","planning reçu","planning",this.myAgent);
			                 break;

			       
						case "ams":	// un agent mort a tenté de contacter l'agent Equipement. l'agent le réanime
							
							Outils.defibrillateur(msg.getContent(),this.myAgent);
							break;
			        

						case "dummy": // l'agent dummy vient d'envoyer r2d2 pour l'interroger sur les équipement présent à une certaine heure
							String heuretext = msg.getContent();
							String[] h = heuretext.split("h");
							int heure =  Integer.parseInt(h[0]);
							Equipallume(heure,this.myAgent);
							break;
			        	
			        default: 			//message non traité par le switch case, enregistré dans le log.
			        	PrintStream printlog;
			        	try {						
			        		printlog = new PrintStream(log);						
			        		printlog.print(this.myAgent.getLocalName() +" message non traité:\n "+msg.getLanguage());						
			        		printlog.close();					
			        	} catch (FileNotFoundException e) {													
			        		e.printStackTrace();					
			        	}
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


	public ArrayList <Equipement> Equipallume(int heure,Agent a) {	//fonction permettant de savoir quel équipement est allumé selon l'heure demandée
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
			try {
	    		PrintStream printlog = new PrintStream(log);
				printlog.print(a.getLocalName());
				e.printStackTrace(printlog);
				printlog.close();
			}
			catch (FileNotFoundException e1) {
			}
		}
    	
    	for (int j=0;j<planning.size();j++)
		{	
			if(planning.get(j).getIndice()<=heure && ((planning.get(j).getIndice()+planning.get(j).getDuree())>heure))
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
