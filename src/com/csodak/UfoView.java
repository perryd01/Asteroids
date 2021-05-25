

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * Ez az osztály felelős az Ufo entitások grafikus megjelenítéséért
 * @since 2021.04.26.
 */
public class UfoView {
    public Ufo ufo;
    private String name;
    private BufferedImage skin;

    /**
     * UfoView konstruktor
     * @param ufo ufo logikai része
     * @param name kiírandó név
     * @param skin textúra
     */
    public UfoView(Ufo ufo, String name, BufferedImage skin) {
        this.ufo = ufo;
        this.name = name;
        this.skin = skin;
    }

    /**
     * Kirajzolja az ufot
     * @param p pozíció
     * @param g Graphics objektum
     */
    public void Draw(Point2D p, Graphics g) {
        g.drawImage(skin.getScaledInstance(30,30,1), (int)p.getX() - 15, (int)p.getY() - 15, Controller.getInstance().asteroidFieldContainer);
        g.setColor(Color.WHITE);
        FontMetrics m = g.getFontMetrics();
        g.drawString(name, (int)p.getX() - m.stringWidth(name)/2, (int)p.getY() - 15 -5);
        g.setColor(Color.BLACK);
    }
}
