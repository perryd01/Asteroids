

import java.util.ArrayList;

/**
 * Örökli az ősosztály felelősségeit. Továbbá képes kilopni az aszteroida magját,
 * teleportkapukat használni. Korlátlan nyersanyagot ki tud lopni.
 *
 * @version 1.0
 * @since proto
 * @since 2021.03.27
 */
public class Ufo extends Entity {
    /**
     * Ufo konstruktor
     *
     * @param place
     */
    public Ufo(Asteroid place) {
        super(place);
    }


    /**
     * Visszaállítja a maximum enrgiát
     */
    @Override
    public void Next() {
        super.energy = Entity.maxEnergy;
    }

    /**
     * Az Ufo kilopja az aszteroida magját
     *
     * @return true: ha kiszedte, false: ha nem sikerült
     */
    public Resource Steal() {
        if (super.energy < Entity.miningCost) {
            return null;
        }
        Resource r = super.location.Mine();
        if (r != null) {
            //inventory.Store(r);
            super.energy -= Entity.miningCost;
            return r;
        }
        return null;
    }

    /**
     * A Drill nem csinál semmit.
     *
     * @return false
     */
    @Override
    public boolean Drill() {
        return false;
    }

    /**
     * Az Ufokra nem hat a SolarStorm
     */
    @Override
    public boolean ApplySolarStorm() {
        return false;
    }

    /**
     * Az Ufokra nem hat a Robbanás
     */
    @Override
    public void ApplyExplosion() {
        ArrayList<Asteroid> neighbours = super.location.GetNeighbours();
        location = neighbours.get(0);
        location.AddEntity(this);
    }

    /**
     * Kiprinteli a státuszt.
     */
    public void Status() {
        System.out.println("id = " + Command.GetInstance().ufos.indexOf(this) + "\n"
                + "location = " + Command.GetInstance().asteroids.indexOf(super.location) + "\n"
                + "energy = " + super.energy);
    }

    @Override
    public String toString() {
        return "Ufo" + "@" + Integer.toHexString(hashCode());
    }

}
