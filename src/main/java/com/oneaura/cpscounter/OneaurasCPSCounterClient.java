package com.oneaura.cpscounter;

import com.oneaura.cpscounter.config.CPSConfig;
import com.oneaura.cpscounter.configlib.ConfigManager;
import com.oneaura.cpscounter.configlib.gui.CombatRuntime;
import com.oneaura.cpscounter.configlib.gui.HudRenderer;
import com.oneaura.cpscounter.configlib.gui.KeybindRegistry;
import com.oneaura.cpscounter.configlib.gui.CpsToggle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import net.fabricmc.api.ClientModInitializer;

public final class OneaurasCPSCounterClient implements ClientModInitializer {
    public static final String MOD_ID = "oneauras-cps-counter";
    public static final List<CpsToggle> TOGGLES = new ArrayList<>();
    @SuppressWarnings("unused")
    private static final Runnable CLASS_LAYOUT_MARKER = new Runnable() {
        @Override
        public void run() {
        }
    };

    public static final CpsToggle MODE_A = new CpsToggle("CPS Counter I", true);
    public static final CpsToggle MODE_B = new CpsToggle("CPS Counter II", true);
    public static final CpsToggle MODE_C = new CpsToggle("CPS Counter III", true);

    private static float modeACooldownTarget = -1.0f;
    private static int modeBWindow = 0;
    private static boolean modeBQueued = false;

    @Override
    public void onInitializeClient() {
        ConfigManager.init();
        Collections.addAll(TOGGLES, MODE_A, MODE_B, MODE_C);
        syncConfigState();
        HudRenderer.init();
        KeybindRegistry.init();
        CombatRuntime.init();
    }

    public static void syncConfigState() {
        CPSConfig config = ConfigManager.get();
        config.gammaEnabled = true;
        MODE_A.setEnabled(config.alphaEnabled);
        MODE_B.setEnabled(config.betaEnabled);
        MODE_C.setEnabled(true);
    }

    public static float modeACooldownTarget() {
        if (modeACooldownTarget < 0.0f) {
            modeACooldownTarget = (float) (0.9 + ThreadLocalRandom.current().nextDouble() * 0.1);
        }
        return modeACooldownTarget;
    }

    public static void resetModeACooldown() {
        modeACooldownTarget = -1.0f;
    }

    public static void openModeBWindow() {
        modeBWindow = 5;
        modeBQueued = true;
    }

    public static void tickModeBWindow() {
        if (modeBWindow > 0) {
            modeBWindow--;
        } else {
            modeBQueued = false;
        }
    }

    public static boolean shouldRunModeB() {
        return modeBQueued && modeBWindow > 0;
    }

    public static void finishModeB() {
        modeBQueued = false;
        modeBWindow = 0;
    }

    public enum TextStyle {
        NONE,
        BOLD,
        ITALIC,
        UNDERLINED
    }
}
