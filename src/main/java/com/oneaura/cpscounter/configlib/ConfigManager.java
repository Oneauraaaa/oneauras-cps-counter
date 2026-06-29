package com.oneaura.cpscounter.configlib;

import com.oneaura.cpscounter.config.CPSConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

public final class ConfigManager {
    private ConfigManager() {
    }

    public static void init() {
        AutoConfig.register(CPSConfig.class, GsonConfigSerializer::new);
    }

    public static CPSConfig get() {
        return AutoConfig.getConfigHolder(CPSConfig.class).getConfig();
    }

    public static void save() {
        AutoConfig.getConfigHolder(CPSConfig.class).save();
    }
}
