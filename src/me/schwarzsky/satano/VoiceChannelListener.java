package me.schwarzsky.satano;

import java.util.Timer;
import java.util.TimerTask;

import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class VoiceChannelListener extends ListenerAdapter {
	
	static int minutesPassed = 0;
	
	static Timer paperTimer = new Timer();
	static TimerTask paperTimerTask = new TimerTask() {
		public void run() {
			
			for(VoiceChannel channel : Main.jda.getGuildById("482693670144049153").getVoiceChannels()) {
				
				
				System.out.println(channel.getName());
				
				for(Member channelMember: channel.getMembers()) {
					
					System.out.println(channelMember.getUser().getId());
					
					int beforeVoicePapers = Main.RANKS.get(channelMember.getUser().getId());
					int afterVoicePapers = Main.RANKS.get(channelMember.getUser().getId());
					
					afterVoicePapers += Utils.getInteger(60);
					
					Main.RANKS.put(channelMember.getUser().getId(), afterVoicePapers);
					
					Emote Paper = Main.jda.getGuildById("482693670144049153").getEmotesByName("Paper", true).get(0);
					
					Main.jda.getGuildById("482693670144049153").getTextChannelsByName("paperlogs", true).get(0).sendMessage("Stoled paper by voice activity: ``" + channelMember.getUser().getAsTag() + " " + beforeVoicePapers +  "->" + afterVoicePapers + "`` " + Paper.getAsMention()).queue();
					
					Main.db.commit();
					
				}
			}
			
			minutesPassed++;
			System.out.println("Minutes passed: " + minutesPassed);
		}
	};
}
