package org.uml.visual.navigator;

import java.awt.BorderLayout;
import java.util.Collections;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.netbeans.spi.navigator.NavigatorPanel;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.uml.visual.widgets.ClassDiagramScene;

@NavigatorPanel.Registration(mimeType = "application/uml", displayName = "UML Diagram Satellite View")
public class UMLNavigatorPanel extends JPanel implements NavigatorPanel, LookupListener {

    JScrollPane pane;
    Lookup lookup;
    InstanceContent ic = new InstanceContent();
    Result<ClassDiagramScene> scenesInLookup;
    ClassDiagramScene currentScene;

    public UMLNavigatorPanel() {
        setLayout(new BorderLayout());
        pane = new JScrollPane();
        add(pane, BorderLayout.CENTER);
        lookup = new AbstractLookup(ic);
    }

    @Override
    public void panelActivated(Lookup lkp) {
        scenesInLookup = Utilities.actionsGlobalContext().lookupResult(ClassDiagramScene.class);
        scenesInLookup.addLookupListener(this);
        resultChanged(new LookupEvent(scenesInLookup));
    }

    @Override
    public void panelDeactivated() {
        scenesInLookup.removeLookupListener(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void resultChanged(LookupEvent le) {
        if (!scenesInLookup.allInstances().isEmpty()) {
            ClassDiagramScene s = scenesInLookup.allInstances().iterator().next();
            if (s != null) {
                pane.setViewportView(s.createSatelliteView());
                ic.set(Collections.singleton(s), null);
                currentScene = s;
            }
        } else {
//            if (!isActivatedLinkedTC()) {
//                emptyNavigator();
//            }
        }
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    @Override
    public String getDisplayName() {
        return "UML Diagram Satellite View";
    }

    @Override
    public String getDisplayHint() {
        return "UML Diagram Satellite View";
    }

    @Override
    public JComponent getComponent() {
        return this;
    }
    
     private void emptyNavigator() {
        JLabel lblNothingSelected = new JLabel("<no file selected>");
        lblNothingSelected.setHorizontalAlignment(JLabel.CENTER);
        pane.setViewportView(lblNothingSelected);
        ic.set(Collections.EMPTY_SET, null);
        currentScene = null;
    }

//    private boolean isActivatedLinkedTC() {
//        // current activated
//        TopComponent activatedTC = WindowManager.getDefault().getRegistry().getActivated();
//        // linked
//        TopComponent projectsLogicalTC = WindowManager.getDefault().findTopComponent("projectTabLogical_tc");
//        ClassDiagram projectsSelectedClassDiagram = projectsLogicalTC.getLookup().lookup(ClassDiagram.class);
//        TopComponent propertiesTC = WindowManager.getDefault().findTopComponent("properties");
//        TopComponent paletteTC = WindowManager.getDefault().findTopComponent("CommonPalette");
//        TopComponent navigatorTC = WindowManager.getDefault().findTopComponent("navigatorTC");
//
//        if (activatedTC == navigatorTC && (isCurrentSceneOpened() || isValidSceneShown(projectsSelectedClassDiagram))
//                || activatedTC == paletteTC && isCurrentSceneOpened()
//                || activatedTC == propertiesTC //&& isAnyUMLTopComponentOpen()
//                || isUMLTopComponent(activatedTC)
//                // problem when 2 are open at same time, close one, focus component from another, it gets emptied,
//                // as that component doesn't have class diagram in its lookup
//                && isSceneCorrespondingToUMLTC(activatedTC)) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    private boolean isUMLTopComponent(TopComponent tc) {
//        if (tc.getClass().getSimpleName().equals("UMLTopComponent")) return true;
//        else return false;
//    }
//
////    private boolean isAnyUMLTopComponentOpen() {
////        for (TopComponent tc : WindowManager.getDefault().getRegistry().getOpened()) {
////            if (isUMLTopComponent(tc)) return true;
////        }
////        return false;
////    }
////    private boolean isDeselectAllSituation() {
////        TopComponent activatedTC = WindowManager.getDefault().getRegistry().getActivated();
////        TopComponent paletteTC = WindowManager.getDefault().findTopComponent("CommonPalette");
////        TopComponent projectsLogicalTC = WindowManager.getDefault().findTopComponent("projectTabLogical_tc");
////        if (activatedTC == paletteTC && isCurrentSceneOpened() || activatedTC == projectsLogicalTC) {
////            return true;
////        } else {
////            return false;
////        }
////    }
//
//    private boolean isCurrentSceneOpened() {
//        Set<TopComponent> tcs = WindowManager.getDefault().getRegistry().getOpened();
//        for (TopComponent tc : tcs) {
//            // Maybe remove
//            if (isUMLTopComponent(tc)) { // Class name is hardcoded, because we cannot access UMLTopComponent from here because of cyclic dependency
//                ObjectScene os = tc.getLookup().lookup(ObjectScene.class);
//                if (os.equals(currentScene))
//                    return true;
//            }
//        }
//        return false;
//    }
//
//    private boolean isValidSceneShown(ClassDiagram diagram) {
//        if (currentScene.getClassDiagram() == diagram) {
//            return true;
//        }
//        emptyNavigator();
//        return false;
//    }
//
//    private boolean isSceneCorrespondingToUMLTC(TopComponent tc) {
//        ClassDiagram classDiagram = currentScene.getClassDiagram();
//        ClassDiagram tcClassDiagram = tc.getLookup().lookup(ClassDiagram.class);
//        ComponentBase tcComponentBase = tc.getLookup().lookup(ComponentBase.class);
//        MemberBase tcMemberBase = tc.getLookup().lookup(MemberBase.class);
//
//        if (classDiagram == tcClassDiagram) {
//            return true;
//        } else if (tcComponentBase != null && classDiagram == tcComponentBase.getParentDiagram()) {
//            return true;
//        } else if (tcMemberBase != null && tcMemberBase.getDeclaringComponent() != null && classDiagram == tcMemberBase.getDeclaringComponent().getParentDiagram()) {
//            return true;
//        }
//        return false;
//    }
}
