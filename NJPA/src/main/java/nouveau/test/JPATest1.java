package nouveau.test;

import nouveau.server.SingletonManager;
import nouveau.shared.NCommentaire;
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
    private static int exe=1;

    public void setManager(EntityManager manager) {
        this.manager = manager;
    }

    public JPATest1(EntityManager manager) {
        this.setManager(manager);
    }


    public static void decExe(){
        exe--;
    }

    public static int getExe(){
        return exe;
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



            NVoiture v1 = new NVoiture(3);
            NPersonne p1 = new NPersonne("p1",v1);
            //p1.setMyCar(v1);
            //p1.setNom("p1");
            //v1.setOwner(p1);


            manager.persist(p1);
            manager.persist(v1);


            NPersonne p2 = new NPersonne();
            p2.setNom("p2");
            manager.persist(p2);


            NEvenement ev1 = p1.createTrajet(v1, "Rennes", "paris");
            p2.ajouteMoi(ev1);
            manager.persist(ev1);
            Logger.getGlobal().info(ev1.toString());

            NVoiture v2 = new NVoiture();
            NPersonne p3 = new NPersonne("p3",v2);
            v2.setNbPlaceTotal(4);
            v2.setOwner(p3);
            p3.setNom("p3");
            //p3.setMyCar(v2);
            manager.persist(v2);
            manager.persist(p3);

            //NEvenement ev2=new NEvenement();
            NEvenement ev2 = p3.createTrajet(v2, "Paris", "Rennes");
            ev2.setVoiture(v2);
            ev2.setConducteur(p3);
            ev2.addParticipant(p1);
            ev2.setVilleDepart("Paris");
            ev2.setVilleDest("Rennes");
            ev2.addParticipant(p2);

            NEvenement ev3 = new NEvenement(p3,"Shanghai","Pekin");
            ev3.addParticipant(p2);
            manager.persist(ev3);
            NCommentaire c1=new NCommentaire(p1,ev1,"nice");
            NCommentaire c2=new NCommentaire(p1,ev2,"nice2");
            NCommentaire c3=new NCommentaire(p1,ev1,"nice3");
            manager.persist(c1);
            manager.persist(c2);
            manager.persist(c3);
            Logger.getGlobal().info(ev2.toString());
            manager.persist(ev2);

            Logger.getGlobal().info(p1.toString());

            Logger.getGlobal().info(ev3.toString());


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            t.commit();
        }


        Query query=manager.createQuery("select p.commentaires from NPersonne p where p.id=1");
        List<NCommentaire> res=(List<NCommentaire>) query.getResultList();
        for(NCommentaire c:res){
            System.out.println("red: "+c.getReducteur()+" ev: "+c.getEvenement());
            System.out.println(c.getContent());
        }

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

        /*Query query=manager.createQuery("select p.evenements from NPersonne p where p.id=1");


        List<NEvenement> evs=query.getResultList();

        //evs.get(1).removePersonne((NPersonne)query1.getSingleResult());
        for(int i=0;i<evs.size();i++){

            System.out.println(evs.get(i).toString());
        }
*/
    /*    t.begin();
        manager.remove(evs.get(1));
        //Query query1=manager.createQuery("delete from NEvenement e where e.id=2");
        //int res=query1.executeUpdate();
        //System.out.println("res:"+res);

        t.commit();


         query=manager.createQuery("select p from NPersonne p");


        List<NPersonne> evs1=query.getResultList();

        t.begin();
        manager.remove(evs1.get(0));
        //System.out.println("res1");
        t.commit();
        //evs.get(1).removePersonne((NPersonne)query1.getSingleResult());
        for(int i=0;i<evs1.size();i++){

            System.out.println(evs1.get(i).toString());
        }
*/

        /*Query query2=manager.createQuery("select p from NPersonne as p where p.id=2");

        EntityTransaction tx = manager.getTransaction();
        try {
            tx.begin();
            NPersonne p=(NPersonne) query2.getSingleResult();

            for(NEvenement ev:p.getEvenements()){
                p.removeEvenement(ev);
                System.out.println("eveid:"+ev.getId());
            }


            manager.remove(query2.getSingleResult());

        }
        catch (Exception e){}
        finally {
            tx.commit();
        }
*/
    }

    public static void main(String[] args){
        main();
    }
}
