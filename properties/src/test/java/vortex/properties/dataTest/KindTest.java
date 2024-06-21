package vortex.properties.dataTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import vortex.properties.filemanager.FileReader;
import vortex.properties.kinds.Server;

public class KindTest {

    @BeforeAll
    static void setUp() throws IOException {
	FileReader.readPropertyFile("default-test.properties");
    }
    @Test
    void kindTest() {
	assertEquals(Server.PORT.value(),Byte.valueOf((byte) 23));
    }
}
