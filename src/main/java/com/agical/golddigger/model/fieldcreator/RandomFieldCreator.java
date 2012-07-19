package com.agical.golddigger.model.fieldcreator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.agical.golddigger.model.tiles.BankSquare;
import com.agical.golddigger.model.tiles.GoldSquare;
import com.agical.golddigger.model.tiles.Square;
import com.agical.golddigger.model.tiles.WallSquare;
import com.agical.jambda.Functions.Fn4;


public class RandomFieldCreator extends FieldCreator {
    
    private final int maxLatitude;
    private final int maxLongitude;
    private int startLatitude = 1;
    private int startLongitude = 1;
    private int line_of_sight_length = 1;
    private int number_of_sides = 4;
    private int join_time = 30; // seconds
    private int game_time = 120;// seconds
    private int end_time = 10;  // seconds
    private final int nrOfGoldItems;
    private final int nrOfWalls;
    private static final Fn4<Integer, Integer, Integer, Integer, List<Square>> create_available = new Fn4<Integer, Integer, Integer, Integer, List<Square>>() {
        @Override
        public List<Square> apply(Integer nrOfGoldItems2, Integer nrOfWalls2, Integer maxLatitude2, Integer maxLongitude2) {
            ArrayList<Square> available = new ArrayList<Square>();
            int oneForTheBank = 1;
            int goldLeft = nrOfGoldItems2;
            int nrOfGoldSquares = 0;
            while(goldLeft>0) {
                int gold = (int) Math.min((Math.random()*9)+1, goldLeft);
                goldLeft = goldLeft-gold;
                nrOfGoldSquares++;
                GoldSquare newGoldSquare = new GoldSquare(gold);
                newGoldSquare.setCost((int)(Math.random()*10));
                available.add(newGoldSquare);
            }
            for(int i = 0; i < (maxLatitude2*maxLongitude2)-nrOfGoldSquares-nrOfWalls2-oneForTheBank; i++) {
            	Square emptySquare = Square.empty();
            	emptySquare.setCost((int)(Math.random()*10));
                available.add(emptySquare);
            }
            for(int i = 0; i < nrOfWalls2; i++) {
                available.add(new WallSquare());
            }
            Collections.shuffle(available);
            return available;
        }
    };

    public RandomFieldCreator(int maxLatitude, int maxLongitude, int nrOfGoldItems) {
        this(maxLatitude, maxLongitude, nrOfGoldItems, 0);
    }

    public RandomFieldCreator(int maxLatitude, int maxLongitude, int nrOfGoldItems, int nrOfWalls) {
        this.maxLatitude = maxLatitude;
        this.maxLongitude = maxLongitude;
        this.nrOfGoldItems = nrOfGoldItems;
        this.nrOfWalls = nrOfWalls;
    }

    @Override
    public Square[][] createField() {
        int tries = 0;
        int limit = 20;
        while(tries<limit){
            Square[][] field = null;
            field=createUnvalidatedField();
            if(isValid(field)) {
                return field;
            }
            tries++;
            System.out.println("Discarded\n" + Square.getField(field));
        }
        throw new RuntimeException("Couldn't create field");
    }

    private Square[][] createUnvalidatedField() {
        int maxLatitude2 = maxLatitude;
        int maxLongitude2 = maxLongitude;
        int startLatitude2 = startLatitude;
        int startLongitude2 = startLongitude;
        List<Square> available = getAvailable();
        
        Square[][] field = new Square[maxLatitude2+2][maxLongitude2+2];
        for(int lat=0;lat<maxLatitude2+2;lat++) {
            for(int lon=0;lon<maxLongitude2+2;lon++) {
                if(lat==0||lon==0||lat==maxLatitude2+1||lon==maxLongitude2+1) {
                    field[lat][lon] = Square.wall();
                } else {
                    if(lat==startLatitude2&&lon==startLongitude2) {
                        field[lat][lon] = new BankSquare();
                    } else {
                        field[lat][lon] = available.remove(0);
                    }
                }
            }
        }
        return field;
    }
    
    @Override
    public int getMaxLatitude() {
        return maxLatitude;
    }
    
    @Override
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
    
    private List<Square> getAvailable() {
        int nrOfGoldItems2 = nrOfGoldItems;
        int nrOfWalls2 = nrOfWalls;
        int maxLatitude2 = maxLatitude;
        int maxLongitude2 = maxLongitude;
        return create_available.apply(nrOfGoldItems2, nrOfWalls2, maxLatitude2, maxLongitude2);
    }
    
}
