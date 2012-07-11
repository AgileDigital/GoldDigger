package com.agical.golddigger;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PluginServiceFactory {
	public static PluginService createPluginService(){
		//addPluginJarsToClasspath();
		//return StandardPluginService.getInstance();
		return new StaticPluginService();
	}
	
	private static void addPluginJarsToClasspath(){
		try {
			ClasspathUtils.addDirToClasspath(new File("plugins"));
		} catch (IOException ioe){
			Logger.getLogger(PluginServiceFactory.class.getName()).log(Level.SEVERE, null, ioe);
		}
	}
}
