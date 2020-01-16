package me.schwarzsky.satano;

import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class rankCommand extends ListenerAdapter {
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		
		if(args[0].equalsIgnoreCase("-papers")) {
			
			if(Main.RANKS.containsKey(event.getAuthor().getId())) {
				int papers = Main.RANKS.get(event.getAuthor().getId());
				Emote Paper = event.getGuild().getEmotesByName("Paper", true).get(0);
				
				event.getChannel().sendMessage("You stoled " + papers + " " + Paper.getAsMention()).queue();
			} else {
				event.getChannel().sendMessage("You are not stoled papers yet. Try next time.");
			}
			
		}
	}
	
}
