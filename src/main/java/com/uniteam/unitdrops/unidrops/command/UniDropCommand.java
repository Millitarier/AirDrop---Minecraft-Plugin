package com.uniteam.unitdrops.unidrops.command;

import com.uniteam.unitdrops.unidrops.FUtils;
import com.uniteam.unitdrops.unidrops.UniDrops;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class UniDropCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!sender.hasPermission("*")) return false;
        switch(args[0].toLowerCase()) {
            case "auto":

                UniDrops.getInstance().getConfig().set("settings.plugin_enabled",UniDrops.getInstance().getConfig().getBoolean("settings.plugin_enabled") ? false : true);
                UniDrops.getInstance().saveConfig();
                UniDrops.getInstance().isEnabled = UniDrops.getInstance().getConfig().getBoolean("settings.plugin_enabled");

                ChatColor color = UniDrops.getInstance().isEnabled ? ChatColor.GREEN : ChatColor.RED;

                String text = UniDrops.getInstance().isEnabled ? UniDrops.getInstance().getConfig().getString("messages.plugin_enabled") : UniDrops.getInstance().getConfig().getString("messages.plugin_disabled");
                sender.sendMessage(color + text);
                break;

            case "drop":

                if(args.length < 3) {
                    UniDrops.getInstance().spawnUniDrop(false , 0 , 0, 0);
                    return true;
                }

                UniDrops.getInstance().spawnUniDrop(true, Integer.parseInt(args[1]) ,  Integer.parseInt(args[2]),Integer.parseInt(args[3]));



                break;
            case "clear":
                UniDrops.getInstance().getConfig().set("drops", "");
                UniDrops.getInstance().saveConfig();
                break;
            case "add":

                Player p = (Player) sender;

                Inventory inv = p.getInventory();
                if(FUtils.isInvEmpty(inv)) {
                    sender.sendMessage(UniDrops.getInstance().getConfig().getString("messages.airdrop_no_items"));
                }

                for(int i = 0; i < inv.getContents().length; i++) {
                    ItemStack item = inv.getItem(i);

                     if(item != null) {
                         System.out.println("drops." + i);
                         UniDrops.getInstance().getConfig().set("drops." + i + ".itemtype", item.getType().name());
                         UniDrops.getInstance().getConfig().set("drops." + i + ".amount", item.getAmount());




                         UniDrops.getInstance().saveConfig();
                    }
                }

                break;
        }
        return false;
    }
}
