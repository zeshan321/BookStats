package com.zeshanaslam.bookstats;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scheduler.BukkitRunnable;

import callbacks.ValuesCallback;
import me.clip.placeholderapi.PlaceholderAPI;
import util.Vault;

public class DataHandler {

	public void loadSQLData(Player player) {
		final UUID uuid = player.getUniqueId();
		final PlayerData pd = new PlayerData(uuid);

		new BukkitRunnable()
		{
			public void run()
			{
				Main.sql.getValue(uuid, new ValuesCallback() {

					@Override
					public void onRequestComplete(UUID uuid, ResultSet rs) {
						try {
							while(rs.next()) {
								pd.setKills(rs.getInt("Kills"));
								pd.setDeaths(rs.getInt("Death"));
								pd.setKillStreak(rs.getInt("KillStreak"));
								pd.setBrokenBlocks(rs.getInt("BlocksBroken"));
								pd.setPlacedBlocks(rs.getInt("BlocksPlaced"));
								pd.setMobKills(rs.getInt("MobKills"));
								pd.setGiveBook(rs.getInt("GiveBook"));
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}

				});
			}
		}.runTaskAsynchronously(Main.inst());
	}


	public void loadYMLData(Player player) {
		UUID uuid = player.getUniqueId();
		PlayerData pd = new PlayerData(uuid);
		PlayerFile pf = Main.getPlayerYaml(uuid);

		pd.setKills(pf.getInteger("Kills"));
		pd.setDeaths(pf.getInteger("Death"));
		pd.setKillStreak(pf.getInteger("KillStreak"));
		pd.setBrokenBlocks(pf.getInteger("BlocksBroken"));
		pd.setPlacedBlocks(pf.getInteger("BlocksPlaced"));
		pd.setMobKills(pf.getInteger("MobKills"));
		pd.setGiveBook(pf.getInteger("GiveBook"));
	}


	public void saveSQLData(Player player) {
		Main.sql.saveValues(player);
	}

	public void saveYMLData(Player player) {
		UUID uuid = player.getUniqueId();
		PlayerData pd = new PlayerData(uuid);
		PlayerFile pf = Main.getPlayerYaml(uuid);

		pf.set("Kills", pd.getKills());
		pf.set("Death", pd.getDeaths());
		pf.set("KillStreak", pd.getKillStreak());
		pf.set("BlocksBroken", pd.getBrokenBlocks());
		pf.set("BlocksPlaced", pd.getPlacedBlocks());
		pf.set("MobKills", pd.getMobKills());
		pf.set("GiveBook", pd.getGiveBook());

		pf.save(true);
	}

	public void saveDYMLData(Player player) {
		UUID uuid = player.getUniqueId();
		PlayerData pd = new PlayerData(uuid);
		PlayerFile pf = Main.getPlayerYaml(uuid);

		pf.set("Kills", pd.getKills());
		pf.set("Death", pd.getDeaths());
		pf.set("KillStreak", pd.getKillStreak());
		pf.set("BlocksBroken", pd.getBrokenBlocks());
		pf.set("BlocksPlaced", pd.getPlacedBlocks());
		pf.set("MobKills", pd.getMobKills());
		pf.set("GiveBook", pd.getGiveBook());

		pf.save();
	}

	public ItemStack createBook(Player player) {
		UUID uuid = player.getUniqueId();
		PlayerData pd = new PlayerData(uuid);

		ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta bm = (BookMeta)book.getItemMeta();

		String bookData = Main.inst().bookData;
		bookData = bookData
				.replace("{Player}", player.getName())
				.replace("{Kills}", String.valueOf(pd.getKills()))
				.replace("{Deaths}", String.valueOf(pd.getDeaths()))
				.replace("{Killstreak}", String.valueOf(pd.getKillStreak()))
				.replace("{Mobkills}", String.valueOf(pd.getMobKills()))
				.replace("{Blocksplaced}", String.valueOf(pd.getPlacedBlocks()))
				.replace("{Blocksbroken}", String.valueOf(pd.getBrokenBlocks()))
				.replace("{Balance}", String.valueOf(Vault.getBal(player)));

		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			bookData = PlaceholderAPI.setPlaceholders(player, bookData);
		}

		String[] pages = bookData.split("/p");
		bm.setPages(pages);
		bm.setAuthor(Main.inst().author.replace("{Player}", player.getName()));
		bm.setTitle(Main.inst().title.replace("{Player}", player.getName()));
		book.setItemMeta(bm);
		return book;
	}
}
