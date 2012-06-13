package com.agical.golddigger.model.tiles;

public class RoadSquare extends Square {
	private static int DEFAULT_COST = 25;

	public RoadSquare() {
		setCost(DEFAULT_COST);
	}

	@Override
	public String getStringRepresentation() {
		return "r";
	}

}
