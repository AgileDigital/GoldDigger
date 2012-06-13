package com.agical.golddigger.model.tiles;

public class DeepWaterTile extends Square {
	private static int DEFAULT_COST = 500;

	public DeepWaterTile() {
		setCost(DEFAULT_COST);
	}

	@Override
	public String getStringRepresentation() {
		return "d";
	}

}
