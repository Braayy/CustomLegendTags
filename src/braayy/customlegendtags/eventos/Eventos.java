package braayy.customlegendtags.eventos;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import braayy.customlegendtags.Main;

public class Eventos implements Listener {
	
	@EventHandler
	public void tagEvent(ChatMessageEvent e) {
		Player p = e.getSender();
		if (Main.config.contains("Jogador." + p.getUniqueId().toString())) {
			for (String tag : Main.config.getStringList("Jogador." + p.getUniqueId().toString())) {
				if (e.getTags().contains(tag)) {
					e.setTagValue(tag, Main.config.getString("Tags." + tag).replace('&', '§'));
				}
			}
		}
	}
	
}