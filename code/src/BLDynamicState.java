/**
 * Created by felix on 02.10.16.
 */

//BLDynamicState is a wrapper object for the dynamic attributes of a BLState
public class BLDynamicState {
    private int costes;
    private int felicidad;
    //number of allready assigned packages (=index of next package)
    private int paqNum;
    //remaining space in the offers
    private double[] pesoRests;

    public BLDynamicState(int costes, int felicidad, int paqNum, double[] pesoRests) {
        this.costes = costes;
        this.felicidad = felicidad;
        this.paqNum = paqNum;
        this.pesoRests = pesoRests;
    }

    public int getCostes() {
        return costes;
    }

    public int getFelicidad() {
        return felicidad;
    }

    public int getPaqNum() {
        return paqNum;
    }

    //returns a copy of pesoRests, not the array itself
    public double[] getPesoRests() {
        double[] pesoRestsCopy = new double[pesoRests.length];
        System.arraycopy(pesoRests, 0, pesoRestsCopy, 0, pesoRests.length);
        return pesoRestsCopy;
    }
}