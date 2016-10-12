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
        Paquete[] paquetes = BLState.getPaquetes();

        //initialize variables that we will use
        ArrayList successors = new ArrayList();
        Oferta[] ofertas;
        int [] assignment = state.getAssignment();

        //go through the list of ofertas and search for those that the paquete could be assigned to
        //upon finding a suitable oferta, add the number of the oferta and the resulting state to successors
        for(int i = 0; i < paquetes.length; i++) {
            if(assignment[i]<0){
                ofertas = BLState.getOfertasByPrio(paquetes[i].getPrioridad());
                for (int j = 0; j < ofertas.length; j++) {
                    if(state.assignable(i,j)){
                        successors.add(new Successor("a"+i+"."+j, state.assign(i,j)));
                    }
                }
            }
            else{
                successors.add(new Successor("u" + i, state.unassign(i)));
            }
        }

        return successors;
    }
}