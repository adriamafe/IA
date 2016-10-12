/**
 * Created by felix on 02.10.16.
 */

//BLDynamicState is a wrapper object for the dynamic attributes of a BLState
public class BLDynamicState {
    private int costes;
    private int felicidad;
    private int [] assignment;
    //remaining space in the offers
    private double[] pesoRests;

    public BLDynamicState(int [] assignment, int costes, int felicidad, double[] pesoRests) {
        this.assignment = assignment;
        this.costes = costes;
        this.felicidad = felicidad;
        this.pesoRests = pesoRests;
    }

    //returns a copy of assignment, not the array itself
    public int[] getAssignment() {
        int[] assignmentCopy = new int[assignment.length];
        System.arraycopy(assignment, 0, assignmentCopy, 0, assignment.length);
        return assignmentCopy;
    }


    public int getCostes() {
        return costes;
    }

    public int getFelicidad() {
        return felicidad;
    }

    //returns a copy of pesoRests, not the array itself
    public double[] getPesoRests() {
        double[] pesoRestsCopy = new double[pesoRests.length];
        System.arraycopy(pesoRests, 0, pesoRestsCopy, 0, pesoRests.length);
        return pesoRestsCopy;
    }
}