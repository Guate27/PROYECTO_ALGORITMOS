Clase Function:
class Function {
    private final List<String> parameters;
    private final Expression body;
    
    public Function(List<String> parameters, Expression body) {
        this.parameters = parameters;
        this.body = body;
    }
    
    public List<String> getParameters() {
        return parameters;
    }
    
    public Expression getBody() {
        return body;
    }
}