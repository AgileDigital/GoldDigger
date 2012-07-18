package com.agical.golddigger.model.tiles;

import com.agical.golddigger.model.Digger;

public class GoldSquare extends Square {

    private int nrOfGoldPieces;
    private static final int SQUARE_CAPACITY = 9;

    public GoldSquare(int nrOfGoldPieces) {
        this.nrOfGoldPieces = nrOfGoldPieces;
    }
    public void grabBy(Digger digger) {
        nrOfGoldPieces = digger.addPendingGold(nrOfGoldPieces);
    }
    public void dropBy(Digger digger) {
        int capacity = SQUARE_CAPACITY-nrOfGoldPieces;
        if(capacity>digger.getCarriedGold()) {
            nrOfGoldPieces += digger.getCarriedGold();
            digger.addPendingGold(-1*digger.getCarriedGold());
        } else {
            nrOfGoldPieces += capacity;
            digger.addPendingGold(-1*(capacity));
        }
    }
    
    @Override
    public String getStringRepresentation() {
        return nrOfGoldPieces==0?".":nrOfGoldPieces+"";
    }
    @Override
    public boolean isEmpty() {
        return nrOfGoldPieces==0;
    }

	@Override
	public String getType() {
		if(nrOfGoldPieces > 0)
		{
			return "gold";
		}
		else
		{
			return "plain";
		}
	}

	@Override
	public String getJSONAttributes() {
		if(nrOfGoldPieces > 0)
		{
			return ",\"goldleft\":" +  nrOfGoldPieces + "";
		}
		else
		{
			return "";
		}
	}

}
