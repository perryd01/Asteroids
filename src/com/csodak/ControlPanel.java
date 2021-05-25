

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * GUI megjelenítése
 */
public class ControlPanel extends JPanel {

    /**
     * Játékos - mozgás gomb
     */
    private final JButton moveBtn = new JButton("Move");

    /**
     * Játékos - nyersanyagátadás gomb
     */
    private final JButton giveBtn = new JButton("Give");

    /**
     * Bányászás gomb
     */
    private final JButton mineBtn = new JButton("Mine");

    /**
     * Fúrás gomb
     */
    private final JButton drillBtn = new JButton("Drill");

    /**
     * Nyersanyag visszarakása üres aszteroidába
     */
    private final JButton placeResourceBtn = new JButton("Place Resource");

    /**
     * Robot készítés gomb
     */
    private final JButton craftRobotBtn = new JButton("Craft Robot");

    /**
     * Portál készítés gomb
     */
    private final JButton craftPortalBtn = new JButton("Craft Portal");

    /**
     * Portál elhelyezés gomb
     */
    private final JButton placePortalBtn = new JButton("Place Portal");

    /**
     * Következő kör gomb
     */
    private final JButton nextBtn = new JButton("Next round");

    /**
     * Játékos kiválasztó lista
     */
    private final JComboBox<PlayerItem> activePlayerCB = new JComboBox<>();

    /**
     * Aszteroida kiválasztó lista
     */
    private final JComboBox<AsteroidItem> targetLocationCB = new JComboBox<>();

    /**
     * Nyersanyagot fogadó játékos kiválasztó lista
     */
    private final JComboBox<PlayerItem> targetPlayerCB = new JComboBox<>();

    /**
     * Inventory GUI megvalósítása
     */
    private ArrayList<ItemButton> items = new ArrayList<>();

    /**
     * Összes valaha létezett játékos listája
     */
    private ArrayList<PlayerItem> players = new ArrayList<>();

    /**
     * Konstruktor
     */
    public ControlPanel() {
        targetLocationCB.setPreferredSize(new Dimension(200, (int) targetLocationCB.getSize().getHeight()));

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;

        setLayout(layout);

        JPanel panel1 = new JPanel();

        JLabel pLabel = new JLabel(" Player");
        JLabel mLabel = new JLabel(" Move to");
        JLabel gLabel = new JLabel(" Give to");

        panel1.setBackground(Color.darkGray);
        pLabel.setForeground(Color.WHITE);
        mLabel.setForeground(Color.WHITE);
        gLabel.setForeground(Color.WHITE);

        mineBtn.setBackground(Color.darkGray);
        drillBtn.setBackground(Color.darkGray);
        moveBtn.setBackground(Color.darkGray);
        giveBtn.setBackground(Color.darkGray);
        placePortalBtn.setBackground(Color.darkGray);
        placeResourceBtn.setBackground(Color.darkGray);
        craftPortalBtn.setBackground(Color.darkGray);
        craftRobotBtn.setBackground(Color.darkGray);

        mineBtn.setForeground(Color.WHITE);
        drillBtn.setForeground(Color.WHITE);
        moveBtn.setForeground(Color.WHITE);
        giveBtn.setForeground(Color.WHITE);
        placePortalBtn.setForeground(Color.WHITE);
        placeResourceBtn.setForeground(Color.WHITE);
        craftPortalBtn.setForeground(Color.WHITE);
        craftRobotBtn.setForeground(Color.WHITE);

        activePlayerCB.setBackground(Color.darkGray);
        activePlayerCB.setForeground(Color.WHITE);
        targetLocationCB.setBackground(Color.darkGray);
        targetLocationCB.setForeground(Color.WHITE);
        targetPlayerCB.setBackground(Color.darkGray);
        targetPlayerCB.setForeground(Color.WHITE);

        nextBtn.setBackground(Color.darkGray);
        nextBtn.setForeground(Color.WHITE);

        JPanel filler = new JPanel();
        filler.setBackground(Color.darkGray);
        JPanel filler2 = new JPanel();
        filler2.setBackground(Color.darkGray);
        JPanel filler3 = new JPanel();
        filler3.setBackground(Color.darkGray);
        JPanel filler4 = new JPanel();
        filler4.setBackground(Color.darkGray);

        GridLayout gl = new GridLayout(6, 3);
        panel1.setLayout(gl);
        panel1.add(pLabel);
        panel1.add(activePlayerCB);
        panel1.add(filler);
        panel1.add(mLabel);
        panel1.add(targetLocationCB);
        panel1.add(moveBtn);
        panel1.add(gLabel);
        panel1.add(targetPlayerCB);
        panel1.add(giveBtn);

        panel1.add(filler2);
        panel1.add(filler3);
        panel1.add(filler4);

        panel1.add(mineBtn);
        panel1.add(craftRobotBtn);
        panel1.add(placeResourceBtn);
        panel1.add(drillBtn);
        panel1.add(craftPortalBtn);
        panel1.add(placePortalBtn);

        add(panel1, c);
        add(nextBtn, c);

        JPanel panel2 = new JPanel();
        GridLayout itemGrid = new GridLayout(2, 5);
        panel2.setLayout(itemGrid);


        for (int i = 0; i < 10; ++i) {
            items.add(new ItemButton());
            panel2.add(items.get(i));
            items.get(i).addActionListener(e -> {
                ItemButton btn = (ItemButton) e.getSource();
                for (int j = 0; j < 10; j++) {
                    items.get(j).setBackground(Color.darkGray);
                    items.get(j).lastClicked = false;
                }
                btn.lastClicked = true;
                btn.setBackground(Color.gray);
            });
        }

        TitledBorder invBorder = new TitledBorder("Inventory");
        invBorder.setTitleColor(Color.WHITE);
        panel2.setBackground(Color.darkGray);
        panel2.setBorder(invBorder);

        add(panel2, c);

        moveBtn.addActionListener(l -> {
            Player me;
            Asteroid target;
            try {
                me = ((PlayerItem) activePlayerCB.getSelectedItem()).player;
                target = ((AsteroidItem) targetLocationCB.getSelectedItem()).asteroid;
            } catch (NullPointerException ignored) {
                return;
            }
            me.Move(target);
            updateAll();
        });

        drillBtn.addActionListener(l -> {
            Player me;
            try {
                me = ((PlayerItem) activePlayerCB.getSelectedItem()).player;
            } catch (NullPointerException ignore) {
                return;
            }
            me.Drill();
            updateAll();
        });

        mineBtn.addActionListener(l -> {
            Player me;
            try {
                me = ((PlayerItem) activePlayerCB.getSelectedItem()).player;
            } catch (NullPointerException ignore) {
                return;
            }
            me.Mine();
            updateAll();
        });

        craftPortalBtn.addActionListener(l -> {
            Player me;
            try {
                me = ((PlayerItem) activePlayerCB.getSelectedItem()).player;
            } catch (NullPointerException ignore) {
                return;
            }
            me.CraftPortal();
            updateAll();
        });

        craftRobotBtn.addActionListener(l -> {
            Player me;

            try {
                me = ((PlayerItem) activePlayerCB.getSelectedItem()).player;
            } catch (NullPointerException ignore) {
                return;
            }
            Robot robot = me.CraftRobot();
            if (robot != null) {
                Controller.getInstance().asteroidFieldContainer.AddRobot(robot);
                Controller.getInstance().AddRobot(robot);
                Game.GetInstance().AddEntity(robot);
            }
            updateAll();
        });

        placeResourceBtn.addActionListener(l -> {
            Player me;
            try {
                me = ((PlayerItem) activePlayerCB.getSelectedItem()).player;
            } catch (NullPointerException ignore) {
                return;
            }

            for (ItemButton it : items) {
                if (it.lastClicked) {
                    if (it.resource != null) {
                        me.PlaceResource(it.resource);
                    }
                    break;
                }
            }
            updateAll();
        });

        placePortalBtn.addActionListener(l -> {
            Player me;
            try {
                me = ((PlayerItem) activePlayerCB.getSelectedItem()).player;
            } catch (NullPointerException ignore) {
                return;
            }

            for (ItemButton it : items) {
                if (it.lastClicked) {
                    if (it.portal != null) {
                        me.PlacePortal();
                    }
                    break;
                }
            }
            updateAll();
        });

        nextBtn.addActionListener(l -> {
            Controller.getInstance().Next();
            updateAll();
        });

        targetLocationCB.addItemListener(l -> {
            try {
                Asteroid a1 = ((AsteroidItem) targetLocationCB.getSelectedItem()).asteroid;
                Asteroid a2 = ((PlayerItem) activePlayerCB.getSelectedItem()).player.GetLocation();
                Controller.getInstance().asteroidFieldContainer.SetActiveConnection(a1, a2);
            }
            catch (NullPointerException ignore) {
                Controller.getInstance().asteroidFieldContainer.SetActiveConnection(null, null);
            }
            Controller.getInstance().asteroidFieldContainer.repaint();
        });

        giveBtn.addActionListener(l -> {
            Player me;
            Player target;
            try {
                me = ((PlayerItem) activePlayerCB.getSelectedItem()).player;
                target = ((PlayerItem) targetPlayerCB.getSelectedItem()).player;
            } catch (NullPointerException ignored) {
                return;
            }
            for (ItemButton it : items) {
                if (it.lastClicked) {
                    if (it.resource != null) {
                        me.GiveResource(target, it.resource);
                    }
                    break;
                }
            }
            updateAll();
        });

    }

    /**
     * GUI teljes letiltása
     */
    private void disableUI(){
        this.setEnabled(false);
        moveBtn.setEnabled(false);
        giveBtn.setEnabled(false);
        mineBtn.setEnabled(false);
        drillBtn.setEnabled(false);
        placeResourceBtn.setEnabled(false);
        craftRobotBtn.setEnabled(false);
        craftPortalBtn.setEnabled(false);
        placePortalBtn.setEnabled(false);
        nextBtn.setEnabled(false);
        activePlayerCB.setEnabled(false);
        targetPlayerCB.setEnabled(false);
        targetLocationCB.setEnabled(false);
    }

    /**
     * Játék vége esetén hívódik
     */
    private void updateAll() {

        if(Game.GetInstance().win || Game.GetInstance().lose){
            disableUI();
        }

        boolean someone_died = false;
        for (int i = 0; i < activePlayerCB.getItemCount(); ++i) {
            if (activePlayerCB.getItemAt(i).player.GetLocation() == null) {
                someone_died = true;
                break;
            }
        }
        if (someone_died) {
            activePlayerCB.removeAllItems();
            for (PlayerItem pi: players) {
                if (pi.player.GetLocation() != null) {
                    activePlayerCB.addItem(pi);
                }
            }
        }

        targetLocationCB.removeAllItems();
        targetPlayerCB.removeAllItems();

        Player me;
        try {
            me = ((PlayerItem) activePlayerCB.getSelectedItem()).player;
        } catch (NullPointerException ignore) {
            return;
        }
        Controller.getInstance().asteroidFieldContainer.SetFocus(me);
        var neighbours = me.GetLocation().GetNeighbours();

        // Ez a controller resze lesz
        for (Asteroid a : neighbours) {
            targetLocationCB.addItem(new AsteroidItem(Controller.getInstance().GetAsteroidName(a), a));
        }


        var entities = me.GetLocation().GetEntities();
        for (Entity e : entities) {
            if (e != me && e instanceof Player) {
                Player p = (Player) e;
                targetPlayerCB.addItem(new PlayerItem(Controller.getInstance().GetPlayerName(p), p));
            }
        }

        {
            int resourceIndex = 0;
            int portalIndex = 0;
            var resources = me.ViewResources();
            var portals = me.ViewPortals();
            for (ItemButton item : items) {
                if (resourceIndex < resources.size()) {
                    item.Set(resources.get(resourceIndex));
                    resourceIndex++;
                } else if (portalIndex < portals.size()) {
                    item.Set(portals.get(portalIndex));
                    portalIndex++;
                } else {
                    item.Set((Portal) null);
                }
            }
        }

        Controller.getInstance().asteroidFieldContainer.repaint();
    }

    /**
     * Játékoslista update
     * @param players játékoslista
     */
    public void UpdatePlayerList(ArrayList<Player> players) {
        for (Player p : players) {
            PlayerItem pi = new PlayerItem(Controller.getInstance().GetPlayerName(p), p);
            activePlayerCB.addItem(pi);
            this.players.add(pi);
        }
        updateAll();
        activePlayerCB.addItemListener(l -> {
            updateAll();
        });
    }

}
