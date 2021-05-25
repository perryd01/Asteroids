

/**
 * Az {@code Ice} objektum a játék leírásában szereplő vízjég nyersanyag megvalósítása. Aszteroidák
 * belsejében található és szükséges a játékban való győzelemhez. Amennyiben egy -vízjeget
 * tartalmazó- aszteroida napközelben van és egy Robot vagy Player eltávolítja az utolsó
 * nyersanyagot fedő réteget is, a vízjég szublimál
 * @version 1.0
 * @since skeleton
 * @since 2021.03.21
 */
public class Ice extends Resource {

    public Ice(){
        super.name = "ice";
    }


    /**
     * Napközelben a teljesen megfúrt aszteroidában levő vízjég szublimál (eltűnik).
     * @return
     */
    public PerihelionResponse Perihelion() {
        return PerihelionResponse.Destroy;
    }
}
