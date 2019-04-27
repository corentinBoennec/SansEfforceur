package Event;

import Rand.Rand;

/**
 * Created by Corentin on 24/04/2019.
 */
public class EntrerAscenseur extends Evenement {
    Rand r;
    public EntrerAscenseur(int etageDepart, int ID)
    {
        super(0,ID);
        this.r = new Rand(0.5, 60, 1, 7);
        this.etageDepart = etageDepart;
        if(etageDepart == 0)
            etageArrive = r.getUni();
        else
            etageArrive = 0;
    }
    @Override
    public Evenement action() {
        ArriveEtage a = new ArriveEtage(etageArrive, ID);
        return a;
    }

    @Override
    public boolean condition(int[] etageAscenseur) {
        for (int i = 0; i < etageAscenseur.length; i++) {
            if (etageAscenseur[i] == etageArrive)
                return true;
        }
        return false;
    }
}
