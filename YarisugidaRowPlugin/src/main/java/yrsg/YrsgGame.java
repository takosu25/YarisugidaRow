package yrsg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.boss.BossBar;
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

	public void NewGame() {
		ReScoreboard();
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(mainPlugin, new Runnable() {
			public void run() {
				checktime = false;
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
							GameSet("?????????");
						}
					}
				}, 0, 20);
			}
		},240);
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
				ys.add(new YrsgSyusi(2,"?????????????????????"));
				syusis.put(p, ys);
			}
			GameSet("??????????????????");
		}else {
			for(Player p:sengen) {
				List<YrsgSyusi> ys = syusis.get(p);
				boolean firstbonus = false;
				if(sengen.size() >= 2 && sengenfirst == p) {
					firstbonus = true;
				}
				ys.add(new YrsgSyusi(firstbonus?-1:-2,"?????????????????????"));
				syusis.put(p, ys);
			}
			GameSet("??????????????????");
		}
	}

	public void AddPoint(Player player,int point, String cause) {
		List<YrsgSyusi> ys = syusis.get(player);
		ys.add(new YrsgSyusi(point,cause));
		syusis.put(player, ys);
	}

	public void GameSet(String cause) {
		for(Player p:players) {
			p.sendTitle("??????????????????", "?????????" + cause,20,100,20);
			int amount = 2;
			amount = amount + (time / 10);
			if(rules.get(p) == 3) {
				amount = amount + 3;
			}
			p.getInventory().addItem(new ItemStack(Material.EMERALD,amount));
			if(cause.equals("?????????")) {
				if(rules.get(p) == 8) {
					for(Player pp:players) {
						if(pp != p) {
							pp.getInventory().addItem(new ItemStack(Material.EMERALD,2));
						}
					}
					AddPoint(p,1,"??????");
				}
				if(p.getInventory().contains(Material.LIME_DYE)) {
					AddPoint(p,1,"???????????????");
				}
			}
			if(p.getInventory().contains(Material.LIME_DYE)) {
				ItemStack item = p.getInventory().getItem(p.getInventory().first(Material.LIME_DYE));
				ReduceItem(p,item);
			}
			p.sendMessage("<???????????????>");
			for(Player pp:players) {
				p.sendMessage(pp.getDisplayName() + "???" + (String)YrsgData.rule[rules.get(pp)][0]);
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
						p.sendMessage(oys.GetCause() + "???" + oys.GetPlus() + "????????????");
					}
					p.sendMessage("???????????????" + lastplus + "??????????????????");
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
		for(Player p:players) {
			p.sendTitle(winner.getDisplayName() + "????????????","???????????????" + max, 20, 100, 20);
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
					ys.add(new YrsgSyusi(1,"?????????????????????"));
					syusis.put(p, ys);
				}
				GameSet("??????????????????????????????");
			}else if(ind == 1) {
				for(Player p:livingplayers) {
					List<YrsgSyusi> ys = syusis.get(p);
					ys.add(new YrsgSyusi(1,"?????????????????????"));
					syusis.put(p, ys);
				}
				GameSet("?????????????????????");
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
				if(item.getItemMeta().getDisplayName().equals("????????????")) {
					if(!sengen.contains(player)) {
						player.sendMessage("??????????????????????????????5??????????????????????????????");
						if(!heiwa) {
							if(time <= 5) {
								player.sendMessage("??????????????????5?????????????????????????????????????????????");
								return;
							}else{
								for(Player p:players) {
									p.sendTitle("???????????????????????????", player.getDisplayName(),10,80,10);
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
				}else if(item.getItemMeta().getDisplayName().equals("?????????")) {
					ReduceItem(player,item);
					for(Player p:livingplayers) {
						p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,200,1));
					}
				}else if(item.getItemMeta().getDisplayName().equals("?????????")) {
					ReduceItem(player,item);
					List<String> rulelist = new ArrayList<String>();
					for(Player p:players) {
						rulelist.add((String)YrsgData.rule[rules.get(p)][0]);
					}
					Collections.shuffle(rulelist);
					for(int i = 0 ; i < rulelist.size() ; i++) {
						player.sendMessage(rulelist.get(i));
					}
				}else if(item.getItemMeta().getDisplayName().equals("?????????????????????")) {
					ReduceItem(player,item);
					player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,200,1));
				}else if(item.getItemMeta().getDisplayName().equals("??????????????????")) {
					ReduceItem(player,item);
					Player target = GetRandomPlayerWithoutMe(player);
					Inventory inv = Bukkit.createInventory(null, 9, target.getDisplayName() + "????????????");
					for(int i = 0 ; i < 9 ; i++) {
						Inventory in = target.getInventory();
						inv.setItem(i, in.getItem(i));
					}
					player.openInventory(inv);
				}else if(item.getItemMeta().getDisplayName().equals("???????????????")) {
					ReduceItem(player,item);
					for(Player p:players) {
						p.sendTitle("???????????????????????????", player.getDisplayName(),10,80,10);
					}
				}else if(item.getItemMeta().getDisplayName().equals("??????????????????")) {
					ReduceItem(player,item);
					if(time <= 60) {
						time = time + 20;
						if(time >= 60) {
							time = 60;
						}
					}
				}else if(item.getItemMeta().getDisplayName().equals("????????????????????????")) {
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
		Objective ob = sb.registerNewObjective("Scores", "dummy","??????????????????");
		ob.setDisplaySlot(DisplaySlot.SIDEBAR);
		Score s = ob.getScore("<????????????>");
		s.setScore(players.size());
		for(int i = 0 ; i < players.size() ; i++) {
			Score ps = ob.getScore(players.get(i).getDisplayName() + "???" + scores.get(players.get(i)));
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
								AddPoint(player,-1,"??????");
							}else {
								ItemStack item = player.getInventory().getItem(player.getInventory().first(Material.RED_DYE));
								ReduceItem(player,item);
							}
							if(livingplayers.size() == 1) {
								GameSet("?????????????????????");
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
				if(v.getCustomName().equals("????????????")) {
					YrsgShop.OpenMonsterDropShop(player,rules.get(player)==2);
				}
			}
		}
	}
}
