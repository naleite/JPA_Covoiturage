package nouveau.shared;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by naleite on 14/12/11.
 */
@Entity
public class NCommentaire implements Serializable {
    private long id;
    private NPersonne reducteur;
    private NEvenement evenement;
    private String content;

    NCommentaire(){
    //JPA default
    }

    public NCommentaire(NPersonne reducteur, NEvenement evenement,String content){
        this.content=content;
        this.evenement=evenement;
        this.reducteur=reducteur;
    }

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne
    public NPersonne getReducteur() {
        return reducteur;
    }

    public void setReducteur(NPersonne reducteur) {
        this.reducteur = reducteur;
    }

    @ManyToOne
    public NEvenement getEvenement() {
        return evenement;
    }

    public void setEvenement(NEvenement evenement) {
        this.evenement = evenement;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
