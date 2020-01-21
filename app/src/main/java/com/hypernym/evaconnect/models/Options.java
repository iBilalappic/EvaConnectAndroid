package com.hypernym.evaconnect.models;

import android.graphics.Color;

public class Options {
    private int color;
    private int elevation;
    private String text;
    private boolean isMainCategory;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getElevation() {
        return elevation;
    }

    public void setElevation(int elevation) {
        this.elevation = elevation;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isMainCategory() {
        return isMainCategory;
    }

    public void setMainCategory(boolean mainCategory) {
        isMainCategory = mainCategory;
    }
}
