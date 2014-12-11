import nouveau.server.SingletonManager;
import nouveau.shared.NEvenement;
import nouveau.shared.NPersonne;
import nouveau.shared.NVoiture;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * Created by naleite on 14/12/11.
 */
public class JpaTest {
    SingletonManager singletonManager=new SingletonManager();
    //@Test
    public void testJPA() {


        //EntityManagerFactory factory = Persistence
         //       .createEntityManagerFactory("dev");
       // EntityManager manager = factory.createEntityManager();

        EntityManager manager = SingletonManager.getManager();
        EntityTransaction t = manager.getTransaction();
        /*try {

            t.begin();

            NVoiture v1=new NVoiture();
            v1.setNbPlaceTotal(4);
            NPersonne p1=new NPersonne();
            p1.setMyCar(v1);
            p1.setNom("p1");
            v1.setOwner(p1);
            Assert.assertTrue(v1 != null);
            manager.persist(p1);
            manager.persist(v1);


            NPersonne p2=new NPersonne();
            p2.setNom("p2");
            manager.persist(p2);

            NEvenement ev1=p1.createTrajet("Rennes", "paris");
            p2.ajouteMoi(ev1);
            manager.persist(ev1);
            Logger.getGlobal().info(ev1.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        t.commit();
*/

        Assert.assertTrue(t!=null);
    }


}
