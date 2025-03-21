package com.example;

import java.util.*;
import java.io.*;
    
class Environment {
    private final Map<String, Object> variables;
    private final Map<String, Function> functions;
    private final Map<String, OperatorStrategy> operators;
    private final Environment parent;
    
    public Environment() {
        this(null);
    }
    
    public Environment(Environment parent) {
        this.variables = new HashMap<>();
        this.functions = new HashMap<>();
        this.operators = new HashMap<>();
        this.parent = parent;
        registerBuiltInOperators();
    }
    
    private void registerBuiltInOperators() {
        //aritméticos
        operators.put("+", (args, env) -> {
            int sum = 0;
            for (Expression arg : args) {
                sum += Integer.parseInt(arg.evaluate(env).toString());
            }
            return sum;
        });
        
        operators.put("-", (args, env) -> {
            if (args.size() == 1) {
                return -Integer.parseInt(args.get(0).evaluate(env).toString());
            }
            int result = Integer.parseInt(args.get(0).evaluate(env).toString());
            for (int i = 1; i < args.size(); i++) {
                result -= Integer.parseInt(args.get(i).evaluate(env).toString());
            }
            return result;
        });
        
        operators.put("*", (args, env) -> {
            int product = 1;
            for (Expression arg : args) {
                product *= Integer.parseInt(arg.evaluate(env).toString());
            }
            return product;
        });
        
        operators.put("/", (args, env) -> {
            int result = Integer.parseInt(args.get(0).evaluate(env).toString());
            for (int i = 1; i < args.size(); i++) {
                result /= Integer.parseInt(args.get(i).evaluate(env).toString());
            }
            return result;
        });
        
        //comparación
        operators.put("<", (args, env) -> 
            Integer.parseInt(args.get(0).evaluate(env).toString()) < 
            Integer.parseInt(args.get(1).evaluate(env).toString()));
        
        operators.put(">", (args, env) -> 
            Integer.parseInt(args.get(0).evaluate(env).toString()) > 
            Integer.parseInt(args.get(1).evaluate(env).toString()));
        
        operators.put("equal", (args, env) -> 
            args.get(0).evaluate(env).equals(args.get(1).evaluate(env)));
        
        //especiales
        operators.put("setq", (args, env) -> {
            if (args.size() != 2 || !(args.get(0) instanceof SymbolExpression)) {
                throw new RuntimeException("setq requiere un símbolo y un valor");
            }
            String varName = ((SymbolExpression) args.get(0)).getName();
            Object value = args.get(1).evaluate(env);
            env.setVariable(varName, value);
            return value;
        });
        
        operators.put("quote", (args, env) -> args.get(0));
        
        operators.put("'", (args, env) -> args.get(0));
        
        operators.put("list", (args, env) -> {
            List<Object> result = new ArrayList<>();
            for (Expression arg : args) {
                result.add(arg.evaluate(env));
            }
            return result;
        });
        
        operators.put("atom", (args, env) -> {
            Object value = args.get(0).evaluate(env);
            return !(value instanceof List) || ((List<?>) value).isEmpty();
        });
        
        operators.put("print", (args, env) -> {
            Object value = args.get(0).evaluate(env);
            System.out.println(value);
            return value;
        });
        
        operators.put("defun", (args, env) -> {
            if (!(args.get(0) instanceof SymbolExpression)) {
                throw new RuntimeException("El nombre de la función debe ser un símbolo");
            }
            
            String funcName = ((SymbolExpression) args.get(0)).getName();
            
            if (!(args.get(1) instanceof ListExpression)) {
                throw new RuntimeException("Los parámetros deben ser una lista");
            }
            
            List<String> params = new ArrayList<>();
            for (Expression param : ((ListExpression) args.get(1)).getElements()) {
                if (!(param instanceof SymbolExpression)) {
                    throw new RuntimeException("Los parámetros deben ser símbolos");
                }
                params.add(((SymbolExpression) param).getName());
            }
            
            Expression body = args.get(2);
            Function function = new Function(params, body);
            env.defineFunction(funcName, function);
            
            return "Función definida: " + funcName;
        });
        
        operators.put("if", (args, env) -> {
            boolean condition = Boolean.parseBoolean(args.get(0).evaluate(env).toString());
            return condition ? args.get(1).evaluate(env) : args.get(2).evaluate(env);
        });
        
        operators.put("cond", (args, env) -> {
            for (int i = 0; i < args.size(); i += 2) {
                if (i + 1 < args.size()) {
                    boolean condition = Boolean.parseBoolean(args.get(i).evaluate(env).toString());
                    if (condition) {
                        return args.get(i + 1).evaluate(env);
                    }
                }
            }
            return null;
        });
    }
    
    public boolean isVariable(String name) {
        return variables.containsKey(name) || (parent != null && parent.isVariable(name));
    }
    
    public Object getVariable(String name) {
        if (variables.containsKey(name)) {
            return variables.get(name);
        }
        if (parent != null) {
            return parent.getVariable(name);
        }
        throw new RuntimeException("Variable no definida: " + name);
    }
    
    public void setVariable(String name, Object value) {
        if (parent != null && parent.isVariable(name)) {
            parent.setVariable(name, value);
        } else {
            variables.put(name, value);
        }
    }
    
    public boolean isFunction(String name) {
        return functions.containsKey(name) || (parent != null && parent.isFunction(name));
    }
    
    public void defineFunction(String name, Function function) {
        functions.put(name, function);
    }
    
    public Object callFunction(String name, List<Expression> args, Environment callerEnv) {
        Function function;
        if (functions.containsKey(name)) {
            function = functions.get(name);
        } else if (parent != null) {
            return parent.callFunction(name, args, callerEnv);
        } else {
            throw new RuntimeException("Función no definida: " + name);
        }
        
        Environment functionEnv = new Environment(this);
        List<String> params = function.getParameters();
        
        if (params.size() != args.size()) {
            throw new RuntimeException("Número incorrecto de argumentos para " + name);
        }
        
        for (int i = 0; i < params.size(); i++) {
            functionEnv.setVariable(params.get(i), args.get(i).evaluate(callerEnv));
        }
        
        return function.getBody().evaluate(functionEnv);
    }
    
    public OperatorStrategy getOperator(String name) {
        if (operators.containsKey(name)) {
            return operators.get(name);
        }
        if (parent != null) {
            return parent.getOperator(name);
        }
        return null;
    }
}