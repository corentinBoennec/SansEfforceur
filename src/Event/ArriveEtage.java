package Event;

import Rand.Rand;

/**
 * Created by Corentin on 24/04/2019.
 */
public class ArriveEtage extends Evenement {
    Rand r;
    public ArriveEtage(int etageArrive, int ID)
    {
        super(0,ID);
        this.etageArrive = etageArrive;
        this.r = new Rand(0.5, 60, 1, 7);
    }
    public Evenement action() {
        Evenement e;
        AppelerAscenseur appel;
        Sortir s;

        if(etageArrive == 0)
        {
            s = new Sortir(ID);
            e = s;
        }
        else
        {
            appel = new AppelerAscenseur(etageArrive, ID);
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
