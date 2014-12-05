package shared;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;


@Entity
@XmlRootElement
public class Personne {
	
	private long id;
	private String localisation;
	private String destination; //l'endroit ou veut aller la personne
	private String nom;
	private List<Evenement> listEvent= new ArrayList<Evenement>();
	private List<Evenement> listEvCond= new ArrayList<Evenement>();//liste des evenements ou la personne est conducteur
	private Voiture voiture;
	private List<Commentaire> listCom=new ArrayList<Commentaire>();;
	

	protected void setId(long id) {
		this.id = id;
	}
	

	protected void setListEvent(List<Evenement> listEvent) {
		this.listEvent = listEvent;
	}

	
	
	@JsonIgnore 
	@ManyToMany(mappedBy="participants")
	public List<Evenement> getListEvent() {
		return listEvent;
	}


	/**
	 * @return the dummy
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @param dummy the dummy to set
	 */
	public void setNom(String nom) {
		this.nom = nom; 
	}

	@Transient
	public String getLocalisation() {
		return localisation;
	}

	public void setLocalisation(String localisation) {
		this.localisation = localisation;
	}
	@Transient
	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}
	
	@OneToOne
	@JsonIgnore
	public Voiture getVoiture() {
		return voiture;
	}

	public void setVoiture(Voiture voiture) {
		this.voiture = voiture;
		if (voiture!=null)
			voiture.setProprietaire(this);
	}
	
	/**
	 *  Le conducteur peut proposer un trajet avec sa voiture 
	 * @param depart
	 * @param dest
	 * @param dateDeDepart
	 * @return
	 */
	public Evenement proposeTrajet(String depart, String dest, Date dateDeDepart){
		Evenement trajet=new Evenement();
		trajet.setDateDeDepart(dateDeDepart);
		trajet.setDepart(depart);
		trajet.setDest(dest);
		trajet.setConducteur(this);
		trajet.setNbPersonRest(this.getVoiture().getNbPlaceTotal()-1);
		this.listEvCond.add(trajet);
		return trajet;
	}
	
	public Commentaire redigeCom(Evenement ev, String ch)
	{
		Commentaire c = new Commentaire();
		c.setPersonne(this);
		c.setEvenement(ev);
		ev.getListComEv().add(c);
		c.setValue(ch);
		ev.getListComEv().add(c);
		return c;
		
	}
	
	@JsonIgnore 
	@OneToMany(mappedBy="conducteur")
	public List<Evenement> getListEvCond() {
		return listEvCond;
	}


	public void setListEvCond(List<Evenement> listEvCond) {
		this.listEvCond = listEvCond;
	}
	
	@JsonIgnore 
	@OneToMany(mappedBy="personne")
	public List<Commentaire> getListCom() {
		return listCom;
	}


	public void setListCom(List<Commentaire> listCom) {
		this.listCom = listCom;
	}
}
