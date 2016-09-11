package com.o2cinemas.models;

/**
 * Created by admin on 9/8/16.
 */
public class PartDetails {
    private String partName;
    private String path;
    private boolean isSelected;

    public PartDetails(String partName, String path, boolean isSelected) {
        this.partName = partName;
        this.path = path;
        this.isSelected = isSelected;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
