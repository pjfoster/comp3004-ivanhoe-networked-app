package comp3004.ivanhoe.view.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import comp3004.ivanhoe.util.ClientParser;


@SuppressWarnings("serial")
public class PlayerDisplay extends JPanel {

	private int userId;
	private String username; 
	private JLabel icon;
	private ArrayList<String> cards;
	private ArrayList<String> tokens;
	private String displayTotal;
	private JLabel displayLabel;
	
	public JPanel userComposite;
	public JPanel displayComposite;
	
	public JPanel tokensComposite;
	public JScrollPane cardsScrollPane;
	
	public PlayerDisplay(Object player, JLabel imageIcon) {
		this.setLayout(new FlowLayout());
		this.setOpaque(false);
		
		this.userId = ClientParser.getPlayerId(player).intValue();
		this.username = ClientParser.getPlayerName(player);
		this.icon = imageIcon;
		this.cards = ClientParser.getPlayerDisplay(player);
		this.tokens = ClientParser.getPlayerTokens(player);
		this.displayTotal = ClientParser.getPlayerDisplayTotal(player);
		
		this.userComposite = createUserComposite(this.username, imageIcon);
		this.displayComposite = createDisplayComposite(this.tokens, this.cards, 
												       ClientParser.getPlayerSpecial(player), this.displayTotal);
		
		this.add(userComposite);
		this.add(Box.createRigidArea(new Dimension(10,0)));
		this.add(displayComposite);
		
	}
	
	public int getUserId() { return userId; }
	public String getUserName() { return username; }
	public JLabel getIcon() { return icon; }
	
	/**
	 * Create the userComposite, which displays a player's username and an icon
	 * @param username
	 * @param imageIcon
	 * @return
	 */
	public JPanel createUserComposite(String username, JLabel imageIcon) {
		JPanel userComposite = new JPanel();
		userComposite.setOpaque(false);
		//userComposite.setBorder(BorderFactory.createLineBorder(Color.black));
		userComposite.setLayout(new BoxLayout(userComposite, BoxLayout.Y_AXIS));
		
		imageIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
		userComposite.add(imageIcon);
		userComposite.add(Box.createRigidArea(new Dimension(0,10)));
		
		JLabel usernameLabel = new JLabel(username.toUpperCase());
		usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		userComposite.add(usernameLabel);
		
		return userComposite;
	}
	
	/**
	 * Creat the displayComposite, which includes the user's display, displayTotal, and tokens
	 * @param tokens
	 * @param displayCards
	 * @param displayTotal
	 * @return
	 */
	public JPanel createDisplayComposite(ArrayList<String> tokens, ArrayList<String> displayCards, 
			                             ArrayList<String> special, String displayTotal) {
		JPanel displayComposite = new JPanel();
		displayComposite.setOpaque(false);
		displayComposite.setLayout(new BoxLayout(displayComposite, BoxLayout.Y_AXIS));
		
		displayLabel = new JLabel("DISPLAY TOTAL: " + displayTotal);
		displayLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		tokensComposite = makeTokensComposite(tokens);
		cardsScrollPane = makeCardsScrollPane(displayCards, special);
		
		displayComposite.add(displayLabel);
		displayComposite.add(tokensComposite);
		displayComposite.add(cardsScrollPane);
		
		return displayComposite;
	}
	
	private JPanel makeTokensComposite(ArrayList<String> tokens) {
		JPanel tokensComposite = new JPanel();
		tokensComposite.setOpaque(false);
		tokensComposite.setSize(350, 25);
		tokensComposite.setAlignmentX(Component.LEFT_ALIGNMENT);
		tokensComposite.setLayout(new FlowLayout(FlowLayout.LEFT));
		for (String t: tokens) {
			if (t.toLowerCase().trim().equals("undecided")) System.out.println("HOW?????");
			JLabel token = ImageHandler.loadToken(t);
			tokensComposite.add(token);
		}
		tokensComposite.add(Box.createHorizontalGlue());
		return tokensComposite;
	}
	
	private JScrollPane makeCardsScrollPane(ArrayList<String> displayCards, ArrayList<String> special) {
		JPanel cardsComposite = new JPanel();
		cardsComposite.setOpaque(false);
		cardsComposite.setLayout(new FlowLayout());
		 
		// add special cards - SHIELD OR STUNNED
		for (String c: special) {
			JLabel card = ImageHandler.loadCard(c);
			card.setBorder(BorderFactory.createLineBorder(Color.RED));
			cardsComposite.add(card);
		}
		
		// add regular display cards
		for (String c: displayCards) {
			JLabel card = ImageHandler.loadCard(c);
			cardsComposite.add(card);
		}
		JScrollPane cardsScrollPane = new JScrollPane(cardsComposite, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
										              JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		cardsScrollPane.setBorder(null);
		cardsScrollPane.setOpaque(false);
		cardsScrollPane.getViewport().setOpaque(false);
		cardsScrollPane.setPreferredSize(new Dimension(350, 115));
		cardsScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		return cardsScrollPane;
	}
	
	public void updateDisplay(Object player) {
		// update display total
		String newTotal = ClientParser.getPlayerDisplayTotal(player);
		displayLabel.setText("DISPLAY TOTAL: " + newTotal);
		
		displayComposite.remove(tokensComposite);
		displayComposite.remove(cardsScrollPane);
		
		// update tokens
		tokensComposite = makeTokensComposite(ClientParser.getPlayerTokens(player));
		
		// update display
		//System.out.println(ClientParser.getPlayerName(player) + " new display " + ClientParser.getPlayerDisplay(player));
		cardsScrollPane = makeCardsScrollPane(ClientParser.getPlayerDisplay(player),
											  ClientParser.getPlayerSpecial(player));
		
		displayComposite.add(tokensComposite);
		displayComposite.add(cardsScrollPane);
	}
	
	public void withdraw() {
		displayLabel.setText("DISPLAY TOTAL: ---");
		displayComposite.remove(cardsScrollPane);
		JLabel withdrawLabel = new JLabel(username + " has withdrawn from the tournament");
		displayComposite.add(withdrawLabel);
	}
	
}
