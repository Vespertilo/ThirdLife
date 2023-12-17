package me.vespertilo.thirdlife.config;

import me.vespertilo.thirdlife.ThirdLife;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ConfigManager {

    private final ThirdLife instance;
    private File timeConfigFile;
    private FileConfiguration timeConfig;

    public ConfigManager(ThirdLife instance) {
        this.instance = instance;
    }

    public void createTimeConfig() {
        timeConfigFile = new File(instance.getDataFolder(), "times.yml");
        try {
            boolean created = timeConfigFile.createNewFile();
            if (created) {
                instance.saveResource("times.yml", false);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        timeConfig = YamlConfiguration.loadConfiguration(timeConfigFile);
    }

    public void saveTimeConfig() {
       setTimeHashmap(timeConfig, timeConfigFile, instance.timeManager.getCachedTimes());
    }

    public void setTimeHashmap(FileConfiguration config, File configFile, HashMap<UUID, Integer> map) {
        List<String> data = new ArrayList<>();
        for (UUID uuid : map.keySet()) {
            data.add(uuid.toString() + ": " + map.get(uuid));
        }

        config.set("time", data);
        try {
            config.save(configFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public HashMap<UUID, Integer> getTimeHashmap(FileConfiguration config) {
        HashMap<UUID, Integer> map = new HashMap<>();
        List<String> values = config.getStringList("time");
        for (String str : values) {
            String[] split = str.split(": ");
            String uuidStr = split[0];
            String intStr = split[1];

            UUID parsedUUID = UUID.fromString(uuidStr);
            Integer parsedInt = Integer.parseInt(intStr);
            map.put(parsedUUID, parsedInt);
        }
        return map;
    }

    public FileConfiguration getTimeConfig() {
        return timeConfig;
    }
}