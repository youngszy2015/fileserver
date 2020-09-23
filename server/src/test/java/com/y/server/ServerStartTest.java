package com.y.server;

import com.y.common.exception.CommonException;
import com.y.server.exception.ServerException;
import org.junit.Test;

public class ServerStartTest {


    @Test
    public void start() throws ServerException, CommonException {
        Server.init();
    }


}
