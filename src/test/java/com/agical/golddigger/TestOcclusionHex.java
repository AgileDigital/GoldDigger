package com.agical.golddigger;
import static com.agical.golddigger.model.fieldcreator.StringFieldCreator.DELIMITER;
import static com.agical.golddigger.model.fieldcreator.StringFieldCreator.LINE_OF_SIGHT;
import static com.agical.golddigger.model.fieldcreator.StringFieldCreator.NO_OF_SIDES;
import static com.agical.golddigger.model.fieldcreator.StringFieldCreator.SEPERATOR;
import static com.agical.golddigger.model.fieldcreator.StringFieldCreator.TILES;

import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.agical.golddigger.model.Digger;
import com.agical.golddigger.model.GoldField;
import com.agical.golddigger.model.Position;
import com.agical.golddigger.model.fieldcreator.FieldCreator;
import com.agical.golddigger.model.fieldcreator.StringFieldCreator;

public class TestOcclusionHex {
	private FieldCreator testfieldCreator;
	private GoldField testField;
	private Digger digger;
	
	
	@Before
	public void before() throws Exception{
		
		
		String testMap = "wwwwwwwww\nw.......w\nw.......w\nw.......w\nw...w...w\nw.......w\nw.......w\nw.......w\nwwwwwwwww\n";
		
		testfieldCreator = new StringFieldCreator(createSetting(4,6) + testMap);
	
		testField = new GoldField(testfieldCreator);
		testField.toggleDiggercentreingTo(false);
	}


	private String createSetting(int los_length, int no_of_sides) {
    	return DELIMITER+LINE_OF_SIGHT+" "+SEPERATOR +" "+ los_length + "\n"+
    			DELIMITER+NO_OF_SIDES+" "+SEPERATOR+" "+ no_of_sides + "\n"+
    			DELIMITER+TILES+"\n";
    }
	
	
	// Test hex occlusion from the north
		@Test
	    public void hexOcclusionNorth() throws Exception {
	    	digger = new Digger("Diggers name", "secretName");
	        digger.newGame(testField);
	        
	        Position startPosition = new Position(3,4);
	        digger.setPosition(startPosition);
	        assertEquals("?wwwwwww?\nw.......w\nw.......w\nw.......w\nw...w...w\nw..???..w\n??.???.??\n?????????\n", digger.getView());
	        
	    }
	
	// Test hex occlusion from the N30W
		@Test
	    public void hexOcclusionN30W() throws Exception {
	    	digger = new Digger("Diggers name", "secretName");
	        digger.newGame(testField);
	        
	        Position startPosition = new Position(2,3);
	        digger.setPosition(startPosition);
	        assertEquals("?wwwww?w\nw.......\nw.......\nw.......\nw...w?..\nw...????\n??..????\n", digger.getView());
	        
		    }
		
		
	// Test hex occlusion from the N60W
		@Test
	    public void hexOcclusionN60W() throws Exception {
	    	digger = new Digger("Diggers name", "secretName");
	        digger.newGame(testField);
	        
	        Position startPosition = new Position(3,3);
	        digger.setPosition(startPosition);
	        assertEquals("?wwwww??\nw.......\nw.......\nw.......\nw...w???\nw....???\nw.....??\n??...???\n", digger.getView());
	        
	    }
	
		// Test hex occlusion from the west
		@Test
	    public void hexOcclusionWest() throws Exception {
	    	digger = new Digger("Diggers name", "secretName");
	        digger.newGame(testField);
	        
	        Position startPosition = new Position(4,2);
	        digger.setPosition(startPosition);
	        assertEquals("?www???\nw.....?\nw......\nw....??\nw...w??\nw.....?\nw......\nw....??\n??w????\n", digger.getView());
	        
	    }
		
	// Test hex occlusion from the S60W
		@Test
	    public void hexOcclusionS60W() throws Exception {
	    	digger = new Digger("Diggers name", "secretName");
	        digger.newGame(testField);
	        
	        Position startPosition = new Position(4,3);
	        digger.setPosition(startPosition);
	        assertEquals("???w????\n?.....??\nw....???\nw....???\nw...w.?.\nw.......\nw.......\nw......?\n??www???\n", digger.getView());
	        
	    }
				
	// Test hex occlusion from the S30W
		@Test
	    public void hexOcclusionS30W() throws Exception {
	    	digger = new Digger("Diggers name", "secretName");
	        digger.newGame(testField);
	        
	        Position startPosition = new Position(5,3);
	        digger.setPosition(startPosition);
	        assertEquals("???.????\n?...????\nw...???.\nw...w...\nw.......\nw.......\nw.......\nwwwwwww?\n", digger.getView());
	        
	    }
				
	// Test hex occlusion from the South
		@Test
	    public void hexOcclusionSouth() throws Exception {
	    	digger = new Digger("Diggers name", "secretName");
	        digger.newGame(testField);
	        
	        Position startPosition = new Position(5,4);
	        digger.setPosition(startPosition);
	        assertEquals("?????????\n?..???..?\nw...?...w\nw...w...w\nw.......w\nw.......w\nw.......w\n??wwwww??\n", digger.getView());
	        
	    }
				
	// Test hex occlusion from the S30E
		@Test
	    public void hexOcclusionS30E() throws Exception {
	    	digger = new Digger("Diggers name", "secretName");
	        digger.newGame(testField);
	        
	        Position startPosition = new Position(5,5);
	        digger.setPosition(startPosition);
	        assertEquals("????.???\n????...?\n.???...w\n...w...w\n.......w\n.......w\n.......w\n?wwwwwww\n", digger.getView());
	        
	    }
	// Test hex occlusion from the S60E
		@Test
	    public void hexOcclusionS60E() throws Exception {
	    	digger = new Digger("Diggers name", "secretName");
	        digger.newGame(testField);
	        
	        Position startPosition = new Position(4,5);
	        digger.setPosition(startPosition);
	        assertEquals("????w???\n??.....?\n???....w\n???....w\n.?.w...w\n.......w\n.......w\n?......w\n???www??\n", digger.getView());
	        
	    }
				
				
	// Test hex occlusion from the east
		@Test
	    public void hexOcclusionEast() throws Exception {
	    	digger = new Digger("Diggers name", "secretName");
	        digger.newGame(testField);
	        
	        Position startPosition = new Position(4,6);
	        digger.setPosition(startPosition);
	        assertEquals("???www?\n?.....w\n......w\n??....w\n??w...w\n?.....w\n......w\n??....w\n????w??\n", digger.getView());
	        
	    }
		
	// Test hex occlusion from the N60E
		@Test
	    public void hexOcclusionN60E() throws Exception {
	    	digger = new Digger("Diggers name", "secretName");
	        digger.newGame(testField);
	        
	        Position startPosition = new Position(3,5);
	        digger.setPosition(startPosition);
	        assertEquals("??wwwww?\n.......w\n.......w\n.......w\n???w...w\n???....w\n??.....w\n???...??\n", digger.getView());
	        
	    }
		
	// Test hex occlusion from the N30E
		@Test
	    public void hexOcclusionN30E() throws Exception {
	    	digger = new Digger("Diggers name", "secretName");
	        digger.newGame(testField);
	        
	        Position startPosition = new Position(2,5);
	        digger.setPosition(startPosition);
	        assertEquals("w?wwwww?\n.......w\n.......w\n.......w\n..?w...w\n????...w\n????..??\n", digger.getView());
			        
			    }
}
