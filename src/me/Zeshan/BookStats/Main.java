package me.Zeshan.BookStats;

import java.io.File;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import Util.Vault;

public class Main extends JavaPlugin implements Listener {

	public static Main instance;

	boolean useMySQL;
	String sMySQLAddr;
	String sMySQLPort;
	String sMySQLUser;
	String sMySQLPass;
	String sMySQLDataBase;
	String sMySQLTable;

	String bookData = null;
	String title = null;
	String author = null;

	int time = 0;
	boolean giveBook;
	int slot;
	boolean updateBook;
	boolean giveLimit;
	boolean bookMove;
	boolean bookDrop;

	@Override
	public void onEnable() {
		instance = this;

		saveDefaultConfig();

		// Setup
		getServer().getPluginManager().registerEvents(new Events(this), this);
		getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
		getCommand("BookStats").setExecutor(this);

		// Load book
		for (String s: getConfig().getStringList("Book")) {
			if (bookData != null) {
				bookData = bookData + "\n" + s;
			} else {
				bookData = s;
			}
		}

		bookData = bookData.replace("&", "§");
		title = getConfig().getString("Book Title").replace("&", "§");
		author = getConfig().getString("Book Author").replace("&", "§");

		// MySQL
		useMySQL = getConfig().getBoolean("MySQL.Use MySQL");
		sMySQLAddr = getConfig().getString("MySQL.IP Address");
		sMySQLPort = getConfig().getString("MySQL.Port");
		sMySQLUser = getConfig().getString("MySQL.User");
		sMySQLPass = getConfig().getString("MySQL.Pass");
		sMySQLDataBase = getConfig().getString("MySQL.Database");
		sMySQLTable = getConfig().getString("MySQL.Table");

		time = getConfig().getInt("Save Interval");
		giveBook = getConfig().getBoolean("On Join.Give Book");
		slot = getConfig().getInt("On Join.Slot");
		updateBook = getConfig().getBoolean("Update Book");
		giveLimit = getConfig().getBoolean("On Join.Give Limit");
		bookDrop = getConfig().getBoolean("On Join.Prevent Drop");
		bookMove = getConfig().getBoolean("On Join.Prevent Drag");

		// Connect
		if (useMySQL) {
			System.out.println("BookStats: Connecting to MySQL..");
			MySQL.startUp();
		}

		// Using flat file
		if (useMySQL == false) {
			System.out.println("BookStats: Using flat file!");
			File usersDir = new File("plugins/BookStats/UserData/");
			if (!usersDir.exists()) {
				usersDir.mkdir();
			}
		}

		// Hooks
		if (bookData.contains("{Balance}")) {
			Vault.setupEconomy();
		}

		autoSave();
	}

	@Override
	public void onDisable() {
		for (Player players: Bukkit.getOnlinePlayers()) {
			if (useMySQL) {
				DataHandler.saveSQLData(players);
				try {
					MySQL.con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return;
			}
			DataHandler.saveDYMLData(players);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender.hasPermission("BookStats.getbook")) {
			final Player player = (Player) sender;

			player.getInventory().addItem(DataHandler.createBook(player));
		}
		return true;
	}

	public static Main inst() {
		return instance;
	}

	public static PlayerFile getPlayerYaml(UUID string)
	{
		return new PlayerFile("plugins/BookStats/UserData/" + string + ".yml");
	}

	public static void autoSave() {
		Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(Main.inst(), new Runnable()
		{
			public void run()
			{
				if (Main.inst().useMySQL) {
					new BukkitRunnable()
					{
						public void run()
						{
							for (Player player: Bukkit.getOnlinePlayers())
								if (Main.inst().useMySQL) {
									DataHandler.saveSQLData(player);
								} else {
									DataHandler.saveYMLData(player);
								}
						}
					}.runTaskAsynchronously(Main.inst());
				}
			}
		}, 60L, 1200 * Main.inst().time);
	}
}
