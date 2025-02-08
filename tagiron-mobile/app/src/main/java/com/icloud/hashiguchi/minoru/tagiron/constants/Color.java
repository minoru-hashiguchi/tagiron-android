package com.icloud.hashiguchi.minoru.tagiron.constants;

import com.icloud.hashiguchi.tagironmobile.R;

public enum Color {
    RED(),
    BLUE(),
    YELLOW(),
    ;

    @Override
    public String toString() {
        return this.name().substring(0, 1);
    }

    public Color toggle() {
        switch (this) {
            case YELLOW:
                return YELLOW;
            case RED:
                return BLUE;
            case BLUE:
                return RED;
            default:
                throw new IllegalArgumentException("Unexpected value: " + this);
        }
    }

    public int getColorInt() {
        switch (this) {
            case YELLOW:
                return R.color.tile_color_yellow;
            case RED:
                return R.color.tile_color_red;
            case BLUE:
                return R.color.tile_color_blue;
            default:
                return R.color.tile_color_undefined;
        }
    }
}
