import IA.Azamon.Paquete;

import java.util.Comparator;

/**
 * Created by felix on 02.10.16.
 */

//Used in BLState to order the paquetes by prioridad ascending, or peso descending if dias are equal
//Refer to BLState for more information on why we do this
public class BLPaqComparator implements Comparator<Paquete>{
    @Override
    public int compare(Paquete p1, Paquete p2) {
        if(p1.getPrioridad()<p2.getPrioridad()) return -1;
        if(p1.getPrioridad()>p2.getPrioridad()) return 1;
        return 0;
    }
}
