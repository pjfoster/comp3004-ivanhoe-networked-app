package comp3004.ivanhoe.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Squire 2 - 8
 * Squire 3 - 8
 * 
 * Maiden 6- 4
 * @author David Farrar
 *
 */
public class SupporterCard extends Card {
	
	public SupporterCard(){
		name = "";
		value = 0;
	}
	
	public SupporterCard(String nm, int val){
		name = nm;
		value = val;
	}
	
	public String toString() {
		if (name.equals("squire")) {
			return "s" + value;
		}
		else {
			return "m" + value;
		}
	}
	
	public static List<SupporterCard> getSupporterDeck(){
		List<SupporterCard> output = new ArrayList<SupporterCard>();
		for(int i = 0; i < 8; i++){
			output.add(new SupporterCard("squire",2));
			output.add(new SupporterCard("squire",3));
		}
		for(int i = 0; i < 4; i++){
			output.add(new SupporterCard("maiden",6));
		}
		return output;
	}
}
