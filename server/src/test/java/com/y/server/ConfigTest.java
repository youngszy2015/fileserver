package com.y.server;

import com.y.server.config.Config;
import com.y.server.exception.ServerException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigTest {
    private static final Logger log = LoggerFactory.getLogger(ConfigTest.class);

    @Test
    public void testConfig() throws ServerException {
        System.out.println(1024 * 1024 * 10);
        Config config = new Config();
        config.parse();
        log.info(config.toString());
    }
}
