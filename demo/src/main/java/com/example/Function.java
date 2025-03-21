package com.example;

import java.util.*;

class Function {
    private final List<String> parameters;
    private final Expression body;
    
    public Function(List<String> parameters, Expression body2) {
        this.parameters = parameters;
        this.body = body2;
    }
    
    public List<String> getParameters() {
        return parameters;
    }
    
    public Expression getBody() {
        return body;
    }
}