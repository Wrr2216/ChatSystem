package space.wirr.chatsystem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/**
 * This project "ChatSystem" was created by Logan on 5/12/2017.
 */
public class EventsHandler implements Listener {
    Plugin pl = Main.getPlugin();
    Main main = new Main();
    Utility util = new Utility();

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        main.isEnabled.put(player, false);

        //final int number = genOrbNumber(player);

        //oNumbers.getYaml().set("Numbers." + player.getUniqueId().toString(), number);
        //oNumbers.modifyYaml();
        //oNumbers.saveYaml();

        PlayerConfig pConfig = PlayerConfig.getConfig(player);
        pConfig.addDefault("Data.name", player.getDisplayName());
        // String something = pConfig.getString("something.something.idk");
        pConfig.addDefault("Data.age", "n/a");
        pConfig.addDefault("Data.gender", "n/a");
        //pConfig.addDefault("Data.orbNumber", number);
        pConfig.save();

        ItemStack orb = new ItemStack(OrbHandler.OrbOfComms());
        if (!player.getInventory().contains(orb))
            player.getInventory().addItem(orb);


    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        if (event.getPlayer().isBanned()) {
            main.RemovePlayer(player);
        }
        main.RemovePlayer(player);
        PlayerConfig pConfig = PlayerConfig.getConfig(player);
        pConfig.discard();

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        PlayerConfig pConfig = PlayerConfig.getConfig(player);
        pConfig.discard();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (pl.getConfig().getBoolean("Chat.globalchat")) {
            if (main.isEnabled.get(player)) {

                Double lranges = pl.getConfig().getDouble("Chat.ranges.local");
                if (pl.getConfig().getBoolean("Chat.emotes") && util.cleanseString(event.getMessage())) {
                    player.sendMessage(ChatColor.RED + "Please do not use emotes!");
                    event.setCancelled(true);
                } else {
                    player.sendMessage(util.getStr(1, player.getDisplayName()) + event.getMessage());
                    for (Entity e : player.getNearbyEntities(lranges, lranges, lranges)) {
                        e.sendMessage(util.getStr(1, player.getDisplayName()) + event.getMessage());
                    }
                    util.Log(player.getName() + ": " + event.getMessage(), "local.log");

                    event.setCancelled(true);
                }
            }
        } else if (!pl.getConfig().getBoolean("Chat.globalchat")) {
            Double lrange = pl.getConfig().getDouble("Chat.ranges.local");
            if (pl.getConfig().getBoolean("Chat.emotes") && util.cleanseString(event.getMessage())) {
                player.sendMessage(ChatColor.RED + "Please do not use emotes!");
                event.setCancelled(true);
            } else {
                player.sendMessage(util.getStr(1, player.getDisplayName()) + event.getMessage());
                for (Entity e : player.getNearbyEntities(lrange.doubleValue(), lrange.doubleValue(),
                        lrange.doubleValue())) {
                    e.sendMessage(util.getStr(1, player.getDisplayName()) + event.getMessage());
                }
                util.Log(player.getName() + ": " + event.getMessage(), "local.log");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void OnInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        Action action = e.getAction();

        if (!(action == Action.RIGHT_CLICK_AIR) || (action == Action.RIGHT_CLICK_BLOCK)) return;

        if (player.getItemInHand().getType().equals(Material.EYE_OF_ENDER)) {
            if (player.getItemInHand().getItemMeta().getDisplayName() == null) {
                e.setCancelled(false);
            } else {
                e.setCancelled(true);
                player.openInventory(OrbHandler.OrbInterface("OrbInterface"));
            }

        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void CancelOrbThrow(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (player.getItemInHand().getType().equals(Material.EYE_OF_ENDER)) {
                if (player.getItemInHand().getItemMeta().getDisplayName() == null) {
                    e.setCancelled(false);
                } else {
                    e.setCancelled(true);
                }

            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void doInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() == null) return;
        ItemStack cStack = event.getCurrentItem();
        Inventory inv = event.getInventory();
        if (inv.getTitle().equalsIgnoreCase("orbinterface")) {
            switch (cStack.getItemMeta().getDisplayName().toLowerCase()) {
                case "answer":
                    event.setCancelled(true);
                    player.sendMessage("Answer");
                    OrbHandler.doAnswer(player);
                    break;
                case "end":
                    event.setCancelled(true);
                    player.sendMessage("End");
                    OrbHandler.doEnd(player);
                    break;
                case "contacts":
                    event.setCancelled(true);
                    OrbHandler.con(player);
                    break;
                default:
                    event.setCancelled(true);
                    player.sendMessage("Something went wrong.");
                    break;
            }
        } else if (inv.getTitle().equalsIgnoreCase("contacts")) {
            if (cStack.getType() == Material.SKULL_ITEM) {
                event.setCancelled(true);
                Player ply = pl.getServer().getPlayer(cStack.getItemMeta().getDisplayName());
                OrbHandler.doCall(player, ply);

            }
        } else {
            return;
        }


    }
}
