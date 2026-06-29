package com.oneaura.cpscounter.configlib.gui;

import com.oneaura.cpscounter.CPSManager;
import com.oneaura.cpscounter.OneaurasCPSCounterClient;
import com.oneaura.cpscounter.config.CPSConfig;
import com.oneaura.cpscounter.configlib.ConfigManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.tags.ItemTags;
import java.util.UUID;

public final class CombatRuntime {
    private static boolean movementKeyOverridden = false;
    private static int lastHurtTime = 0;
    private static boolean attackWasDown = false;
    private static boolean useWasDown = false;
    private static UUID focusTargetId = null;
    private static long lastFocusSwapAt = 0L;
    private static boolean internalAttack = false;
    private static final long FOCUS_SWAP_GRACE_MS = 50L;
    private CombatRuntime() {
    }

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(CombatRuntime::tickClient);
    }

    private static void tickClient(Minecraft mc) {
        CPSConfig config = ConfigManager.get();
        while (KeybindRegistry.KEY_A.consumeClick()) {
            toggleCpsToggle(OneaurasCPSCounterClient.MODE_A);
        }
        while (KeybindRegistry.KEY_B.consumeClick()) {
            toggleCpsToggle(OneaurasCPSCounterClient.MODE_B);
        }
        while (KeybindRegistry.KEY_C.consumeClick()) {
            toggleCpsToggle(OneaurasCPSCounterClient.MODE_C);
        }

        LocalPlayer player = mc.player;
        if (player == null || mc.level == null) {
            releaseMovementKey(mc);
            lastHurtTime = 0;
            attackWasDown = false;
            useWasDown = false;
            clearFocus();
            OneaurasCPSCounterClient.resetModeACooldown();
            OneaurasCPSCounterClient.finishModeB();
            return;
        }

        OneaurasCPSCounterClient.syncConfigState();
        tickManualClickCounter(mc);
        tickModeC(mc, player);

        tickModeB(player);
        tickModeA(mc, player);
    }

    private static void toggleCpsToggle(CpsToggle toggle) {
        if (toggle == OneaurasCPSCounterClient.MODE_C) {
            toggle.setEnabled(true);
            ConfigManager.get().gammaEnabled = true;
            ConfigManager.save();
            return;
        }
        toggle.toggle();
        CPSConfig config = ConfigManager.get();
        config.alphaEnabled = OneaurasCPSCounterClient.MODE_A.enabled();
        config.betaEnabled = OneaurasCPSCounterClient.MODE_B.enabled();
        config.gammaEnabled = true;
        ConfigManager.save();
    }

    private static void tickManualClickCounter(Minecraft mc) {
        boolean attackDown = mc.options.keyAttack.isDown();
        if (attackDown && !attackWasDown) {
            CPSManager.recordLeftClick();
        }
        attackWasDown = attackDown;

        boolean useDown = mc.options.keyUse.isDown();
        if (useDown && !useWasDown) {
            CPSManager.recordRightClick();
        }
        useWasDown = useDown;
    }

    private static void tickModeC(Minecraft mc, LocalPlayer player) {
        ClientInput input = player.input;
        boolean forwardHeld = mc.options.keyUp.isDown();
        boolean shouldMove = mc.screen == null
            && input != null
            && (forwardHeld || input.hasForwardImpulse())
            && !input.keyPresses.shift()
            && !player.isCrouching()
            && !player.isUsingItem()
            && !player.isPassenger()
            && !player.isSwimming()
            && !player.isFallFlying();

        if (!shouldMove) {
            releaseMovementKey(mc);
            return;
        }

        mc.options.keySprint.setDown(true);
        player.setSprinting(true);
        movementKeyOverridden = true;
    }

    private static void releaseMovementKey(Minecraft mc) {
        if (!movementKeyOverridden) {
            return;
        }
        mc.options.keySprint.setDown(false);
        movementKeyOverridden = false;
    }

    private static void tickModeB(LocalPlayer player) {
        if (!OneaurasCPSCounterClient.MODE_B.enabled()) {
            lastHurtTime = player.hurtTime;
            OneaurasCPSCounterClient.finishModeB();
            return;
        }

        if (player.hurtTime > lastHurtTime && rollChance(ConfigManager.get().betaChance)) {
            OneaurasCPSCounterClient.openModeBWindow();
        }
        lastHurtTime = player.hurtTime;
        OneaurasCPSCounterClient.tickModeBWindow();

        if (!OneaurasCPSCounterClient.shouldRunModeB()) {
            return;
        }
        if (!player.onGround() || player.isSwimming() || player.isPassenger() || player.isFallFlying()) {
            return;
        }

        player.input.makeJump();
        OneaurasCPSCounterClient.finishModeB();
    }

    private static void tickModeA(Minecraft mc, LocalPlayer player) {
        long now = System.currentTimeMillis();
        refreshFocus(player, mc);
        if (!OneaurasCPSCounterClient.MODE_A.enabled()) {
            clearFocus();
            OneaurasCPSCounterClient.resetModeACooldown();
            return;
        }
        if (mc.screen != null) {
            OneaurasCPSCounterClient.resetModeACooldown();
            return;
        }
        if (isFocusSwapRecent(now, FOCUS_SWAP_GRACE_MS)) {
            OneaurasCPSCounterClient.resetModeACooldown();
            return;
        }
        if (!player.getMainHandItem().is(ItemTags.SWORDS)) {
            OneaurasCPSCounterClient.resetModeACooldown();
            return;
        }
        if (player.getAttackStrengthScale(0.0f) < OneaurasCPSCounterClient.modeACooldownTarget()) {
            return;
        }
        if (!(mc.hitResult instanceof EntityHitResult hit) || hit.getType() != HitResult.Type.ENTITY) {
            return;
        }

        Entity entity = hit.getEntity();
        if (!(entity instanceof Player target) || target == player) {
            return;
        }
        if (!target.isAlive() || target.isSpectator() || !target.isAttackable()) {
            return;
        }
        if (!player.hasLineOfSight(target)) {
            return;
        }
        if (!isFocusAllowed(target)) {
            return;
        }
        if (player.distanceTo(target) > 3.6f) {
            return;
        }
        if (mc.gameMode == null) {
            return;
        }

        internalAttack = true;
        try {
            mc.gameMode.attack(player, target);
        } finally {
            internalAttack = false;
        }
        player.swing(InteractionHand.MAIN_HAND);
        CPSManager.recordLeftClick();
        captureFocusIfEmpty(target);
        OneaurasCPSCounterClient.resetModeACooldown();
    }

    public static void mark(Entity entity) {
        if (internalAttack || !(entity instanceof Player target)) {
            return;
        }
        if (!target.isAlive() || target.isSpectator() || !target.isAttackable()) {
            return;
        }
        lastFocusSwapAt = System.currentTimeMillis();
        focusTargetId = target.getUUID();
        OneaurasCPSCounterClient.resetModeACooldown();
    }

    private static void captureFocusIfEmpty(Entity entity) {
        if (focusTargetId != null) {
            return;
        }
        if (entity instanceof Player target && target.isAlive() && !target.isSpectator() && target.isAttackable()) {
            focusTargetId = target.getUUID();
        }
    }

    private static boolean isFocusSwapRecent(long now, long windowMs) {
        return lastFocusSwapAt > 0L && now - lastFocusSwapAt < windowMs;
    }

    private static boolean isFocusAllowed(Player target) {
        return focusTargetId == null || focusTargetId.equals(target.getUUID());
    }

    private static void refreshFocus(LocalPlayer player, Minecraft mc) {
        if (focusTargetId == null || mc.level == null) {
            return;
        }
        Entity tracked = mc.level.getEntity(focusTargetId);
        if (!(tracked instanceof Player target)
            || !target.isAlive()
            || target.isSpectator()
            || player.distanceToSqr(target) > 256.0) {
            clearFocus();
        }
    }

    private static void clearFocus() {
        focusTargetId = null;
    }

    private static boolean rollChance(int percent) {
        return percent >= 100 || Math.random() * 100.0 < percent;
    }
}
