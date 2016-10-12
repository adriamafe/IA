import IA.Azamon.Oferta;
import IA.Azamon.Paquete;
import aima.search.framework.HeuristicFunction;

/**
 * Created by felix on 03.10.16.
 */

/*This Heuristic Function is the one that only uses one attribute (costes) for it's heuristics.
 * The heuristic is derived from a relaxed problem, in which the maximumPeso restrictions are ignored.
 * Therefore all paquetes are assigned to the cheapest oferta, whose arrivalDate satisfies it's prioridad.
 * */
public class BLHeuristicFunction1 implements HeuristicFunction{

    public BLHeuristicFunction1(){}

    @Override
    public double getHeuristicValue(Object o) {
        double value = 0;

        //cast given object to BLState and get all relevant attributes
        BLState state = (BLState)o;
        int [] assignment = state.getAssignment();
        Paquete[] paquetes = BLState.getPaquetes();
        Oferta[] ofertas = BLState.getOfertas();
        int [] diaIndices = BLState.getDiaIndices();

        //initialize variables
        int maxDays;
        Oferta oferta;
        double precio;
        double minPrecio;

        //approximiate the minimum cost of all unassigned paquetes and add it to value
        for(int i = 0; i<paquetes.length; i++){
            if(assignment[i]<0){
                minPrecio = BLState.butWhatDoesItCost(paquetes[i].getPeso(), ofertas[0].getDias(), ofertas[0].getPrecio());
                maxDays=BLState.maxDiaOfPrio(paquetes[i].getPrioridad());
                for(int j=0; j<maxDays-1; j++){
                    oferta = ofertas[diaIndices[j]];
                    precio = BLState.butWhatDoesItCost(paquetes[i].getPeso(), oferta.getDias(), oferta.getPrecio());
                    if(precio < minPrecio) minPrecio=precio;
                }
                value+=minPrecio;
            }
        }

        return value;
    }
}