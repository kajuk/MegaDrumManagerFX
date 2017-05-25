package info.megadrum.managerfx.data;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.PropertiesConfigurationLayout;

import info.megadrum.managerfx.utils.Constants;
import info.megadrum.managerfx.utils.Utils;

public class ConfigPad {
	public int note = 0;
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
	public boolean inputDisabled = false;
	public int syncState = Constants.SYNC_STATE_UNKNOWN;
	public boolean sysexReceived = false;

	
	public ConfigPad (){
	}
	
	public void copyToPropertiesConfiguration(PropertiesConfiguration prop, PropertiesConfigurationLayout layout, String prefix, Integer id) {
		id++;
		prefix = prefix+"["+id.toString()+"].";
		layout.setComment(prefix+"note", "\n#Input "+id.toString()+" settings");
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
	
	public int getIntType() {
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
}
