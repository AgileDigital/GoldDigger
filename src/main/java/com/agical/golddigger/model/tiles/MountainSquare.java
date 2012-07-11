package com.agical.golddigger.model.tiles;

public class MountainSquare extends Square {
	private static int DEFAULT_COST = 500;
	private static int DEFAULT_RADIUS = 100;
	private static int DEFAULT_OCCLUSION_COST = 100;
	
	public MountainSquare(){
		setCost(DEFAULT_COST);
		setRadius(DEFAULT_RADIUS);
		setOcclusionCost(DEFAULT_OCCLUSION_COST);
	}
	
	@Override
	public String getStringRepresentation() {
		return "m";
	}
}
