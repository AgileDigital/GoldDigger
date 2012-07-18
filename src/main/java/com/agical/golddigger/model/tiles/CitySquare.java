package com.agical.golddigger.model.tiles;

public class CitySquare extends Square {
	private static int DEFAULT_COST = 200;
	private static int DEFAULT_RADIUS = 80;
	private static int DEFAULT_OCCLUSION_COST = 100;

	public CitySquare() {
		setCost(DEFAULT_COST);
		setRadius(DEFAULT_RADIUS);
		setOcclusionCost(DEFAULT_OCCLUSION_COST);
	}

	@Override
	public String getStringRepresentation() {
		return "c";
	}

	@Override
	public String getType() {
		return "city";
	}

	@Override
	public String getJSONAttributes() {
		return "";
	}

}
