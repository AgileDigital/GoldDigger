package com.agical.golddigger.model.tiles;

public class ShallowWaterSquare extends Square {
	private static int DEFAULT_COST = 150;

	public ShallowWaterSquare() {
		setCost(DEFAULT_COST);
	}

	@Override
	public String getStringRepresentation() {
		return "s";
	}
}
