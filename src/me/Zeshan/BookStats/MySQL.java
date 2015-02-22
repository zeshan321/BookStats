package me.Zeshan.BookStats;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MySQL {
	public static Connection con;

	public static void connect() {
		new BukkitRunnable()
		{
			public void run()
			{
				try {
					Class.forName("com.mysql.jdbc.Driver").newInstance(); 
					con = DriverManager.getConnection("jdbc:mysql://" + Main.inst().sMySQLAddr + ":" + Main.inst().sMySQLPort + "/" + Main.inst().sMySQLDataBase+ "?user=" + Main.inst().sMySQLUser + "&password=" + Main.inst().sMySQLPass);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(Main.inst());
	}

	public static void startUp() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance(); 
			con = DriverManager.getConnection("jdbc:mysql://" + Main.inst().sMySQLAddr + ":" + Main.inst().sMySQLPort + "/" + Main.inst().sMySQLDataBase+ "?user=" + Main.inst().sMySQLUser + "&password=" + Main.inst().sMySQLPass);
			System.out.println("BookStats: Connected to database!");
			MySQL.createTable("BookStats", "UUID VARCHAR(36), Kills INT, Death INT, KillStreak INT, BlocksBroken INT, BlocksPlaced INT, MobKills INT");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createTable(String tablename, String s)
	{
		try
		{
			String sql = "CREATE TABLE IF NOT EXISTS `" + tablename + "` (" + s + ");";
			PreparedStatement ps = con.prepareStatement(sql);

			ps.execute(sql);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public static void saveValues(Player player)
	{
		try
		{
			PlayerData pd = new PlayerData(player);
			String sql = "UPDATE BookStats SET Kills = ?, Death = ?, KillStreak = ?, BlocksBroken = ?, BlocksPlaced = ?, MobKills = ? WHERE UUID = ?";
			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, pd.getKills());
			ps.setInt(2, pd.getDeaths());
			ps.setInt(3, pd.getKillStreak());
			ps.setInt(4, pd.getBrokenBlocks());
			ps.setInt(5, pd.getPlacedBlocks());
			ps.setInt(6, pd.getMobKills());
			ps.setString(7, player.getUniqueId().toString());

			ps.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public static void createUser(final Player player) {
		new BukkitRunnable()
		{
			public void run()
			{
				try
				{
					if (doesExist(player.getUniqueId().toString())) {
						return;
					}

					PlayerData pd = new PlayerData(player);
					String sql = "INSERT INTO BookStats (UUID,Kills,Death,KillStreak,BlocksBroken,BlocksPlaced,MobKills)"
							+ " VALUES (?,?,?,?,?,?,?)";

					PreparedStatement ps = con.prepareStatement(sql);

					ps.setString(1, player.getUniqueId().toString());
					ps.setInt(2, pd.getKills());
					ps.setInt(3, pd.getDeaths());
					ps.setInt(4, pd.getKillStreak());
					ps.setInt(5, pd.getBrokenBlocks());
					ps.setInt(6, pd.getPlacedBlocks());
					ps.setInt(7, pd.getMobKills());

					ps.executeUpdate();
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(Main.inst());
	}
	
	public static void createUser(final Player player, final boolean use) {
				try
				{
					if (doesExist(player.getUniqueId().toString())) {
						return;
					}

					PlayerData pd = new PlayerData(player);
					String sql = "INSERT INTO BookStats (UUID,Kills,Death,KillStreak,BlocksBroken,BlocksPlaced,MobKills)"
							+ " VALUES (?,?,?,?,?,?,?)";

					PreparedStatement ps = con.prepareStatement(sql);

					ps.setString(1, player.getUniqueId().toString());
					ps.setInt(2, pd.getKills());
					ps.setInt(3, pd.getDeaths());
					ps.setInt(4, pd.getKillStreak());
					ps.setInt(5, pd.getBrokenBlocks());
					ps.setInt(6, pd.getPlacedBlocks());
					ps.setInt(7, pd.getMobKills());

					ps.executeUpdate();
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
	}

	public static int getValue(String column, UUID uuid) throws SQLException
	{
		int value = 0;
		String sql = "SELECT * FROM `BookStats` WHERE `UUID` = '" + uuid + "'";
		PreparedStatement ps = con.prepareStatement(sql);

		ResultSet rs = ps.executeQuery(sql);
		rs.next();

		value = rs.getInt(column);
		return value;
	}

	public static boolean doesExist(String uuid) throws SQLException
	{
		String sql = "SELECT * FROM `BookStats` WHERE `UUID` = '" + uuid + "'";
		PreparedStatement ps = con.prepareStatement(sql);

		ResultSet rs = ps.executeQuery(sql);
		rs.next();
		
		String value = rs.getString("UUID");
		
		if (value.equalsIgnoreCase(uuid)) {
			return true;
		}
		return false;
	}
}
