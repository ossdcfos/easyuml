package org.uml.visual.widgets.providers;

import org.uml.visual.widgets.components.ClassWidget;
import org.uml.visual.widgets.components.ComponentWidgetBase;
import org.uml.visual.widgets.components.InterfaceWidget;
import org.uml.model.relations.ImplementsRelation;
import org.uml.model.relations.IsRelation;
import org.uml.model.relations.UseRelation;
import org.uml.model.relations.RelationBase;
import org.uml.model.relations.HasBaseRelation;
import java.awt.Point;
import org.netbeans.api.visual.action.*;
import org.netbeans.api.visual.widget.*;
import org.openide.*;
import org.openide.util.Exceptions;
import org.uml.visual.dialogs.*;
import org.uml.visual.palette.PaletteItem;
import org.uml.visual.palette.PaletteSupport;

/**
 *
 * @author "NUGS"
 */
public class ComponentConnectProvider implements ConnectProvider {

    DialogDescriptor dd;

    public ComponentConnectProvider() {
    }

    @Override
    public boolean hasCustomTargetWidgetResolver(Scene scene) {
        return false;
    }

    @Override
    public Widget resolveTargetWidget(Scene scene, Point point) {
        return null;
    }

    @Override
    public boolean isSourceWidget(Widget sourceWidget) {
        return (sourceWidget instanceof ClassWidget || sourceWidget instanceof InterfaceWidget);
    }

    @Override
    public ConnectorState isTargetWidget(Widget sourceWidget, Widget targetWidget) {
        if (sourceWidget instanceof ComponentWidgetBase && targetWidget instanceof ComponentWidgetBase) {
            return ConnectorState.ACCEPT;
        } else {
            return ConnectorState.REJECT;
        }
    }

    @Override
    public void createConnection(Widget sourceWidget, Widget targetWidget) {
        PaletteItem pi = PaletteSupport.getPalette().getSelectedItem().lookup(PaletteItem.class);
        ComponentWidgetBase source = (ComponentWidgetBase) sourceWidget;

        try {
            ComponentWidgetBase target = (ComponentWidgetBase) targetWidget;
            if (pi != null && RelationBase.class.isAssignableFrom(pi.getDropClass())) {
                RelationBase relation = (RelationBase) pi.getDropClass().newInstance();
                if (relation.canConnect(source.getComponent(), target.getComponent())) {
                    if (relation instanceof IsRelation) {
                        connect(relation, source, target);
                    } else if (relation instanceof ImplementsRelation) {
                        connect(relation, source, target);
                    } else if (relation instanceof HasBaseRelation) {
                        ConnectRelationPanel panel = new ConnectRelationPanel(source.getClassDiagramScene(), source, target, relation);
                        panel.openRelationDialog();
                    } else if (relation instanceof UseRelation) {
                        ConnectRelationPanel panel = new ConnectRelationPanel(source.getClassDiagramScene(), source, target, relation);
                        panel.openRelationDialog();
                    }
                }
            } else {
                // Create generic dialog with these two components
                ConnectRelationPanel panel = new ConnectRelationPanel(source.getClassDiagramScene(), source, target);
                panel.openRelationDialog();
            }
        } catch (InstantiationException | IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void connect(RelationBase relation, ComponentWidgetBase source, ComponentWidgetBase target) {
        relation.setSource(source.getComponent());
        relation.setTarget(target.getComponent());

        source.getClassDiagramScene().addRelationToScene(relation, source.getComponent(), target.getComponent());
    }
}
