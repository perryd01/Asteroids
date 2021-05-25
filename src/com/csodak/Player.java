

import java.util.ArrayList;

/**
 * Örökli az ősosztály felelősségeit. Továbbá képes kibányászni az aszteroida magját, teleportkapu-párt és robotokat készíteni. Rendelkezik inventoryval, amiben tárolhat Portal-okat és Resource-okat.
 *
 * @version 1.0
 * @since skeleton
 * @since 2021.03.21
 */
public class Player extends Entity {
    /**
     * Hátizsák/eszköztár, ebben férnek el a nyersanyagok, és max 1 db portál párt, ami 1 helyet foglal.
     */
    private Inventory inventory = new Inventory();

    public Player(Asteroid location) {
        super(location);
    }


    /**
     * Kibányássza az aszteroida magját
     *
     * @return Mit sikerült bányászni, ha semmit, akkor null
     */
    public Resource Mine() {
        if (super.energy < Entity.miningCost || inventory.FreeSlots() == 0) {
            return null;
        }
        Resource r = super.location.Mine();
        if (r != null) {
            inventory.Store(r);
            super.energy -= Entity.miningCost;
            return r;
        }
        return null;
    }

    /**
     * Készít egy robotot és ha sikeres akkor levonja az elkészítéséhez szükséges nyersayagokat
     *
     * @return Az elkészített robot vagy null
     */
    public Robot CraftRobot() {
        if (super.energy < Entity.craftingCost) return null;
        Blueprint robotBlueprint = new Blueprint(Iron.class, Coal.class, Uranium.class);
        boolean success = robotBlueprint.Apply(ViewResources());
        if (success) {
            Robot r = new Robot(location);
            location.AddEntity(r);
            for (Resource u : robotBlueprint.used) {
                inventory.Remove(u);
            }
            super.energy -= Entity.craftingCost;
            return r;
        }
        return null;
    }

    /**
     * Egy új kört reprezentál
     * Feltölti az energiáját.
     */
    public void Next() {
        super.energy = Entity.maxEnergy;
    }

    /**
     * Visszaad egy nézetet, az összes nála lévő nyersagot egy tömbben.
     *
     * @return Resource tömb
     */
    @Override
    public ArrayList<Resource> ViewResources() {
        return inventory.ViewResources();
    }

    /**
     * Vissazad egy nézetet az összes nála lévő portált egy tömbben.
     * @return Portal tömb
     */
    public ArrayList<Portal> ViewPortals() {return inventory.ViewPortals();}

    /**
     * Elfogadhat vagy elutasíthat nyersanyagokat.
     *
     * @param resource Átadandó resource
     * @return true esetén sikeres, false esetén nem
     */
    public boolean Trade(Resource resource) {
        if (inventory.FreeSlots() > 0) {
            inventory.Store(resource);
            return true;
        }
        return false;
    }

    /**
     * Elkészít egy portál párt és elhelyezi a hátizsákban
     *
     * @return Sikerült-e
     */
    public boolean CraftPortal() {
        if (inventory.FreePortalSlots() < 2) return false;
        if (super.energy < Entity.craftingCost) return false;
        Blueprint portalBlueprint = new Blueprint(Iron.class, Iron.class, Uranium.class, Ice.class);
        boolean success = portalBlueprint.Apply(ViewResources());
        if (success) {
            for (Resource u : portalBlueprint.used) {
                inventory.Remove(u);
            }
            Portal[] portals = Portal.CreatePair();
            inventory.Store(portals[0]);
            inventory.Store(portals[1]);
            super.energy -= Entity.craftingCost;
        }
        return success;
    }

    /**
     * Letesz egy portált az adott aszteroidán és eltávolítja az inverntoryból
     *
     * @return Sikerült-e
     */
    public boolean PlacePortal() {
        if (super.energy < Entity.placingCost) return false;
        try {
            Portal portal = inventory.ViewPortals().get(0);
            if (portal.Link(location)) {
                inventory.Remove(portal);
                super.energy -= Entity.placingCost;
                return true;
            }
        } catch (IndexOutOfBoundsException | IllegalArgumentException ignored) {
        }
        return false;
    }

    /**
     * Beleteszi a játékos az aszteroida közepébe a paraméterként kapott nyersanyagot
     *
     * @param resource Átadandó resource
     * @return Sikerült-e
     */
    public boolean PlaceResource(Resource resource) {
        if (super.energy < Entity.placingCost || !inventory.ViewResources().contains(resource)) return false;
        Resource placeable = inventory.Remove(resource);
        if (placeable == null) {
            return false;
        } else if (super.location.PlaceBackCore(placeable)) {
            super.energy -= Entity.placingCost;
            return true;
        }
        inventory.Store(placeable);
        return false;
    }

    /**
     * Az adott játékos egy kiválasztott játékosnak adhat egy kiválasztott resourcet
     *
     * @param player   a játékos akinek adja a nyersanyagot
     * @param resource a nyersanyag amit adnak a másik játékosnak
     * @return Sikerült-e
     */
    public boolean GiveResource(Player player, Resource resource) {
        if (super.energy < Entity.tradingCost || !inventory.ViewResources().contains(resource)) return false;
        Resource tradeable = inventory.Remove(resource);
        if (player.Trade(tradeable)) {
            super.energy -= Entity.tradingCost;
            return true;
        }
        inventory.Store(tradeable);
        return false;
    }

    /**
     * Kiprinteli a státuszt.
     *
     * Format:
     * id = 0
     * inventory = { [], [] }
     * location = -1 //ha -1 akkor azt jelenti, hogy nincs helye, vagyis nem él
     * energy = 5
     */
    public void Status() {
        System.out.println("id = " + Command.GetInstance().players.indexOf(this) + "\n"
                + "inventory = { " + inventory.ViewResources().toString() + ", " + inventory.ViewPortals().toString() + " }\n"
                + "location = " + Command.GetInstance().asteroids.indexOf(super.location) + "\n"
                + "energy = " + super.energy);
    }

    /**
     * Meghal a játékos
     * @return true
     */
    @Override
    public boolean ApplySolarStorm() {
        super.ApplySolarStorm();
        Game.GetInstance().PlayerDied();
        return true;
    }

    /**
     * Felrobbanás hatásai érvényesülnek
     */
    @Override
    public void ApplyExplosion() {
        Game.GetInstance().PlayerDied();
        inventory.Explode();
        location = null;
    }


    @Override
    public String toString() {
        return "Player" + "@" + Integer.toHexString(hashCode());
    }
}
