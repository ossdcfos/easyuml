package org.uml.explorer.unused;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node.Cookie;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Jelena
 */
public class CategoryNode extends AbstractNode {

    Category category;

    /**
     * Creates a new instance of CategoryNode
     */
    public CategoryNode(Category category) {
        this(category, new InstanceContent());
        setDisplayName(category.getName());
        //setIconBaseWithExtension("org/netbeans/myfirstexplorer/marilyn_category.gif");
    }

    private CategoryNode(Category category, InstanceContent content) {
        super(new CategoryChildren(category), new AbstractLookup(content));
        content.add(this);
        content.add(category);
        this.category = category;
    }

////    public PasteType getDropType(Transferable t, final int action, int index) {
////        final Node dropNode = NodeTransfer.node( t, 
////                DnDConstants.ACTION_COPY_OR_MOVE+NodeTransfer.CLIPBOARD_CUT );
////        if( null != dropNode ) {
////            final Movie movie = (Movie)dropNode.getLookup().lookup( Movie.class );
////            if( null != movie  && !this.equals( dropNode.getParentNode() )) {
////                return new PasteType() {
////                    @Override
////                    public Transferable paste() throws IOException {
////                        getChildren().add( new Node[] { new MovieNode(movie) } );
////                        if( (action & DnDConstants.ACTION_MOVE) != 0 ) {
////                            dropNode.getParentNode().getChildren().remove( new Node[] {dropNode} );
////                        }
////                        return null;
////                    }
////                };
////            }
////        }
////        return null;
////    }
//    @Override
//    public Cookie getCookie(Class clazz) {
//        Children ch = getChildren();
//
//        if (clazz.isInstance(ch)) {
//            return (Cookie) ch;
//        }
//
//        return super.getCookie(clazz);
//    }
//
////    @Override
////    protected void createPasteTypes(Transferable t, java.util.List s) {
////        super.createPasteTypes(t, s);
////        PasteType paste = getDropType( t, DnDConstants.ACTION_COPY, -1 );
////        if( null != paste ) {
////            s.add( paste );
////        }
////    }
////    
////    @Override
////    public Action[] getActions(boolean context) {
////        return new Action[] {
////            SystemAction.get( NewAction.class ),
////            SystemAction.get( PasteAction.class ) };
////    }
//    @Override
//    public boolean canDestroy() {
//        return true;
//    }
}
