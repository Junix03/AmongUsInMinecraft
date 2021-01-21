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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.EulerAngle;

import java.util.ArrayList;
import java.util.Collection;

public class SkeldGameManager implements Listener {

    private final static String PREFIX = "§2[Among Us | The Skeld]: §r";
    public static SkeldGameManager INSTANCE;

    private final ArrayList<Player> playerList = new ArrayList<>();
    private final ColorUtil colorUtil = new ColorUtil();
    private boolean isOpen = true;
    private final SkeldSettings settings = new SkeldSettings();

    private final ArrayList<Player> crewmateList = new ArrayList<>();
    private final ArrayList<Player> impostorList = new ArrayList<>();
    private final ArrayList<SkeldTaskManager> taskmanagerList = new ArrayList<>();

    private final ArrayList<Player> ghostList = new ArrayList<>();
    private final SkeldImpostorManager impostorManager = new SkeldImpostorManager();
    private final ArrayList<ArmorStand> corpsesList = new ArrayList<>();

    private boolean isMeeting = false;

    // Locations
    private final Cafeteria cafeteria = new Cafeteria();

    public SkeldGameManager() {
        this.init();
    }

    private void init() {
        INSTANCE = this;
        AmongUs.registerCommand("skeld", new SkeldCommand());
    }

    public void startGame(Player player) {
        if (!isHost(player)) {
            player.sendTitle(new Title("", "§cYou are not the Host!", 20, 60, 20));
            return;
        }

        isOpen = false;
        setLobbyEntrance(false);
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

            if ((livingCrewmateCount - livingImpostorCount) <= 0)
                endGame(2);
            else
                endGame(1);
        }
    }

    public void endGame(int winner) {
        boolean crewmateWin = true;
        if (winner == 2)
            crewmateWin = false;
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

    public boolean isCrewmate(Player player) {
        return crewmateList.contains(player);
    }

    public boolean isImpostor(Player player) {
        return impostorList.contains(player);
    }

    public void addImpostor(Player player) {
        impostorList.add(player);
    }

    public ArrayList<Player> getImpostor() { return impostorList; }

    public boolean isOpen() {
        return isOpen;
    }

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
                endGame(2);

            int count = 0;
            for (SkeldTaskManager taskManager: taskmanagerList)
                count += taskManager.getFullTaskList().size();

            if (count == 0)
                endGame(1);
        }
    }
}
