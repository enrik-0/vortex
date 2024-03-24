package vortex.properties;

import org.junit.jupiter.api.Test;

import vortex.utils.MappingUtils;

class DateTest 
{
	@Test
    void DateFormatTest() { 
		String[] dateFormats = {
				"dd-mm-yyyy", "mm-dd-yyyy",
				"yyyy-mm-dd", "yyyy-dd-mm", "dd-yyyy-mm", "dd-mm-yy"
		};
		MappingUtils.parseDate("2020/12/02", dateFormats[1], "");
		
    }
          
    
	
}
