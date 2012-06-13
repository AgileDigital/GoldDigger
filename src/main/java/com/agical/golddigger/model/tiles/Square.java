package com.agical.golddigger.model.tiles;

import com.agical.golddigger.model.Digger;
import com.agical.jambda.Option;

public abstract class Square {

	/*
	 * This is the move cost, higher numbers mean longer delays when a digger
	 * moves onto this tile. Should be be 0 or higher.
	 */
	private int moveCost = 100;

	public static String getField(Square[][] createField) {
		String result = "";
		for (Square[] squares : createField) {
			for (Square square : squares) {
				result += square.getStringRepresentation();
			}
			result += "\n";
		}
		return result;
	}

	public static Square createFromChar(char squareChar) {
		//Any Gold Tiles
		if (squareChar >= ASCII_NR_START && squareChar <= ASCII_NR_END)
			return new GoldSquare(squareChar - ASCII_NR_START);
		
		switch (squareChar){
		case 't': return new TeleportSquare();
		case 'w': return new WallSquare();
		case 'b': return new BankSquare();
		case 'm': return new MountainSquare();
		case 's': return new ShallowWaterSquare();
		case 'd': return new DeepWaterTile();
		case 'f': return new ForestSquare();
		case 'h': return new HillSquare();
		case 'c': return new CitySquare();
		case 'r': return new RoadSquare();
		default: return new GoldSquare(0);
		}
	}

	public static final Square empty() {
		return new GoldSquare(0);
	}

	public static Square wall() {
		return new WallSquare();
	}

	private static final int ASCII_NR_START = 48;
	private static final int ASCII_NR_END = 59;
	private Option<Square> viewed = Option.none();

	public Square() {
		super();
	}

	protected void doNothing() {
	}

	public abstract String getStringRepresentation();

	public final String toString() {
		return getStringRepresentation();
	}

	public void grabBy(Digger digger) {
		doNothing();
	}

	public void dropBy(Digger digger) {
		doNothing();
	}

	public boolean isTreadable() {
		return true;
	}

	public Option<Square> hasBeenViewed() {
		return viewed;
	}

	public void viewed() {
		viewed = Option.some(this);
	}

	public boolean isEmpty() {
		return true;
	}

	public void setCost(int newCost) {
		if (newCost >= 0) {
			moveCost = newCost;
		}
	}

	public int getCost() {
		return moveCost;
	}
}
