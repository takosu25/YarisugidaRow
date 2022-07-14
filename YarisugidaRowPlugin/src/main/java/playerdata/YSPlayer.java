package playerdata;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import ys.YSData;

public class YSPlayer implements Listener{

	private Plugin plugin;
	private Player player;
	
	//役職
	private int jobID;
	//陣営
	private YSData.Zinei zinei;
	//スコア
	private int score = 0;
	//予約されたスコア
	private HashMap<String,Integer> reserve = new HashMap<String,Integer>();
	
	boolean stan = false;
	
	public YSPlayer(Plugin plugin,Player player) {
		this.plugin = plugin;
		this.player = player;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
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
		ItemStack sword = new ItemStack(Material.IRON_SWORD);
		ItemMeta swordm = sword.getItemMeta();
		swordm.addEnchant(Enchantment.DAMAGE_ALL, 100, true);
		sword.setItemMeta(swordm);
		ItemStack heiwa = new ItemStack(Material.STICK);
		ItemMeta heiwam = heiwa.getItemMeta();
		heiwam.setDisplayName("平和宣言");
		heiwa.setItemMeta(heiwam);
		player.getInventory().addItem(bow,sword,heiwa,new ItemStack(Material.ARROW));
	}
	
	public void setJob(int jobID) {
		this.jobID = jobID;
		this.zinei = (YSData.Zinei)YSData.jobs[jobID][2];
		player.setGameMode(GameMode.ADVENTURE);
		reserve.clear();
		for(PotionEffect pe: player.getActivePotionEffects()) {
			player.removePotionEffect(pe.getType());
		}
		jobCheck();
	}
	
	void jobCheck() {
		player.sendTitle(ChatColor.WHITE + "あなたは" + YSData.getCC(zinei) + YSData.getJobName(jobID), "役職確認タイム",20,200,20);
		player.sendMessage("");
		player.sendMessage("");
		player.sendMessage("--------------------");
		player.sendMessage("役職名：" + YSData.getJobName(jobID));
		player.sendMessage("能力：" + YSData.getJobDiscription(jobID));
		player.sendMessage("陣営：" + YSData.getZineiName(zinei));
		player.sendMessage("勝利条件：" + YSData.getZineiWin(zinei));
		stan(240);
	}
	
	void stan(int delayForTicks) {
		stan = true;
		new BukkitRunnable() {
			public void run() {
				stan = false;
			}
		}.runTaskLater(plugin, delayForTicks);
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(stan) {
			e.setCancelled(true);
		}
	}
	
	public int getScore() {
		return score;
	}
	public int getJob() {
		return jobID;
	}
	public YSData.Zinei getZinei(){
		return zinei;
	}
}
