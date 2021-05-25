

/**
 * Egy aszteroida nevet köt össze egy aszteroidával, betehető a lenyíló listába
 */
public class AsteroidItem {
    private String name;
    public Asteroid asteroid;

    /**
     * Konstruktor
     * @param name Aszteroida megjelenítendő neve
     * @param asteroid Aszteroida logikai része
     */
    public AsteroidItem(String name, Asteroid asteroid) {
        this.name = name;
        this.asteroid = asteroid;
    }

    /**
     * Név stringként kiírása
     * @return Aszteroida neve
     */
    @Override
    public String toString() {
        return name;
    }
}
