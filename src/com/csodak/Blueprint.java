

import java.util.ArrayList;
import java.lang.reflect.InvocationTargetException;

/**
 * {@code Blueprint} osztály, amely megvalósítja a portál, illetve Robot craftoláshoz és
 * a nyerési feltételek ellenőrzéséhez elengedhetetlen építési sablonokat.
 * @version 1.0
 * @since skeleton
 * @since 2021.03.21
 */
public class Blueprint {
    private final ArrayList<Resource> required = new ArrayList<>();
    public final ArrayList<Resource> used = new ArrayList<>();

    /**
     * A Blueprint konstruktora Class objektumokat vár majd ezeket példányosítva lehelyezi őket egy tömbben
     * -- ex: Új Blueprint létrehozása
     * <code>
     *     new Blueprint(Uranium.class, Iron.class, Iron.class)
     * </code>
     * @param rs Az alkalmazashoz szükséges nyersanyagok listája
     */
    public Blueprint(Class<? extends Resource> ... rs) {
        for (Class<? extends Resource> rt : rs) {
            try {
                required.add(rt.getConstructor().newInstance());
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Végig halad a megkapott inventoryn és minden elemet megpróbál eltávolítani a required tömbből, ha
     * az elávolítás sikeres akkor beteszi azt a used tömmbe.
     * A felhasznált nyersanyagok listáját, a used listában tárolja
     * ezt a hívó felelőssége eltávolítani a saját nyersanyag lsitájából
     * @param resources A rendelkezésre álló nyersanyagok listája
     * @return {@code required.IsEmpty()} sepcifikusabban igaz ha van elég nyersanyag a blueprint alkalmazáshához, hamis ha nincs
     */
    public boolean Apply(ArrayList<Resource> resources) {
        for (Resource available: resources) {
            if (required.contains(available)) { // Equals
                used.add(available);
                required.remove(available);
            }
        }
        return required.isEmpty();
    }

    @Override
    public String toString() {
        return "Blueprint" + "@" + Integer.toHexString(hashCode());
    }
}
