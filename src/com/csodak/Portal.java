
import java.util.Optional;

/**
 * Nyersanyagokért cserébe tudja egy Player elkészíteni (2-esével). Összekapcsol 2 aszteroidát és így -akár nem szomszédos- aszteroidák között is lehet közlekedni.
 * @version 1.0
 * @since skeleton
 * @since 2021.03.21
 */
public class Portal {
    private Asteroid location;
    private Portal other;
    private int lastMoved;

    private Portal() { }

    public static Portal[] CreatePair() {
        Portal p1 = new Portal();
        Portal p2 = new Portal();
        p1.other = p2;
        p2.other = p1;
        Portal ret[] =  new Portal[]{p1, p2};
        return ret;
    }

    /**
     * Az azsteroidára helyezi a portált ha a portál párja nem ugyanott van
     * @param asteroid Aszteroida
     * @return sikeres-e a linkelés
     */
    public boolean Link(Asteroid asteroid) {
        if (other.location == asteroid) {
            return false;
        }
        location = asteroid;
        asteroid.SetPortal(this);
        return true;
    }


    /**
     * @return A portál túloldala
     */
    public Asteroid OtherSide() {
        return other.location;
    }

    /**
     * Átmegy egy másik aszteroidához, ha a portál párja nem ott van és ebben a körben még nem mozgott
     * @param asteroid A cél aszteroida
     * @return sikeres-e mozgás
     */
    public boolean Move(Asteroid asteroid) {
        if (lastMoved == Game.GetInstance().GetRoundCount()) {
            return false;
        }
        if (other.location == asteroid) {
            return false;
        }
        location = asteroid;
        asteroid.SetPortal(this);
        lastMoved = Game.GetInstance().GetRoundCount();
        return true;
    }

    /**
     *  A par elso fele ami robban
     *  Jelzi a tuloldalnak, hogy robbanas tortent
     *  Es a sajat location-jet beallitja null-ra (innentol kezdve nem lehet ide utazni)
     *  Ha player hivja akkor annak el kell tavolitania a hatizsakbol
     *  Ha aszteroida hivja akkor az aszteroida tavolitja el magatol
     */
    public void Explode() {
        other.explode();
        location = null;
    }

    /**
     * A par masodik fele ami robban
     * Ha aszteroidan van akkor arrol el tavolitodik
     * Ha a jatekos hatizsakjaban van akkor nem tortenik vele semmi viszont lerakas utan hasznalhatatlan lesz
     */
    private void explode() {
        if (location != null) {
            location.RemovePortal(this);
        }
        location = null;
    }

    @Override
    public String toString() {
        return "portal";
    }

}
