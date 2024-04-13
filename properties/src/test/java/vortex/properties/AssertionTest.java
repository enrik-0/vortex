package vortex.properties;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import vortex.utils.Asserttions;

/**
 * AssertionTest
 */
public class AssertionTest {

    @Test
    void booleanTest(){
        try{
        Asserttions.isTrue(true, () -> "this cant be thrown");
        assertTrue(true);
        }catch(IllegalArgumentException e){
        assertTrue(false);
        }
         try{
        Asserttions.isTrue(false, () -> "this must be thrown");
        assertTrue(false);
        }catch(IllegalArgumentException e){
        assertTrue(true);
        }              
        try{

        Asserttions.isFalse(false, () -> "this cant be thrown");
        assertTrue(true);
        }catch(IllegalArgumentException e){
            assertTrue(false);
        }

        try{
        Asserttions.isFalse(true, () -> "this must be thrown");
        assertTrue(false);
        }catch(IllegalArgumentException e){
            assertTrue(true);
        }
}

@Test
void inRangeTest(){
    assertTrue(Asserttions.inRange(13, 20, 10));
    assertFalse(Asserttions.inRange(13, 12, 10));
}
@Test
void isPrimitiveTest(){
    assertTrue(Asserttions.isPrimitive("this is a String"));
    assertTrue(Asserttions.isPrimitive(Character.valueOf('c')));
    assertTrue(Asserttions.isPrimitive(Byte.valueOf("2")));
    assertTrue(Asserttions.isPrimitive(Integer.valueOf(12)));
    assertTrue(Asserttions.isPrimitive(Long.valueOf(12)));
    assertTrue(Asserttions.isPrimitive(Double.valueOf(12.3)));
    assertTrue(Asserttions.isPrimitive(Double.valueOf(12.3)));
    assertTrue(Asserttions.isPrimitive(Float.valueOf("12.3")));
    assertTrue(Asserttions.isPrimitive(Short.valueOf("1")));
    assertFalse(Asserttions.isPrimitive(new AssertionTest()));
    
}
}
