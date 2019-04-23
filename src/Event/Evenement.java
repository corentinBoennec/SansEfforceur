package Event;


/**
 * Created by Corentin on 21/04/2019.
 */
public abstract class Evenement {
    int tempsDattenteAvantEffet;
    int ID;
    int etageDepart;
    int etageArrive;

    public Evenement(int tempsDattenteAvantEffet, int ID)
    {
        this.tempsDattenteAvantEffet = tempsDattenteAvantEffet;
        this.ID = ID;
    }

    public abstract Evenement action();

    public int getID()
    {
        return ID;
    }

    public int getTempsDattenteAvantEffet() {
        return tempsDattenteAvantEffet;
    }

    public void setTempsDattenteAvantEffet(int tempsDattenteAvantEffet){
        this.tempsDattenteAvantEffet = tempsDattenteAvantEffet;
    }

    public int getEtageArrive() {
        return etageArrive;
    }

    public int getEtageDepart() {
        return etageDepart;
    }
}
