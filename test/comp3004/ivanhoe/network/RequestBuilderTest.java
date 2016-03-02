package comp3004.ivanhoe.network;

import static org.junit.Assert.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comp3004.ivanhoe.util.ClientRequestBuilder;

public class RequestBuilderTest {

	private ClientRequestBuilder requestBuilder = new ClientRequestBuilder();
	private JSONParser parser = new JSONParser();
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBuildRegisterPlayer() throws ParseException {
		
		String testMoveString = requestBuilder.buildRegisterPlayer("Alexei").toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 2);
		assertEquals(testMove.get("request_type"), "register_player");
		assertEquals(testMove.get("username"), "Alexei");
		
		// TODO: test weird cases
	}
	
	@Test
	public void testBuildChooseToken() throws ParseException {
		
		String testMoveString = requestBuilder.buildChooseToken("red").toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 2);
		assertEquals(testMove.get("request_type"), "choose_token");
		assertEquals(testMove.get("token_color"), "red");
		
	}
	
	@Test
	public void testBuildColorCardMove() throws ParseException {
		
		String testMoveString = requestBuilder.buildColorCardMove("r6").toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 3);
		assertEquals(testMove.get("request_type"), "turn_move");
		assertEquals(testMove.get("move_type"), "color_card");
		assertEquals(testMove.get("card_code"), "r6");
		
	}
	
	@Test
	public void testBuildSupporterCardMove() throws ParseException {
		
		String testMoveString = requestBuilder.buildSupporterCardMove("s3").toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 3);
		assertEquals(testMove.get("request_type"), "turn_move");
		assertEquals(testMove.get("move_type"), "supporter_card");
		assertEquals(testMove.get("card_code"), "s3");
		
		testMoveString = requestBuilder.buildSupporterCardMove("m6").toJSONString();
		testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 3);
		assertEquals(testMove.get("request_type"), "turn_move");
		assertEquals(testMove.get("move_type"), "supporter_card");
		assertEquals(testMove.get("card_code"), "m6");
		
	}
	
	@Test
	public void testBuildActionCardMove() throws ParseException {
		
		String testMoveString = requestBuilder.buildActionCardMove("dg").toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 3);
		assertEquals(testMove.get("request_type"), "turn_move");
		assertEquals(testMove.get("move_type"), "action_card");
		assertEquals(testMove.get("card_code"), "dg");
		
	}
	
	@Test
	public void testBuildWithdrawMove() throws ParseException {
		
		String testMoveString = requestBuilder.buildWithdrawMove().toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 2);
		assertEquals(testMove.get("request_type"), "turn_move");
		assertEquals(testMove.get("move_type"), "withdraw");
		
	}
	
	@Test
	public void testBuildSelectOpponent() throws ParseException {
		
		String testMoveString = requestBuilder.buildSelectOpponent("Alexei").toJSONString();
		JSONObject testMove = (JSONObject)parser.parse(testMoveString);
		
		assertEquals(testMove.size(), 2);
		assertEquals(testMove.get("request_type"), "select_opponent");
		assertEquals(testMove.get("opponent_username"), "Alexei");
		
	}

}
