package cube;

import org.junit.Test;
import proto.HyperCube;
import proto.Raft;
import util.Config;

import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.*;

public class ConfigTest {

    @Test
    public void parseConfigTest() throws IOException {
        HyperCube.Config config = Config.parseConfig("HyperConfig1.json");

        assertEquals(2,config.getPeerCount());
    }
}
