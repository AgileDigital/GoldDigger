package com.agical.golddigger.model.fieldcreator;

import java.util.HashMap;

import com.agical.golddigger.model.tiles.Square;

/** Uses field definitions defined as Strings to create a field.
 * @author Brett Wandel
 */
public class StringFieldCreator extends FieldCreator {
	public static final String DELIMITER = "!", SEPERATOR = "=";
    private static final int WALLS = 2;
    private final String result;
    private Square[][] squares;
    
	public final static String TILES = "field-tiles",
							   COSTS = "cost-per-type",
							   LINE_OF_SIGHT = "line-of-sight",
							   NO_OF_SIDES   = "number-of-sides";
	
    private static final int DEFAULT_NUMBER_OF_SIDES = 4,
    						 DEFAULT_LINE_OF_SIGHT   = 1;
    
	public StringFieldCreator(String result) {
		this.result = result;
	}

	/**
	 * Takes the string given to the constructor and returns the tiles.
	 */
	public Square[][] createField() {
		if (result.contains(DELIMITER)){
			squares = parseFieldWithSections();
		} else {
			squares = createSquares(result);
		}
		return squares;
	}
    
    /**
     * Returns the line of sight length from the field file
     * If not found, will set it to the default line of sight length
     */
    public int getLineOfSightLength() {
    	String value = getAttribute(LINE_OF_SIGHT);
    	if (value == null) return DEFAULT_LINE_OF_SIGHT;
    	try {
    		return Integer.parseInt(value);
    	} catch (NumberFormatException e){
    		e.printStackTrace();
    		return DEFAULT_LINE_OF_SIGHT;
    	}
    }
    
    /**
     * Returns the number of sides for tile from the field file
     * If not found, will set and return and return the number of sides
     * to the default as defined in this class
     */
    public int getNumberOfSides() {
    	String value = getAttribute(NO_OF_SIDES);
    	if (value == null) return DEFAULT_NUMBER_OF_SIDES;
    	try {
    		return Integer.parseInt(value);
    	} catch (NumberFormatException e){
    		e.printStackTrace();
    		return DEFAULT_NUMBER_OF_SIDES;
    	}
    }

	/**
	 * Parses the field from the string stored at result.
	 * note: the string must contain the TILES section
	 * 
	 * @return The 2d array of squares that make up a field.
	 */
	private Square[][] parseFieldWithSections() {
		if (!result.contains(TILES)) {
			throw new RuntimeException("Tried to parse a map that has a delimiter, but does not contain a \""+DELIMITER+TILES+"\" section.");
		}
		String typesSection;
		String[] tiles = null;
		HashMap<Character,Integer> types = null;
		
		typesSection = getSection(COSTS);
		if (typesSection != null) { types = convertCosts(typesSection);}
		
		tiles = getSection(TILES).split("\n");
		Square[][] field = new Square[tiles.length][tiles[0].length()];
		
		int x=0,y=0;
		for (String row : tiles){
			for (char c : row.toCharArray()){
				field[y][x] = Square.createFromChar(c);
				if (c != 'w' && types != null){
					int cost = -1;
					if (types.containsKey(c)){
						cost = Integer.parseInt(types.get(c)+"");
					}
					if (cost >=0) { field[y][x].setCost(cost); }
				}
				x++;
			}
			x = 0;
			y++;
		}
		return field;
	}
	
	/**
	 * Builds a field of squares from a string
	 * The field must have more than 3 rows.
	 * 
	 * This is method assumes no extra information is included in the string, just the map tiles.
	 * @param field_str The string representation of the field
	 * @return The field of Squares
	 */
	private Square[][] createSquares(String field_str){
		String[] rows = field_str.split("\n");
		if (rows.length < 3) return null;
		Square[][] field = new Square[rows.length][];
		
		int x=0,y=0; //Cartesian coordinates
		for (String row : rows){
			field[y] = new Square[row.length()];
			for (char tile : row.toCharArray()){
				field[y][x] = Square.createFromChar(tile);
				x++;
			}
			x=0;
			y++;
		}
		return field;
	}


	/**
	 *  Parses the types section and returns a hash of the tile types and their costs.
	 * @param section The section to parse for types.
	 * @return The HashMap.
	 */
	private HashMap<Character, Integer> convertCosts(String section) {
		HashMap<Character, Integer> costs = new HashMap<Character,Integer>();
		if (section == null) return costs;
		
		String[] lines = section.split("\n");
		for (String line : lines) {
			if (line.contains(SEPERATOR)){
				int index = line.indexOf(SEPERATOR);
				char key = line.substring(0,index).trim().charAt(0);
				Integer value = Integer.parseInt(line.substring(index+1).trim());
				costs.put(key, value);
			}
		}
		return (costs.isEmpty() ? null: costs);
	}

	private Square[][] getSquares() {
		if (squares == null) createField();
		return squares;
	}

	public int getMaxLatitude() {
		return getSquares().length - WALLS;
	}

	public int getMaxLongitude() {
		return getSquares()[0].length - WALLS;
	}

	/** 
	 * parses the "results" variable to retrieve a particular section.
	 * The start of a new section is denoted with the DELIMITER
	 * @param title The section name, with out the delimiter to look for.
	 * @return The String between the section name and the next delimiter
	 */
	public String getSection(String title){
		if (!result.contains(DELIMITER+title)) {
			return null;
		}
		
		int start = result.indexOf(title)+title.length();
		int end = result.indexOf(DELIMITER, start);

		String value;
		if (end == -1) value = result.substring(start);
		else value = result.substring(start, end);
		
		return value.trim();
	}
	
	/**
	 * Returns an attribute in result.
	 * expected format "!title = ???".
	 * @param title the name of the attribute
	 * @return the value of the attribute
	 */
	public String getAttribute(String title){
		String result = getSection(title);
		if (result == null) return null;
		else return result.substring(result.indexOf(SEPERATOR)+1).trim();
	}
}
