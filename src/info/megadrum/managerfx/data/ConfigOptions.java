package info.megadrum.managerfx.data;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import info.megadrum.managerfx.utils.Constants;
import info.megadrum.managerfx.utils.Utils;
import javafx.geometry.Point2D;

public class ConfigOptions implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4465922793257742902L;
	public boolean useSamePort = false;
	public boolean useThruPort = false;
	public boolean autoOpenPorts = false;
	public boolean saveOnExit = false;
	public boolean liveUpdates = false;
	//public String lastDir = "";
	public int lastConfig = 0;
	//public String [] configsNames;
	public String [] configFileNames;
	//public String lastFullPathConfig = "";
	public String [] configFullPaths;
	public boolean [] configLoaded;
	public String lastFullPathFirmware = "";
	public String lastFullPathSysex = "";
	public String MidiInName = "";
	public String MidiOutName = "";
	public String MidiThruName = "";
	public int chainId = 0;
	//public int inputsCount = 56;
	public int sysexDelay = 30;
	public String LookAndFeelName = "";
	public Point2D mainWindowPosition = new Point2D(10,10);
	public Point2D mainWindowSize = new Point2D(1000,600);
	// Show panels. 0 - Misc, 1 - Pedal, 2 - Pads, 3 - Curves, 4 - MIDI Log
	public Point2D [] framesPositions = { new Point2D(10,10), new Point2D(210,10), new Point2D(410,10), new Point2D(610,10), new Point2D(810,10)};
	public Point2D [] framesSizes = { new Point2D(200,300), new Point2D(200,300), new Point2D(400,500), new Point2D(400,500), new Point2D(500,300)};
	public int [] showPanels = { Constants.PANEL_SHOW, Constants.PANEL_SHOW, Constants.PANEL_SHOW, Constants.PANEL_SHOW, Constants.PANEL_HIDE };
	public int globalMiscViewState = Constants.PANEL_SHOW; 
	public int mcuType = 0;
	public int version = 0;
	public boolean autoResize = true;
	public boolean changeNotified = false;
	public boolean showAdvancedSettings = true;

	public ConfigOptions() {
		configFileNames = new String[Constants.CONFIGS_COUNT];
		configFullPaths = new String[Constants.CONFIGS_COUNT];
		configLoaded = new boolean[Constants.CONFIGS_COUNT];
		Integer n;
		for (Integer i = 0;i < Constants.CONFIGS_COUNT;i++) {
			//configFileNames[i] = "Config"+(i+1).toString();
			n = i + 1;
			configFileNames[i] = "config"+n.toString();
			configFullPaths[i] = "";
			configLoaded[i] = false; 
		}
		
	}
	public void copyToPropertiesConfiguration(PropertiesConfiguration prop) {
		prop.setHeader("MegaDrum options");
		prop.setProperty("MDconfigVersion", Constants.MD_CONFIG_VERSION.toString());
		prop.setProperty("useSamePort", useSamePort);
		prop.setProperty("useThruPort", useThruPort);
		prop.setProperty("autoOpenPorts", autoOpenPorts);
		prop.setProperty("saveOnExit", saveOnExit);
		prop.setProperty("showAdvancedSettings", showAdvancedSettings);
		prop.setProperty("interactive", liveUpdates);
		//prop.setProperty("lastDir", lastDir);
		prop.setProperty("lastConfig", lastConfig);
		for (Integer i = 0;i<Constants.CONFIGS_COUNT;i++) {
			prop.setProperty("configFileName"+i.toString(), configFileNames[i]);
			prop.setProperty("configFullPath"+i.toString(), configFullPaths[i]);
		}
		//prop.setProperty("lastFullPathConfig", lastFullPathConfig);
		prop.setProperty("lastFullPathFirmware", lastFullPathFirmware);
		prop.setProperty("lastFullPathSysex", lastFullPathSysex);
		prop.setProperty("MidiInName", MidiInName);
		prop.setProperty("MidiOutName", MidiOutName);
		prop.setProperty("MidiThruName", MidiThruName);
		prop.setProperty("chainId", chainId);
		//prop.setProperty("inputsCount", inputsCount);
		prop.setProperty("sysexDelay", sysexDelay);
		prop.setProperty("LookAndFeelName", LookAndFeelName);
		prop.setProperty("mainWindowPositionX", mainWindowPosition.getX());
		prop.setProperty("mainWindowPositionY", mainWindowPosition.getY());
		prop.setProperty("mainWindowSizeX", mainWindowSize.getX());
		prop.setProperty("mainWindowSizeY", mainWindowSize.getY());
		for (int i = 0;i<Constants.PANELS_COUNT;i++) {
			prop.setProperty("framesPositions"+ ((Integer)i).toString()+"X", framesPositions[i].getX());
			prop.setProperty("framesPositions"+ ((Integer)i).toString()+"Y", framesPositions[i].getY());
			prop.setProperty("framesSizes"+ ((Integer)i).toString()+"W", framesSizes[i].getX());
			prop.setProperty("framesSizes"+ ((Integer)i).toString()+"H", framesSizes[i].getY());
			prop.setProperty("showPanels"+ ((Integer)i).toString(), showPanels[i]);
		}
		prop.setProperty("globalMiscViewState", globalMiscViewState);
		prop.setProperty("autoResize", autoResize);
		prop.setProperty("changeNotified", changeNotified);
	}

	public void copyFromPropertiesConfiguration(PropertiesConfiguration prop) {
		prop.setListDelimiter((char)0);
		try {
			prop.refresh();
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		useSamePort = prop.getBoolean("useSamePort", useSamePort);
		useThruPort = prop.getBoolean("useThruPort", useThruPort);
		autoOpenPorts = prop.getBoolean("autoOpenPorts", autoOpenPorts);
		saveOnExit = prop.getBoolean("saveOnExit", saveOnExit);
		showAdvancedSettings = prop.getBoolean("showAdvancedSettings", showAdvancedSettings);
		liveUpdates = prop.getBoolean("interactive", liveUpdates);
		//lastDir = prop.getString("lastDir", lastDir);
		lastConfig = Utils.validateInt(prop.getInt("lastConfig",lastConfig),0,Constants.CONFIGS_COUNT-1,lastConfig);
		for (Integer i = 0;i<Constants.CONFIGS_COUNT;i++) {
			configFileNames[i] = prop.getString("configFileName"+i.toString(), configFileNames[i]);
			configFullPaths[i] = prop.getString("configFullPath"+i.toString(), configFullPaths[i]);
		}
		//lastFullPathConfig = prop.getString("lastFullPathConfig", lastFullPathConfig);
		lastFullPathFirmware = prop.getString("lastFullPathFirmware", lastFullPathFirmware);
		lastFullPathSysex = prop.getString("lastFullPathSysex", lastFullPathSysex);
		MidiInName = prop.getString("MidiInName", MidiInName);
		MidiOutName = prop.getString("MidiOutName", MidiOutName);
		MidiThruName = prop.getString("MidiThruName", MidiThruName);
		chainId = Utils.validateInt(prop.getInt("chainId", chainId),0,3,chainId);
		//inputsCount = Utils.validateInt(prop.getInt("inputsCount", inputsCount),18,56,inputsCount);
		sysexDelay = Utils.validateInt(prop.getInt("sysexDelay", sysexDelay),10,100,sysexDelay);
		LookAndFeelName = prop.getString("LookAndFeelName", LookAndFeelName);
		mainWindowPosition = new Point2D(
				Utils.validateDouble(prop.getDouble("mainWindowPositionX", 0.0),0.0,1920.0,0.0),
				Utils.validateDouble(prop.getDouble("mainWindowPositionY", 0.0),0.0,800.0,0.0)
				);
		mainWindowSize = new Point2D(
				Utils.validateDouble(prop.getDouble("mainWindowSizeX", 0.0),0.0,3200.0,0.0),
				Utils.validateDouble(prop.getDouble("mainWindowSizeY", 0.0),0.0,2000.0,0.0)
				);
		for (int i = 0;i<Constants.PANELS_COUNT;i++) {
			framesPositions[i] = new Point2D (
					Utils.validateDouble(prop.getDouble("framesPositions"+ ((Integer)i).toString()+"X", 0.0),0.0,1600.0,0.0),
					Utils.validateDouble(prop.getDouble("framesPositions"+ ((Integer)i).toString()+"Y", 0.0),0.0,600.0,0.0)
					);
			framesSizes[i] = new Point2D (
					Utils.validateDouble(prop.getDouble("framesSizes"+ ((Integer)i).toString()+"W", 0.0),0.0,3200.0,0.0),
					Utils.validateDouble(prop.getDouble("framesSizes"+ ((Integer)i).toString()+"H", 0.0),0.0,2000.0,0.0)
					);
			showPanels[i] = Utils.validateInt(prop.getInt("showPanels"+ ((Integer)i).toString(),showPanels[i]),0,2,showPanels[i]);
		}
		globalMiscViewState = Utils.validateInt(prop.getInt("globalMiscViewState", globalMiscViewState),0,1,globalMiscViewState);
		autoResize = prop.getBoolean("autoResize", autoResize);
		changeNotified = prop.getBoolean("changeNotified", false);
	}
}
