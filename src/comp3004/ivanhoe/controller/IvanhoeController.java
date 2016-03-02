package comp3004.ivanhoe.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.json.simple.JSONObject;

import comp3004.ivanhoe.model.ActionCard;
import comp3004.ivanhoe.model.Card;
import comp3004.ivanhoe.model.ColourCard;
import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.SupporterCard;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;
import comp3004.ivanhoe.server.AppServer;
import comp3004.ivanhoe.util.ServerParser;
import comp3004.ivanhoe.util.ServerResponseBuilder;

public class IvanhoeController {
	
	protected final int WAITING_FOR_MORE_PLAYERS = 	1;
	protected final int WAITING_FOR_COLOR = 			2;
	protected final int WAITING_FOR_PLAYER_MOVE = 	3;
	
	protected int maxPlayers;
	protected HashMap<Integer, Player> players;
	protected ArrayList<Integer> playerTurns;
	protected ServerResponseBuilder responseBuilder;
	protected ServerParser parser;
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
		this.parser = new ServerParser();
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
		else {
			JSONObject waitingForPlayers = responseBuilder.buildWaiting();
			server.sendToClient(playerId, waitingForPlayers);
		}
	
	}
	
	public void startGame() {
		
		state = WAITING_FOR_COLOR;	
		
		// since there is no dealer, pick a random person to choose the first color
		playerTurns = new ArrayList<Integer>(players.keySet());
		int r = rnd.nextInt(players.size());
		currentTurn = r;
		
		currentTournament = new Tournament(players, Token.UNDECIDED);
		
		JSONObject startGameMessage = responseBuilder.buildStartGame(currentTournament, getCurrentTurnId());
		server.broadcast(startGameMessage);
	}
	
	public void processPlayerMove(int id, JSONObject playerMove) {
		
		switch (state) {
		case WAITING_FOR_COLOR:
			if (getCurrentTurnId() == id) {
				
				if (parser.getRequestType(playerMove).equals("turn_move")) {
					
					if (parser.getMoveType(playerMove).equals("withdraw")) {
						// TODO: handle this
					}
					else if (parser.getMoveType(playerMove).equals("play_card")) {
						ArrayList<Card> card = parser.getCard(playerMove, currentTournament);
						if (playCard(card)) {
							
							state = WAITING_FOR_PLAYER_MOVE;
							
							JSONObject newSnapshot = responseBuilder.buildUpdateView(currentTournament);
							server.broadcast(newSnapshot);
							
							nextPlayerTurn();
							JSONObject playerTurn = responseBuilder.buildStartPlayerTurn();
							server.sendToClient(getCurrentTurnId(), playerTurn);
						}
						else {
							JSONObject invalidResponse = responseBuilder.buildInvalidResponse();
							server.sendToClient(getCurrentTurnId(), invalidResponse);
						}
					}
		
				}
				else {
					JSONObject invalidResponse = responseBuilder.buildInvalidResponse();
					server.sendToClient(getCurrentTurnId(), invalidResponse);
				}
			}
			break;
		case WAITING_FOR_PLAYER_MOVE:
			if (getCurrentTurnId() == id) {
				
				System.out.println("MOVE: " + playerMove);
				
				if (((String)playerMove.get("request_type")).equals("turn_move")) {
					String moveType = (String)playerMove.get("move_type");
					
					if (moveType.equals("withdraw")) {
						System.out.println("WITHDRAW MOVE");
					}
					else if (moveType.equals("play_card")) {
						System.out.println("CARD MOVE");
						ArrayList<Card> card = parser.getCard(playerMove, currentTournament);
						System.out.println("Card: " + card);
						boolean success = playCard(card);
					}
					else if (moveType.equals("color_card")) {
						System.out.println("COLOR CARD MOVE");
						ArrayList<Card> card = parser.getCard(playerMove, currentTournament);
						System.out.println("Card: " + card);
					}
					else if (moveType.equals("supporter_card")) {
						System.out.println("SUPPORTER MOVE");
						ArrayList<Card> card = parser.getCard(playerMove, currentTournament);
						System.out.println("Card: " + card);
					}		
					else if (moveType.equals("action_card")) {
						System.out.println("ACTION MOVE");
						ArrayList<Card> card = parser.getCard(playerMove, currentTournament);
						System.out.println("Card: " + card);
					}
				}
			}
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
	
	public boolean playCard(ArrayList<Card> c)
	{
		
		if (c.get(0) instanceof ColourCard) {
			System.out.println("It's a color card!");
			
			// Check that the player has the card in their hand
			ColourCard card = null;
			for (Card colourCard: c) {
				if (getCurrentTurnPlayer().hasCardInHand(colourCard)) {
					card = (ColourCard)colourCard;
					break;
				}
			}
			if (card == null) { return false; }
			
			if (currentTournament.getToken().equals(Token.UNDECIDED)) {
				currentTournament.setToken(Token.fromString(card.getColour()));
			}
			else if (!card.getColour().toLowerCase().equals(currentTournament.getToken().toString().toLowerCase())) 
			{ 
				return false; 
			}
			
			int newDisplayTotal = getCurrentTurnPlayer().getDisplayTotal() + card.getValue();
			if (newDisplayTotal <= currentTournament.getHighestDisplayTotal()) { return false; }
			
			// Play the card
			getCurrentTurnPlayer().playCard(card);
			return true;
			
		}
		else if (c.get(0) instanceof SupporterCard) {
			System.out.println("It's a supporter card!");
		} 
		else if (c.get(0) instanceof ActionCard) {
			System.out.println("It's an action card!");
		}
		return false;
	}
	
	public boolean playActionCard(Card c) {
		return false;
	}
	
	public boolean withdraw(int playerId) {
		return false;
	}
	
	public int nextPlayerTurn() {
		currentTurn = (currentTurn + 1) % playerTurns.size();
		return currentTurn;
	}
	
	/**
	 * Returns ID of the player whose turn it is
	 * @return
	 */
	public int getCurrentTurnId() {
		return playerTurns.get(currentTurn);
	}
	
	/**
	 * Returns player whose turn it is
	 * @return
	 */
	public Player getCurrentTurnPlayer() { 
		return players.get(playerTurns.get(currentTurn));			
	}
	
}
