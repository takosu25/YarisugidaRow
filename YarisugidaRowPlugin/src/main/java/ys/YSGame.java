package ys;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import playerdata.YSPlayer;

public class YSGame {
	Plugin plugin;
	World world;
	Random rnd;
	
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
	
	public YSGame(Plugin plugin) {
		this.plugin = plugin;
		rnd = new Random();
		world = Bukkit.getServer().getWorld("battleRoom");
	}
	
	/**
	 * ゲーム開始の処理
	 */
	public void start() {
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
