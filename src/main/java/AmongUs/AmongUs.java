package AmongUs;

import AmongUs.Skeld.SkeldGameManager;
import AmongUs.Utils.UniversalListener;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public final class AmongUs extends JavaPlugin {

    public final static String PREFIX = "§a[Among Us]: §r";
    public static AmongUs INSTANCE;

    private SkeldGameManager skeldGameManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.init();
    }

    private void init() {
        INSTANCE = this;

        // Lobby
        getServer().createWorld(new WorldCreator("lobby"));
        getServer().getPluginManager().registerEvents(new UniversalListener(), this);

        // The Skeld
        getServer().createWorld(new WorldCreator("skeld"));
        skeldGameManager = new SkeldGameManager();
        getServer().getPluginManager().registerEvents(skeldGameManager, this);
        skeldGameManager.setLobbyEntrance(true);

        // Mira HQ
        getServer().createWorld(new WorldCreator("mira"));

        // Polus
        getServer().createWorld(new WorldCreator("polus"));

        log("§aPlugin enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        log("§cPlugin disabled");
    }

    public void log(String log) {
        Bukkit.getConsoleSender().sendMessage(PREFIX + log);
    }

    public static void registerCommand(String command, CommandExecutor commandExecutor) {
        Bukkit.getPluginCommand(command).setExecutor(commandExecutor);
    }
}
