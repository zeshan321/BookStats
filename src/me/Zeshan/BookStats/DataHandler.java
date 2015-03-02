package me.Zeshan.BookStats;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scheduler.BukkitRunnable;

import Util.Vault;

public class DataHandler {

	public static void loadSQLData(Player player) {
		final PlayerData pd = new PlayerData(player);
		final UUID uuid = player.getUniqueId();
		new BukkitRunnable()
		{
			public void run()
			{
				if (Main.inst().useMySQL) {
					try {
						pd.setKills(MySQL.getValue("Kills", uuid));
						pd.setDeaths(MySQL.getValue("Death", uuid));
						pd.setKillStreak(MySQL.getValue("KillStreak", uuid));
						pd.setBrokenBlocks(MySQL.getValue("BlocksBroken", uuid));
						pd.setPlacedBlocks(MySQL.getValue("BlocksPlaced", uuid));
						pd.setMobKills(MySQL.getValue("MobKills", uuid));
						pd.setGiveBook(MySQL.getValue("GiveBook", uuid));
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}.runTaskAsynchronously(Main.inst());
	}


	public static void loadYMLData(Player player) {
		PlayerData pd = new PlayerData(player);
		UUID uuid = player.getUniqueId();

		PlayerFile pf = Main.getPlayerYaml(uuid);

		pd.setKills(pf.getInteger("Kills"));
		pd.setDeaths(pf.getInteger("Death"));
		pd.setKillStreak(pf.getInteger("KillStreak"));
		pd.setBrokenBlocks(pf.getInteger("BlocksBroken"));
		pd.setPlacedBlocks(pf.getInteger("BlocksPlaced"));
		pd.setMobKills(pf.getInteger("MobKills"));
		pd.setGiveBook(pf.getInteger("GiveBook"));
	}


	public static void saveSQLData(Player player) {
		MySQL.saveValues(player);
	}

	public static void saveYMLData(Player player) {
		PlayerData pd = new PlayerData(player);
		UUID uuid = player.getUniqueId();
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
	
	public static void saveDYMLData(Player player) {
		PlayerData pd = new PlayerData(player);
		UUID uuid = player.getUniqueId();
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

	public static ItemStack createBook(Player player) {
		PlayerData pd = new PlayerData(player);

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
		String[] pages = bookData.split("/p");
		bm.setPages(pages);
		bm.setAuthor(Main.inst().author.replace("{Player}", player.getName()));
		bm.setTitle(Main.inst().title.replace("{Player}", player.getName()));
		book.setItemMeta(bm);
		return book;
	}
}
