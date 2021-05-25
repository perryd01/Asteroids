

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Tárolja a nézeteket (aszteroidák és különféle entitások) és ezeknek az elhelyezését kontrollálja. Egyben egy ‘vászon’ is amire a rajzolás történhet.
 */
public class AsteroidFieldContainer extends JPanel {
    private final ArrayList<Point2D> asteroidPosition = new ArrayList<>();

    private final ArrayList<AsteroidView> asteroids = new ArrayList<>();
    private final ArrayList<PlayerView> players = new ArrayList<>();
    private final ArrayList<RobotView> robots = new ArrayList<>();
    private final ArrayList<UfoView> ufos = new ArrayList<>();

    /**
     * Melyik játékosra fókuszálunk jelenleg
     */
    private int focused = 0;

    private Asteroid sourceAsteroid = null;
    private Asteroid targetAsteroid = null;

    private BufferedImage portalImage;

    /**
     * Vizuálisan megjeleníti, melyik aszteroida összeköttetés van kijelölve
     * @param source kezdeti aszteroida
     * @param target cél aszteroida
     */
    public void SetActiveConnection(Asteroid source, Asteroid target) {
        sourceAsteroid = source;
        targetAsteroid = target;
    }

    /**
     * p aszteroidájára állítja a fókuszt
     * @param p fókuszáltplayer
     */
    public void SetFocus(Player p) {
        for (int i = 0; i < players.size(); ++i) {
            if (players.get(i).player == p) {
                focused = i;
                return;
            }
        }
    }

    /**
     * Visszaadja az aszteroida helyzetét.
     * @param a adott Aszeroida
     * @return pozíció
     */
    private Point2D getPosition(Asteroid a) {
        for (int i = 0; i < asteroids.size(); ++i) {
            if (asteroids.get(i).asteroid == a) {
                return asteroidPosition.get(i);
            }
        }
        return null;
    }

    /**
     * Visszaadja a fókuszált játékos asszteroidáját
     * @return asteroid
     */
    private Asteroid GetFocusedPlayerLocation() {
        Asteroid asteroid = players.get(focused).player.GetLocation();
        if (asteroid != null)
            return asteroid;
        for (int i = 0; i < players.size(); ++i) {
            if ((asteroid = players.get(i).player.GetLocation()) != null) {
                focused = i;
                return asteroid;
            }
        }
        return asteroids.get(0).asteroid;
    }

    /**
     * Overload clippinggel
     * @param a aszteroida
     * @return aszteroida helye
     */
    private Point2D calcAsteroidPosition(Asteroid a) {
        return calcAsteroidPosition(a, true);
    }

    /**
     *Aszteroida pozíció kiszámolása clipping beállításával
     * @param a aszeroida
     * @param clip k
     * @return aszteroida helye
     */
    private Point2D calcAsteroidPosition(Asteroid a, boolean clip) {
        if (a == null) {
            return null;
        }
        Point2D offset = getPosition(GetFocusedPlayerLocation());
        offset = new Point2D.Double(-offset.getX() + (1280.0 / 2.0),-offset.getY() + (720.0 / 2.0));
        Point2D p = getPosition(a);
        p = new Point2D.Double(p.getX() + offset.getX(), p.getY() + offset.getY());
        if (clip && (p.getX() < -50 || p.getY() < -50 || p.getX() > 1280 + 50 || p.getY() > 720 + 50))
            return null;
        return p;
    }

    /**
     *Entity helyének kiszámolása
     * @param center aszteroida közepe
     * @param population aszteroiádn lévő entitások száma
     * @param n a játékos hányadik
     * @return entitás helye
     */
    private Point2D calcEntityLocation(Point2D center, int population, int n){
        double angle = (double)n/(double)population * 2.0f * 3.14f;
        return new Point2D.Double(40 * Math.cos(angle) + center.getX(),40 * Math.sin(angle) + center.getY());
    }

    /**
     * kirajzolás
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g){
        Color bgcolor = new Color(0, 72, 128);
        if (Game.GetInstance().win) {
            bgcolor = Color.GREEN;
        }
        else if (Game.GetInstance().lose) {
            bgcolor = Color.RED;
        }

        g.setColor(bgcolor);
        g.fillRect(0,0,1280,720);
        g.setColor(Color.BLACK);

        if (Game.GetInstance().win || Game.GetInstance().lose) {
            return;
        }

        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(10));
        for (int i = 0; i < asteroids.size(); ++i) {
            Point2D p1 = calcAsteroidPosition(asteroids.get(i).asteroid, false);
            for (int j = i + 1; j < asteroids.size(); ++j) {
                Point2D p2 = calcAsteroidPosition(asteroids.get(j).asteroid, false);
                Point2D p1Top2 = new Point2D.Double((p2.getX() - p1.getX()) / p1.distance(p2), (p2.getY() - p1.getY()) / p1.distance(p2));
                Point2D p1Eltolt = new Point2D.Double(p1.getX() + p1Top2.getX() * 80, p1.getY() + p1Top2.getY() * 80);
                Point2D p2Eltolt = new Point2D.Double(p2.getX() + p1Top2.getX() * -80, p2.getY() + p1Top2.getY() * -80);
                if (asteroids.get(i).asteroid.GetAsteroidLinks().contains(asteroids.get(j).asteroid)) {
                    if ((asteroids.get(i).asteroid == sourceAsteroid && asteroids.get(j).asteroid == targetAsteroid) || (asteroids.get(j).asteroid == sourceAsteroid && asteroids.get(i).asteroid == targetAsteroid)) {
                        g2.setColor(Color.RED);
                    }
                    g2.drawLine((int)p1Eltolt.getX(),(int)p1Eltolt.getY(),(int)p2Eltolt.getX(),(int)p2Eltolt.getY());
                    g2.setColor(Color.BLACK);
                }
                if (asteroids.get(i).asteroid.GetPortalLinks().contains(asteroids.get(j).asteroid)) {
                    if ((asteroids.get(i).asteroid == sourceAsteroid && asteroids.get(j).asteroid == targetAsteroid) || (asteroids.get(j).asteroid == sourceAsteroid && asteroids.get(i).asteroid == targetAsteroid)) {
                        g2.setColor(Color.RED);
                    }
                    else {
                        g2.setColor(Color.PINK);
                    }
                    g2.drawLine((int)p1Eltolt.getX(),(int)p1Eltolt.getY(),(int)p2Eltolt.getX(),(int)p2Eltolt.getY());
                    g2.setColor(Color.BLACK);
                    g.drawImage(portalImage.getScaledInstance(24, 24, 1), (int)p1Eltolt.getX() - 12, (int)p1Eltolt.getY() - 12, this);
                    g.drawImage(portalImage.getScaledInstance(24, 24, 1), (int)p2Eltolt.getX() - 12, (int)p2Eltolt.getY() - 12, this);
                }
            }
        }
        g2.setStroke(new BasicStroke(1));
        HashMap<Asteroid, Integer> population = new HashMap<>();
        for (AsteroidView a: asteroids) {
            population.put(a.asteroid, 0);
            Point2D p = calcAsteroidPosition(a.asteroid);
            if (p != null){
                a.Draw(p, g);
            }
        }
        for (PlayerView pl: players) {
            Point2D p = calcAsteroidPosition(pl.player.GetLocation());
            if (p == null)
                continue;
            p = calcEntityLocation(p, pl.player.GetLocation().getPopulation(), population.get(pl.player.GetLocation()));
            population.replace(pl.player.GetLocation(), population.get(pl.player.GetLocation()) + 1); // population[pl.player.GetLocation()]++;
            Color color = Color.WHITE;
            if (pl == players.get(focused)) {
                color = Color.RED;
            }
            pl.Draw(p, g, color);
        }
        for (RobotView r: robots) {
            Point2D p = calcAsteroidPosition(r.robot.GetLocation());
            if (p == null)
                continue;
            p = calcEntityLocation(p, r.robot.GetLocation().getPopulation(), population.get(r.robot.GetLocation()));
            population.replace(r.robot.GetLocation(), population.get(r.robot.GetLocation()) + 1); // population[pl.player.GetLocation()]++;
            r.Draw(p, g);

        }
        for (UfoView u: ufos) {
            Point2D p = calcAsteroidPosition(u.ufo.GetLocation());
            if (p == null)
                continue;
            p = calcEntityLocation(p, u.ufo.GetLocation().getPopulation(), population.get(u.ufo.GetLocation()));
            population.replace(u.ufo.GetLocation(), population.get(u.ufo.GetLocation()) + 1); // population[pl.player.GetLocation()]++;
            u.Draw(p, g);
        }
    }

    /**
     * Random aszteroida kinézet generálás
     * @return skinpack
     */
    private AsteroidView.AsteroidSkinPack getRandomAsteroidSkin() {
        String[] normal = {"asteroid0.png","asteroid1.png","asteroid2.png"};
        String[] exploded = {"asteroid_red0.png","asteroid_red1.png","asteroid_red2.png"};
        Random r = new Random();
        int type = r.nextInt(3);
        AsteroidView.AsteroidSkinPack skins = new AsteroidView.AsteroidSkinPack();
        try {
            skins.normal = ImageIO.read(new File("assets/" + normal[type]));
            skins.solarstorm = ImageIO.read(new File("assets/" + exploded[type]));
            skins.uranium = ImageIO.read(new File("assets/uranium.png"));
            skins.iron = ImageIO.read(new File("assets/iron.png"));
            skins.ice = ImageIO.read(new File("assets/ice.png"));
            skins.coal = ImageIO.read(new File("assets/coal.png"));
            skins.hole = ImageIO.read(new File("assets/hole.png"));
        }
        catch (IOException hupsz) {
            hupsz.printStackTrace();
            System.exit(1);
        }
        return skins;
    }

    /**
     * Random player skin nevének kiválasztása
     * @return random player skin név
     */
    private String getRandomPlayerSkin(){
        String[] skins = {"player0.png","player1.png","player2.png"};
        Random r = new Random();
        return skins[r.nextInt(3)];
    }

    /**
     * Random Ufo skin
     * @return skin neve
     */
    private String getRandomUfoSkin(){
        String[] skins = {"ufo0.png","ufo1.png","ufo2.png"};
        Random r = new Random();
        return skins[r.nextInt(3)];
    }

    /**
     * Random robot skin
     * @return skin neve
     */
    private String getRandomRobotSkin(){
        String[] skins = {"robot0.png","robot1.png","robot2.png"};
        Random r = new Random();
        return skins[r.nextInt(3)];
    }

    /**
     * Robot hozááadása a játékhoz
     * @param robot robot
     */
    public void AddRobot(Robot robot){
        Random r = new Random();
        try {
            this.robots.add(new RobotView(robot, Controller.getInstance().GenNameR(r.nextInt(10)), ImageIO.read(new File("assets/" + getRandomRobotSkin()))));
        } catch (IOException ignored) {
        }
    }

    /**
     * Inicializálás
     * @param asteroids aszteroidaöv
     * @param players játékos lista
     * @param robots robotok lista
     * @param ufos ufo lista
     */
    public void Setup(ArrayList<Asteroid> asteroids, ArrayList<Player> players, ArrayList<Robot> robots, ArrayList<Ufo> ufos) {
        try {
            portalImage = ImageIO.read(new File("assets/portal.png"));
        }
        catch (IOException ex) {
            System.err.println("Cant load file: portal.png");
            System.exit(1);
        }
        {
            int i = 0;
            double angle = 0.0;
            rings:
            while (true) {
                angle -= Math.PI/(float)Controller.getInstance().countAsteroidsOnRing(i);
                for (int j = 0; j < Controller.getInstance().countAsteroidsOnRing(i); j++) {
                    if (asteroidPosition.size() == asteroids.size())
                        break rings;
                    double radius = 300.0 * (float)i;

                    double posx = radius * Math.cos((float)j / Controller.getInstance().countAsteroidsOnRing(i) * 2.0 * Math.PI + angle) + 1280 / 2;
                    double posy = radius * Math.sin((float)j / Controller.getInstance().countAsteroidsOnRing(i) * 2.0f * Math.PI + angle) + 720 / 2;
                    asteroidPosition.add(new Point2D.Double(posx, posy));
                }
                i++;
            }
        }
        for (Asteroid a: asteroids) {
            this.asteroids.add(new AsteroidView(a, Controller.getInstance().GetAsteroidName(a), getRandomAsteroidSkin()));
        }
        for (Player p: players) {
            try {
                this.players.add(new PlayerView(p, Controller.getInstance().GetPlayerName(p), ImageIO.read(new File("assets/" + getRandomPlayerSkin()))));
            } catch (IOException ignored) {
            }
        }
        for (Ufo u: ufos) {
            try {
                this.ufos.add(new UfoView(u, Controller.getInstance().GetUfoName(u), ImageIO.read(new File("assets/" + getRandomUfoSkin()))));
            } catch (IOException ignored) {
            }
        }
    }
}
