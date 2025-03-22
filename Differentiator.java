import java.util.Scanner;

abstract class Expression {
    abstract Expression differentiate();
    abstract Expression simplify();
    public abstract String toString();
}

class Constant extends Expression {
    private final double value;

    public Constant(double value) {
        this.value = value;
    }

    @Override
    Expression differentiate() {
        return new Constant(0);
    }

    @Override
    Expression simplify() {
        return this;
    }

    public double getValue() {  // Getter method to access value
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

class Variable extends Expression {
    @Override
    Expression differentiate() {
        return new Constant(1);
    }

    @Override
    Expression simplify() {
        return this;
    }

    @Override
    public String toString() {
        return "x";
    }
}

class Power extends Expression {
    private final Expression base;
    private final int exponent;

    public Power(Expression base, int exponent) {
        this.base = base;
        this.exponent = exponent;
    }

    @Override
    Expression differentiate() {
        if (exponent == 0) {
            return new Constant(0);
        }
        return new Product(new Constant(exponent), new Power(base, exponent - 1)).simplify();
    }

    @Override
    Expression simplify() {
        if (exponent == 0) return new Constant(1);
        if (exponent == 1) return base.simplify();
        return this;
    }

    @Override
    public String toString() {
        return base.toString() + "^" + exponent;
    }
}

class Sum extends Expression {
    private final Expression left;
    private final Expression right;

    public Sum(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    Expression differentiate() {
        return new Sum(left.differentiate(), right.differentiate()).simplify();
    }

    @Override
    Expression simplify() {
        if (left instanceof Constant && ((Constant) left).getValue() == 0) return right;
        if (right instanceof Constant && ((Constant) right).getValue() == 0) return left;
        return this;
    }


    @Override
    public String toString() {
        return "(" + left + " + " + right + ")";
    }
}

class Product extends Expression {
    private final Expression left;
    private final Expression right;

    public Product(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    Expression differentiate() {
        return new Sum(new Product(left.differentiate(), right), new Product(left, right.differentiate())).simplify();
    }

    @Override
    Expression simplify() {
        if (left instanceof Constant && ((Constant) left).getValue() == 0) return new Constant(0);
        if (right instanceof Constant && ((Constant) right).getValue() == 0) return new Constant(0);
        if (left instanceof Constant && ((Constant) left).getValue() == 1) return right;
        if (right instanceof Constant && ((Constant) right).getValue() == 1) return left;
        return this;
    }


    @Override
    public String toString() {
        return "(" + left + " * " + right + ")";
    }
}

class Tokenizer {
    private final String input;
    private int pos = 0;

    public Tokenizer(String input) {
        this.input = input;
    }

    public boolean hasNext() {
        return pos < input.length();
    }

    public char peek() {
        return input.charAt(pos);
    }

    public char next() {
        return input.charAt(pos++);
    }

    public String nextToken() {
        StringBuilder token = new StringBuilder();
        while (pos < input.length() && (Character.isDigit(input.charAt(pos)) || input.charAt(pos) == '.')) {
            token.append(input.charAt(pos++));
        }
        return token.toString();
    }
}

public class Differentiator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a function of x to differentiate, e.g., '(x^3 + 5 * x) * x': ");
        String input = scanner.nextLine();

        try {
            Expression expr = parseExpression(input);
            System.out.println("Original Expression: " + expr);
            System.out.println("Differentiated Expression: " + expr.differentiate().simplify());
        } catch (Exception e) {
            System.out.println("Error parsing expression: " + e.getMessage());
        }

        scanner.close();
    }

    public static Expression parseExpression(String input) throws Exception {
        input = input.replaceAll(" ", "");
        return parseSum(new Tokenizer(input));
    }

    private static Expression parseSum(Tokenizer tokenizer) throws Exception {
        Expression expr = parseProduct(tokenizer);
        while (tokenizer.hasNext() && tokenizer.peek() == '+') {
            tokenizer.next();
            expr = new Sum(expr, parseProduct(tokenizer));
        }
        return expr;
    }

    private static Expression parseProduct(Tokenizer tokenizer) throws Exception {
        Expression expr = parsePower(tokenizer);
        while (tokenizer.hasNext() && tokenizer.peek() == '*') {
            tokenizer.next();
            expr = new Product(expr, parsePower(tokenizer));
        }
        return expr;
    }

    private static Expression parsePower(Tokenizer tokenizer) throws Exception {
        Expression base = parseFactor(tokenizer);
        if (tokenizer.hasNext() && tokenizer.peek() == '^') {
            tokenizer.next();
            int exponent = Integer.parseInt(tokenizer.nextToken());
            return new Power(base, exponent);
        }
        return base;
    }

    private static Expression parseFactor(Tokenizer tokenizer) throws Exception {
        if (!tokenizer.hasNext()) throw new Exception("Unexpected end of expression");

        char next = tokenizer.peek();
        if (next == '(') {
            tokenizer.next();
            Expression expr = parseSum(tokenizer);
            if (tokenizer.next() != ')') throw new Exception("Expected closing parenthesis");
            return expr;
        } else if (Character.isDigit(next)) {
            return new Constant(Double.parseDouble(tokenizer.nextToken()));
        } else if (next == 'x') {
            tokenizer.next();
            return new Variable();
        } else {
            throw new Exception("Unexpected character: " + next);
        }
    }
}
