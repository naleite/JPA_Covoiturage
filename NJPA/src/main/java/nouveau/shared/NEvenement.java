package nouveau.shared;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by naleite on 14/12/11.
 */

@Entity
public class NEvenement {
    private long id;
    private String villeDepart,villeDest;
    private List<NPersonne> participants=new ArrayList<NPersonne>();
    private NPersonne conducteur;
    private int nbPlaceReste;
    private NVoiture voiture;





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
        setConducteur(voiture.getOwner());

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
            p.addEvenement(this);
            return b;
        }
        else{
            return false;
        }


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
