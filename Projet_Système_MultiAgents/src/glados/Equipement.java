package glados;

public class Equipement {
		private String nom;
		private	int dur�e;
		private	int conso;
		private	int d�butmin;
		private	int finmax;
		private	int indice;
		/**
		 * @param nom
		 * @param dur�e
		 * @param conso
		 * @param d�butmin
		 * @param finmax
		 * @param indice
		 */
		public Equipement(String nom, int dur�e, int conso, int d�butmin,
				int finmax) {
			this.nom = nom;
			this.dur�e = dur�e;
			this.conso = conso;
			this.d�butmin = d�butmin;
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
		 * @return the dur�e
		 */
		public int getDur�e() {
			return dur�e;
		}
		/**
		 * @param dur�e the dur�e to set
		 */
		public void setDur�e(int dur�e) {
			this.dur�e = dur�e;
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
		 * @return the d�butmin
		 */
		public int getD�butmin() {
			return d�butmin;
		}
		/**
		 * @param d�butmin the d�butmin to set
		 */
		public void setD�butmin(int d�butmin) {
			this.d�butmin = d�butmin;
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
