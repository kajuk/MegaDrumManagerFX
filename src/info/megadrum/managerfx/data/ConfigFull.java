package info.megadrum.managerfx.data;

import org.apache.commons.configuration.ConversionException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.PropertiesConfigurationLayout;

import info.megadrum.managerfx.utils.Constants;
import info.megadrum.managerfx.utils.Utils;

public class ConfigFull implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4402239785688415814L;
	public ConfigGlobalMisc configGlobalMisc;
	public ConfigMisc configMisc;
	public ConfigPedal configPedal;
	public ConfigPad [] configPads;
	public ConfigPositional [] configPos;
	public Config3rd [] config3rds;
	public ConfigCurve [] configCurves;
	public ConfigCustomName [] configCustomNames;
	public ConfigConfigName [] configConfigNames;
	public int customNamesCount = 2;
	public int configNamesCount;
	public int configCurrent;
	public boolean configNameChanged = false;
	public boolean configNameSysexReceived = false;
	public boolean configCountSysexReceived = false;
	public boolean configCurrentSysexReceived = false;	
	private static final String configGlobalMiscPrefix = "global_misc.";
	private static final String configMiscPrefix = "misc.";
	private static final String configPedalPrefix = "pedal.";
	private static final String configPadPrefix = "input";
	private static final String configPosPrefix = "pos";
	private static final String config3rdPrefix = "pad3d_zone";
	private static final String configCurvePrefix = "curve";
	private static final String configCustomNamePrefix = "customName";
	
	public ConfigFull() {
		
		configGlobalMisc = new ConfigGlobalMisc();
		configMisc = new ConfigMisc();
		configPedal = new ConfigPedal();
		configPads = new ConfigPad[Constants.PADS_COUNT];
		configPos = new ConfigPositional[Constants.PADS_COUNT];
		config3rds = new Config3rd[(Constants.PADS_COUNT-1)/2];
		for (Integer i = 0; i < Constants.PADS_COUNT;i++) {
			configPads[i] = new ConfigPad(i);
			configPos[i] = new ConfigPositional(i); 
			if ((i>0) && ((i&0x01)==0)) {
				config3rds[(i-1)/2] = new Config3rd((i-1)/2);
			}
		}
		configCurves = new ConfigCurve[Constants.CURVES_COUNT];
		for (Integer i = 0; i < Constants.CURVES_COUNT;i++) {
			configCurves[i] = new ConfigCurve(i);
		}
		configCustomNames = new ConfigCustomName[Constants.CUSTOM_NAMES_MAX];
		for (Integer i = 0; i < Constants.CUSTOM_NAMES_MAX;i++) {
			configCustomNames[i] = new ConfigCustomName(i);
        	configCustomNames[i].name = "Custom" + i.toString(); 
		}
		configConfigNames = new ConfigConfigName[Constants.CONFIG_NAMES_MAX];
		for (Integer i = 0; i < Constants.CONFIG_NAMES_MAX;i++) {
			configConfigNames[i] = new ConfigConfigName(i);
			//configConfigNames[i].name = "Custom" + i.toString(); 
			configConfigNames[i].name = "            "; 
		}
		configNamesCount = Constants.CONFIG_NAMES_MAX;
	}
	
	public void copyToPropertiesConfiguration(PropertiesConfiguration prop, PropertiesConfigurationLayout layout) {
		configGlobalMisc.copyToPropertiesConfiguration(prop, layout,configGlobalMiscPrefix);
		configMisc.copyToPropertiesConfiguration(prop, layout,configMiscPrefix);
		configPedal.copyToPropertiesConfiguration(prop, layout,configPedalPrefix);
		for (Integer i = 0; i < Constants.PADS_COUNT;i++) {
			configPads[i].copyToPropertiesConfiguration(prop, layout,configPadPrefix, i);
			configPos[i].copyToPropertiesConfiguration(prop, layout,configPosPrefix, i);
			if ((i>0) && ((i&0x01)==0)) {
				config3rds[(i-1)/2].copyToPropertiesConfiguration(prop, layout,config3rdPrefix, (i-1)/2);				
			}
		}
		for (Integer i = 0; i < Constants.CURVES_COUNT;i++) {
			configCurves[i].copyToPropertiesConfiguration(prop, layout,configCurvePrefix, i);
		}
		layout.setComment(configCustomNamePrefix+"Count", "\n#Custom Pads Names");
		prop.setProperty(configCustomNamePrefix+"Count", customNamesCount);
		for (Integer i = 0; i < Constants.CUSTOM_NAMES_MAX;i++) {
			configCustomNames[i].copyToPropertiesConfiguration(prop, layout,configCustomNamePrefix, i);
		}
	}
	
	public void copyFromPropertiesConfiguration(PropertiesConfiguration prop) throws ConversionException {
		configGlobalMisc.copyFromPropertiesConfiguration(prop, configGlobalMiscPrefix);
		configMisc.copyFromPropertiesConfiguration(prop, configMiscPrefix);
		configPedal.copyFromPropertiesConfiguration(prop, configPedalPrefix);
		for (Integer i = 0; i < Constants.PADS_COUNT;i++) {
			configPads[i].copyFromPropertiesConfiguration(prop, configPadPrefix, i);
			configPos[i].copyFromPropertiesConfiguration(prop, configPosPrefix, i);
			if ((i>0) && ((i&0x01)==0)) {
				config3rds[(i-1)/2].copyFromPropertiesConfiguration(prop, config3rdPrefix, (i-1)/2);				
			}
		}
		for (Integer i = 0; i < Constants.CURVES_COUNT;i++) {
			configCurves[i].copyFromPropertiesConfiguration(prop, configCurvePrefix, i);
		}
		customNamesCount = Utils.validateInt(prop.getInt(configCustomNamePrefix+"Count", customNamesCount),2,32,customNamesCount);
		for (Integer i = 0; i < Constants.CUSTOM_NAMES_MAX;i++) {
			configCustomNames[i].copyFromPropertiesConfiguration(prop, configCustomNamePrefix, i);
		}
	}
	
	public void resetSyncState() {
		configMisc.syncState = Constants.SYNC_STATE_UNKNOWN;
		configPedal.syncState = Constants.SYNC_STATE_UNKNOWN;
		configGlobalMisc.syncState = Constants.SYNC_STATE_UNKNOWN;
		for (Integer i = 0; i < Constants.PADS_COUNT;i++) {
			configPads[i].syncState = Constants.SYNC_STATE_UNKNOWN;
			if ((i>0) && ((i&0x01)==0)) {
				config3rds[(i-1)/2].syncState = Constants.SYNC_STATE_UNKNOWN;				
			}
		}
		for (Integer i = 0; i < Constants.CURVES_COUNT;i++) {
			configCurves[i].syncState = Constants.SYNC_STATE_UNKNOWN;
		}
		for (Integer i = 0; i < Constants.CUSTOM_NAMES_MAX;i++) {
			configCustomNames[i].syncState = Constants.SYNC_STATE_UNKNOWN;
		}
	}
}
