package org.uml.visual.widgets.popups;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.relations.RelationBase;
import org.uml.visual.dialogs.CardinalityChangePanel;

/**
 *
 * @author Boris
 */
public class CardinalityPopupMenuProvider implements PopupMenuProvider {

    private final JPopupMenu menu;
    private final JMenuItem changeCardinality;

    public CardinalityPopupMenuProvider(final RelationBase relationB, final LabelWidget cardinalityLbl, final boolean src) {
        menu = new JPopupMenu("Cardinality Menu");

        (changeCardinality = new JMenuItem("Change cardinality")).addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                CardinalityChangePanel panel = new CardinalityChangePanel(relationB, src, cardinalityLbl);
                panel.openRelationDialog();
            }
        });
        menu.add(changeCardinality);
    }

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point localLocation) {
        return menu;
    }
}
