package com.agical.golddigger.model.tiles;

public class TeleportSquare extends Square {
	private static int DEFAULT_COST = 100;

	public TeleportSquare() {
		setCost(DEFAULT_COST);
	}

	@Override
	public String getStringRepresentation() {
		return "t";
	}

}
