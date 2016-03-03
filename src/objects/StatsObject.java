package objects;

public class StatsObject {
	
	public int playerKills;
	public int deaths;
	public int blocksPlaced;
	public int blocksBroken;
	public int killStreak;
	public int mobKills;
	public int giveBook;
	
	public StatsObject(int playerKills, int deaths, int blocksPlaced, int blocksBroken, int killStreak, int mobKills, int giveBook) {
		this.playerKills = playerKills;
		this.deaths = deaths;
		this.blocksPlaced = blocksPlaced;
		this.blocksBroken = blocksBroken;
		this.killStreak = killStreak;
		this.mobKills = mobKills;
		this.giveBook = giveBook;
	}
}
