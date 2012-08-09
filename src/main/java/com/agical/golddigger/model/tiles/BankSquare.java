package com.agical.golddigger.model.tiles;

import com.agical.golddigger.model.Digger;

public class BankSquare extends Square {
    
	private String diggerName;
	
	public BankSquare (String diggerName){
	this.diggerName=diggerName;
	}
	
	public BankSquare(){
		
	}
	
    public void dropBy(Digger digger) {
        digger.cashGold();
    }

    public String getStringRepresentation() {
        return "b";
    }
    
    public String getName(){
    	return diggerName;
    }
}
