package nouveau.shared;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by naleite on 14/12/11.
 */
@Entity
@XmlRootElement
public class NVoiture implements Serializable {

    private long id;
    private NPersonne owner;
    private int nbPlaceTotal=4;

    public NVoiture(){
        //JPA
    }
    public NVoiture(NPersonne owner){
        setOwner(owner);
    }
    public NVoiture(NPersonne owner,int nbPlaceTotal){
        setOwner(owner);
        setNbPlaceTotal(nbPlaceTotal);
    }
    public NVoiture(int nbPlaceTotal){
        setNbPlaceTotal(nbPlaceTotal);
    }
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
