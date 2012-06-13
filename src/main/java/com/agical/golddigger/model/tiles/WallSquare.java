package com.agical.golddigger.model.tiles;

public class WallSquare extends Square {

    public boolean isTreadable() {
        return false;
    }

    public String getStringRepresentation() {
        return "w";
    }
}
