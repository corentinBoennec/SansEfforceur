package Event;
import Rand.Rand;
/**
 * Created by Corentin on 21/04/2019.
 */
public class Entrer extends Evenement {
   public Entrer(int tempsDattenteAvantEffet, int ID, Rand r)
    {
        super(tempsDattenteAvantEffet, ID, r);
    }


    public Evenement action() {
       AppelerAscenseur appel = new AppelerAscenseur(0, ID, r);
       return appel;
    }

    public boolean condition(int[] etageCourrant)
    {
        return true;
    }
}
