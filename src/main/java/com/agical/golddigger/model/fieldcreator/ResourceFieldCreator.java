package com.agical.golddigger.model.fieldcreator;

import java.net.URL;

import com.agical.golddigger.PluginService;
import com.agical.golddigger.model.tiles.Square;
import com.agical.jambda.Functions.Fn0;

/**
 * This is a stateful field creator that will read a new field from resource every time createField is called, and act as if 
 * that read field.
 * 
 * The class passed to the constructor will point to the root of the fields 
 * @author daniel
 *
 */
public class ResourceFieldCreator extends FieldCreator {
    
    private final Class<?> resourceRoot;
    private int nr = 1;
    private StringFieldCreator fieldCreator;
    

    public ResourceFieldCreator(Class<?> resourceRoot) {
        this.resourceRoot = resourceRoot;
    }

    @Override
    public Square[][] createField() {
        URL resource = resourceRoot.getResource(nr + ".field");
        if(resource==null) {
            nr = 1;
            resource = resourceRoot.getResource(nr + ".field");
        }
        fieldCreator = new StringFieldCreator(Reader.read(resource));
        nr++;
        return fieldCreator.createField();
    }
    
    @Override
    public int getMaxLatitude() {
        return fieldCreator.getMaxLatitude();
    }
    
    @Override
    public int getMaxLongitude() {
        return fieldCreator.getMaxLongitude();
    }
    
    @Override
    public int getLineOfSightLength() {
        return fieldCreator.getLineOfSightLength();
    }
    
    @Override
    public int getNumberOfSides() {
        return fieldCreator.getNumberOfSides();
    }
    
    @Override
    public int getJoinTime() {
        return fieldCreator.getJoinTime();
    }
    
    @Override
    public int getGameTime() {
        return fieldCreator.getGameTime();
    }
    
    @Override
    public int getEndTime() {
        return fieldCreator.getEndTime();
    }

	public static Fn0<FieldCreator> factory(
			final Class<?> location) {
		return new Fn0<FieldCreator>() {
			@Override
			public FieldCreator apply() {
				return new ResourceFieldCreator(location);
			}
		};
	}
	
	@Override
	public PluginService getPluginService(){
		return fieldCreator.getPluginService();
	}
}
