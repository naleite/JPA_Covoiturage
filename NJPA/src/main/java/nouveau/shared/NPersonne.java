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
    private NVoiture voiture;
    private List<NCommentaire> commentaires=new ArrayList<NCommentaire>();

    public NPersonne(){
        //JPA
    }
    public NPersonne(String nom){
        setNom(nom);
    }
    public NPersonne(String nom,NVoiture voiture){
        setNom(nom);
        setVoiture(voiture);

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


    @OneToOne(cascade = CascadeType.REMOVE)
    @JsonIgnore
    public NVoiture getVoiture() {
        return voiture;
    }

    public void setVoiture(NVoiture voiture) {
        this.voiture = voiture;

    }

    @OneToMany(mappedBy = "reducteur")
    @JsonIgnore
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

        return evenement;
    }
    public void addEvenement(NEvenement ev){
        if(!ev.getParticipants().contains(this)) {
            ev.addParticipant(this);
            evenements.add(ev);
        }
    }


    public void removeEvenement(NEvenement ev){

        if(evenements.contains(ev)) {
            evenements.remove(ev);
        }
    }

    public void addCommentaire(NCommentaire c){
        commentaires.add(c);
    }

    public void removeCommentaire(NCommentaire c){
        if(commentaires.contains(c)){
            commentaires.remove(c);
        }
    }

    public String toString(){
        String s="nom: "+this.nom+" evs:\n";
        for(int i=0;i<evenements.size();i++){
            s+=evenements.get(i).getId()+"\n";
        }
        return s;
    }


    public void onPreRemove(){

        System.out.println("in personne onPreRemove");
        for(NEvenement e: this.evenements){
            e.removePersonneSimple(this);
        }
        this.commentaires.clear();
        this.evenements.clear();
    }





}
