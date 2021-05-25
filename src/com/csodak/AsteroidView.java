

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * Az aszteroida grafikus megjelenítéséért felelős.
 * @since 2021.04.26.
 */
public class AsteroidView {
    /**
     * Tartalmazza az asseteket
     */
    public static class AsteroidSkinPack {
        public BufferedImage normal;
        public BufferedImage solarstorm;

        public BufferedImage uranium;
        public BufferedImage iron;
        public BufferedImage ice;
        public BufferedImage coal;

        public BufferedImage hole;
    }

    /**
     * Pack ami tartalmazza az asseteket
     */
    private final AsteroidSkinPack skins;

    /**
     * Aszteroida logikai eleme
     */
    public Asteroid asteroid;

    /**
     * Aszteroida neve
     */
    private final String name;

    /**
     * AsteroidView konstruktor
     * @param asteroid aszteroida logikai része
     * @param name megjelenítendő név
     * @param skins skinpack
     */
    public AsteroidView(Asteroid asteroid, String name, AsteroidSkinPack skins) {
        this.asteroid = asteroid;
        this.name = name;
        this.skins = skins;
    }

    /**
     * Asteroid view rajzolási metódusa
     * @param p pozíció
     * @param g Graphics objektum
     */
    public void Draw(Point2D p, Graphics g) {
        if(asteroid.NextSolarStorm()){
            g.setColor(Color.RED);
            g.fillOval((int)p.getX(), (int) p.getY(), 5, 5);
            g.setColor(Color.BLACK);
        }

        if (asteroid.IsPerihelion()) {
            g.setColor(Color.YELLOW);
            g.fillOval((int)p.getX() -  skins.normal.getWidth() / 2  -10, (int) p.getY() - skins.normal.getHeight() / 2 - 10, skins.normal.getWidth()+20, skins.normal.getHeight()+20);
            g.setColor(Color.BLACK);
        }
        if (asteroid.IsExploded()) {
            g.setColor(Color.RED);
            g.fillOval((int)p.getX() -  skins.normal.getWidth() / 2  -15, (int) p.getY() - skins.normal.getHeight() / 2 - 15, skins.normal.getWidth()+20, skins.normal.getHeight()+30);
            g.setColor(Color.BLACK);
        }
        if (asteroid.IsSolarStorm()) {
            g.drawImage(skins.solarstorm, (int) p.getX() - skins.normal.getWidth() / 2, (int) p.getY() - skins.normal.getHeight() / 2, Controller.getInstance().asteroidFieldContainer);
        }
        else {
            g.drawImage(skins.normal, (int) p.getX() - skins.normal.getWidth() / 2, (int) p.getY() - skins.normal.getHeight() / 2, Controller.getInstance().asteroidFieldContainer);
        }
        if (asteroid.GetCrust() <= 0) {
            g.drawImage(skins.hole.getScaledInstance(32, 32, 1), (int)p.getX() - 16, (int)p.getY() - 16, Controller.getInstance().asteroidFieldContainer);
            Resource core = asteroid.GetCore();
            if (core != null) {
                if (core instanceof Uranium) {
                    g.drawImage(skins.uranium.getScaledInstance(24, 24, 1), (int)p.getX() - 12, (int)p.getY() - 12, Controller.getInstance().asteroidFieldContainer);
                }
                else if (core instanceof Iron) {
                    g.drawImage(skins.iron.getScaledInstance(24, 24, 1), (int)p.getX() - 12, (int)p.getY() - 12, Controller.getInstance().asteroidFieldContainer);
                }
                else if (core instanceof Ice) {
                    g.drawImage(skins.ice.getScaledInstance(24, 24, 1), (int)p.getX() - 12, (int)p.getY() - 12, Controller.getInstance().asteroidFieldContainer);
                }
                else if (core instanceof Coal) {
                    g.drawImage(skins.coal.getScaledInstance(24, 24, 1), (int)p.getX() - 12, (int)p.getY() - 12, Controller.getInstance().asteroidFieldContainer);
                }
            }
        }
        g.setColor(Color.WHITE);
        FontMetrics m = g.getFontMetrics();
        g.drawString(name, (int)p.getX() - m.stringWidth(name) / 2, (int)p.getY() - skins.normal.getHeight()/2 - 20);
        g.setColor(Color.BLACK);
    }
}
