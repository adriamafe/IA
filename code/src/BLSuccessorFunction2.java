
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

public class BLSuccessorFunction2 implements SuccessorFunction{

    public BLSuccessorFunction2(){}

    @Override
    /**Gets all Successors of a given state
     *
     * Possible successors of a state are retrieved, by assigning the most important paquete to a oferta
     * For definition of "important", refer to BLPaqComparator.
     *
     * The actions to get to those states are numbers, which represent the oferta the package has been assigned to
     * */
    //In this case we are using assign and unassign. But is different from SuccessorFunction because a packet that is assigned can 
    //be unassigned or assigned to another offer
    public List getSuccessors(Object o) {
        //cast the given object to a BLState and get all relevant attributes of it
        BLState state = (BLState)o;
        Paquete[] paquetes = BLState.getPaquetes();
        int packRests = state.get_packRests();
        double costes = state.get_costes();
        int felicidad = state.get_felicidad();
        //initialize variables that we will use
        ArrayList successors = new ArrayList();
        Oferta[] ofertas = state.getOfertas();

        //go through the list of ofertas and search for those that the paquete could be assigned to
        //upon finding a suitable oferta, add the number of the oferta and the resulting state to successors
        
        for(int i = 0; i < paquetes.length;++i){
        	for(int j = 0; j < paquetes.length; ++j){
        		//System.out.println("comprovant");
        		
        		if(i!=j && state.CanISwap(i, j)){
        			int[] assignment = state.getAssignment();
            		double[] pesoRests = state.get_pesoRests();
            		BLState S =  new BLState(assignment, costes, felicidad, pesoRests,packRests);
        			//System.out.println("afegint");
            		S.Swap(i, j);
        			successors.add(new Successor("swap"+i+j,S));
        		}

        	
        	}
        }

        return successors;
    }
}