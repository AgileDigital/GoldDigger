package com.agical.golddigger.model.tiles;

public class ForestSquare extends Square {
	private static int DEFAULT_COST = 300;

	public ForestSquare() {
		setCost(DEFAULT_COST);
	}

	@Override
	public String getStringRepresentation() {
		return "f";
	}

}
