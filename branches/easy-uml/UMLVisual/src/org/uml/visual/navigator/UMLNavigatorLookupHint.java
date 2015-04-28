package org.uml.visual.navigator;

import org.netbeans.spi.navigator.NavigatorLookupHint;

public class UMLNavigatorLookupHint implements NavigatorLookupHint {

    @Override
    public String getContentType() {
        return "application/uml";
    }
}
