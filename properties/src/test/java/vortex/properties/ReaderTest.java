package vortex.properties;


import org.junit.jupiter.api.Test;

import vortex.properties.filemanager.FileReader;
/**
 * ReaderTest
 */
public class ReaderTest {

    @Test
    void readerTest() throws Exception {
        FileReader.readPropertyFile("application-dev.properties");
    }
    
}

