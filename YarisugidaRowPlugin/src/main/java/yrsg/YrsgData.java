package yrsg;

import org.bukkit.Material;

import net.md_5.bungee.api.ChatColor;

public class YrsgData {
	//[0]役職名
	//[1]役職の内容
	//[2]陣営
	public static Object[][] rule = {
			//0
			{"村人","無能力",Zinei.Murabito},
			{"人狼","無能力",Zinei.Zinrou},
			{"商人","ショップで、商品を格安で購入できる。",Zinei.Murabito},
			{"富豪","自分が死亡せずにゲームが終了した時、金貨を3獲得できる。",Zinei.Murabito},
			{"平和主義者","ゲーム開始時に平和宣言が成功するかどうか分かる。",Zinei.Murabito},
			//5
			{"狂人","占われると、村人陣営だと出る。",Zinei.Zinrou},
			{"瞬狼","足が速い。",Zinei.Zinrou},
			{"占い師","ゲーム開始時、ランダムで自分以外の誰か一人の陣営が分かる。",Zinei.Murabito},
			{"賂人","無難日になった場合、1ポイント入手できるが、自分以外のプレイヤーは2エメラルド入手できる。",Zinei.Midium}};

	//[0]アイテム名
	//[1]見た目
	//[2]値段
	//[3]商人値段
	//[4]効果名
	public static Object[][] items = {
			{"所持品サーチ",Material.BLUE_DYE,1,1,"右クリックで使用する。自分以外のランダムなプレイヤーの持ち物を見ることが出来る。"},
			{"ポケットショップ",Material.LEATHER,1,1,"右クリックで使用する。その場でショップを開く。"},
			{"光の力",Material.GLOWSTONE_DUST,2,1,"右クリックで使用する。自分以外の全プレイヤーが10秒間発光する。"},
			{"延長チケット",Material.PAPER,2,1,"右クリックで使用する。制限時間を20秒延ばすことができる。このアイテムによって制限時間が60秒を超えることはない。"},
			{"偽平和宣言",Material.BLAZE_ROD,4,3,"右クリックで使用する。平和宣言をしたという偽のタイトルを全員に流すことが出来る。"},
			{"透明テクスチャ",Material.CYAN_DYE,4,3,"右クリックで使用する。10秒間、透明になることができる。"},
			{"平和の恩恵",Material.LIME_DYE,7,5,"1試合終了後、このアイテムが消費される。もし、その試合が「無難日」だった場合、1ポイント入手できる。"},
			{"誤殺保険",Material.RED_DYE,8,7,"誤殺をしても、このアイテムを消費して1度だけ自分のポイントが消費されない。"},
			{"設定簿",Material.BOOK,9,7,"右クリックで使用する。今回のゲームで存在する全役職を知ることができる。"},
			{"積立",Material.BOWL,25,15,"ゲーム終了時、このアイテムを持っている数分、ポイントを取得できる。"}};

	public static enum Zinei {
		Murabito,Zinrou,Midium
	};

	public static String[] zineiName = {"村人","人狼","中立"};
	public static String[] zineiWin = {"別陣営の全滅","別陣営の全滅","なし"};

	public static ChatColor GetCC(Zinei z) {
		switch(z) {
		case Murabito:
			return ChatColor.GREEN;
		case Zinrou:
			return ChatColor.RED;
		case Midium:
			return ChatColor.LIGHT_PURPLE;
		default:
			return ChatColor.WHITE;
		}
	}

	public static int GetZinei(Zinei z) {
		switch(z) {
		case Murabito:
			return 0;
		case Zinrou:
			return 1;
		case Midium:
			return 2;
		default:
			return -1;
		}
	}
}
