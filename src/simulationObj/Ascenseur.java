package simulationObj;

import Event.Evenement;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Corentin on 21/04/2019.
 */
public class Ascenseur {
    private List<Integer> clientsID;
    private List<Integer> destinations;
    private int nbPersonne;
    //private int nbMaxPersonne;
    private int etage;
    private int nbEtage;
    private boolean direction; //false up, true down
    private int ID;
    private int tempsOccupe;

    Ascenseur(int ID)
    {
        this.ID = ID;
        direction = false;
        nbEtage = 8;
        etage = 0;
       // nbMaxPersonne = 10;
       // nbPersonne = 0;
        clientsID = new ArrayList();
        destinations = new ArrayList<>();
    }

    public List<Integer> getClientsID() {
        return clientsID;
    }

    public void addClient(Client client)
    {
        clientsID.add(client.getID());
        nbPersonne++;
    }
    public void removeClient(Client client) {
        clientsID.remove((Integer)client.getID());
        nbPersonne--;
    }
    public void addDestination(int etage)
    {
        destinations.add(etage);
    }
    public void removeDestination(int etage) {destinations.remove((Integer)etage);}

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

    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    public void setEtage(int etage) {
        this.etage = etage;
    }

    public void setNbPersonne(int nbPersonne) {
        this.nbPersonne = nbPersonne;
    }

    public int getNbPersonne() {
        return nbPersonne;
    }

    public List<Integer> getDestinations() {
        return destinations;
    }

    public void addTempsOccupe(int time) {this.tempsOccupe += time; }

    public int getTempsOccupe() {
        return tempsOccupe;
    }
}
