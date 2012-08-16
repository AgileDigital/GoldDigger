package com.agical.golddigger.model.tiles;

public class OtherDiggerSquare extends Square {
	private static int DEFAULT_RADIUS = 100;
	private static int DEFAULT_OCCLUSION_COST = 100;
	
	public OtherDiggerSquare() {
		setRadius(DEFAULT_RADIUS);
		setOcclusionCost(DEFAULT_OCCLUSION_COST);
	}
    public boolean isTreadable() {
        return false;
    }

    public String getStringRepresentation() {
        return "o";
    }
}
