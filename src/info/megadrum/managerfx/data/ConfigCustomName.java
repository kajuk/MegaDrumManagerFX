package info.megadrum.managerfx.data;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.PropertiesConfigurationLayout;

import info.megadrum.managerfx.utils.Constants;
import info.megadrum.managerfx.utils.Utils;

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

	public byte[] getSysexFromConfig(int chainId, int nameId) {
		byte [] sysex_byte = new byte[2];
		byte [] sysex = new byte[Constants.MD_SYSEX_CUSTOM_NAME_SIZE];
		String nameString;
		byte [] nameBytes;
		int i = 0;
		sysex[i++] = Constants.SYSEX_START;
		sysex[i++] = Constants.MD_SYSEX;
		sysex[i++] = (byte) chainId;
		sysex[i++] = Constants.MD_SYSEX_CUSTOM_NAME;
		sysex[i++] = (byte)nameId;
		
		nameString = name + "        ";
		nameString = nameString.substring(0, 8);
		nameBytes = nameString.getBytes();
		for (int p = 0; p < 8;p++) {
			sysex_byte = Utils.byte2sysex(nameBytes[p]);
			sysex[i++] = sysex_byte[0];
			sysex[i++] = sysex_byte[1];
		}
		sysex[i++] = Constants.SYSEX_END;
		return sysex;
	}
	
	public void setConfigFromSysex(byte [] sysex) {
		byte [] sysex_byte = new byte[2];
		char [] bytes_string = { 1, 2, 3, 4, 5, 6, 7, 8 };
		int i = 5;
		if (sysex.length >= Constants.MD_SYSEX_CUSTOM_NAME_SIZE) {
			for (int p = 0; p < 8;p++) {
				sysex_byte[0] = sysex[i++];
				sysex_byte[1] = sysex[i++];
				bytes_string[p] = (char)Utils.sysex2byte(sysex_byte);
			}
			name = String.copyValueOf(bytes_string).trim();
		}
	}

}
