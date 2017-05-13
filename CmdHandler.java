package space.wirr.chatsystem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

/**
 * This project "ChatSystem" was created by Logan on 5/12/2017.
 */
public class CmdHandler implements Listener, CommandExecutor {

    Plugin pl = Main.getPlugin();
    Main main = new Main();
    Utility util = new Utility();

    //region Commands
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;
        String cmdName = cmd.getName().toLowerCase();
        PlayerConfig pConfig = PlayerConfig.getConfig(player);

        // String str1;
        switch (cmdName) {
            case "rpmessage":
                cmdPm(player, pConfig, args);
                return true;
            case "rpinfo":
                cmdInfo(player);
                return true;
            case "l":
                cmdToggleLocal(player);
                return true;
            case "s":
                cmdShout(player, pConfig, args);
                return true;
            case "w":
                cmdWhisper(player, pConfig, args);
                return true;
            case "do":
                cmdDo(player, pConfig, args);
                return true;
            case "me":
                cmdMe(player, pConfig, args);
                return true;
            case "ooc":
                cmdOoc(player, pConfig, args);
                return true;
            case "rpreload":
                cmdReload(player);
                return true;
            case "rpid":
                cmdRPID(player, args);
                return true;
            case "rpconfig":
                cmdConfig(player, args);
                return true;
            case "call":
                OrbHandler.doCall(player, args);
                return true;
        }
        return false;
    }

    public void cmdToggleLocal(CommandSender sender) {
        if (!(sender instanceof Player)) {
            return;
        }
        Player player = (Player) sender;
        if (!pl.getConfig().getBoolean("Chat.globalchat")) {
            sender.sendMessage("Local chat is already on!");
            return;
        }
        if (main.isEnabled.containsKey(player) && (main.isEnabled.get(player))) {
            main.isEnabled.put(player, false);
            util.Log(player.getName() + " toggled Local chat off", "local.log");
            sender.sendMessage("Local chat is now " + ChatColor.RED + "OFF");
        } else {
            main.isEnabled.put(player, true);
            util.Log(player.getName() + " toggled Local chat on", "local.log");
            sender.sendMessage("Local chat is now " + ChatColor.GREEN + "ON");
        }
    }

    public void cmdConfig(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            return;
        }
        if (!sender.isOp()) {
            return;
        }

        Player player = (Player) sender;

        if (args.length == 0) {// var = args[0]
            player.sendMessage("Correct format: /rpconfig <var> <value>");
            return;
        }

        String message = "";
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        message = sb.toString().trim();
        if (message == "") {
            return;
        }

        switch (args[0]) {
            case "ranges.local":
                pl.getConfig().set("Chat.ranges.local", Integer.parseInt(message));
                break;
            case "ranges.shout":
                pl.getConfig().set("Chat.ranges.shout", Integer.parseInt(message));
                break;
            case "ranges.whisper":
                pl.getConfig().set("Chat.ranges.whisper", Integer.parseInt(message));
                break;
            case "ranges.pm":
                pl.getConfig().set("Chat.ranges.pm", Integer.parseInt(message));
                break;
            case "ranges.me":
                pl.getConfig().set("Chat.ranges.me", Integer.parseInt(message));
                break;
            case "ranges.do":
                pl.getConfig().set("Chat.ranges.do", Integer.parseInt(message));
                break;
            case "ranges.ooc":
                pl.getConfig().set("Chat.ranges.ooc", Integer.parseInt(message));
                break;
            case "globalchat":
                pl.getConfig().set("Chat.globalchat", Boolean.parseBoolean(message));
                break;
            case "pmlocal":
                pl.getConfig().set("Chat.pmlocal", Boolean.parseBoolean(message));
                break;
            case "emotes":
                pl.getConfig().set("Chat.emotes", Boolean.parseBoolean(message));
                break;
            case "serious":
                pl.getConfig().set("Chat.serious", Boolean.parseBoolean(message));
                break;
        }
        sender.sendMessage(ChatColor.WHITE + "You've changed " + ChatColor.GREEN + "Chat." + args[0] + ChatColor.WHITE
                + " to " + ChatColor.RED + message);
        pl.saveConfig();

    }

    public void cmdInfo(CommandSender sender) {
        if (!(sender instanceof Player)) {
            return;
        }
        Player player = (Player) sender;
        if (!player.isOp()) {
            return;
        }
        sender.sendMessage("[]=======================================[]");
        sender.sendMessage("         Local ranges: " + pl.getConfig().getDouble("Chat.ranges.local"));
        sender.sendMessage("         Shout ranges: " + pl.getConfig().getDouble("Chat.ranges.shout"));
        sender.sendMessage("         Whisper ranges: " + pl.getConfig().getDouble("Chat.ranges.whisper"));
        sender.sendMessage("         PM ranges: " + pl.getConfig().getDouble("Chat.ranges.pm"));
        sender.sendMessage("         Me ranges: " + pl.getConfig().getDouble("Chat.ranges.me"));
        sender.sendMessage("         Do ranges: " + pl.getConfig().getDouble("Chat.ranges.do"));
        sender.sendMessage("         OOC ranges: " + pl.getConfig().getDouble("Chat.ranges.ooc"));
        sender.sendMessage("         Global Chat: " + pl.getConfig().getBoolean("Chat.globalchat"));
        sender.sendMessage("         PM Local: " + pl.getConfig().getBoolean("Chat.pmlocal"));
        sender.sendMessage("         Emotes: " + pl.getConfig().getBoolean("Chat.emotes"));
        sender.sendMessage("         Serious: " + pl.getConfig().getBoolean("Chat.serious"));
        sender.sendMessage("[]=======================================[]");
        sender.sendMessage(
                "You can change these values using the /rpconfig command or the config.yml in your plugins directory.");
        util.Log(player.getName() + " requested information on the config.", "admincmds.log");
    }

    public void cmdWhisper(CommandSender sender, PlayerConfig pCfg, String[] args) {
        if (!(sender instanceof Player)) {
            return;
        }
        Player player = (Player) sender;
        String Name = pCfg.getString("Data.name");

        String message = "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        message = sb.toString().trim();
        if (message == "") {
            return;
        }
        Double wranges = pl.getConfig().getDouble("Chat.ranges.whisper");

        if (pl.getConfig().getBoolean("Chat.serious")) {
            player.sendMessage(util.getStr(2, player.getDisplayName()) + " " + message);
            player.sendMessage(ChatColor.GREEN + "*" + Name + " whispers " + message + "*");
            for (Entity e : player.getNearbyEntities(wranges.doubleValue(), wranges.doubleValue(), wranges.doubleValue())) {
                e.sendMessage(ChatColor.GREEN + "*" + Name + " whispers " + message + "*");
            }
        } else {
            player.sendMessage(util.getStr(2, player.getDisplayName()) + " " + message);
            for (Entity e : player.getNearbyEntities(wranges.doubleValue(), wranges.doubleValue(), wranges.doubleValue())) {
                e.sendMessage(util.getStr(2, player.getDisplayName()) + " " + message);
            }
        }

        util.Log(player.getName() + " sent a whisper ('" + message + "')", "whispers.log");
    }

    public void cmdShout(CommandSender sender, PlayerConfig pCfg, String[] args) {
        if (!(sender instanceof Player)) {
            return;
        }
        Player player = (Player) sender;
        String Name = pCfg.getString("Data.name");

        String message = "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        message = sb.toString().trim();
        if (message == "") {
            return;
        }
        Double sranges = pl.getConfig().getDouble("Chat.ranges.shout");

        if (pl.getConfig().getBoolean("Chat.serious")) {
            if (message.contains("!")) {
                player.sendMessage(ChatColor.YELLOW + "*" + Name + " shouts " + message.toUpperCase() + "*");
                for (Entity e : player.getNearbyEntities(sranges.doubleValue(), sranges.doubleValue(), sranges.doubleValue())) {
                    e.sendMessage(ChatColor.YELLOW + "*" + Name + " shouts " + message.toUpperCase() + "*");
                }
            } else {
                player.sendMessage(ChatColor.YELLOW + "*" + Name + " shouts " + message.toUpperCase() + "!*");
                for (Entity e : player.getNearbyEntities(sranges.doubleValue(), sranges.doubleValue(), sranges.doubleValue())) {
                    e.sendMessage(ChatColor.YELLOW + "*" + Name + " shouts " + message.toUpperCase() + "!*");
                }
            }

        } else {
            player.sendMessage(util.getStr(3, player.getDisplayName()) + " " + message);
            for (Entity e : player.getNearbyEntities(sranges.doubleValue(), sranges.doubleValue(), sranges.doubleValue())) {
                e.sendMessage(util.getStr(3, player.getDisplayName()) + " " + message);
            }
        }


        util.Log(player.getName() + " sent a shout ('" + message + "')", "shouts.log");
    }

    public void cmdMe(CommandSender sender, PlayerConfig pCfg, String[] args) {
        if (!(sender instanceof Player)) {
            return;
        }
        Player player = (Player) sender;
        String Name = pCfg.getString("Data.name");

        String message = "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        message = sb.toString().trim();
        if (Objects.equals(message, "")) {
            return;
        }
        Double ranges = pl.getConfig().getDouble("Chat.ranges.me");
        String fMessage = ChatColor.DARK_PURPLE + "* [" + player.getName() + "] " + message + " *";

        if (pl.getConfig().getBoolean("Chat.serious")) {
            player.sendMessage(ChatColor.DARK_PURPLE + "*" + Name + " " + message + "*");
            for (Entity e : player.getNearbyEntities(ranges.doubleValue(), ranges.doubleValue(), ranges.doubleValue())) {
                e.sendMessage(ChatColor.DARK_PURPLE + "*" + Name + " " + message + "*");
            }
        } else {
            player.sendMessage(fMessage);
            for (Entity e : player.getNearbyEntities(ranges.doubleValue(), ranges.doubleValue(), ranges.doubleValue())) {
                e.sendMessage(fMessage);
            }
        }


        util.Log(player.getName() + " sent a me ('" + message + "')", "mes.log");
    }

    public void cmdDo(CommandSender sender, PlayerConfig pCfg, String[] args) {
        if (!(sender instanceof Player)) {
            return;
        }
        Player player = (Player) sender;
        String Name = pCfg.getString("Data.name");

        String message = "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        message = sb.toString().trim();
        if (message == "") {
            return;
        }
        Double ranges = pl.getConfig().getDouble("Chat.ranges.do");
        String fMessage = ChatColor.DARK_AQUA + "*" + message + " * [" + player.getName() + "]";

        if (pl.getConfig().getBoolean("Chat.serious")) {
            player.sendMessage(ChatColor.DARK_AQUA + "*" + Name + message + "*");
            for (Entity e : player.getNearbyEntities(ranges.doubleValue(), ranges.doubleValue(), ranges.doubleValue())) {
                e.sendMessage(ChatColor.DARK_AQUA + "*" + Name + message + "*");
            }
        } else {
            player.sendMessage(fMessage);
            for (Entity e : player.getNearbyEntities(ranges.doubleValue(), ranges.doubleValue(), ranges.doubleValue())) {
                e.sendMessage(fMessage);
            }
        }


        util.Log(player.getName() + " sent a do ('" + message + "')", "dos.log");
    }

    // TODO: Maybe implement a different OOC variant for 'SeriousRP' mode.
    public void cmdOoc(CommandSender sender, PlayerConfig pCfg, String[] args) {
        if (!(sender instanceof Player)) {
            return;
        }
        Player player = (Player) sender;
        String Name = pCfg.getString("Data.name");

        String message = "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        message = sb.toString().trim();
        if (message == "") {
            return;
        }
        Double ranges = pl.getConfig().getDouble("Chat.ranges.ooc");
        String fMessage = ChatColor.DARK_GRAY + "[" + ChatColor.RED + "OOC" + ChatColor.DARK_GRAY + "] "
                + ChatColor.WHITE + "<" + player.getDisplayName() + "> ((" + message + "))";

        if (pl.getConfig().getBoolean("Chat.serious")) {
            player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "OOC" + ChatColor.DARK_GRAY + "] "
                    + ChatColor.WHITE + "<" + Name + "> ((" + message + "))");
            for (Entity e : player.getNearbyEntities(ranges.doubleValue(), ranges.doubleValue(), ranges.doubleValue())) {
                e.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "OOC" + ChatColor.DARK_GRAY + "] "
                        + ChatColor.WHITE + "<" + Name + "> ((" + message + "))");
            }
        } else {
            player.sendMessage(fMessage);
            for (Entity e : player.getNearbyEntities(ranges.doubleValue(), ranges.doubleValue(), ranges.doubleValue())) {
                e.sendMessage(fMessage);
            }
        }


        util.Log(player.getName() + " sent a ooc ('" + message + "')", "ooc.log");
    }

    // Reload plugin config files.
    public void cmdReload(CommandSender sender) {
        if (!(sender instanceof Player)) {
            return;
        }
        Player player = (Player) sender;
        if (player.isOp()) {
            pl.reloadConfig();
            util.Log(player.getName() + " reloaded the config files.", "admincmds.log");
        }
        pl.reloadConfig();
    }

    public void cmdToggleGlobal(CommandSender sender) {
        if (!(sender instanceof Player)) {
            return;
        }
        if (!sender.isOp()) {
            return;
        }
        Player player = (Player) sender;
        for (Player p : Bukkit.getOnlinePlayers()) {
            main.isEnabled.put(p, Boolean.valueOf(false));
        }
        if (pl.getConfig().getBoolean("Chat.globalchat")) {
            util.Log(player.getName() + " toggled global chat off.", "admincmds.log");
            pl.getConfig().set("Chat.globalchat", Boolean.valueOf(false));
            pl.saveConfig();
            sender.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Local Chat" + ChatColor.WHITE + "] "
                    + "Global Chat is now " + ChatColor.RED + "OFF");
        } else if (!pl.getConfig().getBoolean("Chat.globalchat")) {
            util.Log(player.getName() + " toggled global chat on.", "toggles.log");
            pl.getConfig().set("Chat.globalchat", Boolean.valueOf(true));
            pl.saveConfig();
            sender.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "Local Chat" + ChatColor.WHITE + "] "
                    + "Global Chat is now " + ChatColor.GREEN + "ON");
        }
    }

    public void cmdPm(CommandSender sender, PlayerConfig pCfg, String[] args) {
        if (!(sender instanceof Player)) {
            return;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage("Correct format: <player> <message>");
            return;
        }
        Player target = sender.getServer().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(args[0] + " is not currently online.");
            return;
        }

        String Name = pCfg.getString("Data.name");

        //TODO: Implement Serious/Non-Serious PM
        if (pl.getConfig().getBoolean("Chat.serious")) {
            ItemStack key = new ItemStack(Material.EYE_OF_ENDER);
            if (player.getItemInHand().equals(key)) {
                String message = "";
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    sb.append(args[i]).append(" ");
                }
                message = sb.toString().trim();
                if (message == "") {
                    return;
                }
                String fMessage = ChatColor.DARK_GRAY + "[" + ChatColor.RED + "SMS" + ChatColor.DARK_GRAY + "] "
                        + ChatColor.WHITE + Name + ": " + message;
                if (pl.getConfig().getBoolean("Chat.pmlocal")) {
                    int prange = 10;
                    for (Entity e : player.getNearbyEntities(prange, prange, prange)) {
                        e.sendMessage(Name + ": " + message);
                    }
                }
                util.Log(player.getName() + "(" + Name + ") sent an SMS to " + target.getName() + ": ('" + message + "')",
                        "privatemessages.log");

                player.sendMessage(fMessage);
                target.sendMessage(fMessage);
            } else {
                player.sendMessage("You must be holding an Eye of Ender to message this person.");
            }
        } else {
            String message = "";
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                sb.append(args[i]).append(" ");
            }
            message = sb.toString().trim();
            if (message == "") {
                return;
            }

            String fMessage = ChatColor.DARK_GRAY + "[" + ChatColor.RED + "PM" + ChatColor.DARK_GRAY + "] "
                    + ChatColor.WHITE + player.getDisplayName() + ": " + message;
            if (pl.getConfig().getBoolean("Chat.pmlocal")) {
                int prange = 10;
                for (Entity e : player.getNearbyEntities(prange, prange, prange)) {
                    e.sendMessage(player.getDisplayName() + ": " + message);
                }
            }
            util.Log(player.getName() + " sent a PM to " + target.getName() + ": ('" + message + "')",
                    "privatemessages.log");
            player.sendMessage(fMessage);
            target.sendMessage(fMessage);
        }


    }

    public void cmdRPID(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            return;
        }
        Player player = (Player) sender;

        if (args[0] == null) {
            player.sendMessage("Correct usage: /rpid set <attribute> <value> or /rpid show");
            return;
        }

        String cmd = args[0];

        PlayerConfig pConfig = PlayerConfig.getConfig(player);

        String Name = pConfig.getString("Data.name");
        String Age = pConfig.getString("Data.age");
        String Gender = pConfig.getString("Data.gender");
        //String oNumber = pConfig.getString("Data.orbNumber");

        switch (cmd) {
            case "set":
                RPHandler.rpid_set(player, args);
                break;
            case "show":
                RPHandler.rpid_show(player, args);
                break;
        }

    }
    //endregion

}
