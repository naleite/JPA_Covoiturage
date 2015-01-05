package server;

import java.util.Date;
import java.util.List;

import shared.Commentaire;
import shared.Evenement;
import shared.Personne;

public interface MyService {
	
	//EVENEMENT
	
	public Evenement proposeTrajet(String id,String depart,String dest);//OK
	
	public abstract List<Evenement> listev(); //OK
	
	public abstract Evenement getEvById(String idE); //OK
	
	//COMMENTAIRE
	
	public abstract void redigeCom(long idPersone,long idEven, String ch);
	
	public abstract List<Commentaire> getAllComFromEv(String idEven);//OK
	
	//PERSONNE
	
	public abstract void takeTrajet(String idPersonne, String idEvenement);//OK

	public abstract void deleteById(String arg0);//ok
	
	public abstract void deleteByEvId(String arg0);//OK
	
	public abstract Personne changeNom(String id, String nom);//Ok
	
	//OK
	public abstract Personne createPersonne(String nom,String dest,String local, String series,String nbplace );
	
	public List<Personne> getAllPersonne(); //OK
	
	public Personne getPersonne(String id); //OK
}
