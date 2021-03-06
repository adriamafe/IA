
import IA.Azamon.Oferta;
import IA.Azamon.Paquete;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import IA.Azamon.Oferta;
import IA.Azamon.Paquete;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;

public class BLSuccessorFunction1 implements SuccessorFunction{

    public BLSuccessorFunction1(){}

    @Override
    /**Gets all Successors of a given state
     *
     * Possible successors of a state are retrieved, by assigning the most important paquete to a oferta
     * For definition of "important", refer to BLPaqComparator.
     *
     * The actions to get to those states are numbers, which represent the oferta the package has been assigned to
     * */
    //In this case we are using assign and unassign. We only assign packets which are not assigned and deassign packets already assigned. 
    //this means that to assign a packet that is already assigned to another offer we will need to steps (two levels in the tree).
    public List getSuccessors(Object o) {
        //cast the given object to a BLState and get all relevant attributes of it
        BLState state = (BLState)o;
        Paquete[] paquetes = BLState.getPaquetes();
        double costes = state.get_costes();
        int felicidad = state.get_felicidad();
        int packRests = state.get_packRests();

        //initialize variables that we will use
        ArrayList successors = new ArrayList();
        Oferta[] ofertas = state.getOfertas();

        //go through the list of ofertas and search for those that the paquete could be assigned to
        //upon finding a suitable oferta, add the number of the oferta and the resulting state to successors
        for(int i= 0; i < paquetes.length; ++i){
        	for(int j = 0; j < ofertas.length; ++j){
        		if(state.assignable(i,j)){
        			int [] assignment = state.getAssignment();
        			double[] pesoRests = state.get_pesoRests();
        			BLState S = new BLState(assignment, costes, felicidad, pesoRests,packRests);
        			S.assign(i,j);
        			successors.add(new Successor("a"+i+j,S));
        		}
        	}
        }

        return successors;
    }
}
