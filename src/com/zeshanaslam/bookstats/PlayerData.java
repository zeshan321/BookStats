package com.zeshanaslam.bookstats;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import objects.StatsObject;

public class PlayerData
{
	public UUID uuid;
	public static HashMap<UUID, StatsObject> map = new HashMap<UUID, StatsObject>();

	public PlayerData(UUID uuid)
	{
		this.uuid = uuid;
		
		if (!map.containsKey(uuid)) {
			map.put(uuid, new StatsObject(0, 0, 0, 0, 0, 0, 0));
		}
	}

	public UUID getUUID() {
		return uuid;
	}

	public void addDeaths() {
		StatsObject stats = map.get(uuid);
		stats.deaths++;
		
		map.put(uuid, stats);
	}

	public void setDeaths(int i) {
		StatsObject stats = map.get(uuid);
		stats.deaths = i;
		
		map.put(uuid, stats);
	}

	public int getDeaths() {
		return map.get(uuid).deaths;
	}

	public void addKills() {		
		StatsObject stats = map.get(uuid);
		stats.playerKills++;
		
		map.put(uuid, stats);
	}

	public void setKills(int i) {
		StatsObject stats = map.get(uuid);
		stats.playerKills = i;
		
		map.put(uuid, stats);
	}

	public int getKills() {
		return map.get(uuid).playerKills;
	}

	public void addPlacedBlocks() {
		StatsObject stats = map.get(uuid);
		stats.blocksPlaced++;
		
		map.put(uuid, stats);
	}

	public void setPlacedBlocks(int i) {
		StatsObject stats = map.get(uuid);
		stats.blocksPlaced = i;
		
		map.put(uuid, stats);
	}

	public int getPlacedBlocks() {
		return map.get(uuid).blocksPlaced;
	}

	public void addBrokenBlocks() {
		StatsObject stats = map.get(uuid);
		stats.blocksBroken++;
		
		map.put(uuid, stats);
	}

	public void setBrokenBlocks(int i) {
		StatsObject stats = map.get(uuid);
		stats.blocksBroken = i;
		
		map.put(uuid, stats);
	}

	public int getBrokenBlocks() {
		return map.get(uuid).blocksBroken;
	}

	public void addKillStreak() {
		StatsObject stats = map.get(uuid);
		stats.killStreak++;
		
		map.put(uuid, stats);
	}

	public void setKillStreak(int i) {
		StatsObject stats = map.get(uuid);
		stats.killStreak = i;
		
		map.put(uuid, stats);
	}

	public void resetKillStreak() {
		StatsObject stats = map.get(uuid);
		stats.killStreak = 0;
		
		map.put(uuid, stats);
	}

	public int getKillStreak() {
		return map.get(uuid).killStreak;
	}

	public void addMobKills() {
		StatsObject stats = map.get(uuid);
		stats.mobKills++;
		
		map.put(uuid, stats);
	}

	public void setMobKills(int i) {
		StatsObject stats = map.get(uuid);
		stats.mobKills = i;
		
		map.put(uuid, stats);
	}

	public int getMobKills() {
		return map.get(uuid).mobKills;
	}
	
	public void setGiveBook(int i) {
		StatsObject stats = map.get(uuid);
		stats.giveBook = i;
		
		map.put(uuid, stats);
	}

	public boolean canGiveBook() {
		int i = map.get(uuid).giveBook;
		
		if (i == 1) {
			return false;
		}
		return true;
	}
	
	public int getGiveBook() {
		return map.get(uuid).giveBook;
	}
}
