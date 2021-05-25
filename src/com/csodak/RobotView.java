

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 *Ez az osztály felelős a Robot entitások grafikus megjelenítéséért
 * @since 2021.04.26.
 */
public class RobotView {

    /**
     * Robot logikai része
     */
    public Robot robot;
    /**
     * Robot név
     */
    private String name;
    /**
     * textúra
     */
    private BufferedImage skin;

    /**
     * RobotView konstruktor
     * @param robot robot logikai része
     * @param name megjelenítendő név
     * @param skin textúra
     */
    public RobotView(Robot robot, String name, BufferedImage skin) {
        this.robot = robot;
        this.name = name;
        this.skin = skin;
    }

    /**
     * Kirajzolja a robotot
     * @param p pozíció
     * @param g Graphics objektum
     */
    public void Draw(Point2D p, Graphics g) {
        g.drawImage(skin, (int)p.getX() - skin.getWidth() / 2, (int)p.getY() - skin.getHeight() / 2, Controller.getInstance().asteroidFieldContainer);
        g.setColor(Color.WHITE);
        FontMetrics m = g.getFontMetrics();
        g.drawString(name, (int)p.getX() - m.stringWidth(name) / 2, (int)p.getY() - skin.getHeight()/2-5);
        g.setColor(Color.BLACK);
    }
}
