package com.oneaura.cpscounter.configlib.gui;

import com.oneaura.cpscounter.CPSManager;
import com.oneaura.cpscounter.OneaurasCPSCounterClient;
import com.oneaura.cpscounter.config.CPSConfig;
import com.oneaura.cpscounter.configlib.ConfigManager;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;

public final class HudRenderer {
    private static final Identifier HUD_ID = Identifier.fromNamespaceAndPath(OneaurasCPSCounterClient.MOD_ID, "hud");

    private HudRenderer() {
    }

    public static void init() {
        HudElementRegistry.addLast(HUD_ID, HudRenderer::render);
    }

    private static void render(GuiGraphics graphics, net.minecraft.client.DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;
        if (mc.player == null || font == null) {
            return;
        }

        long now = System.currentTimeMillis();
        CPSManager.tick(now);
        CPSConfig config = ConfigManager.get();

        if (config.cpsCounterEnabled) {
            renderCpsCounter(graphics, font, config);
        }
    }

    private static void renderCpsCounter(GuiGraphics graphics, Font font, CPSConfig config) {
        String text = switch (config.displayMode) {
            case BOTH -> CPSManager.getLeftCPS() + " | " + CPSManager.getRightCPS() + config.labelText;
            case LEFT -> CPSManager.getLeftCPS() + config.labelText;
            case RIGHT -> CPSManager.getRightCPS() + config.labelText;
        };
        int x = Math.max(4, config.cpsCounterX);
        int y = Math.max(4, config.cpsCounterY);
        int width = font.width(text);
        if (config.showBackground) {
            int backgroundColor = parseBackgroundColor(config.backgroundColor);
            fillBackground(graphics, x - 2, y - 2, x + width + 2, y + 10, backgroundColor, Math.max(0, config.backgroundCornerRadius));
        }
        graphics.drawString(font, styledComponent(text, config.textStyle), x, y, parseTextColor(config.textColor), config.textShadow);
    }

    private static MutableComponent styledComponent(String text, OneaurasCPSCounterClient.TextStyle style) {
        MutableComponent component = Component.literal(text);
        return switch (style) {
            case BOLD -> component.withStyle(ChatFormatting.BOLD);
            case ITALIC -> component.withStyle(ChatFormatting.ITALIC);
            case UNDERLINED -> component.withStyle(ChatFormatting.UNDERLINE);
            case NONE -> component;
        };
    }

    private static int parseTextColor(String value) {
        try {
            return 0xFF000000 | Integer.parseInt(value.trim(), 16);
        } catch (NumberFormatException ignored) {
            return 0xFFFFFFFF;
        }
    }

    private static int parseBackgroundColor(String value) {
        try {
            return (int) Long.parseLong(value.trim(), 16);
        } catch (NumberFormatException ignored) {
            return 0x80000000;
        }
    }

    private static void fillBackground(GuiGraphics graphics, int x1, int y1, int x2, int y2, int color, int radius) {
        if (radius <= 0) {
            graphics.fill(x1, y1, x2, y2, color);
            return;
        }
        int clampedRadius = Math.min(radius, Math.min((x2 - x1) / 2, (y2 - y1) / 2));
        graphics.fill(x1 + clampedRadius, y1, x2 - clampedRadius, y2, color);
        graphics.fill(x1, y1 + clampedRadius, x1 + clampedRadius, y2 - clampedRadius, color);
        graphics.fill(x2 - clampedRadius, y1 + clampedRadius, x2, y2 - clampedRadius, color);
    }

}
