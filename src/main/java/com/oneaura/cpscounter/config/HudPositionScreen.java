package com.oneaura.cpscounter.config;

import com.oneaura.cpscounter.OneaurasCPSCounterClient;
import com.oneaura.cpscounter.config.CPSConfig.CpsDisplayMode;
import com.oneaura.cpscounter.configlib.ConfigManager;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class HudPositionScreen {
    private HudPositionScreen() {
    }

    public static Screen create(Screen parent) {
        CPSConfig config = ConfigManager.get();
        ConfigBuilder builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(Component.literal("oneaura's CPS Counter"))
            .setSavingRunnable(() -> {
                OneaurasCPSCounterClient.syncConfigState();
                ConfigManager.save();
            });

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory counter = builder.getOrCreateCategory(Component.literal("oneaura's CPS Counter"));

        counter.addEntry(entryBuilder.startBooleanToggle(Component.literal("Enabled"), config.cpsCounterEnabled)
            .setDefaultValue(true)
            .setTooltip(Component.literal("Enable or disable the CPS counter on the screen."))
            .setSaveConsumer(value -> config.cpsCounterEnabled = value)
            .build());
        counter.addEntry(entryBuilder.startEnumSelector(Component.literal("Display mode"), CpsDisplayMode.class, config.displayMode)
            .setDefaultValue(CpsDisplayMode.BOTH)
            .setEnumNameProvider(value -> Component.literal(titleCase(value.name())))
            .setTooltip(Component.literal("Choose what click values are rendered: both, left, or right."))
            .setSaveConsumer(value -> config.displayMode = value)
            .build());
        counter.addEntry(entryBuilder.startStrField(Component.literal("Label text"), config.labelText)
            .setDefaultValue(" CPS")
            .setTooltip(Component.literal("Custom text to show after the numbers. Leave blank to hide."))
            .setSaveConsumer(value -> config.labelText = value)
            .build());
        counter.addEntry(entryBuilder.startStrField(Component.literal("Text color"), config.textColor)
            .setDefaultValue("FFFFFF")
            .setTooltip(Component.literal("Hex text color, for example FFFFFF."))
            .setSaveConsumer(value -> config.textColor = value)
            .build());
        counter.addEntry(entryBuilder.startBooleanToggle(Component.literal("Show background"), config.showBackground)
            .setDefaultValue(true)
            .setTooltip(Component.literal("Show or hide the counter background."))
            .setSaveConsumer(value -> config.showBackground = value)
            .build());
        counter.addEntry(entryBuilder.startStrField(Component.literal("Background color"), config.backgroundColor)
            .setDefaultValue("80000000")
            .setTooltip(Component.literal("ARGB hex background color, for example 80000000."))
            .setSaveConsumer(value -> config.backgroundColor = value)
            .build());
        counter.addEntry(entryBuilder.startBooleanToggle(Component.literal("Text shadow"), config.textShadow)
            .setDefaultValue(true)
            .setTooltip(Component.literal("Render the CPS text with a shadow."))
            .setSaveConsumer(value -> config.textShadow = value)
            .build());
        counter.addEntry(entryBuilder.startEnumSelector(Component.literal("Text style"), OneaurasCPSCounterClient.TextStyle.class, config.textStyle)
            .setDefaultValue(OneaurasCPSCounterClient.TextStyle.NONE)
            .setEnumNameProvider(value -> Component.literal(titleCase(value.name())))
            .setTooltip(Component.literal("Choose the text style for the counter."))
            .setSaveConsumer(value -> config.textStyle = value)
            .build());
        counter.addEntry(entryBuilder.startIntSlider(Component.literal("Corner radius"), config.backgroundCornerRadius, 0, 8)
            .setDefaultValue(0)
            .setTooltip(Component.literal("Background corner radius. Set to 0 for sharp corners."))
            .setSaveConsumer(value -> config.backgroundCornerRadius = value)
            .build());
        counter.addEntry(entryBuilder.startIntField(Component.literal("HUD X"), config.cpsCounterX)
            .setDefaultValue(5)
            .setTooltip(Component.literal("Horizontal HUD position in pixels."))
            .setSaveConsumer(value -> config.cpsCounterX = Math.max(0, value))
            .build());
        counter.addEntry(entryBuilder.startIntField(Component.literal("HUD Y"), config.cpsCounterY)
            .setDefaultValue(5)
            .setTooltip(Component.literal("Vertical HUD position in pixels."))
            .setSaveConsumer(value -> config.cpsCounterY = Math.max(0, value))
            .build());

        return builder.build();
    }

    private static String titleCase(String value) {
        String lower = value.toLowerCase().replace('_', ' ');
        String[] parts = lower.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return builder.toString();
    }
}
