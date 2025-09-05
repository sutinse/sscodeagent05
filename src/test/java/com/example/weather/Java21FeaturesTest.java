package com.example.weather;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class to verify Java 21 features work correctly in the development environment.
 * This validates that the devcontainer setup provides the correct Java version.
 */
public class Java21FeaturesTest {

    @Test
    public void testJavaVersionIs21() {
        String javaVersion = System.getProperty("java.version");
        assertTrue(javaVersion.startsWith("21"), 
            "Expected Java 21, but found version: " + javaVersion);
    }

    @Test
    public void testTextBlocks() {
        // Text blocks were introduced in Java 15, but are a good feature to test
        String json = """
            {
              "name": "Java 21 Test",
              "version": "21",
              "features": ["text-blocks", "pattern-matching", "records"]
            }
            """;
        
        assertNotNull(json);
        assertTrue(json.contains("Java 21 Test"));
        assertTrue(json.contains("\"version\": \"21\""));
    }

    @Test
    public void testRecords() {
        // Records were finalized in Java 17, available in Java 21
        record TestRecord(String name, int value) {}
        
        TestRecord record = new TestRecord("test", 42);
        assertEquals("test", record.name());
        assertEquals(42, record.value());
    }

    @Test
    public void testSwitchExpressions() {
        // Switch expressions are available in Java 21
        String dayType = switch (1) {
            case 1, 2, 3, 4, 5 -> "weekday";
            case 6, 7 -> "weekend";
            default -> "unknown";
        };
        
        assertEquals("weekday", dayType);
    }
}