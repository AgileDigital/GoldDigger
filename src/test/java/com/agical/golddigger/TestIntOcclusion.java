package com.agical.golddigger;

import static com.agical.golddigger.model.fieldcreator.StringFieldCreator.DELIMITER;
import static com.agical.golddigger.model.fieldcreator.StringFieldCreator.LINE_OF_SIGHT;
import static com.agical.golddigger.model.fieldcreator.StringFieldCreator.NO_OF_SIDES;
import static com.agical.golddigger.model.fieldcreator.StringFieldCreator.SEPERATOR;
import static com.agical.golddigger.model.fieldcreator.StringFieldCreator.TILES;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.MalformedURLException;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.agical.golddigger.model.Digger;
import com.agical.golddigger.model.Diggers;
import com.agical.golddigger.model.GoldField;
import com.agical.golddigger.model.Position;
import com.agical.golddigger.model.fieldcreator.FieldCreator;
import com.agical.golddigger.model.fieldcreator.StringFieldCreator;
import com.agical.golddigger.server.GolddiggerServer;

import com.agical.jambda.Functions.Fn0;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;

public class TestIntOcclusion {

	private GolddiggerServer server;
    private WebConversation wc;
    
    private FieldCreator fieldCreator_2;
	private GoldField square_2;
	
    private static String map_2 = "wwwwwwww\n" +
    							  "w......w\n" +
    							  "w....w.w\n" +
    							  "w......w\n" +
    							  "w.w....w\n" +
    							  "w......w\n" +
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
				return new StringFieldCreator(createSetting(2,4) + map_2);
			}
    	};
        Diggers diggers = new Diggers(ff);
        Digger digger = diggers.createDigger("Diggers name", "secretname");
        diggers.newGame(digger);
        server = new GolddiggerServer();
        server.start(diggers, "target/calls.log");
        wc = new WebConversation();
    }
	
    @After
    public void stopServer() throws Exception {
        server.stop();
    }
    
    // Test square occlusion coming from the north with a line of sight length 2
    // Start position (5, 2)
 	@Test
     public void squareNorthOcclusion_2() throws Exception {
 		 WebResponse response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/south");
 		 response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/south");
 		 response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/south");
 		 response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/south");
 		 response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/east");
 		 
 		 
 		 response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/view");
 		 assertEquals("w???.\nw.w..\nw....\n?www?\n-----\n", response.getText());
     }
 	
 	// Test square occlusion coming from the north-east with a line of sight length 2
 	// Start position (5, 1)
 	@Test
 	public void squareNorthEastOcclusion_2() throws Exception {
 		WebResponse response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/south");
 		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/south");
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/south");
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/south");
		
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/view");
		assertEquals("-?.??\n-w.w?\n-w...\n-www?\n-----\n", response.getText());
 	}
 	
 	// Test square occlusion coming from the east with a line of sight length 2
 	// Start position (4, 1)
 	@Test
 	public void squareEastOcclusion_2() throws Exception {
 		WebResponse response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/south");
 		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/south");
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/south");
		
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/view");
		assertEquals("-?...\n-w..?\n-w.w?\n-w..?\n-?www\n", response.getText());
 	}
 	
 	// Test square occlusion coming from the south-east with a line of sight length 2
 	// Start position (1, 4)
 	@Test
 	public void squareSouthEastOcclusion_2() throws Exception {
 		WebResponse response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/east");
 		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/east");
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/east");
		
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/view");
		assertEquals("-----\n?www?\n.....\n...w?\n...??\n", response.getText());
 	}
	
 	// Test square occlusion coming from the south with a line of sight length 2
 	// Start position (1, 5)
 	@Test
 	public void squareSouthOcclusion_2() throws Exception {
 		WebResponse response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/east");
 		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/east");
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/east");
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/east");
		
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/view");
		assertEquals("-----\n?www?\n....w\n..w.w\n.???w\n", response.getText());
 	}
 	
	// Test square occlusion coming from the south-west with a line of sight length 2
 	// Start position (1, 6)
	@Test
	public void squareSouthWestOcclusion_2() throws Exception {
		WebResponse response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/east");
 		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/east");
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/east");
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/east");
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/east");

		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/view");
		assertEquals("-----\n?www-\n...w-\n?w.w-\n??.?-\n", response.getText());
	}
	
	// Test square occlusion coming from the west with a line of sight length 2
	// Start position (2, 6)
	@Test
	public void squareWestOcclusion_2() throws Exception {
		WebResponse response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/east");
 		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/east");
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/east");
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/east");
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/east");
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/south");
		
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/view");
		assertEquals("www?-\n?..w-\n?w.w-\n?..w-\n...?-\n", response.getText());
	}
	
	// Test square occlusion coming from the north-west with a line of sight length 2
	// Start position (2, 6)
	@Test
	public void squareNorthWestOcclusion_2() throws Exception {
		WebResponse response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/east");
 		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/east");
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/east");
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/east");
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/east");
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/south");
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/south");
		
		response = wc.getResponse("http://localhost:8066/golddigger/digger/secretname/view");
		assertEquals("??.?-\n?w.w-\n...w-\n...w-\n...?-\n", response.getText());   
	}
}
