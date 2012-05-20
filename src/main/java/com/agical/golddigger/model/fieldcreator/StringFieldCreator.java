package com.agical.golddigger.model.fieldcreator;

import com.agical.golddigger.model.Square;

public class StringFieldCreator extends FieldCreator {
    
    private static final int WALLS = 2;
    private final String result;
    private Square[][] squares;
    private String[] result_sections;
    
    // constants for defining default values
    private final int default_no_of_sides = 4;
    private final int default_line_of_sight_length = 1;
    
    public StringFieldCreator(String result) {
    	String default_settings = "";
        this.result = result;
        
        // if the field file or the field string does not contain
        // attributes, then add default attributes
        if (!result.contains("!")) {
        	default_settings = "!line_of_sight_length=" + default_line_of_sight_length + "\n!number_of_sides=" + default_no_of_sides + "\n!map=\n";
        	result = default_settings + result;
        }
                
        result_sections = result.split("!");
    }

    public Square[][] createField() {
        return getSquares();
    }
    
    // the field file or field string is divided into sections
    // that contain the map and its attributes
    // this method will return an attribute value (if it exists)
    // when given an attribute name
    private String extractAttributeValue(String attribute_name) {
    	String[] attribute_sections;
    	String attribute_value = "not_found";
    	
    	for (String result_section : result_sections) {
    		if (result_section.indexOf(attribute_name) >= 0) {
    			attribute_sections = result_section.split(attribute_name);
    			attribute_value = attribute_sections[1];
    			return attribute_value;
    		}
    	}
    	
    	return attribute_value;
    }

    private Square[][] getSquares() {
    	String map = extractAttributeValue("map=\n");
    	if(squares!=null) return squares;
        String[] rows = map.split("\n");
    	
        squares = new Square[rows.length][];
        for (int rowCount = 0; rowCount<rows.length; rowCount++) {
            String charRow = rows[rowCount];
            Square[] squareRow = new Square[charRow.length()];
            squares[rowCount] = squareRow;
            for (int i = 0; i < charRow.length(); i++) {
                char squareChar = charRow.charAt(i);
                squareRow[i] = Square.createFromChar(squareChar); 
            }
        }
        return squares;
    }
    
    // return the length of line of sight which determines the radius
    // of the view of the digger in a specific map
    public int getLineOfSightLength() {
    	int line_of_sight_length = default_line_of_sight_length;
    	String raw_line_of_sight_length = extractAttributeValue("line_of_sight_length=");
    	raw_line_of_sight_length = raw_line_of_sight_length.replace("\n", ""); 
    	    	   	
    	if (isInteger(raw_line_of_sight_length)) {
    		line_of_sight_length = Integer.parseInt(raw_line_of_sight_length);
    		if (line_of_sight_length < 1) {
    			line_of_sight_length = default_line_of_sight_length;
    		}
    	}
    	        	    	
    	return line_of_sight_length;
    }
    
    // this determines the shape of tiles based on the number of sides defined in the
    // filed file for a specific map
    public int getNumberOfSides() {
    	int number_of_sides = default_no_of_sides; //default shape of tiles is square
    	String raw_number_of_sides = extractAttributeValue("number_of_sides=");
    	raw_number_of_sides = raw_number_of_sides.replace("\n", ""); 
    	    	   	
    	if (isInteger(raw_number_of_sides)) {
    		number_of_sides = Integer.parseInt(raw_number_of_sides);
    		if (number_of_sides < 3) { // a tile must have at least 3 sides
    			number_of_sides = default_no_of_sides; // however, if it is given a number less than 3 in the field file, then it should go back to default (square)
    		}
    	}
    	        	    	
    	return number_of_sides;
    }
    
    public int getMaxLatitude() {
        return getSquares().length-WALLS;
    }
    
    public int getMaxLongitude() {
        return getSquares()[0].length-WALLS;
    }
        
    public static boolean isInteger(String input){
		try{
			Integer.parseInt(input);
			return true;
		} 
		catch(Exception e){
			return false;
		}
    }
}
