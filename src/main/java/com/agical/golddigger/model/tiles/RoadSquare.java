package com.agical.golddigger.model.tiles;

public class RoadSquare extends Square {
	private static int DEFAULT_COST = 25;
	private static int DEFAULT_RADIUS = 0;
	private static int DEFAULT_OCCLUSION_COST = 0;

	public RoadSquare() {
		setCost(DEFAULT_COST);
		setRadius(DEFAULT_RADIUS);
		setOcclusionCost(DEFAULT_OCCLUSION_COST);
	}

	@Override
	public String getStringRepresentation() {
		return "r";
	}

	@Override
	public String getType() {
		return "road";
	}

	@Override
	public String getJSONAttributes() {
		return "";
	}

}
