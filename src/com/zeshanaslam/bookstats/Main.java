package com.zeshanaslam.bookstats;

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

import net.md_5.bungee.api.ChatColor;
import util.InvCheck;
import util.Vault;

public class Main extends JavaPlugin implements Listener {

	public static Main instance;
	public String author = null;
	
	boolean useMySQL;
	String sMySQLAddr;
	String sMySQLPort;
	String sMySQLUser;
	String sMySQLPass;
	String sMySQLDataBase;
	String sMySQLTable;

	String bookData = null;
	String title = null;
	String bookLimitMessage = null;

	int time = 0;
	boolean giveBook;
	int slot;
	boolean updateBook;
	boolean giveLimit;
	boolean bookMove;
	boolean bookDrop;
	
	boolean bookLimit;
	
	//On Death
	boolean giveBookDeath;
	int slotDeath;
	boolean deathRemove;

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
		
		giveBookDeath = getConfig().getBoolean("On Death.Give Book");
		slotDeath = getConfig().getInt("On Death.Slot");
		deathRemove =  getConfig().getBoolean("On Death.Death Remove");
		
		bookLimit = getConfig().getBoolean("Book Limit"); 
		bookLimitMessage = getConfig().getString("Book Limit Message").replace("&", "§");

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
			
			if (args.length > 0 == false) {
				if (bookLimit && InvCheck.canGiveBook(player) == false) {
					player.sendMessage(bookLimitMessage);
					return true;
				}
				
				player.getInventory().addItem(DataHandler.createBook(player));
				return true;
			}

			if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("BookStats.reload")) {
				sender.sendMessage(ChatColor.GOLD + "BookStats: Reloading data... MySQL/flat file changes require restart");
				reloadConfig();
				
				// Load book
				bookData = "";
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
				
				giveBookDeath = getConfig().getBoolean("On Death.Give Book");
				slotDeath = getConfig().getInt("On Death.Slot");
				deathRemove =  getConfig().getBoolean("On Death.Death Remove");
				
				bookLimit = getConfig().getBoolean("Book Limit"); 
				bookLimitMessage = getConfig().getString("Book Limit Message").replace("&", "§");
				sender.sendMessage(ChatColor.GOLD + "BookStats: Done reloading!");
			}
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
