package Event;

/**
 * Created by Corentin on 21/04/2019.
 */
public class Entrer extends Evenement {
   public Entrer(int tempsDattenteAvantEffet, int ID)
    {
        super(tempsDattenteAvantEffet, ID);
    }


    public Evenement action() {
       AppelerAscenseur appel = new AppelerAscenseur(0, ID);
       return appel;
    }

    public boolean condition(int etageCourrant)
    {
        return true;
    }
}
