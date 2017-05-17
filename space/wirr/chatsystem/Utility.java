package space.wirr.chatsystem;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * This project "ChatSystem" was created by Logan on 5/12/2017.
 */
class Utility {
    private Plugin pl = Main.getPlugin();

    //region Util Functions
    void LogMessage(String str) {
        if (str == null) return;
        pl.getLogger().info(str);
    }

    void Log(String message, String fname) {
        try {
            File dataFolder = new File("plugins" + File.separator + "ChatSystem" + File.separator + "logs");
            if (!dataFolder.exists()) {
                dataFolder.mkdir();
            }
            File saveTo = new File(dataFolder, fname);
            if (!saveTo.exists()) {
                
                saveTo.createNewFile();
            }
            FileWriter fw = new FileWriter(saveTo, true);

            Date dt = new Date();
            PrintWriter pw = new PrintWriter(fw);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = df.format(dt);
            pw.println("[" + time + "] " + message);

            pw.flush();

            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Boolean cleanseString(String input) {
        List<String> emotes = pl.getConfig().getStringList("Emotes.banned");

        Boolean ret = false;
        for (String emote : emotes)
            if (Objects.equals(input, emote)) {
                ret = true;
                LogMessage("Bad String");
            }
        LogMessage("Good String");
        return ret;
    }

    String getStr(int type, String name) {
        String result = "";
        if (type == 1) {
            result = ChatColor.WHITE + "[" + ChatColor.RED + "Local" + ChatColor.WHITE + "] " + name + ": ";
        } else if (type == 2) {
            result = ChatColor.WHITE + "[" + ChatColor.GREEN + "Whisper" + ChatColor.WHITE + "] " + name + ":";
        } else if (type == 3) {
            result = ChatColor.WHITE + "[" + ChatColor.YELLOW + "Shout" + ChatColor.WHITE + "] " + name + ":";
        } else if (type == 4) {
            result = ChatColor.WHITE + "[" + ChatColor.YELLOW + "Me" + ChatColor.WHITE + "] " + name + ":";
        } else if (type == 5) {
            result = ChatColor.WHITE + "[" + ChatColor.YELLOW + "Do" + ChatColor.WHITE + "] " + name + ":";
        } else if (type == 6) {
            result = ChatColor.WHITE + "[" + ChatColor.YELLOW + "OOC" + ChatColor.WHITE + "] " + name + ":";
        } else if (type == 7) {
            result = ChatColor.DARK_GRAY + "[" + ChatColor.RED + "OOC" + ChatColor.DARK_GRAY + "]";
        }
        return result;
    }
    //endregion
}
