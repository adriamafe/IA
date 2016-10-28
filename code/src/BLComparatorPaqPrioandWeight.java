import java.util.Comparator;

import IA.Azamon.Paquete;

public class BLComparatorPaqPrioandWeight implements Comparator<Paquete>{
    @Override
    public int compare(Paquete p1, Paquete p2) {
        if(p1.getPrioridad()<p2.getPrioridad()) return -1;
        if(p1.getPrioridad()>p2.getPrioridad()) return 1;
        if(p1.getPeso()>p2.getPeso()) return -1;
        if(p1.getPeso()<p2.getPeso()) return 1;
        return 0;
    }
}
