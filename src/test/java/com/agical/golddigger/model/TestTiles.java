package com.agical.golddigger.model;

import static org.junit.Assert.*;

import org.junit.Test;
import com.agical.golddigger.model.tiles.*;

public class TestTiles {
	@Test
	public void TestCreateFromChar() {
		assertTrue(Square.createFromChar('t') instanceof TeleportSquare);
		assertTrue(Square.createFromChar('b') instanceof BankSquare);
		assertTrue(Square.createFromChar('s') instanceof ShallowWaterSquare);
		assertTrue(Square.createFromChar('d') instanceof DeepWaterTile);
		assertTrue(Square.createFromChar('h') instanceof HillSquare);
		assertTrue(Square.createFromChar('m') instanceof MountainSquare);
		assertTrue(Square.createFromChar('f') instanceof ForestSquare);
		assertTrue(Square.createFromChar('c') instanceof CitySquare);
		assertTrue(Square.createFromChar('r') instanceof RoadSquare);
		assertTrue(Square.createFromChar('.') instanceof GoldSquare);
	}
	
	@Test
	public void TestStringRepresentation(){
		assertEquals("t", new TeleportSquare().getStringRepresentation());
		assertEquals("b", new BankSquare().getStringRepresentation());
		assertEquals("s", new ShallowWaterSquare().getStringRepresentation());
		assertEquals("d", new DeepWaterTile().getStringRepresentation());
		assertEquals("h", new HillSquare().getStringRepresentation());
		assertEquals("m", new MountainSquare().getStringRepresentation());
		assertEquals("f", new ForestSquare().getStringRepresentation());
		assertEquals("c", new CitySquare().getStringRepresentation());
		assertEquals("r", new RoadSquare().getStringRepresentation());
	}
}
