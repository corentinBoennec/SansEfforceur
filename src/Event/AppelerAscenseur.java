package Event;

import Rand.Rand;

/**
 * Created by Corentin on 21/04/2019.
 */
public class AppelerAscenseur extends Evenement {
    Rand r;

    AppelerAscenseur(int etageDepart, int ID)
    {
        super(0,ID);
        this.etageDepart = etageDepart;
        this.ID = ID;
        this.r = new Rand(0.5, 60, 1, 7);
        if(etageDepart == 0)
            etageArrive = r.getUni();
        else
            etageArrive = 0;

    }

    public Evenement action(){
        Evenement e;
        AppelerAscenseur appel;
        Sortir s;

        if(etageDepart != 0)
        {
            s = new Sortir(ID);
            e = s;
        }
        else
        {
            appel = new AppelerAscenseur(etageArrive, ID);
            appel.setTempsDattenteAvantEffet(r.getExpo());
            e = appel;
        }

        return e;
    }



}
