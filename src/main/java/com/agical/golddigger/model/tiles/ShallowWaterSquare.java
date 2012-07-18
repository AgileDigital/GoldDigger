package com.agical.golddigger.model.tiles;

public class ShallowWaterSquare extends Square {
	private static int DEFAULT_COST = 150;
	private static int DEFAULT_RADIUS = 0;
	private static int DEFAULT_OCCLUSION_COST = 0;

	public ShallowWaterSquare() {
		setCost(DEFAULT_COST);
		setRadius(DEFAULT_RADIUS);
		setOcclusionCost(DEFAULT_OCCLUSION_COST);
	}

	@Override
	public String getStringRepresentation() {
		return "s";
	}

	@Override
	public String getType() {
		return "shallowwater";
	}

	@Override
	public String getJSONAttributes() {
		return "";
	}
}
