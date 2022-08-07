package bool;

public class ComponentNode implements BooleanNode {
    Boolean booleanExpression;

    //Constructor
    public ComponentNode(Boolean bool) {
        booleanExpression = bool;
    }

    @Override
    public boolean evaluate() {
        return booleanExpression;
    }

    @Override
    public String prettyString() {
        return booleanExpression.toString();
    }
}
