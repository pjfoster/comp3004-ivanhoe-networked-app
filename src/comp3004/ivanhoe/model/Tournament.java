package comp3004.ivanhoe.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Tournament {

	private final int STARTING_HAND_SIZE = 8;
	
	private Token token;
	private HashMap<Integer, Player> currentPlayers;
	private HashMap<String, ArrayList<Card>> cardLookup;
	
	private List<Card> deck;
	private List<Card> discardPile;
	
	/**
	 * Blank constructor for testing
	 */
	public Tournament() {
		currentPlayers = new HashMap<Integer, Player>();
		token = Token.UNDECIDED;
		
		cardLookup = new HashMap<String, ArrayList<Card>>();
		discardPile = new ArrayList<Card>();
		deck = new ArrayList<Card>();
		discardPile = new ArrayList<Card>();
		buildDeck();
	}
	
	public Tournament(HashMap<Integer, Player> players, Token token){
		currentPlayers = new HashMap<Integer, Player>(players);
		cardLookup = new HashMap<String, ArrayList<Card>>();
		this.token = token;
		discardPile = new ArrayList<Card>();
		buildDeck();
		dealStartingHands();
	}
	
	public void reset(HashMap<Integer, Player> players) {
		
		token = Token.UNDECIDED;
		
		// reset players
		currentPlayers = new HashMap<Integer, Player>(players);
		
		// take all players displays and put them in the discard pile
		for (Player p: currentPlayers.values()) {
			discardPile.addAll(p.getDisplay());
			p.resetRound();
		}
		
		for (Player p: currentPlayers.values()) {
			discardPile.addAll(p.getSpecial());
			p.resetRound();
		}
		
		// Add discard pile to deck and shuffle
		resetDiscardToDeck();
		
	}
	
	/**
	 * This function adds a player to the tournament
	 * @param player
	 */
	public void addPlayer(int id, Player player){
		currentPlayers.put(id, player);
	}
	
	/**
	 * This function removes a player from the tournament
	 * @param player
	 * @return
	 */
	public boolean removePlayer(int id){
		if (currentPlayers.containsKey(id)) {
			currentPlayers.remove(id);
			return true;
		}
		return false;
	}

	/**
	 * This function builds the deck from the Card subclasses
	 */
	private void buildDeck(){
		deck = new ArrayList<Card>();
		deck.addAll(ActionCard.getActionDeck());
		deck.addAll(ColourCard.getColourDeck());
		deck.addAll(SupporterCard.getSupporterDeck());
		shuffle();
		
		for (Card c: deck) {
			if (cardLookup.containsKey(c.toString())) {
				cardLookup.get(c.toString()).add(c);
			}
			else {
				ArrayList<Card> newArr = new ArrayList<Card>();
				newArr.add(c);
				cardLookup.put(c.toString(), newArr);
			}
		}
	}
	
	/**
	 * This function sets the hands of all players and removes those cards from the deck
	 **/
	private void dealStartingHands(){
		for (int i = 0; i < STARTING_HAND_SIZE; i++){
			for (Player p: currentPlayers.values()) {
				p.addHandCard(drawCard());
			}
		}
	}
	
	/**
	 * this function returns a card at index 0 from the deck and removes it from deck
	 * @return
	 */
	public Card drawCard(){
		if (deck.size() == 0)
			resetDiscardToDeck();
		return deck.remove(0);
	}
	
	/**
	 * This function adds a card to the discard pile
	 * @param card
	 */
	public void addToDiscard(Card card){
		discardPile.add(card);
	}
	
	/**
	 * This function is called by drawCard() when the deck has no cards and it takes the discard deck shuffles and adds it to the empty deck
	 */
	private void resetDiscardToDeck(){
		deck.addAll(discardPile);
		shuffle();
		discardPile = new ArrayList<Card>();
	}

	/**
	 * This function uses Collections.shuffle to shuffle the deck
	 */
	private void shuffle(){
		Collections.shuffle(deck);
	}
	
	/**
	 * This function returns the player with the highest display
	 * If 2 players have equal displays, the function returns null
	 * @return
	 */
	public Player getPlayerWithHighestDisplay(){
		Player chosenPlayer = null;
		int highestTotal = 0;
		
		for (Player p: currentPlayers.values()){
			if (p.getDisplayTotal(token) > highestTotal) {
				chosenPlayer = p;
				highestTotal = p.getDisplayTotal(token);
			}
			else if (p.getDisplayTotal(token) == highestTotal) {
				chosenPlayer = null;
			}
		}
		return chosenPlayer;
	}
	
	public int getHighestDisplayTotal() {
		int highestTotal = 0;
		for (Player p: currentPlayers.values()){
			if (p.getDisplayTotal(token) > highestTotal)
				highestTotal = p.getDisplayTotal(token);
		}
		return highestTotal;
	}
	
	public HashMap<Integer, Player> getPlayers() {
		return currentPlayers;
	}
	
	public List<Card> getDeck() {
		return deck;
	}
	
	public List<Card> getDiscardPile() {
		return discardPile;
	}
	
	/**
	 * This function returns the current token
	 * @return
	 */
	public Token getToken(){
		return token;
	}
	
	/**
	 * This function allows the user to set the current token
	 * @param tkn
	 */
	public void setToken(Token tkn){
		token  = tkn;
	}
	
	public void setPlayers(HashMap<Integer, Player> players) {
		currentPlayers = new HashMap<Integer, Player>(players);
	}
	
	public ArrayList<Card> getCard(String code) {
		return cardLookup.get(code);
	}

}
