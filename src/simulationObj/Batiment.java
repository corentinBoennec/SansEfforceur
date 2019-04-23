package simulationObj;

import Event.*;
import simulationObj.Ascenseur;

import javax.swing.tree.ExpandVetoException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Corentin on 21/04/2019.
 */
public class Batiment {
    private Ascenseur ascenseurs;
    private List<Evenement> events;
    private List<Client> clients;

    public Batiment(int nbAsenseur)
    {
        //for(int i = 0; i < nbAsenseur; i++)
        ascenseurs = new Ascenseur(0);
        clients= new ArrayList<Client>();
        events = new ArrayList<Evenement>();


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
        //pour chaque event de la liste,
        for (Evenement e: events)
        {
            //si son temps est inférieur ou = à 0 on lance son action.
            if(e.getTempsDattenteAvantEffet() <= 0)
            {
                if(e instanceof Entrer)
                {
                    addClient(new Client(e.getID()));
                }
                if(e instanceof  AppelerAscenseur)
                {
                    ascenseurs.addEvent(e);
                }

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
                c.getOut();
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
                min = e.getTempsDattenteAvantEffet();
        }
    }


}
