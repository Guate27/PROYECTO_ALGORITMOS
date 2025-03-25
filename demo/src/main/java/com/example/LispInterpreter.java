package com.example;

import java.util.Scanner;
import java.util.List;

public class LispInterpreter {
    private final Environment globalEnv;
    private final LispParser parser;
    
    public LispInterpreter() {
        this.globalEnv = new Environment();
        this.parser = new LispParser();
    }
    
    public Object evaluate(String input) {
        List<Expression> expressions = parser.parseMultiple(input); // Cambiar para manejar múltiples expresiones
        Object result = null;
        for (Expression expr : expressions) {
            result = expr.evaluate(globalEnv); // Evaluar cada expresión
        }
        return result; // Retornar el resultado de la última expresión
    }
    
    public static void main(String[] args) {
        LispInterpreter interpreter = new LispInterpreter();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Bienvenido al intérprete LISP Orientado a Objetos. Escribe 'salir' para terminar.");
        while (true) {
            System.out.print("LISP> ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("salir")) break;
            try {
                Object resultado = interpreter.evaluate(input);
                System.out.println("Resultado: " + resultado);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
        scanner.close();
    }
}
