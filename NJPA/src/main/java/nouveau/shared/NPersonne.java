package nouveau.shared;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by naleite on 14/12/11.
 */
@Entity
@XmlRootElement
public class NPersonne implements Serializable {

    private long id;
    private String nom;
    private List<NEvenement> evenements=new ArrayList<NEvenement>();
    //private NVoiture myCar;
    private List<NCommentaire> commentaires=new ArrayList<NCommentaire>();

    public NPersonne(){
        //JPA
    }
    public NPersonne(String nom){
        this.nom=nom;
    }
    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @ManyToMany(mappedBy = "participants")
    @JsonIgnore
    public List<NEvenement> getEvenements() {
        return evenements;
    }

    public void setEvenements(List<NEvenement> evenements) {
        this.evenements = evenements;
    }

    @OneToMany(mappedBy = "reducteur")
    public List<NCommentaire> getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(List<NCommentaire> commentaires) {
        this.commentaires = commentaires;
    }


    public NEvenement createTrajet(NVoiture myCar,String depart, String dest) {
        NEvenement evenement=new NEvenement();
        evenement.setVoiture(myCar);
        evenement.setConducteur(this);
        evenement.setNbPlaceReste(myCar.getNbPlaceTotal());
        evenement.setVilleDepart(depart);
        evenement.setVilleDest(dest);
        evenement.addParticipant(this);
        if(evenement.ValideCreation()){
            return evenement;
        }
        else{
            System.err.println("evenment pas valide");
            return null;
        }

    }
    public void ajouteMoi(NEvenement ev){
        if(!ev.getParticipants().contains(this)) {
            ev.addParticipant(this);
        }
    }

    public void addEvenement(NEvenement ev){
        evenements.add(ev);

    }

    public boolean removeEvenement(NEvenement ev){

        if(evenements.contains(ev)) {
            evenements.remove(ev);
            return true;
        }
        else{
            return false;
        }
    }

    public String toString(){
        String s="nom: "+this.nom+" evs:\n";
        for(int i=0;i<evenements.size();i++){
            s+=evenements.get(i).getId()+"\n";
        }
        return s;
    }


}
