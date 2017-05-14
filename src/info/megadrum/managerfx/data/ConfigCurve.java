package info.megadrum.managerfx.data;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.PropertiesConfigurationLayout;

import info.megadrum.managerfx.utils.Constants;
import info.megadrum.managerfx.utils.Utils;

public class ConfigCurve {
	public int [] yValues = {2, 32, 64, 96, 128, 160, 192, 224, 255};
	public int syncState = Constants.SYNC_STATE_UNKNOWN;
	public boolean sysexReceived = false;

	public ConfigCurve() {
	}

	public void copyToPropertiesConfiguration(PropertiesConfiguration prop, PropertiesConfigurationLayout layout, String prefix, Integer id) {
		id++;
		Integer c;
		prefix = prefix+"["+id.toString()+"].";
		layout.setComment(prefix+"P1", "\n#Curve "+id.toString() + " settings");
		for (Integer i = 0; i<yValues.length;i++) {
			c = i+1;
			prop.setProperty(prefix+"P"+c.toString(), yValues[i]);		
		}
	}

	public void copyFromPropertiesConfiguration(PropertiesConfiguration prop, String prefix, Integer id) {
		id++;
		Integer c;
		prefix = prefix+"["+id.toString()+"].";
		for (Integer i = 0; i<yValues.length;i++) {
			c = i+1;
			yValues[i] = Utils.validateInt(prop.getInt(prefix+"P"+c.toString(), yValues[i]),2,255,yValues[i]);		
		}
	}	


}
