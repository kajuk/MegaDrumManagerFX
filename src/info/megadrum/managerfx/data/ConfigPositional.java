package info.megadrum.managerfx.data;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.PropertiesConfigurationLayout;

import info.megadrum.managerfx.utils.Constants;
import info.megadrum.managerfx.utils.Utils;

public class ConfigPositional {
	public int level = 0;
	public int low = 5;
	public int high = 15;
	public int syncState = Constants.SYNC_STATE_UNKNOWN;
	public boolean sysexReceived = false;

	
	public ConfigPositional (){
	}
	
	public void copyToPropertiesConfiguration(PropertiesConfiguration prop, PropertiesConfigurationLayout layout, String prefix, Integer id) {
		id++;
		prefix = prefix+"["+id.toString()+"].";
		layout.setComment(prefix+"level", "\n#Input "+id.toString()+" positional settings");
		prop.setProperty(prefix+"level", level);
		prop.setProperty(prefix+"low", low);
		prop.setProperty(prefix+"high", high);
	}

	public void copyFromPropertiesConfiguration(PropertiesConfiguration prop, String prefix, Integer id) {
		id++;
		prefix = prefix+"["+id.toString()+"].";
		level = Utils.validateInt(prop.getInt(prefix+"level", level),0,3,level);
		low = Utils.validateInt(prop.getInt(prefix+"low", low),0,100,low);
		high = Utils.validateInt(prop.getInt(prefix+"high", high),0,100,high);
	}	

}
