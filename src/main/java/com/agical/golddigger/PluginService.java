package com.agical.golddigger;

import java.util.Iterator;

import com.agical.golddigger.plugins.api.GoldDiggerPlugin;

public interface PluginService {
	Iterator<GoldDiggerPlugin> getPlugins();
	void initPlugins();
}
