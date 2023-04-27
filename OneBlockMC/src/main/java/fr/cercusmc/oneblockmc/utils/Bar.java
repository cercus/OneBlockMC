package fr.cercusmc.oneblockmc.utils;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class Bar {
	
	private BossBar bossbar;
	private BarColor color;
	private BarStyle style;
	private double progress;
	private String text;
	private BarFlag[] flags;
	private boolean isVisible;
	
	public Bar(BarColor color, BarStyle style, double progress, String text, boolean isVisible, BarFlag... flags) {
		this.bossbar = Bukkit.createBossBar(text, color, style, flags);
		this.bossbar.setProgress(progress);
		this.bossbar.setVisible(isVisible);
		this.color = color;
		this.style = style;
		this.progress = progress;
		this.text = text;
	}
	
	public Bar(BarColor color, BarStyle style, String text) {
		this(color, style, 0.0, text, true, BarFlag.DARKEN_SKY);
	}
	
	public Bar(BarColor color, BarStyle style, String text, double progress) {
		this(color, style, progress, text, true, BarFlag.DARKEN_SKY);
	}
	
	public Bar addPlayer(Player p) {
		this.bossbar.addPlayer(p);
		return this;
	}
	
	public Bar removePlayer(Player p) {
		this.bossbar.removePlayer(p);
		return this;
	}
	
	public Bar addFlag(BarFlag flag) {
		this.bossbar.addFlag(flag);
		return this;
	}

	public BossBar getBossbar() {
		return bossbar;
	}

	public void setBossbar(BossBar bossbar) {
		this.bossbar = bossbar;
	}

	public BarColor getColor() {
		return color;
	}

	public void setColor(BarColor color) {
		this.color = color;
		this.bossbar.setColor(color);
	}

	public BarStyle getStyle() {
		return style;
	}

	public void setStyle(BarStyle style) {
		this.style = style;
		this.bossbar.setStyle(style);
	}

	public double getProgress() {
		return progress;
	}

	public void setProgress(double progress) {
		this.progress = progress;
		this.bossbar.setProgress(progress);
	}

	public String getText() {
		return text;
		
	}

	public void setText(String text) {
		this.text = text;
		this.bossbar.setTitle(text);
	}

	public BarFlag[] getFlags() {
		return flags;
	}

	public void setFlags(BarFlag[] flags) {
		this.flags = flags;
		for(BarFlag f : flags) {
			this.bossbar.addFlag(f);
		}
	}
	
	public boolean isVisible() {
		return isVisible;
	}
	
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

}
