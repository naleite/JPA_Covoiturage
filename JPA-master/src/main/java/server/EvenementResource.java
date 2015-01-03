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
			
			Personne c1=new Personne();
			Voiture v1=new Voiture();
			((Personne) c1).setVoiture(v1);
			v1.setNbPlaceTotal(5);
			v1.setSeries("Peugeot 306");
			manager.persist(v1);
			c1.setNom("Vicky la Conductrice");
			c1.setDestination("Marseille");
			c1.setLocalisation("Rennes");
			Date date1=new Date();
			Evenement trajet2=((Personne) c1).proposeTrajet(c1.getLocalisation(),c1.getDestination(),date1);
			manager.persist(c1);
			manager.persist(trajet2);
			
			
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

    //@TODO Pas reussir ici, ni Get ni Post. Mais je crois post est plus logique.
	@POST
	@Path("propose/{id}-{depart}-{dest}")
	//@Produces({ MediaType.APPLICATION_JSON })
	public Evenement proposeTrajet(@PathParam ("id") String id,@PathParam ("depart") String depart,@PathParam ("dest") String dest,
			Date dateDeDepart) {
		Query query=manager.createQuery("SELECT p FROM PERSONNE  AS p WHERE ID=id");
		List result=query.getResultList();
	
		Personne personne = (Personne) result.get(0);
        Evenement ev=personne.proposeTrajet(depart, dest, dateDeDepart);
		manager.persist(personne.proposeTrajet(depart, dest, dateDeDepart));
		return ev;
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
	public void deleteByEv(@PathParam("id") String arg0) {
		EntityTransaction t = manager.getTransaction();
		t.begin();
		Evenement e = manager.find(Evenement.class,Long.parseLong(arg0));
		
		e.getConducteur().getListEvCond().remove(e);
		for(Personne p: e.getParticipants())
		{
			p.getListEvent().remove(e);
		}
		for(Commentaire c: e.getListComEv())
		{
			c.getPersonne().getListCom().remove(c);
			manager.remove(c);
		}
		t.commit();
	}

	@DELETE
	@Path("delete/personne/{id}")
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
		System.out.println("deleting commentaire c......");
		for(Commentaire c: p.getListCom())
		{
			c.getEvenement().getListComEv().remove(c);
			System.out.println("remove com_id: "+c.getId()+" value = "+c.getValue());
			manager.remove(c);
			
		}
		
		//supprime les evenements ou la personne est conducteur
		System.out.println("deleting event from driver");
		
		for (Evenement e : p.getListEvCond())
		{
			System.out.println("**deleting personne from event");
			for(Personne p1: e.getParticipants())
			{
				e.getParticipants().remove(p1);
				p1.getListEvent().remove(e);//retire la participation à l'evenement avant suppression
				p1.getListEvCond().remove(e);
				System.out.println("remove participant: "+p1.getId());
			}
			System.out.println("**deleting commentaire from event");
			for(Commentaire c: e.getListComEv())
			{
				c.getEvenement().getListComEv().remove(c);
				manager.remove(c);
			}
			manager.remove(e);//supprime l'evenement si la personne est conducteur
		}
	
		if(p.getVoiture() != null)
		{
			manager.remove(p.getVoiture());
		}
		
		manager.remove(p);
		System.out.println("*******finish deleteById");
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
