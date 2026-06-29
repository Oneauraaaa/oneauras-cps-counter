package com.oneaura.cpscounter.configlib.gui;

import com.oneaura.cpscounter.OneaurasCPSCounterClient;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public final class KeybindRegistry {
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(OneaurasCPSCounterClient.MOD_ID, "oneauras"));

    static final KeyMapping KEY_A = register("oneauras_a", InputConstants.KEY_F);
    static final KeyMapping KEY_B = register("oneauras_b", InputConstants.KEY_Z);
    static final KeyMapping KEY_C = register("oneauras_c", InputConstants.KEY_G);

    private KeybindRegistry() {
    }

    public static void init() {
    }

    private static KeyMapping register(String path, int keyCode) {
        return KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key." + OneaurasCPSCounterClient.MOD_ID + "." + path,
            InputConstants.Type.KEYSYM,
            keyCode,
            CATEGORY
        ));
    }
}
