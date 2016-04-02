package comp3004.ivanhoe.view.gui;

import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.json.simple.JSONObject;

import comp3004.ivanhoe.client.AppClient;
import comp3004.ivanhoe.controller.MockController;
import comp3004.ivanhoe.model.ColourCard;
import comp3004.ivanhoe.model.Player;
import comp3004.ivanhoe.model.Token;
import comp3004.ivanhoe.model.Tournament;
import comp3004.ivanhoe.server.MockServer;
import comp3004.ivanhoe.util.ClientRequestBuilder;
import comp3004.ivanhoe.util.ServerResponseBuilder;
import comp3004.ivanhoe.view.MockViewFactory;

public class TestView {

	public static void main(String[] args) {
		//AppClient client = new AppClient()
		GUIView view;
		AppClient client = new AppClient(new MockViewFactory(), "1.2.3.4", 10001);
		view = new GUIView(client, new ClientRequestBuilder());

		/*ArrayList<String> cards = new ArrayList<String>();
			cards.add("s3");
			cards.add("r5");
			cards.add("y2");
			cards.add("p7");
			cards.add("m6");

			ArrayList<String> tokens = new ArrayList<String>();
			tokens.add("purple");
			tokens.add("blue");

			PlayerDisplay p1 = new PlayerDisplay(ImageHandler.loadImage("blackknight"), 60001,
					                             "Alexei", tokens, cards, 8);

			view.add(p1);*/
		//view.launch();
		
		Player alexei = new Player("Alexei");
		Player luke = new Player("Luke");
		Player jayson = new Player("Jayson");
		HashMap<Integer, Player> players = new HashMap<Integer, Player>();
		
		alexei.addHandCard(new ColourCard("red", 3));
		alexei.addHandCard(new ColourCard("purple", 7));
		alexei.addHandCard(new ColourCard("yellow", 2));
		alexei.addHandCard(new ColourCard("green", 1));
		
		luke.addToken(Token.BLUE);
		luke.addDisplayCard(new ColourCard("red", 3));
		luke.addDisplayCard(new ColourCard("purple", 7));
		luke.addDisplayCard(new ColourCard("yellow", 2));
		luke.addDisplayCard(new ColourCard("green", 1));
		
		jayson.addToken(Token.GREEN);
		
		players.put(60001, alexei);
		players.put(60002, luke);
		players.put(60003, jayson);
		
		Tournament tournament = new Tournament();
		tournament.setToken(Token.YELLOW);
		tournament.setPlayers(players);
		
		JFrame testFrame = new JFrame();
		testFrame.setSize(800, 600);
		testFrame.setLayout(new FlowLayout());
		
		testFrame.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
		        System.exit(0);
	         }        
	     });  
		
		ServerResponseBuilder responseBuilder = new ServerResponseBuilder();
		JSONObject snapshot = responseBuilder.buildUpdateView(tournament);
		
		JPanel testPanel = new TournamentView(view, snapshot);
		testFrame.add(testPanel);
		testFrame.setVisible(true);
		
	}

}