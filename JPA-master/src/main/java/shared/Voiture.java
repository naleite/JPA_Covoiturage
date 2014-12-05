package shared;

import javax.persistence.*;

@Entity
public class Voiture {

	
	private long id;
	
	private String series;
	
	 private int nbPlaceTotal;
	 
	
	 private Personne proprietaire;
	 
	 /**
	  * @return the proprietaire de la voiture
	  */
	 @OneToOne
	 public Personne getProprietaire() {
		 return proprietaire;
	 }

	/**
	 * @param proprietaire the proprietaire to set
	 */
	public void setProprietaire(Personne proprietaire) {
		this.proprietaire = proprietaire;
	}

	/**
	 * @return the id
	 */
	@Id @GeneratedValue
	public long getId() {
		return id;
	}
	
	 
	/**
	 * @return the series
	 */
	@Column
	public String getSeries() {
		return series;
	}
	/**
	 * @param series the series to set
	 */
	public void setSeries(String series) {
		this.series = series;
	}
	/**
	 * @return the nbPlaceTotal
	 */
	@Column
	public int getNbPlaceTotal() {
		return nbPlaceTotal;
	}
	/**
	 * @param nbPlaceTotal the nbPlaceTotal to set
	 */
	public void setNbPlaceTotal(int nbPlaceTotal) {
		this.nbPlaceTotal = nbPlaceTotal;
	}

	protected void setId(long id) {
		this.id = id;
	}
	
	
}
