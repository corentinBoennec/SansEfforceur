package Event;

/**
 * Created by Corentin on 21/04/2019.
 */
public class Sortir extends Evenement {


    Sortir(int ID)
    {
        super(0,ID);
    }

    @Override
    public Evenement action() {
        return null;
    }

    public boolean condition(int[] etageAscenseur) {
        for (int i = 0; i < etageAscenseur.length; i++) {
            if (etageAscenseur[i] == 0)
                return true;
        }
        return false;
    }
}
