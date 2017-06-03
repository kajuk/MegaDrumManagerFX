package info.megadrum.managerfx.data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Properties;

import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConversionException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.PropertiesConfigurationLayout;

import info.megadrum.managerfx.utils.Constants;
import info.megadrum.managerfx.utils.Utils;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileManager {
	private FileChooser fileChooser;
	private Stage parent;
	private File file = null;
	//private ConfigFileFilter configFileFilter;
	//private BinFileFilter binFileFilter;
	//private Properties prop;
	private PropertiesConfiguration fullConfig;
	//private CombinedConfiguration cc;
	
	public FileManager (Stage parentWindow) {
		fileChooser = new FileChooser();
		parent = parentWindow;
		//binFileFilter = new BinFileFilter();
		//prop = new Properties();
		//fullConfig = new PropertiesConfiguration();
	}

	public void saveConfigFull(ConfigFull config, File file) {
		if (file.exists()) {
			file.delete();
		}
		fullConfig = new PropertiesConfiguration();
		PropertiesConfigurationLayout layout = new PropertiesConfigurationLayout(fullConfig);
		layout.setHeaderComment("MegaDrum config");
		fullConfig.setLayout(layout);
		config.copyToPropertiesConfiguration(fullConfig,layout);

		try {
			fullConfig.save(file);
		} catch (ConfigurationException e) {
			//e.printStackTrace();
			Utils.show_error("Error saving all settings to:\n" +
					file.getAbsolutePath()+"\n"+"("+e.getMessage()+")");
		}
		
	}
	
	public void save_all(ConfigFull config, ConfigOptions options) {
		FileChooser.ExtensionFilter configFileFilter = new FileChooser.ExtensionFilter("MegaDrum full config files (*.mds)", "*.mds");
		fileChooser.getExtensionFilters().clear();
		fileChooser.getExtensionFilters().add(configFileFilter);
		if (!options.configFullPaths[options.lastConfig].equals("")) {
			fileChooser.setInitialDirectory(new File(options.configFullPaths[options.lastConfig]));
		}
		//fileChooser.setSelectedFile(new File(options.configFileNames[options.lastConfig]));
		fileChooser.setInitialFileName(options.configFileNames[options.lastConfig]);
		//returnVal = fileChooser.showSaveDialog(parent);
		file = fileChooser.showSaveDialog(parent);
		if(file != null) {
			if (!(file.getName().toLowerCase().endsWith(".mds"))) {
				file = new File(file.getAbsolutePath() + ".mds");
			}
			options.configFullPaths[options.lastConfig] = file.getAbsolutePath();
			options.configFileNames[options.lastConfig] = file.getName();
			options.configLoaded[options.lastConfig] = true;

			if (file.exists()) {
				file.delete();
			}
			saveConfigFull(config, file);
		}
	}

	public void loadConfigFull(ConfigFull config, File file, ConfigOptions options) {
		fullConfig = new PropertiesConfiguration();
		try {
			fullConfig.load(file);
			try {
				config.copyFromPropertiesConfiguration(fullConfig);
				options.configLoaded[options.lastConfig] = true;
			} catch (ConversionException e ) {
				Utils.show_error("Error parsing settings from:\n" +
						file.getAbsolutePath()+"\n"+"("+e.getMessage()+")");
			}

		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Utils.show_error("Error loading all settings from:\n" +
					file.getAbsolutePath()+"\n"+"("+e.getMessage()+")");
		}
	}	

	public void load_all(ConfigFull config, ConfigOptions options) {
		FileChooser.ExtensionFilter configFileFilter = new FileChooser.ExtensionFilter("MegaDrum full config files (*.mds)", "*.mds");
		fileChooser.getExtensionFilters().clear();
		fileChooser.getExtensionFilters().add(configFileFilter);
		if (!options.configFullPaths[options.lastConfig].equals("")) {
			fileChooser.setInitialDirectory(new File(options.configFullPaths[options.lastConfig]));
		}
		//fileChooser.setSelectedFile(new File(options.configFileNames[options.lastConfig]));
		fileChooser.setInitialFileName(options.configFileNames[options.lastConfig]);
		//returnVal = fileChooser.showSaveDialog(parent);
		file = fileChooser.showOpenDialog(parent);
		if (file != null) {
			if (file.exists()) {
				loadConfigFull(config,file,options);
				options.configFullPaths[options.lastConfig] = file.getAbsolutePath();
				options.configFileNames[options.lastConfig] = file.getName();
			}
		}
	}


	public File selectFirmwareFile(ConfigOptions options) {
		FileChooser.ExtensionFilter extensionFilter;
		if (!options.lastFullPathFirmware.equals("")) {
			fileChooser.setInitialDirectory(new File(options.lastFullPathFirmware));
		}
		extensionFilter = new FileChooser.ExtensionFilter("Firmware files (*.bin)", "*.bin");
		fileChooser.getExtensionFilters().clear();
		fileChooser.getExtensionFilters().add(extensionFilter);
		if (options.mcuType == 4) {
			extensionFilter = new FileChooser.ExtensionFilter("STM32a firmware files (*.bin)", "megadrumstm32a_*");
			fileChooser.getExtensionFilters().clear();
			fileChooser.getExtensionFilters().add(extensionFilter);
		}
		if (options.mcuType == 5) {
			extensionFilter = new FileChooser.ExtensionFilter("STM32b firmware files (*.bin)", "megadrumstm32b_*");
			fileChooser.getExtensionFilters().clear();
			fileChooser.getExtensionFilters().add(extensionFilter);
		}
		if (options.mcuType == 6) {
			extensionFilter = new FileChooser.ExtensionFilter("STM32c firmware files (*.bin)", "megadrumstm32c_*");
			fileChooser.getExtensionFilters().clear();
			fileChooser.getExtensionFilters().add(extensionFilter);
		}
		if (options.mcuType == 7) {
			extensionFilter = new FileChooser.ExtensionFilter("STM32d firmware files (*.bin)", "megadrumstm32d_*");
			fileChooser.getExtensionFilters().clear();
			fileChooser.getExtensionFilters().add(extensionFilter);
		}
		if (options.mcuType == 8) {
			extensionFilter = new FileChooser.ExtensionFilter("STM32e firmware files (*.bin)", "megadrumstm32e_*");
			fileChooser.getExtensionFilters().clear();
			fileChooser.getExtensionFilters().add(extensionFilter);
		}
		file = fileChooser.showOpenDialog(parent);
		if (file != null) {
			options.lastFullPathFirmware = file.getAbsolutePath();
		}
		return file;
	}

	public void loadAllSilent(ConfigFull config, ConfigOptions options) {
		file = new File(options.configFullPaths[options.lastConfig]);
		if (file.exists()) {
			if (!file.isDirectory()) {
				loadConfigFull(config,file,options);				
				options.configFileNames[options.lastConfig] = file.getName();
				options.configFullPaths[options.lastConfig] = file.getAbsolutePath();
			}
		}
	}

	public ConfigOptions loadLastOptions(ConfigOptions config) {
		file = new File(Constants.MD_MANAGER_CONFIG);
		if (file.exists()) {
			PropertiesConfiguration prop = new PropertiesConfiguration();
			try {
				prop.load(file);
				try {
					config.copyFromPropertiesConfiguration(prop);
				} catch (ConversionException e) {
					// TODO Auto-generated catch block
					Utils.show_error("Error parsing MegaDrum options from file:\n" +
							file.getAbsolutePath() + "\n"
							+"(" + e.getMessage() + ")");
				}
			} catch (ConfigurationException e) {
				// TODO Auto-generated catch block
				saveLastOptions(config);
			}
		} else {
			saveLastOptions(config);
		}
		return config;
	}
	
	public void saveLastOptions(ConfigOptions config) {
		file = new File(Constants.MD_MANAGER_CONFIG);
		PropertiesConfiguration prop = new PropertiesConfiguration();
		config.copyToPropertiesConfiguration(prop);
		try {
			prop.save(file);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Utils.show_error("Error saving MegaDrum options to file:\n" +
					file.getAbsolutePath() + "\n"
					+"(" + e.getMessage() + ")");
		}		
		
	}
		
	public void saveSysex(byte [] sysex, ConfigOptions options) {
		FileChooser.ExtensionFilter sysexFileFilter = new FileChooser.ExtensionFilter("Sysex files (*.syx)", "*.syx");
		fileChooser.getExtensionFilters().clear();
		fileChooser.getExtensionFilters().add(sysexFileFilter);
		
		if (!options.lastFullPathSysex.equals("")) {
			fileChooser.setInitialDirectory(new File(options.lastFullPathSysex));
		}
		file = fileChooser.showSaveDialog(parent);
		if (file != null) {
			if (!(file.getName().toLowerCase().endsWith(".syx"))) {
				file = new File(file.getAbsolutePath() + ".syx");
			}
			options.lastFullPathSysex = file.getAbsolutePath();
			if (file.exists()) {
				file.delete();
			}
			try {
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(sysex);
				fos.flush();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				Utils.show_error("Error saving Sysex to file:\n" +
						file.getAbsolutePath() + "\n"
						+"(" + e.getMessage() + ")");

			}
		}
	}

	public void loadSysex(byte [] sysex, ConfigOptions options) {
		FileChooser.ExtensionFilter sysexFileFilter = new FileChooser.ExtensionFilter("Sysex files (*.syx)", "*.syx");
		fileChooser.getExtensionFilters().clear();
		fileChooser.getExtensionFilters().add(sysexFileFilter);
		
		if (!options.lastFullPathSysex.equals("")) {
			fileChooser.setInitialDirectory(new File(options.lastFullPathSysex));
		}
		file = fileChooser.showOpenDialog(parent);
		if (file != null) {
			options.lastFullPathSysex = file.getAbsolutePath();
			if (file.exists()) {
				FileInputStream fis;
				try {
					fis = new FileInputStream(file);
					fis.read(sysex);
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					Utils.show_error("Error loading Sysex from file:\n" +
							file.getAbsolutePath() + "\n"
							+"(" + e.getMessage() + ")");
				}
			}
		}
	}
}
