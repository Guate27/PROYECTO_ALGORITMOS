package com.example;

import java.util.*;

class Function {
    private final List<String> parameters;
    private final Expression body;
    
    public Function(List<String> parameters, Expression body) {
        this.parameters = parameters;
        this.body = body; // Corregido para aceptar la interfaz Expression
    }
    
    public List<String> getParameters() {
        return parameters;
    }
    
    public Expression getBody() {
        return body;
    }
}