package Event;

import Rand.Rand;

/**
 * Created by Corentin on 24/04/2019.
 */
public class ArriveEtage extends Evenement {
    public ArriveEtage(int etageArrive, int ID, Rand r)
    {
        super(0,ID, r);
        this.etageArrive = etageArrive;

    }
    public Evenement action() {
        Evenement e;
        AppelerAscenseur appel;
        Sortir s;

        if(etageArrive == 0)
        {
            s = new Sortir(ID, r);
            e = s;
        }
        else
        {
            appel = new AppelerAscenseur(etageArrive, ID, r);
            appel.setTempsDattenteAvantEffet(r.getExpo()*60);
            e = appel;
        }

        return e;
    }

    public boolean condition(int[] etageCourrant)
    {
            return true;
    }
}
