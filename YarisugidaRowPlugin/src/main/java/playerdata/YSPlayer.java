package playerdata;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class YSPlayer {

	Plugin plugin;
	Player player;
	
	
	public YSPlayer(Plugin plugin,Player player) {
		this.plugin = plugin;
		this.player = player;
	}
	
	/**
	 * 最初のセットを行う
	 */
	public void startup() {
		player.getInventory().clear();
		ItemStack bow = new ItemStack(Material.BOW);
		ItemMeta bowm = bow.getItemMeta();
		bowm.addEnchant(Enchantment.ARROW_DAMAGE, 100, true);
		bowm.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		bow.setItemMeta(bowm);
		ItemStack heiwa = new ItemStack(Material.STICK);
		ItemMeta heiwam = heiwa.getItemMeta();
		heiwam.setDisplayName("平和宣言");
		heiwa.setItemMeta(heiwam);
		player.getInventory().addItem(bow,heiwa,new ItemStack(Material.ARROW));
	}
}
