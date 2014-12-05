package shared;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;


/**
 * Classe qui définit un Evenement
 *
 */
@Entity
public class Evenement {


private long id ;
private String dest;
private String depart;
private Date dateDeDepart;
private int nbPersonRest;
private Personne conducteur;
private List<Personne> participants=new ArrayList<Personne>() ;
private List<Commentaire> listComEv= new ArrayList<Commentaire>();

protected void setId(long id) {
	this.id = id;
}
protected void setNbPersonRest(int nbPersonRest) {
	this.nbPersonRest = nbPersonRest;
}
protected void setParticipants(List<Personne> participants) {
	this.participants = participants;
}

public Evenement() {
	this.participants=new ArrayList<Personne>();
}
/**
 * @return the id
 */
@Id @GeneratedValue
public long getId() {
	return id;
}

/**
 * @return the dest
 */
@Column
public String getDest() {
	return dest;
}

/**
 * @param dest the dest to set
 */
public void setDest(String dest) {
	this.dest = dest;
}

/**
 * @return the depart
 */
@Column
public String getDepart() {
	return depart;
}

/**
 * @param depart the depart to set
 */
public void setDepart(String depart) {
	this.depart = depart;
}


/**
 * @return the dateDeDepart
 */
@Column
public Date getDateDeDepart() {
	return dateDeDepart;
}
/**
 * @param dateDeDepart the dateDeDepart to set
 */
public void setDateDeDepart(Date dateDeDepart) {
	this.dateDeDepart = dateDeDepart;
}




/**
 * 
 * @param p ajoute p à l'évenement
 */
public void addParticipant(Personne p){
	if (!participants.contains(p) && getNbPersonRest()>0){
		this.participants.add(p);
		p.getListEvent().add(this);//ajoute l'evenement au participant
		this.nbPersonRest--;
		System.out.println("One person added in this trip!");
	}
	else {
		System.err.println("Can not add!");
	}
}



@ManyToMany
public List<Personne> getParticipants(){
	return this.participants;
}


public Iterator<Personne> IterParticipants(){
	
	return getParticipants().iterator();
}

/**
 * @return the nbPersonRest
 */
@Column
public int getNbPersonRest() {
	return nbPersonRest;

}


@Override
public String toString(){
	String info="Info Trajet: id="+this.getId()+" . From "+this.getDepart()+" to "+this.getDest()+" at "+getDateDeDepart();
	String info2 = " \nParicipant:";
	Iterator<Personne> iter=IterParticipants();
	while(iter.hasNext()){
		Personne p = iter.next();
		info2+=p.getId()+"    "+p.getNom()+"\n\t"; 
	}
	
	
	return info+info2;
}

@ManyToOne
public Personne getConducteur() {
	return conducteur;
}
public void setConducteur(Personne conducteur) {
	this.conducteur = conducteur;
}
@OneToMany(mappedBy="evenement")
public List<Commentaire> getListComEv() {
	return listComEv;
}
public void setListComEv(List<Commentaire> listComEv) {
	this.listComEv = listComEv;
}

}
