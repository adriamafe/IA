import java.util.Comparator;

import IA.Azamon.Oferta;

public class BLComparatorOfPrice implements Comparator<Oferta>{
    @Override
    public int compare(Oferta o1, Oferta o2) {
        if(o1.getPrecio()<o2.getPrecio()) return -1;
        if(o1.getPrecio()>o2.getPrecio()) return 1;
        return 0;
    }
}

