package AmongUs.Skeld;

import com.destroystokyo.paper.Title;
import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SkeldTaskManager implements Listener {

    private final Player player;
    private final ArrayList<String> commonTasks;
    private final ArrayList<String> longTasks = new ArrayList<>();
    private final ArrayList<String> shortTasks = new ArrayList<>();

    private boolean alignEngineProgress = false;
    private final ArrayList<String> fixWiringProgress = new ArrayList<>();

    public SkeldTaskManager(ArrayList<String> commonTasks, Player player) {
        this.player = player;
        this.commonTasks = commonTasks;
    }

    private void createTaskList() {
        ArrayList<String> tempTaskList = new ArrayList<>();

    }

    public boolean isPlayer(Player player) {
        return this.player.equals(player);
    }

    public void proceedTask(String task) {
        commonTasks.remove(task);
        longTasks.remove(task);
        shortTasks.remove(task);

        boolean finished = true;

        if (task.split(": ")[1].startsWith("Fix")) {
            fixWiringProgress.add(task);

            if (fixWiringProgress.size() < 3) {
                finished = false;

                ArrayList<String> tempTaskList = new ArrayList<>();
                tempTaskList.add("Cafeteria: Fix Wiring (" + fixWiringProgress.size() + " / 3)");
                tempTaskList.add("Navigation: Fix Wiring( " + fixWiringProgress.size() + " / 3)");
                tempTaskList.add("Storage: Fix Wiring( " + fixWiringProgress.size() + " / 3)");
                tempTaskList.add("Admin: Fix Wiring( " + fixWiringProgress.size() + " / 3)");
                tempTaskList.add("Electrical: Fix Wiring( " + fixWiringProgress.size() + " / 3)");
                tempTaskList.add("Security: Fix Wiring( " + fixWiringProgress.size() + " / 3)");

                tempTaskList.removeAll(fixWiringProgress);
                Random random = new Random();
                commonTasks.add(tempTaskList.get(random.nextInt(tempTaskList.size())));
            }
        }

        if (finished) {
            Title title = new Title("", "§2\"" + task + "\" finished", 20, 60, 20);
            player.sendTitle(title);
        }
    }

    public ArrayList<String> getFullTaskList() {
        ArrayList<String> taskList = new ArrayList<>();
        taskList.addAll(commonTasks);
        taskList.addAll(longTasks);
        taskList.addAll(shortTasks);

        return taskList;
    }

    @EventHandler
    public void onServerTickStartEvent(ServerTickStartEvent event) {
        int tickNumber = event.getTickNumber();
        if (tickNumber % 100 != 0)
            return;

        ArrayList<String> taskList = getFullTaskList();
        Collections.shuffle(taskList);
        int number = taskList.size();
        int counter = 0;

        Inventory inventory = player.getInventory();
        inventory.clear();
        if (number >= 6 && number <= 10) {
            inventory.setItem(3, inventoryDisplayItem(taskList.get(counter++)));
            inventory.setItem(5, inventoryDisplayItem(taskList.get(counter++)));
            inventory.setItem(11, inventoryDisplayItem(taskList.get(counter++)));
            inventory.setItem(15, inventoryDisplayItem(taskList.get(counter++)));
            inventory.setItem(21, inventoryDisplayItem(taskList.get(counter++)));
            inventory.setItem(23, inventoryDisplayItem(taskList.get(counter++)));
        }
        if (number >= 4 && number <= 5 || number == 10) {
            inventory.setItem(6, inventoryDisplayItem(taskList.get(counter++)));
            inventory.setItem(22, inventoryDisplayItem(taskList.get(counter++)));
        }
        if (number >= 2 && number <= 5 || number >= 8 && number <= 10) {
            inventory.setItem(12, inventoryDisplayItem(taskList.get(counter++)));
            inventory.setItem(14, inventoryDisplayItem(taskList.get(counter++)));
        }
        if (number % 2 == 1)
            inventory.setItem(13, inventoryDisplayItem(taskList.get(counter++)));
    }

    private ItemStack inventoryDisplayItem(String displayName) {
        ItemStack itemStack = new ItemStack(Material.PAPER);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
