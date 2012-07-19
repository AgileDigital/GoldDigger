/**
 * 
 */
package com.agical.golddigger.server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

import com.agical.golddigger.model.Digger;
import com.agical.golddigger.model.Diggers;
import com.agical.golddigger.model.Position;
import com.agical.golddigger.model.fieldcreator.FieldCreator;
import com.agical.jambda.Functions;

import java.util.Timer;
import java.util.TimerTask;

public class PathExecutor {
    private final Diggers diggers;
    private final Writer log;
    Timer timer, gameTimer, endingTimer;
    GameTask gameTask;
    EndingTask endingTask;
    int join_time, game_time, end_time; // seconds
    boolean ending = false;
    
    public PathExecutor(Diggers diggers, FieldCreator fieldCreator, Writer log) {
        super();
        this.diggers = diggers;
        this.log = log;
        
        join_time = fieldCreator.getJoinTime();
        game_time = fieldCreator.getGameTime();
        end_time = fieldCreator.getGameTime();
        
        timer = new Timer();
        MyTask t = new MyTask();
        timer.schedule(t, 0, 1000);
        
        endingTimer = new Timer();
        endingTask = new EndingTask();
    }
    
    
    class MyTask extends TimerTask {
    	public void run() {
            join_time--;
            System.out.println(join_time);
            if (join_time <= 0) {

                this.cancel();
                gameTimer.schedule(gameTask, 0);
            }
        }
    }
    
    class GameTask extends TimerTask {
    	public void run(){	
    		game_time--;
    		if (game_time <= 0){
    			this.cancel();
    			System.out.println("\"End of game\"");
    		} else if (ending == true){
    			this.cancel();
    		}
    	}
    }
    
     class EndingTask extends TimerTask {
    	public void run() {
    		//Insert some action performed after the time has expired
    		join_time = 1; //Anything non zero
    		System.out.println("\"End of game\"");
    	}
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
            if (action.equals("view") && (join_time <= 0)) {
                writer.write(digger.getView());
            }
            if (action.equals("score") && (join_time <= 0)) {
                int goldInTheBank = digger.getGoldInTheBank();
                writer.write(goldInTheBank + "\n");
            }
            if (action.equals("grab") && (join_time <= 0)) {
                int carriedBefore = digger.getCarriedGold();
                digger.grab();
                int carriedAfter = digger.getCarriedGold();
                writer.write((carriedAfter - carriedBefore) + "\n");
                
                if (!digger.getGoldField().hasGold() && end_time <= game_time) { //Players should bank
					System.out.println("Last piece of gold collected, x seconds remaining to bank");
					ending = true;
					endingTimer.schedule(endingTask, end_time * 1000);
                }
            }
            if (action.equals("drop") && (join_time <= 0)) {
                int carriedBefore = digger.getCarriedGold();
                digger.drop();
                int carriedAfter = digger.getCarriedGold();
                writer.write((carriedBefore - carriedAfter) + "\n");
				
				if (ending) {
                	/* If a digger banks before the ending timer runs out just immediately stop the game */
                	endingTimer.cancel();
                	join_time = 1;
                }            
            }
            if (action.equals("carrying") && (join_time <= 0)) {
                writer.write(digger.getCarriedGold() + "\n");
            }
            if (action.equals("next") && (join_time <= 0)) {
                if(digger.getGoldField().hasGold()) {
                    writer.write("FAILED\n");
                } else {
                    writer.write("OK\n");
                    diggers.newGame(digger);
                }
            }
            if (action.equals("move") && (join_time <= 0)) {
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
            }
        } catch (Exception e) {
            throw new RuntimeException(digger.toString(), e);
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
