

/**
 * Nyersanyagokat megvalósító osztály
 *
 * @version 1.0
 * @since skeleton
 * @since 2021.03.21
 */
public abstract class Resource {
    protected String name;

    public enum PerihelionResponse {
        None, Destroy, Explode
    }

    /**
     * Mi történik kibányászáskor, paraméterül kapja, hogy napközelben van e éppen, vagy sem.
     *
     * @param perihelion Napközelség állapot aktív-e
     * @return Lehet-e bányászni
     */
    public boolean Mine(boolean perihelion) {
        return false;
    }

    /**
     * Napközelbe kerüléskor hívódik meg
     *
     * @return boolean
     */
    public PerihelionResponse Perihelion() {
        return PerihelionResponse.None;
    }

    public boolean Explode() {
        return false;
    }

    /**
     * Egyenlőség operátor overrideolása hogy össze lehessen hasonlítani 2 resource objektumot
     *
     * @param other amivel össze akarjuk az adott resourcet
     * @return bool visszatérési érték, annak függvényében hogy megegyezik-e
     */
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        boolean ret = other.getClass() == this.getClass();
        return ret;
    }

    @Override
    public String toString() {
        return name;
    }
}
