package fr.cercusmc.oneblockmc.settings;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import fr.cercusmc.oneblockmc.utils.files.FileCustom;
import fr.cercusmc.oneblockmc.utils.MessageUtil;
import fr.cercusmc.oneblockmc.utils.ValidateUtil;

public class ToolSettings {
	
	private static final String SETTINGS2 = "settings.";

	public static List<Setting> getAllSettings(FileCustom file) {
		List<Setting> settings = new ArrayList<>();
		for(String i : file.getConfigurationSection("settings").getKeys(false)) {
			
			for(String j : file.getConfigurationSection(SETTINGS2+i).getKeys(false)) {
				
				ConfigurationSection section2 = file.getConfigurationSection(SETTINGS2+i+"."+j);
				if(ValidateUtil.checkEnum(Material.class, section2.getString("icon").toUpperCase()))
					settings.add(new Setting(i, Material.valueOf(section2.getString("icon").toUpperCase()), MessageUtil.format(section2.getString("name")), section2.getString(j) ,false));
			}
		}
		return settings;
	}
	
	public static List<String> getAllSettingsTypes(FileCustom file, String role) {
		
		List<String> settings = new ArrayList<>();
		for(String i : file.getConfigurationSection(SETTINGS2+role).getKeys(false)) {
			settings.add(i);
		}
		
		return settings;
	}
	
	
	public static List<FileCustom> getAllFiles(Plugin plugin) {
		Path path = Paths.get(plugin.getDataFolder().getAbsolutePath()+"/settings-data");
		if(!Files.exists(path)) {
			try {
				System.out.println("Creation of directory settings-data...");
				Files.createDirectories(path);
			} catch(IOException e) {

			}
		}
		try(Stream<Path> paths = Files.list(path)) {
			List<File> files = paths.map(Path::toFile).filter(File::isFile).toList();
			
			return files.stream().map(k -> new FileCustom(plugin, "settings-data", k.getName())).toList();
			
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
	
	public static Optional<FileCustom> getFileOfPlayer(Plugin plugin, String uuid) {
		return getAllFiles(plugin).stream().filter(k -> k.getFileName().contains(uuid)).findFirst();
	}
	
	

}
