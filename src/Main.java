import Event.Entrer;
import Rand.Rand;
import simulationObj.Ascenseur;
import simulationObj.Batiment;
import simulationObj.Client;

public class Main {

    public static void main(String[] args) {
        int time = 24* 3600; //24h en secondes
        String algo = "SSTF";
        Batiment bat = new Batiment(2, algo);
        Rand r = new Rand(0.5, 60, 0, 7);
        //tout les (poissons) temps quelqu'un entre
        int sum = 0;
        int clientThisMin = 0;
        int i = 0;
        int k;
        //création avec décalage de l'arrivé des clients
        while (sum < time) {
            //on regarde combien de personne arrive  durant cette minute
            clientThisMin = r.getPoisson();
            for (k = 0; k < clientThisMin; k++) {
                //on crée l'évenement de l'arrivée de chacunes de ces personnes durant cette minute
                bat.addEvent(new Entrer(sum, i, r));
                //on garde un ID unique pour les evenements associés à une personne
                i++;
            }
            //on change de minute
            sum += 60;
        }

        //boucle de Simulation
        while (!bat.getEvents().isEmpty()) {
            bat.executeEvents();
        }

        double meanTime = 0;
        int nbClient = 0;
        for (Client c: bat.getClientList())
        {
            //if(c.getID() > 30 && c.getID() < bat.getClientList().size() - 30) // warmup et cooldown
            {
                meanTime += c.getTempsDattente();
                nbClient ++;
            }
        }
        double meanServerOccupancy = 0;
        for(Ascenseur a: bat.getAscenseurs())
        {
            meanServerOccupancy += (double)a.getTempsOccupe()/(double)time;
        }

        if(nbClient != 0)
            meanTime /= nbClient;
        if(bat.getAscenseurs().length != 0)
        {
            meanServerOccupancy /= bat.getAscenseurs().length;
            meanServerOccupancy *= 100;
        }

        System.out.println("le temps d'attente moyen est de " + meanTime);
        System.out.println("Le nombre de clients est "+ nbClient);
        System.out.println("L'algorithme d'ordonnancement utilisé est " + algo);
        System.out.println("l'occupation moyenne des serveurs est de " + meanServerOccupancy + "%");
    }
}