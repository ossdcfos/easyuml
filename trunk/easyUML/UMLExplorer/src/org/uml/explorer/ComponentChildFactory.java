package org.uml.explorer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.WeakListeners;
import org.uml.model.components.ComponentBase;
import org.uml.model.members.MemberBase;

/**
 *
 * @author Boris
 */
public class ComponentChildFactory extends ChildFactory<MemberBase> implements PropertyChangeListener {

    private final ComponentBase component;

    public ComponentChildFactory(ComponentBase component) {
        this.component = component;
        this.component.addPropertyChangeListener(WeakListeners.propertyChange(this, this.component));
    }

    @Override
    protected boolean createKeys(List<MemberBase> toPopulate) {
        if (component.getMembers() != null) {
            for (MemberBase member : component.getMembers()) {
                toPopulate.add(member);
            }
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(MemberBase key) {
        return new MemberNode(key);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (null != evt.getPropertyName()) {
            switch (evt.getPropertyName()) {
                case "ADD_COMPONENT":
                    refresh(true);
                    break;
                case "REMOVE_COMPONENT":
                    refresh(true);
                    break;
            }
        }
    }
}
