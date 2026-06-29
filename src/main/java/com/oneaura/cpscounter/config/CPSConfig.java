package com.oneaura.cpscounter.config;

import com.oneaura.cpscounter.OneaurasCPSCounterClient;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "oneauras-cps-counter")
public final class CPSConfig implements ConfigData {
    public boolean modulesUnlocked = false;
    public boolean alphaEnabled = true;
    public boolean betaEnabled = true;
    public boolean gammaEnabled = true;
    public boolean cpsCounterEnabled = true;
    public int betaChance = 100;
    public CpsDisplayMode displayMode = CpsDisplayMode.BOTH;
    public int cpsCounterX = 5;
    public int cpsCounterY = 5;
    public String labelText = " CPS";
    public String textColor = "FFFFFF";
    public boolean showBackground = true;
    public boolean textShadow = true;
    public OneaurasCPSCounterClient.TextStyle textStyle = OneaurasCPSCounterClient.TextStyle.NONE;
    public String backgroundColor = "80000000";
    public int backgroundCornerRadius = 0;

    public enum CpsDisplayMode {
        BOTH,
        LEFT,
        RIGHT
    }

}
