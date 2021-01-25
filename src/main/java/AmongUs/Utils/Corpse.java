package AmongUs.Utils;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

public class Corpse {

    private final ArmorStand armorStand;

    public Corpse(Location location, Color color) {
        location = location.toCenterLocation();
        while (location.getBlock().getType() == Material.AIR)
            location.setY(location.getY() - 1);
        location.setY(location.getY() + 1);
        assert EntityType.ARMOR_STAND.getEntityClass() != null;
        this.armorStand = (ArmorStand) location.getWorld().spawn(location, EntityType.ARMOR_STAND.getEntityClass());

        armorStand.setRotation(location.getYaw(), 90);
    }

    public void killCorpse() {
        armorStand.remove();
    }
}
