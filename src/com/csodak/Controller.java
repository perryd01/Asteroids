
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Singleton osztály
 * Irányítja a robotokat és az ufokat, pályát és neveket generál, irányítja a játékot.
 * @since 2021.04.26
 */
public class Controller {
    private final HashMap<Asteroid, String> asteroidNames = new HashMap<>();
    private final HashMap<Player, String> playerNames = new HashMap<>();
    private final HashMap<Ufo, String> ufoNames = new HashMap<>();
    private final ArrayList<Ufo> ufos = new ArrayList<>();
    private final ArrayList<Robot> robots = new ArrayList<>();
    AsteroidFieldContainer asteroidFieldContainer;
    ControlPanel controlPanel;

    private static Controller instance = null;

    public void SetAsteroidFieldContainer(AsteroidFieldContainer asteroidFieldContainer) {
        this.asteroidFieldContainer = asteroidFieldContainer;
    }

    public void SetControlPanel(ControlPanel controlPanel) {
        this.controlPanel = controlPanel;
    }

    private Controller() {
    }

    /**
     * Singleton getter
     * @return instance
     */
    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    /**
     * Aszteroida objektum nevét visszaadja
     *
     * @param a Asteroid
     * @return String
     */
    public String GetAsteroidName(Asteroid a) {
        return asteroidNames.get(a);
    }

    /**
     * Player objektum nevét visszaadja
     *
     * @param p Player
     * @return String
     */
    public String GetPlayerName(Player p) {
        return playerNames.get(p);
    }

    /**
     * Ufo objektum nevét visszaadja
     *
     * @param u Ufo
     * @return String
     */
    public String GetUfoName(Ufo u) {
        return ufoNames.get(u);
    }


    /**
     * Irányítja a robotokat, ufokat, a játékot lépteti a következő körbe.
     * AI
     * @since 2021.04.26
     */
    public void Next() {
        Random rand = new Random();
        for(Robot r: robots){
            if (r.GetLocation() != null) {
                if (!r.Drill() || !r.Drill()) {
                    r.Move(r.GetLocation().GetNeighbours().get(rand.nextInt(r.GetLocation().GetNeighbours().size())));
                    r.Drill();
                }
            }
        }

        for (Ufo u: ufos) {
            if (u.GetLocation() != null) {
                if (u.Steal() == null) {
                    u.Move(u.GetLocation().GetNeighbours().get(rand.nextInt(u.GetLocation().GetNeighbours().size())));
                    u.Steal();
                }
            }
        }
        Game.GetInstance().Next();
    }

    /**
     * Új játék létrehozása, a megadott paraméterekkel. ac: Asteroid Count; ad: Asteroid Density (nincs pontos meghatározás lényeg, hogy az összeköttetések száma ezzel arányosan növekedjen); pc: Player Count; uc: Ufo Count
     *
     * @param ac Asteroid Count
     * @param ad Asteroid Density
     * @param pc Player Count
     * @param uc Ufo Count
     * @since 2021.04.26
     */
    public void NewGame(int ac, int ad, int pc, int uc) {
        Game.GetInstance().SetPlayerCount(pc);
        ArrayList<Asteroid> asteroids = new ArrayList<>();
        ArrayList<Effect> perihelions = new ArrayList<>();
        ArrayList<Effect> solarstorms = new ArrayList<>();
        int ring_count = (int) (Math.log(ac + 2) / Math.log(2));
        ArrayList<Resource> corei3 = new ArrayList<>();
        for (int i = 0; i < ac; ++i) {
            corei3.add(i < 12 ? GenResource(i % 4) : GenResource(null));
        }
        Collections.shuffle(corei3);

        Random r = new Random();
        {
            int i = 0;
            rings:
            while (true) {
                for (int j = 0; j < countAsteroidsOnRing(i); j++) {
                    if (asteroids.size() == ac)
                        break rings;
                    Effect peri = new Effect();
                    Effect solar = new Effect();
                    perihelions.add(peri);
                    solarstorms.add(solar);
                    Asteroid a = new Asteroid(peri, solar, corei3.get(asteroids.size()), r.nextInt(5) + 1);
                    asteroids.add(a);

                    if (i != 0) {
                        int parent = offsetOfRing(i - 1) + j / 2;
                        if (i == 1) {
                            parent = 0;
                        }
                        asteroids.get(parent).SetNeighbour(a);
                        a.SetNeighbour(asteroids.get(parent));
                    }
                    if (j != 0 && i != 0) {
                        boolean link = r.nextInt(12) < ad;
                        if (link) {
                            int first = offsetOfRing(i) + j - 1;
                            int second = offsetOfRing(i) + j;
                            asteroids.get(first).SetNeighbour(asteroids.get(second));
                            asteroids.get(second).SetNeighbour(asteroids.get(first));
                        }
                    }
                }
                i++;
            }
        }
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < pc; ++i) {
            Player p = new Player(asteroids.get(0));
            players.add(p);
            asteroids.get(0).AddEntity(p);
            playerNames.put(p, GenName(i));
        }
        for (int i = 0; i < uc; ++i) {
            int locIndex = r.nextInt(asteroids.size() - 1) + 1;
            Ufo u =  new Ufo(asteroids.get(locIndex));
            ufos.add(u);
            asteroids.get(locIndex).AddEntity(u);
            ufoNames.put(u, GenNameU238(i));
        }
        ArrayList<String> hash_Set = new ArrayList<>();
        try {
            Scanner reader = new Scanner(new File("assets/asteroids._"));
            while (reader.hasNextLine()) {

                String data = reader.nextLine();
                hash_Set.add(data);
            }
            reader.close();
        } catch (FileNotFoundException ignored) {
            System.out.println("Asteroid names not found");
            ignored.printStackTrace();
        }
        Collections.shuffle(hash_Set);
        try {
            for (int i = 0; i < ac; ++i) {
                asteroidNames.put(asteroids.get(i), hash_Set.get(i));
            }
        }
        catch (Exception ignored) {}
        Game game = Game.GetInstance();
        for (int i = 0; i < ac; ++i) {
            game.AddAsteroid(asteroids.get(i), perihelions.get(i), solarstorms.get(i));
        }
        for (int i = 0; i< pc; i++){
            game.AddEntity(players.get(i));
        }
        for (int i = 0; i< uc; i++){
            game.AddEntity(ufos.get(i));
        }
        asteroidFieldContainer.Setup(asteroids, players, robots, ufos);
        asteroidFieldContainer.repaint();
        controlPanel.UpdatePlayerList(players);
    }

    /**
     * Az adott sorszamu jatekosnak nevet general
     * @param i
     * @return
     */
    private String GenName(int i){
        String[] names = {"Gagarin", "Elon Musk", "Tihamér", "Laika", "Armstrong",
                "Tasziló", "von Braun", "Victor Glover", "Tom Hanks", "Sirius", "Gajdos Sándor"};
        return names[i];
    }


    /**
     * Az adott sorszamu ufonak nevet general
     * @param i
     * @return
     */
    private String GenNameU238(int i) {
        String[] names = {"Gwanghaegun", "E.T.", "José Bonilla Alvarez Juan Pablo", "Lubbock", "STS-48",
                "Dudley Burrito", "Karen", "Dinzoikgogo", "Ghuudrels", "Uz'eox"};
        return names[i];
    }


    /**
     * Robot nevet general;
     * @param i
     * @return
     */
    public String GenNameR(int i) {
        String[] names = {"MikRobi", "Róbert", "Dögös Robi", "Robb", "R2D2",
                "Robot1", "New_Robot_", "Robot", "Robi", "Robotgép"};
        return names[i];
    }


    /**
     * Kiszamolja, hogy az aszteroidak egy gyurujen (mert gyurukbe vannak rendezve) hany aszteroida talalhato
     * @param n Gyuru sorszama, 0-tol kezdve
     * @return
     */
    public int countAsteroidsOnRing(int n) {
        if (n == 0) {
            return 1;
        }
        return (int) Math.pow(2, n + 1);
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
     * Robot hozzáadása
     * @param robot Robot
     */
    public void AddRobot(Robot robot) {
        robots.add(robot);
    }

    /**
     * Kiszamolja, hogy az adott gyuru elso aszteroidaja hanyadik aszteroida a vilagban
     * @param n num
     * @return offset
     */
    private int offsetOfRing(int n) {
        int offset = 0;
        for (int i = 0; i < n; ++i) {
            offset += countAsteroidsOnRing(i);
        }
        return offset;
    }
}