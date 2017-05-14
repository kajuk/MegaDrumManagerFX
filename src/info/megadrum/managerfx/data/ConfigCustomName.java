package info.megadrum.managerfx.data;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.PropertiesConfigurationLayout;

import info.megadrum.managerfx.utils.Constants;

public class ConfigCustomName {
	//public int [] yValues = {2, 32, 64, 96, 128, 160, 192, 224, 255};
	public String name = "Custom__";
	public int syncState = Constants.SYNC_STATE_UNKNOWN;
	public boolean sysexReceived = false;

	public ConfigCustomName() {
	}

	public void copyToPropertiesConfiguration(PropertiesConfiguration prop, PropertiesConfigurationLayout layout, String prefix, Integer id) {
		id++;
		//Integer c;
		prefix = prefix+"["+id.toString()+"]";
		prop.setProperty(prefix, name);
	}

	public void copyFromPropertiesConfiguration(PropertiesConfiguration prop, String prefix, Integer id) {
		id++;
		//Integer c;
		prefix = prefix+"["+id.toString()+"]";
		name = prop.getString(prefix, name);
	}	


}
