package com.agical.golddigger.model.tiles;

import com.agical.golddigger.model.Digger;

public class BankSquare extends Square {
    
    public void dropBy(Digger digger) {
        digger.cashGold();
    }

    public String getStringRepresentation() {
        return "b";
    }
}
