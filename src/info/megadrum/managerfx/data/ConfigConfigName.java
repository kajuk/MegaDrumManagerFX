package info.megadrum.managerfx.data;

import info.megadrum.managerfx.utils.Constants;
import info.megadrum.managerfx.utils.Utils;

public class ConfigConfigName {
	public String name = "            ";
	public boolean sysexReceived = false;

	public ConfigConfigName() {
	}

	public byte[] getSysexFromConfig(int chainId, int nameId) {
		byte [] sysex_byte = new byte[2];
		byte [] sysex = new byte[Constants.MD_SYSEX_CONFIG_NAME_SIZE];
		String nameString;
		byte [] nameBytes;
		int i = 0;
		sysex[i++] = Constants.SYSEX_START;
		sysex[i++] = Constants.MD_SYSEX;
		sysex[i++] = (byte) chainId;
		sysex[i++] = Constants.MD_SYSEX_CONFIG_NAME;
		sysex[i++] = (byte)nameId;
		
		nameString = name + "            ";
		nameString = nameString.substring(0, 12);
		nameBytes = nameString.getBytes();
		for (int p = 0; p < 12;p++) {
			sysex_byte = Utils.byte2sysex(nameBytes[p]);
			sysex[i++] = sysex_byte[0];
			sysex[i++] = sysex_byte[1];
		}
		sysex[i++] = Constants.SYSEX_END;
		return sysex;
	}

	public void setConfigFromSysex(byte [] sysex) {
		byte [] sysex_byte = new byte[2];
		char [] bytes_string = { 1, 2, 3, 4, 5, 6, 7, 8 , 9, 10, 11, 12};
		int i = 5;
		if (sysex.length >= Constants.MD_SYSEX_CUSTOM_NAME_SIZE) {
			for (int p = 0; p < 12;p++) {
				sysex_byte[0] = sysex[i++];
				sysex_byte[1] = sysex[i++];
				bytes_string[p] = (char)Utils.sysex2byte(sysex_byte);
			}
			name = String.copyValueOf(bytes_string).trim();
		}
	}


}
