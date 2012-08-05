package com.agical.golddigger.model;


import com.agical.golddigger.PluginService;
import com.agical.golddigger.model.event.GolddiggerNotifier;
import com.agical.golddigger.model.fieldcreator.EmptyFieldCreator;
import com.agical.golddigger.model.fieldcreator.FieldCreator;
import com.agical.golddigger.model.fieldcreator.StringFieldCreator;
import com.agical.golddigger.model.tiles.Square;
import com.agical.jambda.Option;
import com.agical.jambda.Functions.Fn1;

public class GoldField {
	private PluginService pluginService;
	
    private Square[][] squares;

    private GolddiggerNotifier golddiggerNotifier;

    private int maxLatitude, maxLongitude;
    private int line_of_sight_length;
    private int number_of_sides;
    private final double unitR = 1.0;
	private final double unitH = Math.sqrt(3.0)/2.0;
	private final double ROUNDING_MARGIN = 0.0000000001;
	private boolean occludeTiles = true;
	private boolean centreDigger = true;
	final static private String wrapper_tile_symbol = "-";
    
    public void setGolddiggerNotifier(GolddiggerNotifier golddiggerNotifier) {
        this.golddiggerNotifier = golddiggerNotifier;
    }

    public GoldField(int maxLatitude, int maxLongitude) {
        this(new EmptyFieldCreator(maxLatitude, maxLongitude));
    }
    public GoldField(int maxLatitude, int maxLongitude,int numberOfSides) {
        this(new EmptyFieldCreator(maxLatitude, maxLongitude));
        this.number_of_sides = numberOfSides;
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
        pluginService = fieldCreator.getPluginService();
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
    	
    	// a string of unknown tile symbols to be added to centre the digger
    	String wrapper = "";
    	int wrapper_length = (line_of_sight_length*2)+1; // length of the wrapper line
    	
    	// an array with a the same width and height of the squares
    	// array that indicates which tiles should be visible
    	
		int arraySizeLength = squares.length;
    	int arraySizeWidth = squares[0].length;
		String[][] visibleTiles;
		String[][] unoccludedTiles;
		
    	visibleTiles = new String[arraySizeLength][arraySizeWidth];
		unoccludedTiles = new String[arraySizeLength][arraySizeWidth];
		
    	
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

		if(line_of_sight_length > 1 && occludeTiles)
		{
			for(int x = 0; x < visibleTiles.length; x++)
			{
				for(int y = 0; y < visibleTiles[0].length; y++)
				{
					if(visibleTiles[x][y] == "Checked" || visibleTiles[x][y] == "True")
					{
						unoccludedTiles[x][y] = squares[x][y].getStringRepresentation();
					}
				}
			}
    		unoccludedTiles = checkOcclusion(digger, unoccludedTiles);
    		
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
							if(unoccludedTiles[lat][longt] != "?")
							{
								Square square = squares[lat][longt];
				    			square.viewed();
				    			view_line += square;
							}
							else if(unoccludedTiles[lat][longt] == "?")
							{
								
								view_line += "?";
							}
			    			new_line = "\n";
			    			add_view_line = true;
			    			
						} else {
							view_line += "?";
						}
					} else {
						if(centreDigger){
							
							if ((digger_long + deltaLong) < 0) { 
							
								view_line = wrapper_tile_symbol + view_line;
								
							} else if ((digger_long + deltaLong) >= squares[0].length) {
								
								view_line = view_line + wrapper_tile_symbol;
								
							}
						}
					}
				}
			}
			wrapper = "";
			if(centreDigger){
				if ((digger_lat + deltaLat) < 0) {
					
					for (int m = 0; m < wrapper_length; m++) wrapper += wrapper_tile_symbol;
					view = wrapper + "\n" + view;
									
				} else if ((digger_lat + deltaLat) >= squares.length) {
					for (int m = 0; m < wrapper_length; m++) wrapper += wrapper_tile_symbol;
					view = view + wrapper + "\n";
					
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
		return squares.clone();
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
    	

    public int getNumberOfSides(){
    	return number_of_sides;
    }
    
    public int getLOS(){
    	return line_of_sight_length;
    }
    
    public void setLOS(int los){
    	line_of_sight_length = los;
    }

	public PluginService getPluginService() {
		return pluginService;
	}

	public void setPluginService(PluginService pluginService) {
		this.pluginService = pluginService;
	}
    
    
  //Occlusion Functions
  	private  String[][] checkOcclusion(Digger digger, String[][] unoccludedTiles)
  	{
  		int listLength = unoccludedTiles.length*unoccludedTiles[0].length;
  		double[] equations;
  		double diggerY = digger.getPosition().getLatitude();
  		double diggerX = digger.getPosition().getLongitude();
  		double blockX;
  		double blockY;
  		double lineOfSightLeft;
  		String[] wallsList = new String[listLength];
  		String[] tilesList = new String[listLength];
  		int[] radii = new int[listLength];
  		int[] occlusionPenalties = new int[listLength];
  		
  		
  		for(int i = 0; i < unoccludedTiles.length; i++)
  		{
  			for(int j = 0; j < unoccludedTiles[i].length; j++)
  			{
  				int index = i*unoccludedTiles[i].length + j;
  				
  				if(unoccludedTiles[i][j] != null)
  				{
  					radii[index] = squares[i][j].getRadius();
  					occlusionPenalties[index] = squares[i][j].getOcclusionCost();
  					if(radii[index] != 0)
  					{
  						wallsList[index]=unoccludedTiles[i][j];

  					}
  						tilesList[index]=unoccludedTiles[i][j];
  				}
  			}
  		}
  		
  		
  			for(int index = 0; index < wallsList.length; index++)
  			{
  				int[] innerOcclusionPenalties = new int[listLength];
  				
  				String block = wallsList[index];
  				if(block != null)
  				{	
  					
  					blockX = index%unoccludedTiles[0].length;
  					blockY = index/unoccludedTiles[0].length;
  					lineOfSightLeft = getLineOfSightLeft(blockX,blockY,diggerX,diggerY,occlusionPenalties[index]);
  					
  					//create lines between digger and the corners of the blocking tile depending on relative position
  					if(number_of_sides == 4)
  					{
  						equations = getEquationsSquare(diggerX, diggerY, blockX, blockY, radii[index]);
  					}
  					else
  					{
  						equations = getEquationsHex(diggerX, diggerY, blockX, blockY, radii[index]);
  			
  					}
  					innerOcclusionPenalties = getInnerOcclusionPenalties(digger,occlusionPenalties, wallsList,equations,index,lineOfSightLeft,unoccludedTiles,radii);
  					
	  				for(int i=0; i < tilesList.length; i++)
	  				{
	  					
	  					if(tilesList[i] != "?")
	  					{
	  						double squareX = i%unoccludedTiles[0].length;
	  						double squareY = i/unoccludedTiles[0].length;
	  	
	  						if(checkOcclusionTile(diggerX, diggerY, blockX, blockY, squareX, squareY, equations))
	  						{
	  							
	  							//Checks to see if the occludded tile is within the line of Sight, after Occlusion Penalty is
	  							//Calculated.
	  							if(lineOfSightLeft >= getLine(blockX,squareX,blockY,squareY)){
	  								//loops over the Walls list to  calculate whether or not the tile in question is being occluded by
	  								//an inner wall
	  								for(int occlusionIndex = 0; occlusionIndex < occlusionPenalties.length; occlusionIndex++){
	  									if(occlusionPenalties[occlusionIndex] != innerOcclusionPenalties[occlusionIndex]){
	  										double innerOcclusionPenalty = innerOcclusionPenalties[occlusionIndex];
	  										innerOcclusionPenalty /= 100;
	  										double innerBlockX = occlusionIndex%unoccludedTiles[0].length;
	  										double innerBlockY = occlusionIndex/unoccludedTiles[0].length;
	  				  						double[] innerEquations = getEquations(diggerX, diggerY, innerBlockX, innerBlockY, radii[occlusionIndex]);
	  				  						
	  										if(checkOcclusionTile(diggerX, diggerY, innerBlockX, innerBlockY, squareX, squareY, innerEquations)){
	  											double innerLineOfSightLeft = lineOfSightLeft;
	  											innerLineOfSightLeft -= Math.sqrt( Math.abs(innerBlockX - blockX)*Math.abs(innerBlockX - blockX) + 
	  																			   Math.abs(innerBlockY - blockY)*Math.abs(innerBlockY - blockY)	);
	  											innerLineOfSightLeft = innerLineOfSightLeft - (innerLineOfSightLeft * innerOcclusionPenalty);
	  											innerLineOfSightLeft = Math.round(innerLineOfSightLeft);	  											
	  						
	  											if(innerLineOfSightLeft < getLine(innerBlockX,squareX,innerBlockY,squareY)){
	  												tilesList[i] = "?";
	  			  								}
	  										}
	 									}
	  										
	  									
	  								}
	  							}
	  							else{
		  								tilesList[i] = "?";
		  						}	  								
	  						}
	  					}
	  				}	
  				}
  			}
  		
  		//remove occluded tiles from the array
  		for(int i = 0; i < tilesList.length; i++)
  		{
  			if(tilesList[i] == "?")
  			{
  				int arrayX = i%unoccludedTiles[0].length;
  				int arrayY = i/unoccludedTiles[0].length;
  				
  				unoccludedTiles[arrayY][arrayX] = "?";
  			}
  		}
  		return unoccludedTiles;
  		
  	
  	}
  	
  

	//Checks the walls list to calculate the accumulating occlusion percentage
	//on walls within the current wall's occlusion. 
	private int[] getInnerOcclusionPenalties(Digger digger,
		int[] occlusionPenalties, String[] wallsList, double[] equations,
		int index, double lineOfSightLeft,String[][] unoccludedTiles, int[] radii) 
	{
		
		int[] innerOcclusionPenalties = occlusionPenalties.clone();
		double diggerY = digger.getPosition().getLatitude();
  		double diggerX = digger.getPosition().getLongitude();
  		double blockX = index%unoccludedTiles[0].length ;
  		double blockY = index/unoccludedTiles[0].length;
  		double innerLineOfSightLeft = lineOfSightLeft;
  		double[] innerEquations = equations;
		
		for(int innerIndex = 0; innerIndex < wallsList.length; innerIndex++){
				if(wallsList[innerIndex] != null){
					double innerBlockX = innerIndex%unoccludedTiles[0].length;
					double innerBlockY = innerIndex/unoccludedTiles[0].length;
					
					if(checkOcclusionTile(diggerX, diggerY, blockX, blockY, innerBlockX, innerBlockY, equations)){
						if(innerIndex != index){
							if(innerLineOfSightLeft > getLine(blockX,innerBlockX,blockY,innerBlockY)	){
								innerOcclusionPenalties[innerIndex] += innerOcclusionPenalties[index];
								for(int i = 0; i < wallsList.length; i++){
									if(wallsList[i] != null){
										double newBlockX = i%unoccludedTiles[0].length;
										double newBlockY = i/unoccludedTiles[0].length;
										innerEquations = getEquations(diggerX, diggerY, newBlockX, newBlockY, radii[i]);
										
										if(checkOcclusionTile(diggerX, diggerY, newBlockX, newBlockY, innerBlockX, innerBlockY, innerEquations)){
											if(innerIndex != i && i != index){
												innerOcclusionPenalties[innerIndex] += squares[(int)newBlockY][(int)newBlockX].getOcclusionCost();
												
											}
										}
									}
									
									
								}
								
							}
						}
						
						
					}
				}
				
			}
		return innerOcclusionPenalties;
	}
	
	private double getLine(double x1, double x2, double y1,	double y2) {
		if(number_of_sides == 6){
			double half = unitH;
			if(x1 % 2 == 1) y1+=half;
			if(x2 % 2 == 1) y2+=half;
		}
		return Math.round(Math.sqrt(Math.abs(x1 - x2)*Math.abs(x1 - x2) + Math.abs(y1 - y2) * Math.abs(y1 - y2)));
	}

	private double getLineOfSightLeft(double blockX, double blockY,double diggerX, double diggerY, int occlusionPenalty) {

		double lineOfSightLeft = line_of_sight_length;
		lineOfSightLeft -= Math.sqrt(Math.abs(blockX - diggerX) * Math.abs(blockX - diggerX) + 
										   Math.abs(blockY - diggerY) * Math.abs(blockY - diggerY));
		double occlusionPercentage = (double)occlusionPenalty;
		occlusionPercentage /= 100;
		lineOfSightLeft = lineOfSightLeft - lineOfSightLeft* occlusionPercentage;
		return Math.round(lineOfSightLeft);

	}

	private double[] getEquations(double diggerX, double diggerY,
			double newBlockX, double newBlockY, int radius) {
		if(number_of_sides == 4)
			{
				return getEquationsSquare(diggerX, diggerY, newBlockX, newBlockY, radius);
			}
			else
			{
				return getEquationsHex(diggerX, diggerY, newBlockX, newBlockY, radius);
	
			}
	}

	private double[] getEquationsSquare(double diggerX, double diggerY,	double blockX, double blockY, int r) {
  		double radius = r/200.0;
  		if (blockX == diggerX)							//vertical
  		{
  			if(blockY > diggerY)							
  			{
  				return getLineEquations(diggerX, diggerY, blockX - radius, blockY - radius, blockX + radius, blockY - radius);
  			}
  			else
  			{
  				return getLineEquations(diggerX, diggerY, blockX - radius, blockY + radius, blockX + radius, blockY + radius);
  			}
  		}
  		else if((blockY - diggerY)/(blockX - diggerX) > 0)		//1st or 3rd quadrant
  		{	
  			
  			return getLineEquations(diggerX, diggerY, blockX - radius, blockY + radius, blockX + radius, blockY - radius);
  		}
  		else if((blockY - diggerY)/(blockX - diggerX) < 0)	//2nd or 4th quadrant
  		{
  			return getLineEquations(diggerX, diggerY, blockX - radius, blockY - radius, blockX + radius, blockY + radius);
  		}
  		
  		else												//horizontal
  		{
  			if(blockX > diggerX)							
  			{
  				return getLineEquations(diggerX, diggerY, blockX - radius, blockY + radius, blockX - radius, blockY - radius);
  			}
  			else
  			{
  				return getLineEquations(diggerX, diggerY, blockX + radius, blockY + radius, blockX + radius, blockY - radius);
  			}
  		}
  	}
  	
  	private double[] getEquationsHex(double diggerX, double diggerY, double blockX, double blockY, int r) {
  		
  		double radius = r/100.0;
  		double height = Math.sqrt(3.0)/2.0 * radius;
  		//Position tiles in hex layout with radius 1.0
  		double dX = (3.0/2.0) * diggerX * unitR;
  		double dY = 2 * unitH * diggerY + (diggerX%2) * unitH;
  		

  		double bX = (3.0/2.0) * blockX * unitR;
  		double bY = 2 * unitH * blockY + (blockX % 2) * unitH;
  		
  		//determines which corners block the most based on the angle between digger and wall
  		double angle;
  		if (bX == dX)							
  		{
  				return getLineEquations(dX, dY, bX - radius, bY, bX + radius, bY);
  		}
  		else
  		{
  			
  			angle = Math.atan((bY-dY)/(bX-dX));
  		}
  		
  		if(angle <= ROUNDING_MARGIN && angle >= -1*ROUNDING_MARGIN)						
  		{
  			if(bX > dX)
  			{
  				return getLineEquations(dX, dY, bX - radius/2.0, bY - height, bX - radius/2.0, bY + height);
  			}
  			else
  			{
  				return getLineEquations(dX, dY, bX + radius/2.0, bY - height, bX + radius/2.0, bY + height);
  			}
  		}
  		else if(isBetween(angle, Math.PI/3.0, Math.PI/6.0, true))			
  		{
  			if(bX > dX)
  			{
  				return getLineEquations(dX, dY, bX - radius, bY, bX + radius/2.0, bY - height);
  			}
  			else
  			{
  				return getLineEquations(dX, dY, bX - radius/2.0, bY + height, bX + radius, bY);
  			}
  		}
  		else if(isBetween(angle, -1*Math.PI/3.0, -1*Math.PI/6.0, true))
  		{
  			if(bX > dX)
  			{
  				return getLineEquations(dX, dY, bX - radius, bY, bX + radius/2.0, bY + height);
  			}
  			else
  			{
  				return getLineEquations(dX, dY, bX - radius/2.0, bY - height, bX + radius, bY);
  			}
  		}
  		else if(isBetween(angle, Math.PI/3.0, 0.0, false))
  		{
  			return getLineEquations(dX, dY, bX - radius/2.0, bY + height, bX + radius/2.0, bY - height);
  		}
  		else if(isBetween(angle, -1*Math.PI/3.0, 0.0, false))
  		{
  			return getLineEquations(dX, dY, bX - radius/2.0, bY - height, bX + radius/2.0, bY + height);
  		}
  		else
  		{

  			return getLineEquations(dX, dY, bX - radius, bY, bX + radius, bY);
  		}
  	}
  	

  	//Compares the diggers position with a blocking tile and another tile. Returns true if the third tile is blocked by the second. 
  	private  boolean checkOcclusionTile(double diggerX, double diggerY, double blockX, double blockY, double squareX, double squareY, double[] equations)
  	{
  		if(blockX == squareX && blockY == squareY)
  		{
  			return false;
  		}
  		else
  		{		
  			
  			
  			double m1 = equations[0];
  			double b1 = equations[1];
  			double m2 = equations[2];
  			double b2 = equations[3];
  			
  			if(number_of_sides == 4)
  			{
  				if (isBetween(blockY,diggerY,squareY,true) && isBetween(blockX,diggerX,squareX,true))
  				{
  					if (blockX != diggerX)
  					{
  						return (isBetween(squareY,lineEq(m1,squareX,b1),lineEq(m2,squareX,b2), false));
  								
  					}
  					else
  					{
  						return (isBetween(squareX,lineEq(1/m1,squareY,-1*b1/m1),lineEq(1/m2,squareY,-1*b2/m2), false));
  					}
  				}
  				else
  				{
  					return false;
  				}
  			}
  			else
  			{
  				
  				double dX = (3.0/2.0) * diggerX * unitR;
  				double dY = 2 * unitH * diggerY + (diggerX%2)*unitH;
  				double bX = (3.0/2.0) * blockX * unitR;
  				double bY = 2 * unitH * blockY + (blockX % 2) * unitH;
  				double sX = (3.0/2.0) * squareX * unitR;
  				double sY = 2 * unitH * squareY + (squareX % 2) * unitH;
  				

  				if (isBetween(bY,dY,sY,true) && isBetween(bX,dX,sX,true))
  				{
  					if(dX != bX)
  					{

  					return (isBetween(sY,lineEq(m1,sX,b1),lineEq(m2,sX,b2), false));
  					}		
  				
  					else			//since all x values are the same, compare y values instead
  					{
  						

  						return (isBetween(sX,lineEq(1/m1,sY,-1*b1/m1),lineEq(1/m2,sY,-1*b2/m2), false));
  					}
  				}
  				else
  				{
  					return false;
  				
  				}
  			}	
  		}
  	}

  	
  	//returns true if y is between y1 and y2
  	private boolean isBetween(double y, double y1, double y2, boolean equal)
  	{
  		if(equal)
  		{
  			return ((y >= y1 - ROUNDING_MARGIN && y <= y2 + ROUNDING_MARGIN) || (y <= y1 + ROUNDING_MARGIN && y >= y2 - ROUNDING_MARGIN));
  		}
  		else
  		{
  			return ((y > y1 + ROUNDING_MARGIN && y < y2 - ROUNDING_MARGIN) || (y < y1 - ROUNDING_MARGIN && y > y2 + ROUNDING_MARGIN));
  		}
  	}
  	
  	//finds the equation of the lines between the digger and two points 
  	private double[] getLineEquations(double diggerX, double diggerY, double x1, double y1, double x2, double y2)
  	{
  	
  		double m1 = (y1 - diggerY)/(x1 - diggerX);	
  		double b1 = diggerY - diggerX * m1;
  		double m2 = (y2 - diggerY)/(x2 - diggerX);	
  		double b2 = diggerY - diggerX * m2;
  		double[] equations = {m1, b1, m2, b2};
  		
  		return equations;
  	}

  	
  	
  	//returns the y value of an equation y=mx+b
  	private double lineEq(double m, double x, double b)
  	{
  		return m*x + b;
  	}
  	
  	public void toggleDiggercentreingTo(boolean centreing){
  		centreDigger = centreing;
  	}
  	
  	public void toggleOcclusionTo(boolean occluding){
  		occludeTiles = occluding;
  	}
  	
  	
  	public void setSquare(Position position, Square square){
  		squares[position.getLatitude()][position.getLongitude()] = square;
  	}
  	
  	public void setField(Square[][] field){
  		int x = 0;
  		for(Square[] innerArray: field){
  			this.squares[x] = innerArray.clone();
  			x++;
  		}
  	}
  //Testing functions
  	private void prEq(double[] equations) 
  	{
  	// TODO Auto-generated method stub
  		System.out.println("y1 = " + equations[0] + "x + " + equations[1]);
  		System.out.println("y2 = " + equations[2] + "x + " + equations[3]);
  	}
  	private void prPos(double x, double y)
  	{
  		System.out.println("(" + x + ", " + y + ")");
  	}
}
