package ys;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import playerdata.YSPlayer;

public class YSGame {
	//諸データ
	Plugin plugin;
	World world;
	Random rnd;
	BossBar bb;
	Villager v;
	//職業パックの有効化
	boolean[] packs = {false};
	
	//プレイヤー一覧
	List<Player> players = new ArrayList<Player>();
	//生存プレイヤー一覧
	List<Player> livings = new ArrayList<Player>();
	//プレイヤーデータ
	HashMap<Player,YSPlayer> playerdata = new HashMap<Player,YSPlayer>();
	
	//試合回数
	private int gameTime = 0;
	//最大試合回数
	private int gameTimeMax = 8;
	
	//ゲーム内時間
	int time;
	int maxTime;
	
	public YSGame(Plugin plugin) {
		this.plugin = plugin;
		rnd = new Random();
		world = Bukkit.getServer().getWorld("battleRoom");
	}
	
	/**
	 * ゲーム開始の処理
	 */
	public void start() {
		//時間制限の設定
		bb = Bukkit.createBossBar("制限時間",BarColor.GREEN,BarStyle.SOLID);
		bb.setProgress(1);
		bb.setVisible(true);
		//村人
		v = (Villager)world.spawnEntity(new Vector(-148,8,-20).toLocation(world), EntityType.VILLAGER);
		v.setCustomName("ショップ");
		v.setCustomNameVisible(true);
		v.setAI(false);
		v.setNoDamageTicks(999999);
		world.setDifficulty(Difficulty.PEACEFUL);
		for(Player p:players) {
			playerdata.get(p).startup();
		}
	}
	
	/**
	 * 新しい1日を始める
	 */
	void newday() {
		List<Integer> occupied = new ArrayList<Integer>();
		gameTime++;
		livings.clear();
		for(Player p: players) {
			//ゲーム開始地点にテレポートする
			int index = 0;
			do {
				index = rnd.nextInt(YSData.vs.length);
			}while(occupied.contains(index));
			occupied.add(index);
			p.teleport(YSData.vs[index].toLocation(world));
			//職業のランダム選択
			int id = selectJob();
			playerdata.get(p).setJob(id);
			livings.add(p);
		}
		timeStart();
		new BukkitRunnable() {
			public void run() {
				for(Player p:players) {
					p.sendTitle("ゲームスタート！", gameTime + "/" + gameTimeMax,20,100,20);
				}
				timeReduce();
			}
		}.runTaskLater(plugin, 240);
	}
	
	void timeStart() {
		bb.setProgress(1);
		bb.setTitle("残り" + time + "秒");
		time = 30 * players.size();
		if(time > 120) {
			time = 120;
		}
		maxTime = time;
	}
	void timeReduce() {
		time--;
		bb.setTitle("残り" + time + "秒");
		bb.setProgress((double)time/(double)maxTime);
		if(time == 0) {
			//TODO 無難日
		}else {
			new BukkitRunnable() {
				public void run() {
					timeReduce();
				}
			}.runTaskLater(plugin, 20);
		}
	}
	
	/**
	 * ランダムに役職を一つ取得する。
	 */
	int selectJob() {
		List<Integer> jobIDs = new ArrayList<Integer>();
		for(int i = 0 ; i < YSData.jobs.length ; i++) {
			if(i > 8 && !packs[0]) {
				continue;
			}
			jobIDs.add(i);
		}
		Collections.shuffle(jobIDs);
		return jobIDs.get(0);
	}
	
	/**
	 * ゲームにプレイヤーを参加させる処理。
	 * @param player 参加させたいプレイヤー。
	 */
	public void addPlayer(Player player) {
		if(!players.contains(player)) {
			players.add(player);
		}
	}
}
