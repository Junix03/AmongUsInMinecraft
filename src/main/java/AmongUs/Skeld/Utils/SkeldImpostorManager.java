package AmongUs.Skeld.Utils;

import AmongUs.AmongUs;
import AmongUs.Skeld.SkeldGameManager;
import com.destroystokyo.paper.Title;
import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;

public class SkeldImpostorManager implements Listener {

    public enum Sabotage {
        OXYGEN, REACTOR, COMMS, LIGHTS, CAFETERIA, ELECTRICAL, LOWERENGINE, MEDBAY, SECURITY, STORAGE, UPPERENGINE
    }

    private final HashMap<Player, Integer> killCooldown = new HashMap<>();
    private final HashMap<Sabotage, Integer> sabotageDurability = new HashMap<>();
    private final HashMap<Sabotage, Integer> sabotageCooldown = new HashMap<>();

    public SkeldImpostorManager() {
        AmongUs.INSTANCE.getServer().getPluginManager().registerEvents(this, AmongUs.INSTANCE);
        init();
    }

    private void init() {
        sabotageDurability.put(Sabotage.CAFETERIA, -1);
        sabotageDurability.put(Sabotage.COMMS, -1);
        sabotageDurability.put(Sabotage.ELECTRICAL, -1);
        sabotageDurability.put(Sabotage.LIGHTS, -1);
        sabotageDurability.put(Sabotage.LOWERENGINE, -1);
        sabotageDurability.put(Sabotage.MEDBAY, -1);
        sabotageDurability.put(Sabotage.OXYGEN, -1);
        sabotageDurability.put(Sabotage.REACTOR, -1);
        sabotageDurability.put(Sabotage.SECURITY, -1);
        sabotageDurability.put(Sabotage.STORAGE, -1);
        sabotageDurability.put(Sabotage.UPPERENGINE, -1);

        sabotageCooldown.put(Sabotage.CAFETERIA, 10);
        sabotageCooldown.put(Sabotage.COMMS, 10);
        sabotageCooldown.put(Sabotage.ELECTRICAL, 10);
        sabotageCooldown.put(Sabotage.LIGHTS, 10);
        sabotageCooldown.put(Sabotage.LOWERENGINE, 10);
        sabotageCooldown.put(Sabotage.MEDBAY, 10);
        sabotageCooldown.put(Sabotage.OXYGEN, 10);
        sabotageCooldown.put(Sabotage.REACTOR, 10);
        sabotageCooldown.put(Sabotage.SECURITY, 10);
        sabotageCooldown.put(Sabotage.STORAGE, 10);
        sabotageCooldown.put(Sabotage.UPPERENGINE, 10);

        for (Player player: SkeldGameManager.INSTANCE.getImpostor())
            impostorInventory(player);
    }

    public void addPlayer(Player player) {
        killCooldown.put(player, 10);
    }

    private void impostorInventory(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.clear();

        for (int i = 9; i < 36; i++)
            inventory.setItem(i, impostorItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, "Sabotage everything and win the game!"));

        if (checkSabotage(Sabotage.UPPERENGINE, false))
            inventory.setItem(11, impostorItemStack(Material.BARRIER, sabotageCooldown.get(Sabotage.UPPERENGINE) > 0 ? sabotageCooldown.get(Sabotage.UPPERENGINE) : 1, "Sabotage Upper Engine Doors"));
        else
            inventory.setItem(11, impostorItemStack(Material.IRON_DOOR, 1, "Sabotage Upper Engine Doors"));

        if (checkSabotage(Sabotage.MEDBAY, false))
            inventory.setItem(13, impostorItemStack(Material.BARRIER, sabotageCooldown.get(Sabotage.MEDBAY) > 0 ? sabotageCooldown.get(Sabotage.MEDBAY) : 1, "Sabotage MedBay Door"));
        else
            inventory.setItem(13, impostorItemStack(Material.IRON_DOOR, 1, "Sabotage MedBay Door"));

        if (checkSabotage(Sabotage.CAFETERIA, false))
            inventory.setItem(14, impostorItemStack(Material.BARRIER, sabotageCooldown.get(Sabotage.CAFETERIA) > 0 ? sabotageCooldown.get(Sabotage.CAFETERIA) : 1, "Sabotage Cafeteria Doors"));
        else
            inventory.setItem(14, impostorItemStack(Material.IRON_DOOR, 1, "Sabotage Cafeteria Doors"));

        if (checkSabotage(Sabotage.REACTOR, true))
            inventory.setItem(20, impostorItemStack(Material.BARRIER, sabotageCooldown.get(Sabotage.REACTOR) > 0 ? sabotageCooldown.get(Sabotage.REACTOR) : 1, "Sabotage Reactor Meltdown"));
        else
            inventory.setItem(20, impostorItemStack(Material.FIRE_CHARGE, 1, "Sabotage Reactor Meltdown"));

        if (checkSabotage(Sabotage.SECURITY, false))
            inventory.setItem(21, impostorItemStack(Material.BARRIER, sabotageCooldown.get(Sabotage.SECURITY) > 0 ? sabotageCooldown.get(Sabotage.SECURITY) : 1, "Sabotage Security Door"));
        else
            inventory.setItem(21, impostorItemStack(Material.IRON_DOOR, 1, "Sabotage Security Door"));

        if (checkSabotage(Sabotage.LIGHTS, true))
            inventory.setItem(22, impostorItemStack(Material.BARRIER, sabotageDurability.get(Sabotage.LIGHTS) < 0 ? sabotageCooldown.get(Sabotage.LIGHTS) : 1, "Sabotage Lights"));
        else
            inventory.setItem(22, impostorItemStack(Material.GLOWSTONE, 1, "Sabotage Lights"));

        if (checkSabotage(Sabotage.OXYGEN, true))
            inventory.setItem(24, impostorItemStack(Material.BARRIER, sabotageCooldown.get(Sabotage.OXYGEN) > 0 ? sabotageCooldown.get(Sabotage.OXYGEN) : 1, "Sabotage Oxygen Depletion"));
        else
            inventory.setItem(24, impostorItemStack(Material.LILY_PAD, 1, "Sabotage Oxygen Depletion"));

        if (checkSabotage(Sabotage.LOWERENGINE, false))
            inventory.setItem(29, impostorItemStack(Material.BARRIER, sabotageCooldown.get(Sabotage.LOWERENGINE) > 0 ? sabotageCooldown.get(Sabotage.LOWERENGINE) : 1, "Sabotage Lower Engine Doors"));
        else
            inventory.setItem(29, impostorItemStack(Material.IRON_DOOR, 1, "Sabotage Lower Engine Doors"));

        if (checkSabotage(Sabotage.ELECTRICAL, false))
            inventory.setItem(31, impostorItemStack(Material.BARRIER, sabotageCooldown.get(Sabotage.ELECTRICAL) > 0 ? sabotageCooldown.get(Sabotage.ELECTRICAL) : 1, "Sabotage Electrical Door"));
        else
            inventory.setItem(31, impostorItemStack(Material.IRON_DOOR, 1, "Sabotage Electrical Door"));

        if (checkSabotage(Sabotage.STORAGE, false))
            inventory.setItem(32, impostorItemStack(Material.BARRIER, sabotageCooldown.get(Sabotage.STORAGE) > 0 ? sabotageCooldown.get(Sabotage.STORAGE) : 1, "Sabotage Storage Doors"));
        else
            inventory.setItem(32, impostorItemStack(Material.IRON_DOOR, 1, "Sabotage Storage Doors"));

        if (checkSabotage(Sabotage.COMMS, true))
            inventory.setItem(33, impostorItemStack(Material.BARRIER, sabotageDurability.get(Sabotage.COMMS) < 0 ? sabotageCooldown.get(Sabotage.COMMS) : 1, "Sabotage Communications"));
        else
            inventory.setItem(33, impostorItemStack(Material.REDSTONE, 1, "Sabotage Communications"));
    }

    private boolean checkEmergency() {
        boolean isEmergency = false;

        if (sabotageCooldown.get(Sabotage.REACTOR) >= 0)
            isEmergency = true;

        if (sabotageCooldown.get(Sabotage.OXYGEN) >= 0)
            isEmergency = true;

        if (sabotageCooldown.get(Sabotage.LIGHTS) >= 0)
            isEmergency = true;

        if (sabotageCooldown.get(Sabotage.COMMS) >= 0)
            isEmergency = true;

        return isEmergency;
    }

    private boolean checkDoors() {
        boolean isDoors = false;

        if (sabotageDurability.get(Sabotage.CAFETERIA) >= 0)
            isDoors = true;

        if (sabotageDurability.get(Sabotage.ELECTRICAL) >= 0)
            isDoors = true;

        if (sabotageDurability.get(Sabotage.LOWERENGINE) >= 0)
            isDoors = true;

        if (sabotageDurability.get(Sabotage.MEDBAY) >= 0)
            isDoors = true;

        if (sabotageDurability.get(Sabotage.SECURITY) >= 0)
            isDoors = true;

        if (sabotageDurability.get(Sabotage.STORAGE) >= 0)
            isDoors = true;

        if (sabotageDurability.get(Sabotage.UPPERENGINE) >= 0)
            isDoors = true;

        return isDoors;
    }

    public boolean checkSabotage(Sabotage sabotage, boolean isEmergencySabotage) {
        boolean isSabotage = false;

        if (sabotageDurability.get(sabotage) >= 0 || sabotageCooldown.get(sabotage) >= 0 || checkEmergency())
            isSabotage = true;

        if (isEmergencySabotage && checkDoors())
            isSabotage = true;

        return isSabotage;
    }

    private ItemStack impostorItemStack(Material material, int amount, String displayName) {
        ItemStack itemStack = new ItemStack(material, amount);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        if (SkeldGameManager.INSTANCE.isCrewmate(event.getPlayer()))
            return;
        if (!(event.getRightClicked() instanceof Player) && SkeldGameManager.INSTANCE.isImpostor((Player) event.getRightClicked()))
            return;

        double x, y, z, ax, ay, az;
        x = event.getPlayer().getLocation().getX();
        y = event.getPlayer().getLocation().getY();
        z = event.getPlayer().getLocation().getZ();
        ax = event.getRightClicked().getLocation().getX();
        ay = event.getRightClicked().getLocation().getY();
        az = event.getRightClicked().getLocation().getZ();
        if (Math.cbrt(Math.sqrt(ax - x) + Math.sqrt(ay - y) + Math.sqrt(az - z)) > (SkeldGameManager.INSTANCE.getSettings().getKillDistance() * 2))
            return;
        Player target = (Player) event.getRightClicked();
        SkeldGameManager.INSTANCE.addGhost(target);

        target.setInvisible(true);
        PotionEffect potionEffect = new PotionEffect(PotionEffectType.BLINDNESS, 60, 255, false, false);
        target.addPotionEffect(potionEffect);
        target.playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 1, 0.25f);
        target.setWalkSpeed((float) (0.2 * (SkeldGameManager.INSTANCE.getSettings().getPlayerSpeed() * 1.5)));

        killCooldown.put(event.getPlayer(), (int) SkeldGameManager.INSTANCE.getSettings().getKillCooldown());
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getCurrentItem() == null)
            return;
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack.getItemMeta() == null)
            return;
        String displayName = itemStack.getItemMeta().getDisplayName();

        Location loc1, loc2;
        switch (displayName) {
            case "Sabotage Upper Engine Doors":
                if (checkSabotage(Sabotage.UPPERENGINE, false))
                    return;
                sabotageDurability.put(Sabotage.UPPERENGINE, 10);
                sabotageCooldown.put(Sabotage.UPPERENGINE, 15);

                loc1 = new Location(event.getWhoClicked().getWorld(), -33, 57, -1);
                loc2 = new Location(event.getWhoClicked().getWorld(), -33, 60, 1);
                fillDoor(loc1, loc2, Material.STONE_BRICK_WALL);


                break;
            case "Sabotage MedBay Door":
                if (checkSabotage(Sabotage.MEDBAY, false))
                    return;
                sabotageDurability.put(Sabotage.MEDBAY, 10);
                sabotageCooldown.put(Sabotage.MEDBAY, 15);

                AmongUs.INSTANCE.getServer().dispatchCommand(AmongUs.INSTANCE.getServer().getConsoleSender(), "fill -20 57 4 -18 60 4 minecraft:stone_brick_wall");
                break;
            case "Sabotage Cafeteria Doors":
                if (checkSabotage(Sabotage.CAFETERIA, false))
                    return;
                sabotageDurability.put(Sabotage.CAFETERIA, 10);
                sabotageCooldown.put(Sabotage.CAFETERIA, 15);

                AmongUs.INSTANCE.getServer().dispatchCommand(AmongUs.INSTANCE.getServer().getConsoleSender(), "execute in minecraft:lobby run fill -11 57 -1 -11 60 1 minecraft:stone_brick_wall");
                AmongUs.INSTANCE.getServer().dispatchCommand(AmongUs.INSTANCE.getServer().getConsoleSender(), "execute in minecraft:lobby run fill -1 57 11 1 60 11 minecraft:stone_brick_wall");
                AmongUs.INSTANCE.getServer().dispatchCommand(AmongUs.INSTANCE.getServer().getConsoleSender(), "execute in minecraft:lobby run fill 11 57 -1 11 60 1 minecraft:stone_brick_wall");
                break;
            case "Sabotage Security Door":
                if (checkSabotage(Sabotage.SECURITY, false))
                    return;
                sabotageDurability.put(Sabotage.SECURITY, 10);
                sabotageCooldown.put(Sabotage.SECURITY, 15);
                break;
            case "Sabotage Lower Engine Doors":
                if (checkSabotage(Sabotage.LOWERENGINE, false))
                    return;
                sabotageDurability.put(Sabotage.LOWERENGINE, 10);
                sabotageCooldown.put(Sabotage.LOWERENGINE, 15);
                break;
            case "Sabotage Electrical Door":
                if (checkSabotage(Sabotage.ELECTRICAL, false))
                    return;
                sabotageDurability.put(Sabotage.ELECTRICAL, 10);
                sabotageCooldown.put(Sabotage.ELECTRICAL, 15);
                break;
            case "Sabotage Storage Doors":
                if (checkSabotage(Sabotage.STORAGE, false))
                    return;
                sabotageDurability.put(Sabotage.STORAGE, 10);
                sabotageCooldown.put(Sabotage.STORAGE, 15);
                break;
            case "Sabotage Reactor Meltdown":
                if (checkSabotage(Sabotage.REACTOR, true))
                    return;
                sabotageDurability.put(Sabotage.REACTOR, 30);
                sabotageCooldown.put(Sabotage.REACTOR, 60);
                break;
            case "Sabotage Lights":
                if (checkSabotage(Sabotage.LIGHTS, true))
                    return;
                sabotageDurability.put(Sabotage.LIGHTS, 30);
                sabotageCooldown.put(Sabotage.LIGHTS, 60);
                break;
            case "Sabotage Oxygen Depletion":
                if (checkSabotage(Sabotage.OXYGEN, true))
                    return;
                sabotageDurability.put(Sabotage.OXYGEN, 30);
                sabotageCooldown.put(Sabotage.OXYGEN, 60);
                break;
            case "Sabotage Communications":
                if (checkSabotage(Sabotage.COMMS, true))
                    return;
                sabotageDurability.put(Sabotage.COMMS, 30);
                sabotageCooldown.put(Sabotage.COMMS, 60);
                break;
        }
    }

    public boolean isEmergency() {
        return !checkEmergency();
    }

    @EventHandler
    public void onServerTickStartEvent(ServerTickStartEvent event) {
        int tickNumber = event.getTickNumber();
        if (tickNumber % 20 == 0) {
            if (sabotageDurability.get(Sabotage.CAFETERIA) == 0) {
                AmongUs.INSTANCE.getServer().dispatchCommand(AmongUs.INSTANCE.getServer().getConsoleSender(), "execute in minecraft:lobby run fill -11 57 -1 -11 60 1 minecraft:air");
                AmongUs.INSTANCE.getServer().dispatchCommand(AmongUs.INSTANCE.getServer().getConsoleSender(), "execute in minecraft:lobby run fill -1 57 11 1 60 11 minecraft:air");
                AmongUs.INSTANCE.getServer().dispatchCommand(AmongUs.INSTANCE.getServer().getConsoleSender(), "execute in minecraft:lobby run fill 11 57 -1 11 60 1 minecraft:air");
            }
            for (Sabotage sabotage : Sabotage.values()) {
                if (/*sabotage != Sabotage.LIGHTS && sabotage != Sabotage.COMMS && */sabotageDurability.get(sabotage) >= 0)
                    sabotageDurability.put(sabotage, sabotageDurability.get(sabotage) - 1);
                if (sabotageCooldown.get(sabotage) >= 0)
                    sabotageCooldown.put(sabotage, sabotageCooldown.get(sabotage) - 1);
            }
            for (Player player: SkeldGameManager.INSTANCE.getImpostor()) {
                impostorInventory(player);
                if (killCooldown.get(player) >= 0)
                    killCooldown.put(player, killCooldown.get(player) - 1);
                else if (killCooldown.get(player) == 0) {
                    Title title = new Title("", "§cYou are ready to kill!", 40, 120, 40);
                    player.sendTitle(title);
                }
            }
        }
    }

    private void fillDoor(Location loc1, Location loc2, Material material) {
        int ax = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int ay = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int az = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int bx = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int by = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int bz = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        ArrayList<Location> locationList = new ArrayList<>();
        for (int i = ax; i <= bx; i++)
            for (int j = ay; j <= by; j++)
                for (int k = az; k <= bz; k++)
                    locationList.add(new Location(AmongUs.INSTANCE.getServer().getWorld("skeld"), i, j, k));

        for (Location location: locationList) {
            location.getChunk().load(false);
            location.getBlock().setType(material);
        }
    }
}
