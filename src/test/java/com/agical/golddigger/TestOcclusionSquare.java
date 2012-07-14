package com.agical.golddigger;
//TEST COMMIT
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
import com.agical.golddigger.model.tiles.WallSquare;

public class TestOcclusionSquare {
	private FieldCreator fieldCreator_2, fieldCreator_3, fieldCreator_multi_2, fieldCreator_multi_3;
	private GoldField square_2, square_3, square_multi_2, square_multi_3;
	private Digger digger;
	
	
	@Before
	public void before() throws Exception{
		
		String map_2 = "wwwwwwww\nw......w\nw....w.w\nw......w\nw.w....w\nw......w\nwwwwwwww\n";
		String map_3 = "wwwwwwwww\nw.......w\nw.......w\nw.......w\nw....w..w\nw.......w\nw.......w\nw.......w\nwwwwwwwww\n";
		String map_multi_2 = "wwwwwwwww\nw.......w\nw.www...w\nw.....w.w\nw.....w.w\nw.....w.w\nwwwwwwwww\n";
		String map_multi_3 = "wwwwwwwww\nw.......w\nw.......w\nw..w....w\nw..www..w\nw....w..w\nw.......w\nw.......w\nwwwwwwwww\n";
		
		fieldCreator_2 = new StringFieldCreator(createSetting(2,4) + map_2);
		fieldCreator_3 = new StringFieldCreator(createSetting(3,4) + map_3);
		fieldCreator_multi_2 = new StringFieldCreator(createSetting(2,4) + map_multi_2);
		fieldCreator_multi_3 = new StringFieldCreator(createSetting(3,4) + map_multi_3);
		
		square_2 = new GoldField(fieldCreator_2);
		square_3 = new GoldField(fieldCreator_3);
		square_multi_2 = new GoldField(fieldCreator_multi_2);
		square_multi_3 = new GoldField(fieldCreator_multi_3);
		
		square_2 = setValues(square_2);
		square_3 = setValues(square_3);
		square_multi_2 = setValues(square_multi_2);
		square_multi_3 = setValues(square_multi_3);
	}

	private GoldField setValues(GoldField field){
		WallSquare wallSquare = new WallSquare();
		wallSquare.setRadius(100);
		wallSquare.setOcclusionCost(100);
		for(int i = 0; i < field.getSquares().length; i++){
			for(int j = 0; j < (field.getSquares())[i].length; j++){
				if(field.getSquares()[i][j].getClass().isInstance(wallSquare)){
					field.getSquares()[i][j] = wallSquare;
				}
			}
		}
		field.toggleDiggercentreingTo(false);
		return field;
	}

	private String createSetting(int los_length, int no_of_sides) {
    	return DELIMITER+LINE_OF_SIGHT+" "+SEPERATOR +" "+ los_length + "\n"+
    			DELIMITER+NO_OF_SIDES+" "+SEPERATOR+" "+ no_of_sides + "\n"+
    			DELIMITER+TILES+"\n";
    }
	
	
	// Test square occlusion coming from the north with a line of sight length 2
	@Test
    public void squareNorthOcclusion_2() throws Exception {
    	digger = new Digger("Diggers name", "secretName");
        digger.newGame(square_2);
        
        Position startPosition = new Position(5, 2);
        digger.setPosition(startPosition);
        assertEquals("w???.\nw.w..\nw....\n?www?\n", digger.getView());
        
    }
	
	// Test square occlusion coming from the north-east with a line of sight length 2
	@Test
	public void squareNorthEastOcclusion_2() throws Exception {
	    digger = new Digger("Diggers name", "secretName");
	    digger.newGame(square_2);
	        
	    Position startPosition = new Position(5, 1);
	    digger.setPosition(startPosition);
	    assertEquals("?.??\nw.w?\nw...\nwww?\n", digger.getView());
	        
	}
	
	// Test square occlusion coming from the east with a line of sight length 2
	@Test
	public void squareEastOcclusion_2() throws Exception {
	    digger = new Digger("Diggers name", "secretName");
	    digger.newGame(square_2);
	        
	    Position startPosition = new Position(4, 1);
	    digger.setPosition(startPosition);
	    assertEquals("?...\nw..?\nw.w?\nw..?\n?www\n", digger.getView());
	        
	}
	
	// Test square occlusion coming from the south-east with a line of sight length 2
	@Test
	public void squareSouthEastOcclusion_2() throws Exception {
	    digger = new Digger("Diggers name", "secretName");
	    digger.newGame(square_2);
	        
	    Position startPosition = new Position(1, 4);
	    digger.setPosition(startPosition);
	    assertEquals("?www?\n.....\n...w?\n...??\n", digger.getView());
	        
	}
	
	// Test square occlusion coming from the south with a line of sight length 2
	@Test
	public void squareSouthOcclusion_2() throws Exception {
	    digger = new Digger("Diggers name", "secretName");
	    digger.newGame(square_2);
	        
	    Position startPosition = new Position(1, 5);
	    digger.setPosition(startPosition);
	    assertEquals("?www?\n....w\n..w.w\n.???w\n", digger.getView());
	        
	}
	
	// Test square occlusion coming from the south-west with a line of sight length 2
	@Test
	public void squareSouthWestOcclusion_2() throws Exception {
	    digger = new Digger("Diggers name", "secretName");
	    digger.newGame(square_2);
	        
	    Position startPosition = new Position(1, 6);
	    digger.setPosition(startPosition);
	    assertEquals("?www\n...w\n?w.w\n??.?\n", digger.getView());
	        
	}
	
	// Test square occlusion coming from the west with a line of sight length 2
	@Test
	public void squareWestOcclusion_2() throws Exception {
	    digger = new Digger("Diggers name", "secretName");
	    digger.newGame(square_2);
	        
	    Position startPosition = new Position(2, 6);
	    digger.setPosition(startPosition);
	    assertEquals("www?\n?..w\n?w.w\n?..w\n...?\n", digger.getView());
	        
	}

	// Test square occlusion coming from the north-west with a line of sight length 2
	@Test
	public void squareNorthWestOcclusion_2() throws Exception {
	    digger = new Digger("Diggers name", "secretName");
	    digger.newGame(square_2);
	        
	    Position startPosition = new Position(3, 6);
	    digger.setPosition(startPosition);
	    assertEquals("??.?\n?w.w\n...w\n...w\n...?\n", digger.getView());
	        
	}
	
	// Test square occlusion coming from the north with a line of sight length 3
	@Test
	public void squareNorthOcclusion_3() throws Exception {
	    digger = new Digger("Diggers name", "secretName");
	    digger.newGame(square_3);
	        
	    Position startPosition = new Position(6, 5);
	    digger.setPosition(startPosition);
	    assertEquals("...?..w\n...w..w\n......w\n......w\n......w\n?wwwww?\n", digger.getView());
	        
	}
	
	// Test square occlusion coming from the north-east with a line of sight length 3
	@Test
	public void squareNorthEastOcclusion_3() throws Exception {
	    digger = new Digger("Diggers name", "secretName");
	    digger.newGame(square_3);
	        
	    Position startPosition = new Position(6, 3);
	    digger.setPosition(startPosition);
	    assertEquals("w....??\nw....w?\nw......\nw......\nw......\n?wwwww?\n", digger.getView());
	        
	}
	
	// Test square occlusion coming from the east with a line of sight length 3
	@Test
	public void squareEastOcclusion_3() throws Exception {
	    digger = new Digger("Diggers name", "secretName");
	    digger.newGame(square_3);
	    
	    Position startPosition = new Position(4, 3);
	    digger.setPosition(startPosition);
	    assertEquals("w......\nw......\nw......\nw....w?\nw......\nw......\nw......\n", digger.getView());
	        
	}
	
	// Test square occlusion coming from the south-east with a line of sight length 3
	@Test
	public void squareSouthEastOcclusion_3() throws Exception {
	    digger = new Digger("Diggers name", "secretName");
	    digger.newGame(square_3);
	        
	    Position startPosition = new Position(2, 3);
	    digger.setPosition(startPosition);
	    assertEquals("?wwwww?\nw......\nw......\nw......\nw....w?\nw....??\n", digger.getView());
	        
	}
	
	// Test square occlusion coming from the south with a line of sight length 3
	@Test
	public void squareSouthOcclusion_3() throws Exception {
	    digger = new Digger("Diggers name", "secretName");
	    digger.newGame(square_3);
	        
	    Position startPosition = new Position(2, 5);
	    digger.setPosition(startPosition);
	    assertEquals("?wwwww?\n......w\n......w\n......w\n...w..w\n...?..w\n", digger.getView());
	        
	}

	// Test square occlusion coming from the south-west with a line of sight length 3
	@Test
	public void squareSouthWestOcclusion_3() throws Exception {
	    digger = new Digger("Diggers name", "secretName");
	    digger.newGame(square_3);
	        
	    Position startPosition = new Position(2, 7);
	    digger.setPosition(startPosition);
	    assertEquals("?www?\n....w\n....w\n....w\n?w..?\n??..?\n", digger.getView());
	        
	}
	
	// Test square occlusion coming from the west with a line of sight length 3
	@Test
	public void squareWestOcclusion_3() throws Exception {
	    digger = new Digger("Diggers name", "secretName");
	    digger.newGame(square_3);
	        
	    Position startPosition = new Position(4, 7);
	    digger.setPosition(startPosition);
	    assertEquals("....?\n....?\n....w\n?w..w\n....w\n....?\n....?\n", digger.getView());
	        
	}
	
	// Test square occlusion coming from the north-west with a line of sight length 3
	@Test
	public void squareNorthWestOcclusion_3() throws Exception {
	    digger = new Digger("Diggers name", "secretName");
	    digger.newGame(square_3);
	        
	    Position startPosition = new Position(6, 7);
	    digger.setPosition(startPosition);
	    assertEquals("??..?\n?w..?\n....w\n....w\n....w\n?www?\n", digger.getView());
	        
	}
	
	// Test multiple square occlusions coming from the north with a line of sight length 2
	@Test
	public void squareMultipleNorthOcclusion_2() throws Exception {
	    digger = new Digger("Diggers name", "secretName");
	    digger.newGame(square_multi_2);
	        
	    Position startPosition = new Position(3, 3);
	    digger.setPosition(startPosition);
	    assertEquals("?????\n?www?\n.....\n.....\n.....\n", digger.getView());
	        
	}
	
	// Test multiple square occlusions coming from the east with a line of sight length 2
	@Test
	public void squareMultipleEastOcclusion_2() throws Exception {
	    digger = new Digger("Diggers name", "secretName");
	    digger.newGame(square_multi_2);
	        
	    Position startPosition = new Position(4, 5);
	    digger.setPosition(startPosition);
	    assertEquals("ww.??\n...w?\n...w?\n...w?\nwww??\n", digger.getView());
	        
	}	
	
	// Test multiple square occlusions coming from the south with a line of sight length 2
	@Test
	public void squareMultipleSouthOcclusion_2() throws Exception {
	    digger = new Digger("Diggers name", "secretName");
	    digger.newGame(square_multi_2);
	        
	    Position startPosition = new Position(1, 3);
	    digger.setPosition(startPosition);
	    assertEquals("?www?\n.....\n?www?\n?????\n", digger.getView());
	        
	}
			
	// Test multiple square occlusions coming from the west with a line of sight length 2
	@Test
	public void squareMultipleWestOcclusion_2() throws Exception {
	    digger = new Digger("Diggers name", "secretName");
	    digger.newGame(square_multi_2);
	        
	    Position startPosition = new Position(4, 7);
	    digger.setPosition(startPosition);
	    assertEquals("??.?\n?w.w\n?w.w\n?w.w\n??w?\n", digger.getView());
	        
	}
	
	// Test multiple square occlusions coming from the north with a line of sight length 3
	@Test
	public void squareMultipleNorthOcclusion_3() throws Exception {
	    digger = new Digger("Diggers name", "secretName");
	    digger.newGame(square_multi_3);
	        
	    Position startPosition = new Position(6, 4);
	    digger.setPosition(startPosition);
	    assertEquals(".??????\n..ww???\n....w?.\n.......\n.......\n?wwwww?\n", digger.getView());
	        
	}
	
	// Test multiple square occlusions coming from the east with a line of sight length 3
	@Test
	public void squareMultipleEastOcclusion_3() throws Exception {
	    digger = new Digger("Diggers name", "secretName");
	    digger.newGame(square_multi_3);
	        
	    Position startPosition = new Position(4, 1);
	    digger.setPosition(startPosition);
	    assertEquals("?....\n?...?\nw..w?\nw..w?\nw....\n?....\n?....\n", digger.getView());
	        
	}
	
	// Test multiple square occlusions coming from the south with a line of sight length 3
	@Test
	public void squareMultipleSouthOcclusion_3() throws Exception {
	    digger = new Digger("Diggers name", "secretName");
	    digger.newGame(square_multi_3);
	        
	    Position startPosition = new Position(2, 4);
	    digger.setPosition(startPosition);
	    assertEquals("?wwwww?\n.......\n.......\n.?w....\n???ww..\n??????.\n", digger.getView());
	        
	}	
	
	// Test multiple square occlusions coming from the west with a line of sight length 3
	@Test
	public void squareMultipleWestOcclusion_3() throws Exception {
	    digger = new Digger("Diggers name", "secretName");
	    digger.newGame(square_multi_3);
	        
	    Position startPosition = new Position(4, 7);
	    digger.setPosition(startPosition);
	    assertEquals("....?\n....?\n....w\n?w..w\n?w..w\n?...?\n....?\n", digger.getView());
	        
	}	
		
}
