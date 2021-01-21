package AmongUs.Skeld.Commands;

import AmongUs.Skeld.SkeldGameManager;
import AmongUs.Skeld.Utils.SkeldSettings;
import AmongUs.Utils.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SkeldCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return true;
        Player player = (Player) sender;
        SkeldSettings settings = SkeldGameManager.INSTANCE.getSettings();

        if (args.length == 0)
            return true;

        switch (args[0]) {
            case "game":
                if (args.length < 2)
                    return true;
                switch (args[1]) {
                    case "start":
                        SkeldGameManager.INSTANCE.startGame(player);
                        break;
                    case "join":
                        SkeldGameManager.INSTANCE.joinGame(player);
                        break;
                    case "leave":
                        SkeldGameManager.INSTANCE.leaveGame(player);
                        break;
                }
                break;
            case "settings":
                if (args.length < 2) {
                    String string = "\n§a[Settings] imposter: " + settings.getImpostor() + "; ";
                    string += "Confirm Ejects: " + settings.isConfirmEjects() + "; ";
                    string += "# Emergency Meetings: " + settings.getEmergencyMeetings() + "; ";
                    string += "Anonymous Voting: " + settings.isAnonymousVoting() + "; ";
                    string += "Emergency Cooldown: " + settings.getEmergencyCooldown() + "; ";
                    string += "Discussion Time: " + settings.getDiscussionTime() + "; ";
                    string += "Voting Time: " + settings.getVotingTime() + "; ";
                    string += "Player Speed: " +settings.getPlayerSpeed() + "; ";
                    string += "Kill Cooldown: " + settings.getKillCooldown() + "; ";
                    string += "Kill Distance: " + settings.getKillDistance() + "; ";
                    string += "Visual Tasks: " + settings.isVisualTasks() + "; ";
                    string += "Task Bar Update: " + settings.getTaskBarUpdate() + "; ";
                    string += "# Common Tasks: " + settings.getCommonTasks() + "; ";
                    string += "# Long Tasks: " + settings.getLongTasks() + "; ";
                    string += "# Short Tasks" + settings.getShortTasks() + ";";

                    player.sendMessage(string);

                    return true;
                }
                switch (args[1]) {
                    case "impostor":
                        if (isNumeric(args[2], true) && inDistance(args[2], 1, 3, 1))
                            settings.setImpostor(Integer.parseInt(args[2]));
                        break;
                    case "recommendedsettings":
                        settings.recommendedSettings();
                        break;
                    case "confirmejects":
                    case "anonymousvoting":
                    case "visualtasks":
                        settings.setConfirmEjects(args[2].equals("on"));
                        break;
                    case "#emergencymeetings":
                        if (isNumeric(args[2], true) && inDistance(args[2], 1, 9, 1))
                            settings.setEmergencyMeetings(Integer.parseInt(args[2]));
                        break;
                    case "emergencycooldown":
                        if (isNumeric(args[2], true) && inDistance(args[2], 0, 60, 5))
                            settings.setEmergencyCooldown(Integer.parseInt(args[2]));
                        break;
                    case "discussiontime":
                        if (isNumeric(args[2], true) && inDistance(args[2], 0, 120, 15))
                            settings.setDiscussionTime(Integer.parseInt(args[2]));
                        break;
                    case "votingtime":
                        if (isNumeric(args[2], true) && inDistance(args[2], 0, 300, 15))
                            settings.setVotingTime(Integer.parseInt(args[2]));
                        break;
                    case "playerspeed":
                        if (isNumeric(args[2], false) && inDistance(args[2], 0.5, 3, 0.25))
                            settings.setPlayerSpeed(Double.parseDouble(args[2]));
                        break;
                    case "killcooldown":
                        if (isNumeric(args[2], false) && inDistance(args[2], 10, 60, 2.5))
                            settings.setKillCooldown(Double.parseDouble(args[2]));
                        break;
                    case "killdistance":
                        if (args[2].equals("short"))
                            settings.setKillDistance(1);
                        else if (args[2].equals("medium"))
                            settings.setKillDistance(2);
                        else
                            settings.setKillDistance(3);
                        break;
                    case "taskbarupdates":
                        if (args[2].equals("always"))
                            settings.setKillDistance(1);
                        else if (args[2].equals("meeting"))
                            settings.setKillDistance(2);
                        else
                            settings.setKillDistance(3);
                        break;
                    case "#commontasks":
                        if (isNumeric(args[2], true) && inDistance(args[1], 0, 2, 1))
                            settings.setCommonTasks(Integer.parseInt(args[1]));
                        break;
                    case "#longtasks":
                        if (isNumeric(args[2], true) && inDistance(args[1], 0, 3, 1))
                            settings.setLongTasks(Integer.parseInt(args[1]));
                        break;
                    case "#shorttasks":
                        if (isNumeric(args[2], true) && inDistance(args[1], 0, 5, 1))
                            settings.setShortTasks(Integer.parseInt(args[1]));
                }
                break;
            case "color":
                if (SkeldGameManager.INSTANCE.isPlayer(player))
                    SkeldGameManager.INSTANCE.getColorUtil().changeColor(player, ColorUtil.ColorEnum.valueOf(args[1].toUpperCase()));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> tabList = new ArrayList<>();

        switch (args.length) {
            case 1:
                if ("game".startsWith(args[0]))
                    tabList.add("game");
                if ("settings".startsWith(args[0]))
                    tabList.add("settings");
                if ("color".startsWith(args[0]))
                    tabList.add("color");
                break;
            case 2:
                switch (args[0]) {
                    case "game":
                        if ("start".startsWith(args[1]))
                            tabList.add("start");
                        if ("join".startsWith(args[1]))
                            tabList.add("join");
                        if ("leave".startsWith(args[1]))
                            tabList.add("leave");
                        break;
                    case "settings":
                        if ("impostor".contains(args[1]))
                            tabList.add("impostor");
                        if ("recommendedsettings".contains(args[1]))
                            tabList.add("recommendedSettings");
                        if ("confirmejects".contains(args[1]))
                            tabList.add("confirmEjects");
                        if ("#emergencymeetings".contains(args[1]))
                            tabList.add("#emergencyMeetings");
                        if ("anonymousvoting".contains(args[1]))
                            tabList.add("anonymousVoting");
                        if ("emergencycooldown".contains(args[1]))
                            tabList.add("emergencyCooldown");
                        if ("discussiontime".contains(args[1]))
                            tabList.add("discussionTime");
                        if ("votingtime".contains(args[1]))
                            tabList.add("votingTime");
                        if ("playerspeed".contains(args[1]))
                            tabList.add("playerSpeed");
                        if ("killcooldown".contains(args[1]))
                            tabList.add("killCooldown");
                        if ("killdistance".contains(args[1]))
                            tabList.add("killDistance");
                        if ("visualtasks".contains(args[1]))
                            tabList.add("visualTasks");
                        if ("taskbarupdates".contains(args[1]))
                            tabList.add("taskBarUpdates");
                        if ("#commontasks".contains(args[1]))
                            tabList.add("#commonTasks");
                        if ("#longtasks".contains(args[1]))
                            tabList.add("#longTasks");
                        if ("#shorttasks".contains(args[1]))
                            tabList.add("#shortTasks");
                        break;
                    case "color":
                        if ("red".contains(args[1]) && !SkeldGameManager.INSTANCE.getColorUtil().getColorList().contains(ColorUtil.ColorEnum.RED))
                            tabList.add("red");
                        if ("blue".contains(args[1]) && !SkeldGameManager.INSTANCE.getColorUtil().getColorList().contains(ColorUtil.ColorEnum.BLUE))
                            tabList.add("blue");
                        if ("green".contains(args[1]) && !SkeldGameManager.INSTANCE.getColorUtil().getColorList().contains(ColorUtil.ColorEnum.GREEN))
                            tabList.add("green");
                        if ("pink".contains(args[1]) && !SkeldGameManager.INSTANCE.getColorUtil().getColorList().contains(ColorUtil.ColorEnum.PINK))
                            tabList.add("pink");
                        if ("orange".contains(args[1]) && !SkeldGameManager.INSTANCE.getColorUtil().getColorList().contains(ColorUtil.ColorEnum.ORANGE))
                            tabList.add("orange");
                        if ("yellow".contains(args[1]) && !SkeldGameManager.INSTANCE.getColorUtil().getColorList().contains(ColorUtil.ColorEnum.YELLOW))
                            tabList.add("yellow");
                        if ("black".contains(args[1]) && !SkeldGameManager.INSTANCE.getColorUtil().getColorList().contains(ColorUtil.ColorEnum.BLACK))
                            tabList.add("black");
                        if ("white".contains(args[1]) && !SkeldGameManager.INSTANCE.getColorUtil().getColorList().contains(ColorUtil.ColorEnum.WHITE))
                            tabList.add("white");
                        if ("purple".contains(args[1]) && !SkeldGameManager.INSTANCE.getColorUtil().getColorList().contains(ColorUtil.ColorEnum.PURPLE))
                            tabList.add("purple");
                        if ("brown".contains(args[1]) && !SkeldGameManager.INSTANCE.getColorUtil().getColorList().contains(ColorUtil.ColorEnum.BROWN))
                            tabList.add("brown");
                        if ("cyan".contains(args[1]) && !SkeldGameManager.INSTANCE.getColorUtil().getColorList().contains(ColorUtil.ColorEnum.CYAN))
                            tabList.add("cyan");
                        if ("lime".contains(args[1]) && !SkeldGameManager.INSTANCE.getColorUtil().getColorList().contains(ColorUtil.ColorEnum.LIME))
                            tabList.add("lime");
                }
                break;
            case 3:
                if (args[0].equals("settings"))
                    switch (args[1]) {
                        case "impostor":
                            for (int i = 1; i <= 3; i++)
                                if (String.valueOf(i).contains(args[2]))
                                    tabList.add("" + i);
                            break;
                        case "confirmejects":
                        case "anonymousvoting":
                        case "visualtasks":
                            System.out.println("Testausgabe");
                            if ("on".contains(args[2])) tabList.add("on");
                            if ("off".contains(args[2])) tabList.add("off");
                            break;
                        case "#emergencymeetings":
                            for (int i = 1; i <= 9; i++)
                                if (String.valueOf(i).contains(args[2]))
                                    tabList.add("" + i);
                            break;
                        case "emergencycooldown":
                            for (int i = 0; i <= 12; i++)
                                if (String.valueOf(i * 5).contains(args[2]))
                                    tabList.add("" + (i * 5));
                            break;
                        case "discussiontime":
                            for (int i = 0; i <= 8; i++)
                                if (String.valueOf(i * 15).contains(args[2]))
                                    tabList.add("" + (i * 15));
                            break;
                        case "votingtime":
                            for (int i = 0; i <= 20; i++)
                                if (String.valueOf(i * 15).contains(args[2]))
                                    tabList.add("" + (i * 15));
                            break;
                        case "playerspeed":
                            for (int i = 2; i <= 12; i++)
                                if (String.valueOf(i * 0.25).contains(args[2]))
                                    tabList.add("" + (i * 0.25));
                            break;
                        case "killcooldown":
                            for (int i = 4; i <= 12; i++)
                                if (String.valueOf(i * 0.25).contains(args[2]))
                                    tabList.add("" + (i * 0.25));
                            break;
                        case "killdistance":
                            if ("short".contains(args[2])) tabList.add("short");
                            if ("medium".contains(args[2])) tabList.add("medium");
                            if ("long".contains(args[2])) tabList.add("long");
                            break;
                        case "taskbarupdate":
                            if ("always".contains(args[2])) tabList.add("always");
                            if ("meetings".contains(args[2])) tabList.add("meetings");
                            if ("never".contains(args[2])) tabList.add("never");
                            break;
                        case "#commontasks":
                            for (int i = 0; i <= 2; i++)
                                if (String.valueOf(i).contains(args[2]))
                                    tabList.add("" + (i));
                            break;
                        case "#longtasks":
                            for (int i = 0; i <= 3; i++)
                                if (String.valueOf(i).contains(args[2]))
                                    tabList.add("" + (i));
                            break;
                        case "#shorttasks":
                            for (int i = 0; i <= 5; i++)
                                if (String.valueOf(i).contains(args[2]))
                                    tabList.add("" + (i));
                            break;
                    }
                break;
        }

        return tabList;
    }

    private boolean isNumeric(String string, boolean integer) {
        try {
            if (integer)
                Integer.parseInt(string);
            else
                Double.parseDouble(string);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    private boolean inDistance(String arg, double min, double max, double density) {
        if (Double.parseDouble(arg) >= min && Double.parseDouble(arg) <= max)
            return Double.parseDouble(arg) % density == 0;
        else
            return false;
    }
}
