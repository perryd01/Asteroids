

/**
 * Ez arra való, hogy a comboboxban(a lenyíló menüben) ahol ki lehet választani egy playert,
 * egy név és egy objektum referencia párost tart számon, szóval a lényege,
 * hogy belerakhassunk egy playert a lenyíló menübe és az a nevével jelenjen meg.
 */
public class PlayerItem {
    private String name;
    public Player player;

    public PlayerItem(String name, Player player) {
        this.name = name;
        this.player = player;
    }

    /**
     * Név kiírása
     * @return String name
     */
    @Override
    public String toString() {
        return name;
    }
}
