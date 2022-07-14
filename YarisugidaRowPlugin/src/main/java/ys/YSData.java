package ys;

public class YSData {

	//役職一覧
	public static Object[][] jobs = {
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
			{"賂人","無難日になった場合、1ポイント入手できるが、自分以外のプレイヤーは2エメラルド入手できる。",Zinei.Midium}
	};
	public static enum Zinei {
		Murabito,Zinrou,Midium
	};
	public static String[] zineiName = {"村人","人狼","中立"};
	public static String[] zineiWin = {"別陣営の全滅","別陣営の全滅","なし"};
	
	//役職パック
	public static String[][] packs = {
			//Vol1
			{"洗脳者(村人)","スリ(人狼)","クリーパー(中立)","偽善平和主義者(中立)","カニバリスト(中立)"}	
	};
}
