import IA.Azamon.Oferta;

import java.util.Comparator;

/**
 * Created by felix on 02.10.16.
 */

//Used in BLState to order the ofertas by dias ascending, or precio ascending if dias are equal
//Refer to BLState for more information on why we do this
public class BLOfComparator implements Comparator<Oferta>{
    @Override
    public int compare(Oferta o1, Oferta o2) {
        if(o1.getDias()<o2.getDias()) return -1;
        if(o1.getDias()>o2.getDias()) return 1;
        if(o1.getPrecio()<o2.getPrecio()) return -1;
        if(o1.getPrecio()>o2.getPrecio()) return 1;
        return 0;
    }
}
