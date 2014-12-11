package nouveau.server;

import nouveau.shared.NEvenement;
import nouveau.test.JPATest1;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by naleite on 14/12/11.
 */
@Path("/")
public class NResourceImpl implements NService {

    private EntityManager em;


    public NResourceImpl() {
        em=SingletonManager.getManager();
        JPATest1.main();
    }


    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public List<NEvenement> allEvenement() {
        Query query=em.createQuery("select evens from NEvenement as evens");
        List<NEvenement> res=query.getResultList();
        return res;
    }
}
