package bool;

import java.util.ArrayList;

public class CompositeNode implements BooleanNode {
    private String operation;
    private ArrayList<BooleanNode> children;

    public CompositeNode(String operation) {
        this.operation = operation;
        this.children = new ArrayList<BooleanNode>();
    }

    public void add(BooleanNode child) {
        children.add(child);
    }

    public void remove(BooleanNode child) {
        children.remove(child);
    }

    public ArrayList<BooleanNode> getChildren() {
        return children;
    }

    @Override
    public boolean evaluate() {
        
        boolean value = false;

        if (operation.equals("AND")) {
            value = true;
            for (BooleanNode child : children) {
                if (!child.evaluate()) {
                    value = false;
                    break;
                }
            }

        } else if (operation.equals("OR")) {
            for (BooleanNode child : children) {
                if (child.evaluate()) {
                    value = true;
                    break;
                }
            }

        } else {
            BooleanNode child = children.get(0);
            value = child.evaluate();
        }

        return value;
    }

    @Override
    public String prettyString() {
        String child1String = children.get(0).prettyString();
        String child2String = children.get(1).prettyString();

        return "(" + operation + " " + child1String + " " + child2String + ")";
    }
}
