package org.uml.visual.palette;

/**
 *
 * @author NUGS
 */
public class PaletteItem {

    String icon;
    String title;
    String category;
    Class<?> dropComponentClass;
    String shortDescription;

    public PaletteItem() {
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Class<?> getDropClass() {
        return dropComponentClass;
    }

    public void setDropClass(Class<?> dropClass) {
        this.dropComponentClass = dropClass;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

}
