package com.agical.golddigger;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.logging.Logger;

import com.agical.golddigger.plugins.api.GoldDiggerPlugin;

public class StandardPluginService implements PluginService {

	private static StandardPluginService pluginService;
	private ServiceLoader<GoldDiggerPlugin> serviceLoader;
	private Logger logger = Logger.getLogger(getClass().getName());
	
	private StandardPluginService(){
        //load all the classes in the classpath that have implemented the interface
        serviceLoader = ServiceLoader.load(GoldDiggerPlugin.class);
	}
	
	public static StandardPluginService getInstance(){
        if(pluginService == null){
            pluginService = new StandardPluginService();
        }
        return pluginService;
    }
	
	@Override
	public Iterator<GoldDiggerPlugin> getPlugins() {
        return serviceLoader.iterator();
	}

	@Override
	public void initPlugins() {
        Iterator<GoldDiggerPlugin> iterator = getPlugins();
        if(!iterator.hasNext()){
            logger.info("No plugins were found!");
        }
        while(iterator.hasNext()){
        	GoldDiggerPlugin plugin = iterator.next();
            logger.info("Initializing the plugin " + plugin.getName());
            plugin.init();
        }
	}

}
