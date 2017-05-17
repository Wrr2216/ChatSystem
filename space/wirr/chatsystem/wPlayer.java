package space.wirr.chatsystem;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * This project "RPChatSystem" was created by Logan on 5/9/2017.
 */
public class wPlayer {
    static final Map<Player, wPlayer> players = new HashMap<>();
    private Player player;
    private boolean isBeingCalled;
    private boolean isInCall;

    private wPlayer(Player player) {
        this.player = player;
    }

    static wPlayer getwPlayer(final Player player) {
        //noinspection Since15
        return players.getOrDefault(player, players.put(player, new wPlayer(player)));
    }

    boolean getIsBeingCalled() {
        return this.isBeingCalled;
    }

    void setIsBeingCalled(boolean value) {
        this.isBeingCalled = value;
    }

    boolean getisInCall() {
        return this.isInCall;
    }

    void setisInCall(boolean value) {
        this.isInCall = value;
    }

    public Player getPlayer() {
        return player;
    }

    void msg(String str) {
        player.sendMessage(ChatColor.GREEN + "[Orb] " + ChatColor.WHITE + str);
    }
}
