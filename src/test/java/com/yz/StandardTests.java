package com.yz;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import com.yz.junit5.Calculator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

@DisplayName("A special test case")
public class StandardTests {

    Calculator calculator;

    static class Person {
        private String firstName;
        private String lastName;

        public Person(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }
    }

    private final Person person = new Person("Jane", "Doe");

    @BeforeAll
    static void initAll() {
    }

    @BeforeEach
    void init() {
        calculator = new Calculator();
    }

    @Test
    @DisplayName("Custom test name containing spaces")
    void succeedingTest() {
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    class A_year_is_not_supported {
        @Test
        void if_it_is_zero() {
        }
    }

    @Nested
    @IndicativeSentencesGeneration(separator = "->", generator = DisplayNameGenerator.ReplaceUnderscores.class)
    class A_year_is_a_leap_year {
        @Test
        void if_it_is_divisible_by_4_but_not_by_100() {
        }

        @ParameterizedTest(name = "Year {0} is a leap year.")
        @ValueSource(ints = {2020, 2016, 2048})
        void if_it_is_one_of_the_following_years(int year) {

        }
    }

    @Test
    @DisplayName("Failing test \uD83D\uDE31")
    @Disabled("just a test for failing using.")
    void failingTest() {
        fail("a failing test");
    }

    @Test
    @Disabled("for demonstration purposes")
    @EnabledIf("isSystem")
    void skippedTest() {
    }

    boolean isSystem() {
        String s = System.getProperty("JAVA_HOME");
        return s != null && !s.isEmpty();
    }

    @Test
    void abortedTest() {
        assumeTrue("abc".contains("Z"));
        fail("test should have been aborted");
    }


    @Test
    void standardAssertions() {
        assertEquals(2, calculator.add(1, 1));
        assertEquals(4, calculator.multiply(2, 2), "The optional failure message is now the last parameter.");
        assertTrue(true, "Assertion messages can be lazily evaluated to avoid constructing complex messages unnecessarily");
    }

    @Test
    void groupedAssertions() {
        assertAll("person", () -> assertEquals("Jane", person.getFirstName()), () -> assertEquals("Doe", person.getLastName()));
    }

    @Test
    void dependentAssertions() {
        assertAll("strings", () -> {
            String firstName = person.getFirstName();
            assertNotNull(firstName);

            // Executed only if the previous assertion is valid.
            assertAll("first name", () -> assertTrue(firstName.startsWith("J")), () -> assertTrue(firstName.endsWith("e")));
        }, () -> {
            // Grouped assertion, so processed independently
            // of results of first name assertions.
            String lastName = person.getLastName();
            assertNotNull(lastName);

            // Executed only if the previous assertion is valid.
            assertAll("last name", () -> assertTrue(lastName.startsWith("D")), () -> assertTrue(lastName.endsWith("e")));
        });
    }

    @Disabled("disabled until your task has been resolved when timeout")
    @Test
    void timeoutExceededWithPreemptiveTermination() {
        assertTimeoutPreemptively(Duration.ofMillis(10), () -> {
            // Simulate task that takes more than 10ms
            new CountDownLatch(1).await();
        }, "your task has been executed timeout");
    }

    @AfterEach
    void teardown() {
    }

    @AfterAll
    static void teardownAll() {
    }
}
