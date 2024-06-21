package vortex.properties;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import vortex.utils.Asserttions;

import java.util.ArrayList;
import java.util.function.Supplier;

class AsserttionsTest {

    @Test
    void testIsTrue() {
        // Should not throw an exception
        Asserttions.isTrue(true, () -> "This should not fail");

        // Should throw an exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            Asserttions.isTrue(false, () -> "This should fail")
        );
        assertEquals("This should fail", exception.getMessage());
    }

    @Test
    void testIsFalse() {
        // Should not throw an exception
        Asserttions.isFalse(false, () -> "This should not fail");

        // Should throw an exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> 
            Asserttions.isFalse(true, () -> "This should fail")
        );
        assertEquals("This should fail", exception.getMessage());
    }

    @Test
    void testNullSafeGet() {
        Supplier<String> nonNullSupplier = () -> "Non-null message";
        Supplier<String> nullSupplier = null;

        assertEquals("Non-null message", Asserttions.nullSafeGet(nonNullSupplier));
        assertNull(Asserttions.nullSafeGet(nullSupplier));
    }

    @Test
    void testInRangeLong() {
        assertTrue(Asserttions.inRange(5L, 10L, 1L));
        assertFalse(Asserttions.inRange(0L, 10L, 1L));
    }

    @Test
    void testInRangeInt() {
        assertTrue(Asserttions.inRange(5, 10, 1));
        assertFalse(Asserttions.inRange(0, 10, 1));
    }

    @Test
    void testInRangeByte() {
        assertTrue(Asserttions.inRange((byte) 5, (byte) 10, (byte) 1));
        assertFalse(Asserttions.inRange((byte) 0, (byte) 10, (byte) 1));
    }

    @Test
    void testIsPrimitive() {
        assertTrue(Asserttions.isPrimitive(1));
        assertTrue(Asserttions.isPrimitive("string"));
        assertFalse(Asserttions.isPrimitive(new Object()));
    }

    @Test
    void testIsList() {
        assertTrue(Asserttions.isList(new ArrayList<>()));
        assertFalse(Asserttions.isList(new Object()));
    }
}
