package com.agical.golddigger.model.tiles;

public class CitySquare extends Square {
	private static int DEFAULT_COST = 200;

	public CitySquare() {
		setCost(DEFAULT_COST);
	}

	@Override
	public String getStringRepresentation() {
		return "c";
	}

}
