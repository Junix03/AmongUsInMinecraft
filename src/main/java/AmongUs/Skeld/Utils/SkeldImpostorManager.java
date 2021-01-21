package AmongUs.Skeld.Utils;

import AmongUs.AmongUs;
import AmongUs.Skeld.SkeldGameManager;
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

public class SkeldImpostorManager implements Listener {

    private boolean isEmergency = false;

    private String oxygen = "-1:-1";
    private String reactor = "-1:-1";
    private String comms = "-1:-1";
    private String lights = "-1:-1";

    private String cafeteria = "-1:-1";
    private String electrical = "-1:-1";
    private String lowerEngine = "-1:-1";
    private String medBay = "-1:-1";
    private String security = "-1:-1";
    private String storage = "-1:-1";
    private String upperEngine = "-1:-1";

    public SkeldImpostorManager() {
        AmongUs.INSTANCE.getServer().getPluginManager().registerEvents(this, AmongUs.INSTANCE);
    }

    private void impostorInventory(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.clear();

        for (int i = 9; i < 36; i++)
            inventory.setItem(i, impostorItemStack(Material.BLACK_STAINED_GLASS_PANE, 1, "Sabotage and win this game!"));

        if (!checkEmergency() || !upperEngine.split(":")[1].equals("-1"))
            inventory.setItem(11, impostorItemStack(Material.BARRIER, 1, "Sabotage Upper Engine Door"));
        else
            inventory.setItem(11, impostorItemStack(Material.IRON_DOOR, 1, "Sabotage Upper Engine Door"));

        if (!checkEmergency() || !medBay.split(":")[1].equals("-1"))
            inventory.setItem(13, impostorItemStack(Material.BARRIER, 1, "Sabotage MedBay Door"));
        else
            inventory.setItem(13, impostorItemStack(Material.IRON_DOOR, 1, "Sabotage MedBay Door"));

        if (!checkEmergency() || !cafeteria.split(":")[1].equals("-1"))
            inventory.setItem(14, impostorItemStack(Material.BARRIER, 1, "Sabotage Cafeteria Door"));
        else
            inventory.setItem(14, impostorItemStack(Material.IRON_DOOR, 1, "Sabotage Cafeteria Door"));

        if (!checkEmergency() || !security.split(":")[1].equals("-1"))
            inventory.setItem(21, impostorItemStack(Material.BARRIER, 1, "Sabotage Security Door"));
        else
            inventory.setItem(21, impostorItemStack(Material.IRON_DOOR, 1, "Sabotage Security Door"));

        if (!checkEmergency() || !lowerEngine.split(":")[1].equals("-1"))
            inventory.setItem(29, impostorItemStack(Material.BARRIER, 1, "Sabotage Lower Engine Door"));
        else
            inventory.setItem(29, impostorItemStack(Material.IRON_DOOR, 1, "Sabotage Lower Engine Door"));

        if (!checkEmergency() || !electrical.split(":")[1].equals("-1"))
            inventory.setItem(31, impostorItemStack(Material.BARRIER, 1, "Sabotage Electrical Door"));
        else
            inventory.setItem(31, impostorItemStack(Material.IRON_DOOR, 1, "Sabotage Electrical Door"));

        if (!checkEmergency() || !storage.split(":")[1].equals("-1"))
            inventory.setItem(32, impostorItemStack(Material.BARRIER, 1, "Sabotage Storage Door"));
        else
            inventory.setItem(32, impostorItemStack(Material.IRON_DOOR, 1, "Sabotage Storage Door"));

        if (!checkEmergency() || !checkDoors() || !reactor.split(":")[1].equals("-1"))
            inventory.setItem(20, impostorItemStack(Material.BARRIER, 1, "Sabotage Reactor"));
        else
            inventory.setItem(20, impostorItemStack(Material.FIRE_CHARGE, 1, "Sabotage Reactor"));

        if (!checkEmergency() || !checkDoors() || !oxygen.split(":")[1].equals("-1"))
            inventory.setItem(24, impostorItemStack(Material.BARRIER, 1, "Sabotage Oxygen"));
        else
            inventory.setItem(24, impostorItemStack(Material.LILY_PAD, 1, "Sabotage Oxygen"));

        if (!checkEmergency() || !checkDoors() || !lights.split(":")[1].equals("-1"))
            inventory.setItem(22, impostorItemStack(Material.BARRIER, 1, "Sabotage Lights"));
        else
            inventory.setItem(22, impostorItemStack(Material.SEA_LANTERN, 1, "Sabotage Lights"));

        if (!checkEmergency() || !checkDoors() || !comms.split(":")[1].equals("-1"))
            inventory.setItem(33, impostorItemStack(Material.BARRIER, 1, "Sabotage Communications"));
        else
            inventory.setItem(33, impostorItemStack(Material.REDSTONE_WIRE, 1, "Sabotage Communications"));
    }

    private boolean checkEmergency() {
        boolean isEmergency = false;

        int active = Integer.parseInt(reactor.split(":")[0]);
        int cooldown = Integer.parseInt(reactor.split(":")[1]);
        if (active >= 0 || cooldown > 0)
            isEmergency = true;

        active = Integer.parseInt(oxygen.split(":")[0]);
        cooldown = Integer.parseInt(oxygen.split(":")[1]);
        if (active >= 0 || cooldown > 0)
            isEmergency = true;

        active = Integer.parseInt(comms.split(":")[0]);
        cooldown = Integer.parseInt(comms.split(":")[1]);
        if (active >= 0 || cooldown > 0)
            isEmergency = true;

        active = Integer.parseInt(lights.split(":")[0]);
        cooldown = Integer.parseInt(lights.split(":")[1]);
        if (active >= 0 || cooldown > 0)
            isEmergency = true;

        return !isEmergency;
    }

    private boolean checkDoors() {
        boolean isDoors = false;

        int active = Integer.parseInt(cafeteria.split(":")[0]);
        int cooldown = Integer.parseInt(cafeteria.split(":")[1]);
        if (active >= 0 || cooldown > 0)
            isDoors = true;

        active = Integer.parseInt(electrical.split(":")[0]);
        cooldown = Integer.parseInt(electrical.split(":")[1]);
        if (active >= 0 || cooldown > 0)
            isDoors = true;

        active = Integer.parseInt(lowerEngine.split(":")[0]);
        cooldown = Integer.parseInt(lowerEngine.split(":")[1]);
        if (active >= 0 || cooldown > 0)
            isDoors = true;

        active = Integer.parseInt(medBay.split(":")[0]);
        cooldown = Integer.parseInt(medBay.split(":")[1]);
        if (active >= 0 || cooldown > 0)
            isDoors = true;

        active = Integer.parseInt(security.split(":")[0]);
        cooldown = Integer.parseInt(security.split(":")[1]);
        if (active >= 0 || cooldown > 0)
            isDoors = true;

        active = Integer.parseInt(storage.split(":")[0]);
        cooldown = Integer.parseInt(storage.split(":")[1]);
        if (active >= 0 || cooldown > 0)
            isDoors = true;

        active = Integer.parseInt(upperEngine.split(":")[0]);
        cooldown = Integer.parseInt(upperEngine.split(":")[1]);
        if (active >= 0 || cooldown > 0)
            isDoors = true;

        return !isDoors;
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
        if (!(event.getRightClicked() instanceof Player))
            return;
        Player target = (Player) event.getRightClicked();
        SkeldGameManager.INSTANCE.addGhost(target);

        target.setInvisible(true);
        PotionEffect potionEffect = new PotionEffect(PotionEffectType.BLINDNESS, (int) 10e300, 255, false, false);
        target.addPotionEffect(potionEffect);
        target.playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 11, 0.25f);
        target.setWalkSpeed((float) (SkeldGameManager.INSTANCE.getSettings().getPlayerSpeed() * 1.5));
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

        switch (displayName) {
            case "Sabotage Upper Engine Door":
                if (checkEmergency() || Integer.parseInt(upperEngine.split(":")[1]) != 0)
                    return;
                upperEngine = "10:15";
                break;
            case "Sabotage MedBay Door":
                if (checkEmergency() || Integer.parseInt(medBay.split(":")[1]) != 0)
                    return;
                medBay = "10:15";
                break;
            case "Sabotage Cafeteria Door":
                if (checkEmergency() || Integer.parseInt(cafeteria.split(":")[1]) != 0)
                    return;
                cafeteria = "10:15";
                break;
            case "Sabotage Security Door":
                if (checkEmergency() || Integer.parseInt(security.split(":")[1]) != 0)
                    return;
                security = "10:15";
                break;
            case "Sabotage Lower Engine Door":
                if (checkEmergency() || Integer.parseInt(lowerEngine.split(":")[1]) != 0)
                    return;
                lowerEngine = "10:15";
                break;
            case "Sabotage Electrical Door":
                if (checkEmergency() || Integer.parseInt(electrical.split(":")[1]) != 0)
                    return;
                electrical = "10:15";
                break;
            case "Sabotage Storage Door":
                if (checkEmergency() || Integer.parseInt(storage.split(":")[1]) != 0)
                    return;
                storage = "10:15";
                break;
            case "Sabotage Reactor":
                if (checkEmergency() || checkDoors() || Integer.parseInt(reactor.split(":")[1]) != 0)
                    return;
                reactor = "30:0";
                isEmergency = true;
                break;
            case "Sabotage Lights":
                if (checkEmergency() || checkDoors() || Integer.parseInt(lights.split(":")[1]) != 0)
                    return;
                lights = "30:0";
                isEmergency = true;
                break;
            case "Sabotage Oxygen":
                if (checkEmergency() || checkDoors() || Integer.parseInt(oxygen.split(":")[1]) != 0)
                    return;
                oxygen = "1:0";
                isEmergency = true;
                break;
            case "Sabotage Communications":
                if (checkEmergency() || checkDoors() || Integer.parseInt(comms.split(":")[1]) != 0)
                    return;
                comms = "30:0";
                isEmergency = true;
                break;

        }
    }

    public boolean isEmergency() {
        return isEmergency;
    }

    @EventHandler
    public void onServerTickStartEvent(ServerTickStartEvent event) {
        int tickNumber = event.getTickNumber();
        if (tickNumber % 20 == 0) {
            ArrayList<Player> impostorList = SkeldGameManager.INSTANCE.getImpostor();
            for (Player player: impostorList)
                impostorInventory(player);

            if (cafeteria.split(":")[0].equals("10")) {
                Location loc1 = new Location(AmongUs.INSTANCE.getServer().getWorld("skeld"), 0, 0, 0);
                Location loc2 = new Location(AmongUs.INSTANCE.getServer().getWorld("skeld"), 0, 0, 0);
            }

            reduceEmergencyCount(reactor);
            reduceEmergencyCount(oxygen);

            reduceDoorCount(cafeteria);
            reduceDoorCount(electrical);
            reduceDoorCount(lowerEngine);
            reduceDoorCount(medBay);
            reduceDoorCount(security);
            reduceDoorCount(storage);
            reduceDoorCount(upperEngine);

            if (reactor.split(":")[0].equals("0"))
                SkeldGameManager.INSTANCE.endGame(2);
            if (oxygen.split(":")[0].equals("0"))
                SkeldGameManager.INSTANCE.endGame(2);
        }
    }

    private String reduceDoorCount(String door) {
        String[] args = door.split(":");
        if (Integer.parseInt(args[0]) >= 0)
            args[0] = String.valueOf(Integer.parseInt(args[0]) - 1);
        if (Integer.parseInt(args[1]) >= 0)
            args[1] = String.valueOf(Integer.parseInt(args[1]) - 1);
        return args[0] + ":" + args[1];
    }

    private String reduceEmergencyCount(String emergency) {
        String[] args = emergency.split(":");
        if (Integer.parseInt(args[0]) >= 0)
            args[0] = String.valueOf(Integer.parseInt(args[0]) - 1);
        if (Integer.parseInt(args[0]) == -1 && Integer.parseInt(args[1]) >= 0)
            args[1] = String.valueOf(Integer.parseInt(args[1]) - 1);
        return args[0] + ":" + args[1];
    }

    private void fill(Location loc1, Location loc2, Material material) {
        int x1 = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int y1 = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int z1 = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

        int x2 = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int y2 = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int z2 = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        for (int i = x1; i <= x2; i++)
            for (int j = y1; j <= y2; j++)
                for (int k = z1; k <= z2; k++) {
                    Location location = new Location(AmongUs.INSTANCE.getServer().getWorld("skeld"), i, j, k);
                    location.getBlock().setType(material);
                }
    }
}
