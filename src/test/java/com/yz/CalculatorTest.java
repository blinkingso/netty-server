package com.yz;

import com.yz.junit5.Calculator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CalculatorTest {

    Calculator calculator;

    @BeforeEach
    void set() {
        this.calculator = new Calculator();
    }

    @Test
    @DisplayName("Simple multiplication should work")
    void testMultiply() {
        assertEquals(20, calculator.multiply(4, 5), "Regular multiplication should work");
    }

    @RepeatedTest(5)
    @DisplayName(("Ensure correct handling of zero"))
    void testMultiplyWithZero() {
        assertEquals(0, calculator.multiply(0, 0), "Multiple with zero should be zero");
        assertEquals(0, calculator.multiply(5, 0), "Multiple with zero should be zero");
    }

    @Test
    void exceptionTesting() {
        Throwable exception = assertThrows(ArithmeticException.class, () -> calculator.divide(1, 0));
        assertEquals("/ by zero", exception.getMessage());
    }

    @Test
    void testGroupedAssertions() {
        InetSocketAddress address = new InetSocketAddress("0.0.0.0", 8080);
        assertAll("ip address", () -> assertEquals("0.0.0.0", address.getHostName()), () -> assertEquals(8080, address.getPort()));
    }

    // Defining timeouts in your tests.
    @Test
    void timeoutNotExceeded() {
        assertTimeout(Duration.ofMinutes(1), () -> calculator.add(1, 2));
    }

    @Test
    void timeoutNotExceededWithResult() {
        int actualResult = assertTimeout(Duration.ofMinutes(1), () -> calculator.add(1, 2));
        assertEquals(actualResult, 3);

        int ar = assertTimeoutPreemptively(Duration.ofSeconds(1), () -> calculator.add(1, 2));
        assertEquals(ar, 3);
    }

    @RepeatedTest(5)
    @DisplayName("Ensure correct handling of negative")
    @DisabledOnOs(OS.LINUX)
    void testMultiplyWithNegative() {
        // this disable tests on linux system.
//        Assumptions.assumeFalse(System.getProperty("os.name").contains("Linux"));
        assertTrue(calculator.multiply(1, -5) < 0, "Multiple positive with negative should be negative");
    }

    // Dynamic Tests
    @TestFactory
    Stream<DynamicTest> testDifferentMultiplyOperations() {
        Calculator cal = new Calculator();
        int[][] data = new int[][]{{1, 2, 2}, {3, 4, 12}, {121, 4, 484}};
        return Arrays.stream(data).map(entry -> {
            int m1 = entry[0];
            int m2 = entry[1];
            int expected = entry[2];
            return DynamicTest.dynamicTest(m1 + " * " + m2 + " = " + expected, () -> {
                assertEquals(expected, cal.multiply(m1, m2));
            });
        });
    }

    public static int[][] data() {
        return new int[][]{{1, 2, 2}, {3, 4, 12}, {121, 4, 484}};
    }

    @ParameterizedTest
    @MethodSource(value = "data")
    void testWithStringParameter(int[] data) {
        int m1 = data[0];
        int m2 = data[1];
        int expected = data[2];
        assertEquals(expected, calculator.multiply(m1, m2));
    }

    @Test
    void add() {
        assertEquals(3, calculator.add(1, 2), "Addition should work");
    }

    @Test
    void subtract() {
        assertEquals(-1, calculator.subtract(1, 2), "Subtract should work");
    }

    @Fast
    @Test
    void multiply() {
        assertEquals(2, calculator.multiply(1, 2), "Multiplication should work");
    }

    @Test
    void divide() {
        assertEquals(2, calculator.divide(4, 2), "Divide should work");
        Throwable t = assertThrowsExactly(ArithmeticException.class, () -> {
            calculator.divide(1, 0);
        });
        assertEquals("/ by zero", t.getMessage());
    }

    static class ToOctalStringArgumentConverter extends SimpleArgumentConverter {
        @Override
        protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
            assertEquals(Integer.class, source.getClass(), "Can only convert from Integers");
            assertEquals(String.class, targetType, "Can only convert to String");
            return Integer.toOctalString((Integer) source);
        }
    }

    // Argument conversion
    @ParameterizedTest
    @ValueSource(ints = {1, 12, 42})
    void testWithExplicitArgumentConversion(@ConvertWith(ToOctalStringArgumentConverter.class) String argument) {
        System.out.println(argument);
        assertNotNull(argument);
    }
}
