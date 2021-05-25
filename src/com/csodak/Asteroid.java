

import java.util.ArrayList;
import java.util.Random;

/**
 * A játékmezőket reprezentáló osztály, ezekre tudnak lépni a Player-ek és a Robot-ok.
 * Egyidejűleg több Player és Robot is lehet rajta. Tartalmazhat nyersanyagot (egyfajtát).
 * Ha teljesülnek a feltételek (napközeli, uránt tartalmaz), felrobban és ennek megfelelően reagálnak a rajta lévő Robotok és Playerek.
 *
 * @version 2.0
 * @since skeleton
 * @since 2021.03.21
 */
public class Asteroid {
    /**
     * Aszteroida kérgének vastagsága
     * Kezdőmérete: 1-5
     */
    private int crust;
    /**
     * Szomszédos Aszteroidák
     */
    private final ArrayList<Asteroid> neighbours = new ArrayList<>();
    /**
     * Portálok általi kapcsolatok
     */
    private final ArrayList<Portal> portals = new ArrayList<>();
    /**
     * Aszteroidához tartozó napközelés effect
     */
    private Effect perihelion;
    /**
     * Aszteroidához tartozó napvihar effect
     */
    private Effect solarStorm;
    /**
     * Aszteroida magja
     */
    private Resource core;
    /**
     * Aszteroidán tartózkodó Entity-k listája
     */
    private final ArrayList<Entity> entities = new ArrayList<>();

    private boolean lastExploded;
    private boolean lastPerihelion;
    private boolean lastSolarStorm;

    public boolean IsExploded() {
        return lastExploded;
    }

    public boolean IsPerihelion() {
        return lastPerihelion;
    }

    public boolean IsSolarStorm() {
        return lastSolarStorm;
    }

    public boolean NextSolarStorm() {
        return solarStorm.IsActive();
    }

    /**
     * @param perihelion Az aszteroidához tartozó napközelség effekt
     * @param solarStorm Az aszteroidához tartozó napvihar effekt
     * @param core       Az aszteroida magja
     */
    public Asteroid(Effect perihelion, Effect solarStorm, Resource core) {
        this.perihelion = perihelion;
        this.solarStorm = solarStorm;
        this.core = core;
        Random r = new Random();
        this.crust = r.nextInt(5) + 1;
    }

    /**
     * @param perihelion Aszteroidához tartozó napközelség effekt
     * @param solarStorm Aszteroidához tartozó napvihar effekt
     * @param core Az aszeroida magja, Eóegy resource vagy null
     * @param crust Azsteroida köpenymérete, nemnegatív egész
     */
    public Asteroid(Effect perihelion, Effect solarStorm, Resource core, int crust) {
        if (crust < 0) throw new IllegalArgumentException("size smaller than 0");
        this.perihelion = perihelion;
        this.solarStorm = solarStorm;
        this.core = core;
        this.crust = crust;
    }


    /**
     * Entitás hozzáadása az aszteroidához
     *
     * @param entity Hozzáadandó Entity
     */
    public void AddEntity(Entity entity) {
        entities.add(entity);
        ArrayList<Resource> acc = new ArrayList<>();
        for (Entity e : entities) {
            acc.addAll(e.ViewResources());
        }
        Blueprint winBlueprint = new Blueprint(Uranium.class, Uranium.class, Uranium.class, Ice.class, Ice.class, Ice.class, Iron.class, Iron.class, Iron.class, Coal.class, Coal.class, Coal.class);
        if (winBlueprint.Apply(acc)) {
            Game.GetInstance().Win();
        }
    }

    public ArrayList<Entity> GetEntities(){
        return entities;
    }

    /**
     * Entitás eltávolítása az aszteroidáról
     *
     * @param entity Eltávolítandó Entity
     */
    public void RemoveEntity(Entity entity) {
        entities.remove(entity);
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }


    /**
     * Következő kör indítása
     * Ha napvihar éri, akkor a portálok elmozdulhatnak,
     * a meghalandó entitások meghalnak
     * Ha perihelion éri, akkor a magra hatással van, ha nincs crust
     */
    public void Next() {
        lastExploded = false;
        lastPerihelion = perihelion.IsActive();
        lastSolarStorm = solarStorm.IsActive();
        if (solarStorm.IsActive() && !(crust == 0 && core == null)) {
            entities.removeIf(Entity::ApplySolarStorm);
        }
        if (solarStorm.IsActive()) {
            try {
                Random random = new Random();
                portals.removeIf(p -> p.Move(neighbours.get(random.nextInt(neighbours.size()))));
            } catch (Exception ignored) { }
        }
        if (perihelion.IsActive() && crust == 0 && core != null) {
            switch (core.Perihelion()) {
                case Destroy:
                    SetCore(null);
                    break;
                case Explode:
                    for (Portal p: portals) {
                        p.Explode();
                    }
                    portals.clear();
                    for (Entity e : entities) {
                        e.ApplyExplosion();
                    }
                    entities.clear();
                    SetCore(null);
                    lastExploded = true;
                    break;
            }
        }
    }

    /**
     * Aszteroida szomszédai azok az aszteroidák amikre közvetlen kapcsolat vagy portál
     * miatt át lehet menni
     *
     * @return ArrayList benne a szomszédokkal és portálok általi szomszédokkal
     */
    public ArrayList<Asteroid> GetNeighbours() {
        ArrayList<Asteroid> ret = new ArrayList<>(neighbours);
        for (Portal p : portals) {
            Asteroid other = p.OtherSide();
            if (other != null) {
                ret.add(other);
            }
        }
        return ret;
    }

    public ArrayList<Asteroid> GetPortalLinks() {
        ArrayList<Asteroid> ret = new ArrayList<>();
        for (Portal p : portals) {
            Asteroid other = p.OtherSide();
            if (other != null) {
                ret.add(other);
            }
        }
        return ret;
    }

    public ArrayList<Asteroid> GetAsteroidLinks() {
        return neighbours;
    }

    /**
     * Mag kibányászása
     * Nyerési feltételek ellenőrzése
     *
     * @return Kibányászott mag
     */
    public Resource Mine() {
        if (core == null || crust > 0) {
            return null;
        }

        ArrayList<Resource> acc = new ArrayList<>();
        for (Entity e : entities) {
            acc.addAll(e.ViewResources());
        }
        acc.add(core);
        Blueprint winBlueprint = new Blueprint(Uranium.class, Uranium.class, Uranium.class, Ice.class, Ice.class, Ice.class, Iron.class, Iron.class, Iron.class, Coal.class, Coal.class, Coal.class);
        if (winBlueprint.Apply(acc)) {
            Game.GetInstance().Win();
        }

        Resource temp = core;
        core = null;
        return temp;
    }

    /**
     * Aszteroida megfúrása
     *
     * @return fúrás sikeressége, boolean
     */
    public boolean Drill() {
        if (crust == 0) {
            return false;
        }
        if (--crust == 0) {
            if (core != null && perihelion.IsActive() && core.Perihelion() == Resource.PerihelionResponse.Destroy) {
                SetCore(null);
            }
        }
        return true;
    }

    /**
     * Aszteroida magjának beállítása
     * A korábbi mag elveszik
     *
     * @param resource Mag
     * @return Igaz ha a mag beállítása sikeres (üres volt vagy most lett üres), hamis minden mas esetben
     */
    public boolean SetCore(Resource resource) {
        if (resource == null || core == null) {
            core = resource;
            return true;
        }
        return false;
    }

    /**
     * Nyersanyag visszahelyezése az asszteroidába
     * @param resource nyersanyag
     * @return true, ha sikeres; false, ha nem
     */
    public boolean PlaceBackCore(Resource resource) {
        if (crust == 0) {
            return SetCore(resource);
        }
        return false;
    }

    /**
     * Aszteroida egyik szomszédjának hozzáadása
     *
     * @param asteroid hozzáadandó szomszédos aszteroida
     */
    public void SetNeighbour(Asteroid asteroid) {
        neighbours.add(asteroid);
    }

    /**
     * Portál lehelyezése az aszteroidára
     *
     * @param asteroid aszteroida
     */
    public void SetPortal(Portal asteroid) {
        portals.add(asteroid);
    }

    /**
     * Aszteroida köpenyének beállítása
     * @param size méret, nemnegatív egésn
     */
    public void SetCrust(int size) {
        if (size < 0) throw new IllegalArgumentException("size smaller than 0");
        this.crust = size;
    }

    /**
     * Aszteroida státuszának kiírása
     *
     * Format:
     * id = 0
     * neighbours = {  }
     * portals = {  }
     * crust = 1
     * resource = null
     * perihelion = 0
     * solarstorm = 0
     * players = {  }
     * robots = {  }
     * ufos = {  }
     */
    public void Status() {

        System.out.println("id = " + Command.GetInstance().asteroids.indexOf(this));
        System.out.print("neighbours = { ");
        for (int i = 0; i < neighbours.size(); i++) {
            System.out.print(Command.GetInstance().asteroids.indexOf(neighbours.get(i)));
            if (i < neighbours.size() - 1)
                System.out.print(", ");
        }
        System.out.println(" }");

        System.out.print("portals = { ");
        for (int i = 0; i < portals.size(); i++) {
            System.out.print(Command.GetInstance().asteroids.indexOf(portals.get(i).OtherSide()));
            if (i < portals.size() - 1)
                System.out.print(", ");
        }
        System.out.println(" }");

        System.out.println("crust = " + crust);
        System.out.println("resource = " + core);
        System.out.println("perihelion = " + (perihelion.IsActive() ? 1 : 0));
        System.out.println("solarstorm = " + (solarStorm.IsActive() ? 1 : 0));

        System.out.print("players = { ");
        StringBuilder s = new StringBuilder();
        for (Entity e : entities) {
            if (e instanceof Player) {
                if (s.length() > 0) {
                    s.append(", ");
                }
                s.append(Command.GetInstance().players.indexOf(e));
            }
        }
        System.out.println(s + " }");

        System.out.print("robots = { ");
        s.setLength(0);
        for (Entity e : entities) {
            if (e instanceof Robot) {
                if (s.length() > 0) {
                    s.append(", ");
                }
                s.append(Command.GetInstance().robots.indexOf(e));
            }
        }
        System.out.println(s + " }");

        System.out.print("ufos = { ");
        s.setLength(0);
        for (Entity e : entities) {
            if (e instanceof Ufo) {
                if (s.length() > 0) {
                    s.append(", ");
                }
                s.append(Command.GetInstance().ufos.indexOf(e));
            }
        }
        System.out.println(s + " }");
    }

    /**
     * Portál eltávolítása az aszteroidáról
     * @param portal Eltávolítandó portál
     */
    public void RemovePortal(Portal portal) {
        portals.remove(portal);
    }

    public int getPopulation(){
        return entities.size();
    }

    public int GetCrust() {
        return crust;
    }

    public Resource GetCore() {
        return core;
    }

    @Override
    public String toString() {
        return "Asteroid" + "@" + Integer.toHexString(hashCode());
    }
}
