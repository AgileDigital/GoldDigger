package com.agical.golddigger;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;


import com.agical.golddigger.model.Digger;
import com.agical.golddigger.model.GoldField;
import com.agical.golddigger.model.Position;
import com.agical.golddigger.model.Square;
import com.agical.golddigger.model.fieldcreator.FieldCreator;
import com.agical.golddigger.model.fieldcreator.StringFieldCreator;

public class TestLineOfSight {
	private FieldCreator fieldCreator;
	private GoldField square_1, square_2, hexagon_1, hexagon_2;
	private Digger digger;
	
	
        
    @Before
    public void before() throws Exception {
    	
    	String map = "wwwww\nw...w\nw.b.w\nw...w\nwwwww\n";
    	
        fieldCreator = new StringFieldCreator(createSetting(1, 4) + map);
        square_1 = new GoldField(fieldCreator);
        
        fieldCreator = new StringFieldCreator(createSetting(2, 4) + map);
        square_2 = new GoldField(fieldCreator);
        
        fieldCreator = new StringFieldCreator(createSetting(1, 6) + map);
        hexagon_1 = new GoldField(fieldCreator);
        
        fieldCreator = new StringFieldCreator(createSetting(2, 6) + map);
        hexagon_2 = new GoldField(fieldCreator);
    }
    
    // Test sight view for the centre
    
    private String createSetting(int los_length, int no_of_sides) {
    	return "!line_of_sight_length=" + los_length + "\n!number_of_sides=" + no_of_sides + "\n!map=\n";
    }
    @Test
    public void centreSquareSightView_1() throws Exception {
    	digger = new Digger("Diggers name", "secretName");
        digger.newGame(square_1);
        
        Position startPosition = new Position(2,2);
        digger.setPosition(startPosition);
        assertEquals( "...\n.b.\n...\n", digger.getView());
        
    }
    
    // Test the sight view for the sides
    @Test
    public void sideSquareSightView_1() throws Exception {
    	digger = new Digger("Diggers name", "secretName");
        digger.newGame(square_1);
        
        Position startPosition = new Position(1,2); 
        digger.setPosition(startPosition);
        assertEquals( "www\n...\n.b.\n", digger.getView());
           
        startPosition = new Position(2,1);
        digger.setPosition(startPosition);
        assertEquals( "w..\nw.b\nw..\n", digger.getView());
        
        startPosition = new Position(3,2);
        digger.setPosition(startPosition);
        assertEquals( ".b.\n...\nwww\n", digger.getView());
        
        startPosition = new Position(2,3);
        digger.setPosition(startPosition);
        assertEquals( "..w\nb.w\n..w\n", digger.getView());
        
    }
    
    // Test the sight view for the corners
    @Test
    public void cornerSquareSightView_1() throws Exception {
    	
    	digger = new Digger("Diggers name", "secretName");
        digger.newGame(square_1);
        
        Position startPosition = new Position(1,1);
        digger.setPosition(startPosition);
        assertEquals( "www\nw..\nw.b\n", digger.getView());
        
        startPosition = new Position(3,1);
        digger.setPosition(startPosition);
        assertEquals( "w.b\nw..\nwww\n", digger.getView());
        
        startPosition = new Position(3,3);
        digger.setPosition(startPosition);
        assertEquals( "b.w\n..w\nwww\n", digger.getView());
        
        startPosition = new Position(1,3);
        digger.setPosition(startPosition);
        assertEquals( "www\n..w\nb.w\n", digger.getView());
        
    }
    
    @Test
    public void centreSquareSightView_2() throws Exception {
    	digger = new Digger("Diggers name", "secretName");
        digger.newGame(square_2);
        
        Position startPosition = new Position(2,2);
        digger.setPosition(startPosition);
        assertEquals( "wwwww\nw...w\nw.b.w\nw...w\nwwwww\n", digger.getView());
        
    }
    
    @Test
    public void sideSquareSightView_2() throws Exception {
    	digger = new Digger("Diggers name", "secretName");
        digger.newGame(square_2);
        
        Position startPosition = new Position(1,2); 
        digger.setPosition(startPosition);
        assertEquals( "wwwww\nw...w\nw.b.w\nw...w\n", digger.getView());
           
        startPosition = new Position(2,1);
        digger.setPosition(startPosition);
        assertEquals( "wwww\nw...\nw.b.\nw...\nwwww\n", digger.getView());
        
        startPosition = new Position(3,2);
        digger.setPosition(startPosition);
        assertEquals( "w...w\nw.b.w\nw...w\nwwwww\n", digger.getView());
        
        startPosition = new Position(2,3);
        digger.setPosition(startPosition);
        assertEquals( "wwww\n...w\n.b.w\n...w\nwwww\n", digger.getView());
        
    }
    
    @Test
    public void cornerSquareSightView_2() throws Exception {
    	
    	digger = new Digger("Diggers name", "secretName");
        digger.newGame(square_2);
        
        Position startPosition = new Position(1,1);
        digger.setPosition(startPosition);
        assertEquals( "wwww\nw...\nw.b.\nw...\n", digger.getView());
        
        startPosition = new Position(3,1);
        digger.setPosition(startPosition);
        assertEquals( "w...\nw.b.\nw...\nwwww\n", digger.getView());
        
        startPosition = new Position(3,3);
        digger.setPosition(startPosition);
        assertEquals( "...w\n.b.w\n...w\nwwww\n", digger.getView());
        
        startPosition = new Position(1,3);
        digger.setPosition(startPosition);
        assertEquals( "wwww\n...w\n.b.w\n...w\n", digger.getView());
        
    }
    
    
    // Test cases for hexagon tiles
    
    public void centreHexagonSightView_1() throws Exception {
    	digger = new Digger("Diggers name", "secretName");
        digger.newGame(hexagon_1);
        
        Position startPosition = new Position(2,2);
        digger.setPosition(startPosition);
        assertEquals( "...\n.b.\n . \n", digger.getView());
        
    }
    
    @Test
    public void sideHexagonSightView_1() throws Exception {
    	digger = new Digger("Diggers name", "secretName");
        digger.newGame(hexagon_1);
        
        Position startPosition = new Position(1,2); 
        digger.setPosition(startPosition);
        assertEquals( "www\n...\n b \n", digger.getView());
           
        startPosition = new Position(2,1);
        digger.setPosition(startPosition);
        assertEquals( " . \nw.b\nw..\n", digger.getView());
        
        startPosition = new Position(3,2);
        digger.setPosition(startPosition);
        assertEquals( ".b.\n...\n w \n", digger.getView());
        
        startPosition = new Position(2,3);
        digger.setPosition(startPosition);
        assertEquals( " . \nb.w\n..w\n", digger.getView());
        
    }
    
    @Test
    public void cornerHexagonSightView_1() throws Exception {
    	
    	digger = new Digger("Diggers name", "secretName");
        digger.newGame(hexagon_1);
        
        Position startPosition = new Position(1,1);
        digger.setPosition(startPosition);
        assertEquals( " w \nw..\nw.b\n", digger.getView());
        
        startPosition = new Position(3,1);
        digger.setPosition(startPosition);
        assertEquals( " . \nw..\nwww\n", digger.getView());
        
        startPosition = new Position(3,3);
        digger.setPosition(startPosition);
        assertEquals( " . \n..w\nwww\n", digger.getView());
        
        startPosition = new Position(1,3);
        digger.setPosition(startPosition);
        assertEquals( " w \n..w\nb.w\n", digger.getView());
        
    }
    
    @Test
    public void centreHexagonSightView_2() throws Exception {
    	digger = new Digger("Diggers name", "secretName");
        digger.newGame(hexagon_2);
        
        Position startPosition = new Position(2,2);
        digger.setPosition(startPosition);
        assertEquals( " www \nw...w\nw.b.w\nw...w\n  w  \n", digger.getView());
        
    }
    
    @Test
    public void sideHexagonSightView_2() throws Exception {
    	digger = new Digger("Diggers name", "secretName");
        digger.newGame(hexagon_2);
        
        Position startPosition = new Position(1,2); 
        digger.setPosition(startPosition);
        assertEquals( "wwwww\nw...w\nw.b.w\n  .  \n", digger.getView());
           
        startPosition = new Position(2,1);
        digger.setPosition(startPosition);
        assertEquals( " w  \nw...\nw.b.\nw...\nwww \n", digger.getView());
        
        startPosition = new Position(3,2);
        digger.setPosition(startPosition);
        assertEquals( " ... \nw.b.w\nw...w\nwwwww\n", digger.getView());
        
        startPosition = new Position(2,3);
        digger.setPosition(startPosition);
        assertEquals( "  w \n...w\n.b.w\n...w\n www\n", digger.getView());
    }
    
    @Test
    public void cornerHexagonSightView_2() throws Exception {
    	
    	digger = new Digger("Diggers name", "secretName");
        digger.newGame(hexagon_2);
        
        Position startPosition = new Position(1,1);
        digger.setPosition(startPosition);
        assertEquals( "wwww\nw...\nw.b.\nw.. \n", digger.getView());
        
        startPosition = new Position(3,1);
        digger.setPosition(startPosition);
        assertEquals( " .  \nw.b.\nw...\nwwww\n", digger.getView());
        
        startPosition = new Position(3,3);
        digger.setPosition(startPosition);
        assertEquals( "  . \n.b.w\n...w\nwwww\n", digger.getView());
        
        startPosition = new Position(1,3);
        digger.setPosition(startPosition);
        assertEquals( "wwww\n...w\n.b.w\n ..w\n", digger.getView());
        
    }
}
