package com.uniteam.unitdrops.unidrops;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Handler implements Listener {

    @EventHandler
    public void onItemAction (InventoryClickEvent e){



        if(e.getView().getType() == InventoryType.CHEST) {
           if(FUtils.isInvEmpty(e.getInventory()) && UniDrops.getInstance().airdrops_locations.contains(e.getInventory().getLocation())) {

                UniDrops.getInstance().airdrops_locations.remove(e.getInventory().getLocation());

                Bukkit.broadcastMessage(UniDrops.getInstance().getConfig().getString("messages.prefix") + " " + UniDrops.getInstance().getConfig().getString("colors.nickname_color") + e.getView().getPlayer().getName() + " " +UniDrops.getInstance().getConfig().getString("messages.airdrop_taken") );

            }

        }
    }
    @EventHandler
    public void onBlockFall(EntityChangeBlockEvent e) {



        if ((e.getEntityType() == EntityType.FALLING_BLOCK && UniDrops.getInstance().airdrops.contains(e.getEntity()))){


            Bukkit.broadcastMessage(UniDrops.getInstance().getConfig().getString("messages.airdrop_spawned") + " x:" + e.getBlock().getLocation().getX() + " y:" + e.getBlock().getLocation().getY() + " z:" + e.getBlock().getLocation().getZ());

            BossBar bar = Bukkit.getServer().createBossBar(UniDrops.getInstance().getConfig().getString("messages.airdrop_bossbar"), BarColor.RED, BarStyle.SOLID, BarFlag.CREATE_FOG);
            bar.setVisible(true);


            for(Player ply : Bukkit.getOnlinePlayers()){
                bar.addPlayer(ply);
            }


            Bukkit.getWorld("world").spawnParticle(Particle.EXPLOSION_LARGE,e.getBlock().getLocation(),6);


            Entity e_ = (Entity) e.getEntity();

            Location loc = e_.getLocation();

            UniDrops.getInstance().airdrops_locations.add(e.getBlock().getLocation());

            e_.remove();




            e.setCancelled(true);

            loc.getBlock().setType(Material.CHEST);




             setDrop(loc.getBlock());
            new BukkitRunnable() {
                float loadTimer = 1;

                @Override
                public void run() {
                    loadTimer = loadTimer - 1 / 60f;

                    if(loadTimer <= 0f){
                        bar.removeAll();

                        Bukkit.broadcastMessage(UniDrops.getInstance().getConfig().getString("messages.airdrop_despawned"));


                        clearInv(loc.getBlock());
                        loc.getBlock().setType(Material.AIR);
                        cancel();


                        UniDrops.getInstance().airdrops_locations.remove(loc.getBlock().getLocation());



                    }else {
                        bar.setProgress(loadTimer);

                    }

                }
            }.runTaskTimer(UniDrops.getInstance(), 0, 100);



        }
    }



    public void clearInv (Block block) {
        Chest airchest = (Chest) block.getState();

        Inventory inv = airchest.getInventory();

        for(int i =0; i < inv.getContents().length; i++) {
            inv.setItem(i , new ItemStack(Material.AIR, 0));
        }
    }
    public void setDrop (Block block) {


        Chest airchest = (Chest) block.getState();

        Inventory inv = airchest.getInventory();

        for(int i = 0; i < inv.getContents().length; i++) {

            if(UniDrops.getInstance().getConfig().get("drops." + i) == null) {
                continue;
            }

            inv.setItem(i , new ItemStack(Material.valueOf(UniDrops.getInstance().getConfig().getString("drops." + i + ".itemtype")), UniDrops.getInstance().getConfig().getInt("drops." + i + ".amount")));
        }

    }

}
