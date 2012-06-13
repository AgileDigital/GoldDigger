package com.agical.golddigger.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.net.URL;

import com.agical.golddigger.view.PeekView;

public class GraphicsPeekView implements PeekView {
	private static int numberOfSides = 4;

	private static Image loadImage(String name) {
		URL url = FieldView.class.getResource("/images/"+name); 
		return Toolkit.getDefaultToolkit().getImage(url);
	}

	private static Image hexImage2 = loadImage("hexWall.png");
	
	private static Image GOLD1 = loadImage("gold1.png");
	private static Image GOLD2 = loadImage("gold2.png");
	private static Image GOLD3 = loadImage("gold3.png");
	private static Image GOLD4 = loadImage("gold4.png");
	private static Image GOLD5 = loadImage("gold5.png");
	private static Image GOLD6 = loadImage("gold6.png");
	private static Image GOLD7 = loadImage("gold7.png");
	private static Image GOLD8 = loadImage("gold8.png");
	private static Image GOLD9 = loadImage("gold9.png");
	
	private static Image HEXGOLD1 = loadImage("hexGold1.png");
	private static Image HEXGOLD2 = loadImage("hexGold2.png");
	private static Image HEXGOLD3 = loadImage("hexGold3.png");
	private static Image HEXGOLD4 = loadImage("hexGold4.png");
	private static Image HEXGOLD5 = loadImage("hexGold5.png");
	private static Image HEXGOLD6 = loadImage("hexGold6.png");
	private static Image HEXGOLD7 = loadImage("hexGold7.png");
	private static Image HEXGOLD8 = loadImage("hexGold8.png");
	private static Image HEXGOLD9 = loadImage("hexGold9.png");
	
	private static Image BANK;
	private static Image SHADE;
	
	private static Image[] golds = new Image[]{GOLD1, GOLD2, GOLD3, GOLD4, GOLD5, GOLD6, GOLD7, GOLD8, GOLD9};
	
	private static Image DIGGER = loadImage("digger.png");
	private static Image EMPTY = loadImage("empty.png");
	private static Image W_CENTER = loadImage("center.png");
	private static Image SOLID = loadImage("solid.png");
	private static Image W_NORTH = loadImage("w_north.png");
	private static Image W_NORTHEAST = loadImage("w_northeast.png");
	private static Image W_NORTHEAST_INVERTED = loadImage("w_northeast_i.png");
	private static Image W_EAST = loadImage("w_east.png");
	private static Image W_SOUTHEAST = loadImage("w_southeast.png");
	private static Image W_SOUTHEAST_INVERTED = loadImage("w_southeast_i.png");
	private static Image W_SOUTH = loadImage("w_south.png");
	private static Image W_SOUTHWEST = loadImage("w_southwest.png");
	private static Image W_SOUTHWEST_INVERTED = loadImage("w_southwest_i.png");
	private static Image W_WEST = loadImage("w_west.png");
	private static Image W_NORTHWEST = loadImage("w_northwest.png");
	private static Image W_NORTHWEST_INVERTED = loadImage("w_northwest_i.png");

	private static Image CITY;
	private static Image DEEP_WATER;
	private static Image FOREST;
	private static Image HILL;
	private static Image MOUNTAIN;
	private static Image ROAD;
	private static Image SHALLOW_WATER;
	private static Image TELEPORT;
	

	
	private final Graphics graphics;

	private ImageObserver imageObserver;

	public GraphicsPeekView(Graphics graphics, ImageObserver imageObserver) {
		this.graphics = graphics;
		this.imageObserver = imageObserver;
	}
	
	public static void changeTileSetBasedOnSides(int newNumberOfSides){
		numberOfSides = newNumberOfSides;
		if(numberOfSides == 4){			
		    BANK = loadImage("square_bank.png");
			SHADE = loadImage("shade square.png");
			DIGGER = loadImage("digger.png");
			EMPTY = loadImage("empty.png");
			CITY = loadImage("city.png");
			DEEP_WATER = loadImage("deep_water.png");
			FOREST = loadImage("forest.png");
			HILL = loadImage("hill.png");
			MOUNTAIN = loadImage("mountain.png");
			ROAD = loadImage("road.png");
			SHALLOW_WATER = loadImage("shallow_water.png");
			TELEPORT = loadImage("teleport.png");
			golds = new Image[]{GOLD1, GOLD2, GOLD3, GOLD4, GOLD5, GOLD6, GOLD7, GOLD8, GOLD9};
			  W_CENTER =  loadImage("center.png");
			  SOLID =  loadImage("solid.png");
			  W_NORTH =  loadImage("w_north.png");
			  W_NORTHEAST =  loadImage("w_northeast.png");
			  W_NORTHEAST_INVERTED =  loadImage("w_northeast_i.png");
			  W_EAST =  loadImage("w_east.png");
			  W_SOUTHEAST =  loadImage("w_southeast.png");
			  W_SOUTHEAST_INVERTED =  loadImage("w_southeast_i.png");
			  W_SOUTH =  loadImage("w_south.png");
			  W_SOUTHWEST =  loadImage("w_southwest.png");
			  W_SOUTHWEST_INVERTED =  loadImage("w_southwest_i.png");
			  W_WEST =  loadImage("w_west.png");
			  W_NORTHWEST =  loadImage("w_northwest.png");
			  W_NORTHWEST_INVERTED =  loadImage("w_northwest_i.png");
		}
		else if (numberOfSides == 6){
			BANK = loadImage("hexbank.png");
			SHADE = loadImage("shade.png");
			DIGGER = loadImage("digger.png");
		    EMPTY = loadImage("hexEmpty.png");
		    CITY = loadImage("hex_city.png");
			DEEP_WATER = loadImage("hex_deep_water.png");
			FOREST = loadImage("hex_forest.png");
			HILL = loadImage("hex_hill.png");
			MOUNTAIN = loadImage("hex_mountain.png");
			ROAD = loadImage("hex_road.png");
			SHALLOW_WATER = loadImage("hex_shallow_water.png");
			TELEPORT = loadImage("hex_teleport.png");
			golds = new Image []{HEXGOLD1, HEXGOLD2, HEXGOLD3, HEXGOLD4, HEXGOLD5, HEXGOLD6, HEXGOLD7, HEXGOLD8, HEXGOLD9};
			  W_CENTER = loadImage("hexWall.png");
			  SOLID = hexImage2;
			  W_NORTH = hexImage2;
			  W_NORTHEAST = hexImage2;
			  W_NORTHEAST_INVERTED = hexImage2;
			  W_EAST = hexImage2;
			  W_SOUTHEAST = hexImage2;
			  W_SOUTHEAST_INVERTED = hexImage2;
			  W_SOUTH = hexImage2;
			  W_SOUTHWEST = hexImage2;
			  W_SOUTHWEST_INVERTED = hexImage2;
			  W_WEST = hexImage2;
			  W_NORTHWEST = hexImage2;
			  W_NORTHWEST_INVERTED = hexImage2;
		}
	}

	@Override
	public void drawCenterWall(int x, int y) {
		drawImage(x, y, W_CENTER);
	}

	@Override
	public void drawEmpty(int x, int y) {
		drawImage(x, y, EMPTY);
	}

	@Override
	public void drawSolidWall(int x, int y) {
		drawImage(x, y, SOLID);
	}

	@Override
	public void drawNorthWall(int x, int y) {
		drawImage(x, y, W_NORTH);
	}

	@Override
	public void drawSouthWall(int x, int y) {
		drawImage(x, y, W_SOUTH);
	}

	@Override
	public void drawEastWall(int x, int y) {
		drawImage(x, y, W_EAST);
	}

	@Override
	public void drawWestWall(int x, int y) {
		drawImage(x, y, W_WEST);
	}

	@Override
	public void drawNorthEastWall(int x, int y) {
		drawImage(x, y, W_NORTHEAST);
	}

	@Override
	public void drawSouthEastWall(int x, int y) {
		drawImage(x, y, W_SOUTHEAST);
	}

	@Override
	public void drawSouthWestWall(int x, int y) {
		drawImage(x, y, W_SOUTHWEST);
	}

	@Override
	public void drawNorthWestWall(int x, int y) {
		drawImage(x, y, W_NORTHWEST);
	}

	private boolean drawImage(int x, int y, Image image) {
		if(numberOfSides == 4){
			return graphics.drawImage(image, x*32, y*32, imageObserver);
		} else {
			return graphics.drawImage(image, x, y, imageObserver);
		}
	}

	@Override
	public void drawInvertedNorthEastWall(int x, int y) {
		drawImage(x, y, W_NORTHEAST_INVERTED);
	}

	@Override
	public void drawInvertedNorthWestWall(int x, int y) {
		drawImage(x, y, W_NORTHWEST_INVERTED);
	}

	@Override
	public void drawInvertedSouthEastWall(int x, int y) {
		drawImage(x, y, W_SOUTHEAST_INVERTED);
	}

	@Override
	public void drawInvertedSouthWestWall(int x, int y) {
		drawImage(x, y, W_SOUTHWEST_INVERTED);
	}

	@Override
	public void drawDigger(int x, int y) {
		drawImage(x, y, DIGGER);
	}

	@Override
	public void drawGold(int x, int y, int count) {
		drawImage(x, y, golds[count-1]);
	}

    @Override
    public void drawShadow(int x, int y) {
    	drawImage(x, y, SHADE);
        
    }

    @Override
    public void drawBank(int x, int y) {
    	drawImage(x, y, BANK);
    }
    
    @Override
    public void drawCity(int x, int y) {
    	drawImage(x, y, CITY);
    }
    
    @Override
    public void drawDeepWater(int x, int y) {
    	drawImage(x, y, DEEP_WATER);
    }
    
    @Override
    public void drawHill(int x, int y) {
    	drawImage(x, y, HILL);
    }
    
    @Override
    public void drawMountain(int x, int y) {
    	drawImage(x, y, MOUNTAIN);
    }
    
    @Override
    public void drawRoad(int x, int y) {
    	drawImage(x, y, ROAD);
    }
    
    @Override
    public void drawShallowWater(int x, int y) {
    	drawImage(x, y, SHALLOW_WATER);
    }
    
    @Override
    public void drawTeleport(int x, int y) {
    	drawImage(x, y, TELEPORT);
    }
    
    @Override
    public void drawForest(int x, int y) {
    	drawImage(x, y, FOREST);
    }

}
