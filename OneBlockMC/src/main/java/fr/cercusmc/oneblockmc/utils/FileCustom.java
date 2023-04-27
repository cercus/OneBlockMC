package fr.cercusmc.oneblockmc.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class FileCustom {
	
	private File file;
    private FileConfiguration fileConfiguration;
    private String fileName;
    private Logger logger;

    /**
     * Contruct a new object FileCustom
     * @param fileName : Name of file
     */
    public FileCustom(Plugin plugin, String fileName) {
        if(fileName.contains(".yml")) {
            file = new File(plugin.getDataFolder(), fileName);
            this.fileName = fileName;
        } else {
            file = new File(plugin.getDataFolder(), (fileName + ".yml"));
            this.fileName = fileName +".yml";
        }
        if(!file.exists()) {
            logger.info("Creation of file " + fileName + "...");
            plugin.saveResource(fileName, false);
            logger.info("File created successfully");
        }

        fileConfiguration = new YamlConfiguration();

        try {
        	logger.info("Loading file " + fileName + "...");
            fileConfiguration.load(file);
            logger.info("File upload complete!");            
            
        } catch (IOException | InvalidConfigurationException e) {
        	logger.info("An error occurred while loading the file :" + this.fileName +"; " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Check one value with Enum given
     * @param <E>
     * @param path
     * @param eEnum
     * @return
     */
    public <E extends Enum<E>> boolean checkValue(String path, Class<E> eEnum) {
    
    	return ValidateUtil.checkEnum(eEnum, this.fileConfiguration.getString(path));
    
    }
    
    public <E extends Enum<E>> Map<String, Boolean> checkValuesOfList(String path, Class<E> eEnum) {
    	HashMap<String, Boolean> res = new HashMap<>();
    	List<String> values = this.fileConfiguration.getStringList(path);
    	if(values.isEmpty())
    		return res;
    	
    	for(String s : values) {
    		res.put(s, ValidateUtil.checkEnum(eEnum, s));
    	}
    	
    	return res;
    }

    /**
     * Reload a file
     * @return true if the load is successfull
     */
    public boolean reloadFile() {
        try {
            fileConfiguration.load(file);
            return true;
        } catch(IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Save a file <br />
     * This method must call after each modification in file
     * @return true if save is successfull
     */
    public boolean save() {
        try {
            fileConfiguration.save(file);
            return true;
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Insert in the file a new value in given path. <br />
     * After insert the value, you don't have to use the method save()
     * @param path The path
     * @param value The value
     * @return The objetc inserted
     */
    public Object set(String path, Object value) {
        fileConfiguration.set(path, value);
        save();
        return value;
    }

    /**
     * Get the File object
     * @return The file object
     */
    public File getFile() {
        return file;
    }

    /**
     * Get the FileConfiguration object from SpigotAPI
     * @return The FileConfiguration object from SpigotAPI
     */
    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    public String getString(String path) {
        return fileConfiguration.getString(path);
    }

    public String getString(String path, String defaultString) {
        return fileConfiguration.getString(path, defaultString);
    }

    public int getInt(String path) {
        return fileConfiguration.getInt(path);
    }

    public int getInt(String path, int defaultInt) {
        return fileConfiguration.getInt(path, defaultInt);
    }

    public double getDouble(String path) {
        return fileConfiguration.getDouble(path);
    }

    public double getDouble(String path, double defaultDouble) {
        return fileConfiguration.getDouble(path, defaultDouble);
    }

    public List<String> getStringList(String path) {
        return fileConfiguration.getStringList(path);
    }

    public List<Double> getDoubleList(String path) {
        return fileConfiguration.getDoubleList(path);
    }

    public List<Integer> getIntegerList(String path) {
        return fileConfiguration.getIntegerList(path);
    }

	public Long getLong(String path, int i) {
		return fileConfiguration.getLong(path, i);
	}
	
	public Long getLong(String path) {
		return fileConfiguration.getLong(path);
	}
	
	public Location getLocation(String path) {
		return fileConfiguration.getLocation(path);
	}
	
	public ConfigurationSection getConfigurationSection(String path) {
		return fileConfiguration.getConfigurationSection(path);
	}
	
	public ConfigurationSection createSection(String path) {
		return fileConfiguration.createSection(path);
	}
	
	public boolean contains(String path) {
		return fileConfiguration.contains(path);
	}
	
	public boolean getBoolean(String path, boolean i) {
		return fileConfiguration.getBoolean(path, i);
	}
	
	public boolean getBoolean(String path) {
		return fileConfiguration.getBoolean(path);
	}

}
