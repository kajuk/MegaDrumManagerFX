package info.megadrum.managerfx.utils;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.functors.IfClosure;

import javafx.scene.paint.Color;

public interface Constants {
	public static final String WINDOWS_TITLE = "MegaDrumManager FX";
	public static final String WINDOWS_TITLE_SHORT = "MDM FX: ";
	public static final String MD_VERSION = "20170511";
	public static final int MD_MINIMUM_VERSION = 20170413;
	public static final String WARNING_VERSION = "<html><font size=2>For full compatibilty between MegaDrum Manager and MegaDrum</font></html>\n" +
			"<html><font size=2>you should upgrade MegaDrum to version " + MD_MINIMUM_VERSION + " or newer</font></html>";
	public static final Double MD_CONFIG_VERSION = 0.5;
	public static final int MIN_INPUTS = 17;
	public static final int MAX_INPUTS = 55;
	public static final int CONFIGS_COUNT = 32;
	public static final int PANEL_HIDE = 0;
	public static final int PANEL_SHOW = 1;
	public static final int PANEL_DETACH = 2;
	public static final int PANELS_COUNT = 5;
	public static final String[] PANELS_NAMES = { "Misc", "Pedal", "Pads", "Pads Extra", "MIDI Log"};
	public static final int MCU_TYPE_UNKNOW = 0;
	public static final int MCU_TYPE_Atmega644 = 1;
	public static final int MCU_TYPE_Atmega1284 = 2;
	public static final int MCU_TYPE_STM32F103VBT6 = 3;
	public static final int MCU_TYPE_STM32F103RBT6 = 4;
	public static final int MCU_TYPE_STM32F103RCT6 = 5;
	public static final int MCU_TYPE_STM32F205RBT6 = 6;
	public static final int MCU_TYPE_STM32F205RCT6 = 7;
	public static final int MCU_TYPE_STM32F205LITE = 8;
	public static final int MCU_TYPE_STM32F205TEST1 = 9;
	public static final int MCU_TYPE_STM32TEST2 = 10;
	public static final int MCU_TYPE_STM32TEST3 = 11;
	public static final int MCU_TYPE_STM32TEST4 = 12;
	public static final String[] MCU_TYPES = { "Unknown", "Atmega644", "Atmega1284", "STM32F103VBT6",
											"STM32F103RBT6", "STM32F103RCT6", "STM32F205RBT6", "STM32F205RCT6",
											"STM32F205LITE", "STM32F205TEST1", "STM32TEST2", "STM32TEST3",
											"STM32TEST4"
											};
	public static final int Error_NoResponse = 0x00;
	public static final int Error_OK = 0x11;
	public static final int Error_CRC = 0x22;
	public static final int Error_Read = 0x23;
	public static final String MD_MANAGER_CONFIG = System.getProperty("user.home") + System.getProperty("file.separator") + "megadrummanagerfx.cfg";
	public static final int PADS_COUNT = 55;
	public static final int CURVES_COUNT = 16;
	public static final int CUSTOM_NAMES_MAX = 32;
	public static final int CONFIG_NAMES_MAX = 127;
	public static final int SYSEX_DELAY = 50;
	public static final byte SYSEX_START = (byte)0xf0;
	public static final byte SYSEX_END = (byte)0xf7;
	public static final byte MD_SYSEX = (byte)0x70;
	public static final byte MD_SYSEX_MISC = (byte)0x01;
	public static final byte MD_SYSEX_MISC_SIZE = 17;
	public static final byte MD_SYSEX_PEDAL = (byte)0x02;
	public static final byte MD_SYSEX_PEDAL_SIZE_OLD = 79;
	public static final byte MD_SYSEX_PEDAL_SIZE = 95;
	public static final byte MD_SYSEX_PAD = (byte)0x03;
	public static final byte MD_SYSEX_PAD_SIZE = 34;
	public static final byte MD_SYSEX_3RD = (byte)0x04;
	public static final byte MD_SYSEX_3RD_SIZE = 16;
	public static final byte MD_SYSEX_VERSION = (byte)0x05;
	public static final byte MD_SYSEX_VERSION_SIZE = 13;
	public static final byte MD_SYSEX_CURVE = (byte)0x06;
	public static final byte MD_SYSEX_CURVE_SIZE = 24;
	public static final byte MD_SYSEX_POS = (byte)0x07;
	public static final byte MD_SYSEX_POS_SIZE = 12;
	public static final byte MD_SYSEX_CUSTOM_NAME = (byte)0x08;
	public static final byte MD_SYSEX_CUSTOM_NAME_SIZE = 22;
	public static final byte MD_SYSEX_GLOBAL_MISC = (byte)0x09;
	public static final byte MD_SYSEX_GLOBAL_MISC_SIZE = 13;
	public static final byte MD_SYSEX_BOOTLOADER = (byte)0x0b;
	public static final byte MD_SYSEX_MCU_TYPE = (byte)0x0c;
	public static final byte MD_SYSEX_MCU_TYPE_SIZE = 7;
	public static final byte MD_SYSEX_CONFIG_NAME = (byte)0x0d;
	public static final byte MD_SYSEX_CONFIG_NAME_SIZE = 30;
	public static final byte MD_SYSEX_CONFIG_COUNT = (byte)0x0e;
	public static final byte MD_SYSEX_CONFIG_COUNT_SIZE = 6;
	public static final byte MD_SYSEX_CONFIG_SAVE = (byte)0x0f;
	public static final byte MD_SYSEX_CONFIG_SAVE_SIZE = 6;
	public static final byte MD_SYSEX_CONFIG_CURRENT = (byte)0x10;
	public static final byte MD_SYSEX_CONFIG_CURRENT_SIZE = 6;
	public static final byte MD_SYSEX_CONFIG_LOAD = (byte)0x11;
	public static final byte MD_SYSEX_CONFIG_LOAD_SIZE = 6;
	public static final String[] MD_SYSEX_NAMES = {
			"Undefined", "Misc","Pedal","Input","3rd Zone","Version","Curve","Positional", 	//0-7
			"Custom Name","Global Misc", "Undefined", "Bootloader","MCU Type","Config Name","Config Count","Config Save",	//8-f
			"Config Current", "Config Load"							//10-
	};
	
	public static final int MD_SYSEX_STATUS_OK = 0;
	public static final int MD_SYSEX_STATUS_WORKING = 1;
	public static final int MD_SYSEX_STATUS_TIMEOUT = 2;
	public static final int MD_SYSEX_STATUS_MISMATCH = 3;
	public static final int MD_SYSEX_STATUS_MIDI_IS_NOT_OPEN = 4;
	public static final int MD_SYSEX_STATUS_MIDI_INIT_ERROR = 5;
	public static final String[] MD_SYSEX_STATUS_NAMES = {
		"Sysex: OK", "Sysex: Working", "Timout", "Mismatch", "MIDI is NOT Open", "MIDI Init Error"
	};
	
	public static final String[] CURVES_LIST = { "LinearCustom1", "Log1Custom2", "Log2Custom3", "Log3Custom4", "Log4Custom5",
		"Exp1Custom6", "Exp2Custom7", "S1Custom8", "S2Custom9", "Strong1Custom10", "Strong2Custom11", "MaxCustom12",
		"Custom13", "Custom14", "Custom15", "Custom16" };
	public static final String[] PADS_NAMES_LIST = { "Kick ", "HiHatB",
			"HiHatE", "SnareH", "SnareR", "RideB", "RideE", "CrashB", "CrashE",
			"Tom1H", "Tom1R", "Tom2H", "Tom2R", "Tom3H", "Tom3R", "Tom4H",
			"Tom4R", "Aux1H", "Aux1R", "Aux2H", "Aux2R", "Aux3H", "Aux3R",
			"Aux4H", "Aux4R", "Aux5H", "Aux5R", "Aux6H", "Aux6R", "Aux7H",
			"Aux7R", "Aux8H", "Aux8R", "Aux9H", "Aux9R", "Aux10H", "Aux10R",
			"Aux11H", "Aux11R", "Aux12H", "Aux12R", "Aux13H", "Aux13R",
			"Aux14H", "Aux14R", "Aux15H", "Aux15R", "Aux16H", "Aux16R",
			"Aux17H", "Aux17R", "Aux18H", "Aux18R", "Aux19H", "Aux19R" };
	public static final String[] CUSTOM_PADS_NAMES_LIST = { "Kick", "HiHat",
			"Snare1", "Snare2", "Ride1", "Ride2", "Crash1", "Crash2", "Tom1",
			"Tom2", "Tom3", "Tom4", "Tom5", "Tom6", "Chinese1", "Chinese2",
			"Tambrn1", "Tambrn2", "Cowbell1", "Cowbell2", "Bongo1", "Bongo2",
			"Conga1", "Conga2", "Conga3", "Timbale1", "Timbale2", "Agogo1",
			"Agogo2", "Claves1", "Claves2", "Wood1", "Wood2", "Cuica1",
			"Cuica2", "Triangl1", "Triangl2", "Bass1", "Bass2", "Sizzle1",
			"Sizzle2", "Splash1", "Splash2", "Swish1", "Swish2", "Clash1",
			"Clash2", "Chenda1", "Chenda2", "Tenor1", "Tenor2", "Timpani1",
			"Timpani2", "Timpani3", "Timpani4",	"Crash3", "Crash4", "Crash5",
			"Splash3", "Splash4", "Splash5", "RotoTom1", "RotoTom2", "RotoTom3",
			"Sticks1", "Sticks2", "Sticks3", "HndClap1", "HndClap2", "HndClap3",
			"FngrSnap", "Mtronome" 
	};
	public static final String MIDI_NOTE_ON_COLOR		= "#007f00";
	public static final String MIDI_NOTE_OFF_COLOR		= "#2fbf2f";
	public static final String MIDI_AFTERTOUCH_COLOR	= "#007f7f";
	public static final String MIDI_CC_COLOR			= "#7f7f00";
	public static final String MIDI_PC_COLOR			= "#7f007f";
	public static final String MIDI_CH_PR_COLOR			= "#00007f";
	public static final String MIDI_PITCH_COLOR			= "#7f0000";
	
	public static final int SYNC_STATE_UNKNOWN				= 0;
	public static final Color SYNC_STATE_UNKNOWN_COLOR		= Color.BLUE;
	public static final int SYNC_STATE_SYNCED				= 1;
	public static final Color SYNC_STATE_SYNCED_COLOR		= Color.BLACK;
	public static final int SYNC_STATE_NOT_SYNCED			= 2;
	public static final Color SYNC_STATE_NOT_SYNCED_COLOR	= Color.RED;
	public static final int SYNC_STATE_RECEIVED				= 3;
	public static final Color SYNC_STATE_RECEIVED_COLOR		= Color.ORANGE;
	
	public static final int VALUE_TYPE_BOOLEAN				= 0;
	public static final int VALUE_TYPE_INT					= 1;
	
	public static final String [] NOTES_NAMES = {
		"C ",
		"C#",
		"D ",
		"D#",
		"E ",
		"F ",
		"F#",
		"G ",
		"G#",
		"A ",
		"A#",
		"B "		
	};
	
	public static final Color MD_HEAD_COLOR = Color.BLUE;
	public static final Color MD_RIM_COLOR = Color.GREEN;
	public static final Color MD_3RD_COLOR = Color.ORANGE;
	public static final Color MD_UNKNOWN_COLOR = Color.YELLOW;
	public static final Color MD_HIHAT_COLOR = Color.MAGENTA;
	public static final Color MD_AFTERTOUCH_ON_COLOR = Color.GRAY;
	public static final Color MD_AFTERTOUCH_OFF_COLOR = Color.LIGHTGREY;
	public static final String HELP_ABOUT = "<html><font size=2>MegaDrum Manager</font></html>\n" +
			"<html><font size=2>Version: " + MD_VERSION + "</font></html>\n" +
			"<html><font size=2>www.megadrum.info</font></html>\n"+
			"<html><font size=2>\u00a9 2007-2016 Dmitri Skachkov</font></html>";
	public static final String HELP_ABOUT_MMJ = "\n<html><font size=2>mmj Library is included with MegaDrum Manager for Mac OS X</font></html>\n" +
			"<html><font size=2>mmj is copyright of Humatic</font></html>\n" +
			"<html><font size=2>See www.humatic.de/htools/mmj.htm for details</font></html>";
	public static final String MIDI_PORTS_WARNING =
			"<html><font size=4>Before using MegaDrum Manager</font></html>\n" +
			"<html><font size=4>you first must set MIDI In/Out ports in Main->Options!!!</font></html>";
	public static final String UPGRADE_INSTRUCTION_ATMEGA =
			"Upgrade instruction for:\n" +
			"    Atmega based MegaDrum\n" +
			"    ARM based MegaDrum in 'recovery' mode\n" +
			"\n" +
			"1. Make sure you have a backup copy of your MegaDrum config.\n" +
			"2. Select the MegaDrum firmware file.\n" +
			"3. Disconnect (power off) MegaDrum.\n" +
			"4. While holding MegaDrum's button LEFT connect (power on) MegaDrum.\n" +
			"5. Skip this step on an ARM based MegaDrum.\n" +
			"    By pressing MegaDrum's button UP select a correct MegaDrum crystal frequency.\n" +
			"6. Skip this step on an ARM based MegaDrum.\n" +
			"    Press MegaDrum's button DOWN. MegaDrum LCD will show 'StartUpdateOnPC'.\n" +
			"7. Click button Start.\n" +
			"8. Wait for the upgrade to finish.\n" +
			"9. Click button Close.";
	public static final String UPGRADE_INSTRUCTION_ARM =
			"Upgrade instruction for:\n" +
			"    ARM based MegaDrum\n" +
			"\n" +
			"1. Make sure you have a backup copy of your MegaDrum config.\n" +
			"2. Select the MegaDrum firmware file.\n" +
			"3. Click button Start.\n" +
			"4. Wait for the upgrade to finish.\n" +
			"5. Click button Close.";
	public static final int SYSEX_TIMEOUT_RETRIES = 15; // 8 at least needed to workaround Windows/Java/USB MIDI sysex corruption(bug?)
	public static final String MIDI_IS_NOT_OPEN = "MIDI is not open";
	public static final String SYSEX_OK_TEXT = "Sysex Ok";
	public static final String SYSEX_ERROR_VERIFY_TEXT = "Sysex Verify Error";
	public static final String SYSEX_TIMEOUT_VERSION_TXT = "Version Sysex Timeout";
	public static final String SYSEX_TIMEOUT_MCU_TXT = "MCU Sysex Timeout";
	public static final String SYSEX_TIMEOUT_MISC_TXT = "Misc Sysex Timeout";
	public static final String SYSEX_TIMEOUT_GLOBAL_MISC_TXT = "GlobalMisc Sysex Timeout";
	public static final String SYSEX_TIMEOUT_PEDAL_TXT = "HiHat Pedal Sysex Timeout";
	public static final String SYSEX_TIMEOUT_INPUT_TXT = "Input Sysex Timeout";
	public static final String SYSEX_TIMEOUT_3RD_ZONE_TXT = "3rd Zone Sysex Timeout";
	public static final String SYSEX_TIMEOUT_POS_TXT = "Positional Sysex Timeout";
	public static final String SYSEX_TIMEOUT_CURVE_TXT = "Curve Sysex Timeout";
	public static final String SYSEX_TIMEOUT_CUSTOM_NAME_TXT = "Custom Name Sysex Timeout";
	public static final String SYSEX_TIMEOUT_CONFIG_NAME_TXT = "Config Name Sysex Timeout";
	public static final String SYSEX_TIMEOUT_CONFIG_CURRENT_TXT = "Config Current Sysex Timeout";
	public static final String SYSEX_TIMEOUT_CONFIG_COUNT_TXT = "Config COUNT Sysex Timeout";

	public static final Double FX_TITLEBARS_FONT_MIN_SIZE	= 4.4;
	public static final Double FX_TOOLBARS_FONT_MIN_SIZE	= 4.0;
	public static final Double FX_TITLEBARS_FONT_SCALE		= 0.017;
	public static final Double FX_SUB_TITLEBARS_FONT_SCALE	= 0.015;
	public static final Double FX_TOOLBARS_FONT_SCALE		= 0.012;
	public static final Double FX_TABS_FONT_SCALE			= 0.010;
	public static final Double FX_COMBOBOX_FONT_SCALE		= 0.009;
	public static final Double FX_MISC_CONTROL_WIDTH_MUL	= 1.2;
	public static final Double FX_PEDAL_CONTROL_WIDTH_MUL	= 1.4;
	public static final Double FX_INPUT_CONTROL_WIDTH_MUL	= 1.3;
	public static final Double FX_MISC_LABEL_WIDTH_MUL	= 0.60;
	public static final Double FX_PEDAL_LABEL_WIDTH_MUL	= 0.45;
	public static final Double FX_INPUT_LABEL_WIDTH_MUL	= 0.36;
	public static final int FX_SPINNER_TYPE_STANDARD = 0;	
	public static final int FX_SPINNER_TYPE_SYSEX = 1;

	public static final String[] PAD_FUNCTION_LIST 	= { "Normal", "ProgramChange", "CutoOff"};
	public static final String[] PAD_COMPRESSION_LIST 		= { "0", "1", "2", "3", "4", "5", "6", "7"};
	public static final String[] PAD_LEVEL_SHIFT_LIST 		= { "0", "8", "16", "24", "32", "40", "48", "56"};
	public static final String[] PAD_XTALK_LEVEL_LIST 		= { "0", "1", "2", "3", "4", "5", "6", "7"};
	public static final String[] PAD_XTALK_GROUP_LIST 		= { "0", "1", "2", "3", "4", "5", "6", "7"};
	public static final String[] PAD_GAIN_LIST 		= { "0", "1", "2", "3", "4", "5", "6", "7", "8"};
	public static final String[] PAD_DYN_LEVEL_LIST	= { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};
	public static final String[] PAD_DYN_TIME_LIST	= { "0", "4", "8", "12", "16", "20", "24", "28", "32", "36", "40", "44", "48", "52", "56", "60"};
	public static final String[] PAD_POS_LEVEL_LIST	= { "Disabled", "1", "2", "3" };
	public static final String[] PAD_TYPE_HEAD_LIST	= { "Single Piezo", "Dual or 3way Yamaha", "3way Roland"};
	public static final String[] PAD_TYPE_EDGE_LIST	= { "Piezo", "Switch"};
	public static final int PAD_TYPE_HEAD = 0;
	public static final int PAD_TYPE_EDGE = 1;
	public static final int CONTROL_CHANGE_EVENT_LEFT_INPUT = 0;
	public static final int CONTROL_CHANGE_EVENT_RIGHT_INPUT = 1;
	public static final int CONTROL_CHANGE_EVENT_3RD_INPUT = 2;
	public static final int CONTROL_CHANGE_EVENT_NAME = 1;
	public static final int CONTROL_CHANGE_EVENT_NOTE_LINKED = 2;
	public static final int CONTROL_CHANGE_EVENT_NOTE_MAIN = 3;

	public static final int CONTROL_CHANGE_EVENT_CURVE = 0;
	public static final int CONTROL_CHANGE_EVENT_CUSTOM_NAME = 1;
	public static final int CUSTOM_NAME_CHANGE_TEXT_START = 64;
	public static final int CUSTOM_NAME_CHANGE_GET_START = CUSTOM_NAME_CHANGE_TEXT_START + CUSTOM_NAMES_MAX;
	public static final int CUSTOM_NAME_CHANGE_SEND_START = CUSTOM_NAME_CHANGE_GET_START + CUSTOM_NAMES_MAX;

	public static final String [] PEDAL_TYPES_LIST = {"Pot", "FootContr"};
	
	public static final int INPUT_VALUE_ID_MIN				= 100;
	public static final int INPUT_VALUE_ID_NAME 			= INPUT_VALUE_ID_MIN;
	public static final int INPUT_VALUE_ID_NOTE 			= INPUT_VALUE_ID_NAME + 1;
	public static final int INPUT_VALUE_ID_ALT_NOTE			= INPUT_VALUE_ID_NOTE + 1;
	public static final int INPUT_VALUE_ID_PRESSROLL_NOTE	= INPUT_VALUE_ID_ALT_NOTE + 1;
	public static final int INPUT_VALUE_ID_CHANNEL 			= INPUT_VALUE_ID_PRESSROLL_NOTE + 1;
	public static final int INPUT_VALUE_ID_FUNCTION 		= INPUT_VALUE_ID_CHANNEL + 1;
	public static final int INPUT_VALUE_ID_CURVE			= INPUT_VALUE_ID_FUNCTION + 1;
	public static final int INPUT_VALUE_ID_COMPRESSION		= INPUT_VALUE_ID_CURVE + 1;
	public static final int INPUT_VALUE_ID_SHIFT 			= INPUT_VALUE_ID_COMPRESSION + 1;
	public static final int INPUT_VALUE_ID_XTALK_LEVEL		= INPUT_VALUE_ID_SHIFT + 1;
	public static final int INPUT_VALUE_ID_XTALK_GROUP		= INPUT_VALUE_ID_XTALK_LEVEL + 1;
	public static final int INPUT_VALUE_ID_THRESHOLD		= INPUT_VALUE_ID_XTALK_GROUP + 1;
	public static final int INPUT_VALUE_ID_GAIN 			= INPUT_VALUE_ID_THRESHOLD + 1;
	public static final int INPUT_VALUE_ID_HIGHLEVEL_AUTO 	= INPUT_VALUE_ID_GAIN + 1;
	public static final int INPUT_VALUE_ID_HIGHLEVEL 		= INPUT_VALUE_ID_HIGHLEVEL_AUTO + 1;
	public static final int INPUT_VALUE_ID_RETRIGGER		= INPUT_VALUE_ID_HIGHLEVEL + 1;
	public static final int INPUT_VALUE_ID_DYN_LEVEL		= INPUT_VALUE_ID_RETRIGGER + 1;
	public static final int INPUT_VALUE_ID_DYN_TIME			= INPUT_VALUE_ID_DYN_LEVEL + 1;
	public static final int INPUT_VALUE_ID_MINSCAN 			= INPUT_VALUE_ID_DYN_TIME + 1;
	public static final int INPUT_VALUE_ID_POS_LEVEL	 	= INPUT_VALUE_ID_MINSCAN + 1;
	public static final int INPUT_VALUE_ID_POS_LOW			= INPUT_VALUE_ID_POS_LEVEL + 1;
	public static final int INPUT_VALUE_ID_POS_HIGH			= INPUT_VALUE_ID_POS_LOW + 1;
	public static final int INPUT_VALUE_ID_TYPE				= INPUT_VALUE_ID_POS_HIGH + 1;
	public static final int INPUT_VALUE_ID_MAX				= INPUT_VALUE_ID_TYPE;
	
	public static final int THIRD_ZONE_VALUE_ID_MIN				= INPUT_VALUE_ID_MAX + 1;
	public static final int THIRD_ZONE_VALUE_ID_NOTE			= THIRD_ZONE_VALUE_ID_MIN;
	public static final int THIRD_ZONE_VALUE_ID_ALT_NOTE		= THIRD_ZONE_VALUE_ID_NOTE + 1;
	public static final int THIRD_ZONE_VALUE_ID_PRESSROLL_NOTE	= THIRD_ZONE_VALUE_ID_ALT_NOTE + 1;
	public static final int THIRD_ZONE_VALUE_ID_DAMPENED_NOTE	= THIRD_ZONE_VALUE_ID_PRESSROLL_NOTE + 1;
	public static final int THIRD_ZONE_VALUE_ID_THRESHOLD		= THIRD_ZONE_VALUE_ID_DAMPENED_NOTE + 1;
	public static final int THIRD_ZONE_VALUE_ID_MAX				= THIRD_ZONE_VALUE_ID_THRESHOLD;

	
}
