package playerdata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import ys.YSData;
import ys.YSData.Zinei;
import ys.YSGame;

public class YSPlayer implements Listener{

	private Plugin plugin;
	private Player player;
	private YSGame ysg;
	
	//役職
	private int jobID;
	//陣営
	private YSData.Zinei zinei;
	//スコア
	private int score = 0;
	//予約されたスコア
	private HashMap<String,Integer> reserve = new HashMap<String,Integer>();
	//動けなくする
	boolean stan = false;
	
	public YSPlayer(Plugin plugin,Player player,YSGame ysg) {
		this.plugin = plugin;
		this.player = player;
		this.ysg = ysg;
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
	
	public void startAbility() {
		switch(jobID) {
		//平和宣言
		case 4:
			if(ysg.getPlayers().size() == ysg.getPlayersFromZinei(Zinei.Murabito).size()) {
				player.sendMessage(ChatColor.GREEN + "平和宣言は成功します！");
			}else {
				player.sendMessage(ChatColor.RED + "平和宣言は失敗します。");
			}
			break;
		//瞬狼
		case 6:
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,999999,2));
			break;
		//占い師
		case 7:
			List<Player> random = new ArrayList<Player>();
			random.addAll(ysg.getPlayersWithout(player));
			Collections.shuffle(random);
			Player target = random.get(0);
			player.sendMessage(target.getDisplayName() + "は" + ysg.getPlayerData(target).getZineiInGame() + "陣営" + ChatColor.WHITE + "です。");
			break;
		}
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
	
	String getZineiInGame() {
		String result = YSData.getZineiName(zinei);
		if(jobID == 5) {
			result = YSData.getZineiName(Zinei.Murabito);
		}
		return result;
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
	public void addScore(int score) {
		this.score += score;
	}
	public int getJob() {
		return jobID;
	}
	public YSData.Zinei getZinei(){
		return zinei;
	}
	public void setReserve(String contents,int point) {
		reserve.put(contents, point);
	}
	public void reserveToScore() {
		player.sendMessage(ChatColor.GOLD + "-----ポイント集計-----");
		int sum = 0;
		for(String s: reserve.keySet()) {
			int score = reserve.get(s);
			player.sendMessage((score<0?ChatColor.RED:ChatColor.GREEN) + s + "：" + score);
			sum += score;
		}
		player.sendMessage(ChatColor.GOLD + "合計：" + sum);
		addScore(sum);
	}
}
