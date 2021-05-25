

/**
 * Az {@code Uranium} osztály a Resource egy leszármazottja.
 * Lehetséges belőle Portal-t és Robot-ot készíteni, valamint szükséges a játék megnyeréséhez.
 * Amennyiben egy urán magú -már teljesen megfúrt- aszteroida napközelbe kerül felrobban.
 *
 * @version 1.0
 * @since skeleton
 * @since 2021.03.21
 */
public class Uranium extends Resource {

    int timesUnderPerihelion = 0;

    public Uranium() {
        super.name = "uranium";
    }

    /**
     * az Urán csak a 3. expozíció során robban fel
     * csak akkor robban, ha már előtte kétszer napközelben volt
     * növeli az Urán számlálóját, amennyiben napközelben van
     *
     * @return true: ha robban, false: ha nem robban
     */
    @Override
    public PerihelionResponse Perihelion() {
        timesUnderPerihelion++;
        if(timesUnderPerihelion == 3){
            return PerihelionResponse.Explode;
        }
        return PerihelionResponse.None;
    }

    @Override
    public String toString() {
        return "uranium:" + timesUnderPerihelion;
    }
}
