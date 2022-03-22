package com.uniteam.unitdrops.unidrops;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class FUtils {

    public static int getRandom (int min , int max) {
        return min + (int) (Math.random() * max);
    }
    public static boolean isNumber(String str){
        return str != null && str.matches("[0-9.]+");
    }

    public static boolean isInvEmpty(Inventory inv) {
        for(ItemStack item : inv.getContents()) {
            if(item != null) {
                return false;
            }
        }
        return true;
    }
}
