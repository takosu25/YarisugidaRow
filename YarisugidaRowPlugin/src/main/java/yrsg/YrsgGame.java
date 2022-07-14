package yrsg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.Vector;

import yrsg.YrsgData.Zinei;


public class YrsgGame implements Listener{

	Plugin mainPlugin;
	int now = 0;
	int max = 8;
	int time = 60;
	int timeid;
	List<Player> players = new ArrayList<Player>();
	List<Player> livingplayers = new ArrayList<Player>();
	List<Player> sengen = new ArrayList<Player>();
	Player sengenfirst;
	HashMap<Player,Integer> scores = new HashMap<Player,Integer>();
	HashMap<Player,Integer> rules = new HashMap<Player,Integer>();
	HashMap<Player,List<YrsgSyusi>> syusis = new HashMap<Player,List<YrsgSyusi>>();
	boolean checktime = false;
	boolean game = false;
	boolean heiwa = false;
	Villager vv;
	Vector[] vs = {new Vector(-109,9,-6),new Vector(-109,9,-42),new Vector(-109,9,-58),new Vector(-134,9,-60),new Vector(-163,9,-57),
			new Vector(-168,9,-32)};
	World world;
	BossBar bb;

	public YrsgGame(Plugin plugin) {
		mainPlugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		world = Bukkit.getServer().getWorld("battleRoom");
	}

	public void CancelEvents() {
		PlayerMoveEvent.getHandlerList().unregister(this);
		EntityDamageByEntityEvent.getHandlerList().unregister(this);
		PlayerInteractEvent.getHandlerList().unregister(this);
		PlayerInteractEntityEvent.getHandlerList().unregister(this);
		vv.remove();
		for(Player p:players) {
			bb.removePlayer(p);
		}
	}

	public void Start() {
		/*
		world.setDifficulty(Difficulty.PEACEFUL);
		*/
		bb = Bukkit.createBossBar("制限時間",BarColor.GREEN,BarStyle.SOLID);
		bb.setProgress(1);
		bb.setVisible(true);
		Villager v = (Villager)world.spawnEntity(new Vector(-148,8,-20).toLocation(world), EntityType.VILLAGER);
		v.setCustomName("ショップ");
		v.setCustomNameVisible(true);
		v.setAI(false);
		vv = v;
		vv.setNoDamageTicks(999999999);
		/*
		for(Player p:players) {
			scores.put(p, 0);
			p.getInventory().clear();
			ItemStack bow = new ItemStack(Material.BOW);
			ItemMeta bowm = bow.getItemMeta();
			bowm.addEnchant(Enchantment.ARROW_DAMAGE, 100, true);
			bowm.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
			bow.setItemMeta(bowm);
			ItemStack heiwa = new ItemStack(Material.STICK);
			ItemMeta heiwam = heiwa.getItemMeta();
			heiwam.setDisplayName("平和宣言");
			heiwam.setLore(YrsgShop.Discription("右クリックで使用できる。平和宣言をする。5秒後に結果が出る。全員が同じ陣営の場合、自分に2ポイント。そうじゃない場合、自分に-2ポイント追加される。宣言は、残り時間が半分以下ではない時に使用すると、周りにばれる。"));
			heiwa.setItemMeta(heiwam);
			p.getInventory().addItem(bow,new ItemStack(Material.ARROW,1),heiwa,new ItemStack(Material.EMERALD,4));
			bb.addPlayer(p);
		}
		*/
		NewGame();
	}

	public void NewGame() {
		/*
		now++;
		//初期化
		heiwa = false;
		livingplayers.clear();
		sengen.clear();
		rules.clear();
		syusis.clear();
		*/
		List<Integer> occupied = new ArrayList<Integer>();
		Random rnd = new Random();
		checktime = true;
		game = true;
		time = players.size() * 30;
		/*
		for(Player p:players) {
			p.setGameMode(GameMode.ADVENTURE);
			for(PotionEffect pe : p.getActivePotionEffects()) {
				p.removePotionEffect(pe.getType());
			}
			List<YrsgSyusi> ys = new ArrayList<YrsgSyusi>();
			syusis.put(p, ys);
			int index = 0;
			do {
				index = rnd.nextInt(vs.length);
			}while(occupied.contains(index));
			occupied.add(index);
			p.teleport(vs[index].toLocation(world));
			int rule = rnd.nextInt(YrsgData.rule.length);
			rules.put(p, rule);
			p.sendTitle(ChatColor.WHITE + "あなたは" + YrsgData.GetCC((Zinei)YrsgData.rule[rule][2]) + (String)YrsgData.rule[rule][0], "役職確認タイム",20, 200, 20);
			p.sendMessage("");
			p.sendMessage("");
			p.sendMessage("-----------------------");
			int zineinumber = YrsgData.GetZinei((Zinei)YrsgData.rule[rule][2]);
			p.sendMessage(ChatColor.WHITE + "役職名：" +  YrsgData.GetCC((Zinei)YrsgData.rule[rule][2]) + (String)YrsgData.rule[rule][0]);
			p.sendMessage(ChatColor.WHITE + "能力：" + (String)YrsgData.rule[rule][1]);
			p.sendMessage(ChatColor.WHITE + "陣営：" + YrsgData.zineiName[YrsgData.GetZinei((Zinei)YrsgData.rule[rule][2])]);
			p.sendMessage(ChatColor.WHITE + "勝利条件：" + YrsgData.zineiWin[zineinumber]);
		}
		*/
		ReScoreboard();
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(mainPlugin, new Runnable() {
			public void run() {
				checktime = false;
				StartAbilities();
				for(Player p:players) {
					p.sendTitle("ゲームスタート！", now +  "/" + max,20,100,20);
					livingplayers.add(p);
				}
				timeid = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(mainPlugin, new Runnable() {
					public void run() {
						if(!game) {
							Bukkit.getServer().getScheduler().cancelTask(timeid);
							return;
						}
						time--;
						bb.setProgress((time / 60d));
						if(time == 0) {
							Bukkit.getServer().getScheduler().cancelTask(timeid);
							GameSet("無難日");
						}
					}
				}, 0, 20);
			}
		},240);
	}

	public void StartAbilities() {
		for(Player p:players) {
			switch(rules.get(p)) {
			//平和主義者
			case 4:
				int ind = -1;
				boolean result = true;
				for(Player ps:players) {
					int index = YrsgData.GetZinei((Zinei)YrsgData.rule[rules.get(ps)][2]);
					if(ind == -1) {
						ind = index;
					}else {
						if(ind != index) {
							result = false;
						}
					}
				}
				p.sendMessage("[平和主義者]" + (result?"平和宣言は成功します。":"平和宣言は失敗します。"));
				break;
			//瞬狼
			case 6:
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,100000,3));
				break;
			//占い師
			case 7:
				Player target = GetRandomPlayerWithoutMe(p);
				String resultn = YrsgData.zineiName[YrsgData.GetZinei((Zinei)YrsgData.rule[rules.get(target)][2])];
				if(rules.get(target) == 5) {
					resultn = "村人";
				}
				p.sendMessage(target.getDisplayName() + "は、" + resultn + "陣営です。");
				break;
			}
		}
	}

	public Player GetRandomPlayerWithoutMe(Player player) {
		List<Player> ps = new ArrayList<Player>();
		for(Player pss:players) {
			if(pss != player) {
				ps.add(pss);
			}
		}
		Collections.shuffle(ps);
		Player target = ps.get(0);
		return target;
	}

	/*
	public void JoinPlayer(Player player) {
		if(!players.contains(player)) {
			players.add(player);
			player.sendMessage("「それちょっと、やりすぎだ狼」に参加しました。");
		}
	}
	*/

	public void HeiwaSengen() {
		int ind = -1;
		boolean result = true;
		for(Player p:players) {
			int index = YrsgData.GetZinei((Zinei)YrsgData.rule[rules.get(p)][2]);
			if(ind == -1) {
				ind = index;
			}else {
				if(ind != index) {
					result = false;
				}
			}
		}
		if(result) {
			for(Player p:sengen) {
				List<YrsgSyusi> ys = syusis.get(p);
				ys.add(new YrsgSyusi(2,"平和宣言の成功"));
				syusis.put(p, ys);
			}
			GameSet("平和宣言成功");
		}else {
			for(Player p:sengen) {
				List<YrsgSyusi> ys = syusis.get(p);
				boolean firstbonus = false;
				if(sengen.size() >= 2 && sengenfirst == p) {
					firstbonus = true;
				}
				ys.add(new YrsgSyusi(firstbonus?-1:-2,"平和宣言の失敗"));
				syusis.put(p, ys);
			}
			GameSet("平和宣言失敗");
		}
	}

	public void AddPoint(Player player,int point, String cause) {
		List<YrsgSyusi> ys = syusis.get(player);
		ys.add(new YrsgSyusi(point,cause));
		syusis.put(player, ys);
	}

	public void GameSet(String cause) {
		for(Player p:players) {
			p.sendTitle("ゲーム終了！", "理由：" + cause,20,100,20);
			int amount = 2;
			amount = amount + (time / 10);
			if(rules.get(p) == 3) {
				amount = amount + 3;
			}
			p.getInventory().addItem(new ItemStack(Material.EMERALD,amount));
			if(cause.equals("無難日")) {
				if(rules.get(p) == 8) {
					for(Player pp:players) {
						if(pp != p) {
							pp.getInventory().addItem(new ItemStack(Material.EMERALD,2));
						}
					}
					AddPoint(p,1,"賄賂");
				}
				if(p.getInventory().contains(Material.LIME_DYE)) {
					AddPoint(p,1,"平和の恩恵");
				}
			}
			if(p.getInventory().contains(Material.LIME_DYE)) {
				ItemStack item = p.getInventory().getItem(p.getInventory().first(Material.LIME_DYE));
				ReduceItem(p,item);
			}
			p.sendMessage("<全員の役職>");
			for(Player pp:players) {
				p.sendMessage(pp.getDisplayName() + "：" + (String)YrsgData.rule[rules.get(pp)][0]);
			}
		}
		game = false;
		if(time != 0) {
			Bukkit.getServer().getScheduler().cancelTask(timeid);
		}
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(mainPlugin, new Runnable() {
			public void run() {
				for(Player p:players) {
					p.setGameMode(GameMode.SPECTATOR);
					int lastplus = 0;
					List<YrsgSyusi> ys = syusis.get(p);
					for(int i = 0 ; i < ys.size() ; i++) {
						YrsgSyusi oys = ys.get(i);
						lastplus = lastplus + oys.GetPlus();
						p.sendMessage(oys.GetCause() + "：" + oys.GetPlus() + "ポイント");
					}
					p.sendMessage("最終結果：" + lastplus + "ポイント追加");
					scores.put(p, scores.get(p) + lastplus);
				}
				ReScoreboard();
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(mainPlugin, new Runnable() {
					public void run() {
						if(now == max) {
							GameEnd();
						}else {
							NewGame();
						}
					}
				},100);
			}
		},140);
	}

	public void GameEnd(){
		int max = -9999;
		Player winner = null;
		for(Player p:players) {
			if(p.getInventory().contains(Material.BOWL)) {
				ItemStack item = p.getInventory().getItem(p.getInventory().first(Material.BOWL));
				scores.put(p, scores.get(p) + item.getAmount());
			}
		}
		ReScoreboard();
		for(Player p: players) {
			int score = scores.get(p);
			if(max < score) {
				max = score;
				winner = p;
			}
		}
		CancelEvents();
		for(Player p:players) {
			p.sendTitle(winner.getDisplayName() + "の勝利！","ポイント：" + max, 20, 100, 20);
			p.setGameMode(GameMode.SURVIVAL);
		}
	}

	public void Check() {
		int ind = -1;
		boolean result = true;
		for(Player p:livingplayers) {
			int index = YrsgData.GetZinei((Zinei)YrsgData.rule[rules.get(p)][2]);
			if(ind == -1) {
				ind = index;
			}else {
				if(ind != index) {
					result = false;
				}
			}
		}
		if(result) {
			if(ind == 0) {
				for(Player p:livingplayers) {
					List<YrsgSyusi> ys = syusis.get(p);
					ys.add(new YrsgSyusi(1,"村人陣営の勝利"));
					syusis.put(p, ys);
				}
				GameSet("村人陣営の平和の奪還");
			}else if(ind == 1) {
				for(Player p:livingplayers) {
					List<YrsgSyusi> ys = syusis.get(p);
					ys.add(new YrsgSyusi(1,"人狼陣営の勝利"));
					syusis.put(p, ys);
				}
				GameSet("人狼陣営の制圧");
			}
		}
	}



	@EventHandler
	public void PlayerInteract(PlayerInteractEvent e) {
		if(players.contains(e.getPlayer())) {
			Player player = (Player)e.getPlayer();
			if(!game) {
				return;
			}
			if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				ItemStack item = player.getInventory().getItemInMainHand();
				if(item.getItemMeta().getDisplayName().equals("平和宣言")) {
					if(!sengen.contains(player)) {
						player.sendMessage("平和宣言をしました。5秒後、結果が出ます。");
						if(!heiwa) {
							if(time <= 5) {
								player.sendMessage("残り時間が、5秒以内のため、使用できません。");
								return;
							}else{
								for(Player p:players) {
									p.sendTitle("平和宣言が出ました", player.getDisplayName(),10,80,10);
								}
							}
							sengenfirst = player;
							sengen.add(player);
							heiwa = true;
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(mainPlugin, new Runnable() {
								public void run() {
									if(game) {
										HeiwaSengen();
									}
								}
							},100);
						}else if(game){
							sengen.add(player);
						}
					}
				}else if(item.getItemMeta().getDisplayName().equals("光の力")) {
					ReduceItem(player,item);
					for(Player p:livingplayers) {
						p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,200,1));
					}
				}else if(item.getItemMeta().getDisplayName().equals("設定簿")) {
					ReduceItem(player,item);
					List<String> rulelist = new ArrayList<String>();
					for(Player p:players) {
						rulelist.add((String)YrsgData.rule[rules.get(p)][0]);
					}
					Collections.shuffle(rulelist);
					for(int i = 0 ; i < rulelist.size() ; i++) {
						player.sendMessage(rulelist.get(i));
					}
				}else if(item.getItemMeta().getDisplayName().equals("透明テクスチャ")) {
					ReduceItem(player,item);
					player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,200,1));
				}else if(item.getItemMeta().getDisplayName().equals("所持品サーチ")) {
					ReduceItem(player,item);
					Player target = GetRandomPlayerWithoutMe(player);
					Inventory inv = Bukkit.createInventory(null, 9, target.getDisplayName() + "の所持品");
					for(int i = 0 ; i < 9 ; i++) {
						Inventory in = target.getInventory();
						inv.setItem(i, in.getItem(i));
					}
					player.openInventory(inv);
				}else if(item.getItemMeta().getDisplayName().equals("偽平和宣言")) {
					ReduceItem(player,item);
					for(Player p:players) {
						p.sendTitle("平和宣言が出ました", player.getDisplayName(),10,80,10);
					}
				}else if(item.getItemMeta().getDisplayName().equals("延長チケット")) {
					ReduceItem(player,item);
					if(time <= 60) {
						time = time + 20;
						if(time >= 60) {
							time = 60;
						}
					}
				}else if(item.getItemMeta().getDisplayName().equals("ポケットショップ")) {
					ReduceItem(player,item);
					YrsgShop.OpenMonsterDropShop(player,rules.get(player)==2);
				}
			}
		}
	}

	public void ReduceItem(Player player,ItemStack item) {
		ItemStack itm = item.clone();
		player.getInventory().remove(item);
		if(itm.getAmount() != 1) {
			itm.setAmount(itm.getAmount() - 1);
			player.getInventory().addItem(itm);
		}
	}

	public void ReScoreboard() {
		ScoreboardManager sbm = Bukkit.getScoreboardManager();
		Scoreboard sb = sbm.getNewScoreboard();
		Objective ob = sb.registerNewObjective("Scores", "dummy","やりすぎだ狼");
		ob.setDisplaySlot(DisplaySlot.SIDEBAR);
		Score s = ob.getScore("<ポイント>");
		s.setScore(players.size());
		for(int i = 0 ; i < players.size() ; i++) {
			Score ps = ob.getScore(players.get(i).getDisplayName() + "：" + scores.get(players.get(i)));
			ps.setScore(i);
		}
		for(Player p:players) {
			p.setScoreboard(sb);
		}
	}

	@EventHandler
	public void PlayerMove(PlayerMoveEvent e) {
		if(players.contains(e.getPlayer())) {
			if(checktime) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void EntityDamage(EntityDamageByEntityEvent e) {
		if(players.contains(e.getEntity())) {
			Player target = (Player)e.getEntity();
			if(target.getHealth() - e.getDamage() <= 0) {
				e.setCancelled(true);
				livingplayers.remove(target);
				target.setGameMode(GameMode.SPECTATOR);
				if(e.getDamager() instanceof Projectile) {
					Projectile arrow = (Projectile)e.getDamager();
					if(players.contains(arrow.getShooter())) {
						Player player = (Player)arrow.getShooter();
						if((Zinei)YrsgData.rule[rules.get(player)][2] == (Zinei)YrsgData.rule[rules.get(target)][2]) {
							if(!player.getInventory().contains(Material.RED_DYE)) {
								AddPoint(player,-1,"誤殺");
							}else {
								ItemStack item = player.getInventory().getItem(player.getInventory().first(Material.RED_DYE));
								ReduceItem(player,item);
							}
							if(livingplayers.size() == 1) {
								GameSet("誤殺による孤独");
							}
						}else {
							Check();
						}
					}
				}
			}
		}else {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void PlayerInteractEntity(PlayerInteractEntityEvent e) {
		Player player = e.getPlayer();
		if(players.contains(player)) {
			if(e.getRightClicked() instanceof Villager) {
				Villager v = (Villager)e.getRightClicked();
				if(v.getCustomName().equals("ショップ")) {
					YrsgShop.OpenMonsterDropShop(player,rules.get(player)==2);
				}
			}
		}
	}
}
