import static com.agical.golddigger.model.fieldcreator.StringFieldCreator.DELIMITER;
import static com.agical.golddigger.model.fieldcreator.StringFieldCreator.LINE_OF_SIGHT;
import static com.agical.golddigger.model.fieldcreator.StringFieldCreator.NO_OF_SIDES;
import static com.agical.golddigger.model.fieldcreator.StringFieldCreator.SEPERATOR;
import static com.agical.golddigger.model.fieldcreator.StringFieldCreator.TILES;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.agical.golddigger.emptyfield.EmptyField;
import com.agical.golddigger.model.Digger;
import com.agical.golddigger.model.Diggers;
import com.agical.golddigger.model.GoldField;
import com.agical.golddigger.model.Position;
import com.agical.golddigger.model.fieldcreator.FieldCreator;
import com.agical.golddigger.model.fieldcreator.ResourceFieldCreator;
import com.agical.golddigger.model.fieldcreator.StringFieldCreator;
import com.agical.golddigger.server.GolddiggerServer;
import com.agical.jambda.Functions.Fn0;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;

public class TestMultiplayer {

	/*
	 * Mimicking TestWebInterface since we are testing the player's requests to
	 * the server
	 */
	
	int lat1 = 1;
	int long1 = 1;
	private GolddiggerServer server;
	private WebConversation wc;
	private Diggers diggers;
	private Digger diggerA;
	private Digger diggerB;
	
	private String diggerName1 = "digger1";
	private String diggerName2 = "digger2";
	
	private String secret1 = "secret1";
	private String secret2 = "secret2";
	
	private FieldCreator fieldCreator_2;
	private GoldField square_2;
	
    private static String map = "wwwwwwww\n" +
    							  "wb....bw\n" +
    							  "wwwwwwww\n";
	
	private String createSetting(int los_length, int no_of_sides) {
    	return DELIMITER+LINE_OF_SIGHT+" "+SEPERATOR +" "+ los_length + "\n"+
    			DELIMITER+NO_OF_SIDES+" "+SEPERATOR+" "+ no_of_sides + "\n"+
    			DELIMITER+TILES+"\n";
    }
	@Before
	public void startServer() throws Exception {
		Fn0<FieldCreator> ff = new Fn0<FieldCreator>(){
    		@Override
			public FieldCreator apply() {
				return new StringFieldCreator(createSetting(6,4) + map);
			}
    	};
		/* Should have a multiplayer map in the test maps folder */
		diggers = new Diggers(ff);
		diggerA = diggers.createDigger(diggerName1, "secretnameA");
		diggerB = diggers.createDigger(diggerName2, "secretnameB");
		diggers.newGame(diggerA);
		diggers.newGame(diggerB);
		server = new GolddiggerServer();
		server.startForMultiplayer(diggers, "target/calls.log");
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
	public void testDiggerCollision() throws Exception {
		diggerA.setPosition(new Position(1, 1));
		diggerB.setPosition(new Position(1, 6));
		
		WebResponse response = wc.getResponse("http://localhost:8066/golddigger/digger/secretnameB/move/east");
		
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretnameB/move/west");
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretnameB/move/west");
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretnameB/move/west");
		System.out.println(response.getText() + "," + diggerB.getPosition().toString());
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretnameB/move/west");
		System.out.println(diggerA.getView()); //Checks where it thinks the other digger is which should be
											  //at 1,2
		
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretnameB/move/west");
		assertEquals("FAILED\n",response.getText()); //Digger B fails to move onto digger A
		
		
		System.out.println(response.getText() + "," + diggerB.getPosition().toString());
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretnameA/move/east");
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretnameA/view");
		System.out.println(response.getText()+ "," + diggerA.getPosition().toString());
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretnameA/move/west");
		System.out.println(response.getText() + "," + diggerA.getPosition().toString());
		assertEquals("FAILED\n",response.getText());
		System.out.println(response.getText());

	}

}
