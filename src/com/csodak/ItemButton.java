

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Ez felelős a különböző itemek (nyersanyag, vagy protál) megjelenítéséért az inventoryban,
 * Kattintás hatására az az item lesz kiválasztva az inventoryban, amire kattintottunk,
 * a többi inaktívvá válik (működése hasonlít egy radio buttonra).
 * @since 2021.04.26.
 */
public class ItemButton extends JButton {
    private static final Icon ironIcon = new ImageIcon("assets/iron.png");
    private static final Icon iceIcon = new ImageIcon("assets/ice.png");
    private static final Icon coalIcon = new ImageIcon("assets/coal.png");
    private static final Icon uraniumIcon = new ImageIcon("assets/uranium.png");
    private static final Icon portalIcon = new ImageIcon("assets/portal.png");
    private static final Icon emptyIcon = new ImageIcon("assets/empty.png");

    Resource resource = null;
    Portal portal = null;

    public boolean lastClicked;

    /**
     * Konstruktor
     */
    public ItemButton() {
        setIcon(emptyIcon);
        setBackground(Color.darkGray);
    }

    /**
     * beállítja a resource objektumra való hivatkozást,
     * a portált nullra állítja, mert csak az egyik lehet aktív (nem lehet egyszerre portál és nyersanyag).
     * @param r nyersanyag
     */
    public void Set(Resource r){
        resource = r;
        portal = null;
        updateIcon();
    }

    /**
     * beállítja a portál objektumra való hivatkozást,
     * a resource-t nullra állítja, mert csak az egyik lehet aktív (nem lehet egyszerre portál és nyersanyag).
     * @param p portal
     */
    public void Set(Portal p){
        portal = p;
        resource = null;
        updateIcon();
    }

    private void updateIcon() {
        if (resource != null) {
            if (resource.equals(new Iron())) {
                setIcon(ironIcon);
            }
            else if (resource.equals(new Coal())) {
                setIcon(coalIcon);
            }
            else if (resource.equals(new Ice())) {
                setIcon(iceIcon);
            }
            else if (resource.equals(new Uranium())) {
                setIcon(uraniumIcon);
            }
        }
        else if (portal != null) {
            setIcon(portalIcon);
        }
        else {
            setIcon(emptyIcon);
        }
    }
}
