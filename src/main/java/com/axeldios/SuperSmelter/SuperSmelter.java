/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.milkbowl.vault.economy.Economy
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.inventory.Recipe
 *  org.bukkit.inventory.ShapedRecipe
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.plugin.RegisteredServiceProvider
 *  org.bukkit.plugin.ServicesManager
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.axeldios.SuperSmelter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class SuperSmelter
extends JavaPlugin {
    public static Economy econ = null;
    public static String smelterName1;
    public static double fuelMultiplier1;
    public static boolean doubleOutput1;
    public static String smeltingList1;
    public static double smelterPrice1;
    public static String smelterName2;
    public static double fuelMultiplier2;
    public static boolean doubleOutput2;
    public static String smeltingList2;
    public static double smelterPrice2;
    public static String smelterName3;
    public static double fuelMultiplier3;
    public static boolean doubleOutput3;
    public static String smeltingList3;
    public static double smelterPrice3;
    public static Map<Block, String> smelterDB;
    public static String currentType;
    Logger log = Logger.getLogger("Minecraft");

    static {
        smelterDB = new HashMap<Block, String>();
        currentType = "";
    }

    public void onEnable() {
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents((Listener)new SuperSmelterListener(), (Plugin)this);
        smelterName1 = this.getConfig().getString("Name.SMELTER1", "Production Furnace");
        fuelMultiplier1 = this.getConfig().getDouble("Fuel.MULTIPLIER1", 2.0);
        doubleOutput1 = this.getConfig().getBoolean("Output.DOUBLE1", true);
        smelterPrice1 = this.getConfig().getDouble("Price.SMELTER1", 1.0);
        smeltingList1 = this.getConfig().getString("Smelt1", "IRON_INGOT GOLD_INGOT STONE").toUpperCase();
        smelterName2 = this.getConfig().getString("Name.SMELTER2", "Charcoal Oven");
        fuelMultiplier2 = this.getConfig().getDouble("Fuel.MULTIPLIER2", 0.5);
        doubleOutput2 = this.getConfig().getBoolean("Output.DOUBLE2", false);
        smelterPrice2 = this.getConfig().getDouble("Price.SMELTER2", 1.0);
        smeltingList2 = this.getConfig().getString("Smelt2", "").toUpperCase();
        smelterName3 = this.getConfig().getString("Name.SMELTER3", "Power Furnace");
        fuelMultiplier3 = this.getConfig().getDouble("Fuel.MULTIPLIER3", 1.0);
        doubleOutput3 = this.getConfig().getBoolean("Output.DOUBLE3", true);
        smelterPrice3 = this.getConfig().getDouble("Price.SMELTER3", 1.0);
        smeltingList3 = this.getConfig().getString("Smelt3", "IRON_INGOT GOLD_INGOT").toUpperCase();
        this.getConfig().set("Name.SMELTER1", (Object)smelterName1);
        this.getConfig().set("Fuel.MULTIPLIER1", (Object)fuelMultiplier1);
        this.getConfig().set("Output.DOUBLE1", (Object)doubleOutput1);
        this.getConfig().set("Price.SMELTER1", (Object)smelterPrice1);
        this.getConfig().set("Smelt1", (Object)smeltingList1);
        this.getConfig().set("Name.SMELTER2", (Object)smelterName2);
        this.getConfig().set("Fuel.MULTIPLIER2", (Object)fuelMultiplier2);
        this.getConfig().set("Output.DOUBLE2", (Object)doubleOutput2);
        this.getConfig().set("Price.SMELTER2", (Object)smelterPrice2);
        this.getConfig().set("Smelt2", (Object)smeltingList2);
        this.getConfig().set("Name.SMELTER3", (Object)smelterName3);
        this.getConfig().set("Fuel.MULTIPLIER3", (Object)fuelMultiplier3);
        this.getConfig().set("Output.DOUBLE3", (Object)doubleOutput3);
        this.getConfig().set("Price.SMELTER3", (Object)smelterPrice3);
        this.getConfig().set("Smelt3", (Object)smeltingList3);
        this.saveConfig();
        if (!this.setupEconomy()) {
            this.log.info("No economy, buying/selling disabled");
        }
        Server server = this.getServer();
        ItemStack newFurnace1 = new ItemStack(Material.FURNACE, 1);
        ItemMeta meta1 = newFurnace1.getItemMeta();
        meta1.setDisplayName((Object)ChatColor.RED + smelterName1);
        ArrayList<String> lore1 = new ArrayList<String>();
        lore1.add((Object)ChatColor.YELLOW + "SuperSmelter");
        meta1.setLore(lore1);
        newFurnace1.setItemMeta(meta1);
        ShapedRecipe supersmelter1 = new ShapedRecipe(new ItemStack(newFurnace1));
        supersmelter1.shape(new String[]{"SSS", "SFS", "IRI"});
        supersmelter1.setIngredient('F', Material.FURNACE);
        supersmelter1.setIngredient('S', Material.STONE);
        supersmelter1.setIngredient('I', Material.IRON_INGOT);
        supersmelter1.setIngredient('R', Material.REDSTONE);
        server.addRecipe((Recipe)supersmelter1);
        ItemStack newFurnace2 = new ItemStack(Material.FURNACE, 1);
        ItemMeta meta2 = newFurnace2.getItemMeta();
        meta2.setDisplayName((Object)ChatColor.RED + smelterName2);
        ArrayList<String> lore2 = new ArrayList<String>();
        lore2.add((Object)ChatColor.YELLOW + "SuperSmelter");
        meta2.setLore(lore2);
        newFurnace2.setItemMeta(meta2);
        ShapedRecipe supersmelter2 = new ShapedRecipe(new ItemStack(newFurnace2));
        supersmelter2.shape(new String[]{"III", "IFI", "RGR"});
        supersmelter2.setIngredient('F', Material.FURNACE);
        supersmelter2.setIngredient('I', Material.IRON_INGOT);
        supersmelter2.setIngredient('R', Material.REDSTONE);
        supersmelter2.setIngredient('G', Material.GOLD_INGOT);
        server.addRecipe((Recipe)supersmelter2);
        ItemStack newFurnace3 = new ItemStack(Material.FURNACE, 1);
        ItemMeta meta3 = newFurnace3.getItemMeta();
        meta3.setDisplayName((Object)ChatColor.RED + smelterName3);
        ArrayList<String> lore3 = new ArrayList<String>();
        lore3.add((Object)ChatColor.YELLOW + "SuperSmelter");
        meta3.setLore(lore3);
        newFurnace3.setItemMeta(meta3);
        ShapedRecipe supersmelter3 = new ShapedRecipe(new ItemStack(newFurnace3));
        supersmelter3.shape(new String[]{"OOO", "OFO", "GRG"});
        supersmelter3.setIngredient('F', Material.FURNACE);
        supersmelter3.setIngredient('O', Material.OBSIDIAN);
        supersmelter3.setIngredient('R', Material.REDSTONE_BLOCK);
        supersmelter3.setIngredient('G', Material.GOLD_INGOT);
        server.addRecipe((Recipe)supersmelter3);
        String pluginFolder = this.getDataFolder().getAbsolutePath();
        File file = new File(String.valueOf(pluginFolder) + File.separator + "SuperSmelter.db");
        Block smelterLocation = null;
        String smelterName = null;
        String[] args = null;
        String[] sp = null;
        try {
            String l;
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            while ((l = br.readLine()) != null) {
                args = l.split("[|]", 2);
                if (args.length != 2) continue;
                sp = args[0].split(",");
                smelterLocation = Bukkit.getWorld((String)sp[0]).getBlockAt(Integer.parseInt(sp[1]), Integer.parseInt(sp[2]), Integer.parseInt(sp[3]));
                smelterName = args[1];
                smelterDB.put(smelterLocation, smelterName);
            }
            br.close();
        }
        catch (Exception e) {
            this.log.info("ERROR:  Loading SuperSmelter.db");
        }
    }

    public void onDisable() {
        if (!smelterDB.isEmpty()) {
            String pluginFolder = this.getDataFolder().getAbsolutePath();
            File file = new File(String.valueOf(pluginFolder) + File.separator + "SuperSmelter.db");
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                for (Block p : smelterDB.keySet()) {
                    String s = String.valueOf(p.getWorld().getName()) + "," + String.valueOf(p.getX()) + "," + String.valueOf(p.getY()) + "," + String.valueOf(p.getZ());
                    bw.write(String.valueOf(s) + "|" + smelterDB.get((Object)p));
                    bw.newLine();
                }
                bw.flush();
                bw.close();
            }
            catch (Exception e) {
                this.log.info("ERROR:  Saving SuperSmelter.db");
            }
        }
        this.log.info("[SuperSmelter] has been disabled.");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = null;
        if (!(sender instanceof Player)) {
            sender.sendMessage((Object)ChatColor.RED + "SuperSmelter Commands cannot be run from the console.");
            return true;
        }
        player = (Player)sender;
        if (cmd.getName().equalsIgnoreCase("ss") && args.length >= 1 && args[0].equalsIgnoreCase("give")) {
            if (player.hasPermission("SuperSmelter.give")) {
                if (args.length == 3) {
                	boolean isOnline = false;
                	Collection<? extends Player> players = Bukkit.getOnlinePlayers();
                	for(Player p : players)
                	{
                		if(p.getPlayerListName().equals(args[1]))
                		{
                			isOnline = true;
                			break;
                		}
                	}
                	
                    if (isOnline) {
                        
                    } else {
                        player.sendMessage("The player must be online to use the Give Command.");
                    }
                } else {
                    player.sendMessage("Syntax: /ss give {player} {type number}");
                }
            } else {
                player.sendMessage("You don't have permission to use the Give Command.");
            }
        }
        return true;
    }

    private boolean setupEconomy() {
        if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = (Economy)rsp.getProvider();
        if (econ != null) {
            return true;
        }
        return false;
    }
}

