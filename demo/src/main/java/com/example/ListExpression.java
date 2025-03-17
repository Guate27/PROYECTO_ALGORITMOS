package com.example;

import java.util.*;
import java.io.*;

class ListExpression implements Expression {
    private final List<Expression> elements;
    
    public ListExpression(List<Expression> elements) {
        this.elements = elements;
    }
    
    @Override
    public Object evaluate(Environment env) {
        if (elements.isEmpty()) {
            return Collections.emptyList();
        }
        
        // El primer elemento es el operador o función
        Expression first = elements.get(0);
        
        if (first instanceof SymbolExpression) {
            String operator = ((SymbolExpression) first).getName();
            
            // Buscar el operador en la fábrica de operadores
            OperatorStrategy strategy = env.getOperator(operator);
            if (strategy != null) {
                return strategy.execute(elements.subList(1, elements.size()), env);
            }
            
            // Si es una función definida por el usuario
            if (env.isFunction(operator)) {
                return env.callFunction(operator, elements.subList(1, elements.size()), env);
            }
        }
        
        // Si no es un operador conocido, evaluar como una lista normal
        List<Object> results = new ArrayList<>();
        for (Expression expr : elements) {
            results.add(expr.evaluate(env));
        }
        return results;
    }
    
    public List<Expression> getElements() {
        return elements;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < elements.size(); i++) {
            sb.append(elements.get(i));
            if (i < elements.size() - 1) {
                sb.append(" ");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}