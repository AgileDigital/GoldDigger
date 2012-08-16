import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.agical.golddigger.emptyfield.EmptyField;
import com.agical.golddigger.model.Digger;
import com.agical.golddigger.model.Diggers;
import com.agical.golddigger.model.Position;
import com.agical.golddigger.model.fieldcreator.ResourceFieldCreator;
import com.agical.golddigger.server.GolddiggerServer;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;

public class TestInitialTimer {

	/*
	 * Mimicking TestWebInterface since we are testing the player's requests to
	 * the server
	 */
	private GolddiggerServer server;
	private WebConversation wc;
	private Diggers diggers;
	private Digger digger;
	private int timerDuration = 10 * 1000; // Get this value from the map

	@Before
	public void startServer() throws Exception {

		/* Should have a multiplayer map in the test maps folder */
		diggers = new Diggers(ResourceFieldCreator.factory(EmptyField.class));
		digger = diggers.createDigger("Diggers name", "secretname");
		diggers.newGame(digger);
		server = new GolddiggerServer();
		server.start(diggers, "target/calls.log");
		wc = new WebConversation();
	}

	@After
	public void stopServer() throws Exception {
		server.stop();
	}

	@Test
	public void viewBeforeGameStart() throws Exception {
		
		/*
		 * Check or set the initial timer value non zero so there is enough time
		 * to issue commands before the timer expiration (game start)
		 */
		WebResponse response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/view");
		assertEquals("The correct message (error) was not returned back to the digger that issued a command BEFORE game start", "desired error message", response.getText());

		/* Check that the view command didn't reveal tiles */
	}

	@Test
	public void moveBeforeGameStart() throws Exception {
		
		/*
		 * Check or set the initial timer value non zero so there is enough time
		 * to issue commands before the timer expiration (game start)
		 */
		Position startingPosition = digger.getPosition();
		WebResponse response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/south");
		assertEquals("The correct message (error) was not returned back to the digger that issued a command BEFORE game start", "desired error message", response.getText());
		assertEquals("The digger has moved even though the game has not started", startingPosition, digger.getPosition());
	}

	@Test
	public void MoveAfterGameStart() throws Exception {

		/*
		 * Wait for game to start and add five seconds because there is always
		 * some lag
		 */
		Thread.sleep((long) timerDuration + 5000L);
		WebResponse response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/south");
		assertEquals("The correct view was not returned to the digger that issued a command AFTER game start", "Finish me", response.getText());

		/* Finish me with correct expected */
	}

}