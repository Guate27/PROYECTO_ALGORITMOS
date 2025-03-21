
import com.example.LispInterpreter;
import com.example.OperatorStrategy;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

class ExpressionTest {
    private Environment mockEnv;
    private OperatorStrategy mockStrategy;

    @BeforeEach
    void setUp() {
        mockEnv = Mockito.mock(Environment.class);
        mockStrategy = Mockito.mock(OperatorStrategy.class);
    }

    @Test
    void testSymbolExpression_WhenVariableExists() {
        Mockito.when(mockEnv.isVariable("x")).thenReturn(true);
        Mockito.when(mockEnv.getVariable("x")).thenReturn(42);

        SymbolExpression expr = new SymbolExpression("x");
        assertEquals(42, expr.evaluate(mockEnv));
    }

    @Test
    void testSymbolExpression_WhenVariableDoesNotExist() {
        Mockito.when(mockEnv.isVariable("y")).thenReturn(false);
        
        SymbolExpression expr = new SymbolExpression("y");
        assertEquals("y", expr.evaluate(mockEnv));
    }

    @Test
    void testLiteralExpression() {
        LiteralExpression expr = new LiteralExpression(100);
        assertEquals(100, expr.evaluate(mockEnv));
    }
    
    @Test
    void testLiteralExpression_ToString() {
        LiteralExpression expr = new LiteralExpression("hello");
        assertEquals("hello", expr.toString());
    }
    
    @Test
    void testSymbolExpression_ToString() {
        SymbolExpression expr = new SymbolExpression("z");
        assertEquals("z", expr.toString());
    }

    @Test
    void testListExpression_Empty() {
        ListExpression expr = new ListExpression(Collections.emptyList());
        assertEquals(Collections.emptyList(), expr.evaluate(mockEnv));
    }

    @Test
    void testListExpression_WithOperator() {
        SymbolExpression operator = new SymbolExpression("+");
        LiteralExpression operand1 = new LiteralExpression(2);
        LiteralExpression operand2 = new LiteralExpression(3);
        ListExpression expr = new ListExpression(Arrays.asList(operator, operand1, operand2));

        Mockito.when(mockEnv.getOperator("+")).thenReturn(mockStrategy);
        Mockito.when(mockStrategy.execute(Arrays.asList(operand1, operand2), mockEnv)).thenReturn(5);

        assertEquals(5, expr.evaluate(mockEnv));
    }

    @Test
    void testLispParser_SimpleExpression() {
        LispParser parser = new LispParser();
        Expression expr = parser.parse("(+ 1 2)");
        assertNotNull(expr);
        assertTrue(expr instanceof ListExpression);
    }

    @Test
    void testLispInterpreter_BasicEvaluation() {
        LispInterpreter interpreter = new LispInterpreter();
        Object result = interpreter.evaluate("(+ 2 3)");
        assertEquals(5, result);
    }
}
