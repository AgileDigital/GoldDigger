package com.agical.golddigger.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agical.golddigger.model.event.GolddiggerNotifier;
import com.agical.golddigger.model.fieldcreator.FieldCreator;
import com.agical.golddigger.model.tiles.OtherDiggerSquare;
import com.agical.jambda.Functions.Fn0;
import com.agical.golddigger.model.tiles.Square;


public class Diggers {
    private List<Digger> diggers = new ArrayList<Digger>();
    private Map<Digger, FieldCreator> diggersFieldCreator = new HashMap<Digger, FieldCreator>();
    private final GolddiggerNotifier golddiggerNotifier;
	private final Fn0<FieldCreator> fieldCreatorFactory;
	
	private boolean multiplayer = false;    
    private Square[][] OriginalMultiplayerGoldField;
    private GoldField CurrentMultiplayerGoldField;
    private boolean multiplayerMapStored = false;
    
    public Diggers(Fn0<FieldCreator> fieldCreatorFactory) {
        this.fieldCreatorFactory = fieldCreatorFactory;
		this.golddiggerNotifier = new GolddiggerNotifier();
    }
    
    public GolddiggerNotifier getGolddiggerNotifier() {
        return golddiggerNotifier;
    }
    
    public Digger createDigger(String name, String secretName) {
        Digger digger = new Digger(name, secretName);
        addDigger(digger, fieldCreatorFactory.apply());
        return digger;
    }

    private void addDigger(Digger digger, FieldCreator fieldCreator) {
        diggers.add(digger);
        digger.setGolddiggerNotifier(golddiggerNotifier);
        diggersFieldCreator.put(digger, fieldCreator);
        golddiggerNotifier.newDigger(digger);
    }

    public void newGame(Digger digger) {
        FieldCreator fieldCreator  = diggersFieldCreator.get(digger);
        GoldField goldField = new GoldField(fieldCreator);
        goldField.setGolddiggerNotifier(golddiggerNotifier);
        digger.newGame(goldField);
        
        golddiggerNotifier.newGame(digger, goldField);
        
        
        
        if (!multiplayerMapStored) {
        	
        	OriginalMultiplayerGoldField = goldField.getSquares();	        
	        CurrentMultiplayerGoldField = goldField;
	        multiplayerMapStored = true;
        }
    }

    public Digger getDigger(String secretName) {
        for (Digger digger : diggers) {
            if(digger.getSecretName().equals(secretName)) {
                return digger;
            }
        }
        return null; // screaming for an option
    }

    public List<Digger> getDiggers() {
        return diggers;
    }
    
    public void updateGoldFields() {
    	
		for(Digger digger : diggers){
			//sets the field to default tiles defined previously to avoid redrawing of digger tiles
			CurrentMultiplayerGoldField.setField(OriginalMultiplayerGoldField);
			for(Digger otherDigger : diggers){
				if(!digger.equals(otherDigger)){					
					CurrentMultiplayerGoldField.setSquare(otherDigger.getPosition(), new OtherDiggerSquare());					
				}
			}
			digger.setGoldField(CurrentMultiplayerGoldField.getSquares());
		}
		
	}
    
    public void setMultiplayer(boolean multi) {
    	multiplayer = multi;
    }
    
    public boolean getMultiplayer() {
    	return multiplayer;
    }

    
    
}
