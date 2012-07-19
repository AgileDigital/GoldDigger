/**
 * 
 */
package com.agical.golddigger.model.fieldcreator;

import com.agical.golddigger.model.tiles.BankSquare;
import com.agical.golddigger.model.tiles.Square;


public class EmptyFieldCreator extends FieldCreator {
    private int maxLatitude; 
    private int maxLongitude;
    private int line_of_sight_length = 1;
    private int number_of_sides = 4;
    private int join_time = 30; //seconds
    private int game_time = 120;//seconds
    private int end_time = 10;  //seconds
    
    public EmptyFieldCreator(int maxLatitude, int maxLongitude) {
        this.maxLatitude = maxLatitude;
        this.maxLongitude = maxLongitude;
    }
    public Square[][] createField() {
        Square[][] fields = new Square[maxLatitude+2][maxLongitude+2];
        for(int lat=0;lat<maxLatitude+2;lat++) {
            for(int lon=0;lon<maxLongitude+2;lon++) {
                if(lon==0 || lat==0 || lat==maxLatitude+1 || lon==maxLongitude+1) {
                    fields[lat][lon] = Square.wall();
                } else {
                    fields[lat][lon] = Square.empty();
                }
            }
        }
        fields[1][1] = new BankSquare();
        return fields;
    }
    public int getMaxLatitude() {
        return maxLatitude;
    }
    public int getMaxLongitude() {
        return maxLongitude;
    }
    
    @Override
    public int getLineOfSightLength() {
        return line_of_sight_length;
    }
    
    @Override
    public int getNumberOfSides() {
        return number_of_sides;
    }
    
    @Override
    public int getJoinTime() {
        return join_time;
    }
    
    @Override
    public int getGameTime() {
        return game_time;
    }
    
    @Override
    public int getEndTime() {
        return end_time;
    }
    
}
