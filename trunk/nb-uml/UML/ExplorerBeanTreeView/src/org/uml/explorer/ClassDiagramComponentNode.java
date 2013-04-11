/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.explorer;

import javax.swing.Action;
import org.openide.actions.CopyAction;
import org.openide.actions.CutAction;
import org.openide.actions.DeleteAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;
import org.uml.model.ClassDiagramComponent;

/**
 *
 * @author Jelena
 */
public class ClassDiagramComponentNode  extends AbstractNode {
    
    private ClassDiagramComponent component;
    
    /** Creates a new instance of InstrumentNode */
    public ClassDiagramComponentNode(ClassDiagramComponent key) {
        super(Children.LEAF, Lookups.fixed( new Object[] {key} ) );
        this.component = key;
        setDisplayName(key.getName());
        //setIconBaseWithExtension("org/netbeans/myfirstexplorer/marilyn.gif");
    }
    
//    @Override
//    public boolean canCut() {
//        
//        return true;
//    }
    
    @Override
    public boolean canDestroy() {
        return true;
    }
    
//    @Override
//    public Action[] getActions(boolean popup) {
//        return new Action[] {
//            SystemAction.get( CopyAction.class ),
//            SystemAction.get( CutAction.class ),
//            null,
//            SystemAction.get( DeleteAction.class ) };
//    }
    
}
