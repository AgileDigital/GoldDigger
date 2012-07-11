package com.agical.golddigger.model.tiles;

public class HillSquare extends Square {
	private static int DEFAULT_COST = 175;
	private static int DEFAULT_RADIUS = 50;
	private static int DEFAULT_OCCLUSION_COST = 50;

	public HillSquare() {
		setCost(DEFAULT_COST);
		setRadius(DEFAULT_RADIUS);
		setOcclusionCost(DEFAULT_OCCLUSION_COST);
	}

	@Override
	public String getStringRepresentation() {
		return "h";
	}

}
