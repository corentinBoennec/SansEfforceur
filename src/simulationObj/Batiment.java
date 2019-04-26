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
    private Ascenseur ascenseurs;
    private List<Evenement> events;
    private List<Client> clients;
    private ArrayList<ArrayList<Client>> filesDattente;

    public Batiment(int nbAsenseur)
    {
        //for(int i = 0; i < nbAsenseur; i++)
        ascenseurs = new Ascenseur(0);
        clients= new ArrayList<Client>();
        events = new ArrayList<Evenement>();
        filesDattente = new ArrayList<ArrayList<Client>>();
        for(int i  = 0; i < 8; i++)
        {
            filesDattente.add(new ArrayList<Client>());
        }

    }

    public void addEvent(Evenement event)
    {
        if(event != null) {
            events.add(event);
    }

    }
    public void addClient(Client client)
    {
        clients.add(client);
    }

    public List<Evenement> getEvents()
    {
        return events;
    }


    public void executeEvents()
    {
        List<Evenement> toRemove = new ArrayList<Evenement>();
        List<Evenement> toAdd = new ArrayList<Evenement>();

        Evenement tmp;
        Client c;
        int time = 0;
        //pour chaque event de la liste,
        for (Evenement e: events)
        {
            c = getClient(e.getID());
            //si son temps est inférieur ou = à 0 on lance son action.
            if(e.getTempsDattenteAvantEffet() <= 0)
            {
                if(e instanceof Entrer)
                {
                    Client nouvelArrivant = new Client(e.getID());
                    nouvelArrivant.setIn(true);
                    addClient(nouvelArrivant);
                }
                if(e instanceof  AppelerAscenseur)
                {
                    //ajout d'un client a sa file d'attente
                    filesDattente.get(e.getEtageDepart()).add(c);
                    c.setWaiting(true);
                }
                if(e instanceof ArriveEtage)
                {
                   /* time = c.getEtageCourrant() - e.getEtageArrive()*10;
                    if(time < 0)
                        time = -time;*/
                    c.setEtageCourrant(e.getEtageArrive());
                    c.setWaiting(false);
                    /*for(Client cl : clients)
                    {
                        if(c.getIsWaiting())
                            c.addTempsDattente(time);
                    }*/
                }
                if(e instanceof EntrerAscenseur)
                {
                    //On sort le client de sa file.
                    filesDattente.get(e.getEtageDepart()).remove(c);
                    ascenseurs.addEvent(e);
                    ascenseurs.addDestination(e.getEtageArrive());
                }


                if(e.condition(getEtageCourrant(e.getID())))
                {
                    tmp = e.action();
                    //SI le nouvel evenement est une sortie on supprime le client
                    if (tmp instanceof Sortir)
                    {
                        deleteClient(tmp.getID());
                    }

                    // SINON le retour est différent de sortir et c'est donc un event qu'on ajoute à la liste
                    if(!(tmp instanceof Sortir))
                    {
                        toAdd.add(tmp);
                    }

                    //on supprime l'event de la liste
                    toRemove.add(e);
                }
            }
        }
        if(toAdd.isEmpty() && toRemove.isEmpty())
        {
            advanceTime();
        }
        else
        {
            events.addAll(toAdd);
            events.removeAll(toRemove);
        }


        // on incrémente tous les temps d'attente client encore dans le circuit
    }

    private void deleteClient(int ID)
    {
        //On va recup ses infos avant
        for (Client c: clients) {
            if(c.getID() == ID)
            {
                c.setIn(false);
                break;
            }
        }
    }

    private void advanceTime()
    {
        int min = events.get(0).getTempsDattenteAvantEffet();
        for (Evenement e: events)
        {
            if(e.getTempsDattenteAvantEffet() < min)
                min = e.getTempsDattenteAvantEffet();
        }
        for (Evenement e: events)
        {
            e.setTempsDattenteAvantEffet(e.getTempsDattenteAvantEffet() - min);
        }
        for(Client c : clients)
        {
            if(c.getIsWaiting())
                c.addTempsDattente(min);
        }
    }

    public int deplacerAscenseurs()
    {
        //deplacer les ascenseur d'une certaine manière
        return 0;
    }

    private Client getClient(int ID)
    {
        for (Client c: clients) {
            if(c.getID() == ID)
            {
                return c;
            }
        }
        return null;
    }

    private int getEtageCourrant(int ID)
    {
            for (Client c: clients) {
                if (c.getID() == ID)
                {
                    return c.getEtageCourrant();
                }
            }
            return -1;
    }

    public List<Client> getClientList() {
        return clients;
    }

    //On a un ascenseur et on monte toujours du 1er on descend toujours des autres
    //premier à demander premier à être servit
    public int clientsAServirFDFS(){
        return filesDattente.get(0).get(0).etageCourrant ;
    }
    //retourne l'étage ou aller et l'ascenseur à utiliser
    public List<Integer> clientsAServirFDFSMuliAscenseur(){
        /*
        * AJOUTER UN BOOLEAN FREE DANS ASCENSEUR
        *
        * int togo = filesDattente.get(0).get(0).etageCourrant;
        * int proxiMin = ascenseurs.getNbEtage() + 1;
        * Ascenseur toUse;
        * for( Ascenseur a: Asenceurs)
        * {
        *   if (a.free == TRUE)
        *   {
        *       int proxi = Math.abs(a.getNbEtage() - togo)
        *       if(proxiMin > proxi)
        *       {
        *           toUse = a;
        *       }
        *   }
        * }
        * List<Integer> res = new ArrayList<>();
        * res.add(togo);
        * res.add(a.ID);
        * return res;
        */
        return null;
    }

    //la demande « la plus proche », quelle que soit la direction dans laquelle on se déplace
    public int clientsAServirSSTF()
    {
        int proxiMin = ascenseurs.getNbEtage() + 1;
        int togo = 0;
        for(List<Client> c: filesDattente){
            int proxi = Math.abs(ascenseurs.getEtage() - c.get(0).getEtageCourrant());
            if(proxi<proxiMin){
                proxiMin = proxi;
                togo = c.get(0).etageCourrant;
            }
        }
        return  togo;
    }

    
}
