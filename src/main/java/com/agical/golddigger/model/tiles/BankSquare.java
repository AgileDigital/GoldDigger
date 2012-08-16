package com.agical.golddigger.model.tiles;

import com.agical.golddigger.model.Digger;

public class BankSquare extends Square {

	private String diggerName = "";
	private boolean digger_assigned = false;
	
	/**
	 * In a multiplayer map diggers can only deposit gold
	 * in their own bank tile. To ensure that a bank tile
	 * belongs to a digger, the name of the digger and digger
	 * name of the bank tile is checked. The name of the bank
	 * tile's digger is specified on the creation of the field.
	 * @param diggerName
	 */
	public BankSquare (String diggerName){
		this.diggerName=diggerName;
	}
	
	public BankSquare(){
		
	}
    
    public void dropBy(Digger digger) {
    	
    	if (digger_assigned) {
	    	if (digger.hasBankTile() && diggerName==digger.getName()) {
	    		digger.cashGold();
	    	} else {
	    		doNothing();
	    	}
    	} else {
    		if (!digger.hasBankTile()) {
    			diggerName = digger.getName();
    			digger_assigned = true;
    			digger.setHasBankTile(true);
    			digger.cashGold();
    		} else {
    			doNothing();
    		}
    	}
    }

    public String getStringRepresentation() {
        return "b";
    }
    
	public String getName(){
    	return diggerName;
    }
}
