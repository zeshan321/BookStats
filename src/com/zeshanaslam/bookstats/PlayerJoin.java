package com.zeshanaslam.bookstats;

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
			Main.sql.createUser(player);
			new DataHandler().loadSQLData(player);
		} else {
			new DataHandler().loadYMLData(player);
		}
		run(player);
	}

	public void run(final Player player) {
		new BukkitRunnable() {  	 
			public void run() {
				PlayerData pd = new PlayerData(player.getUniqueId());

				if (Main.inst().giveLimit && pd.canGiveBook() == false) {
					return;
				}

				if (Main.inst().giveBook) {
					int slot = Main.inst().slot;

					if (slot == 0) {
						player.getInventory().addItem(new DataHandler().createBook(player));
						pd.setGiveBook(1);
					} else {
						player.getInventory().setItem(slot - 1, new DataHandler().createBook(player));
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
					new DataHandler().saveSQLData(player);
				}
			}.runTaskAsynchronously(Main.inst());
		} else {
			new DataHandler().saveYMLData(player);
		}
	}
}
