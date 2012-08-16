/**
 * 
 */
package com.agical.golddigger.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.agical.golddigger.model.ConfigReader;
import com.agical.golddigger.model.Digger;
import com.agical.golddigger.model.Diggers;
import com.agical.golddigger.model.Position;
import com.agical.jambda.Functions;

import com.agical.golddigger.model.GoldField;
import com.agical.golddigger.model.fieldcreator.StringFieldCreator;
import com.agical.golddigger.model.tiles.BankSquare;
import com.agical.golddigger.model.tiles.HillSquare;
import com.agical.golddigger.model.tiles.Square;
import com.agical.golddigger.model.tiles.WallSquare;


public class PathExecutor {
    private final Diggers diggers;
    private final Writer log;
	private boolean multiplayer = false;
	public final static String DROPONWRONGBANKMESSAGE = "You are attempting to drop on someone else's Bank....";
	public PathExecutor(Diggers diggers, Writer log) {
        super();
        this.diggers = diggers;
                
        this.log = log;
        try {
        	readConfig();
        }  catch (Exception e) {
            throw new RuntimeException(e);
        }
        
    }
    

    
    
    public void readConfig() throws IOException {
    	
    	String fileName = "config.txt";
    	String fileContent = "";
    	
    	try {
    		fileContent = ConfigReader.read(fileName);
    	}  catch (Exception e) {
            throw new RuntimeException(e);
        }
    	 	   	
    	String[] lines = fileContent.split("\n");    	
    	String game_mode = "singleplayer";

        for (String line : lines) {        	
            if (line.startsWith("!game_mode")) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    game_mode = parts[1].trim();         
                    
                }
            }
        }
        
        if (game_mode.contains("multiplayer")) multiplayer = true;
        
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
                if(digger.getGoldField().getSquare(digger.getPosition()) instanceof BankSquare){
                	BankSquare tempBankSquare = (BankSquare)digger.getGoldField().getSquare(digger.getPosition());
                	if(tempBankSquare.getName() == digger.getName()); writer.write(DROPONWRONGBANKMESSAGE);
                }
                int carriedAfter = digger.getCarriedGold();
                writer.write((carriedBefore - carriedAfter) + "\n");
            }
            if (action.equals("carrying")) {
                writer.write(digger.getCarriedGold() + "\n");
            }
            if (action.equals("next")) {
                if(digger.getGoldField().hasGold()) {
                    writer.write("FAILED\n");
                } else {
                    writer.write("OK\n");
                    diggers.newGame(digger);                    
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
                
                // every command will update all the diggers
                // fields in a multiplayer game
                if (multiplayer) diggers.updateGoldFields();
                
            }
        } catch (Exception e) {
            throw new RuntimeException(digger.toString(), e);
        }
        
    }
    
     /**
     * Updates the diggers' maps to comply with the master map and account for other diggers.
     * @param multiplayerdiggers
     */
    /*private void updateGoldFields() {
		for(Digger digger : diggers.getDiggers()){
			//sets the field to default tiles defined previously to avoid redrawing of digger tiles
			CurrentMultiplayerGoldField.setField(OrigianlMultiplayerGoldField.getSquares());
			for(Digger otherDigger : diggers.getDiggers()){
				if(!digger.equals(otherDigger)){					
					CurrentMultiplayerGoldField.setSquare(otherDigger.getPosition(), new HillSquare());					
				}
			}
			digger.setGoldField(CurrentMultiplayerGoldField.getSquares());
		}
		
	}*/
    
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
    
    public void setMultiplayer(boolean multi) {
    	multiplayer = multi;
    }
}