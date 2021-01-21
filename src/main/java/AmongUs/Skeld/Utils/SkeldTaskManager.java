package AmongUs.Skeld.Utils;

import AmongUs.Skeld.SkeldGameManager;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class SkeldTaskManager implements Listener {

    private final Player player;
    private final ArrayList<String> commonTasks;
    private final ArrayList<String> longTasks = new ArrayList<>();
    private final ArrayList<String> shortTasks = new ArrayList<>();

    private boolean alignEngineProgress = false;
    private int fixWiringProgress = 0;
    private boolean fuelUpperEngine;

    public SkeldTaskManager(ArrayList<String> commonTasks, Player player) {
        this.player = player;
        this.commonTasks = commonTasks;
        createTaskList();
    }

    private void createTaskList() {
        SkeldSettings settings = SkeldGameManager.INSTANCE.getSettings();
        Random random = new Random();

        // Long Tasks
        ArrayList<String> tempLongTaskList = new ArrayList<>();
        tempLongTaskList.add("Weapons: Clear Asteroids");
        tempLongTaskList.add("O2: Empty Chute");
        tempLongTaskList.add("Cafeteria: Empty Garbage");
        tempLongTaskList.add("MedBay: Submit Scan");
        tempLongTaskList.add("Upper Engine: Align Engine Output");
        tempLongTaskList.add("Lower Engine: Align Engine Output");
        tempLongTaskList.add("Storage: Fuel Engines");
        tempLongTaskList.add("MedBay: Inspect Sample");
        tempLongTaskList.add("Reactor: Start Reactor");

        for (int i = 0; i < settings.getLongTasks(); i++) {
            String task = tempLongTaskList.get(random.nextInt(tempLongTaskList.size() - 1));
            if (task.contains("Align Engine Output")) {
                tempLongTaskList.remove("Upper Engine: Align Engine Output");
                tempLongTaskList.remove("Lower Engine: Align Engine Output");
            }
            tempLongTaskList.remove(task);
            longTasks.add(task);
        }

        // Short Tasks
        ArrayList<String> tempShortTaskList = new ArrayList<>();
        tempShortTaskList.add("Shields: Prime Shields");
        tempShortTaskList.add("Electrical: Calibrate Distributor");
        tempShortTaskList.add("Electrical: Divert Power To Communications");
        tempShortTaskList.add("Electrical: Divert Power To Lower Engine");
        tempShortTaskList.add("Electrical: Divert Power To Navigation");
        tempShortTaskList.add("Electrical: Divert Power To O2");
        tempShortTaskList.add("Electrical: Divert Power To Security");
        tempShortTaskList.add("Electrical: Divert Power To Shields");
        tempShortTaskList.add("Electrical: Divert Power To Upper Engine");
        tempShortTaskList.add("Electrical: Divert Power To Weapons");
        tempShortTaskList.add("Navigation: Stabilize Steering");
        tempShortTaskList.add("Reactor: Unlock Manifolds");
        tempShortTaskList.add("Cafeteria: Download Data");
        tempShortTaskList.add("Communications: Download Data");
        tempShortTaskList.add("Electrical: Download Data");
        tempShortTaskList.add("Navigation: Download Data");
        tempShortTaskList.add("Weapons: Download Data");

        for (int i = 0; i < settings.getLongTasks(); i++) {
            String task = tempShortTaskList.get(random.nextInt(tempShortTaskList.size() - 1));
            if (task.contains("Electrical: Divert Power To")) {
                tempShortTaskList.remove("Electrical: Divert Power To Communications");
                tempShortTaskList.remove("Electrical: Divert Power To Lower Engine");
                tempShortTaskList.remove("Electrical: Divert Power To Navigation");
                tempShortTaskList.remove("Electrical: Divert Power To O2");
                tempShortTaskList.remove("Electrical: Divert Power To Security");
                tempShortTaskList.remove("Electrical: Divert Power To Shields");
                tempShortTaskList.remove("Electrical: Divert Power To Upper Engine");
                tempShortTaskList.remove("Electrical: Divert Power To Weapons");
            }
            if (task.contains(": Download Data")) {
                tempShortTaskList.remove("Cafeteria: Download Data");
                tempShortTaskList.remove("Communications: Download Data");
                tempShortTaskList.remove("Electrical: Download Data");
                tempShortTaskList.remove("Navigation: Download Data");
                tempShortTaskList.remove("Weapons: Download Data");
            }
            tempShortTaskList.remove(task);
            shortTasks.add(task);
        }

        Collections.shuffle(commonTasks);
        Collections.shuffle(longTasks);
        Collections.shuffle(shortTasks);
    }

    public boolean isPlayer(Player player) {
        return this.player.equals(player);
    }

    public void proceedTask(String task) {
        commonTasks.remove(task);
        longTasks.remove(task);
        shortTasks.remove(task);

        boolean finished = true;
        Random random = new Random();

        if (task.contains("Fix Wiring")) {
            fixWiringProgress++;
            if (fixWiringProgress != 3) {
                finished = false;
                switch (task.split(": ")[0]) {
                    case "Electrical":
                        commonTasks.add("Storage: Fix Wiring (" + fixWiringProgress + " / 3)");
                        break;
                    case "Storage":
                        commonTasks.add("Admin: Fix Wiring (" + fixWiringProgress + " / 3)");
                        break;
                    case "Admin":
                        commonTasks.add("Navigation: Fix Wiring (" + fixWiringProgress + " / 3)");
                        break;
                    case "Navigation":
                        commonTasks.add("Cafeteria: Fix Wiring (" + fixWiringProgress + " / 3)");
                        break;
                    case "Cafeteria":
                        commonTasks.add("Security: Fix Wiring (" + fixWiringProgress + " / 3)");
                        break;
                    case "Security":
                        commonTasks.add("Electrical: Fix Wiring (" + fixWiringProgress + " / 3)");
                        break;
                }
            }
        }

        if (task.contains("Electrical: Divert Power To")) {
            finished = false;
            shortTasks.add(task.split(" ")[4] + ": Accept Diverted Power");
        }

        if (task.contains("Download Data")) {
            finished = false;
            shortTasks.add("Admin: Upload Data");
        }

        if (task.contains("O2: Empty Chute")) {
            finished = false;
            longTasks.add("Storage: Empty Chute");
        }

        if (task.contains("Cafeteria: Empty Garbage")) {
            finished = false;
            longTasks.add("Storage: Empty Garbage");
        }

        if (task.contains("Align Engine Output") && !alignEngineProgress) {
            finished = false;
            alignEngineProgress = true;
            if (task.startsWith("Upper Engine"))
                longTasks.add("Lower Engine: Align Engine Output");
            else
                longTasks.add("Upper Engine: Align Engine Output");
        }

        if (task.equals("Storage: Fuel Engines")) {
            finished = false;
            if (random.nextBoolean()) {
                fuelUpperEngine = true;
                longTasks.add("Upper Engine: Fuel Engines (2 / 4)");
            } else {
                fuelUpperEngine = false;
                longTasks.add("Lower Engine: Fuel Engines (2 / 4)");
            }
        }

        if (task.contains("Fuel Engines (2 / 4)")) {
            finished = false;
            longTasks.add("Storage: Fuel Engines (3 / 4)");
        }

        if (task.contains("Fuel Engines (3 / 4)")) {
            finished = false;
            if (fuelUpperEngine)
                longTasks.add("Lower Engine: Fuel Engines (4 / 4)");
            else
                longTasks.add("Upper Engine: Fuel Engines (4 / 4)");
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
