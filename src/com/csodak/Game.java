


import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 * A játékmenetért felelős osztály. Tartalmazza az összes Asteroid-ot, Effect-et és
 * Entity-t(Robot és Player abszrakt ősosztálya). A játékot generálja, valamint véget is vet neki,
 * annak függvényében, hogy a játékosok győztek vagy vesztettek. Valamint kezeli, hogy mi
 * történik, ha meghal egy játékos.
 *
 * @version 1.0
 * @since skeleton
 * @since 2021.03.21
 */
public class Game {


    public boolean win = false;
    public boolean lose = false;

    /**
     * A singleton egyetlen példánya
     */
    private static final Game instance = new Game();
    /**
     * A játékban lévő effektek listája
     */
    public final ArrayList<Effect> perihelions = new ArrayList<>();
    public final ArrayList<Effect> solarStorms = new ArrayList<>();
    /**
     * A játékban lévő aszteroidák listája
     */
    public java.util.ArrayList<Asteroid> asteroids = new ArrayList<>();
    /**
     * A játékban lévő entitások listája (robotok és telepesek)
     */
    public ArrayList<Entity> entities = new ArrayList<>();
    /**
     * A napvihar véletlen bekövetkezésének esélye
     */
    public double randomSolarStormChance = 0.5;
    /**
     * A napközelség véletlen bekövetkezésének esélye
     */
    public double randomPerihelionChance = 0.2;
    /**
     * A játékban lévő playerek (telepesek) száma
     * Maximum 20 telepes lehet a pályán, akár egy aszteroidán is lehet mindenki.
     */
    private int playerCount;
    /**
     * Körszámláló
     */
    private int roundCount;

    /**
     * A singletonnak privát konstruktorra van szüksége
     * SAFE_MODE=1 kornyezeti valtozo beallitasa letiltja a halalos esemenyeket
     */
    private Game() {
        Map<String, String> env = System.getenv();
        String safe_mode = env.get("SAFE_MODE");
        if (safe_mode != null && safe_mode.equals("1")) {
            System.out.println("Safe mode on");
            randomSolarStormChance = 0.0;
            randomPerihelionChance = 0.0;
        }
    }

    /**
     * @return Game
     */
    public static Game GetInstance() {
        return instance;
    }

    /**
     * Létrehozza a játékteret, mennyi aszteroida és telepes legyen a játék kezdetén
     *
     * @param asteroidCount Aszteroidák száma
     * @param playerCount   Játékosok száma
     */
    @Deprecated
    public void Generate(int asteroidCount, int playerCount) {
        this.playerCount = playerCount;
        for (int i = 0; i < asteroidCount; i++) {
            Effect peri = new Effect();
            Effect solar = new Effect();
            perihelions.add(peri);
            solarStorms.add(solar);
            Asteroid a = new Asteroid(peri, solar, GenResource(null));
            asteroids.add(a);
            //to redo with random linking
            if (i > 0) {
                a.SetNeighbour(asteroids.get(i - 1));
                asteroids.get(i).SetNeighbour(a);
            }
        }
        for (int i = 0; i < playerCount; i++) {
            Player p = new Player(asteroids.get(0));
            entities.add(p);
            asteroids.get(0).AddEntity(p);
        }
    }

    /**
     * @param n A generálandó nyersanyag sorszáma, ha null akkor véletlenszerű
     * @return A generált nyersanyag, null ha hibás a megadott sorszám
     */
    private Resource GenResource(Integer n) {
        ArrayList<Class<? extends Resource>> resourceTypes = new ArrayList<>();
        resourceTypes.add(Uranium.class);
        resourceTypes.add(Ice.class);
        resourceTypes.add(Iron.class);
        resourceTypes.add(Coal.class);
        try {
            if (n != null) {
                return resourceTypes.get(n).getConstructor().newInstance();
            }
            Random r = new Random();
            return resourceTypes.get(r.nextInt(resourceTypes.size())).getConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * A győzelemkor hívódik meg, ha megvannak a megfelelő nyersanyagok
     */
    public void Win() {
        System.out.println("You are victorious!");
        win = true;
    }

    /**
     * Ha egy játékos felrobban, akkor hívódik meg
     * Csökkenti a játékos számlálót
     */
    public void PlayerDied() {
        playerCount--;
        if (playerCount < 2) End();
    }

    /**
     * A játék végén hívódik meg
     *
     * @return true, ha vége a játéknak, a játékban lévő telepesek száma túl kevés a győzelemhez
     */
    public boolean End() {
        System.out.println("Defeat, not enough players left.");
        lose = true;
        return true;
    }

    /**
     * Napközelség esetén hívódik meg
     * random időközönként történik
     * nem egyszerre mindenhol történik
     */
    public void SolarStorm() {
        int i = 1;
        for (Effect e : solarStorms) {
            Random r = new Random();
            int p = r.nextInt(10);
            if ((i % 10) == p) {
                e.Activate(r.nextInt(3) + 1);
                i++;
            }
        }
    }

    /**
     * Az összes aszteroidn beállítja a napvihart
     *
     * @param enabled be, vagy kikapcsolás
     */
    public void SolarStormAll(boolean enabled) {
        for (Effect e : solarStorms) {
            e.Activate(enabled ? 1 : 0);
        }
    }

    /**
     * Napvihar esetén hívódik meg
     * random időközönként történik, 1 körig tart
     */
    public void Perihelion() {
        for (Effect e : perihelions) {
            e.Activate(1);
        }
    }

    /**
     * Minden aszteroidára beállítja a napközelséget
     * @param enabled engedélyezés
     * @param duration időtartam
     */
    public void PerihelionAll(boolean enabled, int duration) {
        for (Effect e : perihelions) {
            e.Activate(enabled ? duration : 0);
        }
    }

    /**
     * @return körszámláló
     */
    public int GetRoundCount() {
        return roundCount;
    }

    /**
     * Annak az esélyét állítja be, hogy új kör indulásakor napvihar legyen
     * @param chance esély [0,1]
     */
    public void SetSolarStormPossibility(double chance) {
        if (chance < 0.0 || chance > 1.0)
            throw new IllegalArgumentException();
        randomSolarStormChance = chance;
    }

    /**
     * Annak az esélyét állítja be, hogy új kör indulásakor napközelség legyen
     * @param chance esély [0,1]
     */
    public void SetPerihelionPossibility(double chance) {
        if (chance < 0.0 || chance > 1.0)
            throw new IllegalArgumentException();
        randomPerihelionChance = chance;
    }

    /**
     * Hozzáad egy aszteroidát a játékhoz
     * @param asteroid Aszeteroida
     * @param perihelion napközelség effektje
     * @param solarstorm napvihar effektje
     */
    public void AddAsteroid(Asteroid asteroid, Effect perihelion, Effect solarstorm) {
        asteroids.add(asteroid);
        perihelions.add(perihelion);
        solarStorms.add(solarstorm);
    }

    /**
     * Hozzáad egy entitást a játékhoz
     * @param entity entitás
     */
    public void AddEntity(Entity entity) {
        entities.add(entity);
    }

    /**
     * Új kör a játékban:
     * - Napközelség véletlenszerű aktiválása
     * - Napvihar véletlenszerű aktiválása
     * - Új kör az entitásoknak
     * - Új kör az aszteroidáknak
     * - Effektek léptetése (lejárás)
     *
     */
    public void Next() {
        roundCount++;
        Random r = new Random();
        for (Asteroid a : asteroids) {
            a.Next();
        }
        for (Entity e : entities) {
            e.Next();
        }
        for (Effect e : perihelions) {
            e.Next();
        }
        for (Effect e : solarStorms) {
            e.Next();
        }
        if (r.nextDouble() <= randomPerihelionChance) {
            Perihelion();
        }
        if (r.nextDouble() <= randomSolarStormChance) {
            SolarStorm();
        }
    }

    public void SetPlayerCount(int n) {
        playerCount = n;
    }

    @Override
    public String toString() {
        return "Game" + "@" + Integer.toHexString(hashCode());
    }
}
