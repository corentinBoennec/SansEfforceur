package simulationObj;

import Event.Evenement;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Corentin on 21/04/2019.
 */
public class Ascenseur {
    private List<Evenement> events;
    private int nbPersonne;
    private int nbMaxPersonne;
    private int etage;
    private int nbEtage;
    private boolean direction; //false up, true down
    private int ID;

    Ascenseur(int ID)
    {
        this.ID = ID;
        direction = false;
        nbEtage = 7;
        etage = 0;
        nbMaxPersonne = 10;
        nbPersonne = 0;
        events = new ArrayList();
    }

    public void addEvent(Evenement event)
    {
        events.add(event);
    }

    public int getEtage() {
        return etage;
    }

    public int getID() {
        return ID;
    }

    public boolean isDirectionDown() {
        return direction;
    }

    public int getNbEtage() {
        return nbEtage;
    }

    public int getNbMaxPersonne() {
        return nbMaxPersonne;
    }

    public int getNbPersonne() {
        return nbPersonne;
    }

}
