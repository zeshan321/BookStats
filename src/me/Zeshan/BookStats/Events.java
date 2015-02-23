package me.Zeshan.BookStats;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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

		PlayerData pd = new PlayerData(player);
		pd.addDeaths();
		pd.resetKillStreak();

		if (event.getEntity().getKiller() instanceof Player) {
			Player killer = (Player) event.getEntity().getKiller();

			pd = new PlayerData(killer);
			pd.addKills();
			pd.addKillStreak();
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();

		PlayerData pd = new PlayerData(player);
		pd.addPlacedBlocks();
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();

		PlayerData pd = new PlayerData(player);
		pd.addBrokenBlocks();
	}

	@EventHandler
	public void onMobKill(EntityDeathEvent event) {
		if (event.getEntity().getKiller() instanceof Player) {
			Player player = (Player) event.getEntity().getKiller();

			PlayerData pd = new PlayerData(player);
			pd.addMobKills();
		}
	}

	@EventHandler
	public void onOpen(PlayerInteractEvent event) {
		Player player = event.getPlayer();

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
				player.getInventory().setItemInHand(DataHandler.createBook(player));
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
