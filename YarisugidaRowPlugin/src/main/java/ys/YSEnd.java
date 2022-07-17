package ys;

import org.bukkit.entity.Player;

public class YSEnd {

	YSGame ysg;
	int causeID;
	String cause;
	
	public YSEnd(YSGame ysg,int causeID) {
		this.ysg = ysg;
		this.causeID = causeID;
		check();
		end();
	}
	
	void check() {
		switch(causeID) {
		case 0:
			bunan();
			break;
		}
	}
	
	void end() {
		ysg.gameset(cause);
		
	}
	
	//無難日
	void bunan() {
		cause = "無難日";
		//「賂人」の無難日ポイント
		for(Player p:ysg.getPlayersFromJob(8)) {
			ysg.getPlayerData(p).setReserve("仕組まれた無難日", 1);
		}
	}
}
