package space.wirr.chatsystem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * This project "ChatSystem" was created by Logan on 5/12/2017.
 */
class RPHandler {
    //region ID Card stuff
    static void rpid_set(Player player, String[] args) {
        PlayerConfig pConfig = PlayerConfig.getConfig(player);
        switch (args[1]) {
            case "name":
                pConfig.set("Data.name", args[2]);
                pConfig.save();
                player.sendMessage("Name updated.");
                return;
            case "age":
                pConfig.set("Data.age", args[2]);
                pConfig.save();
                player.sendMessage("Age updated.");
                return;
            case "gender":
                pConfig.set("Data.gender", args[2]);
                pConfig.save();
                player.sendMessage("Gender updated.");
        }

    }

    public static void rpid_show(Player player, String[] args) {
        PlayerConfig pConfig;

        // String Name = PlayerDataConfig.getString("Data.name");
        // String Age = PlayerDataConfig.getString("Data.age");
        // String Gender = PlayerDataConfig.getString("Data.gender");
        // String oNumber = PlayerDataConfig.getString("Data.orbNumber");

        if (args[1] != null) {
            Player target = Bukkit.getServer().getPlayer(args[1]);
            if (player.hasPermission("rpcsys.showidother") | player.isOp()) {
                pConfig = PlayerConfig.getConfig(target);

                player.sendMessage(ChatColor.AQUA + "= " + " I.D. Card for " + target.getDisplayName() + ChatColor.AQUA
                        + "  =" + "\n");
                player.sendMessage(ChatColor.AQUA + "| " + ChatColor.YELLOW + "Name: " + ChatColor.GREEN
                        + pConfig.getString("Data.name"));
                player.sendMessage(ChatColor.AQUA + "| " + ChatColor.YELLOW + "Age: " + ChatColor.GREEN
                        + pConfig.getString("Data.age"));
                player.sendMessage(ChatColor.AQUA + "| " + ChatColor.YELLOW + "Gender: " + ChatColor.GREEN
                        + pConfig.getString("Data.gender"));
                //player.sendMessage(ChatColor.AQUA + "| " + ChatColor.YELLOW + "Number: " + ChatColor.GREEN
                //      + pConfig.getString("Data.orbNumber"));
                player.sendMessage(ChatColor.AQUA + "=========================");
            } else {
                player.sendMessage("Insufficient permission.");
            }
        } else {
            pConfig = PlayerConfig.getConfig(player);

            player.sendMessage(ChatColor.AQUA + "= " + " I.D. Card for " + player.getDisplayName() + ChatColor.AQUA
                    + "  =" + "\n");
            player.sendMessage(ChatColor.AQUA + "| " + ChatColor.YELLOW + "Name: " + ChatColor.GREEN
                    + pConfig.getString("Data.name"));
            player.sendMessage(ChatColor.AQUA + "| " + ChatColor.YELLOW + "Age: " + ChatColor.GREEN
                    + pConfig.getString("Data.age"));
            player.sendMessage(ChatColor.AQUA + "| " + ChatColor.YELLOW + "Gender: " + ChatColor.GREEN
                    + pConfig.getString("Data.gender"));
            //player.sendMessage(ChatColor.AQUA + "| " + ChatColor.YELLOW + "Number: " + ChatColor.GREEN
            //      + pConfig.getString("Data.orbNumber"));
            player.sendMessage(ChatColor.AQUA + "=========================");
        }

    }
    //endregion
}
