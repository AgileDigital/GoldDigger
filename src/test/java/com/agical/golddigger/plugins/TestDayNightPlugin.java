package com.agical.golddigger.plugins;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.agical.golddigger.model.Digger;
import com.agical.golddigger.model.Diggers;
import com.agical.golddigger.model.GoldField;
import com.agical.golddigger.model.fieldcreator.FieldCreator;
import com.agical.golddigger.model.fieldcreator.StringFieldCreator;
import com.agical.golddigger.plugins.api.GoldDiggerPlugin;
import com.agical.golddigger.server.GolddiggerServer;
import com.agical.jambda.Functions.Fn0;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;

import static com.agical.golddigger.model.fieldcreator.StringFieldCreator.*;
import static org.junit.Assert.*;


public class TestDayNightPlugin {
	private static final String FIELD = 
		section(TILES)+
			"wwwww\n"+
			"w.b.w\n"+
			"wwwww\n"+
		section(PLUGINS)+
			DayNightPlugin.NAME+"\n"+
		attribute(LINE_OF_SIGHT, 2)+
		attribute(DayNightPlugin.CYCLE_TIME, 4) +
		attribute(DayNightPlugin.LOS_SCALE, 50);
	
	@Test
	public void unitTest() {
		GoldField field = new GoldField(new StringFieldCreator(FIELD));
		
		DayNightPlugin plugin = null;
		Iterator<GoldDiggerPlugin> plugins = field.getPluginService().getPlugins();
		while(plugins.hasNext()){
			GoldDiggerPlugin next = plugins.next();
			if (next instanceof DayNightPlugin) plugin = (DayNightPlugin) next;
		}
		
		assertNotNull("DayNightPlugin was Not loaded", plugin);
		assertEquals(4, plugin.getCycleTime());
		assertEquals(0, plugin.getTurnCount());
		assertEquals(2, field.getLOS());
		assertTrue(plugin.isDay());
		plugin.update(field);
		plugin.update(field);
		plugin.update(field);
		plugin.update(field);
		assertEquals(4, plugin.getTurnCount());
		assertFalse(plugin.isDay());
		assertEquals(1, field.getLOS());

		plugin.update(field);
		plugin.update(field);
		plugin.update(field);
		plugin.update(field);
		assertEquals(8, plugin.getTurnCount());
		assertTrue(plugin.isDay());
		assertEquals(2, field.getLOS());
	}
	
	@Test
	public void integrationTest() throws Exception{
		Fn0<FieldCreator> ff = new Fn0<FieldCreator>(){
			@Override
			public FieldCreator apply() {
				return new StringFieldCreator(FIELD);
			}
		};
		
		Diggers diggers = new Diggers(ff);
		Digger digger = diggers.createDigger("Diggers name", "secretname");
		diggers.newGame(digger);
		GolddiggerServer server = new GolddiggerServer();
		server.start(diggers, "target/calls.log");
		WebConversation wc = new WebConversation();

		WebResponse response;
		move(wc, "east");
		assertTrue("Wasnt initialised to day-time",((DayNightPlugin) digger.getGoldField().getPluginService().getPlugins().next()).isDay());
		response = view(wc);
		assertEquals("This shouldn't have failed","-----\n?www?\nw.b.w\n?www?\n-----",response.getText().trim());
		move(wc, "east");
		move(wc, "west");
		move(wc, "west");
		move(wc, "east");
		assertFalse("Did not change to night-time",((DayNightPlugin) digger.getGoldField().getPluginService().getPlugins().next()).isDay());
		response = view(wc);
		assertEquals("Did not reduce the line of sight","www\n.b.\nwww",response.getText().trim());
		move(wc, "east");
		move(wc, "west");
		move(wc, "west");
		move(wc, "east");
		assertTrue("Did not return back to day",((DayNightPlugin) digger.getGoldField().getPluginService().getPlugins().next()).isDay());
		response = view(wc);
		assertEquals("Did not reset the line of sight","-----\n?www?\nw.b.w\n?www?\n-----",response.getText().trim());
		move(wc, "east");
		move(wc, "east");//invalid move
		move(wc, "east");//invalud move
		move(wc, "west");
		assertFalse("Did not count the invalid moves",((DayNightPlugin) digger.getGoldField().getPluginService().getPlugins().next()).isDay());
		response = view(wc);
		assertEquals("www\n.b.\nwww",response.getText().trim());
		
		server.stop();
	}
	
	private static WebResponse move(WebConversation wc, String direction) throws MalformedURLException, IOException, SAXException{
		return wc.getResponse("http://localhost:8066/golddigger/digger/secretname/move/"+direction);
	}
	private static WebResponse view(WebConversation wc) throws MalformedURLException, IOException, SAXException{
		return wc.getResponse("http://localhost:8066/golddigger/digger/secretname/view");
	}
}
