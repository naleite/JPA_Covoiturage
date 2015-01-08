package server;

import java.util.Date;
import java.util.List;

import shared.Commentaire;
import shared.Evenement;
import shared.Personne;

public interface MyService {
	
	//EVENEMENT
	
	public List<Evenement> findEvenement(String depart,String dest);//OK - mais pas utilisé dans AnularJS
	
	public Evenement proposeTrajet(String id,String depart,String dest);//OK
	
	public abstract List<Evenement> listev(); //OK
	
	public abstract Evenement getEvById(String idE); //OK
	
	public abstract List<Evenement> getEvPersonne(String idP);
	
	public abstract List<Evenement> getEvNotParticipatePersonne(String idP);
	
	public List<Evenement> getEvNotParticipatePersonneAndDispo(String id);
	
	//COMMENTAIRE
	
	public abstract String redigeCom(String idPersone,String idEven, String ch);
	
	public abstract List<Commentaire> getAllComFromEv(String idEven);//OK
	
	//PERSONNE
	
	public abstract void takeTrajet(String idPersonne, String idEvenement);//OK

	public abstract String deleteById(String arg0);//ok
	
	public abstract String deleteByEvId(String arg0);//OK
	
	public abstract Personne changeNom(String id, String nom);//Ok
	
	//OK
	public abstract Personne createPersonne(String nom,String dest,String local, String series,String nbplace );
	
	public List<Personne> getAllPersonne(); //OK
	
	public Personne getPersonne(String id); //OK
}
