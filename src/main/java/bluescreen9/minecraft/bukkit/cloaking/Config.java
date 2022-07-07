package bluescreen9.minecraft.bukkit.cloaking;

public class Config {
			public static int CloakingTime;
			public static int DeCloakingTime;
			public static boolean Attck;
			public static boolean Action;
			public static boolean Teleport;
			public static boolean ChangeWorld;
			
			public static void reloadConfig() {
				Main.Cloaking.saveDefaultConfig();
				Main.Cloaking.reloadConfig();
				CloakingTime = Main.Cloaking.getConfig().getInt("cloaking-delay");
				DeCloakingTime = Main.Cloaking.getConfig().getInt("decloaking-delay");
				Attck = Main.Cloaking.getConfig().getBoolean("cloak.attck");
				Action = Main.Cloaking.getConfig().getBoolean("clock.action");
				Teleport = Main.Cloaking.getConfig().getBoolean("cloaking.teleport");
				ChangeWorld = Main.Cloaking.getConfig().getBoolean("cloak.changeworld");
			}
}
