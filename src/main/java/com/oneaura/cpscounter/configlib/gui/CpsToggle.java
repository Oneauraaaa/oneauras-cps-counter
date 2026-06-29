package com.oneaura.cpscounter.configlib.gui;

public final class CpsToggle {
    private final String name;
    private boolean enabled;

    public CpsToggle(String name, boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }

    public String name() {
        return this.name;
    }

    public boolean enabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void toggle() {
        this.enabled = !this.enabled;
    }
}
