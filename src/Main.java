import Event.Entrer;
import Rand.Rand;
import simulationObj.Batiment;
import simulationObj.Client;

public class Main {

    public static void main(String[] args) {
        int time = 600; //* 24; //24h en secondes
        Batiment bat = new Batiment(1);
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
                bat.addEvent(new Entrer(sum, i));
                //on garde un ID unique pour les evenements associés à une personne
                i++;
            }
            //on change de minute
            sum += 60;
        }

        System.out.println(bat.getEvents().size());

        while (!bat.getEvents().isEmpty()) {
            bat.executeEvents();
        }
    }
}