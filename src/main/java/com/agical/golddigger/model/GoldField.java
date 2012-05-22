package com.agical.golddigger.model;

import com.agical.golddigger.model.event.GolddiggerNotifier;
import com.agical.golddigger.model.fieldcreator.EmptyFieldCreator;
import com.agical.golddigger.model.fieldcreator.FieldCreator;
import com.agical.golddigger.model.fieldcreator.StringFieldCreator;




public class GoldField {
    private Square[][] squares;

    private GolddiggerNotifier golddiggerNotifier;

    private int maxLatitude, maxLongitude;
    
    private int line_of_sight_length;
    
    private int number_of_sides;
    
    public void setGolddiggerNotifier(GolddiggerNotifier golddiggerNotifier) {
        this.golddiggerNotifier = golddiggerNotifier;
    }

    public GoldField(int maxLatitude, int maxLongitude) {
        this(new EmptyFieldCreator(maxLatitude, maxLongitude));
    }
    @Override
    public String toString() {
        return Square.getField(squares);
    }
    
    public GoldField(FieldCreator fieldCreator) {
        squares = fieldCreator.createField();
        maxLatitude = fieldCreator.getMaxLatitude();
        maxLongitude = fieldCreator.getMaxLongitude();
        line_of_sight_length = fieldCreator.getLineOfSightLength();
        number_of_sides = fieldCreator.getNumberOfSides();
    }

    public int getMaxLatitude() {
        return maxLatitude;
    }

    public int getMaxLongitude() {
        return maxLongitude;
    }

    public String getDiggerView(Digger digger) {
        return constructDiggerView(digger);
    }
    
    
   /**
    * Constructs a view based on the line of sight length and tile shapes
    * The view will be centred around the digger
    * 
    * @param digger
    * @return view as a String that will be sent to the client via HTTP
    */
        
    public String constructDiggerView(Digger digger) {
    	String view = "";
    	String new_line = "";
    	String view_line = "";
    	Boolean add_view_line = false;
    	
    	// an array with a the same width and height of the squares
    	// array that indicates which tiles should be visible
    	
		int arraySizeLength = squares.length;
    	int arraySizeWidth = squares[0].length;
		String[][] visibleTiles;
		
    	visibleTiles = new String[arraySizeLength][arraySizeWidth];
    	
    	// initially all tiles are invisible when view is called
    	for (int x = 0; x < visibleTiles.length; x++) {
			for (int y = 0; y < visibleTiles[0].length; y++) {
				visibleTiles[x][y] = "-";
			}
		}
    	
    	Position position = digger.getPosition();
    	int digger_lat = position.getLatitude();
    	int digger_long = position.getLongitude();
    	
    	// first tile we want to check for its adjacent tiles
    	// is the tile where the digger is currently located
    	visibleTiles[digger_lat][digger_long] = "Check";
    	
    	
    	
    	
    	
    	int k = 0;
    	
    	// iterate through the visible tiles array the same number of times as the
		// length of line of sight.
    	while (k < line_of_sight_length){
    		
    		// those tiles which were determined to be visible in the last iteration
    		// should now be checked to make their adjacent tiles visible
    		for (int x = 0; x < visibleTiles.length; x++) {
    			for (int y = 0; y < visibleTiles[0].length; y++) {
    				if (visibleTiles[x][y] == "True") {
    					visibleTiles[x][y] = "Check";
    				}
    			}    			
    		}
    		
    		for (int i = 0; i < visibleTiles.length; i++) {
    			for (int j = 0; j < visibleTiles[i].length; j++) {
    				// check for adjacent tiles, hence "Check"
					if (visibleTiles[i][j] == "Check") {
    					
    					position = new Position(i, j);
    					// mark all the adjacent tiles to this tile as visible
    					markAdjacentTiles(position, visibleTiles);
    					
    					// now that adjacent tiles have been marked do not check this tile
    					// again, hence "Checked"	        					
    					visibleTiles[i][j] = "Checked";
    				}
    			}
    		}
    		k++;
    	}

    	// run through the visible tiles array, and construct a view from the corresponding tiles in the squares
    	// array to return to the client
    	for (int deltaLat = (-1*line_of_sight_length); deltaLat <= line_of_sight_length; deltaLat++){
			new_line = "";
			view_line = "";
			add_view_line = false;
			for (int deltaLong = (-1*line_of_sight_length); deltaLong <= line_of_sight_length; deltaLong++){
				// ensure that we are not out of bounds of latitude
				if ((digger_lat + deltaLat) >= 0 && (digger_lat + deltaLat) < squares.length) {
					// ensure that we are not out of bounds of longitude
					if ((digger_long + deltaLong) >= 0 && (digger_long + deltaLong) < squares[0].length) {
						int lat = digger_lat + deltaLat;
						int longt = digger_long + deltaLong;
						
						// only make those tiles visible who have been marked as
						// checked or true - the outermost layer is true, while the inner layers
						// are checked
						if (visibleTiles[lat][longt] == "Checked" || visibleTiles[lat][longt] == "True"){
							Square square = squares[lat][longt];
			    			square.viewed();
			    			view_line += square;
			    			new_line = "\n";
			    			add_view_line = true;
						} else {
							view_line += " ";
						}
					}
				}
			}
			// if this line of view is to be added, add it and format it with a new line character
			if (add_view_line) {
				view += view_line + new_line;
			}
		}
    	return view;
    }
    
    public String getField(Digger digger) {
        String result = "";
        for(int lat=1;lat<=getMaxLatitude();lat++) {
            for(int lon=1;lon<=getMaxLongitude();lon++) {
                Position position = digger.getPosition();
                result += squares[lat][lon];
            }
            result += "\n";
        }
        return result;
    }
    public String fieldAt(int lat, int lon) {
        return squares[lat][lon]+"";
    }
    private Square getSquare(int latitude, int longitude) {
        return squares[latitude][longitude];
    }

    public Square getSquare(Position newPosition) {
        return getSquare(newPosition.getLatitude(), newPosition.getLongitude());
    }

    public boolean isTreadable(Position position) {
        int longitude = position.getLongitude();
        int latitude = position.getLatitude();
        return latitude>=1&&longitude>=1&&latitude<=getMaxLatitude()&&longitude<=getMaxLongitude()&&getSquare(position).isTreadable();
    }
    
    public Square[][] getSquares() {
		return squares;
	}

    public boolean hasGold() {
        for(int lat=1;lat<=getMaxLatitude();lat++) {
            for(int lon=1;lon<=getMaxLongitude();lon++) {
                if(!squares[lat][lon].isEmpty()) return true;
            }
        }
        return false;
    }
    
    /**
     * Finds the immediately adjacent tiles to the tile that is passed to it as position
     * Adjacency differs for different tile shapes and also 
     * whether the tile is in an odd column in the field or an even column
     * 
     * @param position of the tile which we want to find its adjacent tiles
     * @param sightedArray which indicates which tiles should be visible to the digger
     */
    public void markAdjacentTiles(Position position, String[][] sightedArray){
    	int deltaLat;
    	int deltaLong;
    	
    	int lat = position.getLatitude();
    	int lng = position.getLongitude();
    			
    	
    	
    	for (deltaLat = -1; deltaLat <= 1; deltaLat++) {
    		
    		// make sure we are within bounds of latitude
    		if (lat + deltaLat >= 0 && lat + deltaLat < sightedArray.length) {
    			
    			for (deltaLong = -1; deltaLong <= 1; deltaLong++) {
        		        		
        			// make sure we are within bounds of longitude
        			if (lng + deltaLong >= 0 && lng + deltaLong < sightedArray[0].length) {
        				switch (number_of_sides) {
        				case 3: // Triangle Tiles
        					if ((lng % 2) == 0) {
        						// with triangle tiles some "adjacent" tiles are invisible
		        				if ((!(deltaLat == 1 && deltaLong == 1)) && (!(deltaLat == 1 && deltaLong == -1))) {
		        					if (sightedArray[lat + deltaLat][lng + deltaLong] == "-") {
		        						sightedArray[lat + deltaLat][lng + deltaLong] = "True";
		        					}
		        				}
		        			} else if ((lng % 2) == 1) {
		        				if ((!(deltaLat == -1 && deltaLong == -1)) && (!(deltaLat == -1 && deltaLong == 1))) {
		        					if (sightedArray[lat + deltaLat][lng + deltaLong] == "-") {
		        						sightedArray[lat + deltaLat][lng + deltaLong] = "True";
		        					}
		        				}
		        			}
        					break;
        					
        				case 4: // Square tiles	
        					if (sightedArray[lat + deltaLat][lng + deltaLong] == "-") {
		        				sightedArray[lat + deltaLat][lng + deltaLong] = "True";
		        			}
        					break;
        					
        				case 6:// Hexagon Tiles
        					if ((lng % 2) == 1) {
        						// with trinangle tiles some "adjacent" tiles are invisible
			        			if (!(deltaLat == -1 && deltaLong == -1) && !(deltaLat == -1 && deltaLong == 1)) {
			        				if (sightedArray[lat + deltaLat][lng + deltaLong] == "-") {
			        					sightedArray[lat + deltaLat][lng + deltaLong] = "True";
			        				}
			        			}
		        			} else if ((lng % 2) == 0) {
		        				if (!(deltaLat == 1 && deltaLong == 1) && !(deltaLat == 1 && deltaLong == -1)) {
		        					if (sightedArray[lat + deltaLat][lng + deltaLong] == "-") {
		        						sightedArray[lat + deltaLat][lng + deltaLong] = "True";
		        					}
			        			}
		        			}
        					break;		
        				}
        			}        			 
        		}	
        	}
    	}
    }
}
