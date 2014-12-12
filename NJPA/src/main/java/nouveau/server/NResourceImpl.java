package nouveau.server;

import nouveau.shared.NEvenement;
import nouveau.shared.NPersonne;
import nouveau.test.JPATest1;

import javax.persistence.EntityManager;
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




}
