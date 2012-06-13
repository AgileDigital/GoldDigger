package com.agical.golddigger.model.tiles;

public class HillSquare extends Square {
	private static int DEFAULT_COST = 175;

	public HillSquare() {
		setCost(DEFAULT_COST);
	}

	@Override
	public String getStringRepresentation() {
		return "h";
	}

}
