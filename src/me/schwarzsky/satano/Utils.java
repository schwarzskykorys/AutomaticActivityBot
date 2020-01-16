package me.schwarzsky.satano;

public class Utils {
	public static int getInteger(int max) {
		
		int randomInteger = (int) (max * Math.random());
		
		return randomInteger;
	}

}
