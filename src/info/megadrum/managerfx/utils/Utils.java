package info.megadrum.managerfx.utils;

import info.megadrum.managerfx.data.Config3rd;
import info.megadrum.managerfx.data.ConfigConfigName;
import info.megadrum.managerfx.data.ConfigCurve;
import info.megadrum.managerfx.data.ConfigCustomName;
import info.megadrum.managerfx.data.ConfigGlobalMisc;
import info.megadrum.managerfx.data.ConfigMisc;
import info.megadrum.managerfx.data.ConfigPad;
import info.megadrum.managerfx.data.ConfigPedal;
import info.megadrum.managerfx.data.ConfigPositional;

public class Utils {

	public static void delayMs(int ms) {
	    try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			Utils.show_error("Unrecoverable timer error. Exiting.\n" +
					"(" + e.getMessage() + ")");
			System.exit(1);
		}
		
	}

	public static void show_error(String msg) {
		System.out.printf("Utils.show_error -> %s\n",msg);
/*		JOptionPane.showMessageDialog(null,
			    msg,
			    "Error",
			    JOptionPane.ERROR_MESSAGE);
*/
	}
	public static byte [] byte2sysex (byte b) {
		byte [] result = new byte[2];

		result[0] = (byte)((b&0xf0)>>4);
		result[1] = (byte)(b&0x0f);
		return result;
	}

	public static byte [] short2sysex (short s) {
		byte [] result = new byte[4];

		result[0] = (byte)((s&0x00f0)>>4);
		result[1] = (byte)(s&0x000f);
		result[2] = (byte)((s&0xf000)>>12);
		result[3] = (byte)((s&0x0f00)>>8);
		return result;
	}
	
	public static byte sysex2byte (byte [] sx) {
		byte result;
		
		result = (byte)(((sx[0]&0x0f)<<4)|(sx[1]&0x0f));
		return result;
	}

	public static short sysex2short (byte [] sx) {
		short result;
		
		result = (short)(((sx[0]&0x0f)<<4)|(sx[1]&0x0f)|((sx[2]&0x0f)<<12)|((sx[3]&0x0f)<<8));
		return result;
	}

	public static void copyConfig3rdToSysex(Config3rd config, byte [] sysex, int chainId, int padId) {
		byte [] sysex_byte = new byte[2];
		int i = 0;

		sysex[i++] = Constants.SYSEX_START;
		sysex[i++] = Constants.MD_SYSEX;
		sysex[i++] = (byte)chainId;
		sysex[i++] = Constants.MD_SYSEX_3RD;
		sysex[i++] = (byte)padId;
		
		sysex_byte = byte2sysex((byte)config.note);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.threshold);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.pressrollNote);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.altNote);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex_byte = byte2sysex((byte)config.dampenedNote);
		sysex[i++] = sysex_byte[0];
		sysex[i++] = sysex_byte[1];
		sysex[i++] = Constants.SYSEX_END;		
	}
	
	public static void copySysexToConfig3rd(byte [] sysex, Config3rd config) {
		byte [] sysex_byte = new byte[2];
		int i = 5;
		if (sysex.length >= Constants.MD_SYSEX_3RD_SIZE) {
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.note = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.threshold = (int)(sysex2byte(sysex_byte)&0xff);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.pressrollNote = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.altNote = sysex2byte(sysex_byte);
			sysex_byte[0] = sysex[i++];
			sysex_byte[1] = sysex[i++];
			config.dampenedNote = sysex2byte(sysex_byte);
		}
		
	}

	public static int validateInt(int value, int min, int max, int fallBack){
		//returns value if min <= value <= max
		//otherwise returns fallBack
		if ((value>=min) && (value<=max)) {
			return value;
		} else {
			return fallBack;
		}
	}
	
	public static Double validateDouble(Double value, Double min, Double max, Double fallBack){
		//returns value if min <= value <= max
		//otherwise returns fallBack
		if ((value>=min) && (value<=max)) {
			return value;
		} else {
			return fallBack;
		}
	}
	
	public static short validateShort(short value, int min, int max, short fallBack){
		//returns value if min <= value <= max
		//otherwise returns fallBack
		if ((value>=min) && (value<=max)) {
			return value;
		} else {
			return fallBack;
		}
	}

}
