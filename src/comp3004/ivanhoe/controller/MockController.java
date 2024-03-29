package comp3004.ivanhoe.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;

import comp3004.ivanhoe.model.Card;
import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;
import comp3004.ivanhoe.server.AppServer;

/**
 * Extension of controller with testing capabilities. Mostly contains getters and setters
 * to data members you typically wouldn't want accessed, but that are necessary for setting
 * up test cases
 * @author PJF
 *
 */
public class MockController extends IvanhoeController {

	public MockController(AppServer server, int maxPlayers) {
		super(server, maxPlayers);
		timeDelayMillis = 0;
	}
	
	public Tournament getTournament() { return tournament; }
	public int getCurrentTurn() { return currentTurn; }
	
	public void setPlayers(HashMap<Integer, Player> players) {
		this.players = players;
	}
	
	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}
	
	public void setPreviousTournament(Token color) {
		this.previousTournament = color;
	}
	
	public Token getPreviousTournament() {
		return previousTournament;
	}
	
	public void givePlayerToken(int playerId, Token token) {
		players.get(playerId).addToken(token);
	}
	
	public void setTurn(int playerId) {
		
		if (playerTurns == null) {
			playerTurns = new ArrayList<Integer>(players.keySet());
		}
		
		for (int i = 0; i < playerTurns.size(); ++i) {
			if (playerTurns.get(i) == playerId) {
				currentTurn = i;
			}
		}
		
	}
	
	public void setState(int state) {
		this.state = state;
	}
	
	public int getState() {
		return state;
	}
	
	public Card getCardFromDeck(String cardCode) {
		for (Card c: tournament.getDeck()) {
			if (c.toString().equals(cardCode)) {
				return c;
			}
		}
		return null;
	}
	
	public ArrayList<Integer> getPlayerTurns() {
		return playerTurns;
	}
	
	public void setPlayerTurns(ArrayList<Integer> turns) {
		this.playerTurns = turns;
	}
	
	public HashMap<Integer, Player> getPlayers() { 
		return players;
	}
	
	public void swapHand(int playerId, ArrayList<Card> cards)  {
		players.get(playerId).setStartingHand(cards);
	}
	
	/*public Card getCardFromString(String cardCode) {
		ArrayList<Card> temp = currentTournament.getCard(cardCode);
		if (temp != null && !temp.isEmpty()) {
			return temp.get(0);
		}
		return null;
	}*/
	
	public ArrayList<Card> getCardsFromStrings(ArrayList<String> cardCodes) {
		ArrayList<Card> cards = new ArrayList<Card>();
		
		for (String cardCode: cardCodes) {
			ArrayList<Card> temp = tournament.getCard(cardCode);
			if (temp != null && !temp.isEmpty()) {
				cards.add(temp.get(0));
			}
		}
		
		return cards;
	}
	
	public ArrayList<Card> getCardsFromStrings(String[] cardCodes) {
		ArrayList<Card> cards = new ArrayList<Card>();
		
		for (String cardCode: cardCodes) {
			ArrayList<Card> temp = tournament.getCard(cardCode);
			if (temp != null && !temp.isEmpty()) {
				Card c = temp.get(0);
				tournament.getDeck().remove(c);
				cards.add(c);
			}
		}
		
		return cards;
	}
	
	public void setPlayerTurns(Integer[] turns) {
		playerTurns = new ArrayList<Integer>();
		for (Integer i: turns) {
			playerTurns.add(i);
		}
	}
	
	public boolean testChangeTournamentColor(JSONObject request) {
		return actionCardChangeTournamentColor(request);
	}
	
}
