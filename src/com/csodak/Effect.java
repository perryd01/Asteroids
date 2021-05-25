

/**
 * Az Effect osztály felelős az adott Asteroid-on lévő effektekért (napközeli-e, napvihar alatt
 * van-e)
 * @version 1.0
 * @since skeleton
 * @since 2021.03.21
 */
public class Effect {
    /**
     * Az adott effekt hátralévő ideje körökben számolva
     */
    private int duration;

    /**
     * Visszaadja, hogy egy adott effect aktív-e, az adott körre
     * @return boolean
     */
    public boolean IsActive() {
        return duration != 0;
    }

    /**
     * Aktiválja az effektet
     * @param d az effekt időtartama, körökben számolva
     */
    public void Activate(int d) {
        duration = d;
    }

    /**
     * Egy kör elteltekor hívódik meg az effektekre
     * Eggyel csökkenti a durationt, ha az még nem 0.
     */
    public void Next() {
        if(duration!=0) duration--;
    }

    public String toString() {
        return "Effect" + "@" + Integer.toHexString(hashCode());
    }
}
