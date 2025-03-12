class SymbolExpression implements Expression {
    private final String name;
    
    public SymbolExpression(String name) {
        this.name = name;
    }
    
    @Override
    public Object evaluate(Environment env) {
        if (env.isVariable(name)) {
            return env.getVariable(name);
        }
        return name; // Si no es una variable, se devuelve como símbolo
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}