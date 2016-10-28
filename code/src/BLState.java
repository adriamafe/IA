import IA.Azamon.Oferta;
import IA.Azamon.Paquete;
import IA.Azamon.Paquetes;
import IA.Azamon.Transporte;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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
    
    private int [] assignment;
    private double costes;
    private int felicidad;
    //remaining space in the offers
    private double[] pesoRests;
    //remaining packets to be assigned
    private int packRests;


    /**This constructor is only called one time (when the initial state is created).
     * It sorts the given Paquetes and Transporte, converts them into arrays and stores them as static attributes.
     * In addition, it calculates the third static attribute, diaIndices, and initializes the dynamic attributes
     * */
    
    
    /////////CONSTRUCTORS///////////////////////
    
    public BLState(Paquetes paq, Transporte trans, int initial_solution){
    	///// Shouldnt be always sorted /////
    	
        //Sort the given Paquetes, convert it to an array and store it as static attribute
        //BLPaqComparator paqComparator = new BLPaqComparator();
       // paq.sort(paqComparator);
       // paquetes = paq.toArray(new Paquete[paq.size()]);
    	paquetes = paq.toArray(new Paquete[paq.size()]);
        //Sort the given Transporte, convert it to an array and store it as static attribute
        //BLOfComparator ofComparator = new BLOfComparator();
        ///trans.sort(ofComparator);
        ofertas = trans.toArray(new Oferta[trans.size()]);
        //initialize assignment as completely unassigned, i.e. [-1,...,-1]
        assignment = new int [paquetes.length];
        for (int i = 0; i<assignment.length; i++){
            assignment[i]=-1;
        }

        //initialize costes and felicidad with 0
        costes = 0;
        felicidad = 0;
        
        //initialize packRests with the number of packets that we have
        packRests = paquetes.length;

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
        initial_solution(initial_solution);
    }

    /**Create a new state with the given dynamic attributes
     * This Constructor is the one that is called for creating successors and therefore only sets dynamic attributes.
     * */
    public BLState(int [] assignment, double costes, int felicidad, double[] pesoRests, int packRests){
        this.assignment = assignment;
        this.costes = costes;
        this.felicidad = felicidad;
        this.pesoRests = pesoRests;
        this.packRests = packRests;
    }
    
    /////////////GETTERS/////////////////////////

    public static Paquete[] getPaquetes() {
        return paquetes;
    }

    public static Oferta[] getOfertas() {
        return ofertas;
    }

    public static int[] getDiaIndices() {
        return diaIndices;
    }
    
    public double get_costes(){
    	return costes;
    }
    
    public int get_felicidad(){
    	return felicidad;
    }
    public double[] get_pesoRests(){
    	double[] copy = new double[pesoRests.length];
    	System.arraycopy(pesoRests, 0, copy, 0, pesoRests.length);
    	return copy;
    }
    public int get_packRests(){
    	return packRests;
    }
	//returns a copy of assignment, not the array itself
    public int[] getAssignment() {
        int[] assignmentCopy = new int[assignment.length];
        System.arraycopy(assignment, 0, assignmentCopy, 0, assignment.length);
        return assignmentCopy;
    }

    //A state is final, if every paquete has been assigned to a oferta, i.e. when assignment[i]>=0 for all i
    public boolean isFinal(){
        if(packRests != 0) return false;
        return true;
        
    }
    
    /////////////CHECKING FUNCTIONS //////////////
    
    public boolean CanISwap(int paq1, int paq2){
    	if(assignment[paq1] != -1 && assignment[paq2] != -1) return true;
    	Oferta of1 = ofertas[assignment[paq1]];
    	Oferta of2 = ofertas[assignment[paq2]];
    	Paquete p1 = paquetes[paq1];
    	Paquete p2 = paquetes[paq2];
    	boolean x1,x2;
    	if(pesoRests[assignment[paq1]]-p1.getPeso() >= p2.getPeso() && maxDiaOfPrio(p1.getPrioridad())>=of2.getDias()) x1 = true;
    	else x1 = false;
    	if(pesoRests[assignment[paq2]]-p2.getPeso() >= p1.getPeso() && maxDiaOfPrio(p2.getPrioridad())>=of1.getDias()) x2 = true;
    	else x2 = false;
    	if(x1 && x2) return true;
    	return false;
    }
    public boolean assignable(int paq, int of){
        return (paquetes[paq].getPeso()<=pesoRests[of]
                && BLState.maxDiaOfPrio(paquetes[paq].getPrioridad())>=ofertas[of].getDias());
    }
    
    public boolean unassignable(int paq){
    	if(assignment[paq] == -1) return false;
    	return true;
    }
    
    
    //////////////OPERADORES///////////////
    // new assign function. Assigns a paquet to an offer and updates felicidad and costes.
    public void assign(int paq, int of){
    	Paquete p = paquetes[paq];
    	Oferta o = ofertas[of];
    	if(assignment[paq] >=0){
    		Paquete p1 = paquetes[paq];
        	Oferta o1 = ofertas[assignment[paq]];
        	felicidad -= makeMeHappy(p1.getPrioridad(), o1.getDias());
        	//System.err.println("resto: "+butWhatDoesItCost(p.getPeso(),o.getDias(),o.getPrecio()));
        	costes = costes - butWhatDoesItCost(p1.getPeso(), o1.getDias(), o1.getPrecio());
        	pesoRests[assignment[paq]] += p1.getPeso();
        	//System.err.println("sumo paquet" + paq);
        	++packRests;
    	}
    	//System.err.println("sumo: "+butWhatDoesItCost(p.getPeso(),o.getDias(),o.getPrecio()));
    	costes = costes + butWhatDoesItCost(p.getPeso(),o.getDias(),o.getPrecio());
    	felicidad = felicidad + makeMeHappy(p.getPrioridad(),o.getDias());
    	pesoRests[of] = pesoRests[of] - p.getPeso();
    	assignment[paq] = of;
    	//System.err.println("resto paquet" + paq);
    	--packRests;
    	if(costes < 0) costes = 0;
    }
  /* OLD ASSIGN FUNCTION
   * 
    public BLState assign(int paq, int of){
        int [] assignment_new = new int[assignment.length];
        System.arraycopy(assignment, 0, assignment_new, 0, assignment.length);
        assignment_new[paq]=of;

        double costes_new = costes + BLState.butWhatDoesItCost(paquetes[paq].getPeso(), ofertas[of].getDias(), ofertas[of].getPrecio());
        int felicidad_new = felicidad + BLState.makeMeHappy(paquetes[paq].getPrioridad(), ofertas[of].getDias());

        double[] pesoRests_new = new double[pesoRests.length];
        System.arraycopy(pesoRests, 0, pesoRests_new, 0, pesoRests.length);
        pesoRests_new[of]-=paquetes[paq].getPeso();

        return new BLState(assignment_new, costes_new, felicidad_new, pesoRests_new);
    }
    */

    public void unassign(int paq){
    	//first update the values
    	Paquete p = paquetes[paq];
    	Oferta o = ofertas[assignment[paq]];
    	felicidad -= makeMeHappy(p.getPrioridad(), o.getDias());
    	//System.err.println("resto: "+butWhatDoesItCost(p.getPeso(),o.getDias(),o.getPrecio()));
    	costes = costes - butWhatDoesItCost(p.getPeso(), o.getDias(), o.getPrecio());
    	pesoRests[assignment[paq]] += p.getPeso();
    	//deassign
    	assignment[paq] = -1;
    	//System.err.println("sumo paquet" + paq);
    	++packRests;
    	if(costes <0) costes = 0;
    }
    
    /* OLD UNASSIGN FUNCTION
    public BLState unassign(int paq){
        int of=assignment[paq];

        int [] assignment_new = new int[assignment.length];
        System.arraycopy(assignment, 0, assignment_new, 0, assignment.length);
        assignment_new[paq]=-1;

        double costes_new = costes - BLState.butWhatDoesItCost(paquetes[paq].getPeso(), ofertas[of].getDias(), ofertas[of].getPrecio());
        int felicidad_new = felicidad - BLState.makeMeHappy(paquetes[paq].getPrioridad(), ofertas[of].getDias());

        double[] pesoRests_new = new double[pesoRests.length];
        System.arraycopy(pesoRests, 0, pesoRests_new, 0, pesoRests.length);
        pesoRests_new[of]+=paquetes[paq].getPeso();

        return new BLState(assignment_new, costes_new, felicidad_new, pesoRests_new);
    }
*/
    
    /*
    //returns all ofertas whose arrival dates satisfy the given prioridad
    public static Oferta[] getOfertasByPrio(int prio){
        int ofertaPartLength = getOfertaPartLengthByPrio(prio);
        Oferta[] ofertasPart = new Oferta[ofertaPartLength];
        System.arraycopy(ofertas,0,ofertasPart,0,ofertaPartLength);
        return ofertasPart;
    }

    //Gets the number of ofertas whose arrival dates satisfy the given prioridad
    public static int getOfertaPartLengthByPrio(int prio){
        switch(prio){
            case 0: return diaIndices[0];
            case 1: return diaIndices[2];

        }
        return diaIndices[4];
    }

*/
    public void Swap(int p1, int p2){
    	int of1 = assignment[p1];
    	int of2 = assignment[p2];
    	unassign(p1);
    	unassign(p2);
    	assign(p1,of2);
    	assign(p2,of1);
    }
  //  
    //////////AUX FUNCTIONS/////////////////
    
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
    //Initial solution
    void initial_solution(int num){
    	switch(num){
    	case 1:
    		//In this case we make an aleatory assignment
    		Initial_solution1();
    		break;
    	case 2:
    		//packets are sorted by priority and offers by days, both in ascending order
    		//then we assign the packets
    		Initial_solution2();
    		break;
    	case 3:
    		//Offers are sorted by prio and price
    		Initial_solution3();
    		break;
    	case 4:
    		//Packets and offers are ordered by priority and days respectively and 
    		//in case they are equal they are ordered by MaxWeight and Weight.
    		Initial_solution4();
    		break;
    	}
    		
    }
    //The first solution consists in just goes over all paquetes and search for an assignable offer
    void Initial_solution1(){
    	List<Integer> L = new ArrayList<Integer>();
    	for(int i = 0; i < paquetes.length; ++i){
    		L.add(i);
    	}
    	List<Integer> L1 = new ArrayList<Integer>();
    	for(int i = 0; i < ofertas.length; ++i){
    		L1.add(i);
    	}
    	while(packRests > 0){
    	for(int i = 0; i <L.size(); ++i){
    		int p = L.get(i);
    		boolean found = false;
    		int j = 0;
    		while(!found && j < ofertas.length){
    			int o = L1.get(j);
    			if(assignable(p,o)){
    				found = true;
    				assign(i,j);
    			}
    			++j;
    		}
    	}
    }
    }
    void Initial_solution2(){
    	//sort by days
    	Arrays.sort(paquetes,new BLPaqComparator());	
    	Arrays.sort(ofertas, new BLOfComparator());
    	for(int i = 0; i <paquetes.length; ++i){
    		boolean found = false;
    		int j = 0;
    		while(!found && j<ofertas.length){
    			if(assignable(i,j)){
    				found = true;
    				assign(i,j);
    			}
    			++j;
    		}
    	}
    }
    
    void Initial_solution3(){
    	//sort by price
    	Arrays.sort(ofertas,new BLComparatorOfPrioandPrice());
    	Arrays.sort(paquetes,new BLPaqComparator());
    	for(int i = 0; i <paquetes.length; ++i){
    		boolean found = false;
    		int j = 0;
    		while(!found && j < ofertas.length){
    			if(assignable(i,j)){
    				found = true;
    				assign(i,j);
    			}
    			++j;
    		}
    	}
    }
    
    void Initial_solution4(){
    	//sort by Weight and Pesomaximo
    	Arrays.sort(ofertas, new BLComparatorOfPrioandWeight());
    	Arrays.sort(paquetes, new BLComparatorPaqPrioandWeight());
    	print_ofertas();
    	print_paquetes();
    	for(int i = 0; i <paquetes.length; ++i){
    		boolean found = false;
    		int j = 0;
    		while(!found && j < ofertas.length){
    			if(assignable(i,j)){
    				found = true;
    				assign(i,j);
    			}
    			++j;
    		}
    	}
    }
    
    void print_assignment(){
    	for(int i = 0; i < ofertas.length; ++i){
    		System.out.println(ofertas[i].toString());
    		for(int j = 0; j < paquetes.length;++j){
    			if(assignment[j] == i) System.out.println(paquetes[j].toString());
    		}
    	}    	
    }
  
    void print_ofertas(){
    	for(int i = 0; i < ofertas.length;++i){
    		System.out.println(ofertas[i]);
    	}
    }
    
    void print_paquetes(){
    	for(int i = 0; i < paquetes.length; ++i){
    		System.out.println(paquetes[i]);
    	}
    }
    
    //testing stuff. TODO: 03.10.16 DELETE AFTER PROJECT IS FINISHED
    
    /*
    public static void main(String[] args){
        Paquetes paq = new Paquetes(100, 1234);
        Transporte trans = new Transporte(paq, 1.2D, 1234);
        BLState state = new BLState(paq, trans);
        Oferta[] ofertasPrio = new Oferta[trans.size()];
        ofertasPrio = trans.toArray(new Oferta[trans.size()]);
        Iterator p1 = trans.iterator();
        while(p1.hasNext()) {
            Oferta o = (Oferta)p1.next();
            System.out.println(o.toString());
            
        }
        Iterator p2 = paq.iterator();
        while(p2.hasNext()){
        	Paquete p = (Paquete) p2.next();
        	System.out.println(p.toString());
        }
    }
    */
    
}
