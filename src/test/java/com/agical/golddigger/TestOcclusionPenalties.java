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

import org.junit.Test;

public class TestOcclusionPenalties {
	private FieldCreator fieldCreator_2, fieldCreator_3, fieldCreator_multi_2, fieldCreator_multi_3;
	private FieldCreator squareFieldCreator_penalties, hexFieldCreator_penalties;
	private GoldField square_2, square_3, square_multi_2, square_multi_3, square_penalties;
	private GoldField hex_penalties;
	private Digger digger;
	
	@Before
	public void before() throws Exception{
		String map_penalties =		"wwwwwwww\n" +
									"w......w\n" +
									"w.h..w.w\n" +
									"w..h...w\n" +
									"w....c.w\n" +
									"wwwwwwww\n";

		String hex_map_penalties =	"wwwwwwww\n" +
									"w......w\n" +
									"w.c..w.w\n" +
									"w..h...w\n" +
									"w.h....w\n" +
									"wwwwwwww\n";
		
		squareFieldCreator_penalties = new StringFieldCreator(createSetting(10,4) + map_penalties);
		hexFieldCreator_penalties = new StringFieldCreator(createSetting(10,6) + hex_map_penalties);
		
		square_penalties = new GoldField(squareFieldCreator_penalties);
		hex_penalties = new GoldField(hexFieldCreator_penalties);
		
		square_penalties.toggleDiggercentreingTo(false);		
		hex_penalties.toggleDiggercentreingTo(false);
	}
	
	private String createSetting(int los_length, int no_of_sides) {
    	return DELIMITER+LINE_OF_SIGHT+" "+SEPERATOR +" "+ los_length + "\n"+
    			DELIMITER+NO_OF_SIDES+" "+SEPERATOR+" "+ no_of_sides + "\n"+
    			DELIMITER+TILES+"\n";
    }
		//Occlusion Penalty tests
	@Test
	public void squareOcclusionPenalties(){
		digger = new Digger("Diggers name", "secretName");
        digger.newGame(square_penalties);
        Position startPosition = new Position(1, 1);
        digger.setPosition(startPosition);
        
        assertEquals(	"www?????\n" +
						"w......w\n" +
						"w.h..w??\n" +
						"?..h..??\n" +
						"?...?c.w\n" +
						"?wwww???\n"   , digger.getView());
        
        startPosition = new Position(1, 6);
        digger.setPosition(startPosition);
        
        assertEquals(	"?????www\n" +
						"w......w\n" +
						"w.h.?w.w\n" +
						"w?????.?\n" +
						"?????c.?\n" +
						"?????ww?\n"   , digger.getView());
        
        startPosition = new Position(4, 6);
        digger.setPosition(startPosition);
        assertEquals(	"??w???w?\n" +
						"??..??.?\n" +
						"???..w.?\n" +
						"????...w\n" +
						"???..c.w\n" +
						"?????www\n", digger.getView());
        
        startPosition = new Position(4, 1);
        digger.setPosition(startPosition);
        assertEquals(	"?wwwww??\n" +
						"?.....??\n" +
						"?.h..w?w\n" +
						"w..h...w\n" +
						"w....c.?\n" +
						"www?????\n", digger.getView());
        
        startPosition = new Position(2, 3);
        digger.setPosition(startPosition);
        assertEquals(	"?wwwww??\n" +
						"w......?\n" +
						"w.h..w??\n" +
						"w..h...?\n" +
						"w....c.w\n" +
						"wwwwww??\n", digger.getView());        
	}
	
	@Test
	public void hexOcclusionPenalties(){
		digger = new Digger("Diggers name", "secretName");
        digger.newGame(hex_penalties);
        
       Position startPosition = new Position(1, 1);
        digger.setPosition(startPosition);
        assertEquals(	"wwww????\n" +
						"w......w\n" +
						"w.c..???\n" +
						"w..h????\n" +
						"?.h.????\n" +
						"?wwww???\n", digger.getView());
        
        startPosition = new Position(4, 1);
        digger.setPosition(startPosition);
        assertEquals(	"?wwwww??\n" +
						"?......?\n" +
						"?.c..???\n" +
						"w..h?..w\n" +
						"w.h....w\n" +
						"www?????\n", digger.getView());
        
        startPosition = new Position(4, 6);
        digger.setPosition(startPosition);
        assertEquals(	"?w????w?\n" +
						"??.???.?\n" +
						"w.c.?w.w\n" +
						"?..h...w\n" +
						"w.h....w\n" +
						"????wwww\n", digger.getView());
        
        startPosition = new Position(2, 6);
        digger.setPosition(startPosition);
        assertEquals(	"?w?w?www\n" +
						"w?.....w\n" +
						"??c?.w.w\n" +
						"?????..w\n" +
						"?????..?\n" +
						"????www?\n", digger.getView());
        
        startPosition = new Position(2, 3);
        digger.setPosition(startPosition);
        assertEquals(	"?wwwww?w\n" +
						"?......?\n" +
						"w.c..w??\n" +
						"w..h..??\n" +
						"w.h....w\n" +
						"wwwwwww?\n", digger.getView());
        
        
	}

}
