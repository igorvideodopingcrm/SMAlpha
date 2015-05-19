package glados;

import java.io.Serializable;

public class Equipement implements Comparable<Equipement>,Serializable{
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private String nom;
		private	int duree;
		private	int conso;
		private	int debutmin;
		private	int finmax;
		private	int indice;
		/**
		 * @param nom
		 * @param duree
		 * @param conso
		 * @param debutmin
		 * @param finmax
		 * @param indice
		 */
		public Equipement(String nom, int duree, int conso, int debutmin, int finmax) {
			this.nom = nom;
			this.duree = duree;
			this.conso = conso;
			this.debutmin = debutmin;
			this.finmax = finmax;
			this.indice = 0;
		}
		/**
		 * @return the nom
		 */
		
		public Equipement(String save) {
		String[] arguments = save.split(",");
		String[] valeurs = new String[6];
		for (int h=0;h< arguments.length;h++)
			{
				valeurs[h]=arguments[h].split("=")[1];
			}
		
		this.nom = valeurs[0];
		this.duree = Integer.parseInt(valeurs[1]);
		this.conso = Integer.parseInt(valeurs[2]);
		this.debutmin = Integer.parseInt(valeurs[3]);
		this.finmax = Integer.parseInt(valeurs[4]);
		this.indice = Integer.parseInt(valeurs[5].replace(";",""));
		}
		
		public String getNom() {
			return nom;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "nom=" + nom + ",duree=" + duree + ",conso="
					+ conso + ",debutmin=" + debutmin + ",finmax=" + finmax
					+ ",indice=" + indice +";";
		}
		/**
		 * @param nom the nom to set
		 */
		public void setNom(String nom) {
			this.nom = nom;
		}
		/**
		 * @return the duree
		 */
		public int getDuree() {
			return duree;
		}
		/**
		 * @param duree the duree to set
		 */
		public void setDuree(int duree) {
			this.duree = duree;
		}
		/**
		 * @return the conso
		 */
		public int getConso() {
			return conso;
		}
		/**
		 * @param conso the conso to set
		 */
		public void setConso(int conso) {
			this.conso = conso;
		}
		/**
		 * @return the debutmin
		 */
		public int getDebutmin() {
			return debutmin;
		}
		/**
		 * @param debutmin the debutmin to set
		 */
		public void setDebutmin(int debutmin) {
			this.debutmin = debutmin;
		}
		/**
		 * @return the finmax
		 */
		public int getFinmax() {
			return finmax;
		}
		/**
		 * @param finmax the finmax to set
		 */
		public void setFinmax(int finmax) {
			this.finmax = finmax;
		}
		/**
		 * @return the indice
		 */
		public int getIndice() {
			return indice;
		}
		/**
		 * @param indice the indice to set
		 */
		public void setIndice(int indice) {
			this.indice = indice;
		}
		public int compareTo(Equipement e){
			if (((this.getFinmax()-this.getDebutmin())-2*this.getDuree())-this.getConso() < ((e.getFinmax()-e.getDebutmin())-2*e.getDuree())-e.getConso()) return 1;//il faudrait trouver quelque chose de mieux
			else if (((this.getFinmax()-this.getDebutmin())-2*this.getDuree())-this.getConso() > ((e.getFinmax()-e.getDebutmin())-2*e.getDuree())-e.getConso()) return -1;
			else return 0;
		}

		
}
