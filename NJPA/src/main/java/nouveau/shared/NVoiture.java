package nouveau.shared;

import javax.persistence.*;

/**
 * Created by naleite on 14/12/11.
 */
@Entity
public class NVoiture {

    private long id;
    private NPersonne owner;
    private int nbPlaceTotal=4;

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @OneToOne
    public NPersonne getOwner() {
        return owner;
    }

    public void setOwner(NPersonne owner) {
        this.owner = owner;

    }

    @Column
    public int getNbPlaceTotal() {
        return nbPlaceTotal;
    }

    public void setNbPlaceTotal(int nbPlaceTotal) {
        this.nbPlaceTotal = nbPlaceTotal;
    }
}
