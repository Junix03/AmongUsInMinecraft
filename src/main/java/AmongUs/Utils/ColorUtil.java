package AmongUs.Utils;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ColorUtil {

    public enum ColorEnum {
        RED, BLUE, GREEN, PINK, ORANGE, YELLOW, BLACK, WHITE, PURPLE, BROWN, CYAN, LIME
    }

    private final ArrayList<Player> playerList = new ArrayList<>();
    private final ArrayList<ColorEnum> colorList = new ArrayList<>();

    public void addPlayer(Player player) {
        playerList.add(player);

        ArrayList<ColorEnum> tempColorList = new ArrayList<>();
        Collections.addAll(tempColorList, ColorEnum.values());
        tempColorList.removeAll(colorList);

        Random random = new Random();
        colorList.add(tempColorList.get(random.nextInt(tempColorList.size())));

        changeClothes(player);
    }

    public void changeColor(Player player, ColorEnum colorEnum) {
        if (colorList.contains(colorEnum) || !playerList.contains(player)) {
            player.sendActionBar("§lCouldn't change color!");
            return;
        }

        int index = playerList.indexOf(player);
        colorList.set(index, colorEnum);

        changeClothes(player);
    }

    public void removePlayer(Player player) {
        if (!playerList.contains(player))
            return;

        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);

        int index = playerList.indexOf(player);
        playerList.remove(index);
        colorList.remove(index);
    }

    private Color getColor(ColorEnum colorEnum) {
        switch (colorEnum) {
            case BLACK: return Color.BLACK;
            case BLUE: return Color.BLUE;
            case BROWN: return Color.fromRGB(127,63,0);
            case CYAN: return Color.AQUA;
            case GREEN: return Color.GREEN;
            case LIME: return Color.LIME;
            case ORANGE: return Color.ORANGE;
            case PINK: return Color.FUCHSIA;
            case PURPLE: return Color.PURPLE;
            case RED: return Color.RED;
            case WHITE: return Color.WHITE;
            case YELLOW: return Color.YELLOW;
        }
        return null;
    }

    public Color getPlayerColor(Player player) {
        int index = playerList.indexOf(player);
        return getColor(colorList.get(index));
    }

    public ArrayList<ColorEnum> getColorList() {
        return colorList;
    }

    private void changeClothes(Player player) {
        ItemStack itemStack = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta itemMeta = (LeatherArmorMeta) itemStack.getItemMeta();
        itemMeta.setColor(getColor(colorList.get(playerList.indexOf(player))));
        itemMeta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
        itemStack.setItemMeta(itemMeta);
        player.getInventory().setHelmet(itemStack);

        itemStack = new ItemStack(Material.LEATHER_CHESTPLATE);
        itemMeta = (LeatherArmorMeta) itemStack.getItemMeta();
        itemMeta.setColor(getColor(colorList.get(playerList.indexOf(player))));
        itemMeta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
        itemStack.setItemMeta(itemMeta);
        player.getInventory().setChestplate(itemStack);
    }
}
