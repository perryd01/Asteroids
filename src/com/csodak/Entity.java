

import java.util.ArrayList;

/**
 * Az entitásokért (Robot, Player) felelős absztrakt ősosztály. Rendelkezik egy névvel és
 * energiaszinttel. Tud mozogni Asteroid-ok között, megfúrni azokat, tud kereskedni más
 * entitásokkal, megnézni az inverntoryját, valamint lekezeli mi történik velük, ha felrobbannak.
 *
 * @version 1.0
 * @since skeleton
 * @since 2021.03.21
 */
public abstract class Entity {
    protected static final int maxEnergy = 5;
    protected static final int movingCost = 3;
    protected static final int craftingCost = 2;
    protected static final int miningCost = 2;
    protected static final int drillingCost = 2;
    protected static final int placingCost = 1;
    protected static final int tradingCost = 1;


    /**
     * Az entitás neve
     */
    private String name;
    /**
     * Entitás energiája; meghatározza, hogy mennyi cselekvést végezhet még a körben.
     */
    protected int energy;
    /**
     * A jelenlegi aszteroida, amin található az entitás
     */
    protected Asteroid location;

    protected Entity(Asteroid location) {
        this.location = location;
        this.energy = maxEnergy;
    }

    /**
     * @return az aszteroida amin jelenleg van
     */
    public Asteroid GetLocation() {
        return location;
    }

    /**
     * Átmozgatja az adott entitást egy másik aszteroidára
     *
     * @param asteroid Szomszédos aszteroida amelyre mozogni szeretne
     * @return mozgás sikeressége
     */
    public boolean Move(Asteroid asteroid) {
        if (energy < Entity.movingCost) return false;
        energy -= Entity.movingCost;
        location.RemoveEntity(this);
        asteroid.AddEntity(this);
        location = asteroid;
        return true;
    }

    /**
     * Az entitás csökkenti az adott aszteroida kérgének vastagságát
     *
     * @return sikerült-e csökkenteni a kéreg vastagságát
     */
    public boolean Drill() {
        if (energy >= drillingCost && location.Drill()) {
            energy -= drillingCost;
            return true;
        }
        return false;
    }

    /**
     * Meghívja az entitásra mit kell tennie amikor az aszteroidája felrobban
     */
    public void ApplyExplosion() {
    }

    /**
     * A leszármazottaknál lekezeli hogy mi történik ha végzett a körrel (pl. energiaszint feltölt)
     */
    public abstract void Next();

    /**
     * Kilistázza az entitásnál található nyersanyagokat
     *
     * @return nyersanyagtömb
     */
    public ArrayList<Resource> ViewResources() {
        return new ArrayList<>();
    }

    /**
     * Eltávolítja az entitást az aszteroidáról, a napvihar hatására (= meghal)
     */
    public boolean ApplySolarStorm() {
        location = null;
        return true;
    }

    /**
     * Átad egy nyersanyagot
     * Ha a csere sikeres akkor a hívónak el kell távolítania a nyersanyagot a saját listájából
     *
     * @param resource Nyersanyag amit át szeretnénk adni
     * @return nyersanyag csere sikeressége
     */
    public boolean Trade(Resource resource) {
        return false;
    }

    /** Energia beállítása
     *  @param energy nemnegatív egész
     */
    public void SetEnergy(int energy) {
        if (energy < 0)
            throw new IllegalArgumentException();
        this.energy = energy;
    }

    /**
     * Beállítja az entitás helyét egy aszteroidára
     * @param asteroid Beállítandó aszteroida
     */
    public void SetLocation(Asteroid asteroid) {
        if (location == asteroid) return;
        this.location = asteroid;
        asteroid.AddEntity(this);
    }
}
