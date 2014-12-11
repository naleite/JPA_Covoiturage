package nouveau.test;

import nouveau.server.SingletonManager;
import nouveau.shared.NEvenement;
import nouveau.shared.NPersonne;
import nouveau.shared.NVoiture;

import javax.persistence.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by naleite on 14/12/11.
 */
public class JPATest1 {

    private EntityManager manager;

    public void setManager(EntityManager manager) {
        this.manager = manager;
    }

    public JPATest1(EntityManager manager) {
        this.setManager(manager);
    }


    public static void main() {

        /*EntityManagerFactory factory = Persistence
                .createEntityManagerFactory("dev");
        EntityManager manager = factory.createEntityManager();*/
        EntityManager manager = SingletonManager.getManager();
        JPATest1 test = new JPATest1(manager);

        EntityTransaction t = manager.getTransaction();


        t.begin();

        try {
            NVoiture v1 = new NVoiture();
            v1.setNbPlaceTotal(4);
            NPersonne p1 = new NPersonne();
            //p1.setMyCar(v1);
            p1.setNom("p1");
            v1.setOwner(p1);



            manager.persist(p1);
            manager.persist(v1);


            NPersonne p2 = new NPersonne();
            p2.setNom("p2");
            manager.persist(p2);


            NEvenement ev1 = p1.createTrajet(v1,"Rennes", "paris");
            p2.ajouteMoi(ev1);
            manager.persist(ev1);
            Logger.getGlobal().info(ev1.toString());

            NVoiture v2=new NVoiture();
            NPersonne p3=new NPersonne();
            v2.setNbPlaceTotal(4);
            v2.setOwner(p3);
            p3.setNom("p3");
            //p3.setMyCar(v2);
            manager.persist(v2);
            manager.persist(p3);

            //NEvenement ev2=new NEvenement();
            NEvenement ev2=p3.createTrajet(v2,"Paris","Rennes");
            ev2.setVoiture(v2);
            ev2.setConducteur(p3);
            ev2.addParticipant(p1);
            ev2.setVilleDepart("Paris");
            ev2.setVilleDest("Rennes");
            ev2.addParticipant(p2);

            Logger.getGlobal().info(ev2.toString());
            manager.persist(ev2);

            Logger.getGlobal().info(p1.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        t.commit();

        /*Query query=manager.createQuery("select p from NPersonne p where p.id=1");
        NPersonne pp=(NPersonne) query.getSingleResult();
        System.out.println(pp.getNom());

        Query query1=manager.createQuery("select v from NVoiture v, NPersonne p where p.id=1 and v.owner=p.id");
        NVoiture v2=(NVoiture) query1.getSingleResult();
        System.out.println(v2.getOwner().getNom());
        v2.setNbPlaceTotal(10);
        System.out.println(v2.getNbPlaceTotal());

        t.begin();
        manager.persist(v2);
        t.commit();*/

        Query query=manager.createQuery("select p.evenements from NPersonne p where p.id=1");


        List<NEvenement> evs=query.getResultList();

        //evs.get(1).removePersonne((NPersonne)query1.getSingleResult());
        for(int i=0;i<evs.size();i++){

            System.out.println(evs.get(i).toString());
        }

    /*    t.begin();
        manager.remove(evs.get(1));
        //Query query1=manager.createQuery("delete from NEvenement e where e.id=2");
        //int res=query1.executeUpdate();
        //System.out.println("res:"+res);

        t.commit();

         query=manager.createQuery("select p.evenements from NPersonne p where p.id=3");


        List<NEvenement> evs1=query.getResultList();

        System.out.println("res1");
        //evs.get(1).removePersonne((NPersonne)query1.getSingleResult());
        for(int i=0;i<evs1.size();i++){

            System.out.println(evs1.get(i).toString());
        }*/
    }
}
