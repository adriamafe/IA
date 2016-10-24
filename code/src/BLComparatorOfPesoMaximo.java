import java.util.Comparator;

import IA.Azamon.Oferta;

public class BLComparatorOfPesoMaximo implements Comparator<Oferta>{
    @Override
    public int compare(Oferta o1, Oferta o2) {
        if(o1.getPesomax()>o2.getPesomax()) return -1;
        if(o1.getPesomax()<o2.getPesomax()) return 1;
        return 0;
    }
}
