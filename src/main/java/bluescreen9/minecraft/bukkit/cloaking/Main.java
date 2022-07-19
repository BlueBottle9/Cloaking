package bluescreen9.minecraft.bukkit.cloaking;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import bluescreen9.minecraft.bukkit.lang.Lang;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;

public class Main extends JavaPlugin{
			protected static Plugin Cloaking;
			protected static HashMap<String, Status> PlayerStatus = new HashMap<String, Status>();
			private static boolean Running;
			protected static Lang Language;
			@Override
			public void onEnable() {
				Cloaking = Main.getPlugin(Main.class);
				saveDefaultConfig();
				reloadConfig();
				Config.reloadConfig();
				getCommand("cloak").setExecutor(new CloakCommand());
				getCommand("cloak").setTabCompleter(new CloakCommand());
				getServer().getPluginManager().registerEvents(new EventsHandler(), Cloaking);
				Running = true;
				for (Player p:Bukkit.getServer().getOnlinePlayers()) {
						PlayerStatus.put(p.getUniqueId().toString(), Status.NORMAL);
				}
				Language = new Lang(Cloaking);
				Language.copyDeafultLangFile();
				Language.loadLanguages();
				Language.setDefaultLang(getConfig().getString("default-language"));
				new Thread() {
					@Override
					public void run() {
						while (Running) {
							for (Player p:Bukkit.getServer().getOnlinePlayers()) {
								Status ps = PlayerStatus.get(p.getUniqueId().toString());
								if (ps == Status.CLOAK) {
									for (Player p2:Bukkit.getServer().getOnlinePlayers()) {
										if (!p2.getUniqueId().toString().equals(p.getUniqueId().toString())) {
											((CraftPlayer)p2).getHandle().b.a(new PacketPlayOutEntityDestroy(p.getEntityId()));
										}
									}
								}
							}
							try {
								sleep(100L);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}.start();
			}
			
			@Override
			public void onDisable() {
				Running = false;
			}
			
			public static void cloak(Player player) {
				new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false, false).apply(player);
				for (Entity e:player.getWorld().getEntities()) {
					if (e instanceof Monster) {
						Monster m = ((Monster)e);
						if (m.getTarget() instanceof Player) {
							Player p = (Player) m.getTarget();
							if (p.getUniqueId().toString().equals(player.getUniqueId().toString())) {
								m.setTarget(null);
							}
						}
					}
					if (e instanceof Wither) {
						Wither w = (Wither) e;
						if (w.getTarget() instanceof Player) {
							Player p = (Player) w.getTarget();
							if (p.getUniqueId().toString().equals(player.getUniqueId().toString())) {
								w.setTarget(null);
							}
						}
					}
				}
				Main.PlayerStatus.replace(player.getUniqueId().toString(), Status.CLOAK);
			}
			
			public static void deCloak(Player player) {
				player.removePotionEffect(PotionEffectType.INVISIBILITY);
				for (Player p:Bukkit.getServer().getOnlinePlayers()) {
					if (!p.getUniqueId().toString().equals(player.getUniqueId().toString())) {
						((CraftPlayer)p).getHandle().b.a(new PacketPlayOutNamedEntitySpawn(((CraftPlayer)player).getHandle()));
					}
					for (Player p2:Bukkit.getServer().getOnlinePlayers()) {
						 p2.hidePlayer(Cloaking, player);
						 p2.showPlayer(Cloaking, player);
					}
				}
				Main.PlayerStatus.replace(player.getUniqueId().toString(), Status.NORMAL);
			}
			
			
}
