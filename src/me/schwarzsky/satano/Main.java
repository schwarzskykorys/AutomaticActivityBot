package me.schwarzsky.satano;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentMap;

import javax.security.auth.login.LoginException;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import me.schwarzsky.satano.GivePages;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Main {
	
	public static JDA jda;
	
	public static DB db = DBMaker.fileDB("file.db").transactionEnable().closeOnJvmShutdown().make();
	public static ConcurrentMap<String, Integer> RANKS = db.hashMap("RANKS", Serializer.STRING, Serializer.INTEGER).createOrOpen();
	
	
	
	
	public static void main(String[] args) throws LoginException{
		jda = new JDABuilder(AccountType.BOT).setToken("").build();
		
		jda.addEventListener(new GivePages());
		jda.addEventListener(new rankCommand());
		jda.addEventListener(new VoiceChannelListener());
		  
	    DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("HH");  
	    LocalDateTime now = LocalDateTime.now();
	    
	    ConcurrentMap<String, Integer> TIMER = db.hashMap("TIMER", Serializer.STRING, Serializer.INTEGER).createOrOpen();
	    
	    Timer rankTimer = new Timer();
		TimerTask rankTimerTask = new TimerTask() {

			@Override
			public void run() {
				if(dateTimeFormat.format(now).equalsIgnoreCase("00")) {
					
					if(TIMER.containsKey("day")) {
						int day = TIMER.get("day");
						
						System.out.println("Day is " + day + " " + now);
						
						if(day == 7) {
							// TODO: Give role the first three and clear the timer table.
							
							
							for(String s : RANKS.keySet()) {
								int clearedPaper = RANKS.get(s);
								
								System.out.println("Cleared " + s + " and " + clearedPaper);
								
								RANKS.remove(s);
							}
								
								
						} else {
							
							System.out.println("Day wasn't 7." + TIMER.get("day") + " " + now);
							
						}
						
					} else {
						TIMER.put("day", 0);
						
						System.out.println("Day was null, i set the day 0. " + now + " " + TIMER.get("day"));
					}
				}
				
			}
			
		};
	    
	    System.out.println(dateTimeFormat.format(now));
	    
	    rankTimer.scheduleAtFixedRate(rankTimerTask, 60000, 60000);
		
		VoiceChannelListener.paperTimer.scheduleAtFixedRate(VoiceChannelListener.paperTimerTask, 65000, 65000);
		
	}
	
	public void onDisable() {
		db.close();
	}
	

}
