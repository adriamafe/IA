/**
 * Created by felix on 02.10.16.
 */
import IA.Azamon.Oferta;
import IA.Azamon.Paquete;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;

public class BLSuccessorFunction implements SuccessorFunction{

    public BLSuccessorFunction(){}

    @Override
    /**Gets all Successors of a given state
     *
     * Possible successors of a state are retrieved, by assigning the most important paquete to a oferta
     * For definition of "important", refer to BLPaqComparator.
     *
     * The actions to get to those states are numbers, which represent the oferta the package has been assigned to
     * */
    public List getSuccessors(Object o) {
        //cast the given object to a BLState and get all relevant attributes of it
        BLState state = (BLState)o;
        BLDynamicState dynState = state.getDynamicState();
        Paquete currentPaq = state.getCurrentPaquete();
        //get all ofertas whose arrival days satisfy the priority of the paquete
        Oferta[] ofertas = state.getOfertasByPrio(currentPaq.getPrioridad());

        //initialize variables that we will use
        ArrayList successors = new ArrayList();
        BLState newState;
        int costes;
        int felicidad;
        int paqNum;
        double[] pesoRests;

        if(currentPaq != null && ofertas != null){
            //go through the list of ofertas and search for those that the paquete could be assigned to
            //upon finding a suitable oferta, add the number of the oferta and the resulting state to successors
            for(int i = 0; i<ofertas.length; i++){
                if(currentPaq.getPeso()<=dynState.getPesoRests()[i]){
                    //get dynamic attributes of old state
                    costes = dynState.getCostes();
                    felicidad = dynState.getFelicidad();
                    paqNum = dynState.getPaqNum();
                    pesoRests = dynState.getPesoRests();

                    //alter attributes depending on the oferta to which the package has been assigned
                    paqNum++;
                    costes += BLState.butWhatDoesItCost(currentPaq.getPeso(), ofertas[i].getDias(), ofertas[i].getPrecio());
                    felicidad += BLState.makeMeHappy(currentPaq.getPrioridad(), ofertas[i].getDias());
                    pesoRests[i] -= currentPaq.getPeso();

                    //create new state and add it to the possible successors
                    newState = new BLState(costes, felicidad, paqNum, pesoRests);
                    successors.add(new Successor(""+i, newState));
                }
            }
        }

        return successors;
    }
}