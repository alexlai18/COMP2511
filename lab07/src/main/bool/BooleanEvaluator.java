package bool;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BooleanEvaluator {

    public static boolean evaluate(BooleanNode expression) {
        // Return the expression evaluated
        return expression.evaluate();
    }

    public static String prettyPrint(BooleanNode expression) {
        // Pretty print the expression
        return expression.prettyString();
    }

    public static void main(String[] args) {
        BooleanNode trueNode = new ComponentNode(true);
        BooleanNode falseNode = new ComponentNode(false);
        
        CompositeNode line1 = new CompositeNode("AND");
        line1.add(trueNode);
        line1.add(falseNode);
        
        assertFalse(line1.evaluate());
        System.out.println("\n");
        System.out.println("Expression: " + line1.prettyString());
        System.out.println("Equivalent Value: " + line1.evaluate() + "\n");

        line1.remove(falseNode);
        line1.add(trueNode);

        assertTrue(line1.evaluate());
        System.out.println("Expression: " + line1.prettyString());
        System.out.println("Equivalent Value: " + line1.evaluate() + "\n");

        line1.remove(trueNode);

        CompositeNode line2 = new CompositeNode("OR");
        line2.add(trueNode);
        line2.add(falseNode);
        
        assertTrue(line2.evaluate());
        System.out.println("Expression: " + line2.prettyString());
        System.out.println("Equivalent Value: " + line2.evaluate() + "\n");
        
        line2.remove(trueNode);
        line2.add(falseNode);
        
        assertFalse(line2.evaluate());
        System.out.println("Expression: " + line2.prettyString());
        System.out.println("Equivalent Value: " + line2.evaluate() + "\n");

        line2.remove(falseNode);
        line2.add(trueNode);

        line1.add(line2);

        assertTrue(line1.evaluate());
        System.out.println("Expression: " + line1.prettyString());
        System.out.println("Equivalent Value: " + line1.evaluate() + "\n");

        line1.remove(trueNode);
        line1.add(falseNode);

        assertFalse(line1.evaluate());
        System.out.println("Expression: " + line1.prettyString());
        System.out.println("Equivalent Value: " + line1.evaluate() + "\n");

        line1.remove(line2);
        line1.add(falseNode);

        line2.remove(falseNode);
        line2.add(line1);

        assertTrue(line2.evaluate());
        System.out.println("Expression: " + line2.prettyString());
        System.out.println("Equivalent Value: " + line2.evaluate() + "\n");

        line1.remove(falseNode);
        line1.add(trueNode);

        assertTrue(line2.evaluate());
        System.out.println("Expression: " + line2.prettyString());
        System.out.println("Equivalent Value: " + line2.evaluate() + "\n");

        line2.remove(trueNode);
        line2.add(falseNode);

        assertFalse(line2.evaluate());
        System.out.println("Expression: " + line2.prettyString());
        System.out.println("Equivalent Value: " + line2.evaluate() + "\n");
    }

}