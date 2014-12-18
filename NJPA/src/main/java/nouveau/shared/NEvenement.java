package nouveau.shared;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by naleite on 14/12/11.
 */

@Entity
@XmlRootElement
public class NEvenement implements Serializable{
    private long id;
    private String villeDepart,villeDest;
    private List<NPersonne> participants=new ArrayList<NPersonne>();
    private NPersonne conducteur;
    private int nbPlaceReste;
    private NVoiture voiture;
    private List<NCommentaire> commentaires=new ArrayList<NCommentaire>();


    NEvenement(){
        //JPA default
    }

    public NEvenement(NPersonne p,String depart,String dest){
        this.voiture=p.getVoiture();
        if(voiture!=null) {
            this.villeDepart = depart;
            this.villeDest = dest;
            this.conducteur = p;
            this.nbPlaceReste = p.getVoiture().getNbPlaceTotal();
            addParticipant(conducteur);
        }
        else{
            throw new NoSuchElementException("Voiture is null");
        }

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
    public String getVilleDepart() {
        return villeDepart;
    }

    public void setVilleDepart(String villeDepart) {
        this.villeDepart = villeDepart;
    }

    @Column
    public String getVilleDest() {
        return villeDest;
    }

    public void setVilleDest(String villeDest) {
        this.villeDest = villeDest;
    }

    @ManyToMany
    public List<NPersonne> getParticipants() {
        return participants;
    }

    public void setParticipants(List<NPersonne> participants) {
        this.participants = participants;
    }

    @Column
    public int getNbPlaceReste() {
        return nbPlaceReste;
    }

    public void setNbPlaceReste(int nbPlaceReste) {
        this.nbPlaceReste = nbPlaceReste;
    }

    @OneToOne
    public NVoiture getVoiture() {
        return voiture;
    }

    public void setVoiture(NVoiture voiture) {
        this.voiture = voiture;
        //setConducteur(voiture.getOwner());

    }

    @OneToOne
    public NPersonne getConducteur() {
        return conducteur;
    }

    public void setConducteur(NPersonne conducteur) {
        this.conducteur = conducteur;
    }

    public boolean addParticipant(NPersonne p){

        if(nbPlaceReste>=1){

            boolean b = participants.add(p);
            nbPlaceReste--;
            return b;
        }
        else{
            return false;
        }


    }

    @OneToMany(mappedBy = "evenement")
    public List<NCommentaire> getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(List<NCommentaire> commentaires) {
        this.commentaires = commentaires;
    }


    protected boolean ValideCreation(){
        if(this.voiture!=null && this.conducteur==voiture.getOwner()
                && this.nbPlaceReste==this.voiture.getNbPlaceTotal()-1
                && this.participants.contains(voiture.getOwner())){
            return true;
        }
        else{
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public boolean removePersonne(NPersonne p){
        if(participants.contains(p) ) {
            if( p!=conducteur) {
                participants.remove(p);
                nbPlaceReste++;
                p.removeEvenement(this);
                return true;
            }
            else{
                for(int i=0;i<participants.size();i++){
                    p.removeEvenement(this);
                }
                participants.clear();
                return true;
            }

        }
        else{
            return false;
        }
    }

    public void removePersonneSimple(NPersonne p){
        if(participants.contains(p) && !getConducteur().equals(p)) {
            participants.remove(p);
            nbPlaceReste++;
        }
        else if((participants.contains(p) && getConducteur().equals(p))) {
            participants.remove(p);
            nbPlaceReste++;
            setConducteur(null);
            setVoiture(null);
            //setNbPlaceReste(0);
        }
    }

    public void removeCommentaire(NCommentaire c){
        commentaires.remove(c);
    }


    @PreRemove
    public void onPreremove(){

            for(NPersonne p: this.participants){
                p.removeEvenement(this);
            }
            this.commentaires.clear();
            this.participants.clear();
    }

    public void addCommentaire(NCommentaire c){
        commentaires.add(c);
    }



    @Override
    public String toString(){
        String s1="conducteur:"+getConducteur().getNom()+".De "+getVilleDepart()+" a "+getVilleDest()+",reste "+nbPlaceReste+" Places, particips:\n";
        Iterator<NPersonne> iter=participants.iterator();
        while (iter.hasNext()){
            s1+=iter.next().getNom()+"\n";
        }
        return s1;
    }


}
