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
	private int id;

	
	public ConfigPositional(int i) {
		id = i;
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

	public void setValueById(int valueId, int value) {
		switch (valueId) {
		case Constants.INPUT_VALUE_ID_POS_LEVEL:
			level = value;
			break;
		case Constants.INPUT_VALUE_ID_POS_LOW:
			low = value;
			break;
		case Constants.INPUT_VALUE_ID_POS_HIGH:
			high = value;
			break;
		default:
				break;
		}
	}

	public int getValueById(int valueId) {
		int value = -1;
		switch (valueId) {
		case Constants.INPUT_VALUE_ID_POS_LEVEL:
			value = level;
			break;
		case Constants.INPUT_VALUE_ID_POS_LOW:
			value = low;
			break;
		case Constants.INPUT_VALUE_ID_POS_HIGH:
			value = high;
			break;
		default:
				break;
		}
		return value;
	}

	public byte[] getSysexFromConfig() {
		byte [] sysex_byte = new byte[2];
		byte [] sysex = new byte[Constants.MD_SYSEX_POS_SIZE];	
		int i = 0;

		sysex[i++] = Constants.SYSEX_START;
		sysex[i++] = Constants.MD_SYSEX;
		sysex[i++] = 0; //(byte)chainId; 
		sysex[i++] = Constants.MD_SYSEX_POS;
		sysex[i++] = (byte)id;
		
		sysex_byte = Utils.byte2sysex((byte)level);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = Utils.byte2sysex((byte)low);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = Utils.byte2sysex((byte)high);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex[i++] = Constants.SYSEX_END;
		return sysex;
	}

	public void setConfigFromSysex(byte [] sysex) {
		byte [] sysex_byte = new byte[2];
		int i = 5;

		if (sysex.length >= Constants.MD_SYSEX_POS_SIZE) {
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			level = Utils.sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			low = Utils.sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			high = Utils.sysex2byte(sysex_byte);
		}
	}


}
