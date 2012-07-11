package com.agical.golddigger.model.tiles;

public class TeleportSquare extends Square {
	private static int DEFAULT_COST = 100;
	private static int DEFAULT_RADIUS = 0;
	private static int DEFAULT_OCCLUSION_COST = 0;

	public TeleportSquare() {
		setCost(DEFAULT_COST);
		setRadius(DEFAULT_RADIUS);
		setOcclusionCost(DEFAULT_OCCLUSION_COST);
	}

	@Override
	public String getStringRepresentation() {
		return "t";
	}

}
