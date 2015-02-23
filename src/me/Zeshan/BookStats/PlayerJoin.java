package me.Zeshan.BookStats;

import java.sql.SQLException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoin implements Listener
{
	Main plugin;

	public PlayerJoin(Main plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		if (Main.inst().useMySQL) {
			try {
				if (MySQL.doesExist(player.getUniqueId().toString())) {
					MySQL.createUser(player);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			DataHandler.loadSQLData(player);
		} else {
			DataHandler.loadYMLData(player);
		}
		run(player);
	}

	public void run(final Player player) {
		new BukkitRunnable() {  	 
			public void run() {
				PlayerData pd = new PlayerData(player);
				
				if (Main.inst().giveLimit && pd.canGiveBook() == false) {
					return;
				}
				
				if (Main.inst().giveBook) {
					int slot = Main.inst().slot;

					if (slot == 0) {
						player.getInventory().addItem(DataHandler.createBook(player));
						pd.setGiveBook(1);
					} else {
						player.getInventory().setItem(slot - 1, DataHandler.createBook(player));
						pd.setGiveBook(1);
					}
				}
			}
		}.runTaskLater(plugin, 20L);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		final Player player = event.getPlayer();

		if (Main.inst().useMySQL) {
			new BukkitRunnable()
			{
				public void run()
				{
					if (Main.inst().useMySQL) {
						DataHandler.saveSQLData(player);
						return;
					}
				}
			}.runTaskAsynchronously(Main.inst());
		}
		DataHandler.saveYMLData(player);
	}
}
