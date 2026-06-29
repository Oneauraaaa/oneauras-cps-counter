package com.oneaura.cpscounter;

import java.util.ArrayList;
import java.util.List;

public final class CPSManager {
    private static final List<Long> leftClicks = new ArrayList<>();
    private static final List<Long> rightClicks = new ArrayList<>();

    private CPSManager() {
    }

    public static void recordLeftClick() {
        leftClicks.add(System.currentTimeMillis());
    }

    public static void recordRightClick() {
        rightClicks.add(System.currentTimeMillis());
    }

    public static void tick() {
        tick(System.currentTimeMillis());
    }

    public static void tick(long now) {
        leftClicks.removeIf(time -> time < now - 1000L);
        rightClicks.removeIf(time -> time < now - 1000L);
    }

    public static int getLeftCPS() {
        return leftClicks.size();
    }

    public static int getRightCPS() {
        return rightClicks.size();
    }
}
