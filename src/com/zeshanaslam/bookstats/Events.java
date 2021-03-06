package com.zeshanaslam.bookstats;

import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class Events implements Listener
{
	Main plugin;

	public Events(Main plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler
	public void onKillAndDeath(PlayerDeathEvent event) {
		Player player = (Player) event.getEntity();

		PlayerData pd = new PlayerData(player.getUniqueId());
		pd.addDeaths();
		pd.resetKillStreak();

		if (event.getEntity().getKiller() instanceof Player) {
			Player killer = (Player) event.getEntity().getKiller();

			pd = new PlayerData(killer.getUniqueId());
			pd.addKills();
			pd.addKillStreak();
		}
		
		if (Main.inst().deathRemove) {
			Iterator items = event.getDrops().iterator();
			while (items.hasNext()) {
				ItemStack item = (ItemStack) items.next();
				if (item != null && item.getType() == Material.WRITTEN_BOOK) {
					if (item.hasItemMeta()) {

						BookMeta bm = (BookMeta) item.getItemMeta();

						if (bm.getAuthor().equalsIgnoreCase(Main.inst().author))
							items.remove();
					}
				}
			}
		}
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		if (Main.inst().giveBookDeath) {
			int slot = Main.inst().slotDeath;

			if (slot == 0) {
				player.getInventory().addItem(new DataHandler().createBook(player));
			} else {
				player.getInventory().setItem(slot - 1, new DataHandler().createBook(player));
			}
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();

		PlayerData pd = new PlayerData(player.getUniqueId());
		pd.addPlacedBlocks();
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();

		PlayerData pd = new PlayerData(player.getUniqueId());
		pd.addBrokenBlocks();
	}

	@EventHandler
	public void onMobKill(EntityDeathEvent event) {
		if (event.getEntity().getKiller() instanceof Player) {
			Player player = (Player) event.getEntity().getKiller();

			PlayerData pd = new PlayerData(player.getUniqueId());
			pd.addMobKills();
		}
	}

	@EventHandler
	public void onOpen(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
			return;
		}

		if (Main.inst().updateBook == false) {
			return;
		}
		
		if (player.getItemInHand() == null) {
			return;
		}
		if (player.getItemInHand().getType() != Material.WRITTEN_BOOK) {
			return;
		}

		if (player.getItemInHand().hasItemMeta()) {

			BookMeta bm = (BookMeta) player.getItemInHand().getItemMeta();

			if (bm.getAuthor().equalsIgnoreCase(Main.inst().author))
				player.getInventory().setItemInHand(new DataHandler().createBook(player));
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {		
		if (Main.inst().bookDrop) {
			
			if (event.getItemDrop() == null) {
				return;
			}
			
			if (event.getItemDrop().getItemStack().getType() != Material.WRITTEN_BOOK) {
				return;
			}

			if (event.getItemDrop().getItemStack().hasItemMeta()) {

				BookMeta bm = (BookMeta) event.getItemDrop().getItemStack().getItemMeta();

				if (bm.getAuthor().equalsIgnoreCase(Main.inst().author))
					event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {		
		if (Main.inst().bookDrop) {
			
			if (event.getCurrentItem() == null) {
				return;
			}
			
			if (event.getCurrentItem().getType() != Material.WRITTEN_BOOK) {
				return;
			}

			if (event.getCurrentItem().hasItemMeta()) {

				BookMeta bm = (BookMeta) event.getCurrentItem().getItemMeta();

				if (bm.getAuthor().equalsIgnoreCase(Main.inst().author))
					event.setCancelled(true);
			}
		}
	}
}
