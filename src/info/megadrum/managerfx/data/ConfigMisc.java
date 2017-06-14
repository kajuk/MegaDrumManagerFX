package info.megadrum.managerfx.data;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.PropertiesConfigurationLayout;

import info.megadrum.managerfx.utils.Constants;
import info.megadrum.managerfx.utils.Utils;

public class ConfigMisc {
	
	public boolean changed; 
	
	private int note_off = 20;
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

	public void setNoteOff(int value) {
		note_off = value;
	}
	public int getNoteOff() {
		return note_off;
	}
	
	public void setConfigFromSysex(byte [] sysex) {
		byte [] sysex_byte = new byte[2];
		byte [] sysex_short = new byte[4];
		short flags;
		int i = 4;
		//System.out.printf("sysex_byte size: %d\n", sysex_byte.length);
		//System.out.printf("sx size: %d\n", sx.length);
		if (sysex.length >= Constants.MD_SYSEX_MISC_SIZE) {
			sysex_short[0] = sysex[i++];
			sysex_short[1] = sysex[i++];
			sysex_short[2] = 0;
			sysex_short[3] = 0;
			setNoteOff(Utils.sysex2short(sysex_short));
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			latency = Utils.sysex2byte(sysex_byte);
			sysex_short[0] = sysex[i++];
			sysex_short[1] = sysex[i++];
			sysex_short[2] = sysex[i++];
			sysex_short[3] = sysex[i++];
			flags = Utils.sysex2short(sysex_short);
			sysex_short[0] = sysex[i++];
			sysex_short[1] = sysex[i++];
			sysex_short[2] = 0;
			sysex_short[3] = 0;
			pressroll = Utils.sysex2short(sysex_short);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			octave_shift = Utils.sysex2byte(sysex_byte);
			
			
			all_gains_low = ((flags&1) != 0);
			alt_note_choking = ((flags&(1<<1)) != 0);
			big_vu_meter = ((flags&(1<<2)) != 0);
			quick_access = ((flags&(1<<3)) != 0);
			big_vu_split = ((flags&(1<<4)) != 0);
			alt_false_tr_supp = ((flags&(1<<5)) != 0);
			inputs_priority = ((flags&(1<<6)) != 0);
			midi_thru = ((flags&(1<<8)) != 0);
			send_triggered_in = ((flags&(1<<11)) != 0);
		}
	}

	public byte[] getSysexFromConfig(int chainId) {
		byte [] sysex_byte = new byte[2];
		byte [] sysex_short = new byte[4];
		byte [] sysex = new byte[Constants.MD_SYSEX_MISC_SIZE];
		short flags;
		int i = 0;
		
		flags = (short) (((all_gains_low)?1:0)|
				(((alt_note_choking)?1:0)<<1)|(((big_vu_meter)?1:0)<<2)
				|(((quick_access)?1:0)<<3)|(((big_vu_split)?1:0)<<4)
				|(((alt_false_tr_supp)?1:0)<<5)|(((inputs_priority)?1:0)<<6)
				|(((midi_thru)?1:0)<<8)|(((send_triggered_in)?1:0)<<11));
		sysex[i++] = Constants.SYSEX_START;
		sysex[i++] = Constants.MD_SYSEX;
		sysex[i++] = (byte) chainId;
		sysex[i++] = Constants.MD_SYSEX_MISC;
		sysex_byte = Utils.byte2sysex((byte)getNoteOff());
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = Utils.byte2sysex((byte)latency);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_short = Utils.short2sysex(flags);
		sysex[i++] = sysex_short[0];
		sysex[i++] = sysex_short[1];
		sysex[i++] = sysex_short[2];
		sysex[i++] = sysex_short[3];
		sysex_byte = Utils.byte2sysex((byte)pressroll);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = Utils.byte2sysex((byte)octave_shift);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex[i++] = Constants.SYSEX_END;
		return sysex;
	}
	

}
