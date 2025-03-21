package com.example;

import java.util.*;

class LispParser {
    public Expression parse(String input) {
        List<String> tokens = tokenize(input);
        return parseExpression(tokens, new int[]{0});
    }
    
    private List<String> tokenize(String input) {
        input = input.replace("(", " ( ").replace(")", " ) ");
        return new ArrayList<>(Arrays.asList(input.trim().split("\\s+")));
    }
    
    private Expression parseExpression(List<String> tokens, int[] position) {
        if (position[0] >= tokens.size()) {
            throw new RuntimeException("Fin inesperado de la entrada");
        }
        
        String token = tokens.get(position[0]++);
        
        if ("(".equals(token)) {
            List<Expression> elements = new ArrayList<>();
            while (position[0] < tokens.size() && !")".equals(tokens.get(position[0]))) {
                elements.add(parseExpression(tokens, position));
            }
            
            if (position[0] >= tokens.size()) {
                throw new RuntimeException("Paréntesis de cierre faltante");
            }
            
            position[0]++; // Consumir el paréntesis de cierre
            return new ListExpression(elements);
        } else if (")".equals(token)) {
            throw new RuntimeException("Paréntesis de cierre inesperado");
        } else {
            // Intentar interpretar como número
            try {
                return new LiteralExpression(Integer.parseInt(token));
            } catch (NumberFormatException e) {
                // Si no es un número, es un símbolo
                return new SymbolExpression(token);
            }
        }
    }
}