package info.megadrum.managerfx.data;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.PropertiesConfigurationLayout;

import info.megadrum.managerfx.utils.Constants;
import info.megadrum.managerfx.utils.Utils;

public class ConfigCurve {
	public int [] yValues = {2, 32, 64, 96, 128, 160, 192, 224, 255};
	public int syncState = Constants.SYNC_STATE_UNKNOWN;
	public boolean sysexReceived = false;

	public ConfigCurve() {
	}

	public void copyToPropertiesConfiguration(PropertiesConfiguration prop, PropertiesConfigurationLayout layout, String prefix, Integer id) {
		id++;
		Integer c;
		prefix = prefix+"["+id.toString()+"].";
		layout.setComment(prefix+"P1", "\n#Curve "+id.toString() + " settings");
		for (Integer i = 0; i<yValues.length;i++) {
			c = i+1;
			prop.setProperty(prefix+"P"+c.toString(), yValues[i]);		
		}
	}

	public void copyFromPropertiesConfiguration(PropertiesConfiguration prop, String prefix, Integer id) {
		id++;
		Integer c;
		prefix = prefix+"["+id.toString()+"].";
		for (Integer i = 0; i<yValues.length;i++) {
			c = i+1;
			yValues[i] = Utils.validateInt(prop.getInt(prefix+"P"+c.toString(), yValues[i]),2,255,yValues[i]);		
		}
	}	

	public byte[] getSysexFromConfig(int chainId, int curveId) {
		byte [] sysex_byte = new byte[2];
		byte [] sysex = new byte[Constants.MD_SYSEX_CURVE_SIZE];
		int i = 0;
		sysex[i++] = Constants.SYSEX_START;
		sysex[i++] = Constants.MD_SYSEX;
		sysex[i++] = (byte) chainId;
		sysex[i++] = Constants.MD_SYSEX_CURVE;
		sysex[i++] = (byte)curveId;

		for (int p = 0; p < 9;p++) {
			sysex_byte = Utils.byte2sysex((byte)yValues[p]);
			sysex[i++] = sysex_byte[0];
			sysex[i++] = sysex_byte[1];
		}
		sysex[i++] = Constants.SYSEX_END;
		return sysex;
	}

	public void setConfigFromSysex(byte [] sysex) {
		byte [] sysex_byte = new byte[2];
		int i = 5;
		if (sysex.length >= Constants.MD_SYSEX_CURVE_SIZE) {
			for (int p = 0; p < 9;p++) {
				sysex_byte[0] = sysex[i++];
				sysex_byte[1] = sysex[i++];
				yValues[p] = Utils.sysex2byte(sysex_byte);
				if (yValues[p]<0) {
					yValues[p] += 256;
				}
			}
		}
		
	}


}
