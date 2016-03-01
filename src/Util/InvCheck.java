package util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import com.zeshanaslam.bookstats.Main;

public class InvCheck {
	
	public static boolean canGiveBook(Player player) {
		for (ItemStack item: player.getInventory()) {
			if (item != null && item.getType() == Material.WRITTEN_BOOK) {
				if (item.hasItemMeta()) {

					BookMeta bm = (BookMeta) item.getItemMeta();

					if (bm.getAuthor().equalsIgnoreCase(Main.inst().author))
						return false;
				}
			}
		}
		return true;
	}
}
