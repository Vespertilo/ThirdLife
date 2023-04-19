package me.vespertilo.thirdlife.config;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ConfigHelper {

    public static void setTimeHashmap(FileConfiguration config, HashMap<UUID, Integer> map) {
        for (UUID uuid : map.keySet()) {
            config.set("time", uuid.toString() + ": " + map.get(uuid));
        }
    }

    public static HashMap<UUID, Integer> getTimeHashmap(FileConfiguration config) {
        HashMap<UUID, Integer> map = new HashMap<>();
        List<String> values = config.getStringList("time");
        for (String str : values) {
            String[] split = str.split(":");
            String uuidStr = split[0];
            String intStr = split[1];

            UUID parsedUUID = UUID.fromString(uuidStr);
            Integer parsedInt = Integer.parseInt(intStr);
            map.put(parsedUUID, parsedInt);
        }
        return map;
    }
}
