package vortex.properties.dataTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import vortex.utils.MappingUtils;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.params.provider.CsvSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
public class MappingTest {


    @Test
    void mapToByteArray() throws Exception {
        String line = "this is a long string with 100 chars";
        byte[] bytes = MappingUtils.writeValueAsBytes(line);
        String parsed = (String) MappingUtils.map(bytes, String.class);
        assertEquals(line, parsed);
    }
    @Test
    void mapToInteger() throws Exception {
    //String into integer by mapping
    String number = "2";
    Integer integer = (Integer) MappingUtils.map(number, Integer.class);
    assertEquals(2, integer.intValue());

    // cast an Integer
    Integer integer2Integer = (Integer) MappingUtils.map(integer, Integer.class);
    assertEquals(2, integer2Integer.intValue());
    //cast into object
    Object numberAsObject = MappingUtils.map(integer, Integer.class);
    assertTrue(numberAsObject.getClass().equals(Integer.class));

    //cast from an Object
    Integer numberFromObject = (Integer) MappingUtils.map(numberAsObject, Integer.class);
    assertEquals(2, numberFromObject.intValue());

    //from InputStream
    InputStream is = new ByteArrayInputStream( number.getBytes() );
    Integer integerFromInput = (Integer) MappingUtils.map(is, Integer.class);
    assertEquals(2,  integerFromInput);

    Integer  integerFromPrimitive =(Integer) MappingUtils.mapToPrimitive(("" + Integer.MIN_VALUE).getBytes(),  "" + Integer.MIN_VALUE);
    assertEquals(Integer.MIN_VALUE,  integerFromPrimitive.intValue());

    Integer  integerFromPrimitiveMax =(Integer) MappingUtils.mapToPrimitive(("" + Integer.MAX_VALUE).getBytes(),  "" + Integer.MAX_VALUE);
    assertEquals(Integer.MAX_VALUE,  integerFromPrimitiveMax.intValue());

    }
 @Test
    void mapToByte() throws Exception {
    //String into integer by mapping
    String number = "2";
    Byte bytes = (Byte) MappingUtils.map(number, Byte.class);
    assertEquals(2, bytes.byteValue());

    // cast an Integer
    Byte byte2byte = (Byte) MappingUtils.map(bytes, Byte.class);
    assertEquals(2, byte2byte.byteValue());
    //cast into object
    Object byteAsObject = MappingUtils.map(bytes, Byte.class);
    assertTrue(byteAsObject.getClass().equals(Byte.class));

    //cast from an Object
    Byte byteFromObject = (Byte) MappingUtils.map(byteAsObject, Byte.class);
    assertEquals(2, byteFromObject.byteValue());

    //from InputStream
    InputStream is = new ByteArrayInputStream(number.getBytes());
    Byte byteFromInput = (Byte) MappingUtils.map(is, Byte.class);
    assertEquals(2,  byteFromInput.byteValue());

    Byte  byteFromPrimitive =(Byte) MappingUtils.mapToPrimitive(("" + Byte.MIN_VALUE).getBytes(),  "" + Byte.MIN_VALUE);
    assertEquals(Byte.MIN_VALUE,  byteFromPrimitive.byteValue());

    Byte  byteFromPrimitiveMax =(Byte) MappingUtils.mapToPrimitive(("" + Byte.MAX_VALUE).getBytes(),  "" + Byte.MAX_VALUE);
    assertEquals(Byte.MAX_VALUE,  byteFromPrimitiveMax.byteValue());
    }

@Test
    void mapToLong() throws Exception {
    //String into integer by mapping
    String number = "100000";
    Long longs = (Long) MappingUtils.map(number, Long.class);
    assertEquals(Long.valueOf(number), longs.longValue());

    // cast an Integer
    Long long2long = (Long) MappingUtils.map(longs, Long.class);
    assertEquals(Long.valueOf(number), long2long.longValue());
    //cast into object
    Object longAsObject = MappingUtils.map(longs, Long.class);
    assertTrue(longAsObject.getClass().equals(Long.class));

    //cast from an Object
    Long longFromObject = (Long) MappingUtils.map(longAsObject, Long.class);
    assertEquals(Long.valueOf(number), longFromObject.longValue());

    //from InputStream
    InputStream is = new ByteArrayInputStream(number.getBytes());
    Long longFromInput = (Long) MappingUtils.map(is, Long.class);
    assertEquals(Long.valueOf(number),  longFromInput.longValue());

    Long  longFromPrimitive =(Long) MappingUtils.mapToPrimitive(("" + Long.MIN_VALUE).getBytes(),  "" + Long.MIN_VALUE);
    assertEquals(Long.MIN_VALUE,  longFromPrimitive.longValue());

    Long  longFromPrimitiveMax =(Long) MappingUtils.mapToPrimitive(("" + Long.MAX_VALUE).getBytes(),  "" + Long.MAX_VALUE);
    assertEquals(Long.MAX_VALUE,  longFromPrimitiveMax.longValue());
    }

@Test
    void mapToFloating() throws Exception {
    //String into integer by mapping
    String number = "12.223";
    Double doubles = (Double) MappingUtils.map(number, Double.class);
    assertEquals(Double.valueOf(number), doubles.doubleValue());

    // cast an Integer
    Double double2double = (Double) MappingUtils.map(doubles, Double.class);
    assertEquals(Double.valueOf(number), double2double.doubleValue());
    //cast into object
    Object doubleAsObject = MappingUtils.map(doubles, Double.class);
    assertTrue(doubleAsObject.getClass().equals(Double.class));

    //cast from an Object
    Double doubleFromObject = (Double) MappingUtils.map(doubleAsObject, Double.class);
    assertEquals(Double.valueOf(number), doubleFromObject.doubleValue());

    //from InputStream
    InputStream is = new ByteArrayInputStream(number.getBytes());
    Double doubleFromInput = (Double) MappingUtils.map(is, Double.class);
    assertEquals(Double.valueOf(number),  doubleFromInput.doubleValue());
    Double  doubleFromPrimitive =(Double) MappingUtils.mapToPrimitive(("" + Double.MIN_VALUE).getBytes(),  "" + Double.MIN_VALUE);
    assertEquals(Double.MIN_VALUE,  doubleFromPrimitive.doubleValue());

    Double  doubleFromPrimitiveMax =(Double) MappingUtils.mapToPrimitive(("" + Double.MAX_VALUE).getBytes(),  "" + Double.MAX_VALUE);
    assertEquals(Double.MAX_VALUE,  doubleFromPrimitiveMax.doubleValue());
}

@ParameterizedTest
@CsvSource({"true", "false"})
void mapToBoolean(String used) throws Exception {
    boolean value = used.equals("true");

    Boolean bool = (Boolean) MappingUtils.map(used, Boolean.class);
    assertEquals(value, bool.booleanValue());

    // cast an Integer
    Boolean bool2bool = (Boolean) MappingUtils.map(bool, Boolean.class);
    assertEquals(value, bool2bool.booleanValue());
    //cast into object
    Object boolAsObject = MappingUtils.map(bool, Boolean.class);
    assertTrue(boolAsObject.getClass().equals(Boolean.class));

    //cast from an Object
    Boolean boolFromObject = (Boolean) MappingUtils.map(boolAsObject, Boolean.class);
    assertEquals(value, boolFromObject.booleanValue());

    //from InputStream
    InputStream is = new ByteArrayInputStream(used.getBytes());
    Boolean boolFromInput = (Boolean) MappingUtils.map(is, Boolean.class);
    assertEquals(value,  boolFromInput.booleanValue());
    Boolean  boolFromPrimitive = (Boolean) MappingUtils.mapToPrimitive(used.getBytes(), used);
    assertEquals(value,  boolFromPrimitive.booleanValue());
}


@Test
void mapToString() throws Exception {
    String value = "this is a test string";
    // cast an Integer
    String str2str = (String) MappingUtils.map(value, String.class);
    assertEquals(value, str2str);
    //cast into object
    Object strAsObject = MappingUtils.map(value, String.class);
    assertTrue(strAsObject.getClass().equals(String.class));

    //cast from an Object
    String strFromObject = (String) MappingUtils.map(strAsObject, String.class);
    assertEquals(value, strFromObject);

    //from InputStream
    InputStream is = new ByteArrayInputStream(value.getBytes());
    String strFromInput = (String) MappingUtils.map(is, String.class);
    assertEquals(value,  strFromInput);
    String  strFromPrimitive = (String) MappingUtils.mapToPrimitive(value.getBytes(), value);
    assertEquals(value,  strFromPrimitive);
}

@Test
void readInputStream(){
    String text = "this is a sample text";
    InputStream is = new ByteArrayInputStream(text.getBytes());
    assertEquals(text, MappingUtils.getInputContent(is));
}





}
