package simulationObj;

import Event.*;
import simulationObj.Ascenseur;

import javax.swing.tree.ExpandVetoException;
import java.util.ArrayList;
import java.util.List;
import java.lang.*;

/**
 * Created by Corentin on 21/04/2019.
 */
public class Batiment {
    private Ascenseur[] ascenseurs;
    private List<Evenement> events;
    private List<Client> clients;
    private ArrayList<ArrayList<Client>> filesDattente;
    private List<Client> fileDattenteGlobale;
    private String algo;

    public Batiment(int nbAsenseur, String algo) {
        ascenseurs = new Ascenseur[nbAsenseur];
        for(int i = 0; i < nbAsenseur; i++) {
            ascenseurs[i] = new Ascenseur(i);
        }
        clients = new ArrayList<Client>();
        events = new ArrayList<Evenement>();
        filesDattente = new ArrayList<ArrayList<Client>>();
        for (int i = 0; i < 8; i++) {
            filesDattente.add(new ArrayList<Client>());
        }
        fileDattenteGlobale = new ArrayList<Client>();

        this.algo = algo;
    }

    public void addEvent(Evenement event) {
        if (event != null) {
            events.add(event);
        }

    }


    public void addClient(Client client) {
        clients.add(client);
    }

    public List<Evenement> getEvents() {
        return events;
    }


    public void executeEvents() {
        List<Evenement> toRemove = new ArrayList<Evenement>();
        List<Evenement> toAdd = new ArrayList<Evenement>();

        Evenement tmp;
        Client c;
        int time = 0;
        //pour chaque event de la liste,
        for (Evenement e : events) {
            c = getClient(e.getID());
            //si son temps est inférieur ou = à 0 on lance son action.
            if (e.getTempsDattenteAvantEffet() <= 0) {
                if (e instanceof Entrer && e.getStatus()) {
                    Client nouvelArrivant = new Client(e.getID());
                    nouvelArrivant.setIn(true);
                    addClient(nouvelArrivant);
                    e.setOff();
                }
                if (e instanceof AppelerAscenseur && e.getStatus()) {
                    //ajout d'un client a sa file d'attente
                    filesDattente.get(c.getEtageCourrant()).add(c);
                    fileDattenteGlobale.add(c);
                    c.setWaiting(true);
                    e.setOff();
                }

                if (e instanceof EntrerAscenseur && e.getStatus()) {
                    //On sort le client de sa file.
                    filesDattente.get(e.getEtageDepart()).remove(c);
                    fileDattenteGlobale.remove(c);
                    for(int i = 0; i < ascenseurs.length; i++) {
                        if(ascenseurs[i].getEtage() == e.getEtageDepart()) {
                            ascenseurs[i].addClient(c);
                            ascenseurs[i].addDestination(e.getEtageArrive());
                            break;
                        }
                    }
                    e.setOff();
                }

                if (e instanceof ArriveEtage && e.getStatus()) {
                    c.setEtageCourrant(e.getEtageArrive());
                    c.setWaiting(false);
                    for(int i = 0; i < ascenseurs.length; i++) {
                            if(ascenseurs[i].getClientsID().contains(c))
                            {
                                ascenseurs[i].removeClient(c);
                                ascenseurs[i].removeDestination(e.getEtageArrive());
                            }
                            break;
                    }
                    e.setOff();
                }


                if (e.condition(getEtagesAscenseurs())) {
                    tmp = e.action();
                    //SI le nouvel evenement est une sortie on supprime le client
                    if (tmp instanceof Sortir) {
                        deleteClient(tmp.getID());
                    }

                    // SINON le retour est différent de sortir et c'est donc un event qu'on ajoute à la liste
                    if (!(tmp instanceof Sortir)) {
                        toAdd.add(tmp);
                    }

                    //on supprime l'event de la liste
                    toRemove.add(e);
                }
            }
        }
        if (!toAdd.isEmpty())
            events.addAll(toAdd);
        else // on laisse le temps aux evenement instantannnés de se succeder
        {
            int nbEtageParcourues = 0;
            //Deplacer les ascenseurs
            for(int i = 0; i < ascenseurs.length; i++)
            {
                nbEtageParcourues += Math.abs(deplacerAscensceur(i, algo));
            }

            //si la fonction a renvoyé 0 donc que personne n'est dans une file d'attente et que personne n'est dans l'ascenseur.
            if (nbEtageParcourues == 0) {
                //on avance jusqu'a prochain evennement
                advanceToNextEvent();
            } else {
                advanceTime( 10);
            }
        }

        if (!toRemove.isEmpty()) {
            events.removeAll(toRemove);
        }

    }

    private void deleteClient(int ID) {
        //On va recup ses infos avant
        for (Client c : clients) {
            if (c.getID() == ID) {
                c.setIn(false);
                break;
            }
        }
    }

    private void advanceToNextEvent() {
        int min = Integer.MAX_VALUE;
        for (Evenement e : events) {
            if (e.getTempsDattenteAvantEffet() < min)
                min = e.getTempsDattenteAvantEffet();
            if (min == 0)
                break;
        }
        if (min != 0) {
            for (Evenement e : events) {
                e.setTempsDattenteAvantEffet(e.getTempsDattenteAvantEffet() - min);
            }
            for (Client c : clients) {
                if (c.getIsWaiting())
                    c.addTempsDattente(min);
            }
        }

    }

    private void advanceTime(int time) {

        for (Evenement e : events) {
            e.setTempsDattenteAvantEffet(e.getTempsDattenteAvantEffet() - time);
        }
        for (Client c : clients) {
            if (c.getIsWaiting())
                c.addTempsDattente(time);
        }
        for(Ascenseur a : ascenseurs)
        {
            if(a.getNbPersonne() != 0)
            {
                a.addTempsOccupe(time);
            }
        }
    }

    private Client getClient(int ID) {
        for (Client c : clients) {
            if (c.getID() == ID) {
                return c;
            }
        }
        return null;
    }

    private int getEtageCourrant(int ID) {
        for (Client c : clients) {
            if (c.getID() == ID) {
                return c.getEtageCourrant();
            }
        }
        return -1;
    }

    public List<Client> getClientList() {
        return clients;
    }

    public int deplacerAscensceur(int ID,String algo) {
        int nbEtageDeplace = 0;

        switch (algo) {
            case "SSTF":
                nbEtageDeplace = deplacerAscenseurSSTF(ID);
                break;
            case "FCFS":
                nbEtageDeplace = deplacerAscenseursFCFS(ID);
                break;
            default:
                System.out.println("default (sstf)");
                nbEtageDeplace = deplacerAscenseurSSTF(ID);
                break;
        }
        return nbEtageDeplace;
    }

    public int ralentiBas(int ID)
    {
        int direction = 0;
        if(ascenseurs[ID].getEtage() > 0)
            direction = -1;
        return direction;
    }
    public int ralentiMilieu(int ID)
    {
        int direction = 0;
        if(ascenseurs[ID].getEtage() > ascenseurs[ID].getNbEtage()/2)
            direction = -1;
        else if(ascenseurs[ID].getEtage() < ascenseurs[ID].getNbEtage()/2)
            direction = 1;
        return direction;
    }

    public int[] getEtagesAscenseurs()
    {
        int[] etages = new int[ascenseurs.length];
        for (int i = 0; i < ascenseurs.length; i++)
        {
            etages[i] = ascenseurs[i].getEtage();
        }
        return etages;
    }

    public int deplacerAscenseurSSTF(int ID)
    {
        int direction = 0; //0 : pas de mouvement, 1 haut, -1 bas
        int etagevide = 0;
        int etageActuel = ascenseurs[ID].getEtage();
        int min = ascenseurs[ID].getNbEtage();
        int diff, abs;

        if(!ascenseurs[ID].getDestinations().isEmpty())// si l'on a des destinations prévues
        {
            for(int i = 0; i < ascenseurs[ID].getDestinations().size(); i++)
            {
                diff = etageActuel - ascenseurs[ID].getDestinations().get(i);
                abs = Math.abs(diff);
                if(diff != 0 && abs < min)
                {
                    min = abs;
                    if(diff > 0)
                    {
                        direction = -1;
                    }
                    else
                        direction = 1;
                }
            }
        }
        if(direction == 0) {
            for (int i = 0; i < filesDattente.size(); i++) {
                if (!filesDattente.get(i).isEmpty()) {
                    diff = etageActuel - i;
                    abs = Math.abs(diff);
                    if (diff != 0 && abs < min) {
                        min = abs;
                        if (diff > 0) {
                            direction = -1;
                        } else
                            direction = 1;
                    }
                } else
                    etagevide++;
            }
            if (etagevide == ascenseurs[ID].getNbEtage()) {
                direction = ralentiBas(ID);
            }

        }
        ascenseurs[ID].setEtage(ascenseurs[ID].getEtage() + direction);
        ascenseurs[ID].removeDestination(ascenseurs[ID].getEtage());
        boolean dir;
        if(direction == 1)
            dir = false;
        else
            dir = true;
        ascenseurs[ID].setDirection(dir);
        return direction;
    }
    public int deplacerAscenseursFCFS(int ID) {
        int direction = 0;
        if (!ascenseurs[ID].getDestinations().isEmpty())// si l'on a des destinations prévues
        {
            int diff;
            diff = ascenseurs[ID].getEtage() - ascenseurs[ID].getDestinations().get(0);
            if (diff != 0) {
                if (diff > 0) {
                    direction = -1;
                } else
                    direction = 1;
            }
        }
        if (direction == 0) {
            if (!fileDattenteGlobale.isEmpty()) {
                fileDattenteGlobale.get(0).getEtageCourrant();
                int diff;
                diff = ascenseurs[ID].getEtage() - fileDattenteGlobale.get(0).getEtageCourrant();
                if (diff != 0) {
                    if (diff > 0) {
                        direction = -1;
                    } else
                        direction = 1;
                }
                //éviter d'avoir 1 client dans 2 ascenseurs
                //ascenseurs[ID].getClientsID().contains(fileDattenteGlobale.get(0))
            } else {
                direction = ralentiBas(ID);
            }
        }
        ascenseurs[ID].setEtage(ascenseurs[ID].getEtage() + direction);
        ascenseurs[ID].removeDestination(ascenseurs[ID].getEtage());
        boolean dir;
        if(direction == 1)
            dir = false;
        else
            dir = true;
        ascenseurs[ID].setDirection(dir);
        return direction;

    }

    public Ascenseur[] getAscenseurs() {
        return ascenseurs;
    }
}
