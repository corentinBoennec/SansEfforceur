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
                if(e instanceof Entrer && e.getStatus())
                {
                    Client nouvelArrivant = new Client(e.getID());
                    nouvelArrivant.setIn(true);
                    addClient(nouvelArrivant);
                    e.setOff();
                }
                if(e instanceof  AppelerAscenseur && e.getStatus())
                {
                    //ajout d'un client a sa file d'attente
                    filesDattente.get(c.getEtageCourrant()).add(c);
                    c.setWaiting(true);
                    e.setOff();
                }

                if(e instanceof EntrerAscenseur && e.getStatus())
                {
                    //On sort le client de sa file.
                    filesDattente.get(e.getEtageDepart()).remove(c);
                    ascenseurs.addClient(c);
                    ascenseurs.addDestination(e.getEtageArrive());
                    e.setOff();
                }

                if(e instanceof ArriveEtage && e.getStatus())
                {
                    c.setEtageCourrant(e.getEtageArrive());
                    c.setWaiting(false);
                    ascenseurs.removeClient(c);
                    ascenseurs.removeDestination(e.getEtageArrive());
                    e.setOff();
                }



                if(e.condition(ascenseurs.getEtage()))
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
        if(!toAdd.isEmpty())
            events.addAll(toAdd);
        else // on laisse le temps aux evenement instantannnés de se succeder
        {
            //Deplacer les ascenseurs
            int nbEtageParcourues = deplacerAscenseurSSTF();
            ascenseurs.removeDestination(ascenseurs.getEtage());
            //si la fonction a renvoyé 0 donc que personne n'est dans une file d'attente et que personne n'est dans l'ascenseur.
            if(nbEtageParcourues == 0) {
                //on avance jusqu'a prochain evennement
                advanceToNextEvent();
            }
            else
            {
                advanceTime(nbEtageParcourues * 10);
            }
        }

        if(!toRemove.isEmpty())
        {
            events.removeAll(toRemove);
        }

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

    private void advanceToNextEvent()
    {
        int min = Integer.MAX_VALUE;
        for (Evenement e: events)
        {
            if(e.getTempsDattenteAvantEffet() < min)
                min = e.getTempsDattenteAvantEffet();
            if(min == 0)
                break;
        }
        if(min != 0)
        {
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

    }
    private void advanceTime(int time)
    {

        for (Evenement e: events)
        {
            e.setTempsDattenteAvantEffet(e.getTempsDattenteAvantEffet() - time);
        }
        for(Client c : clients)
        {
            if(c.getIsWaiting())
                c.addTempsDattente(time);
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

     //premier à demander premier servit
    public int deplacerAscenseurFDFS()
    {
        int togo = 0;
        //si l'ascenseur est vide
        if(ascenseurs.getNbPersonne() == 0)
        {
            int proxiMin = ascenseurs.getNbEtage() + 1;
            int proxiAbs;
            int etagevide = 0;

            for(List<Client> c: filesDattente){
                if(!c.isEmpty())
                {
                    proxiAbs = Math.abs(ascenseurs.getEtage() - c.get(0).getEtageCourrant());

                    if(proxiAbs<proxiMin && proxiAbs != 0){
                        proxiMin = proxiAbs;
                        togo = c.get(0).etageCourrant;
                    }
                }
                else
                {
                    etagevide++;
                }
            }


            //test si il y avait bien quelqu'un en attente
            if(etagevide < ascenseurs.getNbEtage())
            {
                if(togo < ascenseurs.getEtage())
                {
                    ascenseurs.setDirection(true);
                }
                else
                {
                    ascenseurs.setDirection(false);
                }
                ascenseurs.setEtage(togo);
                return  proxiMin;
            }
            else return 0;
        }
        else //si l'ascenseur n'est pas vide
        {
            togo = ascenseurs.getDestinations().get(0);
            if(togo < ascenseurs.getEtage())
            {
                ascenseurs.setDirection(true);
            }
            else
            {
                ascenseurs.setDirection(false);
            }
            int nb = Math.abs(ascenseurs.getEtage() - togo);
            ascenseurs.setEtage(togo);
            return nb;
        }
    }

    public int deplacerAscenseurSSTF()
    {
        int togo = 0;
        //si l'ascenseur est vide
        if(ascenseurs.getNbPersonne() == 0)
        {
            int proxiMin = ascenseurs.getNbEtage() + 1;
            int proxiAbs;
            int etagevide = 0;

            for(List<Client> c: filesDattente)
            {
                if(!c.isEmpty())
                {
                    proxiAbs = Math.abs(ascenseurs.getEtage() - c.get(0).getEtageCourrant());
                    if(proxiAbs<proxiMin && proxiAbs != 0)
                    {
                        proxiMin = proxiAbs;
                        togo = c.get(0).etageCourrant;
                    }
                }
                else
                {
                    etagevide++;
                }
            }
            //test si il y avait bien quelqu'un en attente
            if(etagevide < ascenseurs.getNbEtage())
            {
                if(togo < ascenseurs.getEtage())
                {
                    ascenseurs.setDirection(true);
                }
                else
                {
                    ascenseurs.setDirection(false);
                }

                for(List<Client> c: filesDattente)
                {
                    if(!c.isEmpty())
                    {
                        if (ascenseurs.isDirectionDown() == false)
                        {
                            proxiAbs = Math.abs(ascenseurs.getEtage() - c.get(0).getEtageCourrant());
                            if(c.get(0).getEtageCourrant() < ascenseurs.getEtage())
                            {
                                if(proxiAbs<proxiMin && proxiAbs != 0)
                                {
                                    proxiMin = proxiAbs;
                                    togo = c.get(0).etageCourrant;
                                }
                            }
                        }else
                        {
                            proxiAbs = Math.abs(ascenseurs.getEtage() - c.get(0).getEtageCourrant());
                            if(c.get(0).getEtageCourrant() > ascenseurs.getEtage())
                            {
                                if(proxiAbs<proxiMin && proxiAbs != 0)
                                {
                                    proxiMin = proxiAbs;
                                    togo = c.get(0).etageCourrant;
                                }
                            }
                        }
                    }

                }
                ascenseurs.setEtage(togo);
                return  proxiMin;
            }
            else return 0;
        }
        else //si l'ascenseur n'est pas vide
        {
            int proxiMin = ascenseurs.getNbEtage() + 1;
            int proxiAbs;
            int etagevide = 0;
            togo = ascenseurs.getDestinations().get(0);
            if(togo < ascenseurs.getEtage())
            {
                ascenseurs.setDirection(true);
            }
            else
            {
                ascenseurs.setDirection(false);
            }
            for(List<Client> c: filesDattente)
            {
                if(!c.isEmpty())
                {
                    if (ascenseurs.isDirectionDown() == false)
                    {
                        proxiAbs = Math.abs(ascenseurs.getEtage() - c.get(0).getEtageCourrant());
                        if(c.get(0).getEtageCourrant() < ascenseurs.getEtage())
                        {
                            if(proxiAbs<proxiMin && proxiAbs != 0)
                            {
                                proxiMin = proxiAbs;
                                togo = c.get(0).etageCourrant;
                            }
                        }
                    }else
                    {
                        proxiAbs = Math.abs(ascenseurs.getEtage() - c.get(0).getEtageCourrant());
                        if(c.get(0).getEtageCourrant() > ascenseurs.getEtage())
                        {
                            if(proxiAbs<proxiMin && proxiAbs != 0)
                            {
                                proxiMin = proxiAbs;
                                togo = c.get(0).etageCourrant;
                            }
                        }
                    }
                }
                else
                {
                    etagevide++;
                }
            }

            int nb = Math.abs(ascenseurs.getEtage() - togo);
            ascenseurs.setEtage(togo);
            return nb;
        }
    }



}
