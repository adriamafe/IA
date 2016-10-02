import IA.Azamon.Oferta;
import IA.Azamon.Paquete;
import IA.Azamon.Paquetes;
import IA.Azamon.Transporte;

import java.util.Iterator;

/**
 * Created by felix on 02.10.16.
 */

/**BLState is the Class that represents the states of our problem.
 * It contains two types of attributes: static and dynamic ones.
 * The static ones are the same for all states of one specific problem and they are only set once in the Constructor
 * that creates the initial state.
 * The remaining attributes are dynamic ones. In order to export them, BLDynamicState is used as a wrapper object.
 * */
public class BLState {
    /**Paquetes and ofertas are represented by arrays which are sorted by priority/dias ascending.
     * If those attributes are equal, paquetes are sorted by peso descending and oferteas by precio ascennding
     *
     * The idea behind priority/dia sorting is that we want to assign paquetes with high priority (i.e. 0>1>2) first.
     * By doing so, we can make sure that our searches are complete, without having to check remaining
     * priorities/arrivaldates for every state and we also prune bad states before even creating them.
     *
     * Weight sorting is used, because paquetes with high weight are more important, since they can be assigned to less
     * ofertas, while also contributing more to the total delivery/storage costs.
     *
     * Price sorting is used, because spending less money -> having more money -> ??? -> profit
     * */
    private static Paquete[] paquetes;
    private static Oferta[] ofertas;
    //indexing on delivery dates of ofertas, i.e. diaIndices[x]=number of ofertas with oferta.dias<=x+1
    private static int[] diaIndices;
    
    private int costes;
    private int felicidad;
    //number of allready assigned packages (=index of next package)
    private int paqNum;
    //remaining space in the offers
    private double[] pesoRests;

    /**This constructor is only called one time (when the initial state is created).
     * It sorts the given Paquetes and Transporte, converts them into arrays and stores them as static attributes.
     * In addition, it calculates the third static attribute, diaIndices, and initializes the dynamic attributes
     * */
    public BLState(Paquetes paq, Transporte trans){
        //Sort the given Paquetes, convert it to an array and store it as static attribute
        BLPaqComparator paqComparator = new BLPaqComparator();
        paq.sort(paqComparator);
        paquetes = paq.toArray(new Paquete[paq.size()]);

        //Sort the given Transporte, convert it to an array and store it as static attribute
        BLOfComparator ofComparator = new BLOfComparator();
        trans.sort(ofComparator);
        ofertas = trans.toArray(new Oferta[trans.size()]);

        //initialize costes, felicidad and paqNum with 0
        costes = 0;
        felicidad = 0;
        paqNum = 0;

        //initialize pesoRests with maxPeso of ofertas and calculate diaIndices
        pesoRests = new double[ofertas.length];
        diaIndices = new int [5];
        int j = 0;
        for(int i=0; i<pesoRests.length; i++){
            pesoRests[i] = ofertas[i].getPesomax();
            while(j<ofertas[i].getDias()-1){
                diaIndices[j++] = i;
            }
        }
        diaIndices[4]=ofertas.length;
    }

    /**Create a new state with the given costes, fellicidad, paqNum and pesoRests.
     * This Constructor is the one that is called for creating successors and therefore only sets dynamic attributes.
     * */
    public BLState(int costes, int felicidad, int paqNum, double[] pesoRests){
        this.costes = costes;
        this.felicidad = felicidad;
        this.paqNum = paqNum;
        this.pesoRests = pesoRests;
    }

    public static Paquete[] getPaquetes() {
        return paquetes;
    }

    public static Oferta[] getOfertas() {
        return ofertas;
    }

    public static int[] getDiaIndices() {
        return diaIndices;
    }

    //Exports the dynamic attributes via the wrapper object BLDynamicState
    public BLDynamicState getDynamicState(){
        return new BLDynamicState(costes, felicidad, paqNum, pesoRests);
    }

    //A state is final, if every paquete has been assigned to a oferta, i.e. when paqNum==#paquetes
    public boolean isFinal(){
        return paqNum == paquetes.length;
    }

    //returns the paquete that has to be assigned next
    public Paquete getCurrentPaquete(){
        return paqNum<paquetes.length?paquetes[paqNum]:null;
    }

    //returns all ofertas whose arrival dates satisfy the given prioridad
    public Oferta[] getOfertasByPrio(int prio){
        int ofertaPartLength = getOfertaPartLengthByPrio(prio);

        Oferta[] ofertasPart = new Oferta[ofertaPartLength];
        System.arraycopy(ofertas,0,ofertasPart,0,ofertaPartLength);
        return ofertasPart;
    }

    //Gets the number of ofertas whose arrival dates satisfy the given prioridad
    public int getOfertaPartLengthByPrio(int prio){
        switch(prio){
            case 0: return diaIndices[0];
            case 1: return diaIndices[2];

        }
        return diaIndices[4];
    }

    //returns the amount of felicidad gained by the arrival of a package with given prioridad after the given dias
    public static int makeMeHappy(int prioridad, int dias){
        int minDias;
        switch(prioridad){
            case 0: minDias = 1;
                break;
            case 1: minDias = 2;
                break;
            case 2: minDias = 4;
                break;
            default: minDias = 1000;
        }

        if(dias<minDias) return minDias-dias;

        return 0;
    }

    //calculates the cost of sending a package with given peso via an oferta with given dias and precio
    public static double butWhatDoesItCost(double peso, int dias, double precio){
        int almaDias = 0;
        if(dias>2){
            almaDias++;
            if(dias>4){
                almaDias++;
            }
        }

        return peso*precio+0.25*almaDias*peso;
    }

    public static int maxDiaOfPrio(int prio){
        switch(prio){
            case 0: return 1;
            case 1: return 3;
            default: return 5;
        }
    }

    //testing stuff. TODO: 03.10.16 DELETE AFTER PROJECT IS FINISHED
    public static void main(String[] args){
        Paquetes paq = new Paquetes(100, 1234);
        Transporte trans = new Transporte(paq, 1.2D, 1234);
        BLState state = new BLState(paq, trans);
        Oferta[] ofertasPrio = state.getOfertasByPrio(2);

        Iterator p1 = trans.iterator();
        while(p1.hasNext()) {
            Oferta o = (Oferta)p1.next();
            System.out.println(o.toString());
        }
        System.out.println("\n");
        for(int i=0; i<ofertasPrio.length; i++){
            System.out.println(ofertasPrio[i].toString());
        }
    }
}