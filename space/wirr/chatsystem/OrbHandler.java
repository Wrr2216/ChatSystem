package space.wirr.chatsystem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

/**
 * This project "ChatSystem" was created by Logan on 5/12/2017.
 */
public class OrbHandler {
    static Configuration oNumbers;

    public static int getNumber(final Player player) {
        if (oNumbers.getYaml().contains("Numbers." + player.getUniqueId())) {
            return oNumbers.getYaml().getInt("Numbers." + player.getUniqueId() + ".Number");
        }
        return 0;
    }

    private static List<Integer> getAllNumbers() {
        final List<Integer> numbers = new ArrayList<>();
        final ConfigurationSection sec = oNumbers.getYaml().getConfigurationSection("Numbers");
        for (final String s : sec.getKeys(false)) {
            numbers.add(oNumbers.getYaml().getInt("Numbers." + s));
        }
        return numbers;
    }

    private static void genOrbNumber(Player player) {
        final Random rand = new Random();
        String numb = "";
        for (int i = 0; i < 4; ++i) {
            final int r = rand.nextInt(9) + 1;
            if (!Objects.equals(numb, "")) {
                numb = String.valueOf(numb) + String.valueOf(r);
            } else {
                numb = String.valueOf(r);
            }
        }
        final int number = Integer.parseInt(numb);
        if (getAllNumbers().contains(number)) {
            genOrbNumber(player);
        }
    }

    public static int getONumber(final Player player) {
        return oNumbers.getYaml().getInt("Numbers." + player.getUniqueId() + ".Number");
    }

    public static Player getPlayerFromNumber(final int number) {
        Player p = null;
        final ConfigurationSection sec = oNumbers.getYaml().getConfigurationSection("Numbers");
        for (final String s : sec.getKeys(false)) {
            if (oNumbers.getYaml().getInt("Numbers." + s + ".Number") == number) {
                p = Bukkit.getPlayer(UUID.fromString(s));
                return p;
            }
        }
        return null;
    }

    static void doCall(Player caller, String[] args) {
        Player pt = caller.getServer().getPlayer(args[0]);
        if (pt == null) {
            caller.sendMessage(args[0] + " is not currently online.");
            return;
        }

        wPlayer tPly = wPlayer.getwPlayer(pt);

        tPly.setIsBeingCalled(true);
        tPly.msg(caller.getDisplayName() + " is calling you.");
    }

    static void doCall(Player caller, Player target) {
        Player pt = target;
        if (pt == null) {
            caller.sendMessage(target + " is not currently online.");
            return;
        }

        wPlayer tPly = wPlayer.getwPlayer(pt);

        tPly.setIsBeingCalled(true);
        tPly.msg(caller.getDisplayName() + " is calling you.");
    }

    public static void doAnswer(Player p) {
        wPlayer tPly = wPlayer.getwPlayer(p);
        if (tPly.getIsBeingCalled()) {
            tPly.setIsBeingCalled(false);
            tPly.setisInCall(true);
            tPly.msg("Answered.");
        } else {
            tPly.msg("No incoming calls.");
        }
        p.closeInventory();
    }

    public static void doEnd(Player p) {
        wPlayer tPly = wPlayer.getwPlayer(p);
        if (tPly.getisInCall()) {
            tPly.setisInCall(false);
            tPly.msg("Call ended.");
        } else {
            tPly.msg("You're not in a call.");
        }
        p.closeInventory();
    }

    public static void con(Player p) {
        p.closeInventory();
        Inventory inv = Bukkit.createInventory(null, 27, "Contacts");
        int count = 0;
        for (Player pl : Bukkit.getOnlinePlayers()) {
            inv.setItem(0, Head(pl));
            count++;
        }
        p.openInventory(inv);
        //  ItemStack addcon = new ItemStack(addcon());
        //  inv.setItem(1, addcon);
    }

    public static Inventory OrbInterface(String title) {
        // Interface here
        //Inventory inv = Bukkit.createInventory(null, 27, "Orb");
        Inventory inv = Bukkit.createInventory(null, 9, title);
        ItemStack ans = new ItemStack(Answer());
        ItemStack end = new ItemStack(End());
        EnchantGlow.addGlow(ans);
        EnchantGlow.addGlow(end);
        inv.setItem(0, ans);
        inv.setItem(2, end);
        inv.setItem(1, new ItemStack(Head()));
        return inv;
    }

    public static ItemStack OrbOfComms() {
        ItemStack item = new ItemStack(Material.EYE_OF_ENDER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_AQUA + "Orb of Communication");
        item.setItemMeta(meta);
        EnchantGlow.addGlow(item);
        return item;
    }

    public static ItemStack Head() {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM);
        skull.setDurability((short) 3);
        SkullMeta sm = (SkullMeta) skull.getItemMeta();
        sm.setDisplayName("Contacts");
        skull.setItemMeta(sm);
        return skull;
    }

    public static ItemStack Head(Player p) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM);
        skull.setDurability((short) 3);
        SkullMeta sm = (SkullMeta) skull.getItemMeta();
        sm.setDisplayName(p.getName());
        if (!(p == null)) {
            sm.setOwner("" + p.getName());
        }
        skull.setItemMeta(sm);
        return skull;
    }

    public static ItemStack Answer() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Answer");
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack End() {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("End");
        item.setItemMeta(meta);

        return item;
    }

    // private ItemStack addcon()
    // {
    //   Wool wool = new Wool(DyeColor.LIME);
    //   ItemStack item = new ItemStack(Material.WOOL);
    //   ItemMeta meta = item.getItemMeta();
    //   meta.setDisplayName("Add Contact");
    //   item.setItemMeta(meta);
    //
    //   return item;
    //}
}
