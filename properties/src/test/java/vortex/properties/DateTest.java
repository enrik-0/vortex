package vortex.properties;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import vortex.utils.MappingUtils;

class DateTest {

    @Test
    void testValidDateParsing() {
        String dateToParse = "2022-01-01";
        String datePattern = "yyyy-MM-dd";
        String parsePattern = "dd/MM/yyyy";

        String parsedDate = MappingUtils.parseDate(dateToParse, datePattern, parsePattern);

        assertEquals("01/01/2022", parsedDate);
    }

    @Test
    void testInvalidDateFormat() {
        String dateToParse = "2022/01/01";
        String datePattern = "yyyy-MM-dd";
        String parsePattern = "dd/MM/yyyy";

        String parsedDate = MappingUtils.parseDate(dateToParse, datePattern, parsePattern);

        assertNull(parsedDate);
    }

    @Test
    void testInvalidParsePattern() {
        String dateToParse = "2022-01-01";
        String datePattern = "yyyy-MM-dd";
        String parsePattern = "invalidPattern"; 

        String parsedDate = MappingUtils.parseDate(dateToParse, datePattern, parsePattern);

        assertNull(parsedDate);
    }
    }
