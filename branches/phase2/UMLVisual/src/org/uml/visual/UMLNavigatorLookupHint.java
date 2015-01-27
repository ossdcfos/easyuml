package org.uml.visual;

import org.netbeans.spi.navigator.NavigatorLookupHint;

public class UMLNavigatorLookupHint implements NavigatorLookupHint {

    @Override
    public String getContentType() {
        return "application/uml";
    }
}
