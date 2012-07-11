package com.agical.golddigger;

import static org.junit.Assert.*;
import static com.agical.golddigger.model.fieldcreator.StringFieldCreator.*;

import org.junit.Test;
import org.junit.Ignore;

import com.agical.golddigger.model.Digger;
import com.agical.golddigger.model.GoldField;
import com.agical.golddigger.model.Position;
import com.agical.golddigger.model.tiles.Square;
import com.agical.golddigger.model.fieldcreator.FieldCreator;
import com.agical.golddigger.model.fieldcreator.RandomFieldCreator;
import com.agical.golddigger.model.fieldcreator.StringFieldCreator;
import com.agical.golddigger.plugins.DayNightPlugin;


public class TestStringFieldCreator {

    @Test
    @Ignore
    public void testname() throws Exception {
        int maxLatitude = 50;
        int maxLongitude = 100;
        RandomFieldCreator randomFieldCreator = new RandomFieldCreator(maxLatitude,maxLongitude,400);
        Square[][] createField = randomFieldCreator.createField();
        String result = Square.getField(createField);
        System.out.println(result);
        FieldCreator fieldCreator = new StringFieldCreator(result);
        Square[][] recreatedField = fieldCreator.createField();
        assertEquals(result, Square.getField(recreatedField));
        assertEquals(maxLatitude, fieldCreator.getMaxLatitude());
        assertEquals(maxLongitude, fieldCreator.getMaxLongitude());
    }
    
    @Test
    // Testing to make sure the tiles are parsed properly
    public void testTileParsing(){    	
    	String field;
    	StringFieldCreator sfc;
    	Square[][] squares;
    	final String TILES = "www\nwbw\nw.w\nw9w\nwww";
    	final String PER_TYPES = "b=8\n9=3";
    	final String TILES_HEADER    = section(StringFieldCreator.TILES);
    	final String PER_TYPE_HEADER = section(StringFieldCreator.COSTS);
    	
    	// Test No Sections
    	field = TILES;
    	sfc = new StringFieldCreator(field);
    	squares = sfc.createField();
    	assertEquals(TILES, Square.getField(squares).trim()); //have to trim of the last \n from getField();
    	
    	// Test tile section
    	field = TILES_HEADER + TILES;
    	sfc = new StringFieldCreator(field);
    	squares = sfc.createField();
    	assertEquals(TILES, Square.getField(squares).trim());
    	
    	// Test tile and per-type cost sections
    	field = TILES_HEADER + TILES+ "\n" + PER_TYPE_HEADER + PER_TYPES;
    	sfc = new StringFieldCreator(field);
    	squares = sfc.createField();
    	assertEquals(TILES, Square.getField(squares).trim());
    }
    
    @Test
    // Testing that the cost is parsed properly 
    public void testCosts(){
    	final String FIELD =
    			section(TILES)+
    				"wwwwww\n" +
    				"w..b.w\n" +
    				"wwwwww\n" +
    			section(COSTS)+
    			sectionValue("b", "2");
    	FieldCreator fc = new StringFieldCreator(FIELD);
    	Square[][] field = fc.createField();
    	assertEquals("Default cost is wrong",100,field[1][1].getCost());
    	assertEquals("Default per type cost is wrong",100,field[1][2].getCost());
    	assertEquals("Cost per type is wrong",2,field[1][3].getCost());
    }
    
    @Test
    // Testing how the StringFieldFactory retrieves "sections" from the "result" string.
    public void testGetSection(){
    	final String SECTION = section(TILES)+"\n\n\n\nwww\nw.w\nwww\n\n\n\n";
    	StringFieldCreator scf = new StringFieldCreator(SECTION);
    	String section = scf.getSection(TILES);
    	assertFalse("The newlines weren't trimed of the start",section.startsWith("\n"));
    	assertFalse("The newlines weren't trimed of the end",section.endsWith("\n"));

    	final String tiles = "www\nwbw\nw.w\nw9w\nwww";
    	final String pertypes = (sectionValue("b","8")+sectionValue("9","3")).trim();
    	String field =  section(TILES) + tiles +"\n";
    	field = field + section(COSTS)+ pertypes +"\n";
    	
    	StringFieldCreator sfc = new StringFieldCreator(field);
    	assertEquals("The field tile section was not seperated correctly", tiles,    sfc.getSection(TILES));
    	assertEquals("The types cost section was not seperated correctly", pertypes, sfc.getSection(COSTS));
    }
    
    @Test
    public void testMaxLatLong(){
		String string_map = 
				attribute(LINE_OF_SIGHT,3) +
				attribute(NO_OF_SIDES,6)+
				section(TILES) +
					"wwwww\n" +
					"wbw.w\n" +
					"w1w.w\n" +
					"wwwww\n";
		
        Digger digger = new Digger("Diggers name", "secretName");
        digger.newGame(new GoldField(new StringFieldCreator(string_map)));
		digger.move(Position.SOUTH);
		digger.grab();
		digger.move(Position.NORTH);
		digger.drop();
		assertEquals("max height of the map is wrong",2, digger.getGoldField().getMaxLatitude());
		assertEquals("max width of the map is wrong",3, digger.getGoldField().getMaxLongitude());
		assertFalse("hasGold is calculated wrong",digger.getGoldField().hasGold());
    }
    
    
    @Test
    public void testPlugins(){
    	String str = 
    			section(TILES)+
    				"www\nw.w\nwww\n"+
    			section(PLUGINS)+
    				DayNightPlugin.NAME;
    	
    	Digger digger = new Digger("Diggers name", "secretName");
        digger.newGame(new GoldField(new StringFieldCreator(str)));
        
        PluginService service = digger.getGoldField().getPluginService();
        System.out.println();
        assertNotNull(service);
        assertTrue(service.getPlugins().hasNext());
        assertTrue(service.getPlugins().next() instanceof DayNightPlugin);
    }
}
