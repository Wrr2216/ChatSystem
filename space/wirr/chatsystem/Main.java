package space.wirr.chatsystem;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

/**
 * This project "ChatSystem" was created by Logan Miller (WiRR) on 4/14/2017.
 */

/*
TEMP ChangeLog:
Add Serious RP Mode - 04/15/17
Refactored Commands - 04/26/17
Orb stuff - 05/01/17
Refactored Util functions - 05/04/17

*/
public class Main extends JavaPlugin implements Listener {
    private static Plugin plugin;

    HashMap<Player, Boolean> isEnabled = new HashMap<Player, Boolean>();
    Utility util = new Utility();
    private HashMap<UUID, UUID> chat;

    public static Plugin getPlugin() {
        return plugin;
    }

    // Server startup
    @Override
    public void onEnable() {
        plugin = this;

        util.LogMessage("Plugin Initialized");
        getServer().getPluginManager().registerEvents(this, this);
        util.LogMessage("Loading config file");

        OrbHandler.oNumbers = new Configuration("orb-numbers.yml", this);
        chat = new HashMap<UUID, UUID>();

        // Run config  initialization
        initConfig();
    }

    // Server shutting down
    @Override
    public void onDisable() {
        plugin = null;
        util.LogMessage("Plugin Disabled");
        util.LogMessage("Saving config file");
        saveConfig();
        PlayerConfig.removeConfigs();
    }

    // Initialize Configuration
    private void initConfig() {
        // TODO: Add Emotes from file
        String[] dEmotes = {""};

        reloadConfig();

        getConfig().options().header("## Chat Config ##");
        getConfig().addDefault("Chat.ranges.local", 10);
        getConfig().addDefault("Chat.ranges.shout", 20);
        getConfig().addDefault("Chat.ranges.whisper", 4);
        getConfig().addDefault("Chat.ranges.pm", 10);
        getConfig().addDefault("Chat.ranges.me", 10);
        getConfig().addDefault("Chat.ranges.do", 10);
        getConfig().addDefault("Chat.ranges.ooc", 15);
        getConfig().addDefault("Chat.globalchat", true);
        getConfig().addDefault("Chat.pmlocal", true);
        getConfig().addDefault("Chat.emotes", false);
        getConfig().addDefault("Emotes.banned", dEmotes);

        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    // Get rid of player object (Credit: Idiolore; Implementation Idea)
    void RemovePlayer(Player player) {
        if (isEnabled.containsKey(player)) {
            isEnabled.remove(player);
        }
        if (wPlayer.players.containsKey(player)) {
            wPlayer.players.remove(player);
        }
    }


}