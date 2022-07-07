package bluescreen9.minecraft.bukkit.cloaking;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class EventsHandler implements Listener{
					@EventHandler
					public void onPlayerJoin(PlayerJoinEvent event) {
						Player p = event.getPlayer();
						if (!Main.PlayerStatus.containsKey(p.getUniqueId().toString())) {
							Main.PlayerStatus.put(p.getUniqueId().toString(), Status.NORMAL);
						}
					}
					
					@EventHandler
					public void onPlayerDamage(EntityDamageByEntityEvent event) {
						if (Config.Attck) {
							return;
						}
						if (event.getDamager() instanceof Player) {
							Player p = (Player) event.getDamager();
							if (Main.PlayerStatus.get(p.getUniqueId().toString()) == Status.CLOAK || Main.PlayerStatus.get(p.getUniqueId().toString()) == Status.DECLOAKING) {
								event.setCancelled(true);
								event.setDamage(0.0D);
							}
						}
					}
					
					@EventHandler
					public void onPlayerAction(PlayerInteractEvent event) {
						if (Config.Action) {
							return;
						}
						Player p = event.getPlayer();
						if (Main.PlayerStatus.get(p.getUniqueId().toString()) == Status.CLOAK || Main.PlayerStatus.get(p.getUniqueId().toString()) == Status.DECLOAKING) {
								event.setCancelled(true);
						}
					}
					
					@EventHandler
					public void onEntityTarget(EntityTargetLivingEntityEvent event) {
						if (event.getTarget() instanceof Player) {
							Player p = (Player) event.getTarget();
							if (Main.PlayerStatus.get(p.getUniqueId().toString()) == Status.CLOAK) {
								event.setCancelled(true);
							}
						}
					}
					
					@EventHandler
					public void onPlayerRespawn(PlayerRespawnEvent event) {
						Main.deCloak(event.getPlayer());
					}
					
					@EventHandler
					public void onPlayerChangeWorld(PlayerPortalEvent event) {
						if (Config.ChangeWorld) {
							return;
						}
						Player p = event.getPlayer();
						if (Main.PlayerStatus.get(p.getUniqueId().toString()) == Status.CLOAK || Main.PlayerStatus.get(p.getUniqueId().toString()) == Status.DECLOAKING) {
								event.setCancelled(true);
						}
					}
					
					@EventHandler
					public void onPlayerTeleport(PlayerTeleportEvent event) {
						if (Config.Teleport) {
							return;
						}
						Player p = event.getPlayer();
						if (Main.PlayerStatus.get(p.getUniqueId().toString()) == Status.CLOAK || Main.PlayerStatus.get(p.getUniqueId().toString()) == Status.DECLOAKING) {
								event.setCancelled(true);
						}
					}
					
					@EventHandler
					public void onPlayerQuit(PlayerQuitEvent event) {
							Player player = event.getPlayer();
							if (Main.PlayerStatus.get(player.getUniqueId().toString()) != Status.NORMAL) {
								Main.deCloak(player);
							}
					}
}
