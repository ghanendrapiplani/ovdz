package com.o2cinemas.models;

/**
 * Created by admin on 9/10/16.
 */
public class AlphabetSortItem {
    String value;
    boolean isSelected;

    public AlphabetSortItem(String value, boolean isSelected) {
        this.value = value;
        this.isSelected = isSelected;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
