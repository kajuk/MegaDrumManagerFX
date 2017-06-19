package info.megadrum.managerfx.data;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.PropertiesConfigurationLayout;

import info.megadrum.managerfx.utils.Constants;
import info.megadrum.managerfx.utils.Utils;

public class ConfigPad {
	public int note = 0;
	public boolean disabled = false;
	public int channel = 9;
	public int curve = 0;
	public int threshold = 30;
	public int retrigger = 10;
	public int levelMax = 64;
	public int minScan = 20;
	public boolean type = false;
	public boolean autoLevel = true;
	public boolean dual = false;
	public boolean threeWay = false;
	public boolean leftInput = true;
	public int function = 0;
	public int gain = 0;
	public int xtalkLevel = 3;
	public int xtalkGroup = 0;
	public int dynTime = 3;
	public int dynLevel = 4;
	public int compression = 0;
	public int shift = 0;
	public int name = 0;
	public int altNote = 0;
	public int pressrollNote = 0;
	public boolean altNote_linked = false;
	public boolean pressrollNote_linked = false;
	public int syncState = Constants.SYNC_STATE_UNKNOWN;
	public boolean sysexReceived = false;

	private int id;
	
	public ConfigPad (int i) {
		id = i;
	}
	
	public void copyToPropertiesConfiguration(PropertiesConfiguration prop, PropertiesConfigurationLayout layout, String prefix, Integer id) {
		id++;
		prefix = prefix+"["+id.toString()+"].";
		layout.setComment(prefix+"note", "\n#Input "+id.toString()+" settings");
		prop.setProperty(prefix+"disabled", disabled);
		prop.setProperty(prefix+"note", note);
		prop.setProperty(prefix+"channel", channel);
		prop.setProperty(prefix+"curve", curve);
		prop.setProperty(prefix+"threshold", threshold);
		prop.setProperty(prefix+"retrigger", retrigger);
		prop.setProperty(prefix+"levelMax", levelMax);
		prop.setProperty(prefix+"minScan", minScan);
		prop.setProperty(prefix+"type", type);		
		prop.setProperty(prefix+"autoLevel", autoLevel);
		prop.setProperty(prefix+"dual", dual);
		prop.setProperty(prefix+"threeWay", threeWay);
		prop.setProperty(prefix+"function", function);
		prop.setProperty(prefix+"gain", gain);
		prop.setProperty(prefix+"xtalkLevel", xtalkLevel);
		prop.setProperty(prefix+"xtalkGroup", xtalkGroup);
		prop.setProperty(prefix+"dynTime", dynTime);
		prop.setProperty(prefix+"dynLevel", dynLevel);
		prop.setProperty(prefix+"compression", compression);
		prop.setProperty(prefix+"shift", shift);
		prop.setProperty(prefix+"name", name);
		prop.setProperty(prefix+"altNote", altNote);
		prop.setProperty(prefix+"pressrollNote", pressrollNote);
		prop.setProperty(prefix+"altNote_linked", altNote_linked);
		prop.setProperty(prefix+"pressrollNote_linked", pressrollNote_linked);				
	}

	public void copyFromPropertiesConfiguration(PropertiesConfiguration prop, String prefix, Integer id) {
		id++;
		prefix = prefix+"["+id.toString()+"].";
		autoLevel = prop.getBoolean(prefix+"disabled", disabled);
		note = Utils.validateInt(prop.getInt(prefix+"note", note),0,127,note);
		channel = Utils.validateInt(prop.getInt(prefix+"channel", channel),0,15,channel);
		curve = Utils.validateInt(prop.getInt(prefix+"curve", curve),0,15,curve);
		threshold = Utils.validateInt(prop.getInt(prefix+"threshold", threshold),0,127,threshold);
		retrigger = Utils.validateInt(prop.getInt(prefix+"retrigger", retrigger),0,127,retrigger);
		if (retrigger < 1) retrigger = 1;
		levelMax = Utils.validateInt(prop.getInt(prefix+"levelMax", levelMax),64,1023,levelMax);
		minScan = Utils.validateInt(prop.getInt(prefix+"minScan", minScan),10,100,minScan);
		type = prop.getBoolean(prefix+"type", type);		
		autoLevel = prop.getBoolean(prefix+"autoLevel", autoLevel);
		dual = prop.getBoolean(prefix+"dual", dual);
		threeWay = prop.getBoolean(prefix+"threeWay", threeWay);
		function = Utils.validateInt(prop.getInt(prefix+"function", function),0,2,function);
		gain = Utils.validateInt(prop.getInt(prefix+"gain", gain),0,8,gain);
		xtalkLevel = Utils.validateInt(prop.getInt(prefix+"xtalkLevel", xtalkLevel),0,7,xtalkLevel);
		xtalkGroup = Utils.validateInt(prop.getInt(prefix+"xtalkGroup", xtalkGroup),0,7,xtalkGroup);
		dynTime = Utils.validateInt(prop.getInt(prefix+"dynTime", dynTime),0,15,dynTime);
		dynLevel = Utils.validateInt(prop.getInt(prefix+"dynLevel", dynLevel),0,15,dynLevel);
		compression = Utils.validateInt(prop.getInt(prefix+"compression", compression),0,7,compression);
		shift = Utils.validateInt(prop.getInt(prefix+"shift", shift),0,7,shift);
		name = Utils.validateInt(prop.getInt(prefix+"name", name),0,127,name);
		altNote = Utils.validateInt(prop.getInt(prefix+"altNote", altNote),0,127,altNote);
		pressrollNote = Utils.validateInt(prop.getInt(prefix+"pressrollNote", pressrollNote),0,127,pressrollNote);
		altNote_linked = prop.getBoolean(prefix+"altNote_linked", altNote_linked);
		pressrollNote_linked = prop.getBoolean(prefix+"pressrollNote_linked", pressrollNote_linked);				
	}
	
	public void setTypeInt(int typeInt) {
		if (leftInput) {
			type = false;
			switch (typeInt) {
			case 1:
				dual = true;
				threeWay = false;
				break;
			case 2:
				dual = false;
				threeWay = true;
				break;
			default:
				dual = false;
				threeWay = false;
				break;
			}
		} else {
			if (typeInt == 0) {
				type = false;
			} else {
				type = true;
			}
		}			

	}
	
	public int getTypeInt() {
		int result = 0;
		if (leftInput) {
			if (dual) {
				result = 1;
			} else if (threeWay) {
				result = 2;
			} else {
				result = 0;
			}
		} else {
			if (type) {
				result = 1;
			} else {
				result = 0;
			}
		}
		return result;
	}
	
	public void setValueById(int valueId, int value) {
		switch (valueId) {
		case Constants.INPUT_VALUE_ID_DISABLED:
			disabled = (value > 0);
			break;
		case Constants.INPUT_VALUE_ID_NAME:
			name = value;
			break;
		case Constants.INPUT_VALUE_ID_NOTE:
			note = value;
			break;
		case Constants.INPUT_VALUE_ID_ALT_NOTE:
			altNote = value;
			break;
		case Constants.INPUT_VALUE_ID_PRESSROLL_NOTE:
			pressrollNote = value;
			break;
		case Constants.INPUT_VALUE_ID_CHANNEL:
			channel = value;
			break;
		case Constants.INPUT_VALUE_ID_FUNCTION:
			function = value;
			break;
		case Constants.INPUT_VALUE_ID_CURVE:
			curve = value;
			break;
		case Constants.INPUT_VALUE_ID_COMPRESSION:
			compression = value;
			break;
		case Constants.INPUT_VALUE_ID_SHIFT:
			shift = value;
			break;
		case Constants.INPUT_VALUE_ID_XTALK_LEVEL:
			xtalkLevel = value;
			break;
		case Constants.INPUT_VALUE_ID_XTALK_GROUP:
			xtalkGroup = value;
			break;
		case Constants.INPUT_VALUE_ID_THRESHOLD:
			threshold = value;
			break;
		case Constants.INPUT_VALUE_ID_GAIN:
			gain = value;
			break;
		case Constants.INPUT_VALUE_ID_HIGHLEVEL_AUTO:
			autoLevel = (value > 0);
			break;
		case Constants.INPUT_VALUE_ID_HIGHLEVEL:
			levelMax = value;
			break;
		case Constants.INPUT_VALUE_ID_RETRIGGER:
			retrigger = value;
			break;
		case Constants.INPUT_VALUE_ID_DYN_LEVEL:
			dynLevel = value;
			break;
		case Constants.INPUT_VALUE_ID_DYN_TIME:
			dynTime = value;
			break;
		case Constants.INPUT_VALUE_ID_MINSCAN:
			minScan = value;
			break;
		case Constants.INPUT_VALUE_ID_TYPE:
			setTypeInt(value);
			break;
		default:
			break;
		}
	}
	
	public int getValueById(int valueId) {
		int value = -1;
		switch (valueId) {
		case Constants.INPUT_VALUE_ID_DISABLED:
			value = disabled?1:0;
			break;
		case Constants.INPUT_VALUE_ID_NAME:
			value = name;
			break;
		case Constants.INPUT_VALUE_ID_NOTE:
			value = note;
			break;
		case Constants.INPUT_VALUE_ID_ALT_NOTE:
			value = altNote;
			break;
		case Constants.INPUT_VALUE_ID_PRESSROLL_NOTE:
			value = pressrollNote;
			break;
		case Constants.INPUT_VALUE_ID_CHANNEL:
			value = channel;
			break;
		case Constants.INPUT_VALUE_ID_FUNCTION:
			value = function;
			break;
		case Constants.INPUT_VALUE_ID_CURVE:
			value = curve;
			break;
		case Constants.INPUT_VALUE_ID_COMPRESSION:
			value = compression;
			break;
		case Constants.INPUT_VALUE_ID_SHIFT:
			value = shift;
			break;
		case Constants.INPUT_VALUE_ID_XTALK_LEVEL:
			value = xtalkLevel;
			break;
		case Constants.INPUT_VALUE_ID_XTALK_GROUP:
			value = xtalkGroup;
			break;
		case Constants.INPUT_VALUE_ID_THRESHOLD:
			value = threshold;
			break;
		case Constants.INPUT_VALUE_ID_GAIN:
			value = gain;
			break;
		case Constants.INPUT_VALUE_ID_HIGHLEVEL_AUTO:
			value = autoLevel?1:0;
			break;
		case Constants.INPUT_VALUE_ID_HIGHLEVEL:
			value = levelMax;
			break;
		case Constants.INPUT_VALUE_ID_RETRIGGER:
			value = retrigger;
			break;
		case Constants.INPUT_VALUE_ID_DYN_LEVEL:
			value = dynLevel;
			break;
		case Constants.INPUT_VALUE_ID_DYN_TIME:
			value = dynTime;
			break;
		case Constants.INPUT_VALUE_ID_MINSCAN:
			value = minScan;
			break;
		case Constants.INPUT_VALUE_ID_TYPE:
			value = getTypeInt();
			break;
		default:
			break;
		}
		return value;
	}
	
	public byte[] getSysexFromConfig() {
		byte [] sysex_byte = new byte[2];
		byte [] sysex_short = new byte[4];
		byte [] sysex = new byte[Constants.MD_SYSEX_PAD_SIZE];	
		byte flags;
		int i = 0;

		sysex[i++] = Constants.SYSEX_START;
		sysex[i++] = Constants.MD_SYSEX;
		sysex[i++] = 0; //(byte)chainId; 
		sysex[i++] = Constants.MD_SYSEX_PAD;
		sysex[i++] = (byte)(id + 1);
		
		sysex_byte = Utils.byte2sysex((byte)(note|(disabled?0x80:0)));
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = Utils.byte2sysex((byte)((channel<<4)|(curve)));
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = Utils.byte2sysex((byte)threshold);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = Utils.byte2sysex((byte)retrigger);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_short = Utils.short2sysex((short)levelMax);
		sysex[i++] = sysex_short[0];
		sysex[i++] = sysex_short[1];
		sysex[i++] = sysex_short[2];
		sysex[i++] = sysex_short[3];
		sysex_byte = Utils.byte2sysex((byte)minScan);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		flags = (byte) (((type)?1:0)|(((autoLevel)?1:0)<<1)|(((dual)?1:0)<<2)|(((threeWay)?1:0)<<3)
				|(gain<<4));
		sysex_byte = Utils.byte2sysex(flags);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = Utils.byte2sysex((byte)((xtalkGroup<<3)|(xtalkLevel)));
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = Utils.byte2sysex((byte)((dynLevel<<4)|(dynTime)));
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = Utils.byte2sysex((byte)(((function)<<6)|(shift<<3)|(compression)));
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];		
		sysex_byte = Utils.byte2sysex((byte)name);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = Utils.byte2sysex((byte)pressrollNote);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = Utils.byte2sysex((byte)altNote);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex[i++] = Constants.SYSEX_END;
		return sysex;
	}

	public void setConfigFromSysex(byte [] sysex) {
		byte [] sysex_byte = new byte[2];
		byte [] sysex_short = new byte[4];
		byte flags;
		int i = 5;

		if (sysex.length >= Constants.MD_SYSEX_PAD_SIZE) {
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			disabled = ((Utils.sysex2byte(sysex_byte)&0x80)>0);
			note = (Utils.sysex2byte(sysex_byte)&0x7f);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			flags = Utils.sysex2byte(sysex_byte);
			curve = (flags&0x0f);
			channel = ((flags&0xf0)>>4);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			threshold = Utils.sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			retrigger = Utils.sysex2byte(sysex_byte);
			if (retrigger <1 ) retrigger = 1;
			sysex_short[0] = sysex[i++];
			sysex_short[1] = sysex[i++];
			sysex_short[2] = sysex[i++];
			sysex_short[3] = sysex[i++];
			levelMax = Utils.sysex2short(sysex_short);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			minScan = Utils.sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			flags = Utils.sysex2byte(sysex_byte);
			gain = ((flags&0xf0)>>4);
			type = ((flags&1) != 0);
			autoLevel = ((flags&(1<<1)) != 0);
			dual = ((flags&(1<<2)) != 0);
			threeWay = ((flags&(1<<3)) != 0);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			flags = Utils.sysex2byte(sysex_byte);
			xtalkGroup = ((flags&0x38)>>3);
			xtalkLevel = (flags&0x07);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			flags = Utils.sysex2byte(sysex_byte);
			dynTime = (flags&0x0f);
			dynLevel = ((flags&0xf0)>>4);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			flags = Utils.sysex2byte(sysex_byte);
			shift = ((flags&0x38)>>3);
			compression = (flags&0x07);
			function = ((flags&0xc0)>>6);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			name = Utils.sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			pressrollNote = Utils.sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			altNote = Utils.sysex2byte(sysex_byte);
		}
	}


}
