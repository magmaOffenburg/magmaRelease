/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.logging;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class PropertyMap implements Serializable
{
	private final Map<String, Property> properties = new LinkedHashMap<>();

	public Map<String, Property> getMap()
	{
		return Collections.unmodifiableMap(properties);
	}

	public void resetPropertyStates()
	{
		for (Property property : properties.values()) {
			property.valueChanged = false;
			property.updatedLastCycle = false;
		}
	}

	public void update()
	{
		for (Property property : properties.values()) {
			property.updatedLastCycle = false;
		}
	}

	public class Property implements Serializable
	{
		public String value;

		public boolean valueChanged;

		public boolean updatedLastCycle;

		private int consecutiveUnchanged;

		public void setValueChanged(boolean valueChanged)
		{
			if (valueChanged) {
				this.valueChanged = true;
				consecutiveUnchanged = 10;
			} else {
				if (consecutiveUnchanged <= 0) {
					this.valueChanged = false;
				} else {
					consecutiveUnchanged--;
				}
			}
		}

		@Override
		public String toString()
		{
			return value;
		}
	}
}
