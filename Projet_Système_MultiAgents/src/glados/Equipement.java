package glados;

public class Equipement {
		private String nom;
		private	int durée;
		private	int conso;
		private	int débutmin;
		private	int finmax;
		private	int indice;
		/**
		 * @param nom
		 * @param durée
		 * @param conso
		 * @param débutmin
		 * @param finmax
		 * @param indice
		 */
		public Equipement(String nom, int durée, int conso, int débutmin,
				int finmax) {
			this.nom = nom;
			this.durée = durée;
			this.conso = conso;
			this.débutmin = débutmin;
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
		 * @return the durée
		 */
		public int getDurée() {
			return durée;
		}
		/**
		 * @param durée the durée to set
		 */
		public void setDurée(int durée) {
			this.durée = durée;
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
		 * @return the débutmin
		 */
		public int getDébutmin() {
			return débutmin;
		}
		/**
		 * @param débutmin the débutmin to set
		 */
		public void setDébutmin(int débutmin) {
			this.débutmin = débutmin;
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
}
