package com.agical.golddigger;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.agical.golddigger.model.Digger;
import com.agical.golddigger.model.GoldField;
import com.agical.golddigger.model.Position;
import com.agical.golddigger.model.fieldcreator.FieldCreator;
import com.agical.golddigger.model.fieldcreator.StringFieldCreator;
import static com.agical.golddigger.model.fieldcreator.StringFieldCreator.*;

public class TestHumanReadableHexTilesView {

	private FieldCreator fieldCreator;
	private GoldField hexagon_1, hexagon_2;
	private Digger digger;
	
    @Before
    public void before() throws Exception {
    	
    	String map = "wwwww\nw...w\nw.b.w\nw...w\nwwwww\n";
     
        fieldCreator = new StringFieldCreator(createSetting(1, 6) + map);
        hexagon_1 = new GoldField(fieldCreator);
        hexagon_1.toggleDiggercentreingTo(true);
        hexagon_1.toggleOcclusionTo(false);
        hexagon_1.setHumanReadableHexagonView(true);
        
        fieldCreator = new StringFieldCreator(createSetting(2, 6) + map);
        hexagon_2 = new GoldField(fieldCreator);
        hexagon_2.toggleDiggercentreingTo(true);
        hexagon_2.toggleOcclusionTo(false);
        hexagon_2.setHumanReadableHexagonView(true);
    }
    
    private String createSetting(int los_length, int no_of_sides) {
    	return DELIMITER+LINE_OF_SIGHT+" "+SEPERATOR +" "+ los_length + "\n"+
    			DELIMITER+NO_OF_SIDES+" "+SEPERATOR+" "+ no_of_sides + "\n"+
    			DELIMITER+TILES+"\n";
    }
    
    // Test sight view for the centre
    @Test
    public void centreHexagonSightView_1() throws Exception {
    	digger = new Digger("Diggers name", "secretName");
        digger.newGame(hexagon_1);
        
        Position startPosition = new Position(2,2);
        digger.setPosition(startPosition);
        assertEquals( " . \n" +
        			  ". .\n" +
        			  " b \n" +
        			  ". .\n" +
        			  " . \n" +
        			  "? ?\n" , digger.getView());
        
    }
    
    @Test
    public void sideHexagonSightView_1() throws Exception {
    	digger = new Digger("Diggers name", "secretName");
        digger.newGame(hexagon_1);
        
        Position startPosition = new Position(1,2); 
        digger.setPosition(startPosition);
        assertEquals( " w \n" +
        			  "w w\n" +
        			  " . \n" +
        			  ". .\n" +
        			  " b \n" +
        			  "? ?\n" , digger.getView());
           
        startPosition = new Position(2,1);
        digger.setPosition(startPosition);
        assertEquals( "? ?\n" +
        			  " . \n" +
        			  "w b\n" +
        			  " . \n" +
        			  "w .\n" +
        			  " . \n" , digger.getView());
        
        startPosition = new Position(3,2);
        digger.setPosition(startPosition);
        assertEquals( " b \n" +
        			  ". .\n" +
        			  " . \n" +
        			  ". .\n" +
        			  " w \n" +
        			  "? ?\n" , digger.getView());
        
        startPosition = new Position(2,3);
        digger.setPosition(startPosition);
        assertEquals( "? ?\n" +
        			  " . \n" +
        			  "b w\n" +
        			  " . \n" +
        			  ". w\n" +
        			  " . \n" , digger.getView());
        
    }
    
    @Test
    public void cornerHexagonSightView_1() throws Exception {  	
    	digger = new Digger("Diggers name", "secretName");
        digger.newGame(hexagon_1);
        
        Position startPosition = new Position(1,1);
        digger.setPosition(startPosition);
        assertEquals( "? ?\n" +
        			  " w \n" +
        			  "w .\n" +
        			  " . \n" +
        			  "w b\n" +
        			  " . \n" , digger.getView());
        
        startPosition = new Position(3,1);
        digger.setPosition(startPosition);
        assertEquals( "? ?\n" +
        			  " . \n" +
        			  "w .\n" +
        			  " . \n" +
        			  "w w\n" +
        			  " w \n" , digger.getView());
        
        startPosition = new Position(3,3);
        digger.setPosition(startPosition);
        assertEquals( "? ?\n" +
  			  		  " . \n" +
  			  		  ". w\n" +
  			  		  " . \n" +
  			  		  "w w\n" +
  			  		  " w \n" , digger.getView());
        
        startPosition = new Position(1,3);
        digger.setPosition(startPosition);
        assertEquals( "? ?\n" +
        			  " w \n" +
        			  ". w\n" +
        			  " . \n" +
        			  "b w\n" +
        			  " . \n" , digger.getView());
        
    }
    
    @Test
    public void centreHexagonSightView_2() throws Exception {
    	digger = new Digger("Diggers name", "secretName");
        digger.newGame(hexagon_2);
        
        Position startPosition = new Position(2,2);
        digger.setPosition(startPosition);
        assertEquals( "? w ?\n" +
        			  " w w \n" +
        			  "w . w\n" +
        			  " . . \n" +
        			  "w b w\n" +
        			  " . . \n" +
        			  "w . w\n" +
        			  " . . \n" +
        			  "? w ?\n" +
        			  " ? ? \n" , digger.getView());
        
    }
    
    @Test
    public void sideHexagonSightView_2() throws Exception {
    	digger = new Digger("Diggers name", "secretName");
        digger.newGame(hexagon_2);
        
        Position startPosition = new Position(1,2); 
        digger.setPosition(startPosition);
        assertEquals( "-----\n" +
        			  "-----\n" +
        			  "w w w\n" +
        			  " w w \n" +
        			  "w . w\n" +
        			  " . . \n" +
        			  "w b w\n" +
        			  " . . \n" +
        			  "? . ?\n" +
        			  " ? ? \n" , digger.getView());
           
        startPosition = new Position(2,1);
        digger.setPosition(startPosition);
        assertEquals( "-? ? \n" +
        			  "- w ?\n" +
        			  "-w . \n" +
        			  "- . .\n" +
        			  "-w b \n" +
        			  "- . .\n" +
        			  "-w . \n" +
        			  "- . .\n" +
        			  "-w w \n" +
        			  "- w ?\n" , digger.getView());
        
        startPosition = new Position(3,2);
        digger.setPosition(startPosition);
        assertEquals( "? . ?\n" +
        			  " . . \n" +
        			  "w b w\n" +
        			  " . . \n" +
        			  "w . w\n" +
        			  " . . \n" +
        			  "w w w\n" +
        			  " w w \n" +
        			  "-----\n" , digger.getView());
        
        startPosition = new Position(2,3);
        digger.setPosition(startPosition);
        assertEquals( " ? ?-\n" +
        			  "? w -\n" +
        			  " . w-\n" +
        			  ". . -\n" +
        			  " b w-\n" +
        			  ". . -\n" +
        			  " . w-\n" +
        			  ". . -\n" +
        			  " w w-\n" +
        			  "? w -\n" , digger.getView());
    }
    
    @Test
    public void cornerHexagonSightView_2() throws Exception {
    	
    	digger = new Digger("Diggers name", "secretName");
        digger.newGame(hexagon_2);
        
        Position startPosition = new Position(1,1);
        digger.setPosition(startPosition);
        assertEquals( "-----\n" +
        			  "-w w \n" +
        			  "- w w\n" +
        			  "-w . \n" +
        			  "- . .\n" +
        			  "-w b \n" +
        			  "- . .\n" +
        			  "-w . \n" +
        			  "- . ?\n" , digger.getView());
        
        startPosition = new Position(3,1);
        digger.setPosition(startPosition);
        assertEquals( "-? ? \n" +
        			  "- . ?\n" +
        			  "-w b \n" +
        			  "- . .\n" +
        			  "-w . \n" +
        			  "- . .\n" +
        			  "-w w \n" +
        			  "- w w\n" +
        			  "-----\n" +
        			  "-----\n" , digger.getView());
        
        startPosition = new Position(3,3);
        digger.setPosition(startPosition);
        assertEquals( " ? ?-\n" +
        			  "? . -\n" +
        			  " b w-\n" +
        			  ". . -\n" +
        			  " . w-\n" +
        			  ". . -\n" +
        			  " w w-\n" +
        			  "w w -\n" +
        			  "-----\n" +
        			  "-----\n" , digger.getView());
        
        startPosition = new Position(1,3);
        digger.setPosition(startPosition);
        assertEquals( "-----\n" +
        			  " w w-\n" +
        			  "w w -\n" +
        			  " . w-\n" +
        			  ". . -\n" +
        			  " b w-\n" +
        			  ". . -\n" +
        			  " . w-\n" +
        			  "? . -\n" , digger.getView());
        
    }
    
    @Test
    public void centreingViewOnDiggerHex() throws Exception{
    	hexagon_2.toggleDiggercentreingTo(true);
    	hexagon_2.setHumanReadableHexagonView(true);
    	digger = new Digger("Diggers name", "secretName");
        digger.newGame(hexagon_2);
        
        Position startPosition = new Position(1,1);
        digger.setPosition(startPosition);
        assertEquals( "-----\n" +
        			  "-w w \n" +
        			  "- w w\n" +
        			  "-w . \n" +
        			  "- . .\n" +
        			  "-w b \n" +
        			  "- . .\n" +
        			  "-w . \n" +
        			  "- . ?\n" , digger.getView());
        
        startPosition = new Position(2,2);
        digger.setPosition(startPosition);
        assertEquals( "? w ?\n" +
        			  " w w \n" +
        			  "w . w\n" +
        			  " . . \n" +
        			  "w b w\n" +
        			  " . . \n" +
        			  "w . w\n" +
        			  " . . \n" +
        			  "? w ?\n" +
        			  " ? ? \n" , digger.getView());
        
        startPosition = new Position(3,3);
        digger.setPosition(startPosition);
        System.out.println("*********");
        assertEquals( " ? ?-\n" +
        			  "? . -\n" +
        			  " b w-\n" +
        			  ". . -\n" +
        			  " . w-\n" +
        			  ". . -\n" +
        			  " w w-\n" +
        			  "w w -\n" +
        			  "-----\n" +
        			  "-----\n" , digger.getView());
    }
}
