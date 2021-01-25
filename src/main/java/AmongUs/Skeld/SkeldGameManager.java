package AmongUs.Skeld;

import AmongUs.AmongUs;
import AmongUs.Skeld.Commands.SkeldCommand;
import AmongUs.Skeld.Locations.Cafeteria;
import AmongUs.Skeld.Utils.SkeldImpostorManager;
import AmongUs.Skeld.Utils.SkeldSettings;
import AmongUs.Skeld.Utils.SkeldTaskManager;
import AmongUs.Utils.ColorUtil;
import com.destroystokyo.paper.Title;
import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.EulerAngle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class SkeldGameManager implements Listener {

    private final static String PREFIX = "§2[Among Us | The Skeld]: §r";
    public static SkeldGameManager INSTANCE;

    private final ArrayList<Player> playerList = new ArrayList<>();
    private final ColorUtil colorUtil = new ColorUtil();
    private boolean isOpen = true;
    private final SkeldSettings settings = new SkeldSettings();

    private ArrayList<Player> crewmateList = new ArrayList<>();
    private ArrayList<Player> impostorList = new ArrayList<>();
    private ArrayList<SkeldTaskManager> taskmanagerList = new ArrayList<>();

    private ArrayList<Player> ghostList = new ArrayList<>();
    private SkeldImpostorManager impostorManager;
    private ArrayList<ArmorStand> corpsesList = new ArrayList<>();
    private BossBar taskProcessBar;

    private boolean isMeeting = false;

    // Locations
    private final Cafeteria cafeteria = new Cafeteria();

    public SkeldGameManager() {
        this.init();
    }

    private void init() {
        INSTANCE = this;
        AmongUs.registerCommand("skeld", new SkeldCommand());
        impostorManager = new SkeldImpostorManager();
    }

    public void startGame(Player player) {
        if (!isHost(player)) {
            player.sendTitle(new Title("", "§cYou are not the Host!", 20, 60, 20));
            return;
        }

        impostorManager = new SkeldImpostorManager();
        Random random = new Random();
        isOpen = false;
        setLobbyEntrance(false);

        while (impostorList.size() < settings.getImpostor())
            impostorList.add(playerList.get(random.nextInt(playerList.size() - 1)));
        ArrayList<Player> tempPlayerList = (ArrayList<Player>) playerList.clone();
        tempPlayerList.removeAll(impostorList);
        crewmateList = tempPlayerList;

        Title crewmateTitle;
        if (settings.getImpostor() == 1)
            crewmateTitle = new Title("§bCREWMATE", "There is 1 impostor among us", 20, 60, 20);
        else {
            crewmateTitle = new Title("§bCREWMATE", "There are " + settings.getImpostor() + " impostors among us", 20, 60, 20);
        }
        String impostors = "";
        for (Player player1: impostorList)
            impostors += player1.getName() + "; ";
        Title impostorTitle;
        if (settings.getImpostor() == 1)
            impostorTitle = new Title("§cIMPOSTOR", "You are Impostor", 20, 60, 20);
        else
            impostorTitle = new Title("§cIMPOSTOR", "The impostors are: " + impostors, 20, 60, 20);

        // Common Tasks
        ArrayList<String> tempCommonTasks = new ArrayList<>();
        ArrayList<String> commonTasks = new ArrayList<>();
        tempCommonTasks.add("Electrical: Fix Wiring");
        tempCommonTasks.add("Storage: Fix Wiring");
        tempCommonTasks.add("Admin: Fix Wiring");
        tempCommonTasks.add("Navigation: Fix Wiring");
        tempCommonTasks.add("Cafeteria: Fix Wiring");
        tempCommonTasks.add("Security: Fix Wiring");
        tempCommonTasks.add("Admin: Swipe Card");

        for (int i = 0; i < settings.getLongTasks(); i++) {
            String task = tempCommonTasks.get(random.nextInt(tempCommonTasks.size() - 1));
            if (task.contains("Fix Wiring")) {
                tempCommonTasks.remove("Electrical: Fix Wiring");
                tempCommonTasks.remove("Storage: Fix Wiring");
                tempCommonTasks.remove("Admin: Fix Wiring");
                tempCommonTasks.remove("Navigation: Fix Wiring");
                tempCommonTasks.remove("Cafeteria: Fix Wiring");
                tempCommonTasks.remove("Security: Fix Wiring");
            }
            tempCommonTasks.remove(task);
            commonTasks.add(task);
        }

        for (Player p: crewmateList)
            taskmanagerList.add(new SkeldTaskManager(commonTasks, p));

        PotionEffect potionEffect = new PotionEffect(PotionEffectType.BLINDNESS, 100, 1, false, false);
        for (Player player1: playerList)
            player1.addPotionEffect(potionEffect);

        // TODO Location for Game Start
        Location location = new Location(AmongUs.INSTANCE.getServer().getWorld("skeld"), 0, 0, 0, 0, 0);
        for (Player player1: playerList)
            player1.teleport(location);

        for (Player player1: crewmateList)
            player1.sendTitle(crewmateTitle);
        for (Player player1: impostorList)
            player1.sendTitle(impostorTitle);

        taskProcessBar = AmongUs.INSTANCE.getServer().createBossBar("Tasks completed:", BarColor.GREEN, BarStyle.SOLID, BarFlag.DARKEN_SKY);
        taskProcessBar.setProgress(0);
        for (Player player1: playerList)
            taskProcessBar.addPlayer(player1);

        for (Player player1: playerList)
            player1.setWalkSpeed((float) (0.2 * settings.getPlayerSpeed()));
    }

    public boolean isHost(Player player) {
        return playerList.size() > 0 && playerList.get(0).equals(player);
    }

    public void joinGame(Player player) {
        if (playerList.contains(player))
            return;

        if (!isOpen || playerList.size() == 10)
            player.sendMessage(PREFIX + "§6Sorry, but you cant enter this game. Please try again later!");
        else {
            playerList.add(player);
            colorUtil.addPlayer(player);

            Location location = new Location(AmongUs.INSTANCE.getServer().getWorld("lobby"), 50, 10, 50, 0, 0);
            player.teleport(location);
            for (Player p: playerList)
                p.sendMessage(PREFIX + "§6" + player.getName() + " joined the Game!");
            if (isHost(player))
                playerList.get(0).sendTitle(new Title("", "You are the new Host", 20, 60, 20));

            if (playerList.size() == 10)
                setLobbyEntrance(false);
        }
    }

    public void leaveGame(Player player) {
        boolean isHost = isHost(player);

        playerList.remove(player);
        crewmateList.remove(player);
        impostorList.remove(player);
        colorUtil.removePlayer(player);

        Location location = new Location(AmongUs.INSTANCE.getServer().getWorld("lobby"), 0.0, 10, -8.0, 0, 0);
        player.teleport(location);

        for (Player p: playerList)
            p.sendMessage(PREFIX + "§6" + player.getName() + " left the Game!");
        if (isHost)
            if (!playerList.isEmpty())
                playerList.get(0).sendTitle(new Title("", "You are the new Host", 20, 60, 20));

        if (isOpen)
            setLobbyEntrance(true);

        if (playerList.size() <= 3) {
            ArrayList<Player> livingCrewmateList = crewmateList;
            livingCrewmateList.removeAll(ghostList);
            int livingCrewmateCount = livingCrewmateList.size();
            ArrayList<Player> livingImpostorList = impostorList;
            livingImpostorList.removeAll(ghostList);
            int livingImpostorCount = livingImpostorList.size();

            endGame((livingCrewmateCount - livingImpostorCount) > 0);
        }
    }

    public void endGame(boolean CrewWin) {
        // TODO LOCATION FOR GAME END AND GAME END ITSELF
    }

    public void addGhost(Player player) {
        ghostList.add(player);

        Location location = player.getLocation().clone();
        location.setY(location.getBlockY() - 1);
        ArmorStand armorStand = (ArmorStand) player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
        armorStand.setArms(false);
        armorStand.setBasePlate(false);
        armorStand.setHeadPose(new EulerAngle(106, 0, 0));
        armorStand.setBodyPose(new EulerAngle(82, 31, 0));

        ItemStack itemStack = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta itemMeta = (LeatherArmorMeta) itemStack.getItemMeta();
        itemMeta.setColor(colorUtil.getPlayerColor(player));
        itemStack.setItemMeta(itemMeta);
        itemStack.addEnchantment(Enchantment.BINDING_CURSE, 1);
        player.getInventory().setChestplate(itemStack);

        corpsesList.add(armorStand);
    }

    public void setLobbyEntrance(boolean isOpen) {
        World world = AmongUs.INSTANCE.getServer().getWorld("lobby");
        Location location;

        for (int i = 10; i <= 12; i++)
            for (int j = -2; j <= 1; j++) {
                location = new Location(world, 7, i, j);
                if (isOpen)
                    location.getBlock().setType(Material.AIR);
                else
                    location.getBlock().setType(Material.BLACK_STAINED_GLASS_PANE);
            }
    }

    public void proceedTask(Player player, String task) {
        int totalTaskNumber = 0;
        for (Player player1: crewmateList)
            totalTaskNumber += settings.getCommonTasks() + settings.getLongTasks() + settings.getShortTasks();
        int relativeTaskNumber = 0;
        for (Player player1:crewmateList)
            relativeTaskNumber += getTaskManager(player1).getFullTaskList().size();
        taskProcessBar.setProgress((float) (totalTaskNumber - relativeTaskNumber) / totalTaskNumber);
        getTaskManager(player).proceedTask(task);
    }

    public boolean isCrewmate(Player player) {
        return crewmateList.contains(player);
    }

    public boolean isImpostor(Player player) {
        return impostorList.contains(player);
    }

    public ArrayList<Player> getImpostor() { return impostorList; }

    public boolean isPlayer(Player player) {
        return playerList.contains(player);
    }

    public SkeldTaskManager getTaskManager(Player player) {
        for (SkeldTaskManager taskManager: taskmanagerList)
            if (taskManager.isPlayer(player))
                return taskManager;
        return null;
    }

    public SkeldSettings getSettings() {
        return settings;
    }

    public ColorUtil getColorUtil() {
        return colorUtil;
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        leaveGame(event.getPlayer());
    }

    @EventHandler
    public void onServerTickStartEvent(ServerTickStartEvent event) {
        int tickNumber = event.getTickNumber();

        Collection<Player> playerCollection = (Collection<Player>) AmongUs.INSTANCE.getServer().getOnlinePlayers();
        if (tickNumber % 4 == 0)
            for (Player player: playerCollection) {
                if (player.getWorld().getName().equals("lobby")) {
                    Location location = player.getLocation().toCenterLocation();
                    if (location.getBlockX() >= 8 && location.getBlockX() <= 14 && location.getBlockZ() >= -3 && location.getBlockZ() <= 2)
                        joinGame(player);
                }

                if (player.getWorld().getName().equals("skeld")) {
                    Location location = player.getLocation().clone();
                    location.setY(0);
                    if (!impostorManager.isEmergency())
                        switch (location.getBlock().getType()) {
                            case WHITE_CONCRETE:
                                player.sendActionBar("§2Your current location: CAFETERIA");
                                break;
                            case ORANGE_CONCRETE:
                                player.sendActionBar("§2Your current location: HALLWAY BETWEEN CAFETERIA, MEDBAY AND UPPER ENGINE");
                                break;
                            case MAGENTA_CONCRETE:
                                player.sendActionBar("§2Your current location: MEDBAY");
                                break;
                            case LIGHT_BLUE_CONCRETE:
                                player.sendActionBar("§2Your current location: UPPER ENGINE");
                                break;
                        }
                }
            }

        if (tickNumber % 2 == 0) {
            ArrayList<Player> livingCrewmateList = crewmateList;
            livingCrewmateList.removeAll(ghostList);
            int livingCrewmateCount = livingCrewmateList.size();
            ArrayList<Player> livingImpostorList = impostorList;
            livingImpostorList.removeAll(ghostList);
            int livingImpostorCount = livingImpostorList.size();

            if ((livingCrewmateCount - livingImpostorCount) <= 0)
                endGame(false);

            int count = 0;
            for (SkeldTaskManager taskManager: taskmanagerList)
                count += taskManager.getFullTaskList().size();

            if (count == 0)
                endGame(true);
        }
    }
}
