/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.palette;

import org.netbeans.api.visual.widget.general.IconNodeWidget;

/**
 *
 * @author NUGS
 */
public class PaletteItem {

    String icon;
    String title;
    String category;
    Class targetWidget;
    Class dropClass;

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

    public Class getDropClass() {
        return dropClass;
    }

    public void setDropClass(Class dropClass) {
        this.dropClass = dropClass;
    }

    public Class getTargetWidget() {
        return targetWidget;
    }

    public void setTargetWidget(Class targetWidget) {
        this.targetWidget = targetWidget;
    }
}
