package me.schwarzsky.satano;

import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GivePages extends ListenerAdapter {
	

	
	public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
		
		if(e.getAuthor().isBot()) {
			
		} else {
			
			if(Main.RANKS.containsKey(e.getAuthor().getId())) {
				try {
					
					int beforeAddPapers = Main.RANKS.get(e.getAuthor().getId());
					int lastPapers = Main.RANKS.get(e.getAuthor().getId());
					
					int randomPapers = Utils.getInteger(30);
					
					lastPapers += randomPapers;
					
					Main.RANKS.put(e.getAuthor().getId(), lastPapers);
					
					
					
					TextChannel logChannel = (TextChannel) e.getGuild().getTextChannelsByName("paperlogs", true).get(0);
					Emote Paper = e.getGuild().getEmotesByName("Paper", true).get(0);
					
					String logTemplate = "Stoled paper from satan: ``" + e.getAuthor().getAsTag() + "``\n" + "                          " + "``" + beforeAddPapers + " -> " + lastPapers +  "`` " +  Paper.getAsMention();
					
					
					logChannel.sendMessage(logTemplate).queue();
					logChannel.sendMessage("**--------------------------------------------------**").queue();
					
					Main.db.commit();
				} catch(Error error) {
					e.getChannel().sendMessage("Error: " + error).queue();;
				}
			} else {
				e.getChannel().sendMessage("You never stoled paper from satan. Really? It's funny, just try one time.");
				
				Main.RANKS.put(e.getAuthor().getId(), 20);
			}
			
		}
		
		
		
	}
	
}
