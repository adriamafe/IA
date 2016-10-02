import IA.Azamon.Oferta;
import IA.Azamon.Paquete;
import aima.search.framework.HeuristicFunction;

/**
 * Created by felix on 03.10.16.
 */

/*This Heuristic Functions is the one that uses both attributes (costes and felicidad) for it's heuristics.
 * The heuristic is derived from a relaxed problem, in which the maximumPeso restrictions are ignored.
 * Therefore all paquetes are assigned to the best oferta, whose arrivalDate satisfies it's prioridad, whereby "best"
 * refers to the optimal combination of costes and felicidad.
 * */
public class BLHeuristicFunction2 implements HeuristicFunction{

    public BLHeuristicFunction2(){}

    private static final double FELICIDADVALUE = 10.0;

    @Override
    public double getHeuristicValue(Object o) {
        double value = 0;

        //cast given object to BLState and get all relevant attributes
        BLState state = (BLState)o;
        BLDynamicState dynState = state.getDynamicState();
        Paquete[] paquetes = BLState.getPaquetes();
        Oferta[] ofertas = BLState.getOfertas();
        int [] diaIndices = BLState.getDiaIndices();

        //initialize variables
        int maxDays;
        Oferta oferta;
        double precio;
        double minPrecio;

        //approximiate the minimum value of all unassigned paquetes and add it to value
        for(int i = dynState.getPaqNum(); i<paquetes.length; i++){
            minPrecio = calculatePrecio(ofertas[0], paquetes[i]);
            for(int j=1; j<state.getOfertaPartLengthByPrio(paquetes[i].getPrioridad()); j++){
                oferta = ofertas[j];
                precio = calculatePrecio(oferta, paquetes[i]);
                if(precio < minPrecio) minPrecio=precio;
            }
            value+=minPrecio;
        }

        return value;
    }

    private double calculatePrecio(Oferta oferta, Paquete paquete){
        double precio = BLState.butWhatDoesItCost(paquete.getPeso(), oferta.getDias(), oferta.getPrecio());
        precio -= BLState.makeMeHappy(paquete.getPrioridad(), oferta.getDias())*FELICIDADVALUE;
        return precio;
    }
}