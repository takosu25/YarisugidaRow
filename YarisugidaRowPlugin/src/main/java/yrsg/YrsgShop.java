package yrsg;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class YrsgShop {

	public static void OpenMonsterDropShop(Player player,boolean waribiki) {
		Merchant m = Bukkit.createMerchant("ショップ");
		List<MerchantRecipe> ml = new ArrayList<MerchantRecipe>();
		for(int i = 0 ; i < YrsgData.items.length ; i++) {
			ItemStack item = new ItemStack((Material)YrsgData.items[i][1]);
			ItemMeta itemm = item.getItemMeta();
			itemm.setDisplayName((String)YrsgData.items[i][0]);
			itemm.setLore(Discription((String)YrsgData.items[i][4]));
			item.setItemMeta(itemm);
			MerchantRecipe mr = new MerchantRecipe(item,10000);
			mr.addIngredient(new ItemStack(Material.EMERALD,(int)YrsgData.items[i][waribiki?3:2]));
			ml.add(mr);
		}
		/*
		MerchantRecipe mr4 = new MerchantRecipe(new ItemStack(Material.EMERALD,10),10000);
		ItemStack hakusi = new ItemStack(Material.PAPER);
		ItemMeta hakusim = hakusi.getItemMeta();
		hakusim.setDisplayName("白紙");
		hakusim.setLore(Discription("指定した人の持ち物を、弓と矢と「平和宣言」以外、全て消す。"));
		hakusi.setItemMeta(hakusim);
		mr4.addIngredient(hakusi);
		ml.add(mr4);
		*/
		m.setRecipes(ml);
		player.openMerchant(m, true);
	}

	public static void OpenMonsterDropShopWaribiki(Player player) {
		Merchant m = Bukkit.createMerchant("ショップ");
		List<MerchantRecipe> ml = new ArrayList<MerchantRecipe>();
		ItemStack light = new ItemStack(Material.GLOWSTONE_DUST);
		ItemMeta lightm = light.getItemMeta();
		lightm.setDisplayName("光の力");
		lightm.setLore(Discription("自分以外の全プレイヤーが10秒発光する。"));
		light.setItemMeta(lightm);
		MerchantRecipe mr1 = new MerchantRecipe(light,10000);
		mr1.addIngredient(new ItemStack(Material.EMERALD,3));
		ml.add(mr1);

		ItemStack option = new ItemStack(Material.BOOK);
		ItemMeta optionm = option.getItemMeta();
		optionm.setDisplayName("設定簿");
		optionm.setLore(Discription("今回存在する役職一覧を見ることが出来る。"));
		option.setItemMeta(optionm);
		MerchantRecipe mr2 = new MerchantRecipe(option,10000);
		mr2.addIngredient(new ItemStack(Material.EMERALD,6));
		ml.add(mr2);

		ItemStack inv = new ItemStack(Material.CYAN_DYE);
		ItemMeta invm = inv.getItemMeta();
		invm.setDisplayName("透明テクスチャ");
		invm.setLore(Discription("10秒間、透明になる。"));
		inv.setItemMeta(invm);
		MerchantRecipe mr3 = new MerchantRecipe(inv,10000);
		mr3.addIngredient(new ItemStack(Material.EMERALD,6));
		ml.add(mr3);
		/*
		MerchantRecipe mr4 = new MerchantRecipe(new ItemStack(Material.EMERALD,8),10000);
		ItemStack hakusi = new ItemStack(Material.PAPER);
		ItemMeta hakusim = hakusi.getItemMeta();
		hakusim.setDisplayName("白紙");
		hakusim.setLore(Discription("指定した人の持ち物を、弓と矢と「平和宣言」以外、全て消す。"));
		hakusi.setItemMeta(hakusim);
		mr4.addIngredient(hakusi);
		ml.add(mr4);
		*/
		m.setRecipes(ml);
		player.openMerchant(m, true);
	}


	public static List<String> Discription(String s){
		List<String> result = new ArrayList<String>();
		char[] cs = s.toCharArray();
		StringBuilder sb = new StringBuilder();
		for(char c: cs) {
			sb.append(c);
			if(sb.toString().length() >= 15) {
				result.add(ChatColor.WHITE + sb.toString());
				sb = new StringBuilder();
			}
		}
		if(sb.toString().length() >= 1) {
			result.add(ChatColor.WHITE + sb.toString());
		}
		return result;
	}
}
