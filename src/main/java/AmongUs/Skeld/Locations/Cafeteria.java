package AmongUs.Skeld.Locations;

import AmongUs.AmongUs;
import AmongUs.Skeld.SkeldGameManager;
import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Cafeteria implements Listener {

    public Cafeteria() {
        AmongUs.INSTANCE.getServer().getPluginManager().registerEvents(this, AmongUs.INSTANCE);
    }

    @EventHandler
    public void proceedPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null)
            return;
        if (SkeldGameManager.INSTANCE.isImpostor(event.getPlayer()))
            return;
        if (!checkLocation(event.getClickedBlock().getLocation()))
            return;
        Location location = event.getClickedBlock().getLocation();
    }

    @EventHandler
    public void proceedPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        if (SkeldGameManager.INSTANCE.isImpostor(event.getPlayer()))
            return;
        if (!checkLocation(event.getRightClicked().getLocation()))
            return;
        Location location = event.getRightClicked().getLocation();
    }

    @EventHandler
    public void proceedInventoryClickEvent(InventoryClickEvent event) {
        if (!checkLocation(event.getWhoClicked().getLocation()))
            return;
    }

    private boolean checkLocation(Location location) {
        if (!location.getWorld().getName().startsWith("skeld"))
            return false;

        return location.getBlockX() >= -11 && location.getBlockX() <= 11 && location.getBlockZ() >= -11 && location.getBlockZ() <= 11;
    }

    // Download Data

    private ArrayList<Inventory> downloadDataInventory = new ArrayList<>();
    private ArrayList<String> downloadDataProgress = new ArrayList<>();

    private void downloadData1(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        Inventory inventory = downloadDataInventory();
        player.openInventory(inventory);
        downloadDataInventory.add(inventory);
        downloadDataProgress.add(player.getName() + ":0");
    }

    private void downloadDataUpdate(Inventory inventory) {
        if (inventory.getViewers().get(0) == null)
            return;
        Player player = (Player) inventory.getViewers().get(0);
        String[] progressArray = new String[0];
        for (String progress: downloadDataProgress)
            if (progress.startsWith(player.getName()))
                progressArray = progress.split(":");
        if (progressArray.length == 0)
            return;

        progressArray[1] = String.valueOf(Integer.parseInt(progressArray[1]) + 1);
        int progress = Integer.parseInt(progressArray[1]);
        for (int i = 10; i < 17; i++) {
            int counter = 0;
            while (counter < 20 && progress > 0) {
                counter++;
                progress--;
            }
            if (counter != 0)
                inventory.setItem(i, downloadDataItemStack(Material.ORANGE_STAINED_GLASS, counter));

            if (progressArray[1].equals("140")) {
                player.closeInventory();
                SkeldGameManager.INSTANCE.getTaskManager(player).proceedTask("Cafeteria: Download Data");
            }
        }
    }

    private Inventory downloadDataInventory() {
        Inventory inventory = Bukkit.createInventory(null, 27, "§2Download Data");

        for (int i = 0; i < 27; i++)
            inventory.setItem(i, downloadDataItemStack(Material.LIGHT_BLUE_STAINED_GLASS, 1));

        for (int i = 10; i < 17; i++)
            inventory.setItem(i, new ItemStack(Material.AIR));

        return inventory;
    }

    private ItemStack downloadDataItemStack(Material material, int amount) {
        ItemStack itemStack = new ItemStack(material, amount);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(" ");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private void emptyGarbage1() {

    }

    private void fixWiring1() {

    }

    @EventHandler
    public void proceedServerTicks(ServerTickStartEvent event) {
        int tickNumber = event.getTickNumber();

        // Update Tasks
        for (Inventory inventory: downloadDataInventory)
            downloadDataUpdate(inventory);

        // Clear Running Memory
        if (tickNumber % 20 == 0) {
            for (int i = 0; i < downloadDataInventory.size(); i++)
                if (downloadDataInventory.get(i).getViewers().isEmpty()) {
                    downloadDataInventory.remove(i);
                    downloadDataProgress.remove(i);
                }
        }
    }
}
