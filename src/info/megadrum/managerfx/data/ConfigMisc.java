package info.megadrum.managerfx.data;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.PropertiesConfigurationLayout;

import info.megadrum.managerfx.utils.Constants;
import info.megadrum.managerfx.utils.Utils;

public class ConfigMisc {
	
	public boolean changed; 
	
	public int note_off = 20;
	public int latency = 40;
	public int pressroll = 0;
	public int octave_shift = 2;
	public boolean all_gains_low = false;
	public boolean big_vu_meter = false;
	public boolean big_vu_split = false;
	public boolean quick_access = false;
	public boolean alt_false_tr_supp = false;
	public boolean inputs_priority = false;
	public boolean midi_thru = false;
	public boolean send_triggered_in = false;
	public boolean alt_note_choking = false;
	public int syncState = Constants.SYNC_STATE_UNKNOWN;
	public boolean sysexReceived = false;

	public ConfigMisc (){
	}
	
	public void copyToPropertiesConfiguration(PropertiesConfiguration prop, PropertiesConfigurationLayout layout, String prefix) {
		layout.setComment(prefix+"note_off", "\n#Misc settings");
		prop.setProperty(prefix+"note_off", note_off);
		prop.setProperty(prefix+"latency", latency);
		prop.setProperty(prefix+"pressroll", pressroll);
		prop.setProperty(prefix+"octave_shift", octave_shift);
		prop.setProperty(prefix+"all_gains_low", all_gains_low);
		prop.setProperty(prefix+"big_vu_meter", big_vu_meter);
		prop.setProperty(prefix+"big_vu_split", big_vu_split);
		prop.setProperty(prefix+"quick_access", quick_access);
		prop.setProperty(prefix+"alt_false_tr_supp", alt_false_tr_supp);
		prop.setProperty(prefix+"inputs_priority", inputs_priority);
		prop.setProperty(prefix+"midi_thru", midi_thru);
		prop.setProperty(prefix+"send_triggered_in", send_triggered_in);
		prop.setProperty(prefix+"alt_note_choking", alt_note_choking);
	}

	public void copyFromPropertiesConfiguration(PropertiesConfiguration prop, String prefix) {
		note_off = Utils.validateInt(prop.getInt(prefix+"note_off", note_off),2,200,note_off);
		latency = Utils.validateInt(prop.getInt(prefix+"latency", latency),10,100,latency);
		pressroll = Utils.validateInt(prop.getInt(prefix+"pressroll", pressroll),0,note_off,pressroll);
		octave_shift = Utils.validateInt(prop.getInt(prefix+"octave_shift", octave_shift),0,4,octave_shift);
		all_gains_low = prop.getBoolean(prefix+"all_gains_low", all_gains_low);
		big_vu_meter = prop.getBoolean(prefix+"big_vu_meter", big_vu_meter);
		big_vu_split = prop.getBoolean(prefix+"big_vu_split", big_vu_split);
		quick_access = prop.getBoolean(prefix+"quick_access", quick_access);
		alt_false_tr_supp = prop.getBoolean(prefix+"alt_false_tr_supp", alt_false_tr_supp);
		inputs_priority = prop.getBoolean(prefix+"inputs_priority", inputs_priority);
		midi_thru = prop.getBoolean(prefix+"midi_thru", midi_thru);
		send_triggered_in = prop.getBoolean(prefix+"send_triggered_in", send_triggered_in);
		alt_note_choking = prop.getBoolean(prefix+"alt_note_choking", alt_note_choking);
	}

}
