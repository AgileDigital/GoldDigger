package com.agical.golddigger.model.tiles;

public class DeepWaterTile extends Square {
	private static int DEFAULT_COST = 500;
	private static int DEFAULT_RADIUS = 0;
	private static int DEFAULT_OCCLUSION_COST = 0;

	public DeepWaterTile() {
		setCost(DEFAULT_COST);
		setRadius(DEFAULT_RADIUS);
		setOcclusionCost(DEFAULT_OCCLUSION_COST);
	}

	@Override
	public String getStringRepresentation() {
		return "d";
	}

}
