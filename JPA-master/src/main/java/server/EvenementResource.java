package server;


import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import shared.*;

@Path("/ev")
public class EvenementResource implements MyService {
	static int evaluate=0;
	
	EntityManager manager;
	
	public  EvenementResource() {
		
		manager = SingletonManager.getManager();
		
		if(evaluate==0)
		{
			this.remplirBdd(manager);
			evaluate=1;
		}
		
        }
	
	private void remplirBdd(EntityManager manager) {
		// TODO Auto-generated method stub
		EntityTransaction t = manager.getTransaction();
		try {
			
			
			t.begin();
			Voiture v=new Voiture();
			v.setNbPlaceTotal(3);
			v.setSeries("Benz");
			manager.persist(v);
			Personne c=new Personne();
			((Personne) c).setVoiture(v);
			c.setNom("Sam le Conducteur");
			c.setDestination("Paris");
			c.setLocalisation("Rennes");
			Date date=new Date();
			Evenement trajet=((Personne) c).proposeTrajet(c.getLocalisation(),c.getDestination(),date);
			
			Personne p1=new Personne();
			Personne p2=new Personne();
			
			p1.setNom("toto");
			p2.setNom("tata");
			
			p1.setDestination("Paris");
			p1.setLocalisation("Rennes");
			
			p2.setDestination("Paris");
			p2.setLocalisation("Rennes");
			
			manager.persist(c);
			manager.persist(p1);
			manager.persist(p2);
			
			trajet.addParticipant(p1);
			manager.persist(p1.redigeCom(trajet, "super evenement trajet"));
			manager.persist(p2.redigeCom(trajet, "com 2"));
			trajet.addParticipant(p2);
			
			manager.persist(trajet);
			Logger.getGlobal().info(trajet.toString());
					
		} catch (Exception e) {
			e.printStackTrace();
		}
		t.commit();
		
	}
	
	@GET
	@Path("propose/{id}-{depart}-{dest}")
	@Produces({ MediaType.APPLICATION_JSON })
	public String proposeTrajet(@PathParam ("id") String id,@PathParam ("depart") String depart,@PathParam ("dest") String dest,
			Date dateDeDepart) {
		Query query=manager.createQuery("SELECT p FROM PERSONNE  AS p WHERE ID=id");
		List result=query.getResultList();
	
		Personne personne = (Personne) result.get(0);
		manager.persist(personne.proposeTrajet(depart, dest, dateDeDepart));
		return "OK";
	}
	
	@GET
	@Path("findev/{depart}/{dest}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Evenement> findEvenement(String depart, String dest) {
		Query query=manager.createQuery("SELECT evens FROM EVENEMENT AS evens WHERE DEPART="+depart+" AND DEST="+dest+"");
		List result=query.getResultList();
		return result;
	}

	
	public void redigeCom(long idPersone,long idEven, String ch) {
		Query query_p=manager.createQuery("SELECT p FROM PERSONNE AS p WHERE ID=idPersonne");
		List personnes=query_p.getResultList();	
		Personne personne = (Personne) personnes.get(0);
		Query query_e=manager.createQuery("SELECT evens FROM EVENEMENT AS evens WHERE ID=idEven");
		Evenement ev=(Evenement) query_e.getResultList().get(0);
		manager.persist(personne.redigeCom(ev, ch));
		
	}

	public void takeTrajet(long idPersonne, long idEven) {
		EntityTransaction t = manager.getTransaction();
		t.begin();
		
		Query query_p=manager.createQuery("SELECT p FROM PERSONNE AS p WHERE ID=idPersonne");
		List personnes=query_p.getResultList();	
		Personne personne = (Personne) personnes.get(0);
		Query query_e=manager.createQuery("SELECT evens FROM EVENEMENT AS evens WHERE ID=idEven");
		Evenement ev=(Evenement) query_e.getResultList().get(0);
		ev.addParticipant(personne);
		
		t.commit();
	}
	
	@Override
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Evenement> listev()
	{
		Query query = manager.createQuery("SELECT evens FROM Evenement AS evens");
		List<Evenement> ch=query.getResultList();
		return ch;
	}
	
	@GET
	@Path("/personne")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Personne> getAllPersonne()
	{
		Query query = manager.createQuery("select e from Personne as e");
		System.out.println("chargement des utilisateurs ....");
		List<Personne> ch=query.getResultList();
		return ch;
	}

	@DELETE
	@Path("delete/{id}")
	public void deleteById(@PathParam("id") String arg0) {
		
		EntityTransaction t = manager.getTransaction();
		t.begin();
		Personne p = manager.find(Personne.class,Long.parseLong(arg0));
		System.out.println("personne to delete ="+p.getNom());
		System.out.println("**********************************************");
		
		//retire la personne dans les evenements ou il a participé.
		for (Evenement e : p.getListEvent())
		{
			e.getParticipants().remove(p);
			System.out.println("remove ev_id: "+e.getId());
		}
		
		//supprime les evenements ou la personne est conducteur
		for (Evenement e : p.getListEvCond())
		{
			for(Personne p1: e.getParticipants())
			{
				e.getParticipants().remove(p1);
				p1.getListEvent().remove(e);//retire la participation à l'evenement avant suppression
				p1.getListEvCond().remove(e);
			}
			manager.remove(e);//supprime l'evenement si la personne est conducteur
		}
		manager.remove(p.getVoiture());
		manager.remove(p);
		t.commit();
	}

	@Override
	public void createPersonne() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String proposeTrajet(String id, Date dateDeDepart) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@GET
	@Path("comev/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Commentaire> getAllComFromEv(@PathParam("id") String id)
	{
		Evenement ev = manager.find(Evenement.class,Long.parseLong(id));
		return ev.getListComEv();
	}

	


}
