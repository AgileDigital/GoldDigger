package com.agical.golddigger;

import static org.junit.Assert.*;

import org.junit.Test;

import com.agical.golddigger.plugins.DayNightPlugin;
import com.agical.golddigger.plugins.api.GoldDiggerPlugin;

public class TestPluginServiceFactory {

	@Test
	public void testCreatePluginService() {
		PluginService ps = PluginServiceFactory.createPluginService();
		assertTrue("Factory did not return a StaticPluginService",ps instanceof StaticPluginService);
		assertTrue("Should At least contain DayNightPlugin", ps.getPlugins().hasNext());
		GoldDiggerPlugin plugin = ps.getPlugins().next();
		assertTrue("Plugin Service did not contain a DayNightPlugin",plugin instanceof DayNightPlugin);
	}

}
