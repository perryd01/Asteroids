
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

/**
 * Ez az osztály felelős a Player entitások grafikus megjelenítéséért
 * @since 2021.04.26.
 */
public class PlayerView {

    public Player player;

    private String name;

    private BufferedImage skin;

    /**
     * PlayerView Konstruktor
     * @param player player logikai része
     * @param name megjelenítendő név
     * @param skin textúra
     */
    public PlayerView(Player player, String name, BufferedImage skin) {
        this.player = player;
        this.name = name;
        this.skin = skin;
    }

    /**
     * kirajzolja a playert
     * @param p pozíció
     * @param g Graphics objektum
     * @param color szín
     */
    public void Draw(Point2D p, Graphics g, Color color) {
        g.drawImage(skin, (int)p.getX() - skin.getWidth() / 2, (int)p.getY() - skin.getHeight() / 2, Controller.getInstance().asteroidFieldContainer);
        g.drawRect((int)p.getX() - skin.getWidth()/2+15,(int)p.getY() - skin.getHeight()/2+5, skin.getWidth()/2, 8);
        switch (player.energy){
            case 5: g.setColor(Color.GREEN); break;
            case 4: g.setColor(new Color(50,205,50)); break;
            case 3: g.setColor(Color.YELLOW); break;
            case 2: g.setColor(Color.ORANGE); break;
            case 1: g.setColor(Color.RED); break;
        }
        g.fillRect((int)p.getX() - skin.getWidth()/2+16, (int)p.getY() - skin.getHeight()/2+1+5, (int)((skin.getWidth()/2 - 1)*(player.energy/5.0)), 7);
        g.setColor(color);
        FontMetrics m = g.getFontMetrics();
        g.drawString(name, (int)p.getX() - m.stringWidth(name) / 2, (int)p.getY() - skin.getHeight()/2+1);
        g.setColor(Color.BLACK);
    }
}
