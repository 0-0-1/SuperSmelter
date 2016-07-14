/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  net.milkbowl.vault.economy.Economy
 *  net.milkbowl.vault.economy.EconomyResponse
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.ItemFrame
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.inventory.CraftItemEvent
 *  org.bukkit.event.inventory.FurnaceBurnEvent
 *  org.bukkit.event.inventory.FurnaceSmeltEvent
 *  org.bukkit.event.player.PlayerInteractEntityEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.inventory.Recipe
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.axeldios.SuperSmelter;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class SuperSmelterListener
implements Listener {
    Logger log = Logger.getLogger("Minecraft");

    @EventHandler
    public void onCrafting(CraftItemEvent event) {
        if (event.getRecipe().getResult().getType() == Material.FURNACE && event.getRecipe().getResult().hasItemMeta()) {
            String displayName = event.getRecipe().getResult().getItemMeta().getDisplayName();
            if (displayName.equalsIgnoreCase("Furnace")) {
                return;
            }
            Player player = (Player)event.getWhoClicked();
            if (displayName.equalsIgnoreCase((Object)ChatColor.RED + SuperSmelter.smelterName1)) {
                if (!player.hasPermission("supersmelter.1.craft")) {
                    player.sendMessage("You don't have permission to craft this SuperSmelter Type");
                    event.setCancelled(true);
                }
            } else if (displayName.equalsIgnoreCase((Object)ChatColor.RED + SuperSmelter.smelterName2)) {
                if (!player.hasPermission("supersmelter.2.craft")) {
                    player.sendMessage("You don't have permission to craft this SuperSmelter Type");
                    event.setCancelled(true);
                }
            } else if (displayName.equalsIgnoreCase((Object)ChatColor.RED + SuperSmelter.smelterName3) && !player.hasPermission("supersmelter.3.craft")) {
                player.sendMessage("You don't have permission to craft this SuperSmelter Type");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        Block theFurnace = event.getBlock();
        if (SuperSmelter.smelterDB.containsKey((Object)theFurnace)) {
            String smelterType = SuperSmelter.smelterDB.get((Object)theFurnace);
            boolean doubleOutput = false;
            String smeltingList = "";
            if (smelterType != null) {
                ItemStack output = event.getResult();
                if (smelterType.equalsIgnoreCase(SuperSmelter.smelterName1)) {
                    smeltingList = SuperSmelter.smeltingList1;
                    doubleOutput = SuperSmelter.doubleOutput1;
                } else if (smelterType.equalsIgnoreCase(SuperSmelter.smelterName2)) {
                    smeltingList = SuperSmelter.smeltingList2;
                    doubleOutput = SuperSmelter.doubleOutput2;
                } else if (smelterType.equalsIgnoreCase(SuperSmelter.smelterName3)) {
                    smeltingList = SuperSmelter.smeltingList3;
                    doubleOutput = SuperSmelter.doubleOutput3;
                } else {
                    return;
                }
                if (smeltingList.contains(output.getType().toString())) {
                    Material stackType = output.getType();
                    int stackItemCount = output.getAmount();
                    if (stackItemCount < 63 && doubleOutput) {
                        output = new ItemStack(stackType, stackItemCount + 1);
                        event.setResult(output);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        String smelterName;
        Block theFurnace = event.getBlock();
        if (SuperSmelter.smelterDB.containsKey((Object)theFurnace) && (smelterName = SuperSmelter.smelterDB.get((Object)theFurnace)) != null) {
            Double burnTime = new Double(event.getBurnTime());
            if (smelterName.equalsIgnoreCase(SuperSmelter.smelterName1)) {
                burnTime = burnTime / SuperSmelter.fuelMultiplier1;
            } else if (smelterName.equalsIgnoreCase(SuperSmelter.smelterName2)) {
                burnTime = burnTime / SuperSmelter.fuelMultiplier2;
            } else if (smelterName.equalsIgnoreCase(SuperSmelter.smelterName3)) {
                burnTime = burnTime / SuperSmelter.fuelMultiplier3;
            } else {
                return;
            }
            event.setBurnTime(burnTime.intValue());
        }
    }

    @EventHandler
    public void onPlayer(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block theFurnace = event.getClickedBlock();
        if (theFurnace != null && (theFurnace.getType() == Material.FURNACE || theFurnace.getType() == Material.BURNING_FURNACE)) {
            String furnaceName = SuperSmelter.smelterDB.get((Object)theFurnace);
            if (furnaceName != null) {
                if (player.isSneaking() && player.getInventory().getItemInMainHand().getType() == Material.FURNACE) {
                    event.setCancelled(true);
                    return;
                }
                if (furnaceName.equalsIgnoreCase(SuperSmelter.smelterName1)) {
                    if (!player.hasPermission("supersmelter.1.use")) {
                        player.sendMessage("You don't have permission to use this SuperSmelter Type");
                        event.setCancelled(true);
                    }
                } else if (furnaceName.equalsIgnoreCase(SuperSmelter.smelterName2)) {
                    if (!player.hasPermission("supersmelter.2.use")) {
                        player.sendMessage("You don't have permission to use this SuperSmelter Type");
                        event.setCancelled(true);
                    }
                } else if (furnaceName.equalsIgnoreCase(SuperSmelter.smelterName3) && !player.hasPermission("supersmelter.3.use")) {
                    player.sendMessage("You don't have permission to use this SuperSmelter Type");
                    event.setCancelled(true);
                }
            }
            return;
        }
        if (event.getItem() == null) {
            return;
        }
        if (event.getItem().getType() == Material.FURNACE && event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getItem().hasItemMeta()) {
            if (player.isSneaking()) {
                event.setCancelled(true);
                return;
            }
            String displayName = event.getItem().getItemMeta().getDisplayName();
            if (displayName.equalsIgnoreCase((Object)ChatColor.RED + SuperSmelter.smelterName1)) {
                if (player.hasPermission("supersmelter.1.place")) {
                    SuperSmelter.currentType = SuperSmelter.smelterName1;
                } else {
                    player.sendMessage("You don't have permission to place this SuperSmelter Type");
                    event.setCancelled(true);
                }
            } else if (displayName.equalsIgnoreCase((Object)ChatColor.RED + SuperSmelter.smelterName2)) {
                if (player.hasPermission("supersmelter.2.place")) {
                    SuperSmelter.currentType = SuperSmelter.smelterName2;
                } else {
                    player.sendMessage("You don't have permission to place this SuperSmelter Type");
                    event.setCancelled(true);
                }
            } else if (displayName.equalsIgnoreCase((Object)ChatColor.RED + SuperSmelter.smelterName3)) {
                if (player.hasPermission("supersmelter.3.place")) {
                    SuperSmelter.currentType = SuperSmelter.smelterName3;
                } else {
                    player.sendMessage("You don't have permission to place this SuperSmelter Type");
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.FURNACE) {
            if (SuperSmelter.currentType != "") {
                SuperSmelter.smelterDB.put(event.getBlock(), SuperSmelter.currentType);
            }
            SuperSmelter.currentType = "";
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        String smelterType;
        Block theFurnace;
        if ((event.getBlock().getType() == Material.FURNACE || event.getBlock().getType() == Material.BURNING_FURNACE) && (smelterType = SuperSmelter.smelterDB.get((Object)(theFurnace = event.getBlock()))) != null) {
            Player player = event.getPlayer();
            boolean openSlot = false;
            ItemStack[] arritemStack = player.getInventory().getContents();
            int n = arritemStack.length;
            int n2 = 0;
            while (n2 < n) {
                ItemStack item = arritemStack[n2];
                if (item == null) {
                    openSlot = true;
                }
                ++n2;
            }
            if (openSlot) {
                ItemStack newFurnace = new ItemStack(Material.FURNACE, 1);
                ItemMeta meta = newFurnace.getItemMeta();
                meta.setDisplayName((Object)ChatColor.RED + smelterType);
                ArrayList<String> lore = new ArrayList<String>();
                lore.add((Object)ChatColor.YELLOW + "SuperSmelter");
                meta.setLore(lore);
                newFurnace.setItemMeta(meta);
                PlayerInventory inventory = player.getInventory();
                inventory.addItem(new ItemStack[]{newFurnace});
                SuperSmelter.smelterDB.remove((Object)theFurnace);
                theFurnace.setType(Material.AIR);
            } else {
                player.sendMessage("You don't have any room in your Inventory");
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFrameClick(PlayerInteractEntityEvent event) {
        EntityType itemType = event.getRightClicked().getType();
        if (itemType == EntityType.ITEM_FRAME) {
            Player player = event.getPlayer();
            if (event.getRightClicked() instanceof ItemFrame) {
                ItemFrame i = (ItemFrame)event.getRightClicked();
                ItemStack inFrame = i.getItem();
                if (!inFrame.hasItemMeta()) {
                    return;
                }
                String displayName = inFrame.getItemMeta().getDisplayName();
                if (displayName != null) {
                    event.setCancelled(true);
                    if (displayName.equalsIgnoreCase((Object)ChatColor.RED + SuperSmelter.smelterName1)) {
                        if (player.hasPermission("supersmelter.1.buy")) {
                            this.buySuperSmelter(player, SuperSmelter.smelterPrice1, SuperSmelter.smelterName1);
                        } else {
                            player.sendMessage("You don't have permission to buy this SuperSmelter Type");
                        }
                    } else if (displayName.equalsIgnoreCase((Object)ChatColor.RED + SuperSmelter.smelterName2)) {
                        if (player.hasPermission("supersmelter.2.buy")) {
                            this.buySuperSmelter(player, SuperSmelter.smelterPrice2, SuperSmelter.smelterName2);
                        } else {
                            player.sendMessage("You don't have permission to buy this SuperSmelter Type");
                        }
                        event.setCancelled(true);
                    } else if (displayName.equalsIgnoreCase((Object)ChatColor.RED + SuperSmelter.smelterName3)) {
                        if (player.hasPermission("supersmelter.3.buy")) {
                            this.buySuperSmelter(player, SuperSmelter.smelterPrice3, SuperSmelter.smelterName3);
                        } else {
                            player.sendMessage("You don't have permission to buy this SuperSmelter Type");
                        }
                    }
                }
            }
        }
    }

    public void buySuperSmelter(Player player, double price, String smelterType) {
        if (SuperSmelter.econ == null) {
            player.sendMessage("No economy running on this server.");
            return;
        }
        boolean openSlot = false;
        ItemStack[] arritemStack = player.getInventory().getContents();
        int n = arritemStack.length;
        int n2 = 0;
        while (n2 < n) {
            ItemStack item = arritemStack[n2];
            if (item == null) {
                openSlot = true;
            }
            ++n2;
        }
        if (!openSlot) {
            player.sendMessage("You don't have any room in your inventory");
            return;
        }
        
        double balance = SuperSmelter.econ.getBalance(player);
        if (balance < price) {
            player.sendMessage("You can't afford this SuperSmelter.");
            return;
        }
        SuperSmelter.econ.withdrawPlayer(player, price);
        ItemStack newFurnace = new ItemStack(Material.FURNACE, 1);
        ItemMeta meta = newFurnace.getItemMeta();
        meta.setDisplayName((Object)ChatColor.RED + smelterType);
        ArrayList<String> lore = new ArrayList<String>();
        lore.add((Object)ChatColor.YELLOW + "SuperSmelter");
        meta.setLore(lore);
        newFurnace.setItemMeta(meta);
        PlayerInventory inventory = player.getInventory();
        inventory.addItem(new ItemStack[]{newFurnace});
    }
}

