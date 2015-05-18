package glados;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class AgentEnergie extends jade.core.Agent{
	private static File save = new File("saveglados.txt");
	private static int alpha=1;
	private static int beta1=1;
	private static int beta2=1;
	private static int beta3=1;
	protected void setup(){
		
		ParallelBehaviour energieparallele = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
		
		energieparallele.addSubBehaviour(new OneShotBehaviour(this){


			public void action(){
				System.out.println(getLocalName()+" lance");
				
				if (! AgentEnergie.save.exists()) // si le fichier n'existe pas, le creer
				
				{
					try {
						AgentEnergie.save.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
					}
				}
				
		         
				faireplanning();
		  }
			
	  });
		energieparallele.addSubBehaviour(new TickerBehaviour(this,86400000){ // une fois par jour
			protected void onTick() {

				faireplanning();
			}
			});
		
		addBehaviour(energieparallele);
		// ajout du comportement decrit au dessus.
	}
	
	
	public void faireplanning(){
		//recup equipement -> ajout dans init
		
		ArrayList <Equipement> init = new ArrayList <Equipement>();
		ArrayList <Equipement> planning = new ArrayList <Equipement>();
				
		envoimessage("senor_meteo","meteo demande");
		String meteo = receptionmessage("senor_meteo","meteo demande");
		System.out.println(meteo);
		envoimessage("c3po","prefs");
		envoimessage("c3po","equipements");
		
		String prefs = receptionmessage("c3po","prefs");
		JSONArray equipements = receptionmessage("c3po","equipements");
		System.out.println(prefs);
		
		for (int i=0;i<equipements.length();i++)
		{
			init.add(new Equipement(equipements.getJSONObject(i).getString("nom"), equipements.getJSONObject(i).getInt("duree"), equipements.getJSONObject(i).getInt("conso"), equipements.getJSONObject(i).getInt("debut_min"), equipements.getJSONObject(i).getInt("fin_max")));
		}
		for(Equipement e : init){
			System.out.println(e.getNom());
		}
		sort(init);
		System.out.println("sort");
		for(Equipement e : init){
			System.out.println(e.getNom());
		}
	//	recup conso max // recupère la consommation max que peut s'autoriser glados à l'instant T
		int consoT[]= new int[24]; 
		Equipement eCourant;
		while(!init.isEmpty()) {
				eCourant=pop(init); 
				planning.add(eCourrant);
				placerEquipement(eCourrant,consoT);}
	//	}
		try {
			FileWriter fw = new FileWriter("saveglados.txt");
			FileReader fr = new FileReader("saveglados.txt");
			BufferedReader br = new BufferedReader (fr);
			fw.write("planning save glados");
			fw.close();
			String line = br.readLine();
			 
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
		envoimessage("c3po","planning");
		String S =receptionmessage("c3po","planning");
		envoimessage("r2d2","planning");
		String J = receptionmessage("r2d2","planning");
	}

	
	public static void placerEquipement(Equipement e, int [] consoT){	
				int min[]= new int[2]; // 0=p 1=i | crea tableau 2 dimension min
				min[0]= Integer.MAX_VALUE;// infini
				for (int i =e.getDebutmin(); i< e.getFinmax()-e.getDuree() ; i++ ){ 		
					if (penalite(e,i,consoT) < min[0]) {			// p = fonction penalite
					min[0] = penalite(e,i,consoT);
					min[1]=i;
					}
				}
				e.setIndice( min[1]);
				//placer dans le planning // planning [24] ou on add les conso de chaque h
				for (int i=e.getIndice(); i<e.getIndice()+e.getDuree(); i++) {
					consoT[i]+= e.getConso();
					}
				}
	
	public static Equipement pop(ArrayList liste){
		Equipement e=liste.get(0);
		liste.remove(0);
		return e;
	}
	
	
	public void envoimessage(String destinataire,String contenu){
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		message.setContent(contenu);
		message.addReceiver(new AID(destinataire, AID.ISLOCALNAME));
		send(message);
	}
	
	public String receptionmessage(String agentcontacte, String messageoriginal){
		ACLMessage msg = blockingReceive();
		String contenu ;
		if (msg.getSender().getLocalName().contains("ams"))
		{
			defibrillateur(agentcontacte);
			envoimessage(agentcontacte,messageoriginal);
			contenu =receptionmessage(agentcontacte,messageoriginal);
		}
		else{
			contenu = msg.getContent();
		}
		return contenu;
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
                 
        case "c3po":  
        	
        	try {
			AgentController ac = cc.createNewAgent("c3po","c3po.AgentOccupant", null);
			ac.start();} 
        	catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        	};
                 break;
        		
        default: System.out.println("Erreur dans le reboot d'un agent par defibrillateur.") ;
                 break;}
		
	}
	
	public static int penalite (Equipement e,int h, int[] consoT){
		int pTot=0;
		int[] tempT=Array.copyOf(consoT,consoT.length);
		for(int i=h; i<h + e.getDuree();i++){
			tempT[i]+=e.getConso();
			if(AgentEnergie.alpha >0)
				pTot+= AgentEnergie.alpha*(tempT[i]);
		}
		if(AgentEnergie.beta1 >0)
			pTot += AgentEnergie.beta1*p1(e,tempT);
		if(AgentEnergie.beta2 >0)
			pTot += AgentEnergie.beta2*p2(e,tempT);
		if(AgentEnergie.beta3>0)
			pTot += AgentEnergie.beta3*p3(e,tempT);
		
		return pTot;	
		
	}
	public static int p1(Equipement e,int[] tempT){
		int somme=0;
		for (int i=0;i<24-1;i++){
			somme+=Math.abs(tempT[i+1]-tempT[i]);
		}
		return somme;
	}
	public static int p2(Equipement e,int[] tempT){
		int somme=0;
		for (int i=0;i<24-1;i++){
			somme+=(tempT[i+1]-tempT[i])^2;
		}
		return somme;
	}
	public static int p3(Equipement e,int[] tempT){
		int somme=0;
		for (int i=0;i<24-1;i++){
			if(tempT[i+1]>tempT[i]) somme+=(tempT[i+1]-tempT[i])^2;
		}
		return somme;
	}
	
}
	



