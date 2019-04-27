package Rand;
import distribution.*;
/**
 * Created by Corentin on 21/04/2019.
 */
public class Rand {
    PoissonDistribution poisson;
    ExponentialDistribution expo;
    UniformIntegerDistribution uni;


    public Rand(double tauxMoyenPoisson, int moyenExpo, int minEtage, int maxEtage)
    {
        poisson = new PoissonDistribution(tauxMoyenPoisson);
        expo = new ExponentialDistribution(moyenExpo);
        uni = new UniformIntegerDistribution(minEtage+1, maxEtage);
    }

    public int getPoisson()
    {
        return poisson.sample();
    }
    public int getExpo()
    {
        return (int)(expo.sample());
    }
    public int getUni()
    {
        return uni.sample();
    }
}
