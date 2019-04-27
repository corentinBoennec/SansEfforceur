package simulationObj;

/**
 * Created by Corentin on 21/04/2019.
 */
public class Client {
    private int tempsDattente;
    private boolean in;
    private boolean isWaiting;
    private int ID;
    private int etageCourrant;



    public Client(int ID)
    {
        this.ID = ID;
        tempsDattente = 0;
        in = false;
        isWaiting = false;
    };

    public int getID()
    {
        return ID;
    }

    public void setIn(boolean b){
        in = b;
    }

    public int getEtageCourrant() {
        return etageCourrant;
    }

    public int getTempsDattente() {
        return tempsDattente;
    }

    public void setEtageCourrant(int etageCourrant) {
        this.etageCourrant = etageCourrant;
    }

    public void setTempsDattente(int tempsDattente) {
        this.tempsDattente = tempsDattente;
    }
    public void addTempsDattente(int tempsDattente) {this.tempsDattente += tempsDattente; }

    public void setWaiting(boolean waiting) {
        isWaiting = waiting;
    }

    public boolean getIsWaiting() {
        return isWaiting;
    }
}
