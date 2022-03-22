package com.uniteam.unitdrops.unidrops;


import com.uniteam.unitdrops.unidrops.command.UniDropCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class UniDrops extends JavaPlugin {
    public List<Entity> airdrops = new ArrayList<>();

    public List<Location> airdrops_locations = new ArrayList<>();
    private static UniDrops instance;


    public Boolean isEnabled = false;

    @Override
    public void onEnable() {
        // Plugin startup logic

        isEnabled = getConfig().getBoolean("settings.plugin_enabled");
        Bukkit.getPluginManager().registerEvents(new Handler(), this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this , () -> {
            if(isEnabled) {
                spawnUniDrop(false, 0,0,0);
            }

        }, 0 , 72000);




        saveConfig();

        Bukkit.getServer().getPluginCommand("unidrops").setExecutor(new UniDropCommand());
        instance = this;
    }

    @Override
    public void onDisable() {

    }

    public static UniDrops getInstance() {
        return instance;
    }

    public void spawnUniDrop (Boolean isCommand, int x_ , int y_, int z_) {

            int minX = getConfig().getInt("locations.minX"), maxX = getConfig().getInt("locations.maxX"),
                spawnY = getConfig().getInt("locations.spawnY"),
                minZ = getConfig().getInt("locations.minZ"),
                maxZ = getConfig().getInt("locations.maxZ");

            int x = isCommand == true ? x_ : FUtils.getRandom(minX , maxX), y = isCommand == true ? y_ : spawnY, z = isCommand == true ? z_ : FUtils.getRandom(minZ, maxZ);

        Location l = new Location(Bukkit.getWorld("world"),x,y,z);

        org.bukkit.World w = l.getWorld();
        Entity e = (Entity) w.spawnFallingBlock(l, Material.NETHERITE_BLOCK, (byte) 0);

        airdrops.add(e);



    }

}
