/**
 * 
 */
package com.agical.golddigger.server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.agical.golddigger.model.Digger;
import com.agical.golddigger.model.Diggers;
import com.agical.golddigger.model.GoldField;
import com.agical.golddigger.model.Position;
import com.agical.golddigger.model.tiles.HillSquare;
import com.agical.golddigger.model.tiles.WallSquare;
import com.agical.jambda.Functions;

public class PathExecutor {
    private final Diggers diggers;
    private final Writer log;
    private List<Digger> multiplayerdiggers = new ArrayList<Digger>();
    private String multiplayerMap = "b.........999\n";
    private GoldField multiplayerGoldField;
    
    
    public PathExecutor(Diggers diggers, Writer log) {
        super();
        this.diggers = diggers;
        this.log = log;
    }
    
    public void executePath(String pathInfo, PrintWriter writer) {
        String[] splitPath = pathInfo.split("/");
        String actor = splitPath[0];
        if (actor.equals("digger")) {
            handleDigger(writer, splitPath);
        } else if (actor.equals("admin")) {
            String secretName = splitPath[1];
            if (!secretName.equals("ccret")) {
                writer.write("bad command\n");
                return;
            }
            String action = splitPath[2];
            if (action.equals("listdiggers")) {
                for (Digger digger : diggers.getDiggers()) {
                    writer.write(digger.getName() + " " + digger.getSecretName() + "\n");
                }
            } else if (action.equals("add")) {
                String newName = splitPath[3];
                String newSecretName = splitPath[4];
                Digger digger = diggers.createDigger(newName, newSecretName);
                diggers.newGame(digger);
            }
        }
    }
    
    private void handleDigger(PrintWriter writer, String[] splitPath) {
        String secretName = splitPath[1];
        String action = splitPath[2];
        Digger digger = diggers.getDigger(secretName);
        int numberOfSides = digger.getGoldField().getNumberOfSides();
        try {
            if (action.equals("view")) {
                writer.write(digger.getView());
            }
            if (action.equals("score")) {
                int goldInTheBank = digger.getGoldInTheBank();
                writer.write(goldInTheBank + "\n");
            }
            if (action.equals("grab")) {
                int carriedBefore = digger.getCarriedGold();
                digger.grab();
                int carriedAfter = digger.getCarriedGold();
                writer.write((carriedAfter - carriedBefore) + "\n");
            }
            if (action.equals("drop")) {
                int carriedBefore = digger.getCarriedGold();
                digger.drop();
                int carriedAfter = digger.getCarriedGold();
                writer.write((carriedBefore - carriedAfter) + "\n");
            }
            if (action.equals("carrying")) {
                writer.write(digger.getCarriedGold() + "\n");
            }
            
            /*Ok so here is the code that puts the diggers onto the "same" map.
             * Right now all it does is if someone goes next, it will put them into the 
             * multiplayer map. Obviously this wont work if they go next again so keep testing down to
             * just the 2nd map. When we do this for real we'll have to configure a map to be multiplayer but 
             * yeah for now just work with it being the second.
             * 
             * Also when more diggers enter the map they just get put into position 3,1 so we're just testing with 2 
             * diggers on the map. When we do this for real we can just base the starting Position off of their
             * position in multiplayerdigger list.
             * 
             * */
            if (action.equals("next")) {
                if(digger.getGoldField().hasGold()) {
                    writer.write("FAILED\n");
                } else {
                    writer.write("OK\n");
                    
                    diggers.newGame(digger);
                    
                    
                    System.out.println(digger.getGoldField().toString());
                    System.out.println(this.multiplayerMap);
                    if(this.multiplayerMap.equals(digger.getGoldField().getField(digger))){ 
                    	if(multiplayerdiggers.isEmpty()){
                    		multiplayerGoldField = new GoldField(digger.getGoldField().getMaxLatitude(),digger.getGoldField().getMaxLongitude());
                        	multiplayerGoldField.setField(digger.getGoldField().getSquares().clone());
                        }
                    	System.out.println("aaaaaaaaaa");
                    	multiplayerdiggers.add(digger);
                    	if(multiplayerdiggers.indexOf(digger) > 0) {
                    		System.out.println("asdasdsad");
                    		digger.setPosition(new Position(1,3));
                    		updateGoldFields(multiplayerdiggers);
                    		
                    	}
                    }
                }
            }
            if (action.equals("move")) {
                String direction = splitPath[3].toLowerCase();
                String ok = "OK\n";
                String failed = "FAILED\n";
                
                if (numberOfSides == 4){
                    if (direction.equals("north")) {
                        writer.write(digger.move(Position.NORTH).map(Functions.<Position, String> constantly(ok),
                            Functions.<String> constantly(failed)));
                    } else if (direction.equals("east")) {
                        writer.write(digger.move(Position.EAST).map(Functions.<Position, String> constantly(ok),
                            Functions.<String> constantly(failed)));
                    } else if (direction.equals("west")) {
                        writer.write(digger.move(Position.WEST).map(Functions.<Position, String> constantly(ok),
                            Functions.<String> constantly(failed)));
                    } else if (direction.equals("south")) {
                        writer.write(digger.move(Position.SOUTH).map(Functions.<Position, String> constantly(ok),
                            Functions.<String> constantly(failed)));
                    } else {
                	    writer.write("Cannot Move in that direction");
                    }
                } else if(numberOfSides == 6){
                
                    if (direction.equals("north_east")) {
                        writer.write(digger.move(Position.NORTH_EAST).map(Functions.<Position, String> constantly(ok),
                            Functions.<String> constantly(failed)));
                    } else if (direction.equals("south_east")) {
                        writer.write(digger.move(Position.SOUTH_EAST).map(Functions.<Position, String> constantly(ok),
                            Functions.<String> constantly(failed)));
                    } else if (direction.equals("north_west")) {
                        writer.write(digger.move(Position.NORTH_WEST).map(Functions.<Position, String> constantly(ok),
                            Functions.<String> constantly(failed)));
                    } else if (direction.equals("south_west")) {
                        writer.write(digger.move(Position.SOUTH_WEST).map(Functions.<Position, String> constantly(ok),
                            Functions.<String> constantly(failed)));
                    } else if (direction.equals("south")) {
                        writer.write(digger.move(Position.SOUTH).map(Functions.<Position, String> constantly(ok),
                            Functions.<String> constantly(failed)));
                    } else if (direction.equals("north")) {
                        writer.write(digger.move(Position.NORTH).map(Functions.<Position, String> constantly(ok),
                            Functions.<String> constantly(failed)));
                    } else{
                	    writer.write("Cannot Move in that direction");
                    }
                }
                else{
                	writer.write("Cannot Move in that direction");
                }
                updateGoldFields(multiplayerdiggers);
                System.out.println(multiplayerGoldField.toString());
            }
        } catch (Exception e) {
            throw new RuntimeException(digger.toString(), e);
        }
    }
    //This fucntion just goes over the list of diggers that are in the multiplauyer map and attempts to change their field tiles
    //so that the other digger looks like a hill.
    //At the momenth though when a digger moves the previous tile remains as a hill and so does the tile that
    //the current digger is standing on. THis is due to the fact that for some reason multiplayerGoldField changes along
    //with the digger's GoldField. Need to make it so taht the multiplayerGoldField stays more constant throughout.
    //maybe we need to define it as a constant instead of getting the goldField of the first digger that enters the "multiplayer"
    //map.
    private void updateGoldFields(List<Digger> multiplayerdiggers) {
		for(Digger digger : multiplayerdiggers){
			GoldField tempmultiplayerGoldField = new GoldField(multiplayerGoldField.getMaxLatitude(),multiplayerGoldField.getMaxLongitude());
			for(Digger otherDigger : multiplayerdiggers){
				if(!digger.equals(otherDigger)){
					System.out.println(digger.getName());
					System.out.println(otherDigger.getName());
					tempmultiplayerGoldField.setField(multiplayerGoldField.getSquares().clone());
					
					
					tempmultiplayerGoldField.setSquare(otherDigger.getPosition(), new HillSquare());
					
				}
			}
			digger.setGoldField(tempmultiplayerGoldField.getSquares().clone());
		}
		
	}

	public void restoreFromLog(Reader reader) {
        try {
            BufferedReader bufferedReader = new BufferedReader(reader);
            String logRow = null;
            PrintWriter writer = new PrintWriter(new VoidOutputStream());
            while ((logRow = bufferedReader.readLine()) != null) {
                String pathInfo = logRow.substring(logRow.indexOf(" ") + 1);
                executePath(pathInfo, writer);
            }
        } catch (Exception e) {
            throw new Error(e);
        }
    }
    
    /**
     * Will read from the reader until it is closed. If the reader is blocked, this method blocks.
     * @param reader
     * @param delay
     */
    public void restoreFromLogWithDelay(Reader reader, long delay) {
        String logRow = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(reader);
            PrintWriter writer = new PrintWriter(new VoidOutputStream());
            while ((logRow = bufferedReader.readLine()) != null) {
                long timestamp = Long.parseLong(logRow.substring(0, logRow.indexOf(" ")));
                long timeSinceLog = System.currentTimeMillis() - timestamp;
                if (timeSinceLog < delay) {
                    Thread.sleep(delay - timeSinceLog);
                }
                String pathInfo = logRow.substring(logRow.indexOf(" ") + 1);
                executePath(pathInfo, writer);
            }
        } catch (Exception e) {
            throw new RuntimeException("logRow is:" + logRow, e);
        }
    }
}