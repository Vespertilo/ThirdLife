package me.vespertilo.thirdlife.config;

import pl.mikigal.config.Config;
import pl.mikigal.config.annotation.Comment;
import pl.mikigal.config.annotation.ConfigName;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.vespertilo.thirdlife.ThirdLife.unix24hrs;

@ConfigName("times.yml")
public interface TimeConfig extends Config {



    default Map<String, Integer> getTimeValues() {
        Map<String, Integer> map = new HashMap<>();
        map.put("00000000-0000-0000-0000-000000000000", unix24hrs);
        return map;
    }

    public void setTimeValues(Map<String, Integer> values);
}
