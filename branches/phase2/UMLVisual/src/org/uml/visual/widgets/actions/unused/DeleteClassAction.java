package org.uml.visual.widgets.actions.unused;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author "NUGS"
 */
public class DeleteClassAction extends AbstractAction {

    Widget widget;

    public DeleteClassAction(Widget widget) {
        super("Delete Class Action");
        this.widget = widget;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        widget.removeFromParent();
    }
}
