package Event;

import Rand.Rand;

/**
 * Created by Corentin on 21/04/2019.
 */
public class AppelerAscenseur extends Evenement {

    AppelerAscenseur(int etageDepart, int ID,Rand r)
    {
        super(0,ID, r);
        this.etageDepart = etageDepart;

    }

    public Evenement action(){
        EntrerAscenseur a = new EntrerAscenseur(etageDepart, ID, r);
        return a;
    }

    public boolean condition(int[] etageAscenseur) {
        for (int i = 0; i < etageAscenseur.length; i++)
        {
            if (etageAscenseur[i] == etageDepart)
                return true;
        }
        return false;
    }
}
