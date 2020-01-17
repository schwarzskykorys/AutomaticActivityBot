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
import java.util.stream.Collectors;

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
	    DateTimeFormatter getMinutes = DateTimeFormatter.ofPattern("mm");
	    
	    
	    
	    
	    ConcurrentMap<String, Integer> TIMER = db.hashMap("TIMER", Serializer.STRING, Serializer.INTEGER).createOrOpen();
	    ConcurrentMap<String, String> FIRST_THREE = db.hashMap("FIRST_THREE", Serializer.STRING, Serializer.STRING).createOrOpen();
	    
	    Timer rankTimer = new Timer();
		TimerTask rankTimerTask = new TimerTask() {

			@Override
			public void run() {
				LocalDateTime now = LocalDateTime.now();
				String minutes = getMinutes.format(now);
				
				if(TIMER.containsKey("day")) {
					int notificationDay = TIMER.get("day");
					
					System.out.println("[RankTimer] Day is " + notificationDay + " and time is " + dateTimeFormat.format(now) + ":" + minutes);
					
					
				} else {
					TIMER.put("day", 0);
					
					System.out.println("[RankTimer] I put day to table:  " + TIMER.get("day"));
					
					// db.commit();
				}
				
				if(dateTimeFormat.format(now).equalsIgnoreCase("00")) {
					
					System.out.println("[RankTimer] Time is equal to 00");
					System.out.println("[RankTimer] Time is equal to 00");
					System.out.println("[RankTimer] Time is equal to 00");
					
					
					if(TIMER.containsKey("day")) {
						int day = TIMER.get("day");
						
						if(minutes.equalsIgnoreCase("01")) {
							int nonDay = TIMER.get("day");
							
							System.out.println("[RankTimer] Day is " + day + " " + now + " " + minutes);
							
							day += 1;
							
							TIMER.put("day", day);
							
							// db.commit();
							
							System.out.println("[RankTimer] Day Change "  + nonDay + " -> " + day);
						} else {
							System.out.println("[RankTimer] Minute is not equal to 01.");
						}
						
						
						if(day == 7) {
							// TODO: Give role the first three and clear the timer table.
							
							if(minutes.equalsIgnoreCase("01")) {
								// [SYSTEM]
								int leaderboardCount = 0;
								
								Map<String, Integer> sorted = RANKS
								        .entrySet()
								        .stream()
								        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
								        .collect(
								            Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
								                LinkedHashMap::new));
								
								for(String s : sorted.keySet()) {
									
									leaderboardCount ++;
									
									
									
									if(leaderboardCount == 3) {
										for(String people : RANKS.keySet()) {
											RANKS.remove("people");
										}
										
										
										System.out.println("[RankTimer + LastModeration] Count is 3. I cleared all table.");
										
										
										break;
									}
									
									FIRST_THREE.put("activePerson", s);
									System.out.println("[RankTimer + LastModeration] Added to FIRST_THREE table: " + leaderboardCount + "." + s + " w/" + RANKS.get(s));
									RANKS.remove(s);
									
									Main.jda.getGuildById("482693670144049153").addRoleToMember(Main.jda.getGuildById("482693670144049153").getMemberById(s), Main.jda.getGuildById("482693670144049153").getRolesByName("Haftanýn Aktifi", true).get(0));
									
									
									//db.commit();
									System.out.println("[RankTimer + LastModeration] Deleted from rank table "  + s);
								}
								// [SYSTEM]
							} else {
								System.out.println("[RankTimer] Day is 7 but minute is not equals to 01. Skipped last moderation!");
							}
							
							
							
							
							
							
							
							// DELETE
							//for(String s : RANKS.keySet()) {
							//	int clearedPaper = RANKS.get(s);
							//	
							//	System.out.println("[RankTimer] Cleared " + s + " and " + clearedPaper);
							//	
								//	RANKS.remove(s);
							//	
								// db.commit();
								//}
							// DELETE
								
								
						} else {
							
							System.out.println("[RankTimer] Day is not 7." + TIMER.get("day") + " " + now);
							
						}
						
					} else {
						TIMER.put("day", 0);
						
						// db.commit();
						
						System.out.println("[RankTimer] Day is null, i set day to 0. " + now + " " + TIMER.get("day"));
					}
				}
				
			}
			
		};
	    
	    rankTimer.scheduleAtFixedRate(rankTimerTask, 60000, 60000);
		
		VoiceChannelListener.paperTimer.scheduleAtFixedRate(VoiceChannelListener.paperTimerTask, 65000, 65000);
		
	}
	
	public void onDisable() {
		db.close();
	}
	

}
