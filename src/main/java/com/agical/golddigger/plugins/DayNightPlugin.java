package com.agical.golddigger.plugins;

import com.agical.golddigger.model.GoldField;
import com.agical.golddigger.plugins.api.FieldUpdatePlugin;

public class DayNightPlugin implements FieldUpdatePlugin {

	public static final String NAME = "DayNightPlugin";
	public static final String CYCLE_TIME = "cycle-time";
	public static final String LOS_SCALE = "los-scale";
	int cycleTime = -1;
	int turnCount = 0;
	int scale=100;

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void init() {
		
	}

	@Override
	public GoldField update(GoldField field) {
		boolean day = isDay();
		turnCount += 1;
		if (day != isDay()){
			double newLOS= field.getLOS();
			if (isDay()){
				newLOS = (100/scale)*newLOS;
			} else {
				newLOS = (newLOS*scale)/100;
			}
			field.setLOS((int) Math.round(newLOS));
		}
		return field;
	}

	public boolean isDay() {
		if (cycleTime == -1) return true;
		int x = turnCount/cycleTime;
		return (x % 2) == 0;
	}

	public int getTurnCount() {
		return turnCount;
	}
	
	public void setCycleTime(int i){
		cycleTime = i;
	}

	public int getCycleTime() {
		return cycleTime;
	}

	public void setLOSScale(int i) {
		this.scale = i;
	}

}
