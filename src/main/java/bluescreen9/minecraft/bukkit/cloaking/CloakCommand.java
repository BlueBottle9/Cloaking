package bluescreen9.minecraft.bukkit.cloaking;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;

public class CloakCommand implements TabExecutor{

	public List<String> onTabComplete(CommandSender sender, Command cmd, String lebel, String[] args) {
		return new ArrayList<String>();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String lebel, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(Main.Language.getDefaultLang(),lebel)));
			return true;
		}
		final Player player = (Player)sender;
		if (args.length != 0) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.wrongusage")));
			return true;
		}
		
		if (Main.PlayerStatus.get(player.getUniqueId().toString()) == Status.ClOAKING) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.warning.cloaking")));
			return true;
		}
		
		if (Main.PlayerStatus.get(player.getUniqueId().toString()) == Status.DECLOAKING) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.warning.decloaking")));
		}
		
		final BossBar bar = Bukkit.createBossBar("", BarColor.GREEN, BarStyle.SOLID, BarFlag.PLAY_BOSS_MUSIC);
		bar.removeFlag(BarFlag.PLAY_BOSS_MUSIC);
		bar.setVisible(true);
		bar.addPlayer(player);
		if (Main.PlayerStatus.get(player.getUniqueId().toString()) == Status.NORMAL) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.cloaking")));
			Main.PlayerStatus.replace(player.getUniqueId().toString(), Status.ClOAKING);
			final Timer timer = new Timer(Config.CloakingTime, new Runnable() {
				public void run() {
					if (player.getHealth() > 0) {
						new BukkitRunnable() {
							public void run() {
								Main.cloak(player);
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.cloaked")));
								bar.removePlayer(player);
							}
						}.runTask(Main.Cloaking);
					}
				}
			});
			timer.setTickTask(new Runnable() {
				public void run() {
					bar.setTitle(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "proccessbar.cloak")).replaceAll("<%cloak-time>",
							"" + timer.time));
					double progress = ((double)timer.time / (double)timer.totalTime);
					if (progress >= 0) {
						bar.setProgress(progress);
					}
				}
			});
			timer.start();
		}
		
		if (Main.PlayerStatus.get(player.getUniqueId().toString()) == Status.CLOAK) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.decloaking")));
			Main.PlayerStatus.replace(player.getUniqueId().toString(), Status.DECLOAKING);
			for (Player p:Bukkit.getServer().getOnlinePlayers()) {
				if (!p.getUniqueId().toString().equals(player.getUniqueId().toString())) {
					((CraftPlayer)p).getHandle().b.a(new PacketPlayOutNamedEntitySpawn(((CraftPlayer)player).getHandle()));
				}
			}
			
		final Timer timer2 =	new Timer(Config.DeCloakingTime, new Runnable() {
				public void run() {
						new BukkitRunnable() {
							public void run() {
								Main.deCloak(player);
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "command.decloaked")));
								bar.removePlayer(player);
							}
						}.runTask(Main.Cloaking);
				}
			});
		
		timer2.setTickTask(new Runnable() {
			public void run() {
				bar.setTitle(ChatColor.translateAlternateColorCodes('&', Main.Language.get(player, "proccessbar.decloak")).replaceAll("<%decloak-time>",
						"" + timer2.time));
				double progress = ((double)timer2.time / (double)timer2.totalTime);
				if (progress >= 0) {
					bar.setProgress(progress);
				}
			}
		});
		timer2.start();
				
		}
		return true;
	}

}
