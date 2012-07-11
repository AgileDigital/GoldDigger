package com.agical.golddigger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.agical.golddigger.plugins.DayNightPlugin;
import com.agical.golddigger.plugins.api.GoldDiggerPlugin;

public class StaticPluginService implements PluginService {
	List<GoldDiggerPlugin> plugins;
	
	public StaticPluginService(){
		plugins = new ArrayList<GoldDiggerPlugin>();
		plugins.add(new DayNightPlugin());
	}
	
	@Override
	public Iterator<GoldDiggerPlugin> getPlugins() {
		return plugins.iterator();
	}

	@Override
	public void initPlugins() {
		for (GoldDiggerPlugin plugin : plugins){
			plugin.init();
		}
	}

}
