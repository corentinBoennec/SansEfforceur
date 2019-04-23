package simulationObj;

/**
 * Created by Corentin on 21/04/2019.
 */
public class Client {
    int tempsDattente;
    boolean in;
    int ID;


    public Client(int ID)
    {
        this.ID = ID;
        tempsDattente = 0;
        in = false;
    };

    public int getID()
    {
        return ID;
    }

    public void setIn(boolean b){
        in = b;
    }

}
