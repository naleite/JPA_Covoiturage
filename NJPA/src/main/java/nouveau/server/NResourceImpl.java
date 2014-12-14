package nouveau.server;

import com.sun.jersey.core.spi.factory.ResponseImpl;
import nouveau.shared.NEvenement;
import nouveau.shared.NPersonne;
import nouveau.shared.NVoiture;
import nouveau.test.JPATest1;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.List;

/**
 * Created by naleite on 14/12/11.
 */
@Path("/")
public class NResourceImpl implements NService {

    private EntityManager em;

    boolean hasDataInBd=false;

    public NResourceImpl() {
        em=SingletonManager.getManager();

        if(JPATest1.getExe()>0) {
            JPATest1.main();
            JPATest1.decExe();
        }
    }


    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public List<NEvenement> allEvenement() {
        Query query=em.createQuery("select evens from NEvenement as evens");
        List<NEvenement> res=query.getResultList();
        return res;
    }

    @GET
    @Path("per")
    @Produces({ MediaType.APPLICATION_JSON })
    public List<NPersonne> allPersonne() {
        Query query=em.createQuery("select p from NPersonne as p");
        List<NPersonne> res=query.getResultList();
        return res;
    }


    @GET
    @Path("/per/{iden}")
    @Produces({ MediaType.APPLICATION_JSON })
    public List<NEvenement> evenementsOnePerson(@PathParam("iden")long id) {
        Query query=em.createQuery("select p.evenements from NPersonne as p where p.id=:iden").setParameter("iden",id);
        List<NEvenement> res=query.getResultList();
        for(int i=0;i<res.size();i++){

            System.out.println(id+":\n"+res.get(i).toString());
        }
        return res;
    }

    @POST @Consumes("application/json")
    @Path("per/{nom}")
    public void ajoutePersonne(@PathParam("nom")String nom){
        EntityTransaction tx=em.getTransaction();
        tx.begin();
        NPersonne p=new NPersonne();
        p.setNom(nom);
        em.persist(p);
        tx.commit();

    }

    @POST @Consumes("application/json")
    @Path("per/{id}-{depart}-{dest}")
    public void createEve(@PathParam("id")long id,
                          @PathParam("depart")String depart,
                          @PathParam("dest")String dest){

        Query query=em.createQuery("select p from NPersonne as p where p.id=:iden").setParameter("iden",id);
        NPersonne p=(NPersonne) query.getSingleResult();
        Query query1=em.createQuery("select v from NVoiture as v where v.owner=id");
        NVoiture v= (NVoiture) query1.getSingleResult();
        //System.out.println(query1.getResultList().size());
        if(v!=null) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            NEvenement newEv=p.createTrajet(v, depart, dest);
            em.persist(newEv);
            tx.commit();

        }
        else{
            //TODO
        }

    }

    @DELETE
    @Path("/ev/{id}")
    public void deleteEvById(@PathParam("id")long id){

        Query query=em.createQuery("select ev from NEvenement as ev where ev.id=:iden").setParameter("iden",id);
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(query.getSingleResult());
        }
        catch (Exception e){}
        finally {
            tx.commit();
        }

    }


    @DELETE
    @Path("/per/{id}")
    public void deletePersonById(@PathParam("id")long id){

        Query query=em.createQuery("select p from NPersonne as p where p.id=:iden").setParameter("iden",id);

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            NPersonne p=(NPersonne) query.getSingleResult();

            for(NEvenement ev:p.getEvenements()){
                p.removeEvenement(ev);
            }


            em.remove(query.getSingleResult());
        }
        catch (Exception e){}
        finally {
            tx.commit();
        }

    }



}
