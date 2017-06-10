package braayy.customlegendtags;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import braayy.customlegendtags.eventos.Eventos;

public class Main extends JavaPlugin {
	
	public static FileConfiguration config;
	
	@Override
	public void onEnable() {
		if (getServer().getPluginManager().getPlugin("Legendchat") == null) {
			getServer().getConsoleSender().sendMessage("§cLegendChat nao foi encontrado. Reveja sua pasta plugins");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		saveDefaultConfig();
		getServer().getPluginManager().registerEvents(new Eventos(), this);
		config = getConfig();
		getServer().getConsoleSender().sendMessage("§aO CustomLegendTags foi iniciado com sucesso");
	}
	
	@Override
	public void onDisable() {
		HandlerList.unregisterAll(this);
		getServer().getConsoleSender().sendMessage("§cO CustomLegendTags foi desativado com sucesso");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("clt")) {
			if (!sender.hasPermission("clt.use")) {
				sender.sendMessage("§cVoce nao tem permissao para isso");
				return true;
			}
			
			if (args.length < 1) {
				sender.sendMessage("§cFalta argumentos!");
				sender.sendMessage("§cUse /clt <jogador>");
				sender.sendMessage("§cUse /clt <jogador> add <tag>");
				sender.sendMessage("§cUse /clt <jogador> remove <tag>");
				if (sender.hasPermission("clt.reload"))
					sender.sendMessage("§cUse /clt reload");
				return true;
			}
			
			if (args[0].equalsIgnoreCase("reload")) {
				if (sender.hasPermission("clt.reload")) {
					reloadConfig();
					config = getConfig();
					sender.sendMessage("§aVoce recarregou a config.");
				} else {
					sender.sendMessage("§cVoce nao tem permissao para isso");
				}
				return true;
			}
			
			UUID id = getUUID(args[0]);
			
			if (args.length == 1) {
				if (!sender.hasPermission("clt.list")) {
					sender.sendMessage("§cVoce nao tem permissao para isso");
					return true;
				}
				if (config.contains("Jogador." + id.toString())) {
					List<String> tags = config.getStringList("Jogador." + id.toString());
					if (tags.isEmpty()) {
						sender.sendMessage("§cO Jogador " + args[0] + " nao tem tags");
						return true;
					}
					sender.sendMessage("§aTags do Jogador " + args[0] + ":");
					for (String tag : tags) {
						if (config.contains("Tags." + tag))
							sender.sendMessage("§a- " + tag + " ["+config.getString("Tags." + tag).replace('&', '§')+"]");
					}
				} else {
					sender.sendMessage("§cO Jogador " + args[0] + " nao tem tags");
				}
				return true;
			}
			
			if (args[1].equalsIgnoreCase("add")) {
				if (!sender.hasPermission("clt.add")) {
					sender.sendMessage("§cVoce nao tem permissao para isso");
					return true;
				}
				if (args.length < 3) {
					sender.sendMessage("§cUse /clt "+args[0]+" add <tag>");
					return true;
				}
				if (!config.contains("Tags." + args[2])) {
					sender.sendMessage("§cTag desconhecida");
					return true;
				}
				if (!config.contains("Jogador." + id.toString())) config.set("Jogador." + id.toString(), new ArrayList<>());
				List<String> tags = config.getStringList("Jogador." + id.toString());
				if (tags.contains(args[2])) {
					sender.sendMessage("§cO Jogador " + args[0] + " ja tem a Tag " + args[2]);
					return true;
				}
				tags.add(args[2]);
				config.set("Jogador." + id.toString(), tags);
				saveConfig();
				sender.sendMessage("§aVoce adicionou a Tag " + args[2] + " ao Jogador " + args[0]);
			} else if (args[1].equalsIgnoreCase("remove")) {
				if (!sender.hasPermission("clt.remove")) {
					sender.sendMessage("§cVoce nao tem permissao para isso");
					return true;
				}
				if (args.length < 3) {
					sender.sendMessage("§cUse /clt "+args[0]+" remove <tag>");
					return true;
				}
				List<String> tags = config.getStringList("Jogador." + id.toString());
				if (!tags.contains(args[2])) {
					sender.sendMessage("§cO Jogador " + args[0] + " nao tem a Tag " + args[2]);
					return true;
				}
				tags.remove(args[2]);
				if (tags.size() == 0) config.set("Jogador." + id.toString(), null);
				else config.set("Jogador." + id.toString(), tags);
				saveConfig();
				sender.sendMessage("§aVoce removeu a Tag " + args[2] + " ao Jogador " + args[0]);
			} else {
				sender.sendMessage("§cSub comando desconhecido");
			}
			
		}
		
		return false;
	}
	
	@SuppressWarnings("deprecation")
	private static UUID getUUID(String pname) {
		if (Bukkit.getServer().getPlayer(pname) != null) return Bukkit.getServer().getPlayer(pname).getUniqueId();
		else return Bukkit.getServer().getOfflinePlayer(pname).getUniqueId();
	}
	
}