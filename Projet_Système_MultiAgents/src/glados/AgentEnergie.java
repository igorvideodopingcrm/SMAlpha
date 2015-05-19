package glados;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.UnreadableException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import senor_meteo.Meteo;
import outils.Outils;


public class AgentEnergie extends jade.core.Agent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static File save = new File("saveglados.txt");
	
	private static int alpha=1;
	private static int beta1=1;
	private static int beta2=1;
	private static int beta3=1;
	private Meteo[] tabmeteo= new Meteo[7];
	
	
	
	protected void setup(){
		
		ParallelBehaviour energieparallele = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
		
		energieparallele.addSubBehaviour(new OneShotBehaviour(this){


			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

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
				
		         
				try {
					faireplanning(this.myAgent);
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  }
			
	  });
		energieparallele.addSubBehaviour(new TickerBehaviour(this,86400000){ // une fois par jour
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			protected void onTick() {

				try {
					faireplanning(this.myAgent);
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			});
		
		addBehaviour(energieparallele);
		// ajout du comportement decrit au dessus.
	}
	
	
	public void faireplanning(Agent a) throws UnreadableException{
		//recup equipement -> ajout dans init
		
		ArrayList <Equipement> init = new ArrayList <Equipement>();
		ArrayList <Equipement> planning = new ArrayList <Equipement>();
				
		
		Outils.envoimessage("senor_meteo","meteo demande","meteo demande", a);
		
		tabmeteo=(Meteo[])Outils.receptionobjet("senor_meteo","meteo","meteo","meteo", a);
    	String envoi ="";
		for (int i = 0; i < tabmeteo.length; i++) {
			envoi = envoi+tabmeteo[i].toString()+";";
			}
		System.out.println(envoi);
		
		Outils.envoimessage("c3po","prefs","prefs", a);
		
				
		Serializable prefs = Outils.receptionobjet("c3po","prefs","prefs","prefs",a);
		
		Outils.envoimessage("c3po","equipements","equipements", a);
		
		String equipstring =(String)Outils.receptionobjet("c3po","equipements","equipements","equipements", a);

		System.out.println("glados equip "+equipstring);
		equipstring.replace("]","");
		String[] tabequipJson= equipstring.split("\\{");
		JSONObject jsonequip = null;
		JSONArray Arrayequip = new JSONArray() ;
		for (int h = 1; h < tabequipJson.length; h++) {
			try {
				jsonequip = new JSONObject("{"+tabequipJson[h]);
				System.out.println("glados equip json"+jsonequip.toString());
				Arrayequip.put(jsonequip);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			}
	
		if (Arrayequip != null) { 
			  for (int i=0;i<Arrayequip.length();i++)
			  {
				  try {
					init.add(new Equipement(Arrayequip.getJSONObject(i).getString("nom"), Arrayequip.getJSONObject(i).getInt("duree"), Arrayequip.getJSONObject(i).getInt("conso"), Arrayequip.getJSONObject(i).getInt("debut_min"), Arrayequip.getJSONObject(i).getInt("fin_max")));
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			  }
			}
		
		System.out.println(prefs);
		
		 for(Equipement e : init){
		   System.out.println("avant: "+ e.getNom());
		  }
		 
		  Collections.sort(init);
		  System.out.println("sort");
		  for(Equipement e : init){
		   System.out.println("après: "+e.getNom());
		  }

		
	//	recup conso max // récupère la consommation max que peut s'autoriser glados à l'instant T
		int consoT[]= new int[24]; 
		Equipement eCourant;
		System.out.println(init.isEmpty());
		while(!init.isEmpty()) {
				eCourant=pop(init); 
				planning.add(eCourant);
				placerEquipement(eCourant,consoT);}
	//	}
		try {
			FileWriter fw = new FileWriter("saveglados.txt");
			/*FileReader fr = new FileReader("saveglados.txt");
			BufferedReader br = new BufferedReader(fr);*/
			for (int j=0;j<planning.size();j++)
			{
			fw.write(planning.get(j).toString() );
			}
			fw.close();
		/*	String line = br.readLine();
		        while (line != null)
		        {
		            System.out.println (line);
		            line = br.readLine();
		        }
		 
		    br.close();
		    fr.close();*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("envoi planning à c3po et R2d2");
		Outils.envoimessage("c3po",planning,"planning",a);
		Outils.receptionobjet("c3po",planning,"planning","planning",a);
		Outils.envoimessage("r2d2",planning,"planning",a);
		Outils.receptionobjet("r2d2",planning,"planning","planning", a);
	}

	
	public static void placerEquipement(Equipement e, int [] consoT){
				int min[]= new int[2]; // 0=p 1=i | crea tableau 2 dimension min
				min[0]= Integer.MAX_VALUE;// infini
				min[1]=e.getDebutmin();
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
				System.out.println(e.toString());

		}
	
	public static Equipement pop(ArrayList<Equipement> liste){
		Equipement e=liste.get(0);
		liste.remove(0);
		return e;
	}
	
	public static int penalite (Equipement e,int h, int[] consoT){
		int pTot=0;
		int[] tempT= new int[consoT.length];
				System.arraycopy(consoT, 0, tempT, 0, consoT.length);
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
		for (int i=0;i<24-2;i++){
			somme+=Math.abs(tempT[i+1]-tempT[i]);
		}
		return somme;
	}
	public static int p2(Equipement e,int[] tempT){
		int somme=0;
		for (int i=0;i<24-2;i++){
			somme+=(tempT[i+1]-tempT[i])^2;
		}
		return somme;
	}
	public static int p3(Equipement e,int[] tempT){
		int somme=0;
		for (int i=0;i<24-2;i++){
			if(tempT[i+1]>tempT[i]) somme+=(tempT[i+1]-tempT[i])^2;
		}
		return somme;
	}
	
}
	



