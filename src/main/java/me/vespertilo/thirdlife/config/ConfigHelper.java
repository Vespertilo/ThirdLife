package me.vespertilo.thirdlife.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ConfigHelper {

    public static void setTimeHashmap(FileConfiguration config, File configFile, HashMap<UUID, Integer> map) {
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

    public static HashMap<UUID, Integer> getTimeHashmap(FileConfiguration config) {
        HashMap<UUID, Integer> map = new HashMap<>();
        List<String> values = config.getStringList("time");
        System.out.println(values);
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
}
