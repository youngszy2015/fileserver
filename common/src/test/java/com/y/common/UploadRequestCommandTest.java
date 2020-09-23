package com.y.common;

import com.y.common.codec.upload.UploadRequestCommandDecoder;
import com.y.common.codec.upload.UploadRequestCommandEncoder;
import com.y.common.command.upload.UploadRequestCommand;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * @Author: Young
 * @DATE: 2020/9/21 21:47
 */
public class UploadRequestCommandTest {

    @Test
    public void testEncoder() {
        byte[] file = "c".getBytes(StandardCharsets.UTF_8);

        UploadRequestCommand requestCommand = new UploadRequestCommand("szy", "txt", file);
        UploadRequestCommandEncoder encoder = new UploadRequestCommandEncoder();
        byte[] encoder1 = encoder.encoder(requestCommand);

        UploadRequestCommandDecoder decoder = new UploadRequestCommandDecoder();
        UploadRequestCommand requestCommand1 = decoder.decoder(encoder1);
        System.out.println(requestCommand1);

    }


}
