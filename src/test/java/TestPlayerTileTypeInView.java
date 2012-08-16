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

public class TestPlayerTileTypeInView {

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
	 * Basically have it so that diggers are far apart such that they do not
	 * appear in each other's view but then move them closer to each other and
	 * check that they appear in the views correctly as 'o's or however they are
	 * being represented.
	 */
	@Test
	public void viewCalling() throws Exception {
		// diggerA.setPosition(new Position(lat1, long1));
		// diggerB.setPosition(new Position(lat2, long2));
		WebResponse response = wc.getResponse("http://localhost:8066/golddigger/digger/secretnameA/view");
		// assertEquals("Digger A's view not correct initially", expected,
		// response.getText());

		/* Move digger B into digger A's viewing range */
		// diggerB.setPosition(new Position(lat3, long3));
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretnameA/view");
		// assertEquals("Digger A's view does not correctly represent another digger in its view", expected, response.getText());
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretnameB/view");
		// assertEquals("Digger B's view does not correctly represent another digger in its view", expected, response.getText());

	}

}
