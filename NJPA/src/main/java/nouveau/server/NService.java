package nouveau.server;

import nouveau.shared.NEvenement;
import nouveau.shared.NPersonne;

import javax.ws.rs.PathParam;
import java.util.List;

/**
 * Created by naleite on 14/12/11.
 */
public interface NService {

    public List<NEvenement> allEvenement();

     // public List<NEvenement> evenementsOnePerson(String nom);
    public List<NPersonne> allPersonne() ;
    public List<NEvenement> evenementsOnePerson(@PathParam("id")long id);
}
