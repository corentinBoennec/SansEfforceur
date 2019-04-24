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

    public boolean condition(int etageCourrant)
    {
        if(etageCourrant == 0)
            return true;
        else
            return false;
    }
}
