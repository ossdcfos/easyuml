package org.uml.explorer.unused;

import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Jelena
 */
public class CategoryChildren extends Children.Keys<Category> {

    private Category category;
    private String[] Categories = new String[]{
        "Classes",
        "Interfaces",
        "Enumerations"
    };

    public CategoryChildren(Category category) {
        this.category = category;
    }

    @Override
    protected Node[] createNodes(Category key) {
        Category obj = key;
        return new Node[]{new CategoryNode(obj)};
    }

    @Override
    protected void addNotify() {
        super.addNotify();
        Category[] objs = new Category[Categories.length];
        for (int i = 0; i < objs.length; i++) {
            Category cat = new Category();
            cat.setName(Categories[i]);
            objs[i] = cat;
        }
        setKeys(objs);
    }

}
