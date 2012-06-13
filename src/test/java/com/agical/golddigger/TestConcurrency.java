package com.agical.golddigger;

import static com.agical.golddigger.model.fieldcreator.StringFieldCreator.COSTS;
import static com.agical.golddigger.model.fieldcreator.StringFieldCreator.DELIMITER;
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
import com.agical.golddigger.model.fieldcreator.FieldCreator;
import com.agical.golddigger.model.fieldcreator.StringFieldCreator;
import com.agical.golddigger.server.GolddiggerServer;
import com.agical.jambda.Functions.Fn0;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;


public class TestConcurrency {

    private GolddiggerServer server;
    private WebConversation wc;

	private static String STRING_FIELD = 
			DELIMITER+TILES+ "\n" +
			"wwww\n" +
			"wb.w\n" +
			"w..w\n" +
			"wwww\n" +
			DELIMITER+COSTS+ "\n" +
			".=1000\n";
	
    @Before
    public void startServer() throws Exception {
    	Fn0<FieldCreator> ff = new Fn0<FieldCreator>(){
    		@Override
			public FieldCreator apply() {
				return new StringFieldCreator(STRING_FIELD);
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
    @Test
    public void testMove() throws Exception {        
        Worker worker1 = new Worker("http://localhost:8066/golddigger/digger/secretname/move/east");
        Worker worker2 = new Worker("http://localhost:8066/golddigger/digger/secretname/move/west");
        Thread t1 = new Thread(worker1);
        Thread t2 = new Thread(worker2);

        t1.start();
		Thread.sleep(200);
        t2.start();
        
        while (t1.isAlive() || t2.isAlive()) Thread.sleep(100);
        assertEquals("Sanity Check Failed",200, worker1.response.getResponseCode());
        assertEquals("Did not error",503, worker2.response.getResponseCode());
        
    }
    
    private static class Worker implements Runnable{
    	String URL;
    	public WebResponse response;
    	public Worker(String URL){
    		this.URL = URL;
    	}
		@Override
		public void run() {
			long start = System.currentTimeMillis();
	        WebConversation wc = new WebConversation();
	        wc.setExceptionsThrownOnErrorStatus(false);
	        try {
				response = wc.getResponse(URL);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			}
		}
	}
}