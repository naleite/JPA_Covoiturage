package nouveau.server;

import nouveau.shared.NCommentaire;
import nouveau.shared.NEvenement;
import nouveau.shared.NPersonne;
import nouveau.shared.NVoiture;
import nouveau.test.JPATest1;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
            JPATest1.test(em);
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
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("ev/{iden}")
    public NEvenement evenementById(@PathParam("iden") long id) {
        Query query=em.createQuery("select evens from NEvenement evens where evens.id=:iden").setParameter("iden",id);
        NEvenement res=(NEvenement) query.getSingleResult();
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
    @Path("/per/{iden}/ev") //lister tous les trajets de la personne de id=iden.
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

    @POST
    @Consumes("application/json")
    @Path("/ev/{eid}-{pid}/")
    public void addPertoEvenement(@PathParam("eid")long eid,@PathParam("pid") long pid){
        EntityTransaction tx=em.getTransaction();
        try {
            tx.begin();

            Query queryP = em.createQuery("select p from NPersonne p where p.id=:pid").setParameter("pid",pid);
            NPersonne p = (NPersonne) queryP.getSingleResult();
            Query queryE = em.createQuery("select e from NEvenement e where e.id=:eid").setParameter("eid",eid);
            NEvenement e = (NEvenement) queryE.getSingleResult();
            p.addEvenement(e);
        }
        catch (Exception e){}
        finally {
            tx.commit();
        }


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
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        if(v!=null) {


            NEvenement newEv=p.createTrajet(v, depart, dest);
            em.persist(newEv);


        }
        else{

            NEvenement newEvnoV=new NEvenement(p,depart,dest);
            em.persist(newEvnoV);
        }
        tx.commit();

    }


    @POST
    @Path("/ev/{eid}/per/{pid}")
    @Produces("text/plain")
    public String removeParticipant(@PathParam("pid") long pid, @PathParam("eid") long eid){
        Query queryP=em.createQuery("select p from NPersonne p where p.id=:pid").setParameter("pid",pid);
        Query queryE=em.createQuery("select e from NEvenement e where e.id=:eid").setParameter("eid",eid);
        NPersonne p=(NPersonne)queryP.getSingleResult();
        NEvenement e= (NEvenement) queryE.getSingleResult();
        EntityTransaction tx=em.getTransaction();
        try {
            tx.begin();

            if (e.getParticipants().contains(p) && !e.getConducteur().equals(p)) {
                e.removePersonneSimple(p);
                p.removeEvenement(e);
                return "ok";
            }

            else if((e.getParticipants().contains(p) && e.getConducteur().equals(p))) {
                e.removePersonneSimple(p);
                p.removeEvenement(e);
                e.setConducteur(null);
                e.setVoiture(null);
                tx.commit();
                return "ok";
            }

            else {
                return "nok";
            }

        }
        catch (Exception ex){return "nok";}
        finally {
            tx.commit();
        }
    }

    @DELETE
    @Path("/ev/{id}")
    @Produces("text/plain")
    public String deleteEvById(@PathParam("id") long id){

        Query query=em.createQuery("select ev from NEvenement as ev where ev.id=:iden").setParameter("iden",id);
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            //NEvenement ev=em.find(NEvenement.class,id);
            NEvenement ev=(NEvenement)query.getSingleResult();

            //supprimer les commentaire

            for (NCommentaire c : ev.getCommentaires()) {
                    c.setEvenement(null);
                    c.setReducteur(null);

            }

            for(NPersonne p:ev.getParticipants()){

                p.removeEvenement(ev);

                for(NCommentaire c:p.getCommentaires()){
                    if(c.getEvenement().equals(ev)) {
                        c.setReducteur(null);
                        c.setEvenement(null);
                    }
                }


            }
            ev.setConducteur(null);
            ev.setVoiture(null);
            ev.getParticipants().clear();
            ev.getCommentaires().clear();


            em.remove(ev);
            return "ok";
        }
        catch (Exception e){return "nok";}
        finally {
            tx.commit();
        }


    }


    @DELETE
    @Path("/per/{id}")
    @Produces("text/plain")
    public String deletePersonById(@PathParam("id")long id){

        Query query=em.createQuery("select p from NPersonne as p where p.id=:iden").setParameter("iden",id);

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            NPersonne p=(NPersonne) query.getSingleResult();

            for(NCommentaire c:p.getCommentaires()){
                c.setReducteur(null);
                c.setEvenement(null);
                p.removeCommentaire(c);

            }
            for(NEvenement ev:p.getEvenements()){

                ev.removePersonneSimple(p);

                for(NCommentaire c:ev.getCommentaires()){
                    if(c.getReducteur().equals(p)) {
                        c.setReducteur(null);
                        c.setEvenement(null);
                        ev.removeCommentaire(c);
                    }
                }
            }


            p.setVoiture(null);
            p.getEvenements().clear();
            p.getCommentaires().clear();

            //Query query1=em.createQuery("delete from NEvenement e where e.conducteur=:pid").setParameter("pid",id);
            //query1.executeUpdate();
            em.remove(p);

            return "ok";
        }
        catch (Exception e){return "nok";}
        finally {
            tx.commit();
        }

    }


    @GET
    @Produces("application/json")
    @Path("/com/")
    public List<NCommentaire> allCommentaire(){
        //Query q=em.createNamedQuery("select e from NEvenement e where e.id=:eid").setParameter("eid",eid);
        Query query=em.createQuery("select comm from NCommentaire as comm");
        List<NCommentaire> res=query.getResultList();
        return res;
    }

    @GET
    @Produces("application/json")
    @Path("/ev/{eid}/com")
    public List<NCommentaire> getCommentaireByEvent(@PathParam("eid")long eid){
        //em.clear();
        Query q=em.createQuery("select even.commentaires from NEvenement as even where even.id=:eid").setParameter("eid",eid);

        //NEvenement ev=(NEvenement) q.getSingleResult();


        List<NCommentaire> res=q.getResultList();
        return res;
    }

    @GET
    @Produces("application/json")
    @Path("/per/{pid}/com")
    public List<NCommentaire> getCommentaireByPer(@PathParam("pid")long pid){
        //em.clear();
        Query q=em.createQuery("select per.commentaires from NPersonne as per where per.id=:pid").setParameter("pid",pid);

        //NEvenement ev=(NEvenement) q.getSingleResult();


        List<NCommentaire> res=q.getResultList();
        return res;
    }

    @POST
    @Path("/com/{pid}-{eid}")
    @Produces("text/plain")
    public String addCommentaire(@PathParam("eid") long eid,@PathParam("pid") long pid, @FormParam("cmt") String cmt){
        Query queryP=em.createQuery("select p from NPersonne p where p.id=:pid").setParameter("pid",pid);
        Query queryE=em.createQuery("select e from NEvenement e where e.id=:eid").setParameter("eid",eid);
        NPersonne p=(NPersonne)queryP.getSingleResult();
        NEvenement e= (NEvenement) queryE.getSingleResult();
        EntityTransaction tx=em.getTransaction();

        try{
            tx.begin();
            NCommentaire commentaire=new NCommentaire(p,e,cmt);
            e.addCommentaire(commentaire);
            em.persist(commentaire);
            return "ok";

        }
        catch (Exception ex){return "nok";}
        finally {
            tx.commit();
        }

    }


}
