package nouveau.shared;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by naleite on 14/12/11.
 */
@Entity
@XmlRootElement
public class NCommentaire implements Serializable {
    private long id;
    private NPersonne reducteur;
    private NEvenement evenement;
    private String content;

    public NCommentaire(){
    //JPA default
    }

    public NCommentaire(NPersonne reducteur, NEvenement evenement,String content){
        setContent(content);
        setEvenement(evenement);
        setReducteur(reducteur);

    }

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne//(cascade = CascadeType.REMOVE)
    @JsonIgnore
    public NPersonne getReducteur() {
        return reducteur;
    }

    public void setReducteur(NPersonne reducteur) {
        this.reducteur = reducteur;
    }

    @ManyToOne//(cascade = CascadeType.REMOVE)
    @JsonIgnore
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
