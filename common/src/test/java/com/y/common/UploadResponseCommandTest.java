package com.y.common;

import com.y.common.codec.upload.UpLoadResponseCommandDecoder;
import com.y.common.codec.upload.UploadResponseCommandEncoder;
import com.y.common.command.upload.UploadResponseCommand;
import org.junit.Assert;
import org.junit.Test;

/**
 * @Author: Young
 * @DATE: 2020/9/21 23:12
 */
public class UploadResponseCommandTest {

    @Test
    public void test() {
        UploadResponseCommand uploadResponseCommand = new UploadResponseCommand(0,"xxxxxxxxxxxxxxxx");

        UploadResponseCommandEncoder encoder = new UploadResponseCommandEncoder();
        byte[] encoder1 = encoder.encoder(uploadResponseCommand);

        UpLoadResponseCommandDecoder decoder = new UpLoadResponseCommandDecoder();
        UploadResponseCommand cmd = decoder.decoder(encoder1);

        Assert.assertEquals((short) 1, cmd.getType().getValue());
        Assert.assertEquals(1, cmd.getStatus());
        Assert.assertEquals("xxxxxxxxxxxxxxxx", cmd.getFileId());
    }
}
