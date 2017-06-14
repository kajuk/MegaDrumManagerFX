package info.megadrum.managerfx.data;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.PropertiesConfigurationLayout;

import info.megadrum.managerfx.utils.Constants;
import info.megadrum.managerfx.utils.Utils;

public class ConfigGlobalMisc {
	
	public boolean changed; 
	
	public int lcd_contrast = 50;
	public int inputs_count = 18;
	public boolean custom_names_en = false;
	public boolean config_names_en = false;
	public boolean midi2_for_sysex = false;
	public int syncState = Constants.SYNC_STATE_UNKNOWN;
	public boolean sysexReceived = false;

	public ConfigGlobalMisc (){
	}
	
	public void copyToPropertiesConfiguration(PropertiesConfiguration prop, PropertiesConfigurationLayout layout, String prefix) {
		layout.setComment(prefix+"lcd_contrast", "Global Misc settings");
		prop.setProperty(prefix+"lcd_contrast", lcd_contrast);
		prop.setProperty(prefix+"inputs_count", inputs_count);
		prop.setProperty(prefix+"custom_names_en", custom_names_en);
		prop.setProperty(prefix+"config_names_en", config_names_en);
		prop.setProperty(prefix+"midi2_for_sysex", midi2_for_sysex);
	}

	public void copyFromPropertiesConfiguration(PropertiesConfiguration prop, String prefix) {
		lcd_contrast = Utils.validateInt(prop.getInt(prefix+"lcd_contrast", lcd_contrast),1,100,lcd_contrast);
		inputs_count = Utils.validateInt(prop.getInt(prefix+"inputs_count", inputs_count),18,56,inputs_count);
		custom_names_en = prop.getBoolean(prefix+"custom_names_en", custom_names_en);
		config_names_en = prop.getBoolean(prefix+"config_names_en", config_names_en);
		midi2_for_sysex = prop.getBoolean(prefix+"midi2_for_sysex", midi2_for_sysex);
	}

	public byte[] getSysexFromConfig(int chainId) {
		byte [] sysex_byte = new byte[2];
		byte [] sysex_short = new byte[4];
		byte [] sysex = new byte[Constants.MD_SYSEX_GLOBAL_MISC_SIZE];
		short flags;
		int i = 0;

		flags = (short) (
				(((custom_names_en)?1:0)<<1)|(((config_names_en)?1:0)<<2)|(((midi2_for_sysex)?1:0)<<5)
				);
		sysex[i++] = Constants.SYSEX_START;
		sysex[i++] = Constants.MD_SYSEX;
		sysex[i++] = (byte)chainId;
		sysex[i++] = Constants.MD_SYSEX_GLOBAL_MISC;
		
		sysex_byte = Utils.byte2sysex((byte)(100 - lcd_contrast));
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = Utils.byte2sysex((byte)inputs_count);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_short = Utils.short2sysex(flags);
		sysex[i++] = sysex_short[0];
		sysex[i++] = sysex_short[1];
		sysex[i++] = sysex_short[2];
		sysex[i++] = sysex_short[3];
		sysex[i++] = Constants.SYSEX_END;
		return sysex;
	}
	
	public void setConfigFromSysex(byte [] sysex) {
		byte [] sysex_byte = new byte[2];
		byte [] sysex_short = new byte[4];
		short flags;
		
		int i = 4;
		if (sysex.length >= Constants.MD_SYSEX_GLOBAL_MISC_SIZE) {
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			lcd_contrast = (100 - Utils.sysex2byte(sysex_byte));
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			inputs_count = Utils.sysex2byte(sysex_byte);
			sysex_short[0] = sysex[i++];
			sysex_short[1] = sysex[i++];
			sysex_short[2] = sysex[i++];
			sysex_short[3] = sysex[i++];
			flags = Utils.sysex2short(sysex_short);
			custom_names_en = ((flags&(1<<1)) != 0);
			config_names_en = ((flags&(1<<2)) != 0);
			midi2_for_sysex = ((flags&(1<<5)) != 0);
		}
		
	}
	

}
