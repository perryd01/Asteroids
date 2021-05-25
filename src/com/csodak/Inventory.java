
import java.util.ArrayList;

/**
 * A {@code Player} nyersanyag tárolásáért felelős osztály. Képes eltárolni Portal-okat és Resource-okat.
 *
 * @version 1.0
 * @since skeleton
 * @since 2021.03.21
 */
public class Inventory {

    /**
     * maximum portálszám a tárolóban (nem pár, hanem  darab)
     */
    private static final int maxPortals = 3;
    /**
     * Inventory telíthetősége
     */
    private int maxSlots = 10;
    /**
     * Tárolt portálpár
     */
    private java.util.ArrayList<Portal> portals = new ArrayList<>();
    /**
     * Tárolt nyersanyagok
     */
    private java.util.ArrayList<Resource> resources = new ArrayList<>();

    /**
     * Eltárol egy portált, ha ez helyhiány miatt sikertelen akkor visszaadja
     *
     * @param portal tárolandó portál
     * @return Portál, ha sikertelen az eltárolás, null,ha sikeres
     */
    public Portal Store(Portal portal) {
        if (FreePortalSlots() < 2 || FreeSlots() < 2) {
            return portal;
        } else {
            portals.add(portal);
            return null;
        }
    }

    /**
     * Eltárol egy erőforrást, ha ez helyhiány miatt sikertelen akkor visszaadja.
     *
     * @param resource tárolandó nyersanyag
     * @return Nyersanyag, ha sikertelen az eltárolás
     */
    public Resource Store(Resource resource) {
        if (maxSlots - resources.size() >= 1) { //review this
            resources.add(resource);
            return null;
        } else {
            return resource;
        }
    }

    /**
     * Kiszámítja hány szabad hely van még
     *
     * @return szabad slotok száma
     */
    public int FreeSlots() {
        return maxSlots - resources.size() - portals.size();
    }

    /**
     * Kiveszi az inventoryból a kívánt Resourcet, amennyiben az nem szerepel az inventoryban null a visszatérési értéke.
     *
     * @param resource kivenni való resource
     */
    public Resource Remove(Resource resource) {
        int idx = resources.indexOf(resource);
        if (idx == -1) {
            return null;
        }
        return resources.remove(idx);
    }


    /**
     * Kiveszi az inventoryból a kívánt Portált, amennyiben az nem szerepel az inventoryban null a visszatérési értéke.
     *
     * @param portal kivenni való portal
     */
    public boolean Remove(Portal portal) {
        boolean ret = portals.remove(portal);
        return ret;
    }

    /**
     * Tárolt nyersanyagok megtekintése
     *
     * @return nyersanyag Arraylist
     */
    public ArrayList<Resource> ViewResources() {
        return resources;
    }

    /**
     * Tárolt portálok megtekintése
     *
     * @return portál Arraylist
     */
    public ArrayList<Portal> ViewPortals() {
        return portals;
    }

    /**
     * Szabad portálhelyek
     *
     * @return int, szabad portál helyek
     */
    public int FreePortalSlots() {
        return maxPortals - portals.size();
    }

    /**
     *  Törli az inventory tartalmát, a portálok miatt fontos
     */
    public void Explode() {
        for (Portal p : portals) {
            p.Explode();
        }
    }

    @Override
    public String toString() {
        return "Inventory" + "@" + Integer.toHexString(hashCode());
    }


}
