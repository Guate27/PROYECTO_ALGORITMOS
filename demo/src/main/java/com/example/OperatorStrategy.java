package com.example;

import java.util.List;

public interface OperatorStrategy {
    Object execute(List<com.example.Expression> args, Environment env);
}
