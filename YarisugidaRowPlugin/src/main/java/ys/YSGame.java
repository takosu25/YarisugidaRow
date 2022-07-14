package ys;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import playerdata.YSPlayer;

public class YSGame {
	Plugin plugin;
	World world;
	
	//職業パックの有効化
	boolean[] packs = {false};
	
	//プレイヤー一覧
	List<Player> players = new ArrayList<Player>();
	//プレイヤーデータ
	HashMap<Player,YSPlayer> playerdata = new HashMap<Player,YSPlayer>();
	
	public YSGame(Plugin plugin) {
		this.plugin = plugin;
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
