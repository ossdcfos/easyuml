/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets;

import org.openide.ErrorManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import org.netbeans.api.visual.widget.*;
/**
 *
 * @author gosselin
 */
public class ResampledImageWidget extends Widget {

    private Image image;
    private Image disabledImage;
    private int width, height;
    private boolean paintAsDisabled;
    private ImageObserver observer = new ImageObserver() {
        public boolean imageUpdate (Image img, int infoflags, int x, int y, int width, int height) {
//            System.out.println ("INFO: " + infoflags);
            setImageCore (image);
            getScene ().validate ();
            return (infoflags & (ImageObserver.ABORT | ImageObserver.ERROR)) == 0;
        }
    };

    /**
     * Creates an image widget.
     * @param scene the scene
     */
    public ResampledImageWidget (Scene scene) {
        super (scene);
    }

    /**
     * Creates an image widget.
     * @param scene the scene
     * @param image the image
     */
    public ResampledImageWidget (Scene scene, Image image) {
        super (scene);
        setImage (image);
    }

    /**
     * Returns an image.
     * @return the image
     */
    public Image getImage () {
        return image;
    }

    /**
     * Sets an image
     * @param image the image
     */
    public void setImage (Image image) {
        if (this.image == image)
            return;
        setImageCore (image);
    }

    private void setImageCore (Image image) {
        if (image == this.image) {
            return;
        }
        int oldWidth = width;
        int oldHeight = height;

        this.image = image;
        this.disabledImage = null;
        width = image != null ? image.getWidth (null) : 0;
        height = image != null ? image.getHeight (null) : 0;

        if (oldWidth == width  &&  oldHeight == height)
            repaint ();
        else
            revalidate ();
    }

    /**
     * Returns whether the label is painted as disabled.
     * @return true, if the label is painted as disabled
     */
    public boolean isPaintAsDisabled () {
        return paintAsDisabled;
    }

    /**
     * Sets whether the label is painted as disabled.
     * @param paintAsDisabled if true, then the label is painted as disabled
     */
    public void setPaintAsDisabled (boolean paintAsDisabled) {
        boolean repaint = this.paintAsDisabled != paintAsDisabled;
        this.paintAsDisabled = paintAsDisabled;
        if (repaint)
            repaint ();
    }

    /**
     * Calculates a client area of the image
     * @return the calculated client area
     */
    protected Rectangle calculateClientArea () {
        if (image != null)
            return new Rectangle (0, 0, width, height);
        return super.calculateClientArea ();
    }

    /**
     * Paints the image widget.
     */
    protected void paintWidget () {
        if (image == null)
            return;
        Graphics2D gr = getGraphics ();
        if (image != null) {
            if (paintAsDisabled) {
                if (disabledImage == null) {
                    disabledImage = GrayFilter.createDisabledImage (image);
                    MediaTracker tracker = new MediaTracker (getScene ().getView ());
                    tracker.addImage (disabledImage, 0);
                    try {
                        tracker.waitForAll ();
                    } catch (InterruptedException e) {
                        ErrorManager.getDefault ().notify (e);
                    }
                }
                gr.drawImage (disabledImage, 
                    0, 0, getMaximumSize().width, getMaximumSize().height,
                    0, 0, width, height,
                    observer);
            } else {
                gr.drawImage (image, 
                    0, 0, getMaximumSize().width, getMaximumSize().height,
                    0, 0, width, height,
                    observer);
            }
        }
    }

}
