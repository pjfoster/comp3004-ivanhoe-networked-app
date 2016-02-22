package comp3004.ivanhoe.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import comp3004.ivanhoe.model.Card;
import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;

public class ServerResponseBuilder {

	public JSONObject buildConnectionAccepted() {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "connection_accepted");
		return new JSONObject(responseMap);
	}
	
	public JSONObject buildConnectionRejected() {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "connection_rejected");
		return new JSONObject(responseMap);
	}
	
	public JSONObject buildStartGame() {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "start_game");
		return new JSONObject(responseMap);
	}
	
	public JSONObject buildChooseColor() {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "choose_color");
		return new JSONObject(responseMap);
	}
	
	/**
	 * 
	 * @param tournament
	 * @param playerTurn
	 * @return
	 */
	public JSONObject buildStartTournament(Tournament tournament, int playerTurn) {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "start_tournament");
		responseMap.put("turn", playerTurn + "");
		return createGameSnapshot(responseMap, tournament);
	}
	
	public JSONObject buildStartPlayerTurn() {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "start_player_turn");
		return new JSONObject(responseMap);
	}
	
	public JSONObject buildUpdateView(Player player, Tournament tournament) {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "update_view");
		return new JSONObject(responseMap);
	}
	
	public JSONObject buildTournamentOverWin() {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "tournament_over_win");
		return new JSONObject(responseMap);
	}
	
	public JSONObject buildTournamentOverLoss() {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "tournament_over_loss");
		return new JSONObject(responseMap);
	}
	
	public JSONObject buildQuit() {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("response_type", "quit");
		return new JSONObject(responseMap);
	}
	
	/**
	 * Creates a snapshot of a tournament and adds it to the provided response map
	 * @param responseMap
	 * @param tournament
	 */
	public JSONObject createGameSnapshot(HashMap<String, String> responseMap, Tournament tournament) {
		
		JSONObject snapshot = new JSONObject(responseMap);
		snapshot.put("tournament_color", tournament.getToken().toString());

		// Create a list of players
		JSONArray players = new JSONArray();
		for (Integer key : tournament.getPlayers().keySet()) {
			Player p = tournament.getPlayers().get(key);
			JSONObject player = new JSONObject();
			
			player.put("username", p.getName());
			player.put("id", key);
			
			// create hand
			JSONArray hand = new JSONArray();
			for (Card c : p.getHand()) {
				hand.add(c.toString());
			}
			player.put("hand", hand);
			
			// create display
			JSONArray display = new JSONArray();
			for (Card c : p.getDisplay()) {
				display.add(c.toString());
			}
			player.put("display", display);
			
			// create tokens
			JSONArray tokens = new JSONArray();
			for (Token t: p.getTokens()) {
				tokens.add(t.toString());
			}
			player.put("tokens", tokens);
			
			players.add(player);
		}
		snapshot.put("players", players);
		
		// create a deck
		JSONArray deck = new JSONArray();
		for (Card c : tournament.getDeck()) {
			deck.add(c.toString());
		}
		snapshot.put("deck", deck);
		
		return snapshot;
		
	}
	
}