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
        //cast given object to BLState and get all relevant attributes
        BLState state = (BLState)o;
        return (state.get_costes());
        
    }
    public boolean equals(Object obj) {
	      boolean retValue;
	      retValue = super.equals(obj);
	      return retValue;
	  }
}
