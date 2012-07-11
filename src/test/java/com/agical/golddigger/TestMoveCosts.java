package com.agical.golddigger;

import static org.junit.Assert.*;

import org.junit.Test;

import com.agical.golddigger.model.tiles.*;
import com.agical.golddigger.model.Digger;
import com.agical.golddigger.model.GoldField;
import com.agical.golddigger.model.Position;
import com.agical.golddigger.model.fieldcreator.FieldCreator;
import com.agical.golddigger.model.fieldcreator.StringFieldCreator;
import com.agical.jambda.Functions.Fn1;

import static com.agical.golddigger.model.fieldcreator.StringFieldCreator.*;

public class TestMoveCosts {
	Digger digger;
	GoldField goldField;
	FieldCreator sfc;
	
	@Test
	public void testDefaults() {
		String string_map = 
				section(TILES)+
				"wwwwwww\n" +
				"wbtsdhw\n" +
				"w.frcmw\n" +
				"wwwwwww\n";
		
		sfc = new StringFieldCreator(string_map);
		goldField = new GoldField(sfc);
        digger = new Digger("Diggers name", "secretName");
        digger.newGame(goldField);
        
        moveAndAssertTime(Position.EAST, new TeleportSquare());
        moveAndAssertTime(Position.EAST, new ShallowWaterSquare());
        moveAndAssertTime(Position.EAST, new DeepWaterTile());
        moveAndAssertTime(Position.EAST, new HillSquare());
        moveAndAssertTime(Position.SOUTH, new MountainSquare());
        moveAndAssertTime(Position.WEST, new CitySquare());
        moveAndAssertTime(Position.WEST, new RoadSquare());
        moveAndAssertTime(Position.WEST, new ForestSquare());
        moveAndAssertTime(Position.WEST, new GoldSquare(0));
	}
	@Test
	public void testDefaultsHex() {
		String string_map = 
				attribute(NO_OF_SIDES,6)+
				section(TILES)+
				"wwwwwww\n" +
				"wbtsdhw\n" +
				"w.frcmw\n" +
				"wwwwwww\n";
		
		sfc = new StringFieldCreator(string_map);
		goldField = new GoldField(sfc);
        digger = new Digger("Diggers name", "secretName");
        digger.newGame(goldField);
        
        moveAndAssertTime(Position.NORTH_EAST, new TeleportSquare());
        moveAndAssertTime(Position.SOUTH_EAST, new ShallowWaterSquare());
        moveAndAssertTime(Position.NORTH_EAST, new DeepWaterTile());
        moveAndAssertTime(Position.SOUTH_EAST, new HillSquare());
        moveAndAssertTime(Position.SOUTH, new MountainSquare());
        moveAndAssertTime(Position.NORTH_WEST, new CitySquare());
        moveAndAssertTime(Position.SOUTH_WEST, new RoadSquare());
        moveAndAssertTime(Position.NORTH_WEST, new ForestSquare());
        moveAndAssertTime(Position.SOUTH_WEST, new GoldSquare(0));
	}
	
	@Test
	public void testOveriding(){
		String string_map = 
				section(TILES)+
				"wwwwwww\n" +
				"wbtsdhw\n" +
				"w.frcmw\n" +
				"wwwwwww\n" +
				section(COSTS)+
				"t=400\n"+
				"s=1000\n"+
				"d=250";
		
		sfc = new StringFieldCreator(string_map);
		goldField = new GoldField(sfc);
        digger = new Digger("Diggers name", "secretName");
        digger.newGame(goldField);
        
        moveAndAssertTime(Position.EAST, 400);
        moveAndAssertTime(Position.EAST, 1000);
        moveAndAssertTime(Position.EAST, 250);
	}
	
	private void moveAndAssertTime(Fn1<Position,Position> p, int time){
        long startTime = System.currentTimeMillis();
		digger.move(p);
		assertEquals(time*Digger.BASE_MOVEMENT_TIME/100, System.currentTimeMillis()-startTime, 10);
	}
	
	private void moveAndAssertTime(Fn1<Position,Position> p, Square tile){
        long startTime = System.currentTimeMillis();
		digger.move(p);
		assertEquals(tile.getCost()*Digger.BASE_MOVEMENT_TIME/100, System.currentTimeMillis()-startTime, 10);
	}
}
