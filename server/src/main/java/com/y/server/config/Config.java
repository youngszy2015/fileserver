package com.y.server.config;


import com.y.server.exception.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final Logger log = LoggerFactory.getLogger(Config.class);

    private int port;

    private long maxFileSize;

    private static String fileDir;

    public void parse() throws ServerException {
        try {
            Properties properties = new Properties();
            InputStream resourceAsStream = Config.class.getClassLoader().getResourceAsStream("server.properties");
            if (null == resourceAsStream) {
                throw new ServerException("server.properties is not exists");
            }
            properties.load(resourceAsStream);
            this.port = Integer.parseInt(properties.getOrDefault("port", 8089).toString());
            this.maxFileSize = Long.parseLong(properties.getOrDefault("maxFileSize", 1024 * 1024 * 10).toString());
            fileDir = properties.getOrDefault("fileDir", "/data/fileserver").toString();
        } catch (Exception e) {
            throw new ServerException("read config: server.properties err", e);
        }
        log.info("read config: " + this);

    }

    public static String getFileDir() {
        return fileDir;
    }


    @Override
    public String toString() {
        return "Config{" +
                "port=" + port +
                ", maxFileSize=" + maxFileSize +
                ", fileDir='" + fileDir + '\'' +
                '}';
    }


    public int getPort() {
        return port;
    }

    public long getMaxFileSize() {
        return maxFileSize;
    }

}
