package info.megadrum.managerfx.data;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.PropertiesConfigurationLayout;

import info.megadrum.managerfx.utils.Constants;
import info.megadrum.managerfx.utils.Utils;

public class Config3rd {
	public int note;
	public int threshold = 30;
	public int pressrollNote;
	public int altNote;
	public int dampenedNote;
	public boolean pressrollNote_linked = false;
	public boolean altNote_linked = false;
	public int syncState = Constants.SYNC_STATE_UNKNOWN;
	public boolean sysexReceived = false;
	private int id = 0;


	public Config3rd (int i) {
		id = i;
	}

	public void copyToPropertiesConfiguration(PropertiesConfiguration prop, PropertiesConfigurationLayout layout, String prefix, Integer id) {
		id = id*2+2;
		Integer rim = id+1;
		prefix = prefix+"["+id.toString()+"+"+rim.toString()+"].";
		layout.setComment(prefix+"note", "\n#Third zone for intputs "+id.toString() + " and " + rim.toString() + " settings");
		prop.setProperty(prefix+"note", note);
		prop.setProperty(prefix+"threshold", threshold);
		prop.setProperty(prefix+"altNote", altNote);
		prop.setProperty(prefix+"pressrollNote", pressrollNote);
		prop.setProperty(prefix+"dampenedNote", dampenedNote);
		prop.setProperty(prefix+"altNote_linked", altNote_linked);
		prop.setProperty(prefix+"pressrollNote_linked", pressrollNote_linked);
	}

	public void copyFromPropertiesConfiguration(PropertiesConfiguration prop, String prefix, Integer id) {
		id = id*2+2;
		Integer rim = id+1;
		prefix = prefix+"["+id.toString()+"+"+rim.toString()+"].";
		note = Utils.validateInt(prop.getInt(prefix+"note", note),0,127,note);
		threshold = Utils.validateInt(prop.getInt(prefix+"threshold", threshold),0,256,threshold);
		altNote = Utils.validateInt(prop.getInt(prefix+"altNote", altNote),0,127,altNote);
		pressrollNote = Utils.validateInt(prop.getInt(prefix+"pressrollNote", pressrollNote),0,127,pressrollNote);
		dampenedNote = Utils.validateInt(prop.getInt(prefix+"dampenedNote", dampenedNote),0,127,dampenedNote);
		altNote_linked = prop.getBoolean(prefix+"altNote_linked", altNote_linked);
		pressrollNote_linked = prop.getBoolean(prefix+"pressrollNote_linked", pressrollNote_linked);				
	}
	
	public void setValueById(int valueId, int value) {
		switch (valueId) {
		case Constants.THIRD_ZONE_VALUE_ID_NOTE:
			note = value;
			break;
		case Constants.THIRD_ZONE_VALUE_ID_ALT_NOTE:
			altNote = value;
			break;
		case Constants.THIRD_ZONE_VALUE_ID_PRESSROLL_NOTE:
			pressrollNote = value;
			break;
		case Constants.THIRD_ZONE_VALUE_ID_DAMPENED_NOTE:
			dampenedNote = value;
			break;
		case Constants.THIRD_ZONE_VALUE_ID_THRESHOLD:
			threshold = value;
			break;
		default:
				break;
		}
	}

	public int getValueById(int valueId) {
		int value = -1;
		switch (valueId) {
		case Constants.THIRD_ZONE_VALUE_ID_NOTE:
			value = note;
			break;
		case Constants.THIRD_ZONE_VALUE_ID_ALT_NOTE:
			value = altNote;
			break;
		case Constants.THIRD_ZONE_VALUE_ID_PRESSROLL_NOTE:
			value = pressrollNote;
			break;
		case Constants.THIRD_ZONE_VALUE_ID_DAMPENED_NOTE:
			value = dampenedNote;
			break;
		case Constants.THIRD_ZONE_VALUE_ID_THRESHOLD:
			value = threshold;
			break;
		default:
				break;
		}
		return value;
	}

	public byte[] getSysexFromConfig () {
		byte [] sysex_byte = new byte[2];
		byte [] sysex = new byte[Constants.MD_SYSEX_3RD_SIZE];
		int i = 0;

		sysex[i++] = Constants.SYSEX_START;
		sysex[i++] = Constants.MD_SYSEX;
		sysex[i++] = 0; //(byte)chainId;
		sysex[i++] = Constants.MD_SYSEX_3RD;
		sysex[i++] = (byte)id;
		
		sysex_byte = Utils.byte2sysex((byte)note);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = Utils.byte2sysex((byte)threshold);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = Utils.byte2sysex((byte)pressrollNote);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = Utils.byte2sysex((byte)altNote);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = Utils.byte2sysex((byte)dampenedNote);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex[i++] = Constants.SYSEX_END;
		return sysex;
	}
	
	public void setConfigFromSysex(byte [] sysex) {
		byte [] sysex_byte = new byte[2];
		int i = 5;
		if (sysex.length >= Constants.MD_SYSEX_3RD_SIZE) {
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			note = Utils.sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			threshold = (int)(Utils.sysex2byte(sysex_byte)&0xff);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			pressrollNote = Utils.sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			altNote = Utils.sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			dampenedNote = Utils.sysex2byte(sysex_byte);
		}
		
	}


}
