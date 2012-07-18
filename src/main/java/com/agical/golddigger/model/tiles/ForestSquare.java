package com.agical.golddigger.model.tiles;

public class ForestSquare extends Square {
	private static int DEFAULT_COST = 300;
	private static int DEFAULT_RADIUS = 70;
	private static int DEFAULT_OCCLUSION_COST = 90;

	public ForestSquare() {
		setCost(DEFAULT_COST);
		setRadius(DEFAULT_RADIUS);
		setOcclusionCost(DEFAULT_OCCLUSION_COST);
	}

	@Override
	public String getStringRepresentation() {
		return "f";
	}

	@Override
	public String getType() {
		return "forest";
	}

	@Override
	public String getJSONAttributes() {
		return "";
	}


}
