package com.agical.golddigger.plugins.api;

import com.agical.golddigger.model.GoldField;

public interface FieldUpdatePlugin extends GoldDiggerPlugin {
	GoldField update(GoldField field);
}