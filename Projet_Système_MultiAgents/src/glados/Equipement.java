package glados;

public class Equipement implements Comparable{
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
		
		public String getNom() {
			return nom;
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
