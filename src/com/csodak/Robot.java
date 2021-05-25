

import java.util.ArrayList;

/**
 * A robotokért felelős osztály, képes mozogni aszteroidák között és csökkenteni azok rétegeit.
 * Ha felrobban másik szomszédos aszteroidára kerül.
 * Ha napvihar éri és nincs egy aszteroida belsejében elpusztul.
 *
 * @version 1.0
 * @since skeleton
 * @since 2021.03.21
 */
public class Robot extends Entity {
    public Robot(Asteroid location) {
        super(location);
    }


    /**
     * Robbanástól átmegy egy másik aszteroidára.
     */
    public void ApplyExplosion() {
        ArrayList<Asteroid> neighbours = super.location.GetNeighbours();
        location = neighbours.get(0);
        location.AddEntity(this);
    }

    /**
     * Egy új kört reprezentál
     * Feltölti az energiáját.
     */
    public void Next() {
        super.energy = Entity.maxEnergy;
    }

    /**
     * Kiprinteli a státuszt.
     *
     * Format:
     * id = 0
     * location = -1 //ha -1 akkor azt jelenti, hogy nincs helye, vagyis nem él
     * energy = 5
     */
    public void Status() {
        System.out.println("id = " + Command.GetInstance().robots.indexOf(this) + "\n"
                + "location = " + Command.GetInstance().asteroids.indexOf(super.location) + "\n"
                + "energy = " + super.energy);
    }

    @Override
    public String toString() {
        return "Robot" + "@" + Integer.toHexString(hashCode());
    }
}
