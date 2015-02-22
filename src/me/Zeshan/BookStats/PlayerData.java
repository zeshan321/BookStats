package me.Zeshan.BookStats;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class PlayerData
{
	private Player player;
	public static HashMap<String, Integer> map = new HashMap<String, Integer>();

	public PlayerData(Player player)
	{
		this.player = player;
		
		if (!(map.containsKey(player.getName() + "-PK"))) {
			map.put(player.getName() + "-PK", 0);
		}
		
		if (!(map.containsKey(player.getName() + "-Deaths"))) {
			map.put(player.getName() + "-Deaths", 0);
		}
		
		if (!(map.containsKey(player.getName() + "-BlocksPlaced"))) {
			map.put(player.getName() + "-BlocksPlaced", 0);
		}

		if (!(map.containsKey(player.getName() + "-BlocksBroken"))) {
			map.put(player.getName() + "-BlocksBroken", 0);
		}
		
		if (!(map.containsKey(player.getName() + "-KillStreak"))) {
			map.put(player.getName() + "-KillStreak", 0);
		}
		
		if (!(map.containsKey(player.getName() + "-MobKills"))) {
			map.put(player.getName() + "-MobKills", 0);
		}
	}

	public Player getPlayer() {
		return player;
	}

	public void addDeaths() {
		map.put(player.getName() + "-Deaths", map.get(player.getName() + "-Deaths") + 1);
	}

	public void setDeaths(int i) {
		map.put(player.getName() + "-Deaths", i);
	}

	public int getDeaths() {
		return map.get(player.getName() + "-Deaths");
	}

	public void addKills() {
		map.put(player.getName() + "-PK", map.get(player.getName() + "-PK") + 1);
	}

	public void setKills(int i) {
		map.put(player.getName() + "-PK", i);
	}

	public int getKills() {
		return map.get(player.getName() + "-PK");
	}

	public void addPlacedBlocks() {
		map.put(player.getName() + "-BlocksPlaced", map.get(player.getName() + "-BlocksPlaced") + 1);
	}

	public void setPlacedBlocks(int i) {
		map.put(player.getName() + "-BlocksPlaced", i);
	}

	public int getPlacedBlocks() {
		return map.get(player.getName() + "-BlocksPlaced");
	}

	public void addBrokenBlocks() {
		map.put(player.getName() + "-BlocksBroken", map.get(player.getName() + "-BlocksBroken") + 1);
	}

	public void setBrokenBlocks(int i) {
		map.put(player.getName() + "-BlocksBroken", i);
	}

	public int getBrokenBlocks() {
		return map.get(player.getName() + "-BlocksBroken");
	}

	public void addKillStreak() {
		map.put(player.getName() + "-KillStreak", map.get(player.getName() + "-KillStreak") + 1);
	}

	public void setKillStreak(int i) {
		map.put(player.getName() + "-KillStreak", i);
	}

	public void resetKillStreak() {
		map.put(player.getName() + "-KillStreak", 0);
	}

	public int getKillStreak() {
		return map.get(player.getName() + "-KillStreak");
	}

	public void addMobKills() {
		map.put(player.getName() + "-MobKills", map.get(player.getName() + "-MobKills") + 1);
	}

	public void setMobKills(int i) {
		map.put(player.getName() + "-MobKills", i);
	}

	public int getMobKills() {
		return map.get(player.getName() + "-MobKills");
	}
}
