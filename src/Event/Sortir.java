package Event;

import Rand.Rand;
/**
 * Created by Corentin on 21/04/2019.
 */
public class Sortir extends Evenement {


    Sortir(int ID, Rand r)
    {
        super(0,ID, r);
    }

    @Override
    public Evenement action() {
        return null;
    }

    public boolean condition(int[] etageAscenseur) {
        for (int i : etageAscenseur) {
            if (i == 0)
                return true;
        }
        return false;
    }
}
