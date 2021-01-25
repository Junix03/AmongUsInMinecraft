package AmongUs.Utils;

import AmongUs.AmongUs;
import AmongUs.Skeld.SkeldGameManager;
import com.destroystokyo.paper.Title;
import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class UniversalListener implements org.bukkit.event.Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        event.setJoinMessage("§f[§a+§f] §r§b" + event.getPlayer().getName() + " §r§bjoined the game");

        Player player = event.getPlayer();
        Location location = new Location(AmongUs.INSTANCE.getServer().getWorld("lobby"), 0.0, 10, -8.0, 0, 0);
        player.teleport(location);

        PotionEffect potionEffect = new PotionEffect(PotionEffectType.SATURATION, 999999, 255, false, false);
        player.addPotionEffect(potionEffect);

        Title title = new Title("Among Us in Minecraft", "Welcome on the server!", 40, 120, 40);
        player.sendTitle(title);

        player.getInventory().clear();
        if (AmongUs.INSTANCE.getServer().getOperators().contains(player)) {
            player.setGameMode(GameMode.CREATIVE);
            player.setFlying(false);
        } else
            player.setGameMode(GameMode.ADVENTURE);
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        event.setQuitMessage("§f[§c-§f] §r§b§l" + event.getPlayer().getName() + " §r§bleft the game");
    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        Location location = new Location(AmongUs.INSTANCE.getServer().getWorld("lobby"), 0.0, 10, -8.0, 0, 0);
        event.setRespawnLocation(location);
    }

    @EventHandler
    public void onServerTickStartEvent(ServerTickStartEvent event) {
        int tickNumber = event.getTickNumber();
        if (tickNumber % 200 == 0)
            for (World world: AmongUs.INSTANCE.getServer().getWorlds())
                world.save();
    }
}
