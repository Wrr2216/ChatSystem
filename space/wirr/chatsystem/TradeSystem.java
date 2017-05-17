package space.wirr.chatsystem;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TradeSystem implements Listener {

    public void openWindow(Player p) {
        p.openInventory(TradeWindow1("test"));
    }
    private Inventory TradeWindow1(String title) {
        Inventory inv = Bukkit.createInventory(null, 57, title);
        ItemStack spl = new ItemStack(split());
        ItemStack acc = new ItemStack(accept());
        ItemStack dec = new ItemStack(decline());
        inv.setItem(0, spl);
        inv.setItem(1, spl);
        inv.setItem(2, acc);
        inv.setItem(3, dec);
        return inv;
    }

    public Material split()
    {
        ItemStack item = new ItemStack(Material.IRON_FENCE);
        ItemMeta meta = item.getItemMeta();
        item.setItemMeta(meta);
        return null;
    }

    public Material accept()
    {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)5);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Accept");
        item.setItemMeta(meta);
        return null;
    }

    public Material decline()
    {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Decline");
        item.setItemMeta(meta);
        return null;
    }

}
