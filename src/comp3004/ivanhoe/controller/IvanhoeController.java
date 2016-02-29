package comp3004.ivanhoe.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.json.simple.JSONObject;

import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;
import comp3004.ivanhoe.server.AppServer;
import comp3004.ivanhoe.util.ServerResponseBuilder;

public class IvanhoeController {
	
	protected final int WAITING_FOR_MORE_PLAYERS = 	1;
	protected final int WAITING_FOR_COLOR = 			2;
	protected final int WAITING_FOR_PLAYER_MOVE = 	3;
	
	protected int maxPlayers;
	protected HashMap<Integer, Player> players;
	protected ArrayList<Integer> playerTurns;
	protected ServerResponseBuilder responseBuilder;
	protected AppServer server;
	protected Random rnd = new Random();
	
	protected Tournament currentTournament;
	protected Token previousTournament;
	protected int currentTurn;
	protected boolean gameWon;
	protected int state;
	
	public IvanhoeController(AppServer server, ServerResponseBuilder responseBuilder, int maxPlayers) {
		this.server = server;
		this.responseBuilder = responseBuilder;
		this.maxPlayers = maxPlayers;
		players = new HashMap<Integer, Player>();
		
		gameWon = false;
		state = WAITING_FOR_MORE_PLAYERS;
		currentTournament = null;
		previousTournament = null;
		currentTurn = -1;
	}
	
	public HashMap<Integer, Player> getPlayers() { return players; }
	
	/**
	 * Registers user as a player
	 * @param playerId: id of player thread, used to identify them
	 * @param playerName: player's username
	 */
	public void addPlayer(int playerId, String playerName) {
		
		if (players.size() >= maxPlayers) {
			// Too many players; can't add any more
			return;
		}
		
		if (!players.containsKey(playerId)) {
			Player newPlayer = new Player(playerName);
			players.put(playerId, newPlayer);
		}
		
		if (players.size() == maxPlayers) {
			startGame();
		}
	
	}
	
	public void startGame() {
		
		state = WAITING_FOR_COLOR;
		
		JSONObject startGameMessage = responseBuilder.buildStartGame();
		server.broadcast(startGameMessage);	
		
		// since there is no dealer, pick a random person to choose the first color
		playerTurns = new ArrayList<Integer>(players.keySet());
		int r = rnd.nextInt(players.size());
		currentTurn = r;
		
		JSONObject chooseColor = responseBuilder.buildChooseColor();
		server.sendToClient(playerTurns.get(r), chooseColor);
	}
	
	public void processPlayerMove(int id, JSONObject playerMove) {
		
		switch (state) {
		case WAITING_FOR_COLOR:
			if (playerTurns.get(currentTurn) == id) {
				System.out.println("The tournament shall be " + playerMove.get("token_color") + "!");
				
				state = WAITING_FOR_PLAYER_MOVE;
				currentTournament = new Tournament(players, (String)playerMove.get("token_color"));
				
				JSONObject response = responseBuilder.buildStartTournament(currentTournament, playerTurns.get(currentTurn));
				server.broadcast(response);
			}
			break;
		case WAITING_FOR_PLAYER_MOVE:
			break;
		default:
		}
		
	}
	
	/**
	 * Returns true if one of the players has won the game, false otherwise
	 * @return
	 */
	public boolean checkGameWon() {
		// conditions for 2-3 players
		if (players.size() < 4) {
			return false;
		}
		
		// conditions for 4-5 players
		else {
			return false;
		}
	}
	
	public boolean withdraw(int playerId) {
		return false;
	}
	
	public int nextPlayerTurn(int r) {
		currentTurn = (currentTurn + 1) % playerTurns.size();
		return currentTurn;
	}
	
}
