package com.agical.golddigger.model.tiles;

public class MountainSquare extends Square {
	private static int DEFAULT_COST = 500;
	
	public MountainSquare(){
		setCost(DEFAULT_COST);
	}
	
	@Override
	public String getStringRepresentation() {
		return "m";
	}
}
