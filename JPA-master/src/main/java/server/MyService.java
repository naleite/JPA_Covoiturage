package server;

import java.util.Date;
import java.util.List;

import shared.Commentaire;
import shared.Evenement;
import shared.Personne;

public interface MyService {
	
	//EVENEMENT
	
	public abstract String proposeTrajet(String id, Date dateDeDepart);
	
	public abstract List<Evenement> listev();
	
	//COMMENTAIRE
	
	public abstract void redigeCom(long idPersone,long idEven, String ch);
	
	public abstract List<Commentaire> getAllComFromEv(String idEven);
	
	//PERSONNE
	
	public abstract void takeTrajet(long idPersonne, long idEven);

	public abstract void deleteById(String arg0);
	
	public abstract void createPersonne();
	
	public List<Personne> getAllPersonne();
}
