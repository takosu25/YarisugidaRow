package ys;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	@Override
	public void onEnable() {
		getCommand("yrsg").setExecutor(new YSCommand(this));
	}
}
