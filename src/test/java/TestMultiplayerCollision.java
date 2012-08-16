import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.agical.golddigger.emptyfield.EmptyField;
import com.agical.golddigger.model.Digger;
import com.agical.golddigger.model.Diggers;
import com.agical.golddigger.model.fieldcreator.ResourceFieldCreator;
import com.agical.golddigger.server.GolddiggerServer;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;

public class TestMultiplayerCollision {

	/*
	 * Mimicking TestWebInterface since we are testing the player's requests to
	 * the server
	 */
	private GolddiggerServer server;
	private WebConversation wc;
	private Diggers diggers;
	private Digger diggerA;
	private Digger diggerB;

	@Before
	public void startServer() throws Exception {

		/* Should have a multiplayer map in the test maps folder */
		diggers = new Diggers(ResourceFieldCreator.factory(EmptyField.class));
		diggerA = diggers.createDigger("Diggers name A", "secretnameA");
		diggerB = diggers.createDigger("Diggers name B", "secretnameB");
		diggers.newGame(diggerA);
		diggers.newGame(diggerB);
		server = new GolddiggerServer();
		server.start(diggers, "target/calls.log");
		wc = new WebConversation();
	}

	@After
	public void stopServer() throws Exception {
		server.stop();
	}

	/*
	 * Basically have a digger try to move onto a tile that is already occupied
	 * by a digger, it should return a FAILED message. Remember to test both 4
	 * and 6 sided maps. One method would be to exhaustively test all directions
	 * that a digger can move onto another tile.
	 */
	@Test
	public void testCollision() throws Exception {
		// diggerA.setPosition(new Position(latitude, longitude));
		// diggerB.setPosition(new Position(latitude, longitude));

		WebResponse response = wc.getResponse("http://localhost:8066/golddigger/digger/secretnameB/move/south");
		// assertEquals("A collision was not detected or the correct error message was not sent back", expected, response.getText());
	}

}
