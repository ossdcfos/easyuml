/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.providers;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.ClassDiagram;
import org.uml.visual.widgets.PackageWidget;

/**
 *
 * @author Uros
 */
public class PackagePopupMenuProvider implements PopupMenuProvider{

    private JPopupMenu menu;
    private JMenuItem deletePackage;
    private PackageWidget packageWidget;

    public PackagePopupMenuProvider(PackageWidget packageWidget) {
        this.packageWidget = packageWidget;
        
        menu = new JPopupMenu("Package Menu");
        
        (deletePackage = new JMenuItem("Delete Package")).addActionListener(removeWidgetListener);
        menu.add(deletePackage);
    }
    
        ActionListener removeWidgetListener = new ActionListener() {
        
        @Override
        public void actionPerformed(ActionEvent e) {
           packageWidget.getComponent().getParentDiagram().removeComponent(packageWidget.getName());
           ClassDiagram classDiagram = packageWidget.getComponent().getParentDiagram();
           
           /*for(Map.Entry<String,RelationComponent> entry : classDiagram.getRelations().entrySet()) {
                RelationComponent relation = entry.getValue();
                if (relation.getSource().getName().equals(classWidget.getClassName())||relation.getTarget().getName().equals(classWidget.getClassName())) {
                   classDiagram.removeRelation(relation.getName());
                   classWidget.getClassDiagramScene().removeEdge(relation);
               }
           }*/
           
           packageWidget.removeFromParent();
           
        }
    };
    
    
    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
    
}
